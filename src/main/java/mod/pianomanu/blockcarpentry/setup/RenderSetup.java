package mod.pianomanu.blockcarpentry.setup;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

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
        RenderTypeLookup.setRenderLayer(Registration.EDGED_SLOPE_FRAMEBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.ILLUSION_BLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(Registration.SLOPE_FRAMEBLOCK.get(), RenderType.getTranslucent());
    }
}
