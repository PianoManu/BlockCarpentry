package mod.pianomanu.blockcarpentry.setup;

import mod.pianomanu.blockcarpentry.client.ChestFrameScreen;
import mod.pianomanu.blockcarpentry.client.IllusionChestScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.RenderType;

/**
 * Registering standard rendering layers for all frame blocks - need to be translucent to work with glass and similar blocks
 *
 * @author PianoManu
 * @version 1.2 10/02/23
 */
public class RenderSetup {
    public static void setup() {
        RenderTypeLookup.setRenderLayer(Registration.FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.SLAB_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.STAIRS_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.FENCE_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.DOOR_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.TRAPDOOR_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.PRESSURE_PLATE_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.BUTTON_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.BED_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.WALL_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.LADDER_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.CHEST_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.FENCE_GATE_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.CARPET_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.PANE_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.DAYLIGHT_DETECTOR_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.LAYERED_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.STANDING_SIGN_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.WALL_SIGN_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.EDGED_SLOPE_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.SLOPE_FRAMEBLOCK.get(), RenderType.getTranslucent());

        RenderTypeLookup.setRenderLayer(Registration.ILLUSION_BLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.SLAB_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.STAIRS_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.FENCE_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.DOOR_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.TRAPDOOR_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.PRESSURE_PLATE_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.BUTTON_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.BED_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.WALL_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.LADDER_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.CHEST_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.FENCE_GATE_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.CARPET_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.PANE_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.DAYLIGHT_DETECTOR_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.LAYERED_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.STANDING_SIGN_ILLUSIONBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.WALL_SIGN_ILLUSIONBLOCK.get(), RenderType.getTranslucent());


        ScreenManager.registerFactory(Registration.CHEST_FRAME_CONTAINER.get(), ChestFrameScreen::new);
        ScreenManager.registerFactory(Registration.CHEST_ILLUSION_CONTAINER.get(), IllusionChestScreen::new);
        //ScreenManager.registerFactory(Registration.STANDING_SIGN_FRAME_CONTAINER.get(), EditSignScreen::new);

        //WoodType.register(BCWoodType.FRAME);
        //WoodType.register(BCWoodType.ILLUSION);

        //TileEntityRenderers.register(Registration.SIGN_FRAME_TILE.get(), FrameSignRenderer::new);
        //TileEntityRenderers.register(Registration.SIGN_ILLUSION_TILE.get(), FrameSignRenderer::new);
    }
}
//========SOLI DEO GLORIA========//