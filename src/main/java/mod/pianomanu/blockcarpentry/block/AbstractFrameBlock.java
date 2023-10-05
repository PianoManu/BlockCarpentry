package mod.pianomanu.blockcarpentry.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.extensions.IForgeBlock;
import net.minecraftforge.common.property.Properties;

import javax.annotation.Nullable;

/**
 * Basic class for frame blocks (WIP)
 * Everything here is just for test purposes and subject to change
 *
 * @author PianoManu
 * @version 1.5 09/27/23
 */
public abstract class AbstractFrameBlock extends Block implements IFrameBlock, IForgeBlock {

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
     * @return see {@link ActionResultType}
     */
    @Override
    public ActionResultType onBlockActivated(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitresult) {
        return frameUse(state, level, pos, player, hand, hitresult);
    }

    /**
     * This method returns the light value of the block, i.e. the emitted light level
     *
     * @param state state of the block
     * @param pos   block position
     * @return new amount of light that is emitted by the block
     */
    @Override
    public int getLightValue(BlockState state, IBlockReader level, BlockPos pos) {
        return IFrameBlock.getLightValue(state);
    }

    public abstract BlockState getStateForPlacement(BlockItemUseContext context);

    @Override
    @SuppressWarnings("deprecation")
    public abstract VoxelShape getShape(BlockState state, IBlockReader getter, BlockPos pos, ISelectionContext context);

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        return IFrameBlock.super.canSustainPlant(state, world, pos, facing, plantable);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
//========SOLI DEO GLORIA========//