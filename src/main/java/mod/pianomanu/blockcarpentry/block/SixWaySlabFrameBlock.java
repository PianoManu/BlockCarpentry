package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.item.BaseFrameItem;
import mod.pianomanu.blockcarpentry.item.BaseIllusionItem;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.TwoBlocksFrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import mod.pianomanu.blockcarpentry.util.BlockSavingHelper;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResultType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Main class for frame "slabs", they can be placed in six different ways (that's the reason for this class name) - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.6 09/27/23
 */
@SuppressWarnings("deprecation")
public class SixWaySlabFrameBlock extends AbstractSixWayFrameBlock implements IWaterLoggable {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;
    public static final BooleanProperty CONTAINS_2ND_BLOCK = BCBlockStateProperties.CONTAINS_2ND_BLOCK;
    public static final IntegerProperty LIGHT_LEVEL = BCBlockStateProperties.LIGHT_LEVEL;
    public static final BooleanProperty DOUBLE_SLAB = BCBlockStateProperties.DOUBLE;
    //everything is inverted because when placing, we would need to take the opposite - I figured it out when I completed my work and I don't want to change everything again
    protected static final VoxelShape BOTTOM = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape TOP = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape EAST = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape WEST = Block.makeCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape NORTH = Block.makeCuboidShape(0.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape CUBE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public SixWaySlabFrameBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.DOWN).with(CONTAINS_BLOCK, Boolean.FALSE).with(LIGHT_LEVEL, 0).with(WATERLOGGED, false).with(DOUBLE_SLAB, false).with(CONTAINS_2ND_BLOCK, false));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(WATERLOGGED, DOUBLE_SLAB, CONTAINS_2ND_BLOCK);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.get(DOUBLE_SLAB))
            return CUBE;
        switch (state.get(FACING)) {
            case EAST:
                return EAST;
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            case UP:
                return TOP;
            default:
                return BOTTOM;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        FluidState fluidstate = context.getWorld().getFluidState(blockpos);
        BlockState blockState = context.getWorld().getBlockState(blockpos);
        if (blockState.isIn(this)) {
            return blockState.with(DOUBLE_SLAB, true).with(WATERLOGGED, false);
        }
        if (Objects.requireNonNull(context.getPlayer()).isCrouching() && BCModConfig.SNEAK_FOR_VERTICAL_SLABS.get() || !Objects.requireNonNull(context.getPlayer()).isCrouching() && !BCModConfig.SNEAK_FOR_VERTICAL_SLABS.get()) {
            if (fluidstate.getFluid() == Fluids.WATER) {
                return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite()).with(WATERLOGGED, fluidstate.isSource());
            } else {
                return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
            }
        } else {
            BlockState blockstate1 = this.getDefaultState().with(FACING, Direction.UP).with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
            Direction direction = context.getFace();
            return direction != Direction.DOWN && (direction == Direction.UP || !(context.getHitVec().y - (double) blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.with(FACING, Direction.DOWN);
        }
    }

    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        ItemStack itemstack = useContext.getItem();
        boolean isDouble = state.get(DOUBLE_SLAB);
        if (!isDouble && itemstack.getItem() == this.asItem()) {
            if (useContext.replacingClickedOnBlock()) {
                Direction direction = useContext.getFace();
                switch (state.get(FACING)) {
                    case EAST:
                        return direction == Direction.EAST;
                    case SOUTH:
                        return direction == Direction.SOUTH;
                    case WEST:
                        return direction == Direction.WEST;
                    case NORTH:
                        return direction == Direction.NORTH;
                    case UP:
                        return direction == Direction.UP;
                    default:
                        return direction == Direction.DOWN;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TwoBlocksFrameBlockTile();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitresult) {
        return frameUse(state, level, pos, player, hand, hitresult);
    }

    @Override
    public boolean changeMimic(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        boolean isDouble = state.get(DOUBLE_SLAB);

        if (isDouble && state.get(BCBlockStateProperties.CONTAINS_2ND_BLOCK) || !isDouble && state.get(BCBlockStateProperties.CONTAINS_BLOCK) || itemStack.getItem() instanceof BaseFrameItem || itemStack.getItem() instanceof BaseIllusionItem) {
            return false;
        }
        TileEntity tileEntity = level.getTileEntity(pos);
        int count = itemStack.getCount();
        Block heldBlock = ((BlockItem) itemStack.getItem()).getBlock();
        if (tileEntity instanceof TwoBlocksFrameBlockTile && !itemStack.isEmpty() && BlockSavingHelper.isValidBlock(heldBlock) && !state.get(CONTAINS_2ND_BLOCK)) {
            BlockState handBlockState = ((BlockItem) itemStack.getItem()).getBlock().getDefaultState();
            insertBlock(level, pos, state, handBlockState);
            if (!player.isCreative())
                itemStack.setCount(count - 1);
        }
        return true;
    }

    public boolean removeBlock(World level, BlockPos pos, BlockState state, ItemStack itemStack, PlayerEntity player) {
        if (itemStack.getItem() == Registration.HAMMER.get() || (!BCModConfig.HAMMER_NEEDED.get() && player.isCrouching())) {
            if (!player.isCreative())
                this.dropContainedBlock(level, pos);
            else {
                this.clearTile(level, pos);
            }
            state = state.with(CONTAINS_BLOCK, Boolean.FALSE).with(CONTAINS_2ND_BLOCK, Boolean.FALSE);
            level.setBlockState(pos, state, 2);
            return true;
        }
        return false;
    }

    public void clearTile(World level, BlockPos pos) {
        if (!level.isRemote) {
            TileEntity tileentity = level.getTileEntity(pos);
            if (tileentity instanceof TwoBlocksFrameBlockTile) {
                TwoBlocksFrameBlockTile frameTileEntity = (TwoBlocksFrameBlockTile) tileentity;
                frameTileEntity.clear();
            }
        }
    }

    public void dropContainedBlock(World level, BlockPos pos) {
        if (!level.isRemote) {
            TileEntity tileentity = level.getTileEntity(pos);
            if (tileentity instanceof TwoBlocksFrameBlockTile) {
                TwoBlocksFrameBlockTile frameTileEntity = (TwoBlocksFrameBlockTile) tileentity;
                BlockState blockState = frameTileEntity.getMimic();
                if (!(blockState == null)) {
                    dropItemStackInWorld(level, pos, blockState);
                }
                blockState = frameTileEntity.getMimic_2();
                if (!(blockState == null)) {
                    dropItemStackInWorld(level, pos, blockState);
                }
                frameTileEntity.clear();
            }
        }
    }

    public void insertBlock(World level, BlockPos pos, BlockState state, BlockState handBlock) {
        TileEntity tileentity = level.getTileEntity(pos);
        if (tileentity instanceof TwoBlocksFrameBlockTile) {
            if (!state.get(CONTAINS_BLOCK)) {
                TwoBlocksFrameBlockTile frameTileEntity = (TwoBlocksFrameBlockTile) tileentity;
                frameTileEntity.clear();
                frameTileEntity.setMimic(handBlock);
                level.setBlockState(pos, state.with(CONTAINS_BLOCK, Boolean.TRUE), 2);
            } else if (state.get(DOUBLE_SLAB)) {
                TwoBlocksFrameBlockTile frameTileEntity = (TwoBlocksFrameBlockTile) tileentity;
                frameTileEntity.setMimic_2(handBlock);
                level.setBlockState(pos, state.with(CONTAINS_2ND_BLOCK, Boolean.TRUE), 2);
            }
        }
    }

    @Override
    public void onReplaced(BlockState state, @Nonnull World level, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(level, pos);

            super.onReplaced(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader level, BlockPos pos) {
        if (state.get(LIGHT_LEVEL) > 15) {
            return 15;
        }
        return state.get(LIGHT_LEVEL);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    @Nonnull
    public BlockState updatePostPlacement(BlockState stateIn, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull IWorld worldIn, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
}
//========SOLI DEO GLORIA========//