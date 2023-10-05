package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.tileentity.LockableFrameTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;

/**
 * Main class for frame trapdoors - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.7 09/27/23
 */
public class TrapdoorFrameBlock extends TrapDoorBlock implements IFrameBlock {

    public TrapdoorFrameBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(OPEN, Boolean.valueOf(false)).with(POWERED, Boolean.valueOf(false)).with(CONTAINS_BLOCK, Boolean.FALSE).with(LIGHT_LEVEL, 0));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(CONTAINS_BLOCK, LIGHT_LEVEL);
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
            return trapdoorBehavior(state, level, pos, player, hitresult);
        }
        return ActionResultType.FAIL;
    }

    private ActionResultType trapdoorBehavior(BlockState state, World level, BlockPos pos, PlayerEntity player, BlockRayTraceResult hitresult) {
        TileEntity tileEntity = level.getTileEntity(pos);
        if (tileEntity instanceof LockableFrameTile) {
            LockableFrameTile trapdoorTileEntity = (LockableFrameTile) tileEntity;
            if (trapdoorTileEntity.canBeOpenedByPlayers()) {
                super.onBlockActivated(state, level, pos, player, Hand.MAIN_HAND, hitresult);
                this.playSound(null, level, pos, state.get(OPEN));
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
                LockableFrameTile trapdoorTileEntity = (LockableFrameTile) tileEntity;
                if (trapdoorTileEntity.canBeOpenedByRedstoneSignal()) {
                    player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.redstone_off"), true);
                } else {
                    player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.redstone_on"), true);
                }
                trapdoorTileEntity.setCanBeOpenedByRedstoneSignal(!trapdoorTileEntity.canBeOpenedByRedstoneSignal());
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
                LockableFrameTile trapdoorTileEntity = (LockableFrameTile) tileEntity;
                if (trapdoorTileEntity.canBeOpenedByPlayers()) {
                    player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.lock"), true);
                } else {
                    player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.unlock"), true);
                }
                trapdoorTileEntity.setCanBeOpenedByPlayers(!trapdoorTileEntity.canBeOpenedByPlayers());
            } else {
                convertOutdatedTile(state, level, pos, player);
            }
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
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
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos pos2, boolean update) {
        TileEntity tileEntity = level.getTileEntity(pos);
        if (tileEntity instanceof LockableFrameTile) {
            LockableFrameTile trapdoorTileEntity = (LockableFrameTile) tileEntity;
            if (trapdoorTileEntity.canBeOpenedByRedstoneSignal())
                super.neighborChanged(state, level, pos, block, pos2, update);
        }
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

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        return IFrameBlock.super.canSustainPlant(world, pos, facing);
    }
}
//========SOLI DEO GLORIA========//