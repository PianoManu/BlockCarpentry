package mod.pianomanu.blockcarpentry.setup;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.block.*;
import mod.pianomanu.blockcarpentry.container.ChestFrameContainer;
import mod.pianomanu.blockcarpentry.container.IllusionChestContainer;
import mod.pianomanu.blockcarpentry.item.*;
import mod.pianomanu.blockcarpentry.tileentity.*;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Just a normal registering class. See Forge-Documentation on how to register objects
 *
 * @author PianoManu
 * @version 1.1 05/31/22
 */
@Mod.EventBusSubscriber(modid = BlockCarpentryMain.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("all") //only warning: datafixer for build()-method is null, but method is annotated as "NotNull"
public class Registration {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BlockCarpentryMain.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BlockCarpentryMain.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BlockCarpentryMain.MOD_ID);
    //WHAT THE FACK???
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, BlockCarpentryMain.MOD_ID);
    //private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, BlockCarpentryMain.MOD_ID);
    //private static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, BlockCarpentryMain.MOD_ID);
    private static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, BlockCarpentryMain.MOD_ID);
    private static final Logger LOGGER = LogManager.getLogger();

    public static final RegistryObject<FrameBlock> FRAMEBLOCK = BLOCKS.register("frameblock", () -> new FrameBlock(Block.Properties.of(Material.WOOD)
            .sound(SoundType.WOOD)
            .strength(0.4f)
            .noOcclusion()));
    public static final RegistryObject<Item> FRAMEBLOCK_ITEM = ITEMS.register("frameblock", () -> new BaseFrameItem(FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> FRAMEBLOCK_TILE = TILES.register("frameblock", () -> BlockEntityType.Builder.of(FrameBlockTile::new, FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<FrameBlock> ILLUSION_BLOCK = BLOCKS.register("illusion_block", () -> new FrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> ILLUSION_BLOCK_ITEM = ITEMS.register("illusion_block", () -> new BaseIllusionItem(ILLUSION_BLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> ILLUSION_BLOCK_TILE = TILES.register("illusion_block", () -> BlockEntityType.Builder.of(FrameBlockTile::new, ILLUSION_BLOCK.get()).build(null));


    public static final RegistryObject<SixWaySlabFrameBlock> SLAB_FRAMEBLOCK = BLOCKS.register("frame_slab", () -> new SixWaySlabFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> SLAB_FRAME_ITEM = ITEMS.register("frame_slab", () -> new BaseFrameItem(SLAB_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<TwoBlocksFrameBlockTile>> SLAB_FRAME_TILE = TILES.register("frame_slab", () -> BlockEntityType.Builder.of(TwoBlocksFrameBlockTile::new, SLAB_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<SixWaySlabFrameBlock> SLAB_ILLUSIONBLOCK = BLOCKS.register("illusion_slab", () -> new SixWaySlabFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> SLAB_ILLUSION_ITEM = ITEMS.register("illusion_slab", () -> new BaseIllusionItem(SLAB_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> SLAB_ILLUSION_TILE = TILES.register("illusion_slab", () -> BlockEntityType.Builder.of(FrameBlockTile::new, SLAB_FRAMEBLOCK.get()).build(null));


    public static final RegistryObject<StairsFrameBlock> STAIRS_FRAMEBLOCK = BLOCKS.register("frame_stairs", () -> new StairsFrameBlock(() -> FRAMEBLOCK.get().defaultBlockState(), Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> STAIRS_FRAME_ITEM = ITEMS.register("frame_stairs", () -> new BaseFrameItem(STAIRS_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> STAIRS_FRAME_TILE = TILES.register("frame_stairs", () -> BlockEntityType.Builder.of(FrameBlockTile::new, STAIRS_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<StairsFrameBlock> STAIRS_ILLUSIONBLOCK = BLOCKS.register("illusion_stairs", () -> new StairsFrameBlock(() -> FRAMEBLOCK.get().defaultBlockState(), Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> STAIRS_ILLUSION_ITEM = ITEMS.register("illusion_stairs", () -> new BaseIllusionItem(STAIRS_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> STAIRS_ILLUSION_TILE = TILES.register("illusion_stairs", () -> BlockEntityType.Builder.of(FrameBlockTile::new, STAIRS_FRAMEBLOCK.get()).build(null));


    public static final RegistryObject<ButtonFrameBlock> BUTTON_FRAMEBLOCK = BLOCKS.register("frame_button", () -> new ButtonFrameBlock(Block.Properties.copy(FRAMEBLOCK.get()).noCollission()));
    public static final RegistryObject<Item> BUTTON_FRAME_ITEM = ITEMS.register("frame_button", () -> new BaseFrameItem(BUTTON_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> BUTTON_FRAME_TILE = TILES.register("frame_button", () -> BlockEntityType.Builder.of(FrameBlockTile::new, BUTTON_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<ButtonFrameBlock> BUTTON_ILLUSIONBLOCK = BLOCKS.register("illusion_button", () -> new ButtonFrameBlock(Block.Properties.copy(FRAMEBLOCK.get()).noCollission()));
    public static final RegistryObject<Item> BUTTON_ILLUSION_ITEM = ITEMS.register("illusion_button", () -> new BaseIllusionItem(BUTTON_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> BUTTON_ILLUSION_TILE = TILES.register("illusion_button", () -> BlockEntityType.Builder.of(FrameBlockTile::new, BUTTON_FRAMEBLOCK.get()).build(null));


    public static final RegistryObject<PressurePlateFrameBlock> PRESSURE_PLATE_FRAMEBLOCK = BLOCKS.register("frame_pressure_plate", () -> new PressurePlateFrameBlock(PressurePlateBlock.Sensitivity.EVERYTHING, Block.Properties.copy(FRAMEBLOCK.get()).noCollission()));
    public static final RegistryObject<Item> PRESSURE_PLATE_FRAME_ITEM = ITEMS.register("frame_pressure_plate", () -> new BaseFrameItem(PRESSURE_PLATE_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> PRESSURE_PLATE_FRAME_TILE = TILES.register("frame_pressure_plate", () -> BlockEntityType.Builder.of(FrameBlockTile::new, BUTTON_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<PressurePlateFrameBlock> PRESSURE_PLATE_ILLUSIONBLOCK = BLOCKS.register("illusion_pressure_plate", () -> new PressurePlateFrameBlock(PressurePlateBlock.Sensitivity.EVERYTHING, Block.Properties.copy(FRAMEBLOCK.get()).noCollission()));
    public static final RegistryObject<Item> PRESSURE_PLATE_ILLUSION_ITEM = ITEMS.register("illusion_pressure_plate", () -> new BaseIllusionItem(PRESSURE_PLATE_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> PRESSURE_PLATE_ILLUSION_TILE = TILES.register("illusion_pressure_plate", () -> BlockEntityType.Builder.of(FrameBlockTile::new, BUTTON_FRAMEBLOCK.get()).build(null));


    public static final RegistryObject<DoorFrameBlock> DOOR_FRAMEBLOCK = BLOCKS.register("frame_door", () -> new DoorFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> DOOR_FRAME_ITEM = ITEMS.register("frame_door", () -> new BaseFrameItem(DOOR_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<LockableFrameTile>> DOOR_FRAME_TILE = TILES.register("frame_door", () -> BlockEntityType.Builder.of(LockableFrameTile::new, DOOR_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<DoorFrameBlock> DOOR_ILLUSIONBLOCK = BLOCKS.register("illusion_door", () -> new DoorFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> DOOR_ILLUSION_ITEM = ITEMS.register("illusion_door", () -> new BaseIllusionItem(DOOR_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<LockableFrameTile>> DOOR_ILLUSION_TILE = TILES.register("illusion_door", () -> BlockEntityType.Builder.of(LockableFrameTile::new, DOOR_FRAMEBLOCK.get()).build(null));


    public static final RegistryObject<TrapdoorFrameBlock> TRAPDOOR_FRAMEBLOCK = BLOCKS.register("frame_trapdoor", () -> new TrapdoorFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> TRAPDOOR_FRAME_ITEM = ITEMS.register("frame_trapdoor", () -> new BaseFrameItem(TRAPDOOR_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<LockableFrameTile>> TRAPDOOR_FRAME_TILE = TILES.register("frame_trapdoor", () -> BlockEntityType.Builder.of(LockableFrameTile::new, TRAPDOOR_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<TrapdoorFrameBlock> TRAPDOOR_ILLUSIONBLOCK = BLOCKS.register("illusion_trapdoor", () -> new TrapdoorFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> TRAPDOOR_ILLUSION_ITEM = ITEMS.register("illusion_trapdoor", () -> new BaseIllusionItem(TRAPDOOR_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<LockableFrameTile>> TRAPDOOR_ILLUSION_TILE = TILES.register("illusion_trapdoor", () -> BlockEntityType.Builder.of(LockableFrameTile::new, TRAPDOOR_FRAMEBLOCK.get()).build(null));


    public static final RegistryObject<LadderFrameBlock> LADDER_FRAMEBLOCK = BLOCKS.register("frame_ladder", () -> new LadderFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> LADDER_FRAME_ITEM = ITEMS.register("frame_ladder", () -> new BaseFrameItem(LADDER_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> LADDER_FRAME_TILE = TILES.register("frame_ladder", () -> BlockEntityType.Builder.of(FrameBlockTile::new, LADDER_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<LadderFrameBlock> LADDER_ILLUSIONBLOCK = BLOCKS.register("illusion_ladder", () -> new LadderFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> LADDER_ILLUSION_ITEM = ITEMS.register("illusion_ladder", () -> new BaseIllusionItem(LADDER_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> LADDER_ILLUSION_TILE = TILES.register("illusion_ladder", () -> BlockEntityType.Builder.of(FrameBlockTile::new, LADDER_FRAMEBLOCK.get()).build(null));


    public static final RegistryObject<FenceFrameBlock> FENCE_FRAMEBLOCK = BLOCKS.register("frame_fence", () -> new FenceFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> FENCE_FRAME_ITEM = ITEMS.register("frame_fence", () -> new BaseFrameItem(FENCE_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> FENCE_FRAME_TILE = TILES.register("frame_fence", () -> BlockEntityType.Builder.of(FrameBlockTile::new, FENCE_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<FenceFrameBlock> FENCE_ILLUSIONBLOCK = BLOCKS.register("illusion_fence", () -> new FenceFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> FENCE_ILLUSION_ITEM = ITEMS.register("illusion_fence", () -> new BaseIllusionItem(FENCE_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> FENCE_ILLUSION_TILE = TILES.register("illusion_fence", () -> BlockEntityType.Builder.of(FrameBlockTile::new, FENCE_FRAMEBLOCK.get()).build(null));


    public static final RegistryObject<FenceGateFrameBlock> FENCE_GATE_FRAMEBLOCK = BLOCKS.register("frame_fence_gate", () -> new FenceGateFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> FENCE_GATE_FRAME_ITEM = ITEMS.register("frame_fence_gate", () -> new BaseFrameItem(FENCE_GATE_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<LockableFrameTile>> FENCE_GATE_FRAME_TILE = TILES.register("frame_fence_gate", () -> BlockEntityType.Builder.of(LockableFrameTile::new, FENCE_GATE_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<FenceGateFrameBlock> FENCE_GATE_ILLUSIONBLOCK = BLOCKS.register("illusion_fence_gate", () -> new FenceGateFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> FENCE_GATE_ILLUSION_ITEM = ITEMS.register("illusion_fence_gate", () -> new BaseIllusionItem(FENCE_GATE_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<LockableFrameTile>> FENCE_GATE_ILLUSION_TILE = TILES.register("illusion_fence_gate", () -> BlockEntityType.Builder.of(LockableFrameTile::new, FENCE_GATE_ILLUSIONBLOCK.get()).build(null));


    public static final RegistryObject<WallFrameBlock> WALL_FRAMEBLOCK = BLOCKS.register("frame_wall", () -> new WallFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> WALL_FRAME_ITEM = ITEMS.register("frame_wall", () -> new BaseFrameItem(WALL_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> WALL_FRAME_TILE = TILES.register("frame_wall", () -> BlockEntityType.Builder.of(FrameBlockTile::new, WALL_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<WallFrameBlock> WALL_ILLUSIONBLOCK = BLOCKS.register("illusion_wall", () -> new WallFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> WALL_ILLUSION_ITEM = ITEMS.register("illusion_wall", () -> new BaseIllusionItem(WALL_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> WALL_ILLUSION_TILE = TILES.register("illusion_wall", () -> BlockEntityType.Builder.of(FrameBlockTile::new, WALL_FRAMEBLOCK.get()).build(null));


    public static final RegistryObject<BedFrameBlock> BED_FRAMEBLOCK = BLOCKS.register("frame_bed", () -> new BedFrameBlock(DyeColor.BROWN, Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> BED_FRAME_ITEM = ITEMS.register("frame_bed", () -> new BaseFrameItem(BED_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<BedFrameTile>> BED_FRAME_TILE = TILES.register("frame_bed", () -> BlockEntityType.Builder.of(BedFrameTile::new, BED_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<BedFrameBlock> BED_ILLUSIONBLOCK = BLOCKS.register("illusion_bed", () -> new BedFrameBlock(DyeColor.BROWN, Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> BED_ILLUSION_ITEM = ITEMS.register("illusion_bed", () -> new BaseIllusionItem(BED_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<BedFrameTile>> BED_ILLUSION_TILE = TILES.register("illusion_bed", () -> BlockEntityType.Builder.of(BedFrameTile::new, BED_FRAMEBLOCK.get()).build(null));


    public static final RegistryObject<ChestFrameBlock> CHEST_FRAMEBLOCK = BLOCKS.register("frame_chest", () -> new ChestFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> CHEST_FRAME_ITEM = ITEMS.register("frame_chest", () -> new BaseFrameItem(CHEST_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<ChestFrameBlockEntity>> CHEST_FRAME_TILE = TILES.register("frame_chest", () -> BlockEntityType.Builder.of(ChestFrameBlockEntity::new, CHEST_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<MenuType<ChestFrameContainer>> CHEST_FRAME_CONTAINER = CONTAINERS.register("frame_chest", () -> new MenuType<>(ChestFrameContainer::createFrameContainerMenu));

    public static final RegistryObject<ChestFrameBlock> CHEST_ILLUSIONBLOCK = BLOCKS.register("illusion_chest", () -> new ChestFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> CHEST_ILLUSION_ITEM = ITEMS.register("illusion_chest", () -> new BaseIllusionItem(CHEST_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<ChestFrameBlockEntity>> CHEST_ILLUSION_TILE = TILES.register("illusion_chest", () -> BlockEntityType.Builder.of(ChestFrameBlockEntity::new, CHEST_ILLUSIONBLOCK.get()).build(null));
    public static final RegistryObject<MenuType<IllusionChestContainer>> CHEST_ILLUSION_CONTAINER = CONTAINERS.register("illusion_chest", () -> new MenuType<>(IllusionChestContainer::createIllusionContainerMenu));


    public static final RegistryObject<CarpetFrameBlock> CARPET_FRAMEBLOCK = BLOCKS.register("frame_carpet", () -> new CarpetFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> CARPET_FRAME_ITEM = ITEMS.register("frame_carpet", () -> new BaseFrameItem(CARPET_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> CARPET_FRAME_TILE = TILES.register("frame_carpet", () -> BlockEntityType.Builder.of(FrameBlockTile::new, CARPET_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<CarpetFrameBlock> CARPET_ILLUSIONBLOCK = BLOCKS.register("illusion_carpet", () -> new CarpetFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> CARPET_ILLUSION_ITEM = ITEMS.register("illusion_carpet", () -> new BaseIllusionItem(CARPET_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> CARPET_ILLUSION_TILE = TILES.register("illusion_carpet", () -> BlockEntityType.Builder.of(FrameBlockTile::new, CARPET_ILLUSIONBLOCK.get()).build(null));


    public static final RegistryObject<PaneFrameBlock> PANE_FRAMEBLOCK = BLOCKS.register("frame_pane", () -> new PaneFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> PANE_FRAME_ITEM = ITEMS.register("frame_pane", () -> new BaseFrameItem(PANE_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> PANE_FRAME_TILE = TILES.register("frame_pane", () -> BlockEntityType.Builder.of(FrameBlockTile::new, PANE_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<PaneFrameBlock> PANE_ILLUSIONBLOCK = BLOCKS.register("illusion_pane", () -> new PaneFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> PANE_ILLUSION_ITEM = ITEMS.register("illusion_pane", () -> new BaseIllusionItem(PANE_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> PANE_ILLUSION_TILE = TILES.register("illusion_pane", () -> BlockEntityType.Builder.of(FrameBlockTile::new, PANE_ILLUSIONBLOCK.get()).build(null));


    public static final RegistryObject<DaylightDetectorFrameBlock> DAYLIGHT_DETECTOR_FRAMEBLOCK = BLOCKS.register("frame_daylight_detector", () -> new DaylightDetectorFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> DAYLIGHT_DETECTOR_FRAME_ITEM = ITEMS.register("frame_daylight_detector", () -> new BaseFrameItem(DAYLIGHT_DETECTOR_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<DaylightDetectorFrameTileEntity>> DAYLIGHT_DETECTOR_FRAME_TILE = TILES.register("frame_daylight_detector", () -> BlockEntityType.Builder.of(DaylightDetectorFrameTileEntity::new, DAYLIGHT_DETECTOR_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<DaylightDetectorFrameBlock> DAYLIGHT_DETECTOR_ILLUSIONBLOCK = BLOCKS.register("illusion_daylight_detector", () -> new DaylightDetectorFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> DAYLIGHT_DETECTOR_ILLUSION_ITEM = ITEMS.register("illusion_daylight_detector", () -> new BaseIllusionItem(DAYLIGHT_DETECTOR_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<DaylightDetectorFrameTileEntity>> DAYLIGHT_DETECTOR_ILLUSION_TILE = TILES.register("illusion_daylight_detector", () -> BlockEntityType.Builder.of(DaylightDetectorFrameTileEntity::new, DAYLIGHT_DETECTOR_ILLUSIONBLOCK.get()).build(null));


    public static final RegistryObject<LayeredBlock> LAYERED_FRAMEBLOCK = BLOCKS.register("frame_layered_block", () -> new LayeredBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> LAYERED_FRAME_ITEM = ITEMS.register("frame_layered_block", () -> new BaseFrameItem(LAYERED_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> LAYERED_FRAME_TILE = TILES.register("frame_layered_block", () -> BlockEntityType.Builder.of(FrameBlockTile::new, LAYERED_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<LayeredBlock> LAYERED_ILLUSIONBLOCK = BLOCKS.register("illusion_layered_block", () -> new LayeredBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> LAYERED_ILLUSION_ITEM = ITEMS.register("illusion_layered_block", () -> new BaseIllusionItem(LAYERED_ILLUSIONBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> LAYERED_ILLUSION_TILE = TILES.register("illusion_layered_block", () -> BlockEntityType.Builder.of(FrameBlockTile::new, LAYERED_ILLUSIONBLOCK.get()).build(null));


    //disabled signs - TODO fix signs
    //public static final RegistryObject<StandingSignFrameBlock> STANDING_SIGN_FRAMEBLOCK = BLOCKS.register("standing_frame_sign", () -> new StandingSignFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    //public static final RegistryObject<WallSignFrameBlock> WALL_SIGN_FRAMEBLOCK = BLOCKS.register("wall_frame_sign", () -> new WallSignFrameBlock(Block.Properties.copy(FRAMEBLOCK.get())));
    //public static final RegistryObject<Item> SIGN_FRAME_ITEM = ITEMS.register("frame_sign", () -> new FrameSignItem((new Item.Properties()).maxStackSize(16).tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY), Registration.STANDING_SIGN_FRAMEBLOCK.get(), Registration.WALL_SIGN_FRAMEBLOCK.get()));
    //public static final RegistryObject<BlockEntityType<SignFrameTile>> SIGN_FRAME_TILE = TILES.register("frame_sign", () -> BlockEntityType.Builder.of(SignFrameTile::new, STANDING_SIGN_FRAMEBLOCK.get(), WALL_SIGN_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<SlopeFrameBlock> SLOPE_FRAMEBLOCK = BLOCKS.register("frame_slope", () -> new SlopeFrameBlock(() -> FRAMEBLOCK.get().defaultBlockState(), Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> SLOPE_FRAME_ITEM = ITEMS.register("frame_slope", () -> new BaseFrameItem(SLOPE_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> SLOPE_FRAME_TILE = TILES.register("frame_slope", () -> BlockEntityType.Builder.of(FrameBlockTile::new, SLOPE_FRAMEBLOCK.get()).build(null));


    public static final RegistryObject<SlopeFrameBlock> EDGED_SLOPE_FRAMEBLOCK = BLOCKS.register("frame_edged_slope", () -> new SlopeFrameBlock(() -> FRAMEBLOCK.get().defaultBlockState(), Block.Properties.copy(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> EDGED_SLOPE_FRAME_ITEM = ITEMS.register("frame_edged_slope", () -> new BaseFrameItem(EDGED_SLOPE_FRAMEBLOCK.get(), new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<BlockEntityType<FrameBlockTile>> EDGED_SLOPE_FRAME_TILE = TILES.register("frame_edged_slope", () -> BlockEntityType.Builder.of(FrameBlockTile::new, EDGED_SLOPE_FRAMEBLOCK.get()).build(null));


    public static void init() {
        LOGGER.info("Registering blocks copy BlockCarpentry");
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registering items copy BlockCarpentry");
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registering tiles copy BlockCarpentry");
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registering containers copy BlockCarpentry");
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        //ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        //DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registering points of interest copy Blockcarpentry");
        POI_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


    public static final RegistryObject<HammerItem> HAMMER = ITEMS.register("hammer", () -> new HammerItem(new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY).stacksTo(1)));
    public static final RegistryObject<TextureWrenchItem> TEXTURE_WRENCH = ITEMS.register("texture_wrench", () -> new TextureWrenchItem(new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY).stacksTo(1)));
    public static final RegistryObject<ChiselItem> CHISEL = ITEMS.register("chisel", () -> new ChiselItem(new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY).stacksTo(1)));
    public static final RegistryObject<PaintbrushItem> PAINTBRUSH = ITEMS.register("paintbrush", () -> new PaintbrushItem(new Item.Properties().tab(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY).stacksTo(1)));

}
//========SOLI DEO GLORIA========//