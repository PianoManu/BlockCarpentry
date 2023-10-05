package mod.pianomanu.blockcarpentry.block;

import net.minecraft.block.StairsBlock;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResultType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;

import java.util.function.Supplier;

/**
 * Main class for frame slopes - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.2 09/27/23
 */
public class SlopeFrameBlock extends StairsFrameBlock {
    private static final VoxelShape[] SHAPES_BOTTOM = makeShapes(false);
    private static final VoxelShape[] SHAPES_TOP = makeShapes(true);

    public SlopeFrameBlock(Supplier<BlockState> state, Properties properties) {
        super(state, properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(CONTAINS_BLOCK, Boolean.FALSE).with(HALF, Half.BOTTOM).with(SHAPE, StairsShape.STRAIGHT).with(LIGHT_LEVEL, 0));
    }

    private static VoxelShape[] makeShapes(boolean isTop) {
        VoxelShape[] shapes = new VoxelShape[20];
        VoxelShape[] straightShapesTop = createStraightShapes(true);
        VoxelShape[] straightShapesBottom = createStraightShapes(false);
        VoxelShape[] outerShapesTop = createOuterShapes(true);
        VoxelShape[] outerShapesBottom = createOuterShapes(false);
        if (isTop) {
            //straight south
            shapes[0] = straightShapesTop[0];
            //straight west
            shapes[1] = straightShapesTop[1];
            //straight north
            shapes[2] = straightShapesTop[2];
            //straight east
            shapes[3] = straightShapesTop[3];
            //inner left
            shapes[4] = VoxelShapes.or(straightShapesTop[0], straightShapesTop[3]);
            shapes[5] = VoxelShapes.or(straightShapesTop[1], straightShapesTop[0]);
            shapes[6] = VoxelShapes.or(straightShapesTop[2], straightShapesTop[1]);
            shapes[7] = VoxelShapes.or(straightShapesTop[3], straightShapesTop[2]);
            //inner right
            shapes[8] = VoxelShapes.or(straightShapesTop[0], straightShapesTop[1]);
            shapes[9] = VoxelShapes.or(straightShapesTop[1], straightShapesTop[2]);
            shapes[10] = VoxelShapes.or(straightShapesTop[2], straightShapesTop[3]);
            shapes[11] = VoxelShapes.or(straightShapesTop[3], straightShapesTop[0]);
            //outer left
            shapes[12] = outerShapesTop[3];
            shapes[13] = outerShapesTop[0];
            shapes[14] = outerShapesTop[1];
            shapes[15] = outerShapesTop[2];
            //outer right
            shapes[16] = outerShapesTop[0];
            shapes[17] = outerShapesTop[1];
            shapes[18] = outerShapesTop[2];
            shapes[19] = outerShapesTop[3];
        } else {
            //straight south
            shapes[0] = straightShapesBottom[0];
            //straight west
            shapes[1] = straightShapesBottom[1];
            //straight north
            shapes[2] = straightShapesBottom[2];
            //straight east
            shapes[3] = straightShapesBottom[3];
            //inner left
            shapes[4] = VoxelShapes.or(straightShapesBottom[0], straightShapesBottom[3]);
            shapes[5] = VoxelShapes.or(straightShapesBottom[1], straightShapesBottom[0]);
            shapes[6] = VoxelShapes.or(straightShapesBottom[2], straightShapesBottom[1]);
            shapes[7] = VoxelShapes.or(straightShapesBottom[3], straightShapesBottom[2]);
            //inner right
            shapes[8] = VoxelShapes.or(straightShapesBottom[0], straightShapesBottom[1]);
            shapes[9] = VoxelShapes.or(straightShapesBottom[1], straightShapesBottom[2]);
            shapes[10] = VoxelShapes.or(straightShapesBottom[2], straightShapesBottom[3]);
            shapes[11] = VoxelShapes.or(straightShapesBottom[3], straightShapesBottom[0]);
            //outer left
            shapes[12] = outerShapesBottom[3];
            shapes[13] = outerShapesBottom[0];
            shapes[14] = outerShapesBottom[1];
            shapes[15] = outerShapesBottom[2];
            //outer right
            shapes[16] = outerShapesBottom[0];
            shapes[17] = outerShapesBottom[1];
            shapes[18] = outerShapesBottom[2];
            shapes[19] = outerShapesBottom[3];
        }
        return shapes;
    }

    private static VoxelShape[] createStraightShapes(boolean isTop) {
        VoxelShape[] shapes = new VoxelShape[16];
        VoxelShape[] straightShapes = new VoxelShape[4];
        for (int facing = 0; facing < 4; facing++) {
            for (int i = 0; i < 16; i++) {
                if (!isTop) {
                    //south
                    if (facing == 0)
                        shapes[i] = Block.makeCuboidShape(0, i, i, 16, i + 1, 16);
                        //west
                    else if (facing == 1)
                        shapes[i] = Block.makeCuboidShape(0, i, 0, 16 - i, i + 1, 16);
                        //north
                    else if (facing == 2)
                        shapes[i] = Block.makeCuboidShape(0, i, 0, 16, i + 1, 16 - i);
                        //east
                    else if (facing == 3)
                        shapes[i] = Block.makeCuboidShape(i, i, 0, 16, i + 1, 16);
                } else {
                    //south
                    if (facing == 0)
                        shapes[i] = Block.makeCuboidShape(0, i, 15 - i, 16, i + 1, 16);
                        //west
                    else if (facing == 1)
                        shapes[i] = Block.makeCuboidShape(0, i, 0, i + 1, i + 1, 16);
                        //north
                    else if (facing == 2)
                        shapes[i] = Block.makeCuboidShape(0, i, 0, 16, i + 1, i + 1);
                        //east
                    else if (facing == 3)
                        shapes[i] = Block.makeCuboidShape(15 - i, i, 0, 16, i + 1, 16);
                }
            }
            straightShapes[facing] = VoxelShapes.or(shapes[0], shapes[1], shapes[2], shapes[3], shapes[4], shapes[5], shapes[6], shapes[7], shapes[8], shapes[9], shapes[10], shapes[11], shapes[12], shapes[13], shapes[14], shapes[15]);
        }
        return straightShapes;
    }

    private static VoxelShape[] createOuterShapes(boolean isTop) {
        VoxelShape[] shapes = new VoxelShape[16];
        VoxelShape[] straightShapes = new VoxelShape[4];
        for (int facing = 0; facing < 4; facing++) {
            for (int i = 0; i < 16; i++) {
                if (!isTop) {
                    //south
                    if (facing == 0)
                        shapes[i] = Block.makeCuboidShape(0, i, i, 16 - i, i + 1, 16);
                        //west
                    else if (facing == 1)
                        shapes[i] = Block.makeCuboidShape(0, i, 0, 16 - i, i + 1, 16 - i);
                        //north
                    else if (facing == 2)
                        shapes[i] = Block.makeCuboidShape(i, i, 0, 16, i + 1, 16 - i);
                        //east
                    else if (facing == 3)
                        shapes[i] = Block.makeCuboidShape(i, i, i, 16, i + 1, 16);
                } else {
                    //south
                    if (facing == 0)
                        shapes[i] = Block.makeCuboidShape(0, i, 15 - i, i + 1, i + 1, 16);
                        //west
                    else if (facing == 1)
                        shapes[i] = Block.makeCuboidShape(0, i, 0, i + 1, i + 1, i + 1);
                        //north
                    else if (facing == 2)
                        shapes[i] = Block.makeCuboidShape(15 - i, i, 0, 16, i + 1, i + 1);
                        //east
                    else if (facing == 3)
                        shapes[i] = Block.makeCuboidShape(15 - i, i, 15 - i, 16, i + 1, 16);
                }
            }
            straightShapes[facing] = VoxelShapes.or(shapes[0], shapes[1], shapes[2], shapes[3], shapes[4], shapes[5], shapes[6], shapes[7], shapes[8], shapes[9], shapes[10], shapes[11], shapes[12], shapes[13], shapes[14], shapes[15]);
        }
        return straightShapes;
    }

    public VoxelShape getShape(BlockState state, IBlockReader IBlockReader, BlockPos pos, ISelectionContext context) {
        return (state.get(HALF) == Half.TOP ? SHAPES_TOP : SHAPES_BOTTOM)[this.getShapeIndex(state)];
    }

    private int getShapeIndex(BlockState state) {
        return state.get(SHAPE).ordinal() * 4 + state.get(StairsBlock.FACING).getHorizontalIndex();
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitresult) {
        return super.onBlockActivated(state, level, pos, player, hand, hitresult);
    }
}
//========SOLI DEO GLORIA========//