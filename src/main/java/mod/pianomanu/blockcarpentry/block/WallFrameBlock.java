package mod.pianomanu.blockcarpentry.block;

import com.google.common.collect.ImmutableMap;
import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockSavingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WallHeight;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;


/**
 * Main class for frame walls - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.5 05/01/21
 */
public class WallFrameBlock extends WallBlock {
    public static final BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;
    public static final IntegerProperty LIGHT_LEVEL = BCBlockStateProperties.LIGHT_LEVEL;

    private final Map<BlockState, VoxelShape> stateToShapeMap;
    private final Map<BlockState, VoxelShape> stateToCollisionShapeMap;

    public WallFrameBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(CONTAINS_BLOCK, false));
        this.stateToShapeMap = this.func_235624_a_(4.0F, 3.0F, 16.0F, 0.0F, 14.0F, 16.0F);
        this.stateToCollisionShapeMap = this.func_235624_a_(4.0F, 3.0F, 24.0F, 0.0F, 24.0F, 24.0F);
    }

    private static VoxelShape func_235631_a_(VoxelShape p_235631_0_, WallHeight p_235631_1_, VoxelShape p_235631_2_, VoxelShape p_235631_3_) {
        if (p_235631_1_ == WallHeight.TALL) {
            return VoxelShapes.or(p_235631_0_, p_235631_3_);
        } else {
            return p_235631_1_ == WallHeight.LOW ? VoxelShapes.or(p_235631_0_, p_235631_2_) : p_235631_0_;
        }
    }

    private Map<BlockState, VoxelShape> func_235624_a_(float p_235624_1_, float p_235624_2_, float p_235624_3_, float p_235624_4_, float p_235624_5_, float p_235624_6_) {
        float f = 8.0F - p_235624_1_;
        float f1 = 8.0F + p_235624_1_;
        float f2 = 8.0F - p_235624_2_;
        float f3 = 8.0F + p_235624_2_;
        VoxelShape voxelshape = Block.makeCuboidShape((double) f, 0.0D, (double) f, (double) f1, (double) p_235624_3_, (double) f1);
        VoxelShape voxelshape1 = Block.makeCuboidShape((double) f2, (double) p_235624_4_, 0.0D, (double) f3, (double) p_235624_5_, (double) f3);
        VoxelShape voxelshape2 = Block.makeCuboidShape((double) f2, (double) p_235624_4_, (double) f2, (double) f3, (double) p_235624_5_, 16.0D);
        VoxelShape voxelshape3 = Block.makeCuboidShape(0.0D, (double) p_235624_4_, (double) f2, (double) f3, (double) p_235624_5_, (double) f3);
        VoxelShape voxelshape4 = Block.makeCuboidShape((double) f2, (double) p_235624_4_, (double) f2, 16.0D, (double) p_235624_5_, (double) f3);
        VoxelShape voxelshape5 = Block.makeCuboidShape((double) f2, (double) p_235624_4_, 0.0D, (double) f3, (double) p_235624_6_, (double) f3);
        VoxelShape voxelshape6 = Block.makeCuboidShape((double) f2, (double) p_235624_4_, (double) f2, (double) f3, (double) p_235624_6_, 16.0D);
        VoxelShape voxelshape7 = Block.makeCuboidShape(0.0D, (double) p_235624_4_, (double) f2, (double) f3, (double) p_235624_6_, (double) f3);
        VoxelShape voxelshape8 = Block.makeCuboidShape((double) f2, (double) p_235624_4_, (double) f2, 16.0D, (double) p_235624_6_, (double) f3);
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        for (int lightlevel = 0; lightlevel < 16; lightlevel++) {
            for (Boolean obool : UP.getAllowedValues()) {
                for (WallHeight wallheight : WALL_HEIGHT_EAST.getAllowedValues()) {
                    for (WallHeight wallheight1 : WALL_HEIGHT_NORTH.getAllowedValues()) {
                        for (WallHeight wallheight2 : WALL_HEIGHT_WEST.getAllowedValues()) {
                            for (WallHeight wallheight3 : WALL_HEIGHT_SOUTH.getAllowedValues()) {
                                VoxelShape voxelshape9 = VoxelShapes.empty();
                                voxelshape9 = func_235631_a_(voxelshape9, wallheight, voxelshape4, voxelshape8);
                                voxelshape9 = func_235631_a_(voxelshape9, wallheight2, voxelshape3, voxelshape7);
                                voxelshape9 = func_235631_a_(voxelshape9, wallheight1, voxelshape1, voxelshape5);
                                voxelshape9 = func_235631_a_(voxelshape9, wallheight3, voxelshape2, voxelshape6);
                                if (obool) {
                                    voxelshape9 = VoxelShapes.or(voxelshape9, voxelshape);
                                }

                                BlockState blockstate = this.getDefaultState().with(UP, obool).with(WALL_HEIGHT_EAST, wallheight).with(WALL_HEIGHT_WEST, wallheight2).with(WALL_HEIGHT_NORTH, wallheight1).with(WALL_HEIGHT_SOUTH, wallheight3);
                                builder.put(blockstate.with(WATERLOGGED, Boolean.valueOf(false)).with(CONTAINS_BLOCK, false).with(LIGHT_LEVEL, lightlevel), voxelshape9);
                                builder.put(blockstate.with(WATERLOGGED, Boolean.valueOf(true)).with(CONTAINS_BLOCK, false).with(LIGHT_LEVEL, lightlevel), voxelshape9);
                                builder.put(blockstate.with(WATERLOGGED, Boolean.valueOf(false)).with(CONTAINS_BLOCK, true).with(LIGHT_LEVEL, lightlevel), voxelshape9);
                                builder.put(blockstate.with(WATERLOGGED, Boolean.valueOf(true)).with(CONTAINS_BLOCK, true).with(LIGHT_LEVEL, lightlevel), voxelshape9);

                            }
                        }
                    }
                }
            }
        }

        return builder.build();
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(UP, WALL_HEIGHT_NORTH, WALL_HEIGHT_EAST, WALL_HEIGHT_WEST, WALL_HEIGHT_SOUTH, WATERLOGGED, CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.stateToCollisionShapeMap.get(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.stateToShapeMap.get(state);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FrameBlockTile();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        ItemStack item = player.getHeldItem(hand);
        if (!world.isRemote) {
            BlockAppearanceHelper.setLightLevel(item, state, world, pos, player, hand);
            BlockAppearanceHelper.setTexture(item, state, world, player, pos);
            BlockAppearanceHelper.setDesign(world, pos, player, item);
            BlockAppearanceHelper.setDesignTexture(world, pos, player, item);
            BlockAppearanceHelper.setOverlay(world, pos, player, item);
            BlockAppearanceHelper.setRotation(world, pos, player, item);

            if (item.getItem() instanceof BlockItem) {
                if (state.get(BCBlockStateProperties.CONTAINS_BLOCK) || Objects.requireNonNull(item.getItem().getRegistryName()).getNamespace().equals(BlockCarpentryMain.MOD_ID)) {
                    return ActionResultType.PASS;
                }
                TileEntity tileEntity = world.getTileEntity(pos);
                int count = player.getHeldItem(hand).getCount();
                Block heldBlock = ((BlockItem) item.getItem()).getBlock();
                if (tileEntity instanceof FrameBlockTile && !item.isEmpty() && BlockSavingHelper.isValidBlock(heldBlock) && !state.get(CONTAINS_BLOCK)) {
                    BlockState handBlockState = ((BlockItem) item.getItem()).getBlock().getDefaultState();
                    insertBlock(world, pos, state, handBlockState);
                    if (!player.isCreative())
                        player.getHeldItem(hand).setCount(count - 1);

                }
            }

            if (player.getHeldItem(hand).getItem() == Registration.HAMMER.get() || (!BCModConfig.HAMMER_NEEDED.get() && player.isSneaking())) {
                if (!player.isCreative())
                    this.dropContainedBlock(world, pos);
                state = state.with(CONTAINS_BLOCK, Boolean.FALSE);
                world.setBlockState(pos, state, 2);
            }
        }
        return ActionResultType.SUCCESS;
    }

    protected void dropContainedBlock(World worldIn, BlockPos pos) {
        if (!worldIn.isRemote) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof FrameBlockTile) {
                FrameBlockTile frameTileEntity = (FrameBlockTile) tileentity;
                BlockState blockState = frameTileEntity.getMimic();
                if (!(blockState == null)) {
                    worldIn.playEvent(1010, pos, 0);
                    frameTileEntity.clear();
                    float f = 0.7F;
                    double d0 = (double) (worldIn.rand.nextFloat() * 0.7F) + (double) 0.15F;
                    double d1 = (double) (worldIn.rand.nextFloat() * 0.7F) + (double) 0.060000002F + 0.6D;
                    double d2 = (double) (worldIn.rand.nextFloat() * 0.7F) + (double) 0.15F;
                    ItemStack itemstack1 = new ItemStack(blockState.getBlock());
                    ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, itemstack1);
                    itementity.setDefaultPickupDelay();
                    worldIn.addEntity(itementity);
                    frameTileEntity.clear();
                }
            }
        }
    }

    public void insertBlock(IWorld worldIn, BlockPos pos, BlockState state, BlockState handBlock) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof FrameBlockTile) {
            FrameBlockTile frameTileEntity = (FrameBlockTile) tileentity;
            frameTileEntity.clear();
            frameTileEntity.setMimic(handBlock);
            worldIn.setBlockState(pos, state.with(CONTAINS_BLOCK, Boolean.TRUE), 2);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(worldIn, pos);

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        if (state.get(LIGHT_LEVEL) > 15) {
            return 15;
        }
        return state.get(LIGHT_LEVEL);
    }
}
//========SOLI DEO GLORIA========//
