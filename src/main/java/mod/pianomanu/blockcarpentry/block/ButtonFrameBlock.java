package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.WoodButtonBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

/**
 * Main class for frame buttons - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.5 11/14/22
 */
public class ButtonFrameBlock extends WoodButtonBlock implements EntityBlock, IFrameBlock {
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;

    public ButtonFrameBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CONTAINS_BLOCK, false).setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(POWERED, false).setValue(FACE, AttachFace.WALL).setValue(LIGHT_LEVEL, 0));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FrameBlockTile(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitresult) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (shouldCallFrameUse(state, itemStack)) {
                return frameUse(state, level, pos, player, hand, hitresult);
            }
            return super.use(state, level, pos, player, hand, hitresult);
        }
        return InteractionResult.SUCCESS;
    }

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