package mod.pianomanu.blockcarpentry.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

/**
 * Basic class for rotatable blocks like slabs
 * Nothing special here right now...
 *
 * @author PianoManu
 * @version 1.0 08/15/21
 */
public abstract class AbstractSixWayFrameBlock extends AbstractFrameBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public AbstractSixWayFrameBlock(Properties properties) {
        super(properties);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
}
//========SOLI DEO GLORIA========//