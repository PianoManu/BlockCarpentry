package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
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

import java.util.List;

import static mod.pianomanu.blockcarpentry.block.FrameBlock.LIGHT_LEVEL;
import static mod.pianomanu.blockcarpentry.block.FrameBlock.TEXTURE;

public class DoorFrameBlock extends DoorBlock {
    public static final BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;
    public static final IntegerProperty DESIGN = BCBlockStateProperties.DESIGN;
    //public static final IntegerProperty DESIGN_TEXTURE = BCBlockStateProperties.DESIGN_TEXTURE;

    public DoorFrameBlock(Properties builder) {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(CONTAINS_BLOCK, Boolean.FALSE).with(FACING, Direction.NORTH).with(OPEN, Boolean.valueOf(false)).with(HINGE, DoorHingeSide.LEFT).with(POWERED, Boolean.valueOf(false)).with(HALF, DoubleBlockHalf.LOWER).with(LIGHT_LEVEL, 0).with(TEXTURE,0).with(DESIGN,0));//.with(DESIGN_TEXTURE,0));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CONTAINS_BLOCK, FACING, OPEN, HINGE, POWERED, HALF, LIGHT_LEVEL, TEXTURE, DESIGN);//, DESIGN_TEXTURE);
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
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        ItemStack item = player.getHeldItem(hand);
        if (!world.isRemote) {
            if(state.get(CONTAINS_BLOCK) && player.isSneaking()) {
                this.dropContainedBlock(world, pos);
                state = state.with(CONTAINS_BLOCK, Boolean.FALSE);
                world.setBlockState(pos,state,2);
            } else {
                if(item.getItem() instanceof BlockItem) {
                    TileEntity tileEntity = world.getTileEntity(pos);
                    int count = player.getHeldItem(hand).getCount();
                    if (tileEntity instanceof FrameBlockTile && !item.isEmpty() && ((BlockItem) item.getItem()).getBlock().isSolid(((BlockItem) item.getItem()).getBlock().getDefaultState()) && !state.get(CONTAINS_BLOCK)) {
                        ((FrameBlockTile) tileEntity).clear();
                        BlockState handBlockState = ((BlockItem) item.getItem()).getBlock().getDefaultState();
                        ((FrameBlockTile) tileEntity).setMimic(handBlockState);
                        insertBlock(world,pos, state,handBlockState);
                        player.getHeldItem(hand).setCount(count-1);
                    }
                }
                else if(!(item.getItem()==Items.GLOWSTONE_DUST) && !(item.getItem()==Items.COAL) && !(item.getItem()==Items.CHARCOAL) && !(item.getItem()==Registration.TEXTURE_WRENCH.get()) && !(item.getItem()==Registration.CHISEL.get()) && !(item.getItem()==Registration.PAINTBRUSH.get())){
                    state = state.cycle(OPEN);
                    world.setBlockState(pos, state, 10);
                    world.playEvent(player, state.get(OPEN) ? 1006 : 1012, pos, 0);
                }
            }
            BlockAppearanceHelper.setLightLevel(item,state,world,pos,player,hand);
            BlockAppearanceHelper.setTexture(item,state,world,player,pos);
            if (item.getItem() == Registration.TEXTURE_WRENCH.get() && player.isSneaking()) {
                //TODO
                //System.out.println("You should rotate now!");
            }
            if (item.getItem() == Registration.CHISEL.get() && !player.isSneaking()) {
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof FrameBlockTile) {
                    FrameBlockTile fte = (FrameBlockTile) tileEntity;
                    if (fte.getDesign() < fte.maxDesigns) {
                        fte.setDesign(fte.getDesign()+1);
                    } else {
                        fte.setDesign(0);
                    }
                }
            }
            if (item.getItem() == Registration.PAINTBRUSH.get() && !player.isSneaking()) {
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof FrameBlockTile) {
                    FrameBlockTile fte = (FrameBlockTile) tileEntity;
                    if (fte.getDesignTexture() < fte.maxTextures) {
                        fte.setDesignTexture(fte.getDesignTexture()+1);
                    } else {
                        fte.setDesignTexture(0);
                    }
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
    @SuppressWarnings("deprecation")
    public int getLightValue(BlockState state) {
        if (state.get(LIGHT_LEVEL)>15) {
            return 15;
        }
        return state.get(LIGHT_LEVEL);
    }
}
