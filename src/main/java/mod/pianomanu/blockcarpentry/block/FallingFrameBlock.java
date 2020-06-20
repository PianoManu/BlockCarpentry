package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.entity.FallingFrameBlockEntity;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.FallingFrameBlockTile;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import mod.pianomanu.blockcarpentry.util.BlockContainerProperty;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.sql.Time;
import java.util.Random;

import static mod.pianomanu.blockcarpentry.block.FrameBlock.LIGHT_LEVEL;
import static mod.pianomanu.blockcarpentry.block.FrameBlock.TEXTURE;

public class FallingFrameBlock extends FallingBlock {

    //TODO fix falling block losing tile entity
    public static final BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;
    public static final BlockContainerProperty CONTAINS = BCBlockStateProperties.CONTAINS;

    public FallingFrameBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(CONTAINS_BLOCK, false).with(CONTAINS, "empty").with(LIGHT_LEVEL, 0).with(TEXTURE,0));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CONTAINS_BLOCK, CONTAINS, LIGHT_LEVEL, TEXTURE);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FrameBlockTile();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        ItemStack item = player.getHeldItem(hand);
        if (!world.isRemote) {
            if(state.get(CONTAINS_BLOCK)) {
                this.dropContainedBlock(world, pos);
                state = state.with(CONTAINS_BLOCK, Boolean.FALSE);
                world.setBlockState(pos,state,2);
            } else {
                if(item.getItem() instanceof BlockItem) {
                    TileEntity tileEntity = world.getTileEntity(pos);
                    int count = player.getHeldItem(hand).getCount();
                    if (tileEntity instanceof FrameBlockTile && !item.isEmpty() && ((BlockItem) item.getItem()).getBlock().isSolid(((BlockItem) item.getItem()).getBlock().getDefaultState())) {
                        ((FrameBlockTile) tileEntity).clear();
                        BlockState handBlockState = ((BlockItem) item.getItem()).getBlock().getDefaultState();
                        ((FrameBlockTile) tileEntity).setMimic(handBlockState);
                        insertBlock(world,pos, state,handBlockState);
                        player.getHeldItem(hand).setCount(count-1);
                    }
                }
            }
            if (item.getItem()== Items.GLOWSTONE_DUST && state.get(LIGHT_LEVEL)<15) {
                int count = player.getHeldItem(hand).getCount();
                world.setBlockState(pos,state.with(LIGHT_LEVEL, state.getLightValue()+3));
                player.getHeldItem(hand).setCount(count-1);
            }
            if ((item.getItem() == Items.COAL || item.getItem() == Items.CHARCOAL) && state.get(LIGHT_LEVEL)<15) {
                int count = player.getHeldItem(hand).getCount();
                world.setBlockState(pos,state.with(LIGHT_LEVEL, state.getLightValue()+1));
                player.getHeldItem(hand).setCount(count-1);
            }
            if (item.getItem() == Registration.TEXTURE_WRENCH.get() && !player.isSneaking()) {
                if (state.get(TEXTURE)<3) {
                    world.setBlockState(pos, state.with(TEXTURE, state.get(TEXTURE) + 1));
                } else {
                    world.setBlockState(pos, state.with(TEXTURE, 0));
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    private void dropContainedBlock(World worldIn, BlockPos pos) {
        if (!worldIn.isRemote) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof FrameBlockTile) {
                FrameBlockTile frameTileEntity = (FrameBlockTile) tileentity;
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
        if (tileentity instanceof FrameBlockTile) {
            FrameBlockTile frameTileEntity = (FrameBlockTile) tileentity;
            frameTileEntity.clear();
            frameTileEntity.setMimic(handBlock);
            worldIn.setBlockState(pos, state.with(CONTAINS_BLOCK, Boolean.TRUE), 2);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(worldIn, pos);

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    protected void onStartFalling(FallingBlockEntity fallingEntity) {
        super.onStartFalling(fallingEntity);
    }

    @Override
    public void onEndFalling(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState) {
        super.onEndFalling(worldIn, pos, fallingState, hitState);

    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down())) && pos.getY() >= 0) {
            if (worldIn.getTileEntity(pos) instanceof FrameBlockTile) {
                FrameBlockTile tileEntity = (FrameBlockTile) worldIn.getTileEntity(pos);
                if (tileEntity.getMimic()!=null) {
                    FallingFrameBlockEntity fallingFrameBlockEntity = new FallingFrameBlockEntity(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, worldIn.getBlockState(pos), tileEntity.getMimic());
                    this.onStartFalling(fallingFrameBlockEntity);
                    worldIn.addEntity(fallingFrameBlockEntity);
                }
            }
        }
    }

    private TileEntity createFallingFrameTileEntity(BlockState mimic) {
        return new FallingFrameBlockTile(mimic);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getLightValue(BlockState state) {
        if (state.get(LIGHT_LEVEL)>15) {
            return 15;
        }
        return state.get(LIGHT_LEVEL);
    }
}
