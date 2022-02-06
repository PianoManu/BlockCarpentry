package mod.pianomanu.blockcarpentry.util;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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
 * @version 1.18 02/06/22
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
    private static void putVertex(BakedQuadBuilder builder, Vec3 normal,
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
                            float iu = sprite.getU(u);
                            float iv = sprite.getV(v);
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
    public static BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getNearest(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        builder.setQuadTint(tintIndex);
        putVertex(builder, normal, v1.x, v1.y, v1.z, ulow, vlow, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, ulow, vhigh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, uhigh, vhigh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, uhigh, vlow, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    public static BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex, boolean invert) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();
        if (invert) {
            normal = normal.reverse();
        }

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getNearest(normal.x, normal.y, normal.z));
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

    public static BakedQuad createQuadInverted(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex) {
        return createQuadInverted(v1, v2, v3, v4, sprite, ulow, uhigh, vlow, vhigh, tintIndex, false);
    }

    public static BakedQuad createQuadInverted(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex, boolean invert) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();
        if (invert) {
            normal = normal.reverse();
        }

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getNearest(normal.x, normal.y, normal.z));
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
        Vec3 NWU = v(xl, yh, zl); //North-West-Up
        Vec3 SWU = v(xl, yh, zh); //...
        Vec3 NWD = v(xl, yl, zl);
        Vec3 SWD = v(xl, yl, zh);
        Vec3 NEU = v(xh, yh, zl);
        Vec3 SEU = v(xh, yh, zh);
        Vec3 NED = v(xh, yl, zl);
        Vec3 SED = v(xh, yl, zh); //South-East-Down
        if (xh - xl > 1 || yh - yl > 1 || zh - zl > 1) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.block_error"), true);
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
        if (up) quads.add(createQuad(NWU, SWU, SEU, NEU, texture, xl * 16, xh * 16, zl * 16, zh * 16, tintIndex));
        if (down)
            quads.add(createQuad(NED, SED, SWD, NWD, texture, xh * 16, xl * 16, 16 - zl * 16, 16 - zh * 16, tintIndex));
        if (west)
            quads.add(createQuad(NWU, NWD, SWD, SWU, texture, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        if (east)
            quads.add(createQuad(SEU, SED, NED, NEU, texture, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        if (north)
            quads.add(createQuad(NEU, NED, NWD, NWU, texture, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        if (south)
            quads.add(createQuad(SWU, SWD, SED, SEU, texture, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        return quads;
    }

    public static List<BakedQuad> createSixFaceCuboid(float xl, float xh, float yl, float yh, float zl, float zh, BlockState mimic, BakedModel model, IModelData extraData, Random rand, int tintIndex, int rotation) {
        return createSixFaceCuboid(xl, xh, yl, yh, zl, zh, mimic, model, extraData, rand, tintIndex, true, true, true, true, true, true, rotation);
    }

    public static List<BakedQuad> createSixFaceCuboid(float xl, float xh, float yl, float yh, float zl, float zh, BlockState mimic, BakedModel model, IModelData extraData, Random rand, int tintIndex, boolean north, boolean south, boolean east, boolean west, boolean up, boolean down, int rotation) {
        List<BakedQuad> quads = new ArrayList<>();
        //Eight corners of the block
        Vec3 NWU = v(xl, yh, zl); //North-West-Up
        Vec3 SWU = v(xl, yh, zh); //...
        Vec3 NWD = v(xl, yl, zl);
        Vec3 SWD = v(xl, yl, zh);
        Vec3 NEU = v(xh, yh, zl);
        Vec3 SEU = v(xh, yh, zh);
        Vec3 NED = v(xh, yl, zl);
        Vec3 SED = v(xh, yl, zh); //South-East-Down
        if (xh - xl > 1 || yh - yl > 1 || zh - zl > 1) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.block_error"), true);
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
                Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.block_not_available"), true);
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
            textureNorth = quad.getSprite();
        }
        for (BakedQuad quad : model.getQuads(extraData.getData(FrameBlockTile.MIMIC), Direction.EAST, rand, extraData)) {
            textureEast = quad.getSprite();
        }
        for (BakedQuad quad : model.getQuads(extraData.getData(FrameBlockTile.MIMIC), Direction.SOUTH, rand, extraData)) {
            textureSouth = quad.getSprite();
        }
        for (BakedQuad quad : model.getQuads(extraData.getData(FrameBlockTile.MIMIC), Direction.WEST, rand, extraData)) {
            textureWest = quad.getSprite();
        }
        for (BakedQuad quad : model.getQuads(extraData.getData(FrameBlockTile.MIMIC), Direction.UP, rand, extraData)) {
            textureUp = quad.getSprite();
        }
        for (BakedQuad quad : model.getQuads(extraData.getData(FrameBlockTile.MIMIC), Direction.DOWN, rand, extraData)) {
            textureDown = quad.getSprite();
        }
        if (rotation == 0) {
            if (up) quads.add(createQuad(NWU, SWU, SEU, NEU, textureUp, xl * 16, xh * 16, zl * 16, zh * 16, tintIndex));
            if (down)
                quads.add(createQuad(SWD, NWD, NED, SED, textureDown, 16 - xl * 16, 16 - xh * 16, zh * 16, zl * 16, tintIndex));
            if (west)
                quads.add(createQuad(NWU, NWD, SWD, SWU, textureWest, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (east)
                quads.add(createQuad(SEU, SED, NED, NEU, textureEast, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north)
                quads.add(createQuad(NEU, NED, NWD, NWU, textureNorth, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south)
                quads.add(createQuad(SWU, SWD, SED, SEU, textureSouth, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        }
        if (rotation == 1) {
            if (up) quads.add(createQuad(NEU, NWU, SWU, SEU, textureUp, zl * 16, zh * 16, xl * 16, xh * 16, tintIndex));
            if (down)
                quads.add(createQuad(NWD, NED, SED, SWD, textureDown, 16 - zl * 16, 16 - zh * 16, xh * 16, xl * 16, tintIndex));
            if (west)
                quads.add(createQuad(NWU, NWD, SWD, SWU, textureSouth, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (east)
                quads.add(createQuad(SEU, SED, NED, NEU, textureNorth, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north)
                quads.add(createQuad(NEU, NED, NWD, NWU, textureWest, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south)
                quads.add(createQuad(SWU, SWD, SED, SEU, textureEast, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        }
        if (rotation == 2) {
            if (up) quads.add(createQuad(SEU, NEU, NWU, SWU, textureUp, xl * 16, xh * 16, zl * 16, zh * 16, tintIndex));
            if (down)
                quads.add(createQuad(NED, SED, SWD, NWD, textureDown, 16 - xl * 16, 16 - xh * 16, zh * 16, zl * 16, tintIndex));
            if (west)
                quads.add(createQuad(NWU, NWD, SWD, SWU, textureEast, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (east)
                quads.add(createQuad(SEU, SED, NED, NEU, textureWest, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north)
                quads.add(createQuad(NEU, NED, NWD, NWU, textureSouth, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south)
                quads.add(createQuad(SWU, SWD, SED, SEU, textureNorth, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        }
        if (rotation == 3) {
            if (up) quads.add(createQuad(SWU, SEU, NEU, NWU, textureUp, zl * 16, zh * 16, xl * 16, xh * 16, tintIndex));
            if (down)
                quads.add(createQuad(SED, SWD, NWD, NED, textureDown, 16 - zl * 16, 16 - zh * 16, xh * 16, xl * 16, tintIndex));
            if (west)
                quads.add(createQuad(NWU, NWD, SWD, SWU, textureNorth, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (east)
                quads.add(createQuad(SEU, SED, NED, NEU, textureSouth, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north)
                quads.add(createQuad(NEU, NED, NWD, NWU, textureEast, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south)
                quads.add(createQuad(SWU, SWD, SED, SEU, textureWest, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        }
        if (rotation == 4) {
            if (up)
                quads.add(createQuad(NWU, SWU, SEU, NEU, textureSouth, xl * 16, xh * 16, zl * 16, zh * 16, tintIndex));
            if (down)
                quads.add(createQuad(SWD, NWD, NED, SED, textureNorth, 16 - xl * 16, 16 - xh * 16, zh * 16, zl * 16, tintIndex));
            if (west)
                quads.add(createQuad(NWD, SWD, SWU, NWU, textureWest, yl * 16, yh * 16, zl * 16, zh * 16, tintIndex));
            if (east)
                quads.add(createQuad(NEU, SEU, SED, NED, textureEast, 16 - yh * 16, 16 - yl * 16, zl * 16, zh * 16, tintIndex));
            if (north)
                quads.add(createQuad(NWD, NWU, NEU, NED, textureUp, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south)
                quads.add(createQuad(SED, SEU, SWU, SWD, textureDown, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        }
        if (rotation == 5) {
            if (up)
                quads.add(createQuad(NEU, NWU, SWU, SEU, textureSouth, zl * 16, zh * 16, 16 - xh * 16, 16 - xl * 16, tintIndex));
            if (down)
                quads.add(createQuad(NWD, NED, SED, SWD, textureNorth, 16 - zl * 16, 16 - zh * 16, 16 - xl * 16, 16 - xh * 16, tintIndex));
            if (west)
                quads.add(createQuad(NWU, NWD, SWD, SWU, textureDown, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (east)
                quads.add(createQuad(SEU, SED, NED, NEU, textureUp, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north)
                quads.add(createQuad(NED, NWD, NWU, NEU, textureWest, 16 - yh * 16, 16 - yl * 16, 16 - xh * 16, 16 - xl * 16, tintIndex));
            if (south)
                quads.add(createQuad(SEU, SWU, SWD, SED, textureEast, yl * 16, yh * 16, 16 - xh * 16, 16 - xl * 16, tintIndex));
        }
        if (rotation == 6) {
            if (up)
                quads.add(createQuad(SEU, NEU, NWU, SWU, textureSouth, xl * 16, xh * 16, 16 - zh * 16, 16 - zl * 16, tintIndex));
            if (down)
                quads.add(createQuad(NED, SED, SWD, NWD, textureNorth, 16 - xl * 16, 16 - xh * 16, 16 - zl * 16, 16 - zh * 16, tintIndex));
            if (west)
                quads.add(createQuad(SWU, NWU, NWD, SWD, textureEast, yl * 16, yh * 16, 16 - zh * 16, 16 - zl * 16, tintIndex));
            if (east)
                quads.add(createQuad(SED, NED, NEU, SEU, textureWest, 16 - yh * 16, 16 - yl * 16, 16 - zh * 16, 16 - zl * 16, tintIndex));
            if (north)
                quads.add(createQuad(NEU, NED, NWD, NWU, textureDown, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south)
                quads.add(createQuad(SWU, SWD, SED, SEU, textureUp, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        }
        if (rotation == 7) {
            if (up)
                quads.add(createQuad(SWU, SEU, NEU, NWU, textureSouth, zl * 16, zh * 16, xl * 16, xh * 16, tintIndex));
            if (down)
                quads.add(createQuad(SED, SWD, NWD, NED, textureNorth, 16 - zl * 16, 16 - zh * 16, xh * 16, xl * 16, tintIndex));
            if (west)
                quads.add(createQuad(SWD, SWU, NWU, NWD, textureUp, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (east)
                quads.add(createQuad(NED, NEU, SEU, SED, textureDown, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north)
                quads.add(createQuad(NWU, NEU, NED, NWD, textureEast, 16 - yh * 16, 16 - yl * 16, xl * 16, xh * 16, tintIndex));
            if (south)
                quads.add(createQuad(SWD, SED, SEU, SWU, textureWest, yl * 16, yh * 16, xl * 16, xh * 16, tintIndex));
        }
        if (rotation == 8) {
            if (up)
                quads.add(createQuad(NWU, SWU, SEU, NEU, textureNorth, xl * 16, xh * 16, zl * 16, zh * 16, tintIndex));
            if (down)
                quads.add(createQuad(SWD, NWD, NED, SED, textureSouth, 16 - xl * 16, 16 - xh * 16, zh * 16, zl * 16, tintIndex));
            if (west)
                quads.add(createQuad(NWD, SWD, SWU, NWU, textureWest, yl * 16, yh * 16, zl * 16, zh * 16, tintIndex));
            if (east)
                quads.add(createQuad(NEU, SEU, SED, NED, textureEast, 16 - yh * 16, 16 - yl * 16, zl * 16, zh * 16, tintIndex));
            if (north)
                quads.add(createQuad(NWD, NWU, NEU, NED, textureUp, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south)
                quads.add(createQuad(SED, SEU, SWU, SWD, textureDown, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        }
        if (rotation == 9) {
            if (up)
                quads.add(createQuad(NEU, NWU, SWU, SEU, textureNorth, zl * 16, zh * 16, 16 - xh * 16, 16 - xl * 16, tintIndex));
            if (down)
                quads.add(createQuad(NWD, NED, SED, SWD, textureSouth, 16 - zl * 16, 16 - zh * 16, 16 - xl * 16, 16 - xh * 16, tintIndex));
            if (west)
                quads.add(createQuad(NWU, NWD, SWD, SWU, textureDown, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (east)
                quads.add(createQuad(SEU, SED, NED, NEU, textureUp, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north)
                quads.add(createQuad(NED, NWD, NWU, NEU, textureWest, 16 - yh * 16, 16 - yl * 16, 16 - xh * 16, 16 - xl * 16, tintIndex));
            if (south)
                quads.add(createQuad(SEU, SWU, SWD, SED, textureEast, yl * 16, yh * 16, 16 - xh * 16, 16 - xl * 16, tintIndex));
        }
        if (rotation == 10) {
            if (up)
                quads.add(createQuad(SEU, NEU, NWU, SWU, textureNorth, xl * 16, xh * 16, 16 - zh * 16, 16 - zl * 16, tintIndex));
            if (down)
                quads.add(createQuad(NED, SED, SWD, NWD, textureSouth, 16 - xl * 16, 16 - xh * 16, 16 - zl * 16, 16 - zh * 16, tintIndex));
            if (west)
                quads.add(createQuad(SWU, NWU, NWD, SWD, textureEast, yl * 16, yh * 16, 16 - zh * 16, 16 - zl * 16, tintIndex));
            if (east)
                quads.add(createQuad(SED, NED, NEU, SEU, textureWest, 16 - yh * 16, 16 - yl * 16, 16 - zh * 16, 16 - zl * 16, tintIndex));
            if (north)
                quads.add(createQuad(NEU, NED, NWD, NWU, textureDown, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south)
                quads.add(createQuad(SWU, SWD, SED, SEU, textureUp, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
        }
        if (rotation == 11) {
            if (up)
                quads.add(createQuad(SWU, SEU, NEU, NWU, textureNorth, zl * 16, zh * 16, xl * 16, xh * 16, tintIndex));
            if (down)
                quads.add(createQuad(SED, SWD, NWD, NED, textureSouth, 16 - zl * 16, 16 - zh * 16, xh * 16, xl * 16, tintIndex));
            if (west)
                quads.add(createQuad(SWD, SWU, NWU, NWD, textureUp, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (east)
                quads.add(createQuad(NED, NEU, SEU, SED, textureDown, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north)
                quads.add(createQuad(NWU, NEU, NED, NWD, textureEast, 16 - yh * 16, 16 - yl * 16, xl * 16, xh * 16, tintIndex));
            if (south)
                quads.add(createQuad(SWD, SED, SEU, SWU, textureWest, yl * 16, yh * 16, xl * 16, xh * 16, tintIndex));
        }
        return quads;
    }

    public static List<BakedQuad> createSixFaceCuboid(float xl, float xh, float yl, float yh, float zl, float zh, int tintIndex, TextureAtlasSprite textureNorth, TextureAtlasSprite textureSouth, TextureAtlasSprite textureEast, TextureAtlasSprite textureWest, TextureAtlasSprite textureUp, TextureAtlasSprite textureDown, int rotation) {
        return createSixFaceCuboid(xl, xh, yl, yh, zl, zh, tintIndex, true, true, true, true, true, true, textureNorth, textureSouth, textureEast, textureWest, textureUp, textureDown, true, rotation);
    }

    public static List<BakedQuad> createSixFaceCuboid(float xl, float xh, float yl, float yh, float zl, float zh, int tintIndex, boolean north, boolean south, boolean east, boolean west, boolean up, boolean down, TextureAtlasSprite textureNorth, TextureAtlasSprite textureSouth, TextureAtlasSprite textureEast, TextureAtlasSprite textureWest, TextureAtlasSprite textureUp, TextureAtlasSprite textureDown, Boolean moveOverlay, int rotation) {
        List<BakedQuad> quads = new ArrayList<>();
        //Eight corners of the block
        Vec3 NWU = v(xl, yh, zl); //North-West-Up
        Vec3 NEU = v(xl, yh, zh); //...
        Vec3 NWD = v(xl, yl, zl);
        Vec3 NED = v(xl, yl, zh);
        Vec3 SWU = v(xh, yh, zl);
        Vec3 SEU = v(xh, yh, zh);
        Vec3 SWD = v(xh, yl, zl);
        Vec3 SED = v(xh, yl, zh); //South-East-Down
        if (xh - xl > 1 || yh - yl > 1 || zh - zl > 1) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.block_error"), true);
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
        Vec3 NWU = v(xl, yh, zl); //North-West-Up
        Vec3 NEU = v(xl, yh, zh); //...
        Vec3 NWD = v(xl, yl, zl);
        Vec3 NED = v(xl, yl, zh);
        Vec3 SWU = v(xh, yh, zl);
        Vec3 SEU = v(xh, yh, zh);
        Vec3 SWD = v(xh, yl, zl);
        Vec3 SED = v(xh, yl, zh); //South-East-Down
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
    private static Vec3 v(double x, double y, double z) {
        return new Vec3(x, y, z);
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
            overlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation("minecraft", "block/grass_block_side_overlay"));
            upOverlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation("minecraft", "block/grass_block_top"));
        }
        if (overlayIndex == 2) {
            tintIndex = 1;
            overlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_side_overlay_large"));
            upOverlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation("minecraft", "block/grass_block_top"));
        }
        if (overlayIndex == 3) {
            tintIndex = -1;
            overlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_snow_overlay"));
            upOverlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation("minecraft", "block/snow"));
        }
        if (overlayIndex == 4) {
            tintIndex = -1;
            overlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_snow_overlay_small"));
            upOverlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation("minecraft", "block/snow"));
        }
        if (overlayIndex == 5) {
            tintIndex = 1;
            overlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation("minecraft", "block/vine"));
        }
        if (overlayIndex >= 6 && overlayIndex <= 10) {
            tintIndex = -1;
            doNotMoveOverlay = false;
            if (overlayIndex == 6) {
                overlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/stone_brick_overlay"));
                upOverlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/stone_brick_overlay"));
                downOverlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/stone_brick_overlay"));
            }
            if (overlayIndex == 7) {
                overlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/brick_overlay"));
                upOverlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/brick_overlay"));
                downOverlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/brick_overlay"));
            }
            if (overlayIndex == 8) {
                overlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chiseled_sandstone_overlay"));
            }
            if (overlayIndex == 9) {
                overlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/boundary_overlay"));
                upOverlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/boundary_overlay"));
                downOverlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/boundary_overlay"));
            }
            if (overlayIndex == 10) {
                overlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chiseled_stone_overlay"));
                upOverlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chiseled_stone_overlay"));
                downOverlay = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chiseled_stone_overlay"));
            }
        }
        return ModelHelper.createSixFaceCuboid(xl, xh, yl, yh, zl, zh, tintIndex, north, south, east, west, up, down, overlay, overlay, overlay, overlay, upOverlay, downOverlay, doNotMoveOverlay, 0);
    }

    public static List<BakedQuad> createSlope(float xl, float xh, float yl, float yh, float zl, float zh, TextureAtlasSprite texture, int tintIndex, Direction direction) {
        List<BakedQuad> quads = new ArrayList<>();
        //Eight corners of the block
        Vec3 NWU = v(xl, yh, zl); //North-West-Up
        Vec3 NEU = v(xl, yh, zh); //...
        Vec3 NWD = v(xl, yl, zl);
        Vec3 NED = v(xl, yl, zh);
        Vec3 SWU = v(xh, yh, zl);
        Vec3 SEU = v(xh, yh, zh);
        Vec3 SWD = v(xh, yl, zl);
        Vec3 SED = v(xh, yl, zh); //South-East-Down
        if (xh - xl > 1 || yh - yl > 1 || zh - zl > 1) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.block_error"), true);
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