package mod.pianomanu.blockcarpentry.setup;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.model.*;
import mod.pianomanu.blockcarpentry.renderer.block.FrameSignRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Class for client setup
 * Things like model loaders are registered here
 *
 * @author PianoManu
 * @version 1.3 10/02/23
 */
@Mod.EventBusSubscriber(modid = BlockCarpentryMain.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void init(final ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frameloader"), new FrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_slab_loader"), new SlabFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_button_loader"), new ButtonFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_pressure_plate_loader"), new PressurePlateFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_stairs_loader"), new StairsFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_door_loader"), new DoorFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_trapdoor_loader"), new TrapdoorFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_fence_loader"), new FenceFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_bed_loader"), new BedFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_wall_loader"), new WallFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_ladder_loader"), new LadderFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_chest_loader"), new ChestFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_fence_gate_loader"), new FenceGateFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_carpet_loader"), new CarpetFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_pane_loader"), new PaneFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_daylight_detector_loader"), new DaylightDetectorFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_layered_block_loader"), new LayeredBlockFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_slope_loader"), new SlopeFrameModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "frame_edged_slope_loader"), new EdgedSlopeFrameModelLoader());

        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_block_loader"), new IllusionBlockModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_slab_loader"), new IllusionSlabModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_button_loader"), new IllusionButtonModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_pressure_plate_loader"), new IllusionPressurePlateModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_stairs_loader"), new IllusionStairsModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_door_loader"), new IllusionDoorModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_trapdoor_loader"), new IllusionTrapdoorModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_fence_loader"), new IllusionFenceModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_bed_loader"), new IllusionBedModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_wall_loader"), new IllusionWallModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_ladder_loader"), new IllusionLadderModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_chest_loader"), new IllusionChestModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_fence_gate_loader"), new IllusionFenceGateModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_carpet_loader"), new IllusionCarpetModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_pane_loader"), new IllusionPaneModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_daylight_detector_loader"), new IllusionDaylightDetectorModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BlockCarpentryMain.MOD_ID, "illusion_layered_block_loader"), new IllusionLayeredBlockModelLoader());
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(Registration.SIGN_FRAME_TILE.get(), FrameSignRenderer::new);
    }
}
//========SOLI DEO GLORIA========//