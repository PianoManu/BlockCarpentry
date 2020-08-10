package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.BedFrameTile;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import mod.pianomanu.blockcarpentry.util.BlockSavingHelper;
import mod.pianomanu.blockcarpentry.util.LightLevelHelper;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.*;
import net.minecraft.state.properties.BedPart;
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
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;

public class BedFrameBlock extends BedBlock {
    private boolean pillowFlag = false;
    public static final BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;
    public static final IntegerProperty LIGHT_LEVEL = BCBlockStateProperties.LIGHT_LEVEL;
    //public static final IntegerProperty TEXTURE = BCBlockStateProperties.TEXTURE;
    public static final IntegerProperty PILLOW_COLOR = BCBlockStateProperties.PILLOW_COLOR;
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BedFrameBlock(DyeColor colorIn, Properties properties) {
        super(colorIn, properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(CONTAINS_BLOCK, false).with(LIGHT_LEVEL,0).with(PART, BedPart.FOOT).with(OCCUPIED, Boolean.valueOf(false)).with(HORIZONTAL_FACING, Direction.NORTH));//.with(TEXTURE,0));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CONTAINS_BLOCK, LIGHT_LEVEL, PART, OCCUPIED, HORIZONTAL_FACING);//, TEXTURE);
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
                    Block heldBlock = ((BlockItem) item.getItem()).getBlock();
                    //TODO fix for non-solid blocks
                    //heldBlock.getShape(heldBlock.getDefaultState(),world,pos, ISelectionContext.dummy());
                    if (tileEntity instanceof FrameBlockTile && !item.isEmpty() && BlockSavingHelper.isValidBlock(heldBlock) && !state.get(CONTAINS_BLOCK)) {
                        ((FrameBlockTile) tileEntity).clear();
                        BlockState handBlockState = ((BlockItem) item.getItem()).getBlock().getDefaultState();
                        ((FrameBlockTile) tileEntity).setMimic(handBlockState);
                        insertBlock(world,pos, state,handBlockState);
                        //this.contained_block=handBlockState.getBlock();
                        player.getHeldItem(hand).setCount(count-1);
                    }
                }
            }
            LightLevelHelper.setLightLevel(item,state,world,pos,player,hand);
            /*if (item.getItem() == Registration.TEXTURE_WRENCH.get() && !player.isSneaking()) {
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
            if (Tags.Items.DYES.contains(item.getItem())) {

            }*/
            if(item.getItem() instanceof BlockItem) {
                Block heldBlock = ((BlockItem) item.getItem()).getBlock();
                if (state.get(CONTAINS_BLOCK) && !pillowFlag && BlockSavingHelper.isWoolBlock(heldBlock)) {
                    TileEntity te = world.getTileEntity(pos);
                    int count = player.getHeldItem(hand).getCount();
                    if (te instanceof BedFrameTile) {
                        BedFrameTile bedFrameTile = (BedFrameTile) te;
                        bedFrameTile.setPillow(heldBlock.getDefaultState());
                        player.getHeldItem(hand).setCount(count - 1);
                        pillowFlag = true;
                    }
                } else if (state.get(CONTAINS_BLOCK) && pillowFlag && BlockSavingHelper.isWoolBlock(heldBlock)) {
                    TileEntity te = world.getTileEntity(pos);
                    int count = player.getHeldItem(hand).getCount();
                    if (te instanceof BedFrameTile) {
                        BedFrameTile bedFrameTile = (BedFrameTile) te;
                        bedFrameTile.setBlanket(heldBlock.getDefaultState());
                        player.getHeldItem(hand).setCount(count - 1);
                        pillowFlag = false;
                    }
                }
            }
            if (item.getItem() == Registration.CHISEL.get()) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof BedFrameTile) {
                    BedFrameTile bedFrameTile = (BedFrameTile) te;
                    bedFrameTile.getDesign();
                }
            }
            System.out.println(item.getItem().toString());
        }
        return ActionResultType.SUCCESS;
    }

    protected void dropContainedBlock(World worldIn, BlockPos pos) {
        if (!worldIn.isRemote) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof BedFrameTile) {
                BedFrameTile frameTileEntity = (BedFrameTile) tileentity;
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
                }
                BlockState blockState2 = frameTileEntity.getBlanket();
                if (!(blockState2==null)) {
                    worldIn.playEvent(1010, pos, 0);
                    frameTileEntity.clear();
                    float f = 0.7F;
                    double d0 = (double)(worldIn.rand.nextFloat() * 0.7F) + (double)0.15F;
                    double d1 = (double)(worldIn.rand.nextFloat() * 0.7F) + (double)0.060000002F + 0.6D;
                    double d2 = (double)(worldIn.rand.nextFloat() * 0.7F) + (double)0.15F;
                    ItemStack itemstack1 = blockState2.getBlock().asItem().getDefaultInstance();
                    ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, itemstack1);
                    itementity.setDefaultPickupDelay();
                    worldIn.addEntity(itementity);
                }
                BlockState blockState3 = frameTileEntity.getPillow();
                if (!(blockState3==null)) {
                    worldIn.playEvent(1010, pos, 0);
                    frameTileEntity.clear();
                    float f = 0.7F;
                    double d0 = (double)(worldIn.rand.nextFloat() * 0.7F) + (double)0.15F;
                    double d1 = (double)(worldIn.rand.nextFloat() * 0.7F) + (double)0.060000002F + 0.6D;
                    double d2 = (double)(worldIn.rand.nextFloat() * 0.7F) + (double)0.15F;
                    ItemStack itemstack1 = blockState3.getBlock().asItem().getDefaultInstance();
                    ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, itemstack1);
                    itementity.setDefaultPickupDelay();
                    worldIn.addEntity(itementity);
                }
                frameTileEntity.clear();
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

    @SuppressWarnings("deprecation")
    public int getLightValue(BlockState state) {
        if (state.get(LIGHT_LEVEL)>15) {
            return 15;
        }
        return state.get(LIGHT_LEVEL);
    }
}
