package mod.pianomanu.blockcarpentry.util;

import com.google.common.collect.ImmutableList;
import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Util class for building cuboid shapes
 *
 * @author PianoManu
 * @version 1.16 06/06/21
 */
public class ModelHelper {

    /**
     * This method is used to put all parameters for a vertex. A vertex in this context
     * is something like a point or a vector with special attributes. These include the
     * texture, the light level and so on. This method is needed for the quads (which
     * are something like the faces of a cuboid) and those are determined by 4 vertices
     *
     * @param builder used to construct the quads, content is saved as int array afterwards
     * @param normal  normal vector
     * @param x       x component of the vertex
     * @param y       y component of the vertex
     * @param z       z component of the vertex
     * @param u       u component of the texture, that means the horizontal axis
     * @param v       v component of the texture, that means the vertical axis
     * @param sprite  the texture for the vertex, sprite[u,v] will be displayed for this vertex
     * @param r       red value
     * @param g       green value
     * @param b       blue value
     */
    private static void putVertex(BakedQuadBuilder builder, Vector3d normal,
                                  double x, double y, double z, float u, float v, TextureAtlasSprite sprite, float r, float g, float b) {

        ImmutableList<VertexFormatElement> elements = builder.getVertexFormat().getElements().asList();
        for (int j = 0; j < elements.size(); j++) {
            VertexFormatElement e = elements.get(j);
            switch (e.getUsage()) {
                case POSITION:
                    builder.put(j, (float) x, (float) y, (float) z, 1.0f);
                    break;
                case COLOR:
                    builder.put(j, r, g, b, 1.0f);
                    break;
                case UV:
                    switch (e.getIndex()) {
                        case 0:
                            float iu = sprite.getInterpolatedU(u);
                            float iv = sprite.getInterpolatedV(v);
                            builder.put(j, iu, iv);
                            break;
                        case 2:
                            builder.put(j, (short) 0, (short) 0);
                            break;
                        default:
                            builder.put(j);
                            break;
                    }
                    break;
                case NORMAL:
                    builder.put(j, (float) normal.x, (float) normal.y, (float) normal.z);
                    break;
                default:
                    builder.put(j);
                    break;
            }
        }
    }

    /**
     * This method is used to create quads. Simply put, a quad is one face of a block
     * This method calls the putVertex method four times, because we need 4 vertices
     * to determine a rectangle, which is the face of the block. For this reason we
     * need 4 vectors, the texture, and 4 u and v values
     *
     * @param v1        first vector/point of the face
     * @param v2        second vector/point of the face
     * @param v3        third vector/point of the face
     * @param v4        forth vector/point of the face
     * @param sprite    texture of the block, saved as TextureAtlasSprite
     * @param ulow      low u value, determines the left corners of the sprite
     * @param uhigh     high u value, determines the right corners of the sprite
     * @param vlow      low v value, determines the top corners of the sprite
     * @param vhigh     high v value, determines the bottom corners of the sprite
     * @param tintIndex only needed for tintable blocks like grass
     * @return Baked quad i.e. the completed face of a block
     */
    public static BakedQuad createQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex) {
        Vector3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        builder.setQuadTint(tintIndex);
        putVertex(builder, normal, v1.x, v1.y, v1.z, ulow, vlow, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, ulow, vhigh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, uhigh, vhigh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, uhigh, vlow, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    public static BakedQuad createQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex, boolean invert) {
        Vector3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();
        if (invert) {
            normal = normal.inverse();
        }

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        builder.setQuadTint(tintIndex);
        if (invert) {
            putVertex(builder, normal, v1.x, v1.y, v1.z, ulow, vlow, sprite, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v2.x, v2.y, v2.z, ulow, vhigh, sprite, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v3.x, v3.y, v3.z, uhigh, vhigh, sprite, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v4.x, v4.y, v4.z, uhigh, vlow, sprite, 1.0f, 1.0f, 1.0f);
        } else {
            putVertex(builder, normal, v4.x, v4.y, v4.z, ulow, vlow, sprite, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v3.x, v3.y, v3.z, uhigh, vlow, sprite, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v2.x, v2.y, v2.z, uhigh, vhigh, sprite, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v1.x, v1.y, v1.z, ulow, vhigh, sprite, 1.0f, 1.0f, 1.0f);
        }
        return builder.build();
    }
    public static BakedQuad createQuadInverted(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex) {
        return createQuadInverted(v1, v2, v3, v4, sprite, ulow, uhigh, vlow, vhigh, tintIndex, false);
    }

    public static BakedQuad createQuadInverted(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex, boolean invert) {
        Vector3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();
        if (invert) {
            normal = normal.inverse();
        }

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        builder.setQuadTint(tintIndex);
        if (invert) {
            putVertex(builder, normal, v4.x, v4.y, v4.z, ulow, vlow, sprite, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v3.x, v3.y, v3.z, uhigh, vlow, sprite, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v2.x, v2.y, v2.z, uhigh, vhigh, sprite, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v1.x, v1.y, v1.z, ulow, vhigh, sprite, 1.0f, 1.0f, 1.0f);
        } else {
            putVertex(builder, normal, v1.x, v1.y, v1.z, ulow, vlow, sprite, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v2.x, v2.y, v2.z, uhigh, vlow, sprite, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v3.x, v3.y, v3.z, uhigh, vhigh, sprite, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v4.x, v4.y, v4.z, ulow, vhigh, sprite, 1.0f, 1.0f, 1.0f);
        }
        return builder.build();
    }

    /**
     * This method is used to create a cuboid from six faces. Input values determine
     * the dimensions of the cuboid and the texture to apply.
     * Example 1: full block with dirt texture would be:
     * (0f,1f,0f,1f,0f,1f, >path of dirt texture<, 0)
     * I.e. the dimensions of the block are from (0,0,0) to (16,16,16)
     * Example 2: oak fence:
     * (6/16f,10/16f,0f,1f,6/16f,10/16f, >oak planks<, 0)
     * I.e. the dimensions of a fence, being (6,0,6) to (10,16,10)
     *
     * @param xl        low x component of the block
     * @param xh        high x component of the block
     * @param yl        low y component of the block
     * @param yh        high y component of the block
     * @param zl        low z component of the block
     * @param zh        high z component of the block
     * @param texture   TextureAtlasSprite of the block
     * @param tintIndex only needed for tintable blocks like grass
     * @return List of baked quads, i.e. List of six faces
     */
    public static List<BakedQuad> createCuboid(float xl, float xh, float yl, float yh, float zl, float zh, TextureAtlasSprite texture, int tintIndex) {
        return createCuboid(xl, xh, yl, yh, zl, zh, texture, tintIndex, true, true, true, true, true, true);
    }

    public static List<BakedQuad> createCuboid(float xl, float xh, float yl, float yh, float zl, float zh, TextureAtlasSprite texture, int tintIndex, boolean north, boolean south, boolean east, boolean west, boolean up, boolean down) {
        List<BakedQuad> quads = new ArrayList<>();
        //Eight corners of the block
        Vector3d NWU = v(xl, yh, zl); //North-West-Up
        Vector3d NEU = v(xl, yh, zh); //...
        Vector3d NWD = v(xl, yl, zl);
        Vector3d NED = v(xl, yl, zh);
        Vector3d SWU = v(xh, yh, zl);
        Vector3d SEU = v(xh, yh, zh);
        Vector3d SWD = v(xh, yl, zl);
        Vector3d SED = v(xh, yl, zh); //South-East-Down
        if (xh - xl > 1 || yh - yl > 1 || zh - zl > 1) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.block_error"), true);
            }
            return quads;
        }
        if (xl < 0) {
            xl++;
            xh++;
        }
        if (xh > 1) {
            xh--;
            xl--;
        }
        if (yl < 0) {
            yl++;
            yh++;
        }
        if (yh > 1) {
            yh--;
            yl--;
        }
        if (zl < 0) {
            zl++;
            zh++;
        }
        if (zh > 1) {
            zh--;
            zl--;
        }
        if (up) quads.add(createQuad(NWU, NEU, SEU, SWU, texture, xl * 16, xh * 16, zl * 16, zh * 16, tintIndex));
        if (down)
            quads.add(createQuad(NED, NWD, SWD, SED, texture, xl * 16, xh * 16, 16 - zh * 16, 16 - zl * 16, tintIndex));
        if (north)
            quads.add(createQuad(SWU, SWD, NWD, NWU, texture, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        if (south)
            quads.add(createQuad(NEU, NED, SED, SEU, texture, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        if (west)
            quads.add(createQuad(NWU, NWD, NED, NEU, texture, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        if (east)
            quads.add(createQuad(SEU, SED, SWD, SWU, texture, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        return quads;
    }

    public static List<BakedQuad> createSixFaceCuboid(float xl, float xh, float yl, float yh, float zl, float zh, BlockState mimic, IBakedModel model, IModelData extraData, Random rand, int tintIndex, int rotation) {
        return createSixFaceCuboid(xl, xh, yl, yh, zl, zh, mimic, model, extraData, rand, tintIndex, true, true, true, true, true, true, rotation);
    }

    public static List<BakedQuad> createSixFaceCuboid(float xl, float xh, float yl, float yh, float zl, float zh, BlockState mimic, IBakedModel model, IModelData extraData, Random rand, int tintIndex, boolean north, boolean south, boolean east, boolean west, boolean up, boolean down, int rotation) {
        List<BakedQuad> quads = new ArrayList<>();
        //Eight corners of the block
        Vector3d NWU = v(xl, yh, zl); //North-West-Up
        Vector3d NEU = v(xl, yh, zh); //...
        Vector3d NWD = v(xl, yl, zl);
        Vector3d NED = v(xl, yl, zh);
        Vector3d SWU = v(xh, yh, zl);
        Vector3d SEU = v(xh, yh, zh);
        Vector3d SWD = v(xh, yl, zl);
        Vector3d SED = v(xh, yl, zh); //South-East-Down
        if (xh - xl > 1 || yh - yl > 1 || zh - zl > 1) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.block_error"), true);
            }
            return quads;
        }
        if (xl < 0) {
            xl++;
            xh++;
        }
        if (xh > 1) {
            xh--;
            xl--;
        }
        if (yl < 0) {
            yl++;
            yh++;
        }
        if (yh > 1) {
            yh--;
            yl--;
        }
        if (zl < 0) {
            zl++;
            zh++;
        }
        if (zh > 1) {
            zh--;
            zl--;
        }
        List<TextureAtlasSprite> textureList = TextureHelper.getTextureFromModel(model, extraData, rand);
        if (textureList.size() == 0) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.block_not_available"), true);
            }
            return quads;
        }
        TextureAtlasSprite textureNorth = textureList.get(0);
        TextureAtlasSprite textureEast = textureList.get(0);
        TextureAtlasSprite textureSouth = textureList.get(0);
        TextureAtlasSprite textureWest = textureList.get(0);
        TextureAtlasSprite textureUp = textureList.get(0);
        TextureAtlasSprite textureDown = textureList.get(0);
        for (BakedQuad quad : model.getQuads(extraData.getData(FrameBlockTile.MIMIC), Direction.NORTH, rand, extraData)) {
            textureNorth = quad.func_187508_a();
        }
        for (BakedQuad quad : model.getQuads(extraData.getData(FrameBlockTile.MIMIC), Direction.EAST, rand, extraData)) {
            textureEast = quad.func_187508_a();
        }
        for (BakedQuad quad : model.getQuads(extraData.getData(FrameBlockTile.MIMIC), Direction.SOUTH, rand, extraData)) {
            textureSouth = quad.func_187508_a();
        }
        for (BakedQuad quad : model.getQuads(extraData.getData(FrameBlockTile.MIMIC), Direction.WEST, rand, extraData)) {
            textureWest = quad.func_187508_a();
        }
        for (BakedQuad quad : model.getQuads(extraData.getData(FrameBlockTile.MIMIC), Direction.UP, rand, extraData)) {
            textureUp = quad.func_187508_a();
        }
        for (BakedQuad quad : model.getQuads(extraData.getData(FrameBlockTile.MIMIC), Direction.DOWN, rand, extraData)) {
            textureDown = quad.func_187508_a();
        }
        if (rotation == 0) {
            if (up) quads.add(ModelHelper.createQuad(NWU, NEU, SEU, SWU, textureUp, xl * 16, xh * 16, zl * 16, zh * 16, tintIndex));
            if (down)
                quads.add(ModelHelper.createQuad(NED, NWD, SWD, SED, textureDown, xl * 16, xh * 16, 16 - zh * 16, 16 - zl * 16, tintIndex));
            if (west)
                quads.add(ModelHelper.createQuad(SWU, SWD, NWD, NWU, textureNorth, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (east)
                quads.add(ModelHelper.createQuad(NEU, NED, SED, SEU, textureSouth, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north)
                quads.add(ModelHelper.createQuad(NWU, NWD, NED, NEU, textureWest, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south)
                quads.add(ModelHelper.createQuad(SEU, SED, SWD, SWU, textureEast, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        }
        if (rotation == 1) {
            if (up) quads.add(ModelHelper.createQuadInverted(NWU, NEU, SEU, SWU, textureUp, xl * 16,xh * 16, zh * 16, zl * 16,  tintIndex));
            if (down)
                quads.add(ModelHelper.createQuadInverted(NED, NWD, SWD, SED, textureDown, xl * 16, xh * 16, 16 - zh * 16, 16 - zl * 16, tintIndex));
            if (west)
                quads.add(ModelHelper.createQuad(SWU, SWD, NWD, NWU, textureWest, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (east)
                quads.add(ModelHelper.createQuad(NEU, NED, SED, SEU, textureEast, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north)
                quads.add(ModelHelper.createQuad(NWU, NWD, NED, NEU, textureSouth, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south)
                quads.add(ModelHelper.createQuad(SEU, SED, SWD, SWU, textureNorth, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        }
        if (rotation == 2) {
            if (up) quads.add(ModelHelper.createQuad(NWU, NEU, SEU, SWU, textureUp, xh * 16, xl * 16, zh * 16, zl * 16, tintIndex));
            if (down)
                quads.add(ModelHelper.createQuad(NED, NWD, SWD, SED, textureDown, xh * 16, xl * 16, 16 - zl * 16, 16 - zh * 16, tintIndex));
            if (west)
                quads.add(ModelHelper.createQuad(SWU, SWD, NWD, NWU, textureSouth, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (east)
                quads.add(ModelHelper.createQuad(NEU, NED, SED, SEU, textureNorth, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north)
                quads.add(ModelHelper.createQuad(NWU, NWD, NED, NEU, textureEast, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south)
                quads.add(ModelHelper.createQuad(SEU, SED, SWD, SWU, textureWest, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        }
        if (rotation == 3) {
            if (up) quads.add(ModelHelper.createQuadInverted(NWU, NEU, SEU, SWU, textureUp, xh * 16,xl * 16, zl * 16, zh * 16,  tintIndex));
            if (down)
                quads.add(ModelHelper.createQuadInverted(NED, NWD, SWD, SED, textureDown, xh * 16, xl * 16, 16 - zl * 16, 16 - zh * 16, tintIndex));
            if (west)
                quads.add(ModelHelper.createQuad(SWU, SWD, NWD, NWU, textureEast, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (east)
                quads.add(ModelHelper.createQuad(NEU, NED, SED, SEU, textureWest, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north)
                quads.add(ModelHelper.createQuad(NWU, NWD, NED, NEU, textureNorth, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south)
                quads.add(ModelHelper.createQuad(SEU, SED, SWD, SWU, textureSouth, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        }
        if (rotation == 4) {
            if (up) quads.add(ModelHelper.createQuadInverted(NWU, NEU, SEU, SWU, textureEast, zh * 16, zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (down)
                quads.add(ModelHelper.createQuadInverted(NED, NWD, SWD, SED, textureWest, xh * 16, xl * 16, 16 - zh * 16, 16 - zl * 16, tintIndex));
            if (west)
                quads.add(ModelHelper.createQuadInverted(SWU, SWD, NWD, NWU, textureNorth, 16 - xh * 16, 16 - xl * 16, yh * 16, yl * 16, tintIndex));
            if (east)
                quads.add(ModelHelper.createQuadInverted(NEU, NED, SED, SEU, textureSouth, xh * 16, xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north)
                quads.add(ModelHelper.createQuadInverted(NWU, NWD, NED, NEU, textureUp, zh * 16, zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south)
                quads.add(ModelHelper.createQuad(SEU, SED, SWD, SWU, textureDown, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        }
        if (rotation == 5) {
            if (up) quads.add(ModelHelper.createQuad(NWU, NEU, SEU, SWU, textureSouth, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (down)
                quads.add(ModelHelper.createQuad(NED, NWD, SWD, SED, textureNorth, xh * 16, xl * 16, zh * 16, zl * 16, tintIndex));
            if (west)
                quads.add(ModelHelper.createQuad(SWU, SWD, NWD, NWU, textureUp, xh * 16, xl * 16, yh * 16, yl * 16, tintIndex));
            if (east)
                quads.add(ModelHelper.createQuad(NEU, NED, SED, SEU, textureDown, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north)
                quads.add(ModelHelper.createQuadInverted(NWU, NWD, NED, NEU, textureWest, zh * 16, zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south)
                quads.add(ModelHelper.createQuadInverted(SEU, SED, SWD, SWU, textureEast, 16 - zh * 16, 16 - zl * 16, yh * 16, yl * 16, tintIndex));
        }
        if (rotation == 6) {
            if (up) quads.add(ModelHelper.createQuadInverted(NWU, NEU, SEU, SWU, textureWest, zl * 16, zh * 16, yh * 16, yl * 16, tintIndex));
            if (down)
                quads.add(ModelHelper.createQuadInverted(NED, NWD, SWD, SED, textureEast, xl * 16, xh * 16, zh * 16, zl * 16, tintIndex));
            if (west)
                quads.add(ModelHelper.createQuadInverted(SWU, SWD, NWD, NWU, textureNorth, xh * 16, xl * 16, yl * 16, yh * 16, tintIndex));
            if (east)
                quads.add(ModelHelper.createQuadInverted(NEU, NED, SED, SEU, textureSouth, xl * 16, xh * 16, yh * 16, yl * 16, tintIndex));
            if (north)
                quads.add(ModelHelper.createQuadInverted(NWU, NWD, NED, NEU, textureDown, zh * 16, zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south)
                quads.add(ModelHelper.createQuadInverted(SEU, SED, SWD, SWU, textureUp, 16 - zh * 16, 16 - zl * 16, yh * 16, yl * 16, tintIndex));
        }
        if (rotation == 7) {
            if (up) quads.add(ModelHelper.createQuad(NWU, NEU, SEU, SWU, textureNorth, zh * 16, zl * 16, yh * 16, yl * 16, tintIndex));
            if (down)
                quads.add(ModelHelper.createQuad(NED, NWD, SWD, SED, textureSouth, xl * 16, xh * 16, zl * 16, zh * 16, tintIndex));
            if (west)
                quads.add(ModelHelper.createQuad(SWU, SWD, NWD, NWU, textureDown, xh * 16, xl * 16, yh * 16, yl * 16, tintIndex));
            if (east)
                quads.add(ModelHelper.createQuad(NEU, NED, SED, SEU, textureUp, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north)
                quads.add(ModelHelper.createQuadInverted(NWU, NWD, NED, NEU, textureWest, zl * 16, zh * 16, yh * 16, yl * 16, tintIndex));
            if (south)
                quads.add(ModelHelper.createQuadInverted(SEU, SED, SWD, SWU, textureEast, zh * 16, zl * 16, yl * 16, yh * 16, tintIndex));
        }
        return quads;
    }

    public static List<BakedQuad> createSixFaceCuboid(float xl, float xh, float yl, float yh, float zl, float zh, int tintIndex, TextureAtlasSprite textureNorth, TextureAtlasSprite textureSouth, TextureAtlasSprite textureEast, TextureAtlasSprite textureWest, TextureAtlasSprite textureUp, TextureAtlasSprite textureDown, int rotation) {
        return createSixFaceCuboid(xl, xh, yl, yh, zl, zh, tintIndex, true, true, true, true, true, true, textureNorth, textureSouth, textureEast, textureWest, textureUp, textureDown, true, rotation);
    }

    public static List<BakedQuad> createSixFaceCuboid(float xl, float xh, float yl, float yh, float zl, float zh, int tintIndex, boolean north, boolean south, boolean east, boolean west, boolean up, boolean down, TextureAtlasSprite textureNorth, TextureAtlasSprite textureSouth, TextureAtlasSprite textureEast, TextureAtlasSprite textureWest, TextureAtlasSprite textureUp, TextureAtlasSprite textureDown, Boolean moveOverlay, int rotation) {
        List<BakedQuad> quads = new ArrayList<>();
        //Eight corners of the block
        Vector3d NWU = v(xl, yh, zl); //North-West-Up
        Vector3d NEU = v(xl, yh, zh); //...
        Vector3d NWD = v(xl, yl, zl);
        Vector3d NED = v(xl, yl, zh);
        Vector3d SWU = v(xh, yh, zl);
        Vector3d SEU = v(xh, yh, zh);
        Vector3d SWD = v(xh, yl, zl);
        Vector3d SED = v(xh, yl, zh); //South-East-Down
        if (xh - xl > 1 || yh - yl > 1 || zh - zl > 1) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.block_error"), true);
            }
            return quads;
        }
        if (xl < 0) {
            xl++;
            xh++;
        }
        if (xh > 1) {
            xh--;
            xl--;
        }
        if (yl < 0) {
            yl++;
            yh++;
        }
        if (yh > 1) {
            yh--;
            yl--;
        }
        if (zl < 0) {
            zl++;
            zh++;
        }
        if (zh > 1) {
            zh--;
            zl--;
        }
        if (rotation == 0) {
            if (up && textureUp != null)
                quads.add(createQuad(NWU, NEU, SEU, SWU, textureUp, xl * 16, xh * 16, zl * 16, zh * 16, tintIndex));
            if (down && textureDown != null)
                quads.add(createQuad(NED, NWD, SWD, SED, textureDown, xl * 16, xh * 16, 16 - zh * 16, 16 - zl * 16, tintIndex));
            //not moved overlay - texture starts from y=1
            if (west && textureWest != null && !moveOverlay)
                quads.add(createQuad(SWU, SWD, NWD, NWU, textureWest, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (east && textureEast != null && !moveOverlay)
                quads.add(createQuad(NEU, NED, SED, SEU, textureEast, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north && textureNorth != null && !moveOverlay)
                quads.add(createQuad(NWU, NWD, NED, NEU, textureNorth, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south && textureSouth != null && !moveOverlay)
                quads.add(createQuad(SEU, SED, SWD, SWU, textureSouth, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            //moved overlay - texture starts from height of block
            if (west && textureWest != null && moveOverlay)
                quads.add(createQuad(SWU, SWD, NWD, NWU, textureWest, 16 - xh * 16, 16 - xl * 16, yl * 16, yh * 16, tintIndex));
            if (east && textureEast != null && moveOverlay)
                quads.add(createQuad(NEU, NED, SED, SEU, textureEast, xl * 16, xh * 16, yl * 16, yh * 16, tintIndex));
            if (north && textureNorth != null && moveOverlay)
                quads.add(createQuad(NWU, NWD, NED, NEU, textureNorth, zl * 16, zh * 16, yl * 16, yh * 16, tintIndex));
            if (south && textureSouth != null && moveOverlay)
                quads.add(createQuad(SEU, SED, SWD, SWU, textureSouth, 16 - zh * 16, 16 - zl * 16, yl * 16, yh * 16, tintIndex));
        }
        if (rotation == 1) {
            //rotated
            if (up && textureUp != null)
                quads.add(createQuad(NWU, NEU, SEU, SWU, textureUp, xl * 16, xh * 16, zl * 16, zh * 16, tintIndex));
            if (down && textureDown != null)
                quads.add(createQuad(NED, NWD, SWD, SED, textureDown, xl * 16, xh * 16, 16 - zh * 16, 16 - zl * 16, tintIndex));
            //not moved overlay - texture starts from y=1
            if (north && textureNorth != null && !moveOverlay)
                quads.add(createQuad(SWU, SWD, NWD, NWU, textureNorth, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south && textureSouth != null && !moveOverlay)
                quads.add(createQuad(NEU, NED, SED, SEU, textureSouth, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (east && textureEast != null && !moveOverlay)
                quads.add(createQuad(NWU, NWD, NED, NEU, textureEast, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (west && textureWest != null && !moveOverlay)
                quads.add(createQuad(SEU, SED, SWD, SWU, textureWest, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            //moved overlay - texture starts from height of block
            if (north && textureNorth != null && moveOverlay)
                quads.add(createQuad(SWU, SWD, NWD, NWU, textureNorth, 16 - xh * 16, 16 - xl * 16, yl * 16, yh * 16, tintIndex));
            if (south && textureSouth != null && moveOverlay)
                quads.add(createQuad(NEU, NED, SED, SEU, textureSouth, xl * 16, xh * 16, yl * 16, yh * 16, tintIndex));
            if (east && textureEast != null && moveOverlay)
                quads.add(createQuad(NWU, NWD, NED, NEU, textureEast, zl * 16, zh * 16, yl * 16, yh * 16, tintIndex));
            if (west && textureWest != null && moveOverlay)
                quads.add(createQuad(SEU, SED, SWD, SWU, textureWest, 16 - zh * 16, 16 - zl * 16, yl * 16, yh * 16, tintIndex));
        }
        return quads;
    }

    public static List<BakedQuad> createCuboid(float xl, float xh, float yl, float yh, float zl, float zh, TextureAtlasSprite texture, int tintIndex, int[] ulow, int[] uhigh, int[] vlow, int[] vhigh) {
        return createCuboid(xl, xh, yl, yh, zl, zh, texture, tintIndex, true, true, true, true, true, true, ulow, uhigh, vlow, vhigh);
    }

    public static List<BakedQuad> createCuboid(float xl, float xh, float yl, float yh, float zl, float zh, TextureAtlasSprite texture, int tintIndex, boolean north, boolean south, boolean east, boolean west, boolean up, boolean down, int[] ulow, int[] uhigh, int[] vlow, int[] vhigh) {
        if (ulow.length != 6 || uhigh.length != 6 || vlow.length != 6 || vhigh.length != 6) {
            return Collections.emptyList();
        }
        List<BakedQuad> quads = new ArrayList<>();
        //Eight corners of the block
        Vector3d NWU = v(xl, yh, zl); //North-West-Up
        Vector3d NEU = v(xl, yh, zh); //...
        Vector3d NWD = v(xl, yl, zl);
        Vector3d NED = v(xl, yl, zh);
        Vector3d SWU = v(xh, yh, zl);
        Vector3d SEU = v(xh, yh, zh);
        Vector3d SWD = v(xh, yl, zl);
        Vector3d SED = v(xh, yl, zh); //South-East-Down
        if (up)
            quads.add(createQuad(NWU, NEU, SEU, SWU, texture, ulow[0], uhigh[0], vlow[0], vhigh[0], tintIndex));
        if (down)
            quads.add(createQuad(NED, NWD, SWD, SED, texture, ulow[1], uhigh[1], vlow[1], vhigh[1], tintIndex));
        if (north)
            quads.add(createQuad(NWU, NWD, NED, NEU, texture, ulow[2], uhigh[2], vlow[2], vhigh[2], tintIndex));
        if (east)
            quads.add(createQuad(NEU, NED, SED, SEU, texture, ulow[3], uhigh[3], vlow[3], vhigh[3], tintIndex));
        if (south)
            quads.add(createQuad(SEU, SED, SWD, SWU, texture, ulow[4], uhigh[4], vlow[4], vhigh[4], tintIndex));
        if (west)
            quads.add(createQuad(SWU, SWD, NWD, NWU, texture, ulow[5], uhigh[5], vlow[5], vhigh[5], tintIndex));
        return quads;
    }

    /**
     * This just builds vectors and is useful for clean code
     *
     * @param x x component
     * @param y y component
     * @param z z component
     * @return 3D-Vector with input values. Why am I writing this...
     */
    private static Vector3d v(double x, double y, double z) {
        return new Vector3d(x, y, z);
    }

    public static List<BakedQuad> createOverlay(float xl, float xh, float yl, float yh, float zl, float zh, int overlayIndex) {
        return createOverlay(xl, xh, yl, yh, zl, zh, overlayIndex, true, true, true, true, true, true, true);
    }

    public static List<BakedQuad> createOverlay(float xl, float xh, float yl, float yh, float zl, float zh, int overlayIndex, boolean north, boolean south, boolean east, boolean west, boolean up, boolean down, Boolean doNotMoveOverlay) {
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
            tintIndex = -1;
            overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_snow_overlay"));
            upOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/snow"));
        }
        if (overlayIndex == 4) {
            tintIndex = -1;
            overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_snow_overlay_small"));
            upOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/snow"));
        }
        if (overlayIndex == 5) {
            tintIndex = 1;
            overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/vine"));
        }
        if (overlayIndex >= 6 && overlayIndex <= 10) {
            tintIndex = -1;
            doNotMoveOverlay = false;
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
        return ModelHelper.createSixFaceCuboid(xl, xh, yl, yh, zl, zh, tintIndex, north, south, east, west, up, down, overlay, overlay, overlay, overlay, upOverlay, downOverlay, doNotMoveOverlay, 0);
    }

    public static List<BakedQuad> createSlope(float xl, float xh, float yl, float yh, float zl, float zh, TextureAtlasSprite texture, int tintIndex, Direction direction) {
        List<BakedQuad> quads = new ArrayList<>();
        //Eight corners of the block
        Vector3d NWU = v(xl, yh, zl); //North-West-Up
        Vector3d NEU = v(xl, yh, zh); //...
        Vector3d NWD = v(xl, yl, zl);
        Vector3d NED = v(xl, yl, zh);
        Vector3d SWU = v(xh, yh, zl);
        Vector3d SEU = v(xh, yh, zh);
        Vector3d SWD = v(xh, yl, zl);
        Vector3d SED = v(xh, yl, zh); //South-East-Down
        if (xh - xl > 1 || yh - yl > 1 || zh - zl > 1) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.block_error"), true);
            }
            return quads;
        }
        if (xl < 0) {
            xl++;
            xh++;
        }
        if (xh > 1) {
            xh--;
            xl--;
        }
        if (yl < 0) {
            yl++;
            yh++;
        }
        if (yh > 1) {
            yh--;
            yl--;
        }
        if (zl < 0) {
            zl++;
            zh++;
        }
        if (zh > 1) {
            zh--;
            zl--;
        }
        //bottom face
        quads.add(ModelHelper.createQuad(NED, NWD, SWD, SED, texture, 0, 16, 0, 16, tintIndex));
        switch (direction) {
            case NORTH:
                //top face
                quads.add(ModelHelper.createQuad(NWU, NED, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                break;
            case WEST:
                //back face
                quads.add(ModelHelper.createQuad(NWU, NWD, NED, NEU, texture, 0, 16, 0, 16, tintIndex));

                quads.add(ModelHelper.createQuad(SWD, SWD, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                quads.add(ModelHelper.createQuad(NEU, NED, NED, SED, texture, 0, 16, 0, 16, tintIndex));

                //top face
                quads.add(ModelHelper.createQuad(NEU, SED, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                break;
            case SOUTH:
                //top face
                quads.add(ModelHelper.createQuad(NWD, NEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                break;
            case EAST:
                //back face
                quads.add(ModelHelper.createQuad(SEU, SED, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));

                quads.add(ModelHelper.createQuad(SWD, SWU, NWD, NWD, texture, 0, 16, 0, 16, tintIndex));
                quads.add(ModelHelper.createQuad(SED, NEU, NEU, NED, texture, 0, 16, 0, 16, tintIndex));
                //top face
                quads.add(ModelHelper.createQuad(NED, SEU, SWU, NWD, texture, 16, 0, 16, 0, tintIndex));
                break;
        }
        //top face
        quads.add(ModelHelper.createQuad(SWU, SEU, NEU, NWU, texture, 0, 16, 0, 16, tintIndex));
        return quads;
    }
}
//========SOLI DEO GLORIA========//