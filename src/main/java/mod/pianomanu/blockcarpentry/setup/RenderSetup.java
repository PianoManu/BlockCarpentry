package mod.pianomanu.blockcarpentry.setup;

import mod.pianomanu.blockcarpentry.client.ChestFrameScreen;
import mod.pianomanu.blockcarpentry.client.IllusionChestScreen;
import net.minecraft.client.gui.screens.MenuScreens;

/**
 * Registering standard rendering layers for all frame blocks - need to be translucent to work with glass and similar blocks
 *
 * @author PianoManu
 * @version 1.1 11/07/22
 */
public class RenderSetup {
    public static void setup() {


        //ScreenManager.registerFactory(Registration.STANDING_SIGN_FRAME_CONTAINER.get(), EditSignScreen::new);
        MenuScreens.register(Registration.CHEST_FRAME_CONTAINER.get(), ChestFrameScreen::new);
        MenuScreens.register(Registration.CHEST_ILLUSION_CONTAINER.get(), IllusionChestScreen::new);
    }
}
//========SOLI DEO GLORIA========//