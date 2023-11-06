package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.item.ChiselItem;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.CornerUtils;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.*;
import net.minecraft.block.Block;
import net.minecraftforge.common.property.Properties;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraftforge.common.extensions.IForgeBlockState;

import javax.annotation.Nullable;


/**
 * Main class for frameblocks - all important block info can be found here
 * This class is the most basic one for all frame blocks, so you can find most of the documentation here
 *
 * @author PianoManu
 * @version 1.5 11/03/23
 */
@SuppressWarnings("deprecation")
public class FrameBlock extends AbstractFrameBlock implements IForgeBlockState, IWaterLoggable, IFrameBlock {

    /**
     * classic constructor, all default values are set
     *
     * @param properties determined when registering the block (see {@link Registration}
     */
    public FrameBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(CONTAINS_BLOCK, Boolean.FALSE).with(LIGHT_LEVEL, 0).with(WATERLOGGED, false));
    }

    /**
     * Assign needed blockstates to frame block - we need "contains_block" and "light_level", both because we have to check for blockstate changes
     */
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    /**
     * When placed, this method is called and a new FrameBlockTile is created
     * This is needed to store a block inside the frame, change its light value etc.
     *
     * @param state regardless of its state, we always create the TileEntity
     * @return the new empty FrameBlock-TileEntity
     */
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FrameBlockTile();
    }

    /**
     * This method is called, whenever the state of the block changes (e.g. the block is harvested)
     *
     * @param state    old blockstate
     * @param level    level of the block
     * @param pos      block position
     * @param newState new blockstate
     * @param isMoving whether the block has some sort of motion (should never be moving - false)
     */
    @Override
    public void onReplaced(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(level, pos);

            super.onReplaced(state, level, pos, newState, isMoving);
        }
        if (state.get(WATERLOGGED)) {
            level.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(level));
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitresult) {
        if (player.getHeldItem(hand).getItem() instanceof ChiselItem) {
            ChiselItem chiselItem = (ChiselItem) player.getHeldItem(hand).getItem();
            CornerUtils.changeBoxSize(state, level, pos, player, hitresult.getHitVec(), hitresult.getFace(), chiselItem.shouldShrink());
        }
        updateShape(state, level, pos);
        return super.onBlockActivated(state, level, pos, player, hand, hitresult);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        FluidState fluidstate = context.getWorld().getFluidState(blockpos);
        if (fluidstate.getFluid() == Fluids.WATER) {
            return this.getDefaultState().with(WATERLOGGED, fluidstate.isSource());
        } else {
            return this.getDefaultState();
        }
    }

    @Override
    public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        if (state.get(CONTAINS_BLOCK) && !context.hasItem(Registration.CHISEL.get())) {
            return this.getShape(state, reader, pos);
        }
        return VoxelShapes.fullCube();
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        updateShape(stateIn, worldIn, currentPos);

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader getter, BlockPos pos, ISelectionContext context) {
        if (context.hasItem(Registration.CHISEL.get()))
            return VoxelShapes.fullCube();
        return this.getShape(state, getter, pos);
    }

    private VoxelShape getShape(BlockState state, IBlockReader getter, BlockPos pos) {
        if (state.get(CONTAINS_BLOCK) && BCModConfig.SHOW_COMPLEX_BOUNDING_BOX.get()) {
            TileEntity be = getter.getTileEntity(pos);
            if (be instanceof FrameBlockTile) {
                return ((FrameBlockTile) be).getShape();
            }
        }
        return VoxelShapes.fullCube();
    }

    private void updateShape(BlockState state, IWorld level, BlockPos pos) {
        if (state.get(CONTAINS_BLOCK) && BCModConfig.SHOW_COMPLEX_BOUNDING_BOX.get()) {
            TileEntity be = level.getTileEntity(pos);
            if (be instanceof FrameBlockTile) {
                ((FrameBlockTile) be).updateShape();
            }
        }
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return this.getShape(state, worldIn, pos);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return this.getShape(state, reader, pos);
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return this.getShape(state, worldIn, pos);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader blockGetter, BlockPos pos, ISelectionContext context) {
        return this.getShape(state, blockGetter, pos);
    }
}
//========SOLI DEO GLORIA========//