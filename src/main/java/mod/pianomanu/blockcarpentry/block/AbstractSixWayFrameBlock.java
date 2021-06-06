package mod.pianomanu.blockcarpentry.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;

/**
 * Basic class for rotatable blocks like slabs
 * Nothing special here right now...
 *
 * @author PianoManu
 * @version 1.0 06/06/21
 */
public abstract class AbstractSixWayFrameBlock extends AbstractFrameBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public AbstractSixWayFrameBlock(Properties properties) {
        super(properties);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }
}
//========SOLI DEO GLORIA========//