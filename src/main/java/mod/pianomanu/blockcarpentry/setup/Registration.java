package mod.pianomanu.blockcarpentry.setup;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.block.*;
import mod.pianomanu.blockcarpentry.tileentity.BedFrameTile;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.block.Block;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Just a normal registering class. See Forge-Documentation on how to register objects
 */
@Mod.EventBusSubscriber(modid = BlockCarpentryMain.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("all") //only warning: datafixer for build()-method is null, but method is annotated as "NotNull"
public class Registration {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BlockCarpentryMain.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BlockCarpentryMain.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BlockCarpentryMain.MOD_ID);
    //private static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, BlockCarpentryMain.MOD_ID);
    //private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, BlockCarpentryMain.MOD_ID);
    //private static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, BlockCarpentryMain.MOD_ID);
    private static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        LOGGER.info("Registering blocks from BlockCarpentry");
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registering items from BlockCarpentry");
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registering tiles from BlockCarpentry");
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registering containers from BlockCarpentry");
        //CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        //ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        //DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


    public static final RegistryObject<FrameBlock> FRAMEBLOCK = BLOCKS.register("frameblock", () -> new FrameBlock(Block.Properties.create(Material.WOOD)
            .sound(SoundType.WOOD)
            .hardnessAndResistance(0.4f)
            .harvestTool(ToolType.AXE)
            .harvestLevel(0)
            .variableOpacity()
            .notSolid()));
    public static final RegistryObject<Item> FRAMEBLOCK_ITEM = ITEMS.register("frameblock", () -> new BlockItem(FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> FRAMEBLOCK_TILE = TILES.register("frameblock", () -> TileEntityType.Builder.create(FrameBlockTile::new, FRAMEBLOCK.get()).build(null));

    //TODO think about translucent blocks and baked models
    //public static final RegistryObject<SlabFrameBlock> TRANSPARENT_FRAMEBLOCK = BLOCKS.register("frame_slab", () -> new SlabFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    //public static final RegistryObject<Item> TRANSPARENT_FRAMEBLOCK_ITEM = ITEMS.register("frame_slab", () -> new BlockItem(TRANSPARENT_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    //public static final RegistryObject<TileEntityType<FrameBlockTile>> TRANSPARENT_FRAMEBLOCK_TILE = TILES.register("frame_slab", () -> TileEntityType.Builder.create(FrameBlockTile::new, TRANSPARENT_FRAMEBLOCK.get()).build(null));

    //fixme or deleteme
    //public static final RegistryObject<FallingFrameBlock> FALLING_FRAMEBLOCK = BLOCKS.register("falling_frameblock", () -> new FallingFrameBlock(Block.Properties.from(FRAMEBLOCK.get()).notSolid()));
    //public static final RegistryObject<Item> FALLING_FRAMEBLOCK_ITEM = ITEMS.register("falling_frameblock", () -> new BlockItem(FALLING_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    //public static final RegistryObject<TileEntityType<FrameBlockTile>> FALLING_FRAMEBLOCK_TILE = TILES.register("falling_frameblock", () -> TileEntityType.Builder.create(FrameBlockTile::new, FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<SlabFrameBlock> SLAB_FRAMEBLOCK = BLOCKS.register("frame_slab", () -> new SlabFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> SLAB_FRAME_ITEM = ITEMS.register("frame_slab", () -> new BlockItem(SLAB_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> SLAB_FRAME_TILE = TILES.register("frame_slab", () -> TileEntityType.Builder.create(FrameBlockTile::new, SLAB_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<IllusionBlock> ILLUSION_BLOCK = BLOCKS.register("illusion_block", () -> new IllusionBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> ILLUSION_BLOCK_ITEM = ITEMS.register("illusion_block", () -> new BlockItem(ILLUSION_BLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> ILLUSION_BLOCK_TILE = TILES.register("illusion_block", () -> TileEntityType.Builder.create(FrameBlockTile::new, ILLUSION_BLOCK.get()).build(null));

    public static final RegistryObject<ButtonFrameBlock> BUTTON_FRAMEBLOCK = BLOCKS.register("frame_button", () -> new ButtonFrameBlock(Block.Properties.from(FRAMEBLOCK.get()).notSolid().doesNotBlockMovement()));
    public static final RegistryObject<Item> BUTTON_FRAME_ITEM = ITEMS.register("frame_button", () -> new BlockItem(BUTTON_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> BUTTON_FRAME_TILE = TILES.register("frame_button", () -> TileEntityType.Builder.create(FrameBlockTile::new, BUTTON_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<PressurePlateFrameBlock> PRESSURE_PLATE_FRAMEBLOCK = BLOCKS.register("frame_pressure_plate", () -> new PressurePlateFrameBlock(PressurePlateBlock.Sensitivity.EVERYTHING, Block.Properties.from(FRAMEBLOCK.get()).notSolid().doesNotBlockMovement()));
    public static final RegistryObject<Item> PRESSURE_PLATE_FRAME_ITEM = ITEMS.register("frame_pressure_plate", () -> new BlockItem(PRESSURE_PLATE_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> PRESSURE_PLATE_FRAME_TILE = TILES.register("frame_pressure_plate", () -> TileEntityType.Builder.create(FrameBlockTile::new, BUTTON_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<StairsFrameBlock> STAIRS_FRAMEBLOCK = BLOCKS.register("frame_stairs", () -> new StairsFrameBlock(() -> FRAMEBLOCK.get().getDefaultState(), Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> STAIRS_FRAME_ITEM = ITEMS.register("frame_stairs", () -> new BlockItem(STAIRS_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> STAIRS_FRAME_TILE = TILES.register("frame_stairs", () -> TileEntityType.Builder.create(FrameBlockTile::new, STAIRS_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<DoorFrameBlock> DOOR_FRAMEBLOCK = BLOCKS.register("frame_door", () -> new DoorFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> DOOR_FRAME_ITEM = ITEMS.register("frame_door", () -> new BlockItem(DOOR_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> DOOR_FRAME_TILE = TILES.register("frame_door", () -> TileEntityType.Builder.create(FrameBlockTile::new, DOOR_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<TrapdoorFrameBlock> TRAPDOOR_FRAMEBLOCK = BLOCKS.register("frame_trapdoor", () -> new TrapdoorFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> TRAPDOOR_FRAME_ITEM = ITEMS.register("frame_trapdoor", () -> new BlockItem(TRAPDOOR_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> TRAPDOOR_FRAME_TILE = TILES.register("frame_trapdoor", () -> TileEntityType.Builder.create(FrameBlockTile::new, TRAPDOOR_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<FenceFrameBlock> FENCE_FRAMEBLOCK = BLOCKS.register("frame_fence", () -> new FenceFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> FENCE_FRAME_ITEM = ITEMS.register("frame_fence", () -> new BlockItem(FENCE_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> FENCE_FRAME_TILE = TILES.register("frame_fence", () -> TileEntityType.Builder.create(FrameBlockTile::new, FENCE_FRAMEBLOCK.get()).build(null));

    //public static final RegistryObject<BedFrameBlock> BED_FRAMEBLOCK = BLOCKS.register("frame_bed", () -> new BedFrameBlock(DyeColor.BROWN, Block.Properties.from(FRAMEBLOCK.get()).notSolid().doesNotBlockMovement()));
    //public static final RegistryObject<Item> BED_FRAME_ITEM = ITEMS.register("frame_bed", () -> new BlockItem(BED_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    //public static final RegistryObject<TileEntityType<BedFrameTile>> BED_FRAME_TILE = TILES.register("frame_bed", () -> TileEntityType.Builder.create(BedFrameTile::new, BED_FRAMEBLOCK.get()).build(null));

    //public static final RegistryObject<Block> SLAB_FRAMEBLOCK = BLOCKS.register("frame_slab", () -> new SlabFrameBlock(Block.Properties.create(Material.WOOD)));

    public static final RegistryObject<Item> HAMMER = ITEMS.register("hammer", () -> new Item(new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY).maxStackSize(1)));
    public static final RegistryObject<Item> TEXTURE_WRENCH = ITEMS.register("texture_wrench", () -> new Item(new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY).maxStackSize(1)));
    public static final RegistryObject<Item> CHISEL = ITEMS.register("chisel", () -> new Item(new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY).maxStackSize(1)));
    public static final RegistryObject<Item> PAINTBRUSH = ITEMS.register("paintbrush", () -> new Item(new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY).maxStackSize(1)));

}
