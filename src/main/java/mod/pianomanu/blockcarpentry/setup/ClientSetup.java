package mod.pianomanu.blockcarpentry.setup;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.model.*;
import mod.pianomanu.blockcarpentry.renderer.ButtonFrameTileEntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BlockCarpentryMain.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frameloader"), new FrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_slab_bottom_loader"), new SlabFrameBottomModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_slab_top_loader"), new SlabFrameTopModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_button_loader"), new ButtonFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_pressure_plate_loader"), new PressurePlateFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_pressure_plate_pressed_loader"), new PressurePlatePressedFrameModelLoader());

        //SlabFrameBottomRenderer.register();
        ClientRegistry.bindTileEntityRenderer(Registration.BUTTON_FRAME_TILE.get(), ButtonFrameTileEntityRenderer::new);
    }
}
