package mod.pianomanu.blockcarpentry.setup;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is used to register textures to the Texture Atlas (texture stitching)
 *
 * @author PianoManu
 * @version 1.2 09/30/20
 */
@Mod.EventBusSubscriber(modid = BlockCarpentryMain.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TextureSetup {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onStitchEvent(TextureStitchEvent.Pre event) {
        ResourceLocation stitching = event.getMap().getTextureLocation();
        if (stitching.equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            LOGGER.info("Stitching textures from BlockCarpentry");
            event.addSprite(loc("block/chest_front"));
            event.addSprite(loc("block/chest_side"));
            event.addSprite(loc("block/chest_top"));
            event.addSprite(loc("block/grass_block_snow_overlay"));
            event.addSprite(loc("block/grass_block_snow_overlay_small"));
            event.addSprite(loc("block/grass_block_side_overlay_large"));
            event.addSprite(loc("block/stone_brick_overlay"));
            event.addSprite(loc("block/brick_overlay"));
            event.addSprite(loc("block/chiseled_sandstone_overlay"));
            event.addSprite(loc("block/boundary_overlay"));
            event.addSprite(loc("block/chiseled_stone_overlay"));
            LOGGER.info("Stitched all textures from BlockCarpentry");
        }
    }

    private static ResourceLocation loc(String name) {
        return new ResourceLocation(BlockCarpentryMain.MOD_ID, name);
    }
}
//========SOLI DEO GLORIA========//