package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.TwoBlocksFrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockSavingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static net.minecraft.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * Main class for frame "slabs", they can be placed in six different ways (that's the reason for this class name) - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.11 08/19/21
 */
@SuppressWarnings("deprecation")
public class SixWaySlabFrameBlock extends AbstractSixWayFrameBlock implements IWaterLoggable {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;
    public static final BooleanProperty CONTAINS_2ND_BLOCK = BCBlockStateProperties.CONTAINS_2ND_BLOCK;
    public static final IntegerProperty LIGHT_LEVEL = BCBlockStateProperties.LIGHT_LEVEL;
    public static final BooleanProperty DOUBLE_SLAB = BCBlockStateProperties.DOUBLE;
    //everything is inverted because when placing, we would need to take the opposite - I figured it out when I completed my work and I don't want to change everything again
    protected static final VoxelShape BOTTOM = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape TOP = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape EAST = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape WEST = Block.makeCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape NORTH = Block.makeCuboidShape(0.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape CUBE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public SixWaySlabFrameBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.DOWN).with(CONTAINS_BLOCK, Boolean.FALSE).with(LIGHT_LEVEL, 0).with(WATERLOGGED, false).with(DOUBLE_SLAB, false).with(CONTAINS_2ND_BLOCK, false));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(WATERLOGGED, DOUBLE_SLAB, CONTAINS_2ND_BLOCK);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.get(DOUBLE_SLAB))
            return CUBE;
        switch (state.get(FACING)) {
            case EAST:
                return EAST;
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            case UP:
                return TOP;
            default:
                return BOTTOM;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        FluidState fluidstate = context.getWorld().getFluidState(blockpos);
        BlockState blockState = context.getWorld().getBlockState(blockpos);
        if (blockState.getBlock() instanceof SixWaySlabFrameBlock) {
            return blockState.with(DOUBLE_SLAB, true);
        }
        if (Objects.requireNonNull(context.getPlayer()).isSneaking() && BCModConfig.SNEAK_FOR_VERTICAL_SLABS.get() || !Objects.requireNonNull(context.getPlayer()).isSneaking() && !BCModConfig.SNEAK_FOR_VERTICAL_SLABS.get()) {
            if (fluidstate.getFluid() == Fluids.WATER) {
                return this.getDefaultState().with(FACING, context.getFace()).with(WATERLOGGED, fluidstate.isSource());
            } else {
                return this.getDefaultState().with(FACING, context.getFace());
            }
        } else {
            BlockState blockstate1 = this.getDefaultState().with(FACING, Direction.UP).with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
            Direction direction = context.getFace();
            return direction != Direction.DOWN && (direction == Direction.UP || !(context.getHitVec().y - (double) blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.with(FACING, Direction.DOWN);
        }
    }

    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        ItemStack itemstack = useContext.getItem();
        boolean isDouble = state.get(DOUBLE_SLAB);
        if (!isDouble && itemstack.getItem() == this.asItem()) {
            if (useContext.replacingClickedOnBlock()) {
                Direction direction = useContext.getFace();
                switch (state.get(FACING)) {
                    case EAST:
                        return direction == Direction.EAST;
                    case SOUTH:
                        return direction == Direction.SOUTH;
                    case WEST:
                        return direction == Direction.WEST;
                    case NORTH:
                        return direction == Direction.NORTH;
                    case UP:
                        return direction == Direction.UP;
                    default:
                        return direction == Direction.DOWN;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TwoBlocksFrameBlockTile();
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
                if (state.get(BCBlockStateProperties.CONTAINS_BLOCK) && !state.get(DOUBLE_SLAB) || state.get(BCBlockStateProperties.CONTAINS_2ND_BLOCK) || Objects.requireNonNull(item.getItem().getRegistryName()).getNamespace().equals(BlockCarpentryMain.MOD_ID)) {
                    return ActionResultType.PASS;
                }
                TileEntity tileEntity = world.getTileEntity(pos);
                int count = player.getHeldItem(hand).getCount();
                Block heldBlock = ((BlockItem) item.getItem()).getBlock();
                if (tileEntity instanceof TwoBlocksFrameBlockTile && !item.isEmpty() && BlockSavingHelper.isValidBlock(heldBlock) && !state.get(CONTAINS_2ND_BLOCK)) {
                    BlockState handBlockState = ((BlockItem) item.getItem()).getBlock().getDefaultState();
                    insertBlock(world, pos, state, handBlockState);
                    if (!player.isCreative())
                        player.getHeldItem(hand).setCount(count - 1);
                    checkForVisibility(state, world, pos, (TwoBlocksFrameBlockTile) tileEntity);
                }

            }
            if (player.getHeldItem(hand).getItem() == Registration.HAMMER.get() || (!BCModConfig.HAMMER_NEEDED.get() && player.isSneaking())) {
                if (!player.isCreative())
                    this.dropContainedBlock(world, pos);
                state = state.with(CONTAINS_BLOCK, Boolean.FALSE);
                state = state.with(CONTAINS_2ND_BLOCK, Boolean.FALSE);
                world.setBlockState(pos, state, 2);
            }
        }
        return ActionResultType.SUCCESS;
    }

    protected void dropContainedBlock(World worldIn, BlockPos pos) {
        if (!worldIn.isRemote) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TwoBlocksFrameBlockTile) {
                TwoBlocksFrameBlockTile frameTileEntity = (TwoBlocksFrameBlockTile) tileentity;
                BlockState blockState = frameTileEntity.getMimic_1();
                if (!(blockState == null)) {
                    worldIn.playEvent(1010, pos, 0);
                    float f = 0.7F;
                    double d0 = (double) (worldIn.rand.nextFloat() * 0.7F) + (double) 0.15F;
                    double d1 = (double) (worldIn.rand.nextFloat() * 0.7F) + (double) 0.060000002F + 0.6D;
                    double d2 = (double) (worldIn.rand.nextFloat() * 0.7F) + (double) 0.15F;
                    ItemStack itemstack1 = new ItemStack(blockState.getBlock());
                    ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, itemstack1);
                    itementity.setDefaultPickupDelay();
                    worldIn.addEntity(itementity);
                }
                blockState = frameTileEntity.getMimic_2();
                if (!(blockState == null)) {
                    worldIn.playEvent(1010, pos, 0);
                    float f = 0.7F;
                    double d0 = (double) (worldIn.rand.nextFloat() * 0.7F) + (double) 0.15F;
                    double d1 = (double) (worldIn.rand.nextFloat() * 0.7F) + (double) 0.060000002F + 0.6D;
                    double d2 = (double) (worldIn.rand.nextFloat() * 0.7F) + (double) 0.15F;
                    ItemStack itemstack1 = new ItemStack(blockState.getBlock());
                    ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, itemstack1);
                    itementity.setDefaultPickupDelay();
                    worldIn.addEntity(itementity);
                }
                frameTileEntity.clear();
            }
        }
    }

    public void insertBlock(IWorld worldIn, BlockPos pos, BlockState state, BlockState handBlock) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TwoBlocksFrameBlockTile) {
            if (!state.get(CONTAINS_BLOCK)) {
                TwoBlocksFrameBlockTile frameTileEntity = (TwoBlocksFrameBlockTile) tileentity;
                frameTileEntity.clear();
                frameTileEntity.setMimic_1(handBlock);
                worldIn.setBlockState(pos, state.with(CONTAINS_BLOCK, Boolean.TRUE), 2);
            } else if (state.get(DOUBLE_SLAB)) {
                TwoBlocksFrameBlockTile frameTileEntity = (TwoBlocksFrameBlockTile) tileentity;
                frameTileEntity.setMimic_2(handBlock);
                worldIn.setBlockState(pos, state.with(CONTAINS_2ND_BLOCK, Boolean.TRUE), 2);
            }
        }
    }

    @Override
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

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    @Nonnull
    public BlockState updatePostPlacement(BlockState stateIn, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull IWorld worldIn, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        boolean similarState = false;
        if (adjacentBlockState.isIn(this)) {
            similarState = state.get(FACING) == adjacentBlockState.get(FACING);
            if (similarState && state.get(DOUBLE_SLAB) != adjacentBlockState.get(DOUBLE_SLAB)) {
                similarState = false;

            }
        }
        return similarState || super.isSideInvisible(state, adjacentBlockState, side);// || BlockCullingHelper.skipSideRendering(adjacentBlockState);
    }

    private void checkForVisibility(BlockState state, World world, BlockPos pos, TwoBlocksFrameBlockTile tileEntity) {
        if (!world.isRemote) {
            for (Direction d : Direction.values()) {
                BlockPos.Mutable mutablePos = pos.toMutable();
                BlockState adjacentBlockState = world.getBlockState(mutablePos.move(d));
                if (!state.get(DOUBLE_SLAB))
                    tileEntity.setVisibileSides(d, !(adjacentBlockState.isSolid() || isSideInvisible(state, adjacentBlockState, d)));
            }
        }
    }
}
//========SOLI DEO GLORIA========//