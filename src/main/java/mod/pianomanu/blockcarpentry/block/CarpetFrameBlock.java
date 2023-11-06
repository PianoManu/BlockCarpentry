package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockModificationHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.CarpetBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.common.IPlantable;

/**
 * Main class for frame carpets - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.5 10/07/23
 */
public class CarpetFrameBlock extends CarpetBlock implements IFrameBlock {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    /**
     * classic constructor, all default values are set
     *
     * @param properties determined when registering the block (see {@link Registration}
     */
    public CarpetFrameBlock(Properties properties) {
        super(DyeColor.BLACK, properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(CONTAINS_BLOCK, Boolean.FALSE).with(LIGHT_LEVEL, 0));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitresult) {
        return frameUse(state, level, pos, player, hand, hitresult);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader getter, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, World levelIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(levelIn, pos);

            super.onReplaced(state, levelIn, pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FrameBlockTile();
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
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
