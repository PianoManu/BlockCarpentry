package mod.pianomanu.blockcarpentry.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;

public class BlockCullingHelper {

    public static boolean skipSideRendering(BlockState state) {
        if (state.isSolid())
            return true;
        Block b = state.getBlock();
        if (b instanceof StairsBlock)
            return false;
        return !(b instanceof SlabBlock);
    }
}
//========SOLI DEO GLORIA========//