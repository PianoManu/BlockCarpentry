package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.item.BaseFrameItem;
import mod.pianomanu.blockcarpentry.item.BaseIllusionItem;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.ChestFrameTileEntity;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockModificationHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Main class for frame chests - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.6 09/27/23
 */
public class ChestFrameBlock extends FrameBlock implements IWaterLoggable {
    private static final VoxelShape INNER_CUBE = Block.makeCuboidShape(2.0, 2.0, 2.0, 14.0, 14.0, 14.0);
    private static final VoxelShape BOTTOM_NORTH = Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 2.0);
    private static final VoxelShape BOTTOM_EAST = Block.makeCuboidShape(14.0, 0.0, 2.0, 16.0, 2.0, 14.0);
    private static final VoxelShape BOTTOM_SOUTH = Block.makeCuboidShape(0.0, 0.0, 14.0, 16.0, 2.0, 16.0);
    private static final VoxelShape BOTTOM_WEST = Block.makeCuboidShape(0.0, 0.0, 2.0, 2.0, 2.0, 14.0);
    private static final VoxelShape TOP_NORTH = Block.makeCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 2.0);
    private static final VoxelShape TOP_EAST = Block.makeCuboidShape(14.0, 14.0, 2.0, 16.0, 16.0, 14.0);
    private static final VoxelShape TOP_SOUTH = Block.makeCuboidShape(0.0, 14.0, 14.0, 16.0, 16.0, 16.0);
    private static final VoxelShape TOP_WEST = Block.makeCuboidShape(0.0, 14.0, 2.0, 2.0, 16.0, 14.0);
    private static final VoxelShape NW_PILLAR = Block.makeCuboidShape(0.0, 2.0, 0.0, 2.0, 14.0, 2.0);
    private static final VoxelShape SW_PILLAR = Block.makeCuboidShape(0.0, 2.0, 14.0, 2.0, 14.0, 16.0);
    private static final VoxelShape NE_PILLAR = Block.makeCuboidShape(14.0, 2.0, 0.0, 16.0, 14.0, 2.0);
    private static final VoxelShape SE_PILLAR = Block.makeCuboidShape(14.0, 2.0, 14.0, 16.0, 14.0, 16.0);
    private static final VoxelShape CHEST = VoxelShapes.or(INNER_CUBE, BOTTOM_EAST, BOTTOM_SOUTH, BOTTOM_WEST, BOTTOM_NORTH, TOP_EAST, TOP_SOUTH, TOP_WEST, TOP_NORTH, NW_PILLAR, SW_PILLAR, NE_PILLAR, SE_PILLAR);

    public ChestFrameBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(CONTAINS_BLOCK, false).with(LIGHT_LEVEL, 0).with(HorizontalBlock.HORIZONTAL_FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, HorizontalBlock.HORIZONTAL_FACING, CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        FluidState fluidstate = context.getWorld().getFluidState(blockpos);
        if (fluidstate.getFluid() == Fluids.WATER) {
            return this.getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(WATERLOGGED, fluidstate.isSource());
        } else {
            return this.getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
        }
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ChestFrameTileEntity(Registration.CHEST_FRAME_TILE.get());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitresult) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (hand == Hand.MAIN_HAND) {
            if (!level.isRemote) {
                return frameUseServer(state, level, pos, player, itemStack, hitresult);
            }
            return frameUseClient(state, level, pos, player, itemStack, hitresult);
        }
        return ActionResultType.FAIL;
    }

    public ActionResultType frameUseServer(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack, BlockRayTraceResult hitresult) {
        if (removeBlock(level, pos, state, itemStack, player))
            return ActionResultType.SUCCESS;
        if (state.get(CONTAINS_BLOCK)) {
            if (BlockAppearanceHelper.setAll(itemStack, state, level, pos, player) || BlockModificationHelper.setAll(itemStack, (ChestFrameTileEntity) Objects.requireNonNull(level.getTileEntity(pos)), player))
                return ActionResultType.CONSUME;
        }
        if (itemStack.getItem() instanceof BlockItem) {
            if (changeMimic(state, level, pos, player, itemStack))
                return ActionResultType.SUCCESS;
        }
        TileEntity tileEntity = level.getTileEntity(pos);
        if (tileEntity instanceof ChestFrameTileEntity && state.get(CONTAINS_BLOCK)) {
            return chestBehavior(state, level, pos, player, itemStack);
        }
        return ActionResultType.FAIL;
    }

    private ActionResultType chestBehavior(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        if (level.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            INamedContainerProvider inamedcontainerprovider = this.getContainer(state, level, pos);
            if (inamedcontainerprovider != null) {
                player.openContainer(inamedcontainerprovider);
                player.addStat(Stats.CUSTOM.get(Stats.OPEN_CHEST));
                PiglinTasks.func_234478_a_(player, true);
            }

            return ActionResultType.CONSUME;
        }
    }

    @Override
    public ActionResultType frameUseClient(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack, BlockRayTraceResult hitresult) {
        TileEntity TileEntity = level.getTileEntity(pos);
        if (TileEntity instanceof ChestFrameTileEntity && state.get(CONTAINS_BLOCK)) {
            if (!(itemStack.getItem() instanceof BaseFrameItem || itemStack.getItem() instanceof BaseIllusionItem)) {
                return ActionResultType.CONSUME;
            }
        }
        return super.frameUseClient(state, level, pos, player, itemStack, hitresult);
    }

    public boolean removeBlock(World level, BlockPos pos, BlockState state, ItemStack itemStack, PlayerEntity player) {
        if (itemStack.getItem() == Registration.HAMMER.get() || (!BCModConfig.HAMMER_NEEDED.get() && player.isCrouching())) {
            if (!player.isCreative())
                this.dropContainedBlock(level, pos);
            state = state.with(CONTAINS_BLOCK, Boolean.FALSE);
            level.setBlockState(pos, state, 2);
            return true;
        }
        return false;
    }

    @Override
    public void dropContainedBlock(World level, BlockPos pos) {
        if (!level.isRemote) {
            TileEntity tileentity = level.getTileEntity(pos);
            if (tileentity instanceof ChestFrameTileEntity) {
                ChestFrameTileEntity frameTileEntity = (ChestFrameTileEntity) tileentity;
                BlockState blockState = frameTileEntity.getMimic();
                if (!(blockState == null)) {
                    level.playEvent(1010, pos, 0);
                    frameTileEntity.clear();
                    float f = 0.7F;
                    double d0 = (double) (level.rand.nextFloat() * 0.7F) + (double) 0.15F;
                    double d1 = (level.rand.nextFloat() * 0.7F) + (double) 0.060000002F + 0.6D;
                    double d2 = (double) (level.rand.nextFloat() * 0.7F) + (double) 0.15F;
                    ItemStack itemstack1 = new ItemStack(blockState.getBlock());
                    ItemEntity itementity = new ItemEntity(level, (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, itemstack1);
                    itementity.setDefaultPickupDelay();
                    level.addEntity(itementity);
                    frameTileEntity.clear();
                }
            }
        }
    }

    @Override
    public void insertBlock(World level, BlockPos pos, BlockState state, BlockState handBlock) {
        TileEntity tileentity = level.getTileEntity(pos);
        if (tileentity instanceof ChestFrameTileEntity) {
            ChestFrameTileEntity frameTileEntity = (ChestFrameTileEntity) tileentity;
            frameTileEntity.clear();
            frameTileEntity.setMimic(handBlock);
            level.setBlockState(pos, state.with(CONTAINS_BLOCK, Boolean.TRUE), 2);
        }
    }

    @Override
    public void onReplaced(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity te = level.getTileEntity(pos);
            if (te instanceof ChestFrameTileEntity) {
                dropContainedBlock(level, pos);
                InventoryHelper.dropItems(level, pos, ((ChestFrameTileEntity) te).getItems());
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        return CHEST;
    }

    @Override
    public boolean isCorrectTileInstance(TileEntity TileEntity) {
        return TileEntity instanceof ChestFrameTileEntity;
    }

    @Override
    public float getSlipperiness(BlockState state, IWorldReader level, BlockPos pos, @Nullable Entity entity) {
        return super.getSlipperiness(state, level, pos, entity);
    }

    @Override
    public boolean executeModifications(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        return BlockAppearanceHelper.setAll(itemStack, state, level, pos, player) || getTile(level, pos) != null && BlockModificationHelper.setAll(itemStack, getTile(level, pos), player, true, false);
    }
}
//========SOLI DEO GLORIA========//