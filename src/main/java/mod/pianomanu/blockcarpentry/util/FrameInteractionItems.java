package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

/**
 * Util class to manage appearance modifying items for certain frame block
 * things like light level and textures.
 *
 * @author PianoManu
 * @version 1.2 09/27/23
 */
public class FrameInteractionItems {
    public static boolean isModifier(Item item) {
        return isLightLevelModifier(item)
                || isOverlayModifier(item)
                || isDyeItem(item)
                || isFrictionModifierNegative(item)
                || isFrictionModifierPositive(item)
                || isExplosionResistanceModifierSingle(item)
                || isExplosionResistanceModifierUltra(item)
                || isEnchantingPowerModifier(item)
                || isSustainabilityModifier(item)
                || isEntityDestroyModifier(item);
    }

    public static boolean isLightLevelModifier(Item item) {
        List<Item> lightLevelModifiers = new ArrayList<>();
        lightLevelModifiers.add(Items.GLOWSTONE_DUST);
        lightLevelModifiers.add(Items.COAL);
        lightLevelModifiers.add(Items.CHARCOAL);

        return lightLevelModifiers.contains(item) && BCModConfig.LIGHTING_ENABLED.get();
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

    public static boolean isSustainabilityModifier(Item item) {
        return item == Items.BONE_MEAL && BCModConfig.SUSTAINABILITY_ENABLED.get();
    }

    public static boolean isEnchantingPowerModifier(Item item) {
        return item == Items.EXPERIENCE_BOTTLE && BCModConfig.ENCHANT_POWER_ENABLED.get();
    }

    public static boolean isFrictionModifierNegative(Item item) {
        return item == Items.BLUE_ICE && BCModConfig.FRICTION_ENABLED.get();
    }

    public static boolean isFrictionModifierPositive(Item item) {
        return item == Items.HONEY_BLOCK && BCModConfig.FRICTION_ENABLED.get();
    }

    public static boolean isExplosionResistanceModifierSingle(Item item) {
        return item == Items.FLINT && BCModConfig.EXPLOSION_RESISTANCE_ENABLED.get();
    }

    public static boolean isExplosionResistanceModifierUltra(Item item) {
        return item == Registration.EXPLOSION_RESISTANCE_BALL.get() && BCModConfig.EXPLOSION_RESISTANCE_ENABLED.get();
    }

    public static boolean isEntityDestroyModifier(Item item) {
        return item == Items.NETHER_STAR && BCModConfig.CAN_ENTITY_DESTROY_ENABLED.get();
    }


}
//========SOLI DEO GLORIA========//