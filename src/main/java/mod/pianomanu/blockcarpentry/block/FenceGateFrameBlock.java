package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.tileentity.LockableFrameTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockModificationHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Main class for frame fence gates - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.7 09/27/23
 */
public class FenceGateFrameBlock extends FenceGateBlock implements IWaterLoggable, IFrameBlock {
    public FenceGateFrameBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(OPEN, Boolean.FALSE).with(POWERED, Boolean.FALSE).with(IN_WALL, Boolean.FALSE).with(CONTAINS_BLOCK, false).with(LIGHT_LEVEL, 0).with(WATERLOGGED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(WATERLOGGED, CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new LockableFrameTile();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitresult) {
        return frameUse(state, level, pos, player, hand, hitresult);
    }

    @Override
    public ActionResultType frameUseServer(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack, BlockRayTraceResult hitresult) {
        convertOutdatedTile(state, level, pos, player);
        if (shouldCallFrameUse(state, itemStack))
            return IFrameBlock.super.frameUseServer(state, level, pos, player, itemStack, hitresult);
        if (lockRedstoneSignal(state, level, pos, player, itemStack) || lockOpenClose(state, level, pos, player, itemStack))
            return ActionResultType.CONSUME;
        if (state.get(CONTAINS_BLOCK)) {
            return fenceGateBehavior(state, level, pos, player, hitresult);
        }
        return ActionResultType.FAIL;
    }

    private ActionResultType fenceGateBehavior(BlockState state, World level, BlockPos pos, PlayerEntity player, BlockRayTraceResult hitresult) {
        TileEntity tileEntity = level.getTileEntity(pos);
        if (tileEntity instanceof LockableFrameTile) {
            LockableFrameTile fenceGateTileEntity = (LockableFrameTile) tileEntity;
            if (fenceGateTileEntity.canBeOpenedByPlayers()) {
                super.onBlockActivated(state, level, pos, player, Hand.MAIN_HAND, hitresult);
                level.playEvent(null, state.get(OPEN) ? 1014 : 1008, pos, 0);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.CONSUME;
    }

    private void convertOutdatedTile(BlockState state, World level, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = level.getTileEntity(pos);
        if (!(tileEntity instanceof LockableFrameTile) && (tileEntity instanceof FrameBlockTile)) {
            LockableFrameTile newTile = (LockableFrameTile) createTileEntity(state, level);
            if (newTile != null) {
                newTile.addFromOutdatedTileEntity((FrameBlockTile) tileEntity);
                level.setTileEntity(pos, newTile);
                player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.converting_outdated_block"), true);
            }
        }
    }

    private boolean lockRedstoneSignal(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        if (itemStack.getItem() == Items.REDSTONE) {
            TileEntity tileEntity = level.getTileEntity(pos);
            if (tileEntity instanceof LockableFrameTile) {
                LockableFrameTile fenceGateTileEntity = (LockableFrameTile) tileEntity;
                if (fenceGateTileEntity.canBeOpenedByRedstoneSignal()) {
                    player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.redstone_off"), true);
                } else {
                    player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.redstone_on"), true);
                }
                fenceGateTileEntity.setCanBeOpenedByRedstoneSignal(!fenceGateTileEntity.canBeOpenedByRedstoneSignal());
            } else {
                convertOutdatedTile(state, level, pos, player);
            }
            return true;
        }
        return false;
    }

    private boolean lockOpenClose(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        if (itemStack.getItem() == Items.IRON_INGOT) {
            TileEntity tileEntity = level.getTileEntity(pos);
            if (tileEntity instanceof LockableFrameTile) {
                LockableFrameTile fenceGateTileEntity = (LockableFrameTile) tileEntity;
                if (fenceGateTileEntity.canBeOpenedByPlayers()) {
                    player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.lock"), true);
                } else {
                    player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.unlock"), true);
                }
                fenceGateTileEntity.setCanBeOpenedByPlayers(!fenceGateTileEntity.canBeOpenedByPlayers());
            } else if (tileEntity instanceof FrameBlockTile) {
                LockableFrameTile newTile = (LockableFrameTile) createTileEntity(state, level);
                if (newTile != null) {
                    newTile.addFromOutdatedTileEntity((FrameBlockTile) tileEntity);
                    level.setTileEntity(pos, newTile);
                    player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.converting_outdated_block"), true);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isCorrectTileInstance(TileEntity TileEntity) {
        return TileEntity instanceof LockableFrameTile;
    }

    public void fillTileEntity(World level, BlockPos pos, BlockState state, BlockState handBlock, TileEntity TileEntity) {
        LockableFrameTile frameTileEntity = (LockableFrameTile) TileEntity;
        frameTileEntity.clear();
        frameTileEntity.setMimic(handBlock);
        level.setBlockState(pos, state.with(CONTAINS_BLOCK, Boolean.TRUE), 2);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(level, pos);

            super.onReplaced(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader level, BlockPos pos) {
        return IFrameBlock.getLightValue(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        FluidState fluidstate = context.getWorld().getFluidState(blockpos);
        BlockState state = super.getStateForPlacement(context);
        if (fluidstate.getFluid() == Fluids.WATER) {
            return Objects.requireNonNull(state).with(WATERLOGGED, fluidstate.isSource());
        } else {
            return state;
        }
    }

    @Override
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos pos2, boolean update) {
        TileEntity tileEntity = level.getTileEntity(pos);
        if (tileEntity instanceof LockableFrameTile) {
            LockableFrameTile fenceGateTileEntity = (LockableFrameTile) tileEntity;
            if (fenceGateTileEntity.canBeOpenedByRedstoneSignal())
                super.neighborChanged(state, level, pos, block, pos2, update);
        }
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        return IFrameBlock.super.canSustainPlant(world, pos, facing);
    }

    @Override
    public boolean executeModifications(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        return BlockAppearanceHelper.setAll(itemStack, state, level, pos, player) || getTile(level, pos) != null && BlockModificationHelper.setAll(itemStack, getTile(level, pos), player, true, false);
    }
}
//========SOLI DEO GLORIA========//