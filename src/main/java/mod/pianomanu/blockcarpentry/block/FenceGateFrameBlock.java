package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.tileentity.LockableFrameTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Main class for frame fence gates - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.6 09/23/23
 */
public class FenceGateFrameBlock extends FenceGateBlock implements SimpleWaterloggedBlock, EntityBlock, IFrameBlock {
    public FenceGateFrameBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, Boolean.FALSE).setValue(POWERED, Boolean.FALSE).setValue(IN_WALL, Boolean.FALSE).setValue(CONTAINS_BLOCK, false).setValue(LIGHT_LEVEL, 0).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED, CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LockableFrameTile(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitresult) {
        return frameUse(state, level, pos, player, hand, hitresult);
    }

    @Override
    public InteractionResult frameUseServer(BlockState state, Level level, BlockPos pos, Player player, ItemStack itemStack, BlockHitResult hitresult) {
        convertOutdatedTile(state, level, pos, player);
        if (shouldCallFrameUse(state, itemStack))
            return IFrameBlock.super.frameUseServer(state, level, pos, player, itemStack, hitresult);
        if (lockRedstoneSignal(state, level, pos, player, itemStack) || lockOpenClose(state, level, pos, player, itemStack))
            return InteractionResult.CONSUME;
        if (state.getValue(CONTAINS_BLOCK)) {
            return fenceGateBehavior(state, level, pos, player, hitresult);
        }
        return InteractionResult.FAIL;
    }

    private InteractionResult fenceGateBehavior(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitresult) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof LockableFrameTile fenceGateTileEntity) {
            if (fenceGateTileEntity.canBeOpenedByPlayers()) {
                super.use(state, level, pos, player, InteractionHand.MAIN_HAND, hitresult);
                level.levelEvent(null, state.getValue(OPEN) ? 1014 : 1008, pos, 0);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }

    private void convertOutdatedTile(BlockState state, Level level, BlockPos pos, Player player) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (!(tileEntity instanceof LockableFrameTile) && (tileEntity instanceof FrameBlockTile)) {
            LockableFrameTile newTile = (LockableFrameTile) newBlockEntity(pos, state);
            if (newTile != null) {
                newTile.addFromOutdatedTileEntity((FrameBlockTile) tileEntity);
                level.setBlockEntity(newTile);
                player.displayClientMessage(Component.translatable("message.blockcarpentry.converting_outdated_block"), true);
            }
        }
    }

    private boolean lockRedstoneSignal(BlockState state, Level level, BlockPos pos, Player player, ItemStack itemStack) {
        if (itemStack.getItem() == Items.REDSTONE) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof LockableFrameTile doorTileEntity) {
                if (doorTileEntity.canBeOpenedByRedstoneSignal()) {
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.redstone_off"), true);
                } else {
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.redstone_on"), true);
                }
                doorTileEntity.setCanBeOpenedByRedstoneSignal(!doorTileEntity.canBeOpenedByRedstoneSignal());
            } else {
                convertOutdatedTile(state, level, pos, player);
            }
            return true;
        }
        return false;
    }

    private boolean lockOpenClose(BlockState state, Level level, BlockPos pos, Player player, ItemStack itemStack) {
        if (itemStack.getItem() == Items.IRON_INGOT) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof LockableFrameTile doorTileEntity) {
                if (doorTileEntity.canBeOpenedByPlayers()) {
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.lock"), true);
                } else {
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.unlock"), true);
                }
                doorTileEntity.setCanBeOpenedByPlayers(!doorTileEntity.canBeOpenedByPlayers());
            } else if (tileEntity instanceof FrameBlockTile) {
                LockableFrameTile newTile = (LockableFrameTile) newBlockEntity(pos, state);
                if (newTile != null) {
                    newTile.addFromOutdatedTileEntity((FrameBlockTile) tileEntity);
                    level.setBlockEntity(newTile);
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.converting_outdated_block"), true);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isCorrectTileInstance(BlockEntity blockEntity) {
        return blockEntity instanceof LockableFrameTile;
    }

    public void fillBlockEntity(Level levelIn, BlockPos pos, BlockState state, BlockState handBlock, BlockEntity blockEntity) {
        LockableFrameTile frameBlockEntity = (LockableFrameTile) blockEntity;
        frameBlockEntity.clear();
        frameBlockEntity.setMimic(handBlock);
        levelIn.setBlock(pos, state.setValue(CONTAINS_BLOCK, Boolean.TRUE), 2);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level levelIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(levelIn, pos);

            super.onRemove(state, levelIn, pos, newState, isMoving);
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return IFrameBlock.getLightEmission(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor levelIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            levelIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelIn));
        }

        return super.updateShape(stateIn, facing, facingState, levelIn, currentPos, facingPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(blockpos);
        BlockState state = super.getStateForPlacement(context);
        if (fluidstate.getType() == Fluids.WATER) {
            return Objects.requireNonNull(state).setValue(WATERLOGGED, fluidstate.isSource());
        } else {
            return state;
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean update) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof LockableFrameTile doorTileEntity && doorTileEntity.canBeOpenedByRedstoneSignal()) {
            super.neighborChanged(state, level, pos, block, pos2, update);
        }
    }
}
//========SOLI DEO GLORIA========//