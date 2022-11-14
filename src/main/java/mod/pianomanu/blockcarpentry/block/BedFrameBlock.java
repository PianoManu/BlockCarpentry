package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.tileentity.BedFrameTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

/**
 * Main class for frame beds - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.4 11/14/22
 */
public class BedFrameBlock extends BedBlock implements IFrameBlock {
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BedFrameBlock(DyeColor colorIn, Properties properties) {
        super(colorIn, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONTAINS_BLOCK, false).setValue(LIGHT_LEVEL, 0).setValue(PART, BedPart.FOOT).setValue(OCCUPIED, Boolean.valueOf(false)).setValue(HORIZONTAL_FACING, Direction.NORTH));//.setValue(TEXTURE,0));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BedFrameTile(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitresult) {
        if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (shouldCallFrameUse(state, itemStack)) {
                return frameUse(state, level, pos, player, hand, hitresult);
            }
            super.use(state, level, pos, player, hand, hitresult);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    @SuppressWarnings("deprecation")
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

    @Override
    public boolean isCorrectTileInstance(BlockEntity blockEntity) {
        return blockEntity instanceof BedFrameTile;
    }

    public void fillBlockEntity(Level levelIn, BlockPos pos, BlockState state, BlockState handBlock, BlockEntity blockEntity) {
        BedFrameTile frameBlockEntity = (BedFrameTile) blockEntity;
        frameBlockEntity.clear();
        frameBlockEntity.setMimic(handBlock);
        levelIn.setBlock(pos, state.setValue(CONTAINS_BLOCK, Boolean.TRUE), 2);
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
//========SOLI DEO GLORIA========//