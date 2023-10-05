package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockModificationHelper;
import net.minecraft.block.WoodButtonBlock;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResultType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;

/**
 * Main class for frame buttons - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.7 09/27/23
 */
public class ButtonFrameBlock extends WoodButtonBlock implements IFrameBlock {
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.FACE;

    public ButtonFrameBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(CONTAINS_BLOCK, false).with(HORIZONTAL_FACING, Direction.NORTH).with(POWERED, false).with(FACE, AttachFace.WALL).with(LIGHT_LEVEL, 0));
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
        return new FrameBlockTile();
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
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        return IFrameBlock.super.canSustainPlant(world, pos, facing);
    }

    @Override
    public boolean executeModifications(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        return BlockAppearanceHelper.setAll(itemStack, state, level, pos, player) || getTile(level, pos) != null && BlockModificationHelper.setAll(itemStack, getTile(level, pos), player, false, false);
    }
}
//========SOLI DEO GLORIA========//