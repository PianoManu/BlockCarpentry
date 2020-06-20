package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;


@SuppressWarnings("deprecation") public class FrameBlock extends Block {
    private final boolean isSolid = false;
    private int lightLevel = 0;
    //private static final VoxelShape SHAPE = VoxelShapes.create(.2, .2, .2, .8, .8, .8);
    public static final BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;
    public static final IntegerProperty LIGHT_LEVEL = BCBlockStateProperties.LIGHT_LEVEL;
    public static final IntegerProperty TEXTURE = BCBlockStateProperties.TEXTURE;

    public FrameBlock(Properties properties) {
        /*super(Properties.create(Material.WOOD)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(0.4f)
                .harvestTool(ToolType.AXE)
                .notSolid()
        );*/
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(CONTAINS_BLOCK, Boolean.FALSE).with(LIGHT_LEVEL, 0).with(TEXTURE,0));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CONTAINS_BLOCK).add(LIGHT_LEVEL).add(TEXTURE);
    }

    /*@Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }*/

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
        //if (!item.isEmpty() && item.getItem() instanceof BlockItem) {
            if (!world.isRemote) {
                if(state.get(CONTAINS_BLOCK) && player.isSneaking()) {
                    this.dropContainedBlock(world, pos);
                    state = state.with(CONTAINS_BLOCK, Boolean.FALSE);
                    world.setBlockState(pos,state,2);
                } else {
                    if(item.getItem() instanceof BlockItem) {
                        TileEntity tileEntity = world.getTileEntity(pos);
                        int count = player.getHeldItem(hand).getCount();
                        Block heldBlock = ((BlockItem) item.getItem()).getBlock();
                        //TODO fix for non-solid blocks
                        //heldBlock.getShape(heldBlock.getDefaultState(),world,pos, ISelectionContext.dummy());
                        if (tileEntity instanceof FrameBlockTile && !item.isEmpty() && ((BlockItem) item.getItem()).getBlock().isSolid(((BlockItem) item.getItem()).getBlock().getDefaultState()) && !state.get(CONTAINS_BLOCK)) {
                            ((FrameBlockTile) tileEntity).clear();
                            BlockState handBlockState = ((BlockItem) item.getItem()).getBlock().getDefaultState();
                            ((FrameBlockTile) tileEntity).setMimic(handBlockState);
                            insertBlock(world,pos, state,handBlockState);
                            //this.contained_block=handBlockState.getBlock();
                            player.getHeldItem(hand).setCount(count-1);
                        }
                    }
                }
                if (item.getItem()== Items.GLOWSTONE_DUST && state.get(LIGHT_LEVEL)<15) {
                    int count = player.getHeldItem(hand).getCount();
                    lightLevel=lightLevel+3;
                    world.setBlockState(pos,state.with(LIGHT_LEVEL, state.getLightValue()+3));
                    player.getHeldItem(hand).setCount(count-1);
                }
                if ((item.getItem() == Items.COAL || item.getItem() == Items.CHARCOAL) && state.get(LIGHT_LEVEL)<15) {
                    int count = player.getHeldItem(hand).getCount();
                    lightLevel=lightLevel+1;
                    world.setBlockState(pos,state.with(LIGHT_LEVEL, state.getLightValue()+1));
                    player.getHeldItem(hand).setCount(count-1);
                }
                /*TileEntity te = world.getTileEntity(pos);
                if (te instanceof FrameBlockTile) {
                    BlockState mimicState = ((BlockItem) item.getItem()).getBlock().getDefaultState();
                    ((FrameBlockTile) te).setMimic(mimicState);
                }*/
                if (item.getItem() == Registration.TEXTURE_WRENCH.get() && !player.isSneaking()) {
                    if (state.get(TEXTURE)<3) {
                        world.setBlockState(pos, state.with(TEXTURE, state.get(TEXTURE) + 1));
                    } else {
                        world.setBlockState(pos, state.with(TEXTURE, 0));
                    }
                }
                //NOT WORKING CURRENTLY
                if (item.getItem() == Registration.TEXTURE_WRENCH.get() && player.isSneaking()) {
                    if (state.get(TEXTURE)>0) {
                        world.setBlockState(pos, state.with(TEXTURE, state.get(TEXTURE) - 1));
                    } else {
                        world.setBlockState(pos, state.with(TEXTURE, 3));
                    }
                }
                System.out.println(item.getItem().toString());
            }
            return ActionResultType.SUCCESS;
        //}
        //return super.onBlockActivated(state, world, pos, player, hand, trace);
    }

    protected void dropContainedBlock(World worldIn, BlockPos pos) {
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

    //TODO add everywhere AND FIX!!! - is it fixed?? -> testing!

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(worldIn, pos);

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    public int getLightValue(BlockState state) {
        if (state.get(LIGHT_LEVEL)>15) {
            return 15;
        }
        return state.get(LIGHT_LEVEL);
    }
}
