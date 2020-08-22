package mod.pianomanu.blockcarpentry.block;

import net.minecraft.block.BlockState;

import java.util.function.Supplier;

public class SlopeFrameBlock extends StairsFrameBlock {
    public SlopeFrameBlock(Supplier<BlockState> state, Properties properties) {
        super(state, properties);
    }
}
