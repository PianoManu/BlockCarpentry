package mod.pianomanu.blockcarpentry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

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
        this.registerDefaultState(this.stateDefinition.any().setValue(CONTAINS_BLOCK, Boolean.FALSE).setValue(HALF, Half.BOTTOM).setValue(SHAPE, StairsShape.STRAIGHT).setValue(LIGHT_LEVEL, 0));
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
            shapes[4] = Shapes.or(straightShapesTop[0], straightShapesTop[3]);
            shapes[5] = Shapes.or(straightShapesTop[1], straightShapesTop[0]);
            shapes[6] = Shapes.or(straightShapesTop[2], straightShapesTop[1]);
            shapes[7] = Shapes.or(straightShapesTop[3], straightShapesTop[2]);
            //inner right
            shapes[8] = Shapes.or(straightShapesTop[0], straightShapesTop[1]);
            shapes[9] = Shapes.or(straightShapesTop[1], straightShapesTop[2]);
            shapes[10] = Shapes.or(straightShapesTop[2], straightShapesTop[3]);
            shapes[11] = Shapes.or(straightShapesTop[3], straightShapesTop[0]);
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
            shapes[4] = Shapes.or(straightShapesBottom[0], straightShapesBottom[3]);
            shapes[5] = Shapes.or(straightShapesBottom[1], straightShapesBottom[0]);
            shapes[6] = Shapes.or(straightShapesBottom[2], straightShapesBottom[1]);
            shapes[7] = Shapes.or(straightShapesBottom[3], straightShapesBottom[2]);
            //inner right
            shapes[8] = Shapes.or(straightShapesBottom[0], straightShapesBottom[1]);
            shapes[9] = Shapes.or(straightShapesBottom[1], straightShapesBottom[2]);
            shapes[10] = Shapes.or(straightShapesBottom[2], straightShapesBottom[3]);
            shapes[11] = Shapes.or(straightShapesBottom[3], straightShapesBottom[0]);
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
                        shapes[i] = Block.box(0, i, i, 16, i + 1, 16);
                        //west
                    else if (facing == 1)
                        shapes[i] = Block.box(0, i, 0, 16 - i, i + 1, 16);
                        //north
                    else if (facing == 2)
                        shapes[i] = Block.box(0, i, 0, 16, i + 1, 16 - i);
                        //east
                    else if (facing == 3)
                        shapes[i] = Block.box(i, i, 0, 16, i + 1, 16);
                } else {
                    //south
                    if (facing == 0)
                        shapes[i] = Block.box(0, i, 15 - i, 16, i + 1, 16);
                        //west
                    else if (facing == 1)
                        shapes[i] = Block.box(0, i, 0, i + 1, i + 1, 16);
                        //north
                    else if (facing == 2)
                        shapes[i] = Block.box(0, i, 0, 16, i + 1, i + 1);
                        //east
                    else if (facing == 3)
                        shapes[i] = Block.box(15 - i, i, 0, 16, i + 1, 16);
                }
            }
            straightShapes[facing] = Shapes.or(shapes[0], shapes[1], shapes[2], shapes[3], shapes[4], shapes[5], shapes[6], shapes[7], shapes[8], shapes[9], shapes[10], shapes[11], shapes[12], shapes[13], shapes[14], shapes[15]);
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
                        shapes[i] = Block.box(0, i, i, 16 - i, i + 1, 16);
                        //west
                    else if (facing == 1)
                        shapes[i] = Block.box(0, i, 0, 16 - i, i + 1, 16 - i);
                        //north
                    else if (facing == 2)
                        shapes[i] = Block.box(i, i, 0, 16, i + 1, 16 - i);
                        //east
                    else if (facing == 3)
                        shapes[i] = Block.box(i, i, i, 16, i + 1, 16);
                } else {
                    //south
                    if (facing == 0)
                        shapes[i] = Block.box(0, i, 15 - i, i + 1, i + 1, 16);
                        //west
                    else if (facing == 1)
                        shapes[i] = Block.box(0, i, 0, i + 1, i + 1, i + 1);
                        //north
                    else if (facing == 2)
                        shapes[i] = Block.box(15 - i, i, 0, 16, i + 1, i + 1);
                        //east
                    else if (facing == 3)
                        shapes[i] = Block.box(15 - i, i, 15 - i, 16, i + 1, 16);
                }
            }
            straightShapes[facing] = Shapes.or(shapes[0], shapes[1], shapes[2], shapes[3], shapes[4], shapes[5], shapes[6], shapes[7], shapes[8], shapes[9], shapes[10], shapes[11], shapes[12], shapes[13], shapes[14], shapes[15]);
        }
        return straightShapes;
    }

    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return (state.getValue(HALF) == Half.TOP ? SHAPES_TOP : SHAPES_BOTTOM)[this.getShapeIndex(state)];
    }

    private int getShapeIndex(BlockState state) {
        return state.getValue(SHAPE).ordinal() * 4 + state.getValue(StairBlock.FACING).get2DDataValue();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitresult) {
        return super.use(state, level, pos, player, hand, hitresult);
    }
}
//========SOLI DEO GLORIA========//