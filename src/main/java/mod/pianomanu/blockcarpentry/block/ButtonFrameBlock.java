package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.tileentity.ButtonFrameBlockTile;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WoodButtonBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ButtonFrameBlock extends WoodButtonBlock {
    public static final BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;
    private static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final EnumProperty<AttachFace> FACE = BlockStateProperties.FACE;

    public ButtonFrameBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(CONTAINS_BLOCK, false).with(HORIZONTAL_FACING, Direction.NORTH).with(POWERED,false).with(FACE, AttachFace.WALL));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CONTAINS_BLOCK).add(BlockStateProperties.HORIZONTAL_FACING).add(POWERED).add(FACE);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ButtonFrameBlockTile(this.getDefaultState().get(FACE), this.getDefaultState().get(HORIZONTAL_FACING));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        ItemStack item = player.getHeldItem(hand);
        if (!world.isRemote) {
            if(state.get(CONTAINS_BLOCK)) {
                if (state.get(POWERED) &&!player.isSneaking()) {
                    return ActionResultType.CONSUME;
                } else if (!player.isSneaking()){
                    this.func_226910_d_(state, world, pos);
                    this.playSound(player, world, pos, true);
                }
                if(item.isEmpty() &&player.isSneaking()) {
                    this.dropContainedBlock(world, pos);
                    state = state.with(CONTAINS_BLOCK, Boolean.FALSE);
                    world.setBlockState(pos, state, 2);
                }
            } else {
                if(item.getItem() instanceof BlockItem) {
                    TileEntity tileEntity = world.getTileEntity(pos);
                    int count = player.getHeldItem(hand).getCount();
                    if (tileEntity instanceof ButtonFrameBlockTile && !item.isEmpty() && ((BlockItem) item.getItem()).getBlock().isSolid(((BlockItem) item.getItem()).getBlock().getDefaultState())) {
                        ((ButtonFrameBlockTile) tileEntity).clear();
                        BlockState handBlockState = ((BlockItem) item.getItem()).getBlock().getDefaultState();
                        ((ButtonFrameBlockTile) tileEntity).setMimic(handBlockState);
                        insertBlock(world,pos, state,handBlockState);
                        player.getHeldItem(hand).setCount(count-1);
                        return ActionResultType.CONSUME;
                    }
                }
                if (state.get(POWERED) &&!player.isSneaking()) {
                    return ActionResultType.CONSUME;
                } else if (!player.isSneaking()){
                    this.func_226910_d_(state, world, pos);
                    this.playSound(player, world, pos, true);
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    private void dropContainedBlock(World worldIn, BlockPos pos) {
        if (!worldIn.isRemote) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof ButtonFrameBlockTile) {
                ButtonFrameBlockTile frameTileEntity = (ButtonFrameBlockTile) tileentity;
                BlockState blockState = frameTileEntity.getMimic();
                if (!(blockState==null)) {
                    worldIn.playEvent(1010, pos, 0);
                    frameTileEntity.clear();
                    float f = 0.7F;
                    double d0 = (double)(worldIn.rand.nextFloat() * 0.7F) + (double)0.15F;
                    double d1 = (double)(worldIn.rand.nextFloat() * 0.7F) + (double)0.060000002F + 0.6D;
                    double d2 = (double)(worldIn.rand.nextFloat() * 0.7F) + (double)0.15F;
                    ItemStack itemstack1 = blockState.getBlock().asItem().getDefaultInstance();
                    ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, itemstack1);
                    itementity.setDefaultPickupDelay();
                    worldIn.addEntity(itementity);
                    frameTileEntity.clear();
                }
            }
        }
    }

    public void insertBlock(IWorld worldIn, BlockPos pos, BlockState state, BlockState handBlock) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof ButtonFrameBlockTile) {
            ButtonFrameBlockTile frameTileEntity = (ButtonFrameBlockTile) tileentity;
            frameTileEntity.clear();
            frameTileEntity.setMimic(handBlock);
            worldIn.setBlockState(pos, state.with(CONTAINS_BLOCK, Boolean.TRUE), 2);
        }
    }
}
