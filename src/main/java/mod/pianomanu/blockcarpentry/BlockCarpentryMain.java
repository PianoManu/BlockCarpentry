package mod.pianomanu.blockcarpentry;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.RenderSetup;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.util.BlockColorHandler;
import mod.pianomanu.blockcarpentry.util.BlockSavingHelper;
import mod.pianomanu.blockcarpentry.util.Tags;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.patchouli.api.PatchouliAPI;

import static mod.pianomanu.blockcarpentry.BlockCarpentryMain.MOD_ID;

/**
 * Main class of the BlockCarpentry mod
 *
 * @author PianoManu
 * @version 1.1 09/19/23
 */
@Mod(MOD_ID)
public class BlockCarpentryMain
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "blockcarpentry";
    //TODO main klasse aufräumen - check
    //TODO Hauptverzeichnis aufräumen

    public BlockCarpentryMain() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BCModConfig.COMMON_CONFIG);
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);

        LOGGER.info("Registering all blocks and items from BlockCarpentry");
        Registration.init();
        LOGGER.info("Registered all blocks and items from BlockCarpentry");
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(BlockCarpentryMain::onCreativeModeTabRegister);
    }

    public static void onCreativeModeTabRegister(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MOD_ID, "all"), builder -> builder
                .icon(() -> new ItemStack(Registration.FRAMEBLOCK.get()))
                .title(Component.translatable("itemGroup.blockcarpentry"))
                .displayItems((params, output, gen) -> {
                    output.accept(new ItemStack(Registration.FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.SLAB_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.STAIRS_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.BUTTON_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.PRESSURE_PLATE_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.DOOR_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.TRAPDOOR_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.LADDER_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.FENCE_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.FENCE_GATE_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.WALL_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.BED_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.CHEST_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.CARPET_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.PANE_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.DAYLIGHT_DETECTOR_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.LAYERED_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.SIGN_FRAME_ITEM.get()));
                    output.accept(new ItemStack(Registration.SLOPE_FRAMEBLOCK.get()));
                    output.accept(new ItemStack(Registration.EDGED_SLOPE_FRAMEBLOCK.get()));


                    output.accept(new ItemStack(Registration.ILLUSION_BLOCK.get()));
                    output.accept(new ItemStack(Registration.SLAB_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.STAIRS_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.BUTTON_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.PRESSURE_PLATE_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.DOOR_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.TRAPDOOR_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.LADDER_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.FENCE_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.FENCE_GATE_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.WALL_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.BED_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.CHEST_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.CARPET_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.PANE_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.DAYLIGHT_DETECTOR_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.LAYERED_ILLUSIONBLOCK.get()));
                    output.accept(new ItemStack(Registration.SIGN_ILLUSION_ITEM.get()));

                    output.accept(new ItemStack(Registration.HAMMER.get()));
                    output.accept(new ItemStack(Registration.TEXTURE_WRENCH.get()));
                    output.accept(new ItemStack(Registration.CHISEL.get()));
                    output.accept(new ItemStack(Registration.PAINTBRUSH.get()));
                    output.accept(new ItemStack(Registration.DEBUG_ITEM.get()));
                    output.accept(new ItemStack(Registration.EXPLOSION_RESISTANCE_BALL.get()));
                    output.accept(PatchouliAPI.get().getBookStack(new ResourceLocation("blockcarpentry", "carpentering_manual")));
                })
                .build()
        );
    }

    /**
     * doing setup stuff (currently unused)
     */
    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Setting up BlockCarpentry mod");
    }

    /**
     * client stuff, i.e. things that can only be done client-side, like rendering
     */
    private void doClientStuff(final FMLClientSetupEvent event) {
        /*if (!BCModConfig.OPAQUE_BLOCKS.get()) {
            LOGGER.warn("Config value \"Opaque Blocks\" is set to false. When using OptiFine, frame and illusion blocks may appear invisible. If that is the case, change the value of \"Opaque Blocks\" to \"true\" in the mod config");
            RenderSetup.setup();
        }*/
        RenderSetup.setup();
        BlockColorHandler.registerBlockColors();
        LOGGER.info("Setting up client things for BlockCarpentry");
    }

    /**
     * Dispatching inter-mod-communication
     */
    private void enqueueIMC(final InterModEnqueueEvent event)
    {

    }

    /**
     * Receiving and processing inter-mod-communication from other mods
     */
    private void processIMC(final InterModProcessEvent event)
    {
        LOGGER.info("Processing InterModCommunication");
        BlockSavingHelper.createValidBlockList();
        Tags.init();
        LOGGER.info("Processed InterModCommunication");
    }

    /**
     * Server stuff (currently unused)
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
//This mod is dedicated to the living God and His son, Jesus. Without His support, I would never have had enough strength and perseverance to get this project working and publish it. Learn to hear His voice, it will transform your life. (Based on a quote from Covert_Jaguar, creator of RailCraft)
//========SOLI DEO GLORIA========//