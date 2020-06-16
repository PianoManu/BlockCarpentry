package mod.pianomanu.blockcarpentry.datagen;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.datagen.provider.BCLootTables;
import mod.pianomanu.blockcarpentry.datagen.provider.BCRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(modid = BlockCarpentryMain.MOD_ID,bus = Bus.MOD)
public class BCDataGenerator {

    @SubscribeEvent
    public static void data(GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            //generator.addProvider(new WoodsLootTableProvider(generator));
            generator.addProvider(new BCRecipeProvider(generator));
            generator.addProvider(new BCLootTables(generator));
        }
    }
}
