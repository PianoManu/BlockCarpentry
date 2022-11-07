package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.item.BaseFrameItem;
import mod.pianomanu.blockcarpentry.item.BaseIllusionItem;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.BedFrameTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockSavingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Main class for frame beds - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.2 11/07/22
 */
public class BedFrameBlock extends BedBlock {
    public static final BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;
    public static final IntegerProperty LIGHT_LEVEL = BCBlockStateProperties.LIGHT_LEVEL;
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BedFrameBlock(DyeColor colorIn, Properties properties) {
        super(colorIn, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONTAINS_BLOCK, false).setValue(LIGHT_LEVEL, 0).setValue(PART, BedPart.FOOT).setValue(OCCUPIED, Boolean.valueOf(false)).setValue(HORIZONTAL_FACING, Direction.NORTH));//.setValue(TEXTURE,0));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONTAINS_BLOCK, LIGHT_LEVEL, PART, OCCUPIED, HORIZONTAL_FACING);//, TEXTURE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BedFrameTile(pos, state);
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
                    BlockAppearanceHelper.setWoolColor(level, pos, player, hand) ||
                    BlockAppearanceHelper.setOverlay(level, pos, player, item) ||
                    BlockAppearanceHelper.setRotation(level, pos, player, item))
                return InteractionResult.SUCCESS;
            if ((state.getValue(CONTAINS_BLOCK) && !item.is(Tags.Items.DYES) && item.getItem() instanceof BaseFrameItem || item.getItem() instanceof BaseIllusionItem) || item.isEmpty()) {
                //Taken from BedBlock, should work similar to vanilla beds
                if (state.getValue(PART) != BedPart.HEAD) {
                    pos = pos.relative(state.getValue(HORIZONTAL_FACING));
                    state = level.getBlockState(pos);
                    if (!state.is(this)) {
                        return InteractionResult.CONSUME;
                    }
                }

                if (!canSetSpawn(level)) {
                    level.removeBlock(pos, false);
                    BlockPos blockpos = pos.relative(state.getValue(HORIZONTAL_FACING).getOpposite());
                    if (level.getBlockState(blockpos).is(this)) {
                        level.removeBlock(blockpos, false);
                    }

                    level.explode(null, DamageSource.badRespawnPointExplosion(), null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 5.0F, true, Explosion.BlockInteraction.DESTROY);
                    return InteractionResult.SUCCESS;
                } else if (state.getValue(OCCUPIED)) {
                    if (!this.kickVillagerOutOfBed(level, pos)) {
                        player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
                    }

                    return InteractionResult.SUCCESS;
                } else {
                    player.startSleepInBed(pos).ifLeft((comp) -> {
                        if (comp != null) {
                            player.displayClientMessage(comp.getMessage(), true);
                        }

                    });
                    return InteractionResult.SUCCESS;
                }
            }
            if (item.getItem() instanceof BlockItem) {
                if (item.getItem() instanceof BaseFrameItem || item.getItem() instanceof BaseIllusionItem) {
                    return InteractionResult.PASS;
                }
                BlockEntity tileEntity = level.getBlockEntity(pos);
                int count = player.getItemInHand(hand).getCount();
                Block heldBlock = ((BlockItem) item.getItem()).getBlock();
                if (tileEntity instanceof BedFrameTile && !item.isEmpty() && BlockSavingHelper.isValidBlock(heldBlock) && !state.getValue(CONTAINS_BLOCK)) {
                    BlockState handBlockState = ((BlockItem) item.getItem()).getBlock().defaultBlockState();
                    insertBlock(level, pos, state, handBlockState);
                    if (!player.isCreative())
                        player.getItemInHand(hand).setCount(count - 1);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    private boolean kickVillagerOutOfBed(Level level, BlockPos pos) {
        List<Villager> list = level.getEntitiesOfClass(Villager.class, new AABB(pos), LivingEntity::isSleeping);
        if (list.isEmpty()) {
            return false;
        } else {
            list.get(0).stopSleeping();
            return true;
        }
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

    protected void dropContainedBlock(Level levelIn, BlockPos pos) {
        if (!levelIn.isClientSide) {
            BlockEntity tileentity = levelIn.getBlockEntity(pos);
            if (tileentity instanceof BedFrameTile) {
                BedFrameTile frameBlockEntity = (BedFrameTile) tileentity;
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
                }
                frameBlockEntity.clear();
            }
        }
    }

    public void insertBlock(Level levelIn, BlockPos pos, BlockState state, BlockState handBlock) {
        BlockEntity tileentity = levelIn.getBlockEntity(pos);
        if (tileentity instanceof BedFrameTile) {
            BedFrameTile frameBlockEntity = (BedFrameTile) tileentity;
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
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
//========SOLI DEO GLORIA========//