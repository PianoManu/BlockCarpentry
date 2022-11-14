package mod.pianomanu.blockcarpentry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Basic class for frame blocks (WIP)
 * Everything here is just for test purposes and subject to change
 *
 * @author PianoManu
 * @version 1.3 11/14/22
 */
public abstract class AbstractFrameBlock extends BaseEntityBlock implements IFrameBlock {

    public AbstractFrameBlock(Properties properties) {
        super(properties);
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
        if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            return frameUse(state, level, pos, player, hand, hitresult);
        }
        return player.getItemInHand(hand).getItem() instanceof BlockItem ? InteractionResult.SUCCESS : InteractionResult.PASS;
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
        return IFrameBlock.getLightEmission(state);
    }

    public abstract BlockState getStateForPlacement(BlockPlaceContext context);

    @Override
    @SuppressWarnings("deprecation")
    public abstract VoxelShape getVisualShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context);

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }
}
//========SOLI DEO GLORIA========//