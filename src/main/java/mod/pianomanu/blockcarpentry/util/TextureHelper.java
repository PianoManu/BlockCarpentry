package mod.pianomanu.blockcarpentry.util;

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
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.IModelData;

import java.util.*;

/**
 * Util class for picking the right texture of a block. Pretty stupid at the moment (May be removed and rewritten in the future)
 *
 * @author PianoManu
 * @version 1.3 10/02/23
 */
public class TextureHelper {

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
        return getTextureFromModel(model, extraData.getData(FrameBlockTile.MIMIC), rand);
    }

    public static List<TextureAtlasSprite> getTextureFromModel(IBakedModel model, BlockState state, Random rand) {
        List<TextureAtlasSprite> textureList = new ArrayList<>();
        try {
            for (BakedQuad quad : model.getQuads(state, Direction.UP, rand, null)) {
                if (!textureList.contains(quad.func_187508_a())) {
                    textureList.add(quad.func_187508_a());
                }
            }
            for (BakedQuad quad : model.getQuads(state, Direction.DOWN, rand, null)) {
                if (!textureList.contains(quad.func_187508_a())) {
                    textureList.add(quad.func_187508_a());
                }
            }
            for (BakedQuad quad : model.getQuads(state, Direction.NORTH, rand, null)) {
                if (!textureList.contains(quad.func_187508_a())) {
                    textureList.add(quad.func_187508_a());
                }
            }
            for (BakedQuad quad : model.getQuads(state, Direction.EAST, rand, null)) {
                if (!textureList.contains(quad.func_187508_a())) {
                    textureList.add(quad.func_187508_a());
                }
            }
            for (BakedQuad quad : model.getQuads(state, Direction.SOUTH, rand, null)) {
                if (!textureList.contains(quad.func_187508_a())) {
                    textureList.add(quad.func_187508_a());
                }
            }
            for (BakedQuad quad : model.getQuads(state, Direction.WEST, rand, null)) {
                if (!textureList.contains(quad.func_187508_a())) {
                    textureList.add(quad.func_187508_a());
                }
            }
            return textureList;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static TextureAtlasSprite getTextureFromTileEntity(IFrameTile tile) {
        BlockState mimic = tile.getMimic();
        IBakedModel model = Minecraft.getInstance().getModelManager().getModel(BlockModelShapes.getModelLocation(mimic));
        List<TextureAtlasSprite> sprites = TextureHelper.getTextureFromModel(model, mimic, Objects.requireNonNull(Minecraft.getInstance().world).rand);
        if (sprites.size() > 0 && tile.getTexture() < sprites.size())
            return sprites.get(tile.getTexture());
        return sprites.size() > 0 ? sprites.get(0) : Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(MissingTextureSprite.getLocation());
    }

    private static ResourceLocation loc(String path) {
        return new ResourceLocation("minecraft", path);
    }

    public static ResourceLocation textureLocation(BlockState state) {
        String id = state.getBlock().getTranslationKey();
        String[] id_parted = id.split("\\.");
        if (id_parted.length != 3)
            System.out.println("Suspicious string list " + Arrays.toString(id_parted));
        try {
            String category = id_parted[0];
            String namespace = id_parted[1];
            String element = id_parted[2];
            return new ResourceLocation(namespace, category + "/" + element);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return MissingTextureSprite.getLocation();
        }
    }
}
//========SOLI DEO GLORIA========//