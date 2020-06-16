package mod.pianomanu.blockcarpentry.datagen.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.functions.CopyNbt;
import net.minecraft.world.storage.loot.functions.SetContents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class BCBaseLootTableProvider extends LootTableProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    // Filled by subclasses
    protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();

    private final DataGenerator generator;

    public BCBaseLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
        this.generator=dataGeneratorIn;
    }

    // Subclasses can override this to fill the 'lootTables' map.
    protected abstract void addTables();

    // Subclasses can call this if they want a standard loot table. Modify this for your own needs
    protected LootTable.Builder createStandardTable(String name, Block block) {
        LootPool.Builder builder = LootPool.builder()
                .name(name)
                .rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(block)
                        //.acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY)) //protected
                        .acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY)
                                .addOperation("inv", "BlockEntityTag.inv", CopyNbt.Action.REPLACE)
                                .addOperation("energy", "BlockEntityTag.energy", CopyNbt.Action.REPLACE))
                        .acceptFunction(SetContents.builder()
                                .addLootEntry(DynamicLootEntry.func_216162_a(new ResourceLocation("minecraft", "contents"))))
                );
        return LootTable.builder().addLootPool(builder);
    }

    protected LootTable.Builder createBlockToItemLootTable(String name, Item item) {
        LootPool.Builder builder = LootPool.builder()
                .name(name)
                .rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(item)
                        //.acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY)) //protected
                        .acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY)
                                .addOperation("inv", "BlockEntityTag.inv", CopyNbt.Action.REPLACE)
                                .addOperation("energy", "BlockEntityTag.energy", CopyNbt.Action.REPLACE))
                        .acceptFunction(SetContents.builder()
                                .addLootEntry(DynamicLootEntry.func_216162_a(new ResourceLocation("minecraft", "contents"))))
                );
        return LootTable.builder().addLootPool(builder);
    }
    //TODO maybe fix LootTable automatic generation
    /*protected static LootTable createFortuneBlockLootTable(Block block, IItemProvider itemProvider) {
        return LootTable.builder() //
                .setParameterSet(LootParameterSets.BLOCK) //
                .addLootPool(LootPool.builder() //
                        .rolls(ConstantRange.of(1)) //
                        .addEntry(ItemLootEntry.builder(block) //
                                .acceptCondition(MatchTool.builder(ItemPredicate.Builder.create() //
                                        .enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1)))) //
                                ).func_216080_a(ItemLootEntry.builder(itemProvider) //
                                        .acceptFunction(ApplyBonus.func_215869_a(Enchantments.FORTUNE)) //
                                        .acceptFunction(ExplosionDecay.func_215863_b()))))
                .build();
    }*/

    @Override
    // Entry point
    public void act(DirectoryCache cache) {
        addTables();

        Map<ResourceLocation, LootTable> tables = new HashMap<>();
        for (Map.Entry<Block, LootTable.Builder> entry : lootTables.entrySet()) {
            tables.put(entry.getKey().getLootTable(), entry.getValue().setParameterSet(LootParameterSets.BLOCK).build());
        }
        writeTables(cache, tables);
    }

    // Actually write out the tables in the output folder
    private void writeTables(DirectoryCache cache, Map<ResourceLocation, LootTable> tables) {
        Path outputFolder = this.generator.getOutputFolder();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try {
                IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path);
            } catch (IOException e) {
                LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "BlockCarpentry LootTables";
    }
}
