package mod.pianomanu.blockcarpentry.setup;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.model.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Class for client setup
 * Things like model loaders are registered here
 * @author PianoManu
 * @version 1.0
 */
@Mod.EventBusSubscriber(modid = BlockCarpentryMain.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_block_loader"), new IllusionBlockModelLoader());

        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frameloader"), new FrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_slab_loader"), new SlabFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_slab_top_loader"), new SlabFrameTopModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_button_loader"), new ButtonFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_button_pressed_loader"), new ButtonPressedFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_pressure_plate_loader"), new PressurePlateFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_pressure_plate_pressed_loader"), new PressurePlatePressedFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_stairs_loader"), new StairsFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_door_loader"), new DoorFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_trapdoor_loader"), new TrapdoorFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_fence_loader"), new FenceFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_edged_slope_loader"), new EdgedSlopeFrameModelLoader());

        //SlabFrameBottomRenderer.register();
        //ClientRegistry.bindTileEntityRenderer(Registration.BUTTON_FRAME_TILE.get(), ButtonFrameTileEntityRenderer::new);
        //ClientRegistry.bindTileEntityRenderer(Registration.BED_FRAME_TILE.get(), BedFrameTileEntityRenderer::new);
    }
}
