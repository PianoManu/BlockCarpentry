package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.item.BaseFrameItem;
import mod.pianomanu.blockcarpentry.item.BaseIllusionItem;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.extensions.IForgeBlockState;

import javax.annotation.Nullable;


/**
 * Main class for frameblocks - all important block info can be found here
 * This class is the most basic one for all frame blocks, so you can find most of the documentation here
 *
 * @author PianoManu
 * @version 1.1 06/13/22
 */
@SuppressWarnings("deprecation")
public class FrameBlock extends AbstractFrameBlock implements IForgeBlockState, SimpleWaterloggedBlock {
    /**
     * Block property (can be seed when pressing F3 in-game)
     * This is needed, because we need to detect whether the blockstate has changed
     */
    public static final BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape MIDDLE_STRIP_NORTH = Block.box(0.0, 8.0, 1.0, 16.0, 9.0, 2.0);
    private static final VoxelShape MIDDLE_STRIP_EAST = Block.box(14.0, 8.0, 2.0, 15.0, 9.0, 14.0);
    private static final VoxelShape MIDDLE_STRIP_SOUTH = Block.box(0.0, 8.0, 14.0, 16.0, 9.0, 15.0);
    private static final VoxelShape MIDDLE_STRIP_WEST = Block.box(1.0, 8.0, 2.0, 2.0, 9.0, 14.0);
    private static final VoxelShape TOP = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape DOWN = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    private static final VoxelShape NW_PILLAR = Block.box(0.0, 1.0, 0.0, 2.0, 15.0, 2.0);
    private static final VoxelShape SW_PILLAR = Block.box(0.0, 1.0, 14.0, 2.0, 15.0, 16.0);
    private static final VoxelShape NE_PILLAR = Block.box(14.0, 1.0, 0.0, 16.0, 15.0, 2.0);
    private static final VoxelShape SE_PILLAR = Block.box(14.0, 1.0, 14.0, 16.0, 15.0, 16.0);
    private static final VoxelShape MID = Block.box(2.0, 1.0, 2.0, 14.0, 15.0, 14.0);
    private static final VoxelShape CUBE = Shapes.or(MIDDLE_STRIP_EAST, MIDDLE_STRIP_SOUTH, MIDDLE_STRIP_WEST, MIDDLE_STRIP_NORTH, TOP, DOWN, NW_PILLAR, SW_PILLAR, NE_PILLAR, SE_PILLAR, MID);

    /**
     * classic constructor, all default values are set
     *
     * @param properties determined when registering the block (see {@link Registration}
     */
    public FrameBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONTAINS_BLOCK, Boolean.FALSE).setValue(LIGHT_LEVEL, 0).setValue(WATERLOGGED, false));//.setValue(TEXTURE,0));
    }

    /**
     * Assign needed blockstates to frame block - we need "contains_block" and "light_level", both because we have to check for blockstate changes
     */
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    /**
     * Yep, it's a complex block structure, so we need a tile entity
     *
     * @param state regardless of its state, it always has a BlockEntity
     * @return regardless of its state, it always has a BlockEntity -> returns true every time
     */
    /*@Override
    public boolean hasBlockEntity(BlockState state) {
        return true;
    }*/

    /**
     * When placed, this method is called and a new FrameBlockTile is created
     * This is needed to store a block inside the frame, change its light value etc.
     *
     * @param pos   regardless of the position, we always create the BlockEntity
     * @param state regardless of its state, we always create the BlockEntity
     * @return the new empty FrameBlock-BlockEntity
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FrameBlockTile(pos, state);
    }

    /**
     * Is called, whenever the block is right-clicked:
     * First it is checked, whether the world is remote (this has to be done client-side only).
     * Afterwards, we check, whether the held item is some sort of block item (e.g. logs, but not torches)
     * If that's the case, we ask for the tile entity of the frame and if the frame is empty, we fill it with the held block and remove the item from the player's inventory
     * If the frame is not empty and the player holds the hammer, the contained block is dropped into the world
     *
     * @param state     state of the block that is clicked
     * @param level     world the block is placed in
     * @param pos       position (x,y,z) of block
     * @param player    entity of the player that includes all important information (health, armor, inventory,
     * @param hand      which hand is used (e.g. you have a sword in your main hand and an axe in your off-hand and right click a log -> you use the off-hand, not the main hand)
     * @param hitresult to determine which part of the block is clicked (upper half, lower half, right side, left side, corners...)
     * @return see {@link InteractionResult}
     */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitresult) {
        ItemStack item = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (BlockAppearanceHelper.setLightLevel(item, state, level, pos, player, hand) ||
                    BlockAppearanceHelper.setTexture(item, state, level, player, pos) ||
                    BlockAppearanceHelper.setDesign(level, pos, player, item) ||
                    BlockAppearanceHelper.setDesignTexture(level, pos, player, item) ||
                    BlockAppearanceHelper.setOverlay(level, pos, player, item) ||
                    BlockAppearanceHelper.setRotation(level, pos, player, item))
                return InteractionResult.SUCCESS;
            if (item.getItem() instanceof BlockItem) {
                if (state.getValue(BCBlockStateProperties.CONTAINS_BLOCK) || item.getItem() instanceof BaseFrameItem || item.getItem() instanceof BaseIllusionItem) {
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
                    //TODO
                    //checkForVisibility(state, level, pos, (FrameBlockTile) tileEntity);
                }
            }
            //hammer is needed to remove the block from the frame - you can change it in the config
            if (player.getItemInHand(hand).getItem() == Registration.HAMMER.get() || (!BCModConfig.HAMMER_NEEDED.get() && player.isCrouching())) {
                if (!player.isCreative())
                    this.dropContainedBlock(level, pos);
                state = state.setValue(CONTAINS_BLOCK, Boolean.FALSE);
                level.setBlock(pos, state, 2);
            }
        }
        return item.getItem() instanceof BlockItem ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    /**
     * Used to drop the contained block
     * We check the tile entity, get the block from the tile entity and drop it at the block pos plus some small random coords in the level
     *
     * @param levelIn the level where we drop the block
     * @param pos     the block position where we drop the block
     */
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

    /**
     * Used to place a block in a frame. Therefor we need the tile entity of the block and set its mimic to the given block state.
     * Lastly, we update the block state (useful for observers or something, idk)
     *
     * @param levelIn   the level where we drop the block
     * @param pos       the block position where we drop the block
     * @param state     the old block state
     * @param handBlock the block state of the held block - the block we want to insert into the frame
     */
    public void insertBlock(Level levelIn, BlockPos pos, BlockState state, BlockState handBlock) {
        BlockEntity tileentity = levelIn.getBlockEntity(pos);
        if (tileentity instanceof FrameBlockTile) {
            FrameBlockTile frameBlockEntity = (FrameBlockTile) tileentity;
            frameBlockEntity.clear();
            frameBlockEntity.setMimic(handBlock);
            levelIn.setBlock(pos, state.setValue(CONTAINS_BLOCK, Boolean.TRUE), 2);
        }
    }

    /**
     * This method is called, whenever the state of the block changes (e.g. the block is harvested)
     *
     * @param state    old blockstate
     * @param levelIn  level of the block
     * @param pos      block position
     * @param newState new blockstate
     * @param isMoving whether the block has some sort of motion (should never be moving - false)
     */
    @Override
    public void onRemove(BlockState state, Level levelIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(levelIn, pos);

            super.onRemove(state, levelIn, pos, newState, isMoving);
        }
        if (state.getValue(WATERLOGGED)) {
            levelIn.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelIn));
        }
    }

    /**
     * This method returns the light value of the block, i.e. the emitted light level
     *
     * @param state state of the block
     * @param level level the block is in
     * @param pos   block position
     * @return new amount of light that is emitted by the block
     */
    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.getValue(LIGHT_LEVEL) > 15) {
            return 15;
        }
        return state.getValue(LIGHT_LEVEL);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(blockpos);
        if (fluidstate.getType() == Fluids.WATER) {
            return this.defaultBlockState().setValue(WATERLOGGED, fluidstate.isSource());
        } else {
            return this.defaultBlockState();
        }
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (!state.getValue(CONTAINS_BLOCK)) {
            return CUBE;
        }
        return Shapes.block();
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor levelIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            levelIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelIn));
        }

        return super.updateShape(stateIn, facing, facingState, levelIn, currentPos, facingPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (!state.getValue(CONTAINS_BLOCK)) {
            return CUBE;
        }
        return Shapes.block();
    }

    //TODO
    /*@OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        return adjacentBlockState.is(this) || super.isSideInvisible(state, adjacentBlockState, side);// || BlockCullingHelper.skipSideRendering(adjacentBlockState);
    }

    private void checkForVisibility(BlockState state, Level level, BlockPos pos, FrameBlockTile tileEntity) {
        for (Direction d : Direction.values()) {
            BlockPos.MutableBlockPos mutablePos = pos.mutable();
            BlockState adjacentBlockState = level.getBlockState(mutablePos.move(d));
            tileEntity.setVisibileSides(d, !(adjacentBlockState.isSolid() || isSideInvisible(state, adjacentBlockState, d)));
            System.out.println(d + " " + !(adjacentBlockState.isSolid() || isSideInvisible(state, adjacentBlockState, d)));
        }
    }*/
}
//========SOLI DEO GLORIA========//