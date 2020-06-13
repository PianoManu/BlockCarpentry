package mod.pianomanu.blockcarpentry.setup;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.block.*;
import mod.pianomanu.blockcarpentry.tileentity.ButtonFrameBlockTile;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.block.Block;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = BlockCarpentryMain.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registration {
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, BlockCarpentryMain.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, BlockCarpentryMain.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, BlockCarpentryMain.MOD_ID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, BlockCarpentryMain.MOD_ID);
    private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, BlockCarpentryMain.MOD_ID);
    private static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, BlockCarpentryMain.MOD_ID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


    public static final RegistryObject<FrameBlock> FRAMEBLOCK = BLOCKS.register("frameblock", () -> new FrameBlock(Block.Properties.create(Material.WOOD)
            .sound(SoundType.WOOD)
            .hardnessAndResistance(0.4f)
            .harvestTool(ToolType.AXE)
            .notSolid()));
    public static final RegistryObject<Item> FRAMEBLOCK_ITEM = ITEMS.register("frameblock", () -> new BlockItem(FRAMEBLOCK.get(), new Item.Properties()));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> FRAMEBLOCK_TILE = TILES.register("frameblock", () -> TileEntityType.Builder.create(FrameBlockTile::new, FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<FallingFrameBlock> FALLING_FRAMEBLOCK = BLOCKS.register("falling_frameblock", () -> new FallingFrameBlock(Block.Properties.from(FRAMEBLOCK.get()).notSolid()));
    public static final RegistryObject<Item> FALLING_FRAMEBLOCK_ITEM = ITEMS.register("falling_frameblock", () -> new BlockItem(FALLING_FRAMEBLOCK.get(), new Item.Properties()));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> FALLING_FRAMEBLOCK_TILE = TILES.register("falling_frameblock", () -> TileEntityType.Builder.create(FrameBlockTile::new, FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<SlabFrameBlock> SLAB_FRAMEBLOCK = BLOCKS.register("frame_slab", () -> new SlabFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> SLAB_FRAME_ITEM = ITEMS.register("frame_slab", () -> new BlockItem(SLAB_FRAMEBLOCK.get(), new Item.Properties()));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> SLAB_FRAME_TILE = TILES.register("frame_slab", () -> TileEntityType.Builder.create(FrameBlockTile::new, SLAB_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<ButtonFrameBlock> BUTTON_FRAMEBLOCK = BLOCKS.register("frame_button", () -> new ButtonFrameBlock(Block.Properties.from(FRAMEBLOCK.get()).notSolid().doesNotBlockMovement()));
    public static final RegistryObject<Item> BUTTON_FRAME_ITEM = ITEMS.register("frame_button", () -> new BlockItem(BUTTON_FRAMEBLOCK.get(), new Item.Properties()));
    public static final RegistryObject<TileEntityType<ButtonFrameBlockTile>> BUTTON_FRAME_TILE = TILES.register("frame_button", () -> TileEntityType.Builder.create(ButtonFrameBlockTile::new, BUTTON_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<PressurePlateFrameBlock> PRESSURE_PLATE_FRAMEBLOCK = BLOCKS.register("frame_pressure_plate", () -> new PressurePlateFrameBlock(PressurePlateBlock.Sensitivity.EVERYTHING, Block.Properties.from(FRAMEBLOCK.get()).notSolid().doesNotBlockMovement()));
    public static final RegistryObject<Item> PRESSURE_PLATE_FRAME_ITEM = ITEMS.register("frame_pressure_plate", () -> new BlockItem(PRESSURE_PLATE_FRAMEBLOCK.get(), new Item.Properties()));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> PRESSURE_PLATE_FRAME_TILE = TILES.register("frame_pressure_plate", () -> TileEntityType.Builder.create(FrameBlockTile::new, BUTTON_FRAMEBLOCK.get()).build(null));

    //public static final RegistryObject<Block> SLAB_FRAMEBLOCK = BLOCKS.register("frame_slab", () -> new SlabFrameBlock(Block.Properties.create(Material.WOOD)));

}
