package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.ChestFrameTileEntity;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockSavingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Objects;

import static mod.pianomanu.blockcarpentry.util.BCBlockStateProperties.LIGHT_LEVEL;
import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * Main class for frame chests - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.4 10/06/20
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
        this.setDefaultState(this.getDefaultState().with(CONTAINS_BLOCK, false).with(LIGHT_LEVEL, 0).with(HORIZONTAL_FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, HORIZONTAL_FACING, CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        FluidState fluidstate = context.getWorld().getFluidState(blockpos);
        if (fluidstate.getFluid() == Fluids.WATER) {
            return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(WATERLOGGED, fluidstate.isSource());
        } else {
            return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return Registration.CHEST_FRAME_TILE.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                             Hand hand, BlockRayTraceResult result) {
        ItemStack item = player.getHeldItem(hand);
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (item.getItem() instanceof BlockItem) {
                int count = player.getHeldItem(hand).getCount();
                Block heldBlock = ((BlockItem) item.getItem()).getBlock();
                if (tileEntity instanceof ChestFrameTileEntity && !item.isEmpty() && BlockSavingHelper.isValidBlock(heldBlock) && !state.get(CONTAINS_BLOCK)) {
                    BlockState handBlockState = ((BlockItem) item.getItem()).getBlock().getDefaultState();
                    insertBlock(world, pos, state, handBlockState);
                    if (!player.isCreative())
                        player.getHeldItem(hand).setCount(count - 1);
                }
            }
            //hammer is needed to remove the block from the frame - you can change it in the config
            if (player.getHeldItem(hand).getItem() == Registration.HAMMER.get() || (!BCModConfig.HAMMER_NEEDED.get() && player.isSneaking())) {
                if (!player.isCreative())
                    this.dropContainedBlock(world, pos);
                state = state.with(CONTAINS_BLOCK, Boolean.FALSE);
                world.setBlockState(pos, state, 2);
            }
            BlockAppearanceHelper.setLightLevel(item, state, world, pos, player, hand);
            BlockAppearanceHelper.setTexture(item, state, world, player, pos);
            BlockAppearanceHelper.setDesign(world, pos, player, item);
            BlockAppearanceHelper.setDesignTexture(world, pos, player, item);
            if (tileEntity instanceof ChestFrameTileEntity && state.get(CONTAINS_BLOCK)) {
                if (!(Objects.requireNonNull(item.getItem().getRegistryName()).getNamespace().equals(BlockCarpentryMain.MOD_ID))) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, (ChestFrameTileEntity) tileEntity, pos);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    protected void dropContainedBlock(World worldIn, BlockPos pos) {
        if (!worldIn.isRemote) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof ChestFrameTileEntity) {
                ChestFrameTileEntity frameTileEntity = (ChestFrameTileEntity) tileentity;
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

    @Override
    public void insertBlock(IWorld worldIn, BlockPos pos, BlockState state, BlockState handBlock) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof ChestFrameTileEntity) {
            ChestFrameTileEntity frameTileEntity = (ChestFrameTileEntity) tileentity;
            frameTileEntity.clear();
            frameTileEntity.setMimic(handBlock);
            worldIn.setBlockState(pos, state.with(CONTAINS_BLOCK, Boolean.TRUE), 2);
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof ChestFrameTileEntity) {
                dropContainedBlock(worldIn, pos);
                InventoryHelper.dropItems(worldIn, pos, ((ChestFrameTileEntity) te).getItems());
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
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return CHEST;
    }
}
//========SOLI DEO GLORIA========//