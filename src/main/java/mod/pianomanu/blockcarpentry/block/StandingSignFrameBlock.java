package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.tileentity.SignFrameTile;
import mod.pianomanu.blockcarpentry.util.BCWoodType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResultType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockRayTraceResult;

/**
 * Main class for standing frame signs - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.0 10/02/23
 */
public class StandingSignFrameBlock extends StandingSignBlock implements IFrameBlock {

    public StandingSignFrameBlock(Properties properties) {
        super(properties, WoodType.OAK);
        this.setDefaultState(this.stateContainer.getBaseState().with(CONTAINS_BLOCK, Boolean.FALSE).with(LIGHT_LEVEL, 0).with(AbstractSignBlock.WATERLOGGED, false));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SignFrameTile();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitresult) {
        return frameUse(state, level, pos, player, hand, hitresult);
    }

    public ActionResultType frameUseServer(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack, BlockRayTraceResult hitresult) {
        if (removeBlock(level, pos, state, itemStack, player))
            return ActionResultType.SUCCESS;
        if (state.get(CONTAINS_BLOCK)) {
            if (executeModifications(state, level, pos, player, itemStack))
                return ActionResultType.CONSUME;
            super.onBlockActivated(state, level, pos, player, Hand.MAIN_HAND, hitresult);
        }
        if (itemStack.getItem() instanceof BlockItem) {
            if (changeMimic(state, level, pos, player, itemStack))
                return ActionResultType.SUCCESS;
        }
        return itemStack.getItem() instanceof BlockItem ? ActionResultType.PASS : ActionResultType.CONSUME;
    }

    public void fillTileEntity(World level, BlockPos pos, BlockState state, BlockState handBlock, TileEntity TileEntity) {
        SignFrameTile signFrameTile = (SignFrameTile) TileEntity;
        signFrameTile.clear();
        signFrameTile.setMimic(handBlock);
        level.setBlockState(pos, state.with(CONTAINS_BLOCK, Boolean.TRUE), 2);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader level, BlockPos pos) {
        return IFrameBlock.getLightValue(state);
    }

    @Override
    public boolean isCorrectTileInstance(TileEntity TileEntity) {
        return TileEntity instanceof SignFrameTile;
    }

    public void clearTile(World level, BlockPos pos) {
        if (!level.isRemote) {
            TileEntity tileEntity = level.getTileEntity(pos);
            if (tileEntity instanceof SignFrameTile) {
                SignFrameTile frameTileEntity = (SignFrameTile) tileEntity;
                BlockState blockState = frameTileEntity.getMimic();
                if (!(blockState == null)) {
                    frameTileEntity.clear();
                }
            }
        }
    }

    public void dropContainedBlock(World level, BlockPos pos) {
        if (!level.isRemote) {
            TileEntity tileEntity = level.getTileEntity(pos);
            if (tileEntity instanceof SignFrameTile) {
                SignFrameTile frameTileEntity = (SignFrameTile) tileEntity;
                BlockState blockState = frameTileEntity.getMimic();
                if (!(blockState == null)) {
                    dropItemStackInWorld(level, pos, blockState);
                    frameTileEntity.clear();
                }
            }
        }
    }
}
//========SOLI DEO GLORIA========//