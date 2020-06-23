package mod.pianomanu.blockcarpentry.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BlockSavingHelper {
    public static List<Block> validBlocks = new ArrayList<>();
    public static void createValidBlockList() {
        List<Block> blockList = new ArrayList<>();
        for(Block b : ForgeRegistries.BLOCKS) {
            if (b.isSolid(b.getDefaultState())) {
                blockList.add(b);
            }
        }
        blockList.add(Blocks.GLASS);
        Collection<Block> glass = Tags.Blocks.GLASS.getAllElements();
        blockList.addAll(glass);
        blockList.add(Blocks.ICE);
        validBlocks = blockList;
    }

    public static boolean isValidBlock(Block block) {
        return validBlocks.contains(block);
    }
}
