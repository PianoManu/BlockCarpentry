package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.tileentity.BedFrameTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockModificationHelper;
import net.minecraft.block.BlockRenderType;
import net.minecraft.item.DyeColor;
import net.minecraft.state.properties.BedPart;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResultType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.state.*;
import net.minecraft.util.math.BlockRayTraceResult;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Main class for frame beds - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.7 10/07/23
 */
public class BedFrameBlock extends BedBlock implements IFrameBlock {
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BedFrameBlock(DyeColor colorIn, Properties properties) {
        super(colorIn, properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(CONTAINS_BLOCK, false).with(LIGHT_LEVEL, 0).with(PART, BedPart.FOOT).with(OCCUPIED, Boolean.valueOf(false)).with(HORIZONTAL_FACING, Direction.NORTH));//.with(TEXTURE,0));
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
        return new BedFrameTile();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitresult) {
        if (hand == Hand.MAIN_HAND) {
            ItemStack itemStack = player.getHeldItem(hand);
            if (!level.isRemote) {
                if (shouldCallFrameUse(state, itemStack)) {
                    return frameUseServer(state, level, pos, player, itemStack, hitresult);
                }
                super.onBlockActivated(state, level, pos, player, hand, hitresult);
            }
            return frameUseClient(state, level, pos, player, itemStack, hitresult);
        }
        return ActionResultType.FAIL;
    }

    @Override
    public boolean executeModifications(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        return BlockAppearanceHelper.setAll(itemStack, state, level, pos, player) || getBedTile(level, pos) != null && BlockModificationHelper.setAll(itemStack, Objects.requireNonNull(getBedTile(level, pos)), player, false, false, false);
    }

    private BedFrameTile getBedTile(IBlockReader level, BlockPos pos) {
        TileEntity be = level.getTileEntity(pos);
        if (be instanceof BedFrameTile) {
            return (BedFrameTile) be;
        }
        return null;
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
    public void dropContainedBlock(World level, BlockPos pos) {
        if (!level.isRemote) {
            TileEntity TileEntity = level.getTileEntity(pos);
            if (TileEntity instanceof BedFrameTile) {
                BedFrameTile bedFrameTile = (BedFrameTile) TileEntity;
                BlockState blockState = bedFrameTile.getMimic();
                if (!(blockState == null)) {
                    dropItemStackInWorld(level, pos, blockState);
                    bedFrameTile.clear();
                }
            }
        }
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader level, BlockPos pos) {
        return IFrameBlock.getLightValue(state);
    }

    @Override
    public boolean isCorrectTileInstance(TileEntity TileEntity) {
        return TileEntity instanceof BedFrameTile;
    }

    public void fillTileEntity(World level, BlockPos pos, BlockState state, BlockState handBlock, TileEntity TileEntity) {
        BedFrameTile frameTileEntity = (BedFrameTile) TileEntity;
        frameTileEntity.clear();
        frameTileEntity.setMimic(handBlock);
        level.setBlockState(pos, state.with(CONTAINS_BLOCK, Boolean.TRUE), 2);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
//========SOLI DEO GLORIA========//