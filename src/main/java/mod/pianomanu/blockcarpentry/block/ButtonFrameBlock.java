package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.item.BaseFrameItem;
import mod.pianomanu.blockcarpentry.item.BaseIllusionItem;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockSavingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.WoodButtonBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

import static mod.pianomanu.blockcarpentry.util.BCBlockStateProperties.LIGHT_LEVEL;

/**
 * Main class for frame buttons - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.3 11/07/22
 */
public class ButtonFrameBlock extends WoodButtonBlock implements EntityBlock {
    public static final BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;

    public ButtonFrameBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CONTAINS_BLOCK, false).setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(POWERED, false).setValue(FACE, AttachFace.WALL).setValue(LIGHT_LEVEL, 0));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONTAINS_BLOCK).add(BlockStateProperties.HORIZONTAL_FACING).add(POWERED).add(FACE).add(LIGHT_LEVEL);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FrameBlockTile(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitresult) {
        ItemStack item = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (removeBlock(level, pos, state, item, player))
                return InteractionResult.CONSUME;
            if (BlockAppearanceHelper.setLightLevel(item, state, level, pos, player, hand) ||
                    BlockAppearanceHelper.setTexture(item, state, level, player, pos) ||
                    BlockAppearanceHelper.setDesign(level, pos, player, item) ||
                    BlockAppearanceHelper.setDesignTexture(level, pos, player, item) ||
                    BlockAppearanceHelper.setOverlay(level, pos, player, item) ||
                    BlockAppearanceHelper.setRotation(level, pos, player, item))
                return InteractionResult.CONSUME;
            //TODO clean up
            if (!state.getValue(CONTAINS_BLOCK)) {
                if (item.getItem() instanceof BlockItem) {
                    if (item.getItem() instanceof BaseFrameItem || item.getItem() instanceof BaseIllusionItem) {
                        return InteractionResult.PASS;
                    }
                    BlockEntity tileEntity = level.getBlockEntity(pos);
                    int count = player.getItemInHand(hand).getCount();
                    if (tileEntity instanceof FrameBlockTile && !item.isEmpty() && BlockSavingHelper.isValidBlock(((BlockItem) item.getItem()).getBlock()) && !state.getValue(CONTAINS_BLOCK)) {
                        ((FrameBlockTile) tileEntity).clear();
                        BlockState handBlockState = ((BlockItem) item.getItem()).getBlock().defaultBlockState();
                        ((FrameBlockTile) tileEntity).setMimic(handBlockState);
                        insertBlock(level, pos, state, handBlockState);
                        if (!player.isCreative())
                            player.getItemInHand(hand).setCount(count - 1);
                        return InteractionResult.CONSUME;
                    }
                }
            }
            if (state.getValue(POWERED)) {
                return InteractionResult.CONSUME;
            } else {
                this.press(state, level, pos);
                level.playSound(null, pos, SoundEvents.WOODEN_BUTTON_CLICK_ON, SoundSource.BLOCKS, 1f, 1f);
                level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private boolean removeBlock(Level level, BlockPos pos, BlockState state, ItemStack itemStack, Player player) {
        if (itemStack.getItem() == Registration.HAMMER.get() || (!BCModConfig.HAMMER_NEEDED.get() && player.isCrouching())) {
            if (!player.isCreative())
                this.dropContainedBlock(level, pos);
            state = state.setValue(CONTAINS_BLOCK, Boolean.FALSE);
            level.setBlock(pos, state, 2);
            return true;
        }
        return false;
    }

    private void dropContainedBlock(Level levelIn, BlockPos pos) {
        if (!levelIn.isClientSide) {
            BlockEntity tileentity = levelIn.getBlockEntity(pos);
            if (tileentity instanceof FrameBlockTile) {
                FrameBlockTile frameBlockEntity = (FrameBlockTile) tileentity;
                BlockState blockState = frameBlockEntity.getMimic();
                if (!(blockState == null)) {
                    levelIn.levelEvent(1010, pos, 0);
                    frameBlockEntity.clear();
                    float f = 0.7F;
                    double d0 = (double) (levelIn.random.nextFloat() * 0.7F) + (double) 0.15F;
                    double d1 = (levelIn.random.nextFloat() * 0.7F) + (double) 0.060000002F + 0.6D;
                    double d2 = (double) (levelIn.random.nextFloat() * 0.7F) + (double) 0.15F;
                    ItemStack itemstack1 = blockState.getBlock().asItem().getDefaultInstance();
                    ItemEntity itementity = new ItemEntity(levelIn, (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, itemstack1);
                    itementity.setDefaultPickUpDelay();
                    levelIn.addFreshEntity(itementity);
                    frameBlockEntity.clear();
                }
            }
        }
    }

    public void insertBlock(Level levelIn, BlockPos pos, BlockState state, BlockState handBlock) {
        BlockEntity tileentity = levelIn.getBlockEntity(pos);
        if (tileentity instanceof FrameBlockTile) {
            FrameBlockTile frameBlockEntity = (FrameBlockTile) tileentity;
            frameBlockEntity.clear();
            frameBlockEntity.setMimic(handBlock);
            levelIn.setBlock(pos, state.setValue(CONTAINS_BLOCK, Boolean.TRUE), 2);
        }
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
        if (state.getValue(LIGHT_LEVEL) > 15) {
            return 15;
        }
        return state.getValue(LIGHT_LEVEL);
    }
}
//========SOLI DEO GLORIA========//