package mod.pianomanu.blockcarpentry.util;

import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * Currently unused, may be used (or rewritten) for frame beds
 * @author PianoManu
 * @version 1.2 09/21/20
 */
public class BlockSavingHelper {
    public static List<Block> validBlocks = new ArrayList<>();
    public static void createValidBlockList() {
        List<Block> blockList = new ArrayList<>();
        for(Block b : ForgeRegistries.BLOCKS) {
            if (b.getDefaultState().isSolid()) {
                blockList.add(b);
            }
            if (b instanceof AbstractGlassBlock) {
                blockList.add(b);
            }
            if (b instanceof IceBlock) {
                blockList.add(b);
            }
            if (b instanceof SlimeBlock) {
                blockList.add(b);
            }
            if (b instanceof HoneyBlock) {
                blockList.add(b);
            }
        }
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
//========SOLI DEO GLORIA========//