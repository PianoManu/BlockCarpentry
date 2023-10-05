package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Main class for frame and illusion layered blocks - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.3 09/23/23
 */
public class LayeredBlock extends AbstractSixWayFrameBlock implements IWaterLoggable {
    public static final IntegerProperty LAYERS = BCBlockStateProperties.LAYERS;
    public static final IntegerProperty LIGHT_LEVEL = BCBlockStateProperties.LIGHT_LEVEL;

    public LayeredBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.DOWN).with(CONTAINS_BLOCK, Boolean.FALSE).with(LIGHT_LEVEL, 0).with(WATERLOGGED, false).with(LAYERS, 1));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, CONTAINS_BLOCK, LIGHT_LEVEL, WATERLOGGED, LAYERS);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitresult) {
        return frameUse(state, level, pos, player, hand, hitresult);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return getShapeFromFacing(state);
    }

    private VoxelShape getShapeFromFacing(BlockState state) {
        Direction facing = state.get(FACING);
        int layers = state.get(LAYERS);
        switch (facing) {
            case UP:
                return Block.makeCuboidShape(0.0D, 16.0D - layers * 2.0D, 0.0D, 16.0D, 16.0D, 16.0D);
            case DOWN:
                return Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, layers * 2.0D, 16.0D);
            case WEST:
                return Block.makeCuboidShape(0.0D, 0.0D, 0.0D, layers * 2.0D, 16.0D, 16.0D);
            case EAST:
                return Block.makeCuboidShape(16.0D - layers * 2.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
            case NORTH:
                return Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, layers * 2.0D);
            case SOUTH:
                return Block.makeCuboidShape(0.0D, 0.0D, 16.0D - layers * 2.0D, 16.0D, 16.0D, 16.0D);
        }
        return Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isReplaceable(BlockState state, BlockItemUseContext context) {
        int i = state.get(LAYERS);
        if (context.getItem().getItem() == this.asItem() && i < 8) {
            if (context.replacingClickedOnBlock()) {
                return context.getFace() == Direction.UP;
            } else {
                return true;
            }
        } else {
            return i == 1;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        FluidState fluidstate = context.getWorld().getFluidState(blockpos);
        BlockState blockState = context.getWorld().getBlockState(blockpos);
        if (blockState.isIn(this)) {
            int i = blockState.get(LAYERS);
            return blockState.with(LAYERS, Math.min(8, i + 1));
        }
        if (fluidstate.getFluid() == Fluids.WATER) {
            return this.getDefaultState().with(FACING, context.getNearestLookingDirection()).with(WATERLOGGED, fluidstate.isSource());
        } else {
            return this.getDefaultState().with(FACING, context.getNearestLookingDirection());
        }
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
    public void onReplaced(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(level, pos);

            super.onReplaced(state, level, pos, newState, isMoving);
        }
        if (state.get(WATERLOGGED)) {
            level.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(level));
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return new ArrayList<>(Collections.singleton(new ItemStack(this.asItem(), state.get(LAYERS))));
    }
}
//========SOLI DEO GLORIA========//