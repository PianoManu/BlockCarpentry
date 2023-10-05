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
 * @version 1.1 11/12/22
 */
public class BlockSavingHelper {
    public static List<Block> validBlocks = new ArrayList<>();

    public static void createValidBlockList() {
        List<Block> blockList = new ArrayList<>();
        for (Block b : ForgeRegistries.BLOCKS) {
            if (b.getDefaultState().getRenderType() == BlockRenderType.MODEL) {
                blockList.add(b);
            }
        }
        validBlocks = blockList;
    }

    public static boolean isValidBlock(Block block) {
        return validBlocks.contains(block);
    }

}
//========SOLI DEO GLORIA========//