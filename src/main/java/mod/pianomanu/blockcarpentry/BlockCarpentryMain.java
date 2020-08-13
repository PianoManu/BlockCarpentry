package mod.pianomanu.blockcarpentry;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.RenderSetup;
import mod.pianomanu.blockcarpentry.util.BlockSavingHelper;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

import static mod.pianomanu.blockcarpentry.BlockCarpentryMain.MOD_ID;

/**
 * Main class of the BlockCarpentry mod
 *
 * @author PianoManu
 * @version 1.0
 */
@Mod(MOD_ID)
public class BlockCarpentryMain
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "blockcarpentry";
    //TODO main klasse aufräumen - check
    //TODO Hauptverzeichnis aufräumen

    public BlockCarpentryMain() {
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
    }

    /**
     * doing setup stuff (currently unused)
     */
    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Setting up BlockCarpentry mod");
    }

    /**
     * client stuff, i.e. things that can only be done client-side, like rendering
     */
    private void doClientStuff(final FMLClientSetupEvent event) {
        RenderSetup.setup();
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
        LOGGER.info("Processed InterModCommunication");
    }

    /**
     * Server stuff (currently unused)
     */
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
    }

    /**
     * Registering my ItemGroup for all blocks and items from BlockCarpentry
     */
    public static class BlockCarpentryItemGroup extends ItemGroup {

        public static final BlockCarpentryItemGroup BLOCK_CARPENTRY = new BlockCarpentryItemGroup(ItemGroup.GROUPS.length,"blockcarpentry");
        private BlockCarpentryItemGroup(int index, String label) {
            super(index, label);
        }

        @Override
        @Nonnull
        public ItemStack createIcon() {
            return new ItemStack(Registration.FRAMEBLOCK.get());
        }
    }
}
