package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.DaylightDetectorFrameTileEntity;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockSavingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DaylightDetectorBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Main class for frame carpets - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.0 08/20/21
 */
public class DaylightDetectorFrameBlock extends DaylightDetectorBlock {
    public static final BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;
    public static final IntegerProperty LIGHT_LEVEL = BCBlockStateProperties.LIGHT_LEVEL;

    public DaylightDetectorFrameBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(CONTAINS_BLOCK, Boolean.FALSE).with(LIGHT_LEVEL, 0).with(POWER, 0).with(INVERTED, false));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWER, INVERTED, CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DaylightDetectorFrameTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        ItemStack item = player.getHeldItem(hand);
        if (!world.isRemote) {
            BlockAppearanceHelper.setLightLevel(item, state, world, pos, player, hand);
            BlockAppearanceHelper.setTexture(item, state, world, player, pos);
            BlockAppearanceHelper.setDesign(world, pos, player, item);
            BlockAppearanceHelper.setDesignTexture(world, pos, player, item);
            BlockAppearanceHelper.setOverlay(world, pos, player, item);
            BlockAppearanceHelper.setRotation(world, pos, player, item);
            if (item.getItem() instanceof BlockItem) {
                if (state.get(BCBlockStateProperties.CONTAINS_BLOCK) || Objects.requireNonNull(item.getItem().getRegistryName()).getNamespace().equals(BlockCarpentryMain.MOD_ID)) {
                    if (player.isAllowEdit()) {
                        BlockState blockstate = state.func_235896_a_(INVERTED);
                        world.setBlockState(pos, blockstate, 4);
                        updatePower(blockstate, world, pos);
                        return ActionResultType.CONSUME;
                    }
                    return ActionResultType.PASS;
                }
                TileEntity tileEntity = world.getTileEntity(pos);
                int count = player.getHeldItem(hand).getCount();
                Block heldBlock = ((BlockItem) item.getItem()).getBlock();
                if (tileEntity instanceof DaylightDetectorFrameTileEntity && !item.isEmpty() && BlockSavingHelper.isValidBlock(heldBlock) && !state.get(CONTAINS_BLOCK)) {
                    BlockState handBlockState = ((BlockItem) item.getItem()).getBlock().getDefaultState();
                    insertBlock(world, pos, state, handBlockState);
                    if (!player.isCreative())
                        player.getHeldItem(hand).setCount(count - 1);
                }
            } else {
                //hammer is needed to remove the block from the frame - you can change it in the config
                if (state.get(CONTAINS_BLOCK) && (player.getHeldItem(hand).getItem() == Registration.HAMMER.get() || (!BCModConfig.HAMMER_NEEDED.get() && player.isSneaking()))) {
                    if (!player.isCreative())
                        this.dropContainedBlock(world, pos);
                    state = state.with(CONTAINS_BLOCK, Boolean.FALSE);
                    world.setBlockState(pos, state, 2);
                    return ActionResultType.SUCCESS;
                }
                if (player.isAllowEdit()) {
                    BlockState blockstate = state.func_235896_a_(INVERTED);
                    world.setBlockState(pos, blockstate, 4);
                    updatePower(blockstate, world, pos);
                    return ActionResultType.CONSUME;
                }
                return ActionResultType.PASS;
            }
        }
        return ActionResultType.SUCCESS;
    }

    protected void dropContainedBlock(World worldIn, BlockPos pos) {
        if (!worldIn.isRemote) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof DaylightDetectorFrameTileEntity) {
                DaylightDetectorFrameTileEntity frameTileEntity = (DaylightDetectorFrameTileEntity) tileentity;
                BlockState blockState = frameTileEntity.getMimic();
                if (!(blockState == null)) {
                    worldIn.playEvent(1010, pos, 0);
                    frameTileEntity.clear();
                    float f = 0.7F;
                    double d0 = (double) (worldIn.rand.nextFloat() * 0.7F) + (double) 0.15F;
                    double d1 = (double) (worldIn.rand.nextFloat() * 0.7F) + (double) 0.060000002F + 0.6D;
                    double d2 = (double) (worldIn.rand.nextFloat() * 0.7F) + (double) 0.15F;
                    ItemStack itemstack1 = new ItemStack(blockState.getBlock());
                    ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, itemstack1);
                    itementity.setDefaultPickupDelay();
                    worldIn.addEntity(itementity);
                    frameTileEntity.clear();
                }
            }
        }
    }

    public void insertBlock(IWorld worldIn, BlockPos pos, BlockState state, BlockState handBlock) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof DaylightDetectorFrameTileEntity) {
            DaylightDetectorFrameTileEntity frameTileEntity = (DaylightDetectorFrameTileEntity) tileentity;
            frameTileEntity.clear();
            frameTileEntity.setMimic(handBlock);
            worldIn.setBlockState(pos, state.with(CONTAINS_BLOCK, Boolean.TRUE), 2);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(worldIn, pos);

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        if (state.get(LIGHT_LEVEL) > 15) {
            return 15;
        }
        return state.get(LIGHT_LEVEL);
    }
}
//========SOLI DEO GLORIA========//