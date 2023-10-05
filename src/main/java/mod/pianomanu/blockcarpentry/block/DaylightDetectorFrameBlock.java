package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.tileentity.DaylightDetectorFrameTileEntity;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockModificationHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DaylightDetectorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Main class for frame carpets - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.6 09/27/23
 */
public class DaylightDetectorFrameBlock extends DaylightDetectorBlock implements IFrameBlock {

    public DaylightDetectorFrameBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(CONTAINS_BLOCK, Boolean.FALSE).with(LIGHT_LEVEL, 0).with(POWER, 0).with(INVERTED, false));
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
        return new DaylightDetectorFrameTileEntity(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, @Nonnull World level, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(level, pos);

            super.onReplaced(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitresult) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (hand == Hand.MAIN_HAND) {
            if (!level.isRemote) {
                if (shouldCallFrameUse(state, itemStack)) {
                    return frameUseServer(state, level, pos, player, itemStack, hitresult);
                }
                return super.onBlockActivated(state, level, pos, player, hand, hitresult);
            }
            return frameUseClient(state, level, pos, player, itemStack, hitresult);
        }
        return ActionResultType.FAIL;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader level, BlockPos pos) {
        return IFrameBlock.getLightValue(state);
    }

    @Override
    public boolean isCorrectTileInstance(TileEntity TileEntity) {
        return TileEntity instanceof DaylightDetectorFrameTileEntity;
    }

    public void fillTileEntity(World level, BlockPos pos, BlockState state, BlockState handBlock, TileEntity TileEntity) {
        DaylightDetectorFrameTileEntity frameTileEntity = (DaylightDetectorFrameTileEntity) TileEntity;
        frameTileEntity.clear();
        frameTileEntity.setMimic(handBlock);
        level.setBlockState(pos, state.with(CONTAINS_BLOCK, Boolean.TRUE), 2);
    }

    @Override
    public boolean executeModifications(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        return BlockAppearanceHelper.setAll(itemStack, state, level, pos, player) || getTile(level, pos) != null && BlockModificationHelper.setAll(itemStack, getTile(level, pos), player, false, false);
    }
}
//========SOLI DEO GLORIA========//