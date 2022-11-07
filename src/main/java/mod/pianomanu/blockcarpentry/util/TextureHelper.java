package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.model.data.ModelData;

import java.util.ArrayList;
import java.util.List;

/**
 * Util class for picking the right texture of a block. Pretty stupid at the moment (May be removed and rewritten in the future)
 *
 * @author PianoManu
 * @version 1.2 11/07/22
 */
public class TextureHelper {

    /**
     * Used for dyed glass doors, trapdoors etc
     *
     * @return list of all glass textures
     */
    public static List<TextureAtlasSprite> getGlassTextures() {
        List<TextureAtlasSprite> glassTextures = new ArrayList<>();
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/white_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/orange_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/magenta_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/light_blue_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/yellow_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/lime_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/pink_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/gray_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/light_gray_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/cyan_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/purple_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/blue_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/brown_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/green_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/red_stained_glass")));
        glassTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/black_stained_glass")));
        return glassTextures;
    }

    public static List<TextureAtlasSprite> getWoolTextures() {
        List<TextureAtlasSprite> woolTextures = new ArrayList<>();
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/white_wool")));
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/orange_wool")));
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/magenta_wool")));
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/light_blue_wool")));
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/yellow_wool")));
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/lime_wool")));
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/pink_wool")));
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/gray_wool")));
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/light_gray_wool")));
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/cyan_wool")));
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/purple_wool")));
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/blue_wool")));
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/brown_wool")));
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/green_wool")));
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/red_wool")));
        woolTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/black_wool")));
        return woolTextures;
    }

    public static List<TextureAtlasSprite> getPlanksTextures() {
        List<TextureAtlasSprite> planksTextures = new ArrayList<>();
        planksTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/oak_planks")));
        planksTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/birch_planks")));
        planksTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/spruce_planks")));
        planksTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/jungle_planks")));
        planksTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/acacia_planks")));
        planksTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/dark_oak_planks")));
        planksTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/crimson_planks")));
        planksTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/warped_planks")));
        return planksTextures;
    }

    public static List<TextureAtlasSprite> getMetalTextures() {
        List<TextureAtlasSprite> metalTextures = new ArrayList<>();
        metalTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/iron_block")));
        metalTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/obsidian")));
        metalTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/stone")));
        metalTextures.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc("block/oak_log")));
        return metalTextures;
    }

    public static List<TextureAtlasSprite> getTextureFromModel(BakedModel model, ModelData extraData, RandomSource rand) {
        List<TextureAtlasSprite> textureList = new ArrayList<>();
        for (BakedQuad quad : model.getQuads(extraData.get(FrameBlockTile.MIMIC), Direction.UP, rand, extraData, RenderType.translucent())) {
            if (!textureList.contains(quad.getSprite())) {
                textureList.add(quad.getSprite());
            }
        }
        for (BakedQuad quad : model.getQuads(extraData.get(FrameBlockTile.MIMIC), Direction.DOWN, rand, extraData, RenderType.translucent())) {
            if (!textureList.contains(quad.getSprite())) {
                textureList.add(quad.getSprite());
            }
        }
        for (BakedQuad quad : model.getQuads(extraData.get(FrameBlockTile.MIMIC), Direction.NORTH, rand, extraData, RenderType.translucent())) {
            if (!textureList.contains(quad.getSprite())) {
                textureList.add(quad.getSprite());
            }
        }
        for (BakedQuad quad : model.getQuads(extraData.get(FrameBlockTile.MIMIC), Direction.EAST, rand, extraData, RenderType.translucent())) {
            if (!textureList.contains(quad.getSprite())) {
                textureList.add(quad.getSprite());
            }
        }
        for (BakedQuad quad : model.getQuads(extraData.get(FrameBlockTile.MIMIC), Direction.SOUTH, rand, extraData, RenderType.translucent())) {
            if (!textureList.contains(quad.getSprite())) {
                textureList.add(quad.getSprite());
            }
        }
        for (BakedQuad quad : model.getQuads(extraData.get(FrameBlockTile.MIMIC), Direction.WEST, rand, extraData, RenderType.translucent())) {
            if (!textureList.contains(quad.getSprite())) {
                textureList.add(quad.getSprite());
            }
        }
        return textureList;
    }

    private static ResourceLocation loc(String path) {
        return new ResourceLocation("minecraft", path);
    }
}
//========SOLI DEO GLORIA========//