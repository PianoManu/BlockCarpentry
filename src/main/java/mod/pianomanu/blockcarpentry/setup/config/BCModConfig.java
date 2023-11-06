package mod.pianomanu.blockcarpentry.setup.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

/**
 * Config class for customizable values, values can be found and changed in /config/blockcarpentry-common.toml
 *
 * @author PianoManu
 * @version 1.7 11/03/23
 */
@Mod.EventBusSubscriber
public class BCModConfig {
    public static final String CATEGORY_CLIENT = "client";
    public static final String CATEGORY_TOOLS = "tools";
    public static final String CATEGORY_BLOCKS = "blocks";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue SHOW_DISCORD_INVITATION;
    public static ForgeConfigSpec.BooleanValue ONLY_SHOW_INVITATION_ONCE;

    public static ForgeConfigSpec.BooleanValue SHOW_COMPLEX_BOUNDING_BOX;
    public static ForgeConfigSpec.BooleanValue USE_COMPLEX_QUAD_CALCULATIONS;

    public static ForgeConfigSpec.BooleanValue HAMMER_NEEDED;
    public static ForgeConfigSpec.BooleanValue SNEAK_FOR_VERTICAL_SLABS;
    public static ForgeConfigSpec.BooleanValue LIGHTING_ENABLED;
    public static ForgeConfigSpec.BooleanValue FRICTION_ENABLED;
    public static ForgeConfigSpec.BooleanValue EXPLOSION_RESISTANCE_ENABLED;
    public static ForgeConfigSpec.BooleanValue SUSTAINABILITY_ENABLED;
    public static ForgeConfigSpec.BooleanValue ENCHANT_POWER_ENABLED;
    public static ForgeConfigSpec.BooleanValue CAN_ENTITY_DESTROY_ENABLED;
    public static ForgeConfigSpec.DoubleValue FRICTION_MIN_BOUNDARY;
    public static ForgeConfigSpec.DoubleValue FRICTION_MAX_BOUNDARY;
    public static ForgeConfigSpec.DoubleValue FRICTION_MODIFIER;
    public static ForgeConfigSpec.DoubleValue EXPLOSION_RESISTANCE_MAX;
    public static ForgeConfigSpec.DoubleValue EXPLOSION_RESISTANCE_MODIFIER;

    static {
        COMMON_BUILDER.comment("Client settings").push(CATEGORY_CLIENT);
        setupClientSettings();
        COMMON_BUILDER.pop();

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
        SHOW_COMPLEX_BOUNDING_BOX = COMMON_BUILDER.comment("Whether the bounding box should be recalculated everytime a corner of a frame or illusion cube block is moved. Setting this to \"true\" can cause lag spikes on low-end PCs during the calculation process.").define("complex_bounding_boxes", true);

        USE_COMPLEX_QUAD_CALCULATIONS = COMMON_BUILDER.comment("Whether the collapsable blocks should use the complex quad calculation method for their faces. When set to \"true\", the model will look smoother, but the calculation is more resource-intensive. When set to \"false\", the vanilla system is used instead.").define("complex_quad_calculations", true);

        SNEAK_FOR_VERTICAL_SLABS = COMMON_BUILDER.comment("Determines whether you have to sneak when placing vertical slabs, when set to false, frame slabs will always be placed on the side of the block; when set to true, frame slabs will be placed like vanilla slabs, and you have to sneak in order to make place vertical slabs (default: true)").define("sneak_for_vertical_slabs", true);

        LIGHTING_ENABLED = COMMON_BUILDER.comment("Determines whether the player can use coal, charcoal or glowstone dust to let frame or illusion blocks glow.").define("lighting_enabled", true);

        FRICTION_ENABLED = COMMON_BUILDER.comment("Determines whether the player can use blue ice or honey blocks to increase the frame or illusion block's friction.").define("friction_enabled", true);
        FRICTION_MAX_BOUNDARY = COMMON_BUILDER.comment("Maximum amount of friction modification of a frame or illusion block. Normally, a block has a friction of 0.6. Larger values make the block seem more sticky. However, values > 1 seem to negate the effect.").defineInRange("friction_max", 1, 0.6d, Double.MAX_VALUE);
        FRICTION_MIN_BOUNDARY = COMMON_BUILDER.comment("Minimum amount of friction modification of a frame or illusion block. Normally, a block has a friction of 0.6. Smaller values make the player move faster over the block.").defineInRange("friction_min", 0.2d, 0d, 0.6d);
        FRICTION_MODIFIER = COMMON_BUILDER.comment("The factor, with which the friction is modified, when the corresponding block is applied to the frame or illusion block. Factor 1 means nothing will change at all!").defineInRange("friction_modifier", 1.05, 1, Double.MAX_VALUE);

        EXPLOSION_RESISTANCE_ENABLED = COMMON_BUILDER.comment("Determines whether the player can use flint or the obsidian compound chunk to increase the frame or illusion block's explosion resistance value.").define("explosion_resistance_enabled", true);
        EXPLOSION_RESISTANCE_MAX = COMMON_BUILDER.comment("Maximum amount of explosion resistance modification of a frame or illusion block. For comparison: Stone has 6, Obsidian has 1200.").defineInRange("explosion_resistance_max", 1200, 1, Double.MAX_VALUE);
        EXPLOSION_RESISTANCE_MODIFIER = COMMON_BUILDER.comment("The factor, with which the explosion resistance is modified with a single Flint item. With the default values, 123 Flint items are required to achieve maximum explosion resistance.").defineInRange("explosion_resistance_modifier", 1.05, 1, Double.MAX_VALUE);

        SUSTAINABILITY_ENABLED = COMMON_BUILDER.comment("Determines whether the player can use bone meal to allow for plants planted on top of frame or illusion blocks.").define("sustainability_enabled", true);

        ENCHANT_POWER_ENABLED = COMMON_BUILDER.comment("Determines whether the player can use experience bottles to give frame or illusion blocks the same enchanting power that bookshelves have.").define("enchant_power_enabled", true);

        CAN_ENTITY_DESTROY_ENABLED = COMMON_BUILDER.comment("Determines whether the player can use nether stars on frame or illusion blocks such that certain entities (such as the Ender Dragon and the Wither) can no longer destroy frame or illusion blocks.").define("entity_destroy_modifier_enabled", true);
    }

    private static void setupClientSettings() {
        SHOW_DISCORD_INVITATION = COMMON_BUILDER.comment("Whether the discord server invitation should be displayed when entering a world.").define("show_discord_invitation", true);
        ONLY_SHOW_INVITATION_ONCE = COMMON_BUILDER.comment("Set to true, if the discord server invitation message should only be shown once. If set to false, you can manually disable the message via the \"show_discord_message\" config entry.").define("only_show_invitation_once", false);
    }
}
//========SOLI DEO GLORIA========//