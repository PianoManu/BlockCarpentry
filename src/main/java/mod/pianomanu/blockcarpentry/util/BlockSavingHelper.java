package mod.pianomanu.blockcarpentry.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to check model render shapes for blocks.
 *
 * @author PianoManu
 * @version 1.2 11/05/23
 */
public class BlockSavingHelper {
    public static List<Block> validBlocks = new ArrayList<>();

    public static void createValidBlockList() {
        List<Block> blockList = new ArrayList<>();
        for (Block b : ForgeRegistries.BLOCKS) {
            if (b.getDefaultState().getRenderType() == BlockRenderType.MODEL && !(Tags.isFrameBlock(b) || Tags.isIllusionBlock(b))) {
                blockList.add(b);
            }
        }
        validBlocks = blockList;
    }

    public static boolean isValidBlock(Block block, boolean isClientSide) {
        if (isClientSide)
            return validBlocks.contains(block) && TextureHelper.getTextures(block.getDefaultState()).size() > 0;
        return validBlocks.contains(block);
    }

}
//========SOLI DEO GLORIA========//