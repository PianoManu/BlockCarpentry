package mod.pianomanu.blockcarpentry.setup.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

/**
 * Config class for customizable values, values can be found and changed in the corresponding file at /config/blockcarpentry-common.toml and /config/blockcarpentry-client.toml
 * @author PianoManu
 * @version 1.3 10/21/20
 */
@Mod.EventBusSubscriber
public class BCModConfig {
    public static final String CATEGORY_TOOLS = "tools";
    public static final String CATEGORY_BLOCKS = "blocks";
    //public static final String CATEGORY_CLIENT = "client_things";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    //private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    //public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.BooleanValue HAMMER_NEEDED;
    public static ForgeConfigSpec.BooleanValue SNEAK_FOR_TEXTURE_WRENCH_TO_ROTATE_MIMICKED_BLOCKS;
    public static ForgeConfigSpec.BooleanValue SNEAK_FOR_VERTICAL_SLABS;
    //public static ForgeConfigSpec.BooleanValue OPAQUE_BLOCKS;

    static {
        COMMON_BUILDER.comment("Tool settings").push(CATEGORY_TOOLS);
        setupToolSettings();
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Block settings").push(CATEGORY_BLOCKS);
        setupBlockSettings();
        COMMON_BUILDER.pop();

        /*CLIENT_BUILDER.comment("Optifine Work-Around").push(CATEGORY_CLIENT);
        setupClientSettings();
        CLIENT_BUILDER.pop();*/

        COMMON_CONFIG = COMMON_BUILDER.build();
        //CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupToolSettings() {
        HAMMER_NEEDED = COMMON_BUILDER.comment("Determines whether you need a hammer to remove blocks from a frame, when set to false, you can remove blocks from a frame by sneaking and right-clicking the block (default: true)").define("hammer_needed",true);
        //SNEAK_FOR_TEXTURE_WRENCH_TO_ROTATE_MIMICKED_BLOCKS = COMMON_BUILDER.comment("Determines whether you have to sneak, when rotating a mimicked block. When set to true: right-clicking on a frame block without sneaking will change its texture, right-clicking on a frame block while sneaking will rotate the")
    }

    private static void setupBlockSettings() {
        SNEAK_FOR_VERTICAL_SLABS = COMMON_BUILDER.comment("Determines whether you have to sneak when placing vertical slabs, when set to false, frame slabs will always be placed on the side of the block; when set to true, frame slabs will be placed like vanilla slabs, and you have to sneak in order to make place vertical slabs (default: true)").define("sneak_for_vertical_slabs",true);
    }

    private static void setupClientSettings() {
        //OPAQUE_BLOCKS = CLIENT_BUILDER.comment("Temporary work-around for OptiFine: when OptiFine is installed, frame/illusion blocks may appear invisible. You can enable this setting (change value to true) to make blocks opaque (they will no longer be invisible when using OptiFine), but blocks like glass or slime blocks lose their transparency. When set to false (default value), blocks with transparent parts will be transparent, but when using OptiFine, they will be invisible").define("opaque_blocks", false);
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

    /*@SubscribeEvent
    public static void onLoad(final ModConfigEvent configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }*/
}
//========SOLI DEO GLORIA========//