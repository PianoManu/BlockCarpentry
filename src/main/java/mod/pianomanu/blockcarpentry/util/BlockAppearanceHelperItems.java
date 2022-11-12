package mod.pianomanu.blockcarpentry.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Util class to manage appearance modifying items for certain frame block
 * things like light level and textures.
 *
 * @author PianoManu
 * @version 1.0 11/12/22
 */
public class BlockAppearanceHelperItems {
    public static boolean isAppearanceModifier(Item item) {
        return isLightLevelModifier(item) || isOverlayModifier(item) || isDyeItem(item);
    }

    public static boolean isLightLevelModifier(Item item) {
        List<Item> lightLevelModifiers = new ArrayList<>();
        lightLevelModifiers.add(Items.GLOWSTONE_DUST);
        lightLevelModifiers.add(Items.COAL);
        lightLevelModifiers.add(Items.CHARCOAL);

        return lightLevelModifiers.contains(item);
    }

    public static boolean isOverlayModifier(Item item) {
        List<Item> lightLevelModifiers = new ArrayList<>();
        lightLevelModifiers.add(Items.GUNPOWDER);
        lightLevelModifiers.add(Items.GRASS);
        lightLevelModifiers.add(Items.SNOWBALL);
        lightLevelModifiers.add(Items.VINE);

        return lightLevelModifiers.contains(item);
    }

    public static boolean isDyeItem(Item item) {
        List<Item> dye_items = new ArrayList<>();
        dye_items.add(Items.WHITE_DYE);
        dye_items.add(Items.RED_DYE);
        dye_items.add(Items.MAGENTA_DYE);
        dye_items.add(Items.YELLOW_DYE);
        dye_items.add(Items.PURPLE_DYE);
        dye_items.add(Items.LIME_DYE);
        dye_items.add(Items.GREEN_DYE);
        dye_items.add(Items.LIGHT_BLUE_DYE);
        dye_items.add(Items.BLUE_DYE);
        dye_items.add(Items.PINK_DYE);
        dye_items.add(Items.ORANGE_DYE);
        dye_items.add(Items.LIGHT_GRAY_DYE);
        dye_items.add(Items.GRAY_DYE);
        dye_items.add(Items.CYAN_DYE);
        dye_items.add(Items.BROWN_DYE);
        dye_items.add(Items.BLACK_DYE);
        return dye_items.contains(item);
    }

    public static List<Item> getDyeItems() {
        List<Item> dye_items = new ArrayList<>();
        dye_items.add(Items.WHITE_DYE);
        dye_items.add(Items.ORANGE_DYE);
        dye_items.add(Items.MAGENTA_DYE);
        dye_items.add(Items.LIGHT_BLUE_DYE);
        dye_items.add(Items.YELLOW_DYE);
        dye_items.add(Items.LIME_DYE);
        dye_items.add(Items.PINK_DYE);
        dye_items.add(Items.GRAY_DYE);
        dye_items.add(Items.LIGHT_GRAY_DYE);
        dye_items.add(Items.CYAN_DYE);
        dye_items.add(Items.PURPLE_DYE);
        dye_items.add(Items.BLUE_DYE);
        dye_items.add(Items.BROWN_DYE);
        dye_items.add(Items.GREEN_DYE);
        dye_items.add(Items.RED_DYE);
        dye_items.add(Items.BLACK_DYE);
        return dye_items;
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