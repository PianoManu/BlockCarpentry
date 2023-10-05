package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.tileentity.LockableFrameTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockModificationHelper;
import net.minecraft.block.DoorBlock;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResultType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.common.IPlantable;

/**
 * Main class for frame doors - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.6 09/27/23
 */
public class DoorFrameBlock extends DoorBlock implements IFrameBlock {

    public DoorFrameBlock(Properties builder) {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(CONTAINS_BLOCK, Boolean.FALSE).with(DoorBlock.FACING, Direction.NORTH).with(OPEN, Boolean.valueOf(false)).with(HINGE, DoorHingeSide.LEFT).with(POWERED, Boolean.valueOf(false)).with(HALF, DoubleBlockHalf.LOWER).with(LIGHT_LEVEL, 0));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

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
            return doorBehavior(state, level, pos, player, hitresult);
        }
        return ActionResultType.FAIL;
    }

    private ActionResultType doorBehavior(BlockState state, World level, BlockPos pos, PlayerEntity player, BlockRayTraceResult hitresult) {
        TileEntity tileEntity = level.getTileEntity(pos);
        if (tileEntity instanceof LockableFrameTile) {
            LockableFrameTile doorTileEntity = (LockableFrameTile) tileEntity;
            if (doorTileEntity.canBeOpenedByPlayers()) {
                super.onBlockActivated(state, level, pos, player, Hand.MAIN_HAND, hitresult);
                level.playEvent(null, state.get(OPEN) ? 1012 : 1006, pos, 0);
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
                LockableFrameTile doorTileEntity = (LockableFrameTile) tileEntity;
                LockableFrameTile secondTile = (LockableFrameTile) (state.get(HALF) == DoubleBlockHalf.LOWER ? level.getTileEntity(pos.up()) : level.getTileEntity(pos.down()));
                if (doorTileEntity.canBeOpenedByRedstoneSignal()) {
                    player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.redstone_off"), true);
                } else {
                    player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.redstone_on"), true);
                }
                doorTileEntity.setCanBeOpenedByRedstoneSignal(!doorTileEntity.canBeOpenedByRedstoneSignal());
                if (secondTile != null) {
                    secondTile.setCanBeOpenedByRedstoneSignal(!secondTile.canBeOpenedByRedstoneSignal());
                }
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
                LockableFrameTile doorTileEntity = (LockableFrameTile) tileEntity;
                LockableFrameTile secondTile = (LockableFrameTile) (state.get(HALF) == DoubleBlockHalf.LOWER ? level.getTileEntity(pos.up()) : level.getTileEntity(pos.down()));
                if (doorTileEntity.canBeOpenedByPlayers()) {
                    player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.lock"), true);
                } else {
                    player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.unlock"), true);
                }
                doorTileEntity.setCanBeOpenedByPlayers(!doorTileEntity.canBeOpenedByPlayers());
                if (secondTile != null) {
                    secondTile.setCanBeOpenedByPlayers(!secondTile.canBeOpenedByPlayers());
                }
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
            LockableFrameTile doorTileEntity = (LockableFrameTile) tileEntity;
            if (doorTileEntity.canBeOpenedByRedstoneSignal())
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

    @Override
    public boolean executeModifications(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        return BlockAppearanceHelper.setAll(itemStack, state, level, pos, player) || getTile(level, pos) != null && BlockModificationHelper.setAll(itemStack, getTile(level, pos), player, true, false);
    }
}
//========SOLI DEO GLORIA========//