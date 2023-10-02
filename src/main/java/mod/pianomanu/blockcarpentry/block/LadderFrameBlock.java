package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockModificationHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Main class for frame ladder plates - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.5 09/27/23
 */
public class LadderFrameBlock extends LadderBlock implements EntityBlock, IFrameBlock {

    public LadderFrameBlock(Properties builder) {
        super(builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONTAINS_BLOCK, false).setValue(LIGHT_LEVEL, 0));
    }

    @Override
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
    @Nonnull
    @SuppressWarnings("deprecation")
    public InteractionResult use(@Nullable BlockState state, Level level, @Nullable BlockPos pos, Player player, @Nullable InteractionHand hand, @Nullable BlockHitResult hitResult) {
        return frameUse(state, level, pos, player, hand, hitResult);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, @Nullable Level levelIn, @Nullable BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(Objects.requireNonNull(levelIn), pos);

            super.onRemove(state, levelIn, Objects.requireNonNull(pos), newState, isMoving);
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return IFrameBlock.getLightEmission(state);
    }

    @Override
    public boolean isLadder(@Nullable BlockState state, @Nullable LevelReader level, @Nullable BlockPos pos, @Nullable LivingEntity entity) {
        return true;
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        return IFrameBlock.super.canSustainPlant(state, world, pos, facing);
    }

    @Override
    public boolean executeModifications(BlockState state, Level level, BlockPos pos, Player player, ItemStack itemStack) {
        return BlockAppearanceHelper.setAll(itemStack, state, level, pos, player) || getTile(level, pos) != null && BlockModificationHelper.setAll(itemStack, getTile(level, pos), player, true, false);
    }
}
//========SOLI DEO GLORIA========//