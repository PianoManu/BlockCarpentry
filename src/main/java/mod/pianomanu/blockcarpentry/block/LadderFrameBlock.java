package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockSavingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static mod.pianomanu.blockcarpentry.util.BCBlockStateProperties.CONTAINS_BLOCK;
import static mod.pianomanu.blockcarpentry.util.BCBlockStateProperties.LIGHT_LEVEL;

/**
 * Main class for frame ladder plates - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.0 08/15/21
 */
public class LadderFrameBlock extends LadderBlock implements EntityBlock {
    //public static final BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;

    public LadderFrameBlock(Properties builder) {
        super(builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.FALSE).setValue(CONTAINS_BLOCK, false).setValue(LIGHT_LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    /*@Override
    public boolean hasBlockEntity(BlockState state) {
        return true;
    }*/

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FrameBlockTile(pos, state);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public InteractionResult use(@Nullable BlockState state, Level level, @Nullable BlockPos pos, Player player, @Nullable InteractionHand hand, @Nullable BlockHitResult hitResult) {
        ItemStack item = player.getItemInHand(Objects.requireNonNull(hand));
        if (!level.isClientSide && state != null && pos != null) {
            BlockAppearanceHelper.setLightLevel(item, state, level, pos, player, hand);
            BlockAppearanceHelper.setTexture(item, state, level, player, pos);
            BlockAppearanceHelper.setDesign(level, pos, player, item);
            BlockAppearanceHelper.setDesignTexture(level, pos, player, item);
            BlockAppearanceHelper.setOverlay(level, pos, player, item);
            BlockAppearanceHelper.setRotation(level, pos, player, item);
            if (item.getItem() instanceof BlockItem) {
                if (state.getValue(BCBlockStateProperties.CONTAINS_BLOCK) || Objects.requireNonNull(item.getItem().getRegistryName()).getNamespace().equals(BlockCarpentryMain.MOD_ID)) {
                    return InteractionResult.PASS;
                }
                BlockEntity tileEntity = level.getBlockEntity(pos);
                int count = player.getItemInHand(hand).getCount();
                Block heldBlock = ((BlockItem) item.getItem()).getBlock();
                if (tileEntity instanceof FrameBlockTile && !item.isEmpty() && BlockSavingHelper.isValidBlock(heldBlock) && !state.getValue(CONTAINS_BLOCK)) {
                    BlockState handBlockState = ((BlockItem) item.getItem()).getBlock().defaultBlockState();
                    insertBlock(level, pos, state, handBlockState);
                    if (!player.isCreative())
                        player.getItemInHand(hand).setCount(count - 1);

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

    protected void dropContainedBlock(Level levelIn, BlockPos pos) {
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
    public void onRemove(BlockState state, @Nullable Level levelIn, @Nullable BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(Objects.requireNonNull(levelIn), pos);

            super.onRemove(state, levelIn, Objects.requireNonNull(pos), newState, isMoving);
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
    public boolean isLadder(@Nullable BlockState state, @Nullable LevelReader level, @Nullable BlockPos pos, @Nullable LivingEntity entity) {
        return true;
    }
}
//========SOLI DEO GLORIA========//