package mod.pianomanu.blockcarpentry.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

/**
 * Currently unused, may be used (or rewritten) for frame beds
 * @author PianoManu
 * @version 1.0 08/29/20
 */
public class BlockSavingHelper {
    public static List<Block> validBlocks = new ArrayList<>();
    public static void createValidBlockList() {
        List<Block> blockList = new ArrayList<>();
        for(Block b : ForgeRegistries.BLOCKS) {
            if (b.getDefaultState().isSolid()) {
                blockList.add(b);
            }
        }
        blockList.add(Blocks.GLASS);
        //Collection<Block> glass = Tags.Blocks.GLASS.func_230236_b_();
        //blockList.addAll(glass);
        blockList.add(Blocks.ICE);
        validBlocks = blockList;
    }

    public static boolean isValidBlock(Block block) {
        return validBlocks.contains(block);
    }

    public static boolean isDyeItem(Item item) {
        return Tags.Items.DYES.func_230235_a_(item);
    }

    public static boolean isWoolBlock(Block block) {
        List<Block> wool_blocks = new ArrayList<>();
        wool_blocks.add(Blocks.WHITE_WOOL);
        wool_blocks.add(Blocks.RED_WOOL);
        wool_blocks.add(Blocks.MAGENTA_WOOL);
        wool_blocks.add(Blocks.YELLOW_WOOL);
        wool_blocks.add(Blocks.PURPLE_WOOL);
        wool_blocks.add(Blocks.LIME_WOOL);
        wool_blocks.add(Blocks.GREEN_WOOL);
        wool_blocks.add(Blocks.LIGHT_BLUE_WOOL);
        wool_blocks.add(Blocks.BLUE_WOOL);
        wool_blocks.add(Blocks.PINK_WOOL);
        wool_blocks.add(Blocks.ORANGE_WOOL);
        wool_blocks.add(Blocks.LIGHT_GRAY_WOOL);
        wool_blocks.add(Blocks.GRAY_WOOL);
        wool_blocks.add(Blocks.CYAN_WOOL);
        wool_blocks.add(Blocks.BROWN_WOOL);
        wool_blocks.add(Blocks.BLACK_WOOL);
        return wool_blocks.contains(block);
    }
}
