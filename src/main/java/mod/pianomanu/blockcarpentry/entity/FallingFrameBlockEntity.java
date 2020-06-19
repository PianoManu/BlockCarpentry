package mod.pianomanu.blockcarpentry.entity;

import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import mod.pianomanu.blockcarpentry.util.BlockContainerProperty;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.world.World;

public class FallingFrameBlockEntity extends FallingBlockEntity {

    private BlockContainerProperty contains = BCBlockStateProperties.CONTAINS;
    private BlockState containedBlock = null;

    public FallingFrameBlockEntity(EntityType<? extends FallingBlockEntity> p_i50218_1_, World p_i50218_2_) {
        super(p_i50218_1_, p_i50218_2_);
    }

    public FallingFrameBlockEntity(World worldIn, double x, double y, double z, BlockState fallingBlockState, BlockState containedBlockState) {
        super(worldIn, x, y, z, fallingBlockState);
        this.containedBlock = containedBlockState;
    }

    public BlockState getContainedBlock() {
        return containedBlock;
    }
}
