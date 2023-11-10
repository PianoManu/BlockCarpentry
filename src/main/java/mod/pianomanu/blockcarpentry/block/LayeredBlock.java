package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

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
public class LayeredBlock extends AbstractSixWayFrameBlock implements SimpleWaterloggedBlock {
    public static final IntegerProperty LAYERS = BCBlockStateProperties.LAYERS;
    public static final IntegerProperty LIGHT_LEVEL = BCBlockStateProperties.LIGHT_LEVEL;

    public LayeredBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN).setValue(CONTAINS_BLOCK, Boolean.FALSE).setValue(LIGHT_LEVEL, 0).setValue(WATERLOGGED, false).setValue(LAYERS, 1));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CONTAINS_BLOCK, LIGHT_LEVEL, WATERLOGGED, LAYERS);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitresult) {
        return frameUse(state, level, pos, player, hand, hitresult);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return getShapeFromFacing(state);
    }

    private VoxelShape getShapeFromFacing(BlockState state) {
        Direction facing = state.getValue(FACING);
        int layers = state.getValue(LAYERS);
        switch (facing) {
            case UP -> {
                return Block.box(0.0D, 16.0D - layers * 2.0D, 0.0D, 16.0D, 16.0D, 16.0D);
            }
            case DOWN -> {
                return Block.box(0.0D, 0.0D, 0.0D, 16.0D, layers * 2.0D, 16.0D);
            }
            case WEST -> {
                return Block.box(0.0D, 0.0D, 0.0D, layers * 2.0D, 16.0D, 16.0D);
            }
            case EAST -> {
                return Block.box(16.0D - layers * 2.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
            }
            case NORTH -> {
                return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, layers * 2.0D);
            }
            case SOUTH -> {
                return Block.box(0.0D, 0.0D, 16.0D - layers * 2.0D, 16.0D, 16.0D, 16.0D);
            }
        }
        return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        int i = state.getValue(LAYERS);
        if (context.getItemInHand().is(this.asItem()) && i < 8) {
            if (context.replacingClickedOnBlock()) {
                return context.getClickedFace().getOpposite() == state.getValue(FACING);
            }
        }
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(blockpos);
        BlockState blockState = context.getLevel().getBlockState(blockpos);
        if (blockState.is(this)) {
            int i = blockState.getValue(LAYERS);
            return blockState.setValue(LAYERS, Math.min(8, i + 1));
        }
        //if (Objects.requireNonNull(context.getPlayer()).isCrouching() && BCModConfig.SNEAK_FOR_VERTICAL_SLABS.get() || !Objects.requireNonNull(context.getPlayer()).isCrouching() && !BCModConfig.SNEAK_FOR_VERTICAL_SLABS.get()) {
        if (fluidstate.getType() == Fluids.WATER) {
            return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection()).setValue(WATERLOGGED, fluidstate.isSource());
        } else {
            return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
        }
        /*} else {
            BlockState blockstate1 = this.defaultBlockState().setValue(FACING, Direction.UP).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
            Direction direction = context.getClickedFace();
            return direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double) blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.setValue(FACING, Direction.DOWN);
        }*/
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return this.getShape(state, getter, pos, context);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FrameBlockTile(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level levelIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(levelIn, pos);

            super.onRemove(state, levelIn, pos, newState, isMoving);
        }
        if (state.getValue(WATERLOGGED)) {
            levelIn.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelIn));
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return new ArrayList<>(Collections.singleton(new ItemStack(this.asItem(), state.getValue(LAYERS))));
    }
}
//========SOLI DEO GLORIA========//