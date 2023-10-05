package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.item.BCToolItem;
import mod.pianomanu.blockcarpentry.item.BaseFrameItem;
import mod.pianomanu.blockcarpentry.item.BaseIllusionItem;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.tileentity.IFrameTile;
import mod.pianomanu.blockcarpentry.util.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResultType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.extensions.IForgeBlock;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Basic interface for frame blocks (WIP)
 * Everything here is just for test purposes and subject to change
 *
 * @author PianoManu
 * @version 1.4 09/27/23
 */
public interface IFrameBlock extends IForgeBlock {
    BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;
    BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    IntegerProperty LIGHT_LEVEL = BCBlockStateProperties.LIGHT_LEVEL;
    DirectionProperty FACING = BlockStateProperties.FACING;


    static int getLightValue(BlockState state) {
        if (state.get(LIGHT_LEVEL) > 15) {
            return 15;
        }
        return state.get(LIGHT_LEVEL);
    }

    default boolean shouldCallFrameUse(BlockState state, ItemStack itemStack) {
        return !state.get(CONTAINS_BLOCK) || FrameInteractionItems.isModifier(itemStack.getItem()) || (itemStack.getItem() instanceof BCToolItem);
    }

    default boolean removeBlock(World level, BlockPos pos, BlockState state, ItemStack itemStack, PlayerEntity player) {
        if (itemStack.getItem() == Registration.HAMMER.get() || (!BCModConfig.HAMMER_NEEDED.get() && player.isCrouching())) {
            if (!player.isCreative())
                this.dropContainedBlock(level, pos);
            else {
                this.clearTile(level, pos);
            }
            state = state.with(CONTAINS_BLOCK, Boolean.FALSE);
            level.setBlockState(pos, state, 2);
            return true;
        }
        return false;
    }

    default void clearTile(World level, BlockPos pos) {
        if (!level.isRemote) {
            TileEntity tileentity = level.getTileEntity(pos);
            if (tileentity instanceof FrameBlockTile) {
                FrameBlockTile frameTileEntity = (FrameBlockTile) tileentity;
                BlockState blockState = frameTileEntity.getMimic();
                if (!(blockState == null)) {
                    frameTileEntity.clear();
                }
            }
        }
    }

    /**
     * Used to drop the contained block
     * We check the tile entity, get the block from the tile entity and drop it at the block pos plus some small random coords in the level
     *
     * @param level the level where we drop the block
     * @param pos   the block position where we drop the block
     */
    default void dropContainedBlock(World level, BlockPos pos) {
        if (!level.isRemote) {
            TileEntity tileEntity = level.getTileEntity(pos);
            if (tileEntity instanceof FrameBlockTile) {
                FrameBlockTile frameTileEntity = (FrameBlockTile) tileEntity;
                BlockState blockState = frameTileEntity.getMimic();
                if (!(blockState == null)) {
                    dropItemStackInWorld(level, pos, blockState);
                    frameTileEntity.clear();
                }
            }
        }
    }

    default void dropItemStackInWorld(World level, BlockPos pos, BlockState blockState) {
        level.playEvent(1010, pos, 0);
        float f = 0.7F;
        double dx = (double) (level.rand.nextFloat() * f) + (double) 0.15F;
        double dy = (double) (level.rand.nextFloat() * f) + (double) f;
        double dz = (double) (level.rand.nextFloat() * f) + (double) 0.15F;
        ItemStack itemStack = new ItemStack(blockState.getBlock());
        ItemEntity itemEntity = new ItemEntity(level, (double) pos.getX() + dx, (double) pos.getY() + dy, (double) pos.getZ() + dz, itemStack);
        itemEntity.setDefaultPickupDelay();
        level.addEntity(itemEntity);
    }

    /**
     * Used to place a block in a frame. Therefor we need the tile entity of the block and set its mimic to the given block state.
     * Lastly, we update the block state (useful for observers or something, idk)
     *
     * @param level     the level where we drop the block
     * @param pos       the block position where we drop the block
     * @param state     the old block state
     * @param handBlock the block state of the held block - the block we want to insert into the frame
     */
    default void insertBlock(World level, BlockPos pos, BlockState state, BlockState handBlock) {
        TileEntity TileEntity = level.getTileEntity(pos);
        if (isCorrectTileInstance(TileEntity)) {
            fillTileEntity(level, pos, state, handBlock, TileEntity);
        }
    }

    default void fillTileEntity(World level, BlockPos pos, BlockState state, BlockState handBlock, TileEntity TileEntity) {
        FrameBlockTile frameTileEntity = (FrameBlockTile) TileEntity;
        frameTileEntity.clear();
        frameTileEntity.setMimic(handBlock);
        level.setBlockState(pos, state.with(CONTAINS_BLOCK, Boolean.TRUE), 2);
    }

    default ActionResultType frameUse(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitresult) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (hand == Hand.MAIN_HAND) {
            if (!level.isRemote) {
                return frameUseServer(state, level, pos, player, itemStack, hitresult);
            } else {
                return frameUseClient(state, level, pos, player, itemStack, hitresult);
            }
        }
        return ActionResultType.FAIL;
    }

    default ActionResultType frameUseServer(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack, BlockRayTraceResult hitresult) {
        if (removeBlock(level, pos, state, itemStack, player))
            return ActionResultType.SUCCESS;
        if (state.get(CONTAINS_BLOCK)) {
            if (executeModifications(state, level, pos, player, itemStack))
                return ActionResultType.CONSUME;
        }
        if (itemStack.getItem() instanceof BlockItem) {
            if (changeMimic(state, level, pos, player, itemStack))
                return ActionResultType.SUCCESS;
        }
        return itemStack.getItem() instanceof BlockItem ? ActionResultType.PASS : ActionResultType.CONSUME;
    }

    default ActionResultType frameUseClient(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack, BlockRayTraceResult hitresult) {
        return itemStack.getItem() instanceof BlockItem ? ActionResultType.SUCCESS : ActionResultType.CONSUME;
    }

    default boolean changeMimic(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        if (state.get(BCBlockStateProperties.CONTAINS_BLOCK) || itemStack.getItem() instanceof BaseFrameItem || itemStack.getItem() instanceof BaseIllusionItem) {
            return false;
        }
        TileEntity TileEntity = level.getTileEntity(pos);
        int count = itemStack.getCount();
        Block heldBlock = ((BlockItem) itemStack.getItem()).getBlock();
        if (isCorrectTileInstance(TileEntity) && !itemStack.isEmpty() && BlockSavingHelper.isValidBlock(heldBlock) && !state.get(CONTAINS_BLOCK)) {
            BlockState handBlockState = ((BlockItem) itemStack.getItem()).getBlock().getDefaultState();
            insertBlock(level, pos, state, handBlockState);
            if (!player.isCreative())
                itemStack.setCount(count - 1);
        }
        return true;
    }

    default boolean isCorrectTileInstance(TileEntity TileEntity) {
        return TileEntity instanceof FrameBlockTile;
    }

    default <V extends IFrameTile> V getTile(IBlockReader level, BlockPos pos) {
        TileEntity be = level.getTileEntity(pos);
        if (IFrameTile.class.isAssignableFrom(Objects.requireNonNull(be).getClass())) {
            return (V) be;
        }
        return null;
    }

    default boolean executeModifications(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        return BlockAppearanceHelper.setAll(itemStack, state, level, pos, player) || getTile(level, pos) != null && BlockModificationHelper.setAll(itemStack, getTile(level, pos), player);
    }

    @Override
    default float getSlipperiness(BlockState state, IWorldReader level, BlockPos pos, @Nullable Entity entity) {
        if (getTile(level, pos) != null)
            return getTile(level, pos).getSlipperiness();
        return state.getBlock().getSlipperiness();
    }

    @Override
    default boolean canSustainPlant(BlockState state, IBlockReader level, BlockPos pos, Direction facing, IPlantable plantable) {
        return canSustainPlant(level, pos, facing);
    }

    default boolean canSustainPlant(IBlockReader level, BlockPos pos, Direction facing) {
        if (getTile(level, pos) != null)
            return getTile(level, pos).getCanSustainPlant() && Block.hasSolidSideOnTop(level, pos);
        return false;
    }

    @Override
    default float getExplosionResistance(BlockState state, IBlockReader level, BlockPos pos, Explosion explosion) {
        if (getTile(level, pos) != null)
            return getTile(level, pos).getExplosionResistance();
        return IForgeBlock.super.getExplosionResistance(state, level, pos, explosion);
    }

    @Override
    default float getEnchantPowerBonus(BlockState state, IWorldReader level, BlockPos pos) {
        if (getTile(level, pos) != null)
            return getTile(level, pos).getEnchantPowerBonus();
        return IForgeBlock.super.getEnchantPowerBonus(state, level, pos);
    }

    @Override
    default boolean canEntityDestroy(BlockState state, IBlockReader level, BlockPos pos, Entity entity) {
        if (getTile(level, pos) != null)
            return getTile(level, pos).getCanEntityDestroy();
        return IForgeBlock.super.canEntityDestroy(state, level, pos, entity);
    }
}

//========SOLI DEO GLORIA========//