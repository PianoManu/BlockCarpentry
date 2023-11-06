package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.bakedmodels.ModelInformation;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.tileentity.IFrameTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Util class for picking the right texture of a block given certain inputs.
 * Those functions are useful when creating cuboids and determining the texture for each face.
 * See {@link mod.pianomanu.blockcarpentry.util.ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.7 11/05/23
 */
public class TextureHelper {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Used for dyed glass doors, trapdoors etc
     *
     * @return list of all glass textures
     */
    public static List<TextureAtlasSprite> getGlassTextures() {
        List<TextureAtlasSprite> glassTextures = new ArrayList<>();
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/white_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/orange_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/magenta_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/light_blue_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/yellow_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/lime_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/pink_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/gray_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/light_gray_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/cyan_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/purple_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/blue_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/brown_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/green_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/red_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/black_stained_glass")));
        return glassTextures;
    }

    public static List<TextureAtlasSprite> getWoolTextures() {
        List<TextureAtlasSprite> woolTextures = new ArrayList<>();
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/white_wool")));
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/orange_wool")));
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/magenta_wool")));
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/light_blue_wool")));
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/yellow_wool")));
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/lime_wool")));
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/pink_wool")));
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/gray_wool")));
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/light_gray_wool")));
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/cyan_wool")));
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/purple_wool")));
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/blue_wool")));
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/brown_wool")));
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/green_wool")));
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/red_wool")));
        woolTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/black_wool")));
        return woolTextures;
    }

    public static List<TextureAtlasSprite> getPlanksTextures() {
        List<TextureAtlasSprite> planksTextures = new ArrayList<>();
        planksTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/oak_planks")));
        planksTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/birch_planks")));
        planksTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/spruce_planks")));
        planksTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/jungle_planks")));
        planksTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/acacia_planks")));
        planksTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/dark_oak_planks")));
        planksTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/crimson_planks")));
        planksTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/warped_planks")));
        return planksTextures;
    }

    public static List<TextureAtlasSprite> getMetalTextures() {
        List<TextureAtlasSprite> metalTextures = new ArrayList<>();
        metalTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/iron_block")));
        metalTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/obsidian")));
        metalTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/stone")));
        metalTextures.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc("block/oak_log")));
        return metalTextures;
    }

    public static List<TextureAtlasSprite> getTextureFromModel(IBakedModel model, IModelData extraData, Random rand) {
        return getTextureFromModel(model, extraData.getData(FrameBlockTile.MIMIC), extraData, rand);
    }

    public static List<TextureAtlasSprite> getTextureFromModel(IBakedModel model, BlockState state, Random rand) {
        return getTextureFromModel(model, state, null, rand);
    }

    public static List<TextureAtlasSprite> getTextureFromModel(IBakedModel model, BlockState state, IModelData extraData, Random rand) {
        List<TextureAtlasSprite> textureList = new ArrayList<>();
        try {
            for (Direction dir :
                    Direction.values()) {
                for (BakedQuad quad : model.getQuads(state, dir, rand, extraData)) {
                    if (!textureList.contains(quad.func_187508_a())) {
                        textureList.add(quad.func_187508_a());
                    }
                }
            }
            return textureList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<TextureAtlasSprite> getTextures(BlockState state) {
        try {
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(BlockModelShapes.getModelLocation(state));
            Random rand;
            if (Minecraft.getInstance().world != null) {
                rand = Minecraft.getInstance().world.rand;
            } else {
                rand = new Random();
            }
            return TextureHelper.getTextureFromModel(model, state, rand);
        } catch (RuntimeException e) {
            return Collections.emptyList();
        }
    }

    public static List<TextureAtlasSprite> getTextures(IFrameTile tile) {
        return getTextures(tile.getMimic());
    }

    public static TextureAtlasSprite getTextureFromTileEntity(IFrameTile tile) {
        List<TextureAtlasSprite> sprites = getTextures(tile);
        if (sprites.size() > 0 && tile.getTexture() < sprites.size())
            return sprites.get(tile.getTexture());
        return sprites.size() > 0 ? sprites.get(0) : Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(MissingTextureSprite.getLocation());
    }

    private static ResourceLocation loc(String path) {
        return new ResourceLocation("minecraft", path);
    }

    public static TextureAtlasSprite getTexture(IBakedModel model, @Nonnull Random rand, @Nonnull IModelData extraData, ModelProperty<Integer> modelPropertyTexture) {
        List<TextureAtlasSprite> textureList = getTextureFromModel(model, extraData, rand);
        if (badTextureList(textureList))
            return getMissingTexture();
        TextureAtlasSprite texture;
        Integer tex = extraData.getData(modelPropertyTexture);
        if (tex == null) {
            LOGGER.error("Cannot determine model texture for model " + model);
            return getMissingTexture();
        } else if (textureList.size() <= tex) {
            extraData.setData(modelPropertyTexture, 0);
            tex = 0;
        }

        texture = textureList.get(tex);
        return texture;
    }

    public static TextureAtlasSprite getTexture(IBakedModel model, IModelData extraData, Random rand, Direction direction) {
        List<TextureAtlasSprite> textureList = getTextureFromModel(model, extraData, rand);
        if (badTextureList(textureList))
            return getMissingTexture();

        TextureAtlasSprite texture = textureList.get(0);
        for (BakedQuad quad : model.getQuads(extraData.getData(FrameBlockTile.MIMIC), direction, rand, extraData)) {
            texture = quad.func_187508_a();
        }
        return texture;
    }

    private static boolean badTextureList(List<TextureAtlasSprite> textureList) {
        return textureList.size() == 0;
    }

    public static TextureAtlasSprite getMissingTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("missing"));
    }

    public static ModelInformation getOverlayModelInformation(int overlayIndex) {
        int tintIndex = -1;
        TextureAtlasSprite overlay = null;
        TextureAtlasSprite upOverlay = null;
        TextureAtlasSprite downOverlay = null;
        if (overlayIndex == 1) {
            tintIndex = 1;
            overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/grass_block_side_overlay"));
            upOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/grass_block_top"));
        }
        if (overlayIndex == 2) {
            tintIndex = 1;
            overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_side_overlay_large"));
            upOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/grass_block_top"));
        }
        if (overlayIndex == 3) {
            overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_snow_overlay"));
            upOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/snow"));
        }
        if (overlayIndex == 4) {
            overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_snow_overlay_small"));
            upOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/snow"));
        }
        if (overlayIndex == 5) {
            tintIndex = 1;
            overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/vine"));
        }
        if (overlayIndex >= 6 && overlayIndex <= 10) {
            if (overlayIndex == 6) {
                overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/stone_brick_overlay"));
                upOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/stone_brick_overlay"));
                downOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/stone_brick_overlay"));
            }
            if (overlayIndex == 7) {
                overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/brick_overlay"));
                upOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/brick_overlay"));
                downOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/brick_overlay"));
            }
            if (overlayIndex == 8) {
                overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chiseled_sandstone_overlay"));
            }
            if (overlayIndex == 9) {
                overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/boundary_overlay"));
                upOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/boundary_overlay"));
                downOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/boundary_overlay"));
            }
            if (overlayIndex == 10) {
                overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chiseled_stone_overlay"));
                upOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chiseled_stone_overlay"));
                downOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chiseled_stone_overlay"));
            }
        }
        List<TextureAtlasSprite> textures = new ArrayList<>();
        textures.add(overlay);
        textures.add(overlay);
        textures.add(overlay);
        textures.add(overlay);
        textures.add(upOverlay);
        textures.add(downOverlay);
        return new ModelInformation(textures, tintIndex, overlayIndex);
    }
}
//========SOLI DEO GLORIA========//