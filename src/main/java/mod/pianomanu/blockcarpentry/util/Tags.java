package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * Just some tag stuff, might be migrated to another class later on...
 *
 * @author PianoManu
 * @version 1.0 05/23/22
 */
public class Tags {
    private static final List<Block> frameBlocks = new ArrayList<>();
    private static final List<Block> illusionBlocks = new ArrayList<>();

    public static boolean isFrameBlock(Block b) {
        return frameBlocks.contains(b);
    }

    public static boolean isIllusionBlock(Block b) {
        return illusionBlocks.contains(b);
    }

    public static void init() {
        createFrameBlockList();
        createIllusionBlockList();
    }

    public static void createFrameBlockList() {
        frameBlocks.clear();
        frameBlocks.add(Registration.FRAMEBLOCK.get());
        frameBlocks.add(Registration.SLAB_FRAMEBLOCK.get());
        frameBlocks.add(Registration.BUTTON_FRAMEBLOCK.get());
        frameBlocks.add(Registration.PRESSURE_PLATE_FRAMEBLOCK.get());
        frameBlocks.add(Registration.STAIRS_FRAMEBLOCK.get());
        frameBlocks.add(Registration.DOOR_FRAMEBLOCK.get());
        frameBlocks.add(Registration.TRAPDOOR_FRAMEBLOCK.get());
        frameBlocks.add(Registration.FENCE_FRAMEBLOCK.get());
        frameBlocks.add(Registration.BED_FRAMEBLOCK.get());
        frameBlocks.add(Registration.WALL_FRAMEBLOCK.get());
        frameBlocks.add(Registration.LADDER_FRAMEBLOCK.get());
        frameBlocks.add(Registration.CHEST_FRAMEBLOCK.get());
        frameBlocks.add(Registration.FENCE_GATE_FRAMEBLOCK.get());
        frameBlocks.add(Registration.CARPET_FRAMEBLOCK.get());
        frameBlocks.add(Registration.PANE_FRAMEBLOCK.get());
        frameBlocks.add(Registration.DAYLIGHT_DETECTOR_FRAMEBLOCK.get());
        frameBlocks.add(Registration.SLOPE_FRAMEBLOCK.get());
        frameBlocks.add(Registration.EDGED_SLOPE_FRAMEBLOCK.get());
    }

    public static void createIllusionBlockList() {
        illusionBlocks.clear();
        illusionBlocks.add(Registration.ILLUSION_BLOCK.get());
        illusionBlocks.add(Registration.SLAB_ILLUSIONBLOCK.get());
        illusionBlocks.add(Registration.BUTTON_ILLUSIONBLOCK.get());
        illusionBlocks.add(Registration.PRESSURE_PLATE_ILLUSIONBLOCK.get());
        illusionBlocks.add(Registration.STAIRS_ILLUSIONBLOCK.get());
        illusionBlocks.add(Registration.DOOR_ILLUSIONBLOCK.get());
        illusionBlocks.add(Registration.TRAPDOOR_ILLUSIONBLOCK.get());
        illusionBlocks.add(Registration.FENCE_ILLUSIONBLOCK.get());
        illusionBlocks.add(Registration.BED_ILLUSIONBLOCK.get());
        illusionBlocks.add(Registration.WALL_ILLUSIONBLOCK.get());
        illusionBlocks.add(Registration.LADDER_ILLUSIONBLOCK.get());
        illusionBlocks.add(Registration.CHEST_ILLUSIONBLOCK.get());
        illusionBlocks.add(Registration.FENCE_GATE_ILLUSIONBLOCK.get());
        illusionBlocks.add(Registration.CARPET_ILLUSIONBLOCK.get());
        illusionBlocks.add(Registration.PANE_ILLUSIONBLOCK.get());
        illusionBlocks.add(Registration.DAYLIGHT_DETECTOR_ILLUSIONBLOCK.get());
    }
}
