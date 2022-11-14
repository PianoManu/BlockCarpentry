package mod.pianomanu.blockcarpentry.block;

import com.google.common.collect.ImmutableMap;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;


/**
 * Main class for frame walls - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.3 11/14/22
 */
public class WallFrameBlock extends WallBlock implements EntityBlock, IFrameBlock {

    private final Map<BlockState, VoxelShape> stateToShapeMap;
    private final Map<BlockState, VoxelShape> stateToCollisionShapeMap;

    public WallFrameBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONTAINS_BLOCK, false));
        this.stateToShapeMap = this.func_235624_a_(4.0F, 3.0F, 16.0F, 0.0F, 14.0F, 16.0F);
        this.stateToCollisionShapeMap = this.func_235624_a_(4.0F, 3.0F, 24.0F, 0.0F, 24.0F, 24.0F);
    }

    private static VoxelShape func_235631_a_(VoxelShape p_235631_0_, WallSide p_235631_1_, VoxelShape p_235631_2_, VoxelShape p_235631_3_) {
        if (p_235631_1_ == WallSide.TALL) {
            return Shapes.or(p_235631_0_, p_235631_3_);
        } else {
            return p_235631_1_ == WallSide.LOW ? Shapes.or(p_235631_0_, p_235631_2_) : p_235631_0_;
        }
    }

    private Map<BlockState, VoxelShape> func_235624_a_(float p_235624_1_, float p_235624_2_, float p_235624_3_, float p_235624_4_, float p_235624_5_, float p_235624_6_) {
        float f = 8.0F - p_235624_1_;
        float f1 = 8.0F + p_235624_1_;
        float f2 = 8.0F - p_235624_2_;
        float f3 = 8.0F + p_235624_2_;
        VoxelShape voxelshape = Block.box(f, 0.0D, f, f1, p_235624_3_, f1);
        VoxelShape voxelshape1 = Block.box(f2, p_235624_4_, 0.0D, f3, p_235624_5_, f3);
        VoxelShape voxelshape2 = Block.box(f2, p_235624_4_, f2, f3, p_235624_5_, 16.0D);
        VoxelShape voxelshape3 = Block.box(0.0D, p_235624_4_, f2, f3, p_235624_5_, f3);
        VoxelShape voxelshape4 = Block.box(f2, p_235624_4_, f2, 16.0D, p_235624_5_, f3);
        VoxelShape voxelshape5 = Block.box(f2, p_235624_4_, 0.0D, f3, p_235624_6_, f3);
        VoxelShape voxelshape6 = Block.box(f2, p_235624_4_, f2, f3, p_235624_6_, 16.0D);
        VoxelShape voxelshape7 = Block.box(0.0D, p_235624_4_, f2, f3, p_235624_6_, f3);
        VoxelShape voxelshape8 = Block.box(f2, p_235624_4_, f2, 16.0D, p_235624_6_, f3);
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        for (int lightlevel = 0; lightlevel < 16; lightlevel++) {
            for (Boolean obool : UP.getPossibleValues()) {
                for (WallSide wallheight : EAST_WALL.getPossibleValues()) {
                    for (WallSide wallheight1 : NORTH_WALL.getPossibleValues()) {
                        for (WallSide wallheight2 : WEST_WALL.getPossibleValues()) {
                            for (WallSide wallheight3 : SOUTH_WALL.getPossibleValues()) {
                                VoxelShape voxelshape9 = Shapes.empty();
                                voxelshape9 = func_235631_a_(voxelshape9, wallheight, voxelshape4, voxelshape8);
                                voxelshape9 = func_235631_a_(voxelshape9, wallheight2, voxelshape3, voxelshape7);
                                voxelshape9 = func_235631_a_(voxelshape9, wallheight1, voxelshape1, voxelshape5);
                                voxelshape9 = func_235631_a_(voxelshape9, wallheight3, voxelshape2, voxelshape6);
                                if (obool) {
                                    voxelshape9 = Shapes.or(voxelshape9, voxelshape);
                                }

                                BlockState blockstate = this.defaultBlockState().setValue(UP, obool).setValue(EAST_WALL, wallheight).setValue(WEST_WALL, wallheight2).setValue(NORTH_WALL, wallheight1).setValue(SOUTH_WALL, wallheight3);
                                builder.put(blockstate.setValue(WallBlock.WATERLOGGED, Boolean.valueOf(false)).setValue(CONTAINS_BLOCK, false).setValue(LIGHT_LEVEL, lightlevel), voxelshape9);
                                builder.put(blockstate.setValue(WallBlock.WATERLOGGED, Boolean.valueOf(true)).setValue(CONTAINS_BLOCK, false).setValue(LIGHT_LEVEL, lightlevel), voxelshape9);
                                builder.put(blockstate.setValue(WallBlock.WATERLOGGED, Boolean.valueOf(false)).setValue(CONTAINS_BLOCK, true).setValue(LIGHT_LEVEL, lightlevel), voxelshape9);
                                builder.put(blockstate.setValue(WallBlock.WATERLOGGED, Boolean.valueOf(true)).setValue(CONTAINS_BLOCK, true).setValue(LIGHT_LEVEL, lightlevel), voxelshape9);

                            }
                        }
                    }
                }
            }
        }

        return builder.build();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return this.stateToCollisionShapeMap.get(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return this.stateToShapeMap.get(state);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FrameBlockTile(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitresult) {
        ItemStack item = player.getItemInHand(hand);
        if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            return frameUse(state, level, pos, player, hand, hitresult);
        }
        return item.getItem() instanceof BlockItem ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level levelIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(levelIn, pos);

            super.onRemove(state, levelIn, pos, newState, isMoving);
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return IFrameBlock.getLightEmission(state);
    }
}
//========SOLI DEO GLORIA========//
