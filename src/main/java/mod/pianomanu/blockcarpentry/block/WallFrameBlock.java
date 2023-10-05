package mod.pianomanu.blockcarpentry.block;

import com.google.common.collect.ImmutableMap;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockModificationHelper;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WallHeight;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResultType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Map;


/**
 * Main class for frame walls - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.5 09/27/23
 */
public class WallFrameBlock extends WallBlock implements IFrameBlock {

    private final Map<BlockState, VoxelShape> stateToShapeMap;
    private final Map<BlockState, VoxelShape> stateToCollisionShapeMap;

    public WallFrameBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(CONTAINS_BLOCK, false));
        this.stateToShapeMap = this.func_235624_a_(4.0F, 3.0F, 16.0F, 0.0F, 14.0F, 16.0F);
        this.stateToCollisionShapeMap = this.func_235624_a_(4.0F, 3.0F, 24.0F, 0.0F, 24.0F, 24.0F);
    }

    private static VoxelShape func_235631_a_(VoxelShape p_235631_0_, WallHeight p_235631_1_, VoxelShape p_235631_2_, VoxelShape p_235631_3_) {
        if (p_235631_1_ == WallHeight.TALL) {
            return VoxelShapes.or(p_235631_0_, p_235631_3_);
        } else {
            return p_235631_1_ == WallHeight.LOW ? VoxelShapes.or(p_235631_0_, p_235631_2_) : p_235631_0_;
        }
    }

    private Map<BlockState, VoxelShape> func_235624_a_(float p_235624_1_, float p_235624_2_, float p_235624_3_, float p_235624_4_, float p_235624_5_, float p_235624_6_) {
        float f = 8.0F - p_235624_1_;
        float f1 = 8.0F + p_235624_1_;
        float f2 = 8.0F - p_235624_2_;
        float f3 = 8.0F + p_235624_2_;
        VoxelShape voxelshape = Block.makeCuboidShape(f, 0.0D, f, f1, p_235624_3_, f1);
        VoxelShape voxelshape1 = Block.makeCuboidShape(f2, p_235624_4_, 0.0D, f3, p_235624_5_, f3);
        VoxelShape voxelshape2 = Block.makeCuboidShape(f2, p_235624_4_, f2, f3, p_235624_5_, 16.0D);
        VoxelShape voxelshape3 = Block.makeCuboidShape(0.0D, p_235624_4_, f2, f3, p_235624_5_, f3);
        VoxelShape voxelshape4 = Block.makeCuboidShape(f2, p_235624_4_, f2, 16.0D, p_235624_5_, f3);
        VoxelShape voxelshape5 = Block.makeCuboidShape(f2, p_235624_4_, 0.0D, f3, p_235624_6_, f3);
        VoxelShape voxelshape6 = Block.makeCuboidShape(f2, p_235624_4_, f2, f3, p_235624_6_, 16.0D);
        VoxelShape voxelshape7 = Block.makeCuboidShape(0.0D, p_235624_4_, f2, f3, p_235624_6_, f3);
        VoxelShape voxelshape8 = Block.makeCuboidShape(f2, p_235624_4_, f2, 16.0D, p_235624_6_, f3);
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        for (int lightlevel = 0; lightlevel < 16; lightlevel++) {
            for (Boolean obool : UP.getAllowedValues()) {
                for (WallHeight wallheight : WALL_HEIGHT_EAST.getAllowedValues()) {
                    for (WallHeight wallheight1 : WALL_HEIGHT_NORTH.getAllowedValues()) {
                        for (WallHeight wallheight2 : WALL_HEIGHT_WEST.getAllowedValues()) {
                            for (WallHeight wallheight3 : WALL_HEIGHT_SOUTH.getAllowedValues()) {
                                VoxelShape voxelshape9 = VoxelShapes.empty();
                                voxelshape9 = func_235631_a_(voxelshape9, wallheight, voxelshape4, voxelshape8);
                                voxelshape9 = func_235631_a_(voxelshape9, wallheight2, voxelshape3, voxelshape7);
                                voxelshape9 = func_235631_a_(voxelshape9, wallheight1, voxelshape1, voxelshape5);
                                voxelshape9 = func_235631_a_(voxelshape9, wallheight3, voxelshape2, voxelshape6);
                                if (obool) {
                                    voxelshape9 = VoxelShapes.or(voxelshape9, voxelshape);
                                }

                                BlockState blockstate = this.getDefaultState().with(UP, obool).with(WALL_HEIGHT_EAST, wallheight).with(WALL_HEIGHT_WEST, wallheight2).with(WALL_HEIGHT_NORTH, wallheight1).with(WALL_HEIGHT_SOUTH, wallheight3);
                                builder.put(blockstate.with(WallBlock.WATERLOGGED, Boolean.valueOf(false)).with(CONTAINS_BLOCK, false).with(LIGHT_LEVEL, lightlevel), voxelshape9);
                                builder.put(blockstate.with(WallBlock.WATERLOGGED, Boolean.valueOf(true)).with(CONTAINS_BLOCK, false).with(LIGHT_LEVEL, lightlevel), voxelshape9);
                                builder.put(blockstate.with(WallBlock.WATERLOGGED, Boolean.valueOf(false)).with(CONTAINS_BLOCK, true).with(LIGHT_LEVEL, lightlevel), voxelshape9);
                                builder.put(blockstate.with(WallBlock.WATERLOGGED, Boolean.valueOf(true)).with(CONTAINS_BLOCK, true).with(LIGHT_LEVEL, lightlevel), voxelshape9);

                            }
                        }
                    }
                }
            }
        }

        return builder.build();
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.stateToCollisionShapeMap.get(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.stateToShapeMap.get(state);
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
        return frameUse(state, level, pos, player, hand, hitresult);
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
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        return IFrameBlock.super.canSustainPlant(world, pos, facing);
    }

    @Override
    public boolean executeModifications(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        return BlockAppearanceHelper.setAll(itemStack, state, level, pos, player) || getTile(level, pos) != null && BlockModificationHelper.setAll(itemStack, getTile(level, pos), player, true, false);
    }
}
//========SOLI DEO GLORIA========//
