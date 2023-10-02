package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.tileentity.SignFrameTile;
import mod.pianomanu.blockcarpentry.util.BCWoodType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Main class for wall frame signs - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.0 10/02/23
 */
public class WallSignFrameBlock extends WallSignBlock implements IFrameBlock {
    public WallSignFrameBlock(Properties properties) {
        super(properties, BCWoodType.FRAME);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONTAINS_BLOCK, Boolean.FALSE).setValue(LIGHT_LEVEL, 0).setValue(SignBlock.WATERLOGGED, false));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SignFrameTile(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitresult) {
        return frameUse(state, level, pos, player, hand, hitresult);
    }

    public InteractionResult frameUseServer(BlockState state, Level level, BlockPos pos, Player player, ItemStack itemStack, BlockHitResult hitresult) {
        if (removeBlock(level, pos, state, itemStack, player))
            return InteractionResult.SUCCESS;
        if (state.getValue(CONTAINS_BLOCK)) {
            if (executeModifications(state, level, pos, player, itemStack) || executeSignFunctions(level, pos, player, itemStack))
                return InteractionResult.CONSUME;
            super.use(state, level, pos, player, InteractionHand.MAIN_HAND, hitresult);
        }
        if (itemStack.getItem() instanceof BlockItem) {
            if (changeMimic(state, level, pos, player, itemStack))
                return InteractionResult.SUCCESS;
        }
        return itemStack.getItem() instanceof BlockItem ? InteractionResult.PASS : InteractionResult.CONSUME;
    }

    public void fillBlockEntity(Level levelIn, BlockPos pos, BlockState state, BlockState handBlock, BlockEntity blockEntity) {
        SignFrameTile signFrameTile = (SignFrameTile) blockEntity;
        signFrameTile.clear();
        signFrameTile.setMimic(handBlock);
        levelIn.setBlock(pos, state.setValue(CONTAINS_BLOCK, Boolean.TRUE), 2);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return IFrameBlock.getLightEmission(state);
    }

    @Override
    public boolean isCorrectTileInstance(BlockEntity blockEntity) {
        return blockEntity instanceof SignFrameTile;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    private boolean executeSignFunctions(Level level, BlockPos pos, Player player, ItemStack itemStack) {
        Item item = itemStack.getItem();
        boolean isDye = item instanceof DyeItem;
        boolean isGlowSac = itemStack.is(Items.GLOW_INK_SAC);
        boolean isSac = itemStack.is(Items.INK_SAC);
        boolean canPerformAction = (isGlowSac || isDye || isSac) && player.getAbilities().mayBuild;
        if (!level.isClientSide) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof SignFrameTile tile) {
                boolean hasGlow = tile.hasGlowingText();
                if ((!isGlowSac || !hasGlow) && (!isSac || hasGlow)) {
                    if (canPerformAction) {
                        boolean actionPerformed;
                        if (isGlowSac) {
                            level.playSound((Player) null, pos, SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                            actionPerformed = tile.setHasGlowingText(true);
                            if (player instanceof ServerPlayer) {
                                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, itemStack);
                            }
                        } else if (isSac) {
                            level.playSound((Player) null, pos, SoundEvents.INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                            actionPerformed = tile.setHasGlowingText(false);
                        } else {
                            level.playSound((Player) null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                            actionPerformed = tile.setColor(((DyeItem) item).getDyeColor());
                        }

                        if (actionPerformed) {
                            if (!player.isCreative()) {
                                itemStack.shrink(1);
                            }

                            player.awardStat(Stats.ITEM_USED.get(item));
                            player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.sign_bug"), false);
                        }
                        tile.executeClickCommands((ServerPlayer) player);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
//========SOLI DEO GLORIA========//