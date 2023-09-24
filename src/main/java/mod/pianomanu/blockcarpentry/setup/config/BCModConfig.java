package mod.pianomanu.blockcarpentry.setup.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

/**
 * Config class for customizable values, values can be found and changed in the corresponding file at /config/blockcarpentry-common.toml and /config/blockcarpentry-client.toml
 *
 * @author PianoManu
 * @version 1.4 09/24/23
 */
@Mod.EventBusSubscriber
public class BCModConfig {
    public static final String CATEGORY_TOOLS = "tools";
    public static final String CATEGORY_BLOCKS = "blocks";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue HAMMER_NEEDED;
    public static ForgeConfigSpec.BooleanValue SNEAK_FOR_VERTICAL_SLABS;
    public static ForgeConfigSpec.DoubleValue FRICTION_MIN_BOUNDARY;
    public static ForgeConfigSpec.DoubleValue FRICTION_MAX_BOUNDARY;
    public static ForgeConfigSpec.DoubleValue FRICTION_MODIFIER;
    public static ForgeConfigSpec.DoubleValue EXPLOSION_RESISTANCE_MAX;
    public static ForgeConfigSpec.DoubleValue EXPLOSION_RESISTANCE_MODIFIER;

    static {
        COMMON_BUILDER.comment("Tool settings").push(CATEGORY_TOOLS);
        setupToolSettings();
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Block settings").push(CATEGORY_BLOCKS);
        setupBlockSettings();
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void setupToolSettings() {
        HAMMER_NEEDED = COMMON_BUILDER.comment("Determines whether you need a hammer to remove blocks from a frame, when set to false, you can remove blocks from a frame by sneaking and right-clicking the block (default: true)").define("hammer_needed", true);
    }

    private static void setupBlockSettings() {
        SNEAK_FOR_VERTICAL_SLABS = COMMON_BUILDER.comment("Determines whether you have to sneak when placing vertical slabs, when set to false, frame slabs will always be placed on the side of the block; when set to true, frame slabs will be placed like vanilla slabs, and you have to sneak in order to make place vertical slabs (default: true)").define("sneak_for_vertical_slabs", true);

        FRICTION_MAX_BOUNDARY = COMMON_BUILDER.comment("Maximum amount of friction modification of a frame or illusion block. Normally, a block has a friction of 0.6. Larger values make the block seem more sticky. However, values > 1 seem to negate the effect.").defineInRange("friction_max", 1, 0.6d, Double.MAX_VALUE);
        FRICTION_MIN_BOUNDARY = COMMON_BUILDER.comment("Minimum amount of friction modification of a frame or illusion block. Normally, a block has a friction of 0.6. Smaller values make the player move faster over the block.").defineInRange("friction_min", 0.2d, 0d, 0.6d);
        FRICTION_MODIFIER = COMMON_BUILDER.comment("The factor, with which the friction is modified, when the corresponding block is applied to the frame or illusion block. Factor 1 means nothing will change at all!").defineInRange("friction_modifier", 1.05, 1, Double.MAX_VALUE);

        EXPLOSION_RESISTANCE_MAX = COMMON_BUILDER.comment("Maximum amount of explosion resistance modification of a frame or illusion block. For comparison: Stone has 6, Obsidian has 1200.").defineInRange("explosion_resistance_max", 1200, 1, Double.MAX_VALUE);
        EXPLOSION_RESISTANCE_MODIFIER = COMMON_BUILDER.comment("The factor, with which the explosion resistance is modified with a single Flint item. With the default values, 123 Flint items are required to achieve maximum explosion resistance.").defineInRange("explosion_resistance_modifier", 1.05, 1, Double.MAX_VALUE);
    }
}
//========SOLI DEO GLORIA========//