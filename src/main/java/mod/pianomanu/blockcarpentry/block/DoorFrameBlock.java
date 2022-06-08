package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.tileentity.LockableFrameTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockSavingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Objects;

import static mod.pianomanu.blockcarpentry.util.BCBlockStateProperties.LIGHT_LEVEL;

/**
 * Main class for frame doors - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.1 05/31/22
 */
public class DoorFrameBlock extends DoorBlock implements EntityBlock {
    public static final BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;

    public DoorFrameBlock(Properties builder) {
        super(builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONTAINS_BLOCK, Boolean.FALSE).setValue(FACING, Direction.NORTH).setValue(OPEN, Boolean.valueOf(false)).setValue(HINGE, DoorHingeSide.LEFT).setValue(POWERED, Boolean.valueOf(false)).setValue(HALF, DoubleBlockHalf.LOWER).setValue(LIGHT_LEVEL, 0));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONTAINS_BLOCK, FACING, OPEN, HINGE, POWERED, HALF, LIGHT_LEVEL);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LockableFrameTile(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitresult) {
        ItemStack item = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (BlockAppearanceHelper.setLightLevel(item, state, level, pos, player, hand) ||
                    BlockAppearanceHelper.setTexture(item, state, level, player, pos) ||
                    BlockAppearanceHelper.setDesign(level, pos, player, item) ||
                    BlockAppearanceHelper.setDesignTexture(level, pos, player, item) ||
                    BlockAppearanceHelper.setGlassColor(level, pos, player, hand) ||
                    BlockAppearanceHelper.setOverlay(level, pos, player, item) ||
                    BlockAppearanceHelper.setRotation(level, pos, player, item))
                return InteractionResult.CONSUME;
            if (item.getItem() == Items.REDSTONE) {
                BlockEntity tileEntity = level.getBlockEntity(pos);
                if (tileEntity instanceof LockableFrameTile doorTileEntity) {
                    LockableFrameTile secondTile = (LockableFrameTile) (state.getValue(HALF) == DoubleBlockHalf.LOWER ? level.getBlockEntity(pos.above()) : level.getBlockEntity(pos.below()));
                    if (doorTileEntity.canBeOpenedByRedstoneSignal()) {
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.redstone_off"), true);
                    } else {
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.redstone_on"), true);
                    }
                    doorTileEntity.setCanBeOpenedByRedstoneSignal(!doorTileEntity.canBeOpenedByRedstoneSignal());
                    if (secondTile != null) {
                        secondTile.setCanBeOpenedByRedstoneSignal(!secondTile.canBeOpenedByRedstoneSignal());
                    }
                } else if (tileEntity instanceof FrameBlockTile) {
                    LockableFrameTile newTile = (LockableFrameTile) newBlockEntity(pos, state);
                    if (newTile != null) {
                        newTile.addFromOutdatedTileEntity((FrameBlockTile) tileEntity);
                        level.setBlockEntity(newTile);
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.converting_outdated_block"), true);
                    }
                }
                return InteractionResult.CONSUME;
            }
            if (item.getItem() == Items.IRON_INGOT) {
                BlockEntity tileEntity = level.getBlockEntity(pos);
                if (tileEntity instanceof LockableFrameTile doorTileEntity) {
                    LockableFrameTile secondTile = (LockableFrameTile) (state.getValue(HALF) == DoubleBlockHalf.LOWER ? level.getBlockEntity(pos.above()) : level.getBlockEntity(pos.below()));
                    if (doorTileEntity.canBeOpenedByPlayers()) {
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.lock"), true);
                    } else {
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.unlock"), true);
                    }
                    doorTileEntity.setCanBeOpenedByPlayers(!doorTileEntity.canBeOpenedByPlayers());
                    if (secondTile != null) {
                        secondTile.setCanBeOpenedByPlayers(!secondTile.canBeOpenedByPlayers());
                    }
                } else if (tileEntity instanceof FrameBlockTile) {
                    LockableFrameTile newTile = (LockableFrameTile) newBlockEntity(pos, state);
                    if (newTile != null) {
                        newTile.addFromOutdatedTileEntity((FrameBlockTile) tileEntity);
                        level.setBlockEntity(newTile);
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.converting_outdated_block"), true);
                    }
                }
                return InteractionResult.CONSUME;
            }
            if (item.getItem() instanceof BlockItem) {
                if (Objects.requireNonNull(item.getItem().getRegistryName()).getNamespace().equals(BlockCarpentryMain.MOD_ID)) {
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
                    return InteractionResult.SUCCESS;
                }
            }
            if (!item.getItem().getRegistryName().getNamespace().equals(BlockCarpentryMain.MOD_ID)) {
                BlockEntity tileEntity = level.getBlockEntity(pos);
                if (tileEntity instanceof LockableFrameTile doorTileEntity) {
                    if (doorTileEntity.canBeOpenedByPlayers()) {
                        if (state.getValue(DoorBlock.OPEN)) {
                            state = state.setValue(OPEN, false);
                        } else {
                            state = state.setValue(OPEN, true);
                        }
                        level.setBlock(pos, state, 10);
                        level.levelEvent(null, state.getValue(OPEN) ? 1006 : 1012, pos, 0);
                    }
                } else if (tileEntity instanceof FrameBlockTile) {
                    LockableFrameTile newTile = (LockableFrameTile) newBlockEntity(pos, state);
                    if (newTile != null) {
                        newTile.addFromOutdatedTileEntity((FrameBlockTile) tileEntity);
                        level.setBlockEntity(newTile);
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.converting_outdated_block"), true);
                    }
                }
            }
            if (player.getItemInHand(hand).getItem() == Registration.HAMMER.get() || (!BCModConfig.HAMMER_NEEDED.get() && player.isCrouching())) {
                if (!player.isCreative())
                    this.dropContainedBlock(level, pos);
                state = state.setValue(CONTAINS_BLOCK, Boolean.FALSE);
                level.setBlock(pos, state, 2);
            }
        }
        return InteractionResult.SUCCESS;
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
                    ItemStack itemstack1 = new ItemStack(blockState.getBlock());
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
    @SuppressWarnings("deprecation")
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

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean update) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof LockableFrameTile doorTileEntity && doorTileEntity.canBeOpenedByRedstoneSignal()) {
            boolean shouldOpenNow = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.relative(state.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
            if (!this.defaultBlockState().is(block) && shouldOpenNow != state.getValue(POWERED)) {
                if (shouldOpenNow != state.getValue(OPEN)) {
                    level.playSound(null, pos, shouldOpenNow ? SoundEvents.WOODEN_DOOR_OPEN : SoundEvents.WOODEN_DOOR_CLOSE, SoundSource.BLOCKS, 1f, 1f);
                    level.gameEvent(shouldOpenNow ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                }

                level.setBlock(pos, state.setValue(POWERED, shouldOpenNow).setValue(OPEN, shouldOpenNow), 2);
            }
        }
    }
}
//========SOLI DEO GLORIA========//