package mod.pianomanu.blockcarpentry.setup;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.block.*;
import mod.pianomanu.blockcarpentry.container.ChestFrameContainer;
import mod.pianomanu.blockcarpentry.container.IllusionChestContainer;
import mod.pianomanu.blockcarpentry.item.*;
import mod.pianomanu.blockcarpentry.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Just a normal registering class. See Forge-Documentation on how to register objects
 *
 * @author PianoManu
 * @version 1.4 10/02/23
 */
@Mod.EventBusSubscriber(modid = BlockCarpentryMain.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("all") //only warning: datafixer for build()-method is null, but method is annotated as "NotNull"
public class Registration {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BlockCarpentryMain.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BlockCarpentryMain.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BlockCarpentryMain.MOD_ID);
    //WHAT THE FACK???
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, BlockCarpentryMain.MOD_ID);
    //private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, BlockCarpentryMain.MOD_ID);
    //private static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, BlockCarpentryMain.MOD_ID);
    private static final DeferredRegister<PointOfInterestType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, BlockCarpentryMain.MOD_ID);
    private static final Logger LOGGER = LogManager.getLogger();

    public static final RegistryObject<FrameBlock> FRAMEBLOCK = BLOCKS.register("frameblock", () -> new FrameBlock(Block.Properties.create(Material.WOOD)
            .sound(SoundType.WOOD)
            .hardnessAndResistance(Blocks.OAK_PLANKS.getExplosionResistance())
            .harvestTool(ToolType.AXE)
            .harvestLevel(0)
            .slipperiness(Blocks.OAK_PLANKS.getSlipperiness())
            .variableOpacity()
            .notSolid()));
    public static final RegistryObject<Item> FRAMEBLOCK_ITEM = ITEMS.register("frameblock", () -> new BaseFrameItem(FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> FRAMEBLOCK_TILE = TILES.register("frameblock", () -> TileEntityType.Builder.create(FrameBlockTile::new, FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<FrameBlock> ILLUSION_BLOCK = BLOCKS.register("illusion_block", () -> new FrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> ILLUSION_BLOCK_ITEM = ITEMS.register("illusion_block", () -> new BaseIllusionItem(ILLUSION_BLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> ILLUSION_BLOCK_TILE = TILES.register("illusion_block", () -> TileEntityType.Builder.create(FrameBlockTile::new, ILLUSION_BLOCK.get()).build(null));


    public static final RegistryObject<SixWaySlabFrameBlock> SLAB_FRAMEBLOCK = BLOCKS.register("frame_slab", () -> new SixWaySlabFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> SLAB_FRAME_ITEM = ITEMS.register("frame_slab", () -> new BaseFrameItem(SLAB_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<TwoBlocksFrameBlockTile>> SLAB_FRAME_TILE = TILES.register("frame_slab", () -> TileEntityType.Builder.create(TwoBlocksFrameBlockTile::new, SLAB_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<SixWaySlabFrameBlock> SLAB_ILLUSIONBLOCK = BLOCKS.register("illusion_slab", () -> new SixWaySlabFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> SLAB_ILLUSION_ITEM = ITEMS.register("illusion_slab", () -> new BaseIllusionItem(SLAB_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> SLAB_ILLUSION_TILE = TILES.register("illusion_slab", () -> TileEntityType.Builder.create(FrameBlockTile::new, SLAB_FRAMEBLOCK.get()).build(null));


    public static final RegistryObject<StairsFrameBlock> STAIRS_FRAMEBLOCK = BLOCKS.register("frame_stairs", () -> new StairsFrameBlock(() -> FRAMEBLOCK.get().getDefaultState(), Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> STAIRS_FRAME_ITEM = ITEMS.register("frame_stairs", () -> new BaseFrameItem(STAIRS_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> STAIRS_FRAME_TILE = TILES.register("frame_stairs", () -> TileEntityType.Builder.create(FrameBlockTile::new, STAIRS_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<StairsFrameBlock> STAIRS_ILLUSIONBLOCK = BLOCKS.register("illusion_stairs", () -> new StairsFrameBlock(() -> FRAMEBLOCK.get().getDefaultState(), Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> STAIRS_ILLUSION_ITEM = ITEMS.register("illusion_stairs", () -> new BaseIllusionItem(STAIRS_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> STAIRS_ILLUSION_TILE = TILES.register("illusion_stairs", () -> TileEntityType.Builder.create(FrameBlockTile::new, STAIRS_FRAMEBLOCK.get()).build(null));


    public static final RegistryObject<ButtonFrameBlock> BUTTON_FRAMEBLOCK = BLOCKS.register("frame_button", () -> new ButtonFrameBlock(Block.Properties.from(FRAMEBLOCK.get()).doesNotBlockMovement()));
    public static final RegistryObject<Item> BUTTON_FRAME_ITEM = ITEMS.register("frame_button", () -> new BaseFrameItem(BUTTON_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> BUTTON_FRAME_TILE = TILES.register("frame_button", () -> TileEntityType.Builder.create(FrameBlockTile::new, BUTTON_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> BUTTON_ILLUSION_TILE = TILES.register("illusion_button", () -> TileEntityType.Builder.create(FrameBlockTile::new, BUTTON_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> PRESSURE_PLATE_FRAME_TILE = TILES.register("frame_pressure_plate", () -> TileEntityType.Builder.create(FrameBlockTile::new, BUTTON_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> PRESSURE_PLATE_ILLUSION_TILE = TILES.register("illusion_pressure_plate", () -> TileEntityType.Builder.create(FrameBlockTile::new, BUTTON_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<ButtonFrameBlock> BUTTON_ILLUSIONBLOCK = BLOCKS.register("illusion_button", () -> new ButtonFrameBlock(Block.Properties.from(FRAMEBLOCK.get()).doesNotBlockMovement()));
    public static final RegistryObject<Item> BUTTON_ILLUSION_ITEM = ITEMS.register("illusion_button", () -> new BaseIllusionItem(BUTTON_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<PressurePlateFrameBlock> PRESSURE_PLATE_FRAMEBLOCK = BLOCKS.register("frame_pressure_plate", () -> new PressurePlateFrameBlock(PressurePlateBlock.Sensitivity.EVERYTHING, Block.Properties.from(FRAMEBLOCK.get()).doesNotBlockMovement()));
    public static final RegistryObject<Item> PRESSURE_PLATE_FRAME_ITEM = ITEMS.register("frame_pressure_plate", () -> new BaseFrameItem(PRESSURE_PLATE_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<PressurePlateFrameBlock> PRESSURE_PLATE_ILLUSIONBLOCK = BLOCKS.register("illusion_pressure_plate", () -> new PressurePlateFrameBlock(PressurePlateBlock.Sensitivity.EVERYTHING, Block.Properties.from(FRAMEBLOCK.get()).doesNotBlockMovement()));
    public static final RegistryObject<Item> PRESSURE_PLATE_ILLUSION_ITEM = ITEMS.register("illusion_pressure_plate", () -> new BaseIllusionItem(PRESSURE_PLATE_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));


    public static final RegistryObject<DoorFrameBlock> DOOR_FRAMEBLOCK = BLOCKS.register("frame_door", () -> new DoorFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> DOOR_FRAME_ITEM = ITEMS.register("frame_door", () -> new BaseFrameItem(DOOR_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<DoorFrameBlock> DOOR_ILLUSIONBLOCK = BLOCKS.register("illusion_door", () -> new DoorFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<TileEntityType<LockableFrameTile>> DOOR_FRAME_TILE = TILES.register("frame_door", () -> TileEntityType.Builder.create(LockableFrameTile::new, DOOR_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<Item> DOOR_ILLUSION_ITEM = ITEMS.register("illusion_door", () -> new BaseIllusionItem(DOOR_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TrapdoorFrameBlock> TRAPDOOR_ILLUSIONBLOCK = BLOCKS.register("illusion_trapdoor", () -> new TrapdoorFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<TileEntityType<LockableFrameTile>> DOOR_ILLUSION_TILE = TILES.register("illusion_door", () -> TileEntityType.Builder.create(LockableFrameTile::new, DOOR_FRAMEBLOCK.get()).build(null));


    public static final RegistryObject<TrapdoorFrameBlock> TRAPDOOR_FRAMEBLOCK = BLOCKS.register("frame_trapdoor", () -> new TrapdoorFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> TRAPDOOR_FRAME_ITEM = ITEMS.register("frame_trapdoor", () -> new BaseFrameItem(TRAPDOOR_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<Item> TRAPDOOR_ILLUSION_ITEM = ITEMS.register("illusion_trapdoor", () -> new BaseIllusionItem(TRAPDOOR_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<LadderFrameBlock> LADDER_FRAMEBLOCK = BLOCKS.register("frame_ladder", () -> new LadderFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<TileEntityType<LockableFrameTile>> TRAPDOOR_FRAME_TILE = TILES.register("frame_trapdoor", () -> TileEntityType.Builder.create(LockableFrameTile::new, TRAPDOOR_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<Item> LADDER_FRAME_ITEM = ITEMS.register("frame_ladder", () -> new BaseFrameItem(LADDER_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> LADDER_FRAME_TILE = TILES.register("frame_ladder", () -> TileEntityType.Builder.create(FrameBlockTile::new, LADDER_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> LADDER_ILLUSION_TILE = TILES.register("illusion_ladder", () -> TileEntityType.Builder.create(FrameBlockTile::new, LADDER_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<LockableFrameTile>> TRAPDOOR_ILLUSION_TILE = TILES.register("illusion_trapdoor", () -> TileEntityType.Builder.create(LockableFrameTile::new, TRAPDOOR_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<LadderFrameBlock> LADDER_ILLUSIONBLOCK = BLOCKS.register("illusion_ladder", () -> new LadderFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> LADDER_ILLUSION_ITEM = ITEMS.register("illusion_ladder", () -> new BaseIllusionItem(LADDER_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<FenceFrameBlock> FENCE_ILLUSIONBLOCK = BLOCKS.register("illusion_fence", () -> new FenceFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> FENCE_ILLUSION_ITEM = ITEMS.register("illusion_fence", () -> new BaseIllusionItem(FENCE_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));


    public static final RegistryObject<FenceFrameBlock> FENCE_FRAMEBLOCK = BLOCKS.register("frame_fence", () -> new FenceFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> FENCE_FRAME_ITEM = ITEMS.register("frame_fence", () -> new BaseFrameItem(FENCE_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> FENCE_ILLUSION_TILE = TILES.register("illusion_fence", () -> TileEntityType.Builder.create(FrameBlockTile::new, FENCE_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<FenceGateFrameBlock> FENCE_GATE_FRAMEBLOCK = BLOCKS.register("frame_fence_gate", () -> new FenceGateFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> FENCE_FRAME_TILE = TILES.register("frame_fence", () -> TileEntityType.Builder.create(FrameBlockTile::new, FENCE_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<Item> FENCE_GATE_FRAME_ITEM = ITEMS.register("frame_fence_gate", () -> new BaseFrameItem(FENCE_GATE_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<FenceGateFrameBlock> FENCE_GATE_ILLUSIONBLOCK = BLOCKS.register("illusion_fence_gate", () -> new FenceGateFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> FENCE_GATE_ILLUSION_ITEM = ITEMS.register("illusion_fence_gate", () -> new BaseIllusionItem(FENCE_GATE_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<WallFrameBlock> WALL_ILLUSIONBLOCK = BLOCKS.register("illusion_wall", () -> new WallFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> WALL_ILLUSION_ITEM = ITEMS.register("illusion_wall", () -> new BaseIllusionItem(WALL_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<LockableFrameTile>> FENCE_GATE_FRAME_TILE = TILES.register("frame_fence_gate", () -> TileEntityType.Builder.create(LockableFrameTile::new, FENCE_GATE_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<BedFrameBlock> BED_FRAMEBLOCK = BLOCKS.register("frame_bed", () -> new BedFrameBlock(DyeColor.BROWN, Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> BED_FRAME_ITEM = ITEMS.register("frame_bed", () -> new BaseFrameItem(BED_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<LockableFrameTile>> FENCE_GATE_ILLUSION_TILE = TILES.register("illusion_fence_gate", () -> TileEntityType.Builder.create(LockableFrameTile::new, FENCE_GATE_ILLUSIONBLOCK.get()).build(null));


    public static final RegistryObject<WallFrameBlock> WALL_FRAMEBLOCK = BLOCKS.register("frame_wall", () -> new WallFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> WALL_FRAME_ITEM = ITEMS.register("frame_wall", () -> new BaseFrameItem(WALL_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> WALL_ILLUSION_TILE = TILES.register("illusion_wall", () -> TileEntityType.Builder.create(FrameBlockTile::new, WALL_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<BedFrameBlock> BED_ILLUSIONBLOCK = BLOCKS.register("illusion_bed", () -> new BedFrameBlock(DyeColor.BROWN, Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> WALL_FRAME_TILE = TILES.register("frame_wall", () -> TileEntityType.Builder.create(FrameBlockTile::new, WALL_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<Item> BED_ILLUSION_ITEM = ITEMS.register("illusion_bed", () -> new BaseIllusionItem(BED_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<ChestFrameBlock> CHEST_ILLUSIONBLOCK = BLOCKS.register("illusion_chest", () -> new ChestFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> CHEST_ILLUSION_ITEM = ITEMS.register("illusion_chest", () -> new BaseIllusionItem(CHEST_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<ChestFrameTileEntity>> CHEST_ILLUSION_TILE = TILES.register("illusion_chest", () -> TileEntityType.Builder.create(ChestFrameTileEntity::new, CHEST_ILLUSIONBLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<BedFrameTile>> BED_FRAME_TILE = TILES.register("frame_bed", () -> TileEntityType.Builder.create(BedFrameTile::new, BED_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<CarpetFrameBlock> CARPET_ILLUSIONBLOCK = BLOCKS.register("illusion_carpet", () -> new CarpetFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> CARPET_ILLUSION_ITEM = ITEMS.register("illusion_carpet", () -> new BaseIllusionItem(CARPET_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> CARPET_ILLUSION_TILE = TILES.register("illusion_carpet", () -> TileEntityType.Builder.create(FrameBlockTile::new, CARPET_ILLUSIONBLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<BedFrameTile>> BED_ILLUSION_TILE = TILES.register("illusion_bed", () -> TileEntityType.Builder.create(BedFrameTile::new, BED_FRAMEBLOCK.get()).build(null));


    public static final RegistryObject<ChestFrameBlock> CHEST_FRAMEBLOCK = BLOCKS.register("frame_chest", () -> new ChestFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> CHEST_FRAME_ITEM = ITEMS.register("frame_chest", () -> new BaseFrameItem(CHEST_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<ChestFrameTileEntity>> CHEST_FRAME_TILE = TILES.register("frame_chest", () -> TileEntityType.Builder.create(ChestFrameTileEntity::new, CHEST_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<ContainerType<ChestFrameContainer>> CHEST_FRAME_CONTAINER = CONTAINERS.register("frame_chest", () -> IForgeContainerType.create(ChestFrameContainer::new));
    public static final RegistryObject<PaneFrameBlock> PANE_ILLUSIONBLOCK = BLOCKS.register("illusion_pane", () -> new PaneFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> PANE_ILLUSION_ITEM = ITEMS.register("illusion_pane", () -> new BaseIllusionItem(PANE_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> PANE_ILLUSION_TILE = TILES.register("illusion_pane", () -> TileEntityType.Builder.create(FrameBlockTile::new, PANE_ILLUSIONBLOCK.get()).build(null));
    public static final RegistryObject<ContainerType<IllusionChestContainer>> CHEST_ILLUSION_CONTAINER = CONTAINERS.register("illusion_chest", () -> IForgeContainerType.create(IllusionChestContainer::new));


    public static final RegistryObject<CarpetFrameBlock> CARPET_FRAMEBLOCK = BLOCKS.register("frame_carpet", () -> new CarpetFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> CARPET_FRAME_ITEM = ITEMS.register("frame_carpet", () -> new BaseFrameItem(CARPET_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> CARPET_FRAME_TILE = TILES.register("frame_carpet", () -> TileEntityType.Builder.create(FrameBlockTile::new, CARPET_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<DaylightDetectorFrameBlock> DAYLIGHT_DETECTOR_ILLUSIONBLOCK = BLOCKS.register("illusion_daylight_detector", () -> new DaylightDetectorFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> DAYLIGHT_DETECTOR_ILLUSION_ITEM = ITEMS.register("illusion_daylight_detector", () -> new BaseIllusionItem(DAYLIGHT_DETECTOR_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<DaylightDetectorFrameTileEntity>> DAYLIGHT_DETECTOR_ILLUSION_TILE = TILES.register("illusion_daylight_detector", () -> TileEntityType.Builder.create(() -> new DaylightDetectorFrameTileEntity(DAYLIGHT_DETECTOR_ILLUSIONBLOCK.get().getDefaultState()), DAYLIGHT_DETECTOR_ILLUSIONBLOCK.get()).build(null));


    public static final RegistryObject<PaneFrameBlock> PANE_FRAMEBLOCK = BLOCKS.register("frame_pane", () -> new PaneFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> PANE_FRAME_ITEM = ITEMS.register("frame_pane", () -> new BaseFrameItem(PANE_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<LayeredBlock> LAYERED_FRAMEBLOCK = BLOCKS.register("frame_layered_block", () -> new LayeredBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> PANE_FRAME_TILE = TILES.register("frame_pane", () -> TileEntityType.Builder.create(FrameBlockTile::new, PANE_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<Item> LAYERED_FRAME_ITEM = ITEMS.register("frame_layered_block", () -> new BaseFrameItem(LAYERED_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> LAYERED_FRAME_TILE = TILES.register("frame_layered_block", () -> TileEntityType.Builder.create(FrameBlockTile::new, LAYERED_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<LayeredBlock> LAYERED_ILLUSIONBLOCK = BLOCKS.register("illusion_layered_block", () -> new LayeredBlock(Block.Properties.from(FRAMEBLOCK.get())));


    public static final RegistryObject<DaylightDetectorFrameBlock> DAYLIGHT_DETECTOR_FRAMEBLOCK = BLOCKS.register("frame_daylight_detector", () -> new DaylightDetectorFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> DAYLIGHT_DETECTOR_FRAME_ITEM = ITEMS.register("frame_daylight_detector", () -> new BaseFrameItem(DAYLIGHT_DETECTOR_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<DaylightDetectorFrameTileEntity>> DAYLIGHT_DETECTOR_FRAME_TILE = TILES.register("frame_daylight_detector", () -> TileEntityType.Builder.create(() -> new DaylightDetectorFrameTileEntity(DAYLIGHT_DETECTOR_FRAMEBLOCK.get().getDefaultState()), DAYLIGHT_DETECTOR_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<Item> LAYERED_ILLUSION_ITEM = ITEMS.register("illusion_layered_block", () -> new BaseIllusionItem(LAYERED_ILLUSIONBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<TileEntityType<FrameBlockTile>> LAYERED_ILLUSION_TILE = TILES.register("illusion_layered_block", () -> TileEntityType.Builder.create(FrameBlockTile::new, LAYERED_ILLUSIONBLOCK.get()).build(null));
    public static final RegistryObject<StandingSignFrameBlock> STANDING_SIGN_FRAMEBLOCK = BLOCKS.register("standing_frame_sign", () -> new StandingSignFrameBlock(Block.Properties.from(FRAMEBLOCK.get()).doesNotBlockMovement()));
    public static final RegistryObject<WallSignFrameBlock> WALL_SIGN_FRAMEBLOCK = BLOCKS.register("wall_frame_sign", () -> new WallSignFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> SIGN_FRAME_ITEM = ITEMS.register("frame_sign", () -> new FrameSignItem((new Item.Properties()).maxStackSize(16).group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY), Registration.STANDING_SIGN_FRAMEBLOCK.get(), Registration.WALL_SIGN_FRAMEBLOCK.get()));
    public static final RegistryObject<TileEntityType<SignFrameTile>> SIGN_FRAME_TILE = TILES.register("frame_sign", () -> TileEntityType.Builder.create(SignFrameTile::new, STANDING_SIGN_FRAMEBLOCK.get(), WALL_SIGN_FRAMEBLOCK.get()).build(null));
    public static final RegistryObject<StandingSignFrameBlock> STANDING_SIGN_ILLUSIONBLOCK = BLOCKS.register("standing_illusion_sign", () -> new StandingSignFrameBlock(Block.Properties.from(FRAMEBLOCK.get()).doesNotBlockMovement()));
    public static final RegistryObject<WallSignFrameBlock> WALL_SIGN_ILLUSIONBLOCK = BLOCKS.register("wall_illusion_sign", () -> new WallSignFrameBlock(Block.Properties.from(FRAMEBLOCK.get())));
    public static final RegistryObject<Item> SIGN_ILLUSION_ITEM = ITEMS.register("illusion_sign", () -> new FrameSignItem((new Item.Properties()).maxStackSize(16).group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY), Registration.STANDING_SIGN_ILLUSIONBLOCK.get(), Registration.WALL_SIGN_ILLUSIONBLOCK.get()));
    public static final RegistryObject<TileEntityType<SignFrameTile>> SIGN_ILLUSION_TILE = TILES.register("illusion_sign", () -> TileEntityType.Builder.create(SignFrameTile::new, STANDING_SIGN_ILLUSIONBLOCK.get(), WALL_SIGN_ILLUSIONBLOCK.get()).build(null));
    public static final RegistryObject<HammerItem> HAMMER = ITEMS.register("hammer", () -> new HammerItem(new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY).maxStackSize(1)));
    public static final RegistryObject<TextureWrenchItem> TEXTURE_WRENCH = ITEMS.register("texture_wrench", () -> new TextureWrenchItem(new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY).maxStackSize(1)));
    public static final RegistryObject<ChiselItem> CHISEL = ITEMS.register("chisel", () -> new ChiselItem(new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY).maxStackSize(1)));
    public static final RegistryObject<PaintbrushItem> PAINTBRUSH = ITEMS.register("paintbrush", () -> new PaintbrushItem(new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY).maxStackSize(1)));
    public static final RegistryObject<TileEntityDebugItem> DEBUG_ITEM = ITEMS.register("block_entity_debug_item", () -> new TileEntityDebugItem(new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY).maxStackSize(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> EXPLOSION_RESISTANCE_BALL = ITEMS.register("explosion_resistance_ball", () -> new Item(new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY).rarity(Rarity.UNCOMMON)));

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

    public static final RegistryObject<SlopeFrameBlock> SLOPE_FRAMEBLOCK = BLOCKS.register("frame_slope", () -> new SlopeFrameBlock(() -> FRAMEBLOCK.get().getDefaultState(), Block.Properties.from(FRAMEBLOCK.get())));

    public static final RegistryObject<TileEntityType<FrameBlockTile>> SLOPE_FRAME_TILE = TILES.register("frame_slope", () -> TileEntityType.Builder.create(FrameBlockTile::new, SLOPE_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<SlopeFrameBlock> EDGED_SLOPE_FRAMEBLOCK = BLOCKS.register("frame_edged_slope", () -> new SlopeFrameBlock(() -> FRAMEBLOCK.get().getDefaultState(), Block.Properties.from(FRAMEBLOCK.get())));

    public static final RegistryObject<TileEntityType<FrameBlockTile>> EDGED_SLOPE_FRAME_TILE = TILES.register("frame_edged_slope", () -> TileEntityType.Builder.create(FrameBlockTile::new, EDGED_SLOPE_FRAMEBLOCK.get()).build(null));

    public static final RegistryObject<Item> SLOPE_FRAME_ITEM = ITEMS.register("frame_slope", () -> new BaseFrameItem(SLOPE_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));
    public static final RegistryObject<Item> EDGED_SLOPE_FRAME_ITEM = ITEMS.register("frame_edged_slope", () -> new BaseFrameItem(EDGED_SLOPE_FRAMEBLOCK.get(), new Item.Properties().group(BlockCarpentryMain.BlockCarpentryItemGroup.BLOCK_CARPENTRY)));

}
//========SOLI DEO GLORIA========//