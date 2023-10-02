package mod.pianomanu.blockcarpentry.setup;

import mod.pianomanu.blockcarpentry.client.ChestFrameScreen;
import mod.pianomanu.blockcarpentry.client.IllusionChestScreen;
import mod.pianomanu.blockcarpentry.renderer.block.FrameSignRenderer;
import mod.pianomanu.blockcarpentry.util.BCWoodType;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * Registering standard rendering layers for all frame blocks - need to be translucent to work with glass and similar blocks
 *
 * @author PianoManu
 * @version 1.2 10/02/23
 */
public class RenderSetup {
    public static void setup() {


        //ScreenManager.registerFactory(Registration.STANDING_SIGN_FRAME_CONTAINER.get(), EditSignScreen::new);
        MenuScreens.register(Registration.CHEST_FRAME_CONTAINER.get(), ChestFrameScreen::new);
        MenuScreens.register(Registration.CHEST_ILLUSION_CONTAINER.get(), IllusionChestScreen::new);

        WoodType.register(BCWoodType.FRAME);
        WoodType.register(BCWoodType.ILLUSION);

        BlockEntityRenderers.register(Registration.SIGN_FRAME_TILE.get(), FrameSignRenderer::new);
        BlockEntityRenderers.register(Registration.SIGN_ILLUSION_TILE.get(), FrameSignRenderer::new);
    }
}
//========SOLI DEO GLORIA========//