package mod.pianomanu.blockcarpentry.setup;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.model.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Class for client setup
 * Things like model loaders are registered here
 *
 * @author PianoManu
 * @version 1.2 09/20/23
 */
@Mod.EventBusSubscriber(modid = BlockCarpentryMain.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void init(final ModelEvent.RegisterGeometryLoaders event) {
        event.register("frameloader", new FrameModelLoader());
        event.register("frame_slab_loader", new SlabFrameModelLoader());
        event.register("frame_button_loader", new ButtonFrameModelLoader());
        event.register("frame_pressure_plate_loader", new PressurePlateFrameModelLoader());
        event.register("frame_stairs_loader", new StairsFrameModelLoader());
        event.register("frame_door_loader", new DoorFrameModelLoader());
        event.register("frame_trapdoor_loader", new TrapdoorFrameModelLoader());
        event.register("frame_fence_loader", new FenceFrameModelLoader());
        event.register("frame_bed_loader", new BedFrameModelLoader());
        event.register("frame_wall_loader", new WallFrameModelLoader());
        event.register("frame_ladder_loader", new LadderFrameModelLoader());
        event.register("frame_chest_loader", new ChestFrameModelLoader());
        event.register("frame_fence_gate_loader", new FenceGateFrameModelLoader());
        event.register("frame_carpet_loader", new CarpetFrameModelLoader());
        event.register("frame_pane_loader", new PaneFrameModelLoader());
        event.register("frame_daylight_detector_loader", new DaylightDetectorFrameModelLoader());
        event.register("frame_layered_block_loader", new LayeredBlockFrameModelLoader());
        event.register("frame_slope_loader", new SlopeFrameModelLoader());
        event.register("frame_edged_slope_loader", new EdgedSlopeFrameModelLoader());

        event.register("illusion_block_loader", new IllusionBlockModelLoader());
        event.register("illusion_slab_loader", new IllusionSlabModelLoader());
        event.register("illusion_button_loader", new IllusionButtonModelLoader());
        event.register("illusion_pressure_plate_loader", new IllusionPressurePlateModelLoader());
        event.register("illusion_stairs_loader", new IllusionStairsModelLoader());
        event.register("illusion_door_loader", new IllusionDoorModelLoader());
        event.register("illusion_trapdoor_loader", new IllusionTrapdoorModelLoader());
        event.register("illusion_fence_loader", new IllusionFenceModelLoader());
        event.register("illusion_bed_loader", new IllusionBedModelLoader());
        event.register("illusion_wall_loader", new IllusionWallModelLoader());
        event.register("illusion_ladder_loader", new IllusionLadderModelLoader());
        event.register("illusion_chest_loader", new IllusionChestModelLoader());
        event.register("illusion_fence_gate_loader", new IllusionFenceGateModelLoader());
        event.register("illusion_carpet_loader", new IllusionCarpetModelLoader());
        event.register("illusion_pane_loader", new IllusionPaneModelLoader());
        event.register("illusion_daylight_detector_loader", new IllusionDaylightDetectorModelLoader());
        event.register("illusion_layered_block_loader", new IllusionLayeredBlockModelLoader());
    }
}
//========SOLI DEO GLORIA========//