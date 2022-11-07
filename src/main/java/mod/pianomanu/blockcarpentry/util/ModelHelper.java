package mod.pianomanu.blockcarpentry.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Util class for building cuboid shapes
 *
 * @author PianoManu
 * @version 1.3 11/07/22
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
    private static void putVertex(VertexConsumer builder, Vec3 normal,
                                  double x, double y, double z, float u, float v, TextureAtlasSprite sprite, float r, float g, float b, float a) {

        float iu = sprite.getU(u);
        float iv = sprite.getV(v);
        builder.vertex(x, y, z)
                .uv(iu, iv)
                .uv2(0, 0)
                .color(r, g, b, a)
                .normal((float) normal.x(), (float) normal.y(), (float) normal.z())
                .endVertex();
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

        BakedQuad[] quad = new BakedQuad[1];
        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer(q -> quad[0] = q);
        builder.setSprite(sprite);
        builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
        builder.setTintIndex(tintIndex);

        putVertex(builder, normal, v1.x, v1.y, v1.z, ulow, vlow, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, ulow, vhigh, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, uhigh, vhigh, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, uhigh, vlow, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
        return quad[0];
    }

    public static BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex, boolean invert) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();
        if (invert) {
            normal = normal.reverse();
        }

        BakedQuad[] quad = new BakedQuad[1];
        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer(q -> quad[0] = q);
        builder.setSprite(sprite);
        builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
        builder.setTintIndex(tintIndex);

        if (invert) {
            putVertex(builder, normal, v1.x, v1.y, v1.z, ulow, vlow, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v2.x, v2.y, v2.z, ulow, vhigh, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v3.x, v3.y, v3.z, uhigh, vhigh, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v4.x, v4.y, v4.z, uhigh, vlow, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
        } else {
            putVertex(builder, normal, v4.x, v4.y, v4.z, ulow, vlow, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v3.x, v3.y, v3.z, uhigh, vlow, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v2.x, v2.y, v2.z, uhigh, vhigh, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v1.x, v1.y, v1.z, ulow, vhigh, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
        }
        return quad[0];
    }

    public static BakedQuad createQuadInverted(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex) {
        return createQuadInverted(v1, v2, v3, v4, sprite, ulow, uhigh, vlow, vhigh, tintIndex, false);
    }

    public static BakedQuad createQuadInverted(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex, boolean invert) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();
        if (invert) {
            normal = normal.reverse();
        }

        BakedQuad[] quad = new BakedQuad[1];
        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer(q -> quad[0] = q);
        builder.setSprite(sprite);
        builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
        builder.setTintIndex(tintIndex);

        if (invert) {
            putVertex(builder, normal, v4.x, v4.y, v4.z, ulow, vlow, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v3.x, v3.y, v3.z, uhigh, vlow, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v2.x, v2.y, v2.z, uhigh, vhigh, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v1.x, v1.y, v1.z, ulow, vhigh, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
        } else {
            putVertex(builder, normal, v1.x, v1.y, v1.z, ulow, vlow, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v2.x, v2.y, v2.z, uhigh, vlow, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v3.x, v3.y, v3.z, uhigh, vhigh, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v4.x, v4.y, v4.z, ulow, vhigh, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
        }
        return quad[0];
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
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("message.blockcarpentry.block_error"), true);
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

    public static List<BakedQuad> createSixFaceCuboid(float xl, float xh, float yl, float yh, float zl, float zh, BlockState mimic, BakedModel model, ModelData extraData, RandomSource rand, int tintIndex, int rotation) {
        return createSixFaceCuboid(xl, xh, yl, yh, zl, zh, mimic, model, extraData, rand, tintIndex, true, true, true, true, true, true, rotation);
    }

    public static List<BakedQuad> createSixFaceCuboid(float xl, float xh, float yl, float yh, float zl, float zh, BlockState mimic, BakedModel model, ModelData extraData, RandomSource rand, int tintIndex, boolean north, boolean south, boolean east, boolean west, boolean up, boolean down, int rotation) {
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
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("message.blockcarpentry.block_error"), true);
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
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("message.blockcarpentry.block_not_available"), true);
            }
            return quads;
        }
        TextureAtlasSprite textureNorth = textureList.get(0);
        TextureAtlasSprite textureEast = textureList.get(0);
        TextureAtlasSprite textureSouth = textureList.get(0);
        TextureAtlasSprite textureWest = textureList.get(0);
        TextureAtlasSprite textureUp = textureList.get(0);
        TextureAtlasSprite textureDown = textureList.get(0);
        for (BakedQuad quad : model.getQuads(extraData.get(FrameBlockTile.MIMIC), Direction.NORTH, rand, extraData, RenderType.translucent())) {
            textureNorth = quad.getSprite();
        }
        for (BakedQuad quad : model.getQuads(extraData.get(FrameBlockTile.MIMIC), Direction.EAST, rand, extraData, RenderType.translucent())) {
            textureEast = quad.getSprite();
        }
        for (BakedQuad quad : model.getQuads(extraData.get(FrameBlockTile.MIMIC), Direction.SOUTH, rand, extraData, RenderType.translucent())) {
            textureSouth = quad.getSprite();
        }
        for (BakedQuad quad : model.getQuads(extraData.get(FrameBlockTile.MIMIC), Direction.WEST, rand, extraData, RenderType.translucent())) {
            textureWest = quad.getSprite();
        }
        for (BakedQuad quad : model.getQuads(extraData.get(FrameBlockTile.MIMIC), Direction.UP, rand, extraData, RenderType.translucent())) {
            textureUp = quad.getSprite();
        }
        for (BakedQuad quad : model.getQuads(extraData.get(FrameBlockTile.MIMIC), Direction.DOWN, rand, extraData, RenderType.translucent())) {
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
            if (up)
                quads.add(createQuad(NEU, NWU, SWU, SEU, textureUp, zl * 16, zh * 16, 16 - xh * 16, 16 - xl * 16, tintIndex));
            if (down)
                quads.add(createQuad(NWD, NED, SED, SWD, textureDown, 16 - zl * 16, 16 - zh * 16, 16 - xl * 16, 16 - xh * 16, tintIndex));
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
            if (up)
                quads.add(createQuad(SEU, NEU, NWU, SWU, textureUp, 16 - xh * 16, 16 - xl * 16, 16 - zh * 16, 16 - zl * 16, tintIndex));
            if (down)
                quads.add(createQuad(NED, SED, SWD, NWD, textureDown, xh * 16, xl * 16, 16 - zl * 16, 16 - zh * 16, tintIndex));
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
            if (up)
                quads.add(createQuad(SWU, SEU, NEU, NWU, textureUp, 16 - zh * 16, 16 - zl * 16, xl * 16, xh * 16, tintIndex));
            if (down)
                quads.add(createQuad(SED, SWD, NWD, NED, textureDown, zh * 16, zl * 16, xh * 16, xl * 16, tintIndex));
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
        Vec3 SWU = v(xl, yh, zh); //...
        Vec3 NWD = v(xl, yl, zl);
        Vec3 SWD = v(xl, yl, zh);
        Vec3 NEU = v(xh, yh, zl);
        Vec3 SEU = v(xh, yh, zh);
        Vec3 NED = v(xh, yl, zl);
        Vec3 SED = v(xh, yl, zh); //South-East-Down
        if (xh - xl > 1 || yh - yl > 1 || zh - zl > 1) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("message.blockcarpentry.block_error"), true);
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
                quads.add(createQuad(NWU, SWU, SEU, NEU, textureUp, xl * 16, xh * 16, zl * 16, zh * 16, tintIndex));
            if (down && textureDown != null)
                quads.add(createQuad(NED, SED, SWD, NWD, textureDown, xh * 16, xl * 16, 16 - zl * 16, 16 - zh * 16, tintIndex));
            //not moved overlay - texture starts from y=1
            if (west && textureWest != null && !moveOverlay)
                quads.add(createQuad(NWU, NWD, SWD, SWU, textureWest, zl * 16, zh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (east && textureEast != null && !moveOverlay)
                quads.add(createQuad(SEU, SED, NED, NEU, textureEast, 16 - zh * 16, 16 - zl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (north && textureNorth != null && !moveOverlay)
                quads.add(createQuad(NEU, NED, NWD, NWU, textureNorth, 16 - xh * 16, 16 - xl * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            if (south && textureSouth != null && !moveOverlay)
                quads.add(createQuad(SWU, SWD, SED, SEU, textureSouth, xl * 16, xh * 16, 16 - yh * 16, 16 - yl * 16, tintIndex));
            //moved overlay - texture starts from height of block
            if (west && textureWest != null && moveOverlay)
                quads.add(createQuad(NWU, NWD, SWD, SWU, textureWest, zl * 16, zh * 16, yl * 16, yh * 16, tintIndex));
            if (east && textureEast != null && moveOverlay)
                quads.add(createQuad(SEU, SED, NED, NEU, textureEast, 16 - zh * 16, 16 - zl * 16, yl * 16, yh * 16, tintIndex));
            if (north && textureNorth != null && moveOverlay)
                quads.add(createQuad(NEU, NED, NWD, NWU, textureNorth, 16 - xh * 16, 16 - xl * 16, yl * 16, yh * 16, tintIndex));
            if (south && textureSouth != null && moveOverlay)
                quads.add(createQuad(SWU, SWD, SED, SEU, textureSouth, xl * 16, xh * 16, yl * 16, yh * 16, tintIndex));
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
     * Creates a single voxel starting at {x, y, z} to {x+1, y+1, z+1}
     *
     * @param x         start value in x-direction
     * @param y         start value in y-direction
     * @param z         start value in z-direction
     * @param texture   texture for the voxel
     * @param tintIndex set to value > 0, if you want to enable tinting
     * @param north     whether the north face of the voxel is visible
     * @param south     whether the south face of the voxel is visible
     * @param east      whether the east face of the voxel is visible
     * @param west      whether the west face of the voxel is visible
     * @param up        whether the top face of the voxel is visible
     * @param down      whether the bottom face of the voxel is visible
     * @return list of {@link BakedQuad} representing the voxel
     */
    public static List<BakedQuad> createVoxel(int x, int y, int z, TextureAtlasSprite texture, int tintIndex, boolean north, boolean south, boolean east, boolean west, boolean up, boolean down) {
        return createCuboid(x / 16f, (x + 1) / 16f, y / 16f, (y + 1) / 16f, z / 16f, (z + 1) / 16f, texture, tintIndex, north, south, east, west, up, down);
    }

    public static List<BakedQuad> createSixFaceVoxel(int x, int y, int z, int tintIndex, boolean north, boolean south, boolean east, boolean west, boolean up, boolean down, TextureAtlasSprite textureNorth, TextureAtlasSprite textureSouth, TextureAtlasSprite textureEast, TextureAtlasSprite textureWest, TextureAtlasSprite textureUp, TextureAtlasSprite textureDown, Boolean moveOverlay, int rotation) {
        return createSixFaceCuboid(x / 16f, (x + 1) / 16f, y / 16f, (y + 1) / 16f, z / 16f, (z + 1) / 16f, tintIndex, north, south, east, west, up, down, textureNorth, textureSouth, textureEast, textureWest, textureUp, textureDown, moveOverlay, 0);
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
            overlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/grass_block_side_overlay"));
            upOverlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/grass_block_top"));
        }
        if (overlayIndex == 2) {
            tintIndex = 1;
            overlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_side_overlay_large"));
            upOverlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/grass_block_top"));
        }
        if (overlayIndex == 3) {
            overlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_snow_overlay"));
            upOverlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/snow"));
        }
        if (overlayIndex == 4) {
            overlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_snow_overlay_small"));
            upOverlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/snow"));
        }
        if (overlayIndex == 5) {
            tintIndex = 1;
            overlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/vine"));
        }
        if (overlayIndex >= 6 && overlayIndex <= 10) {
            doNotMoveOverlay = false;
            if (overlayIndex == 6) {
                overlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/stone_brick_overlay"));
                upOverlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/stone_brick_overlay"));
                downOverlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/stone_brick_overlay"));
            }
            if (overlayIndex == 7) {
                overlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/brick_overlay"));
                upOverlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/brick_overlay"));
                downOverlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/brick_overlay"));
            }
            if (overlayIndex == 8) {
                overlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chiseled_sandstone_overlay"));
            }
            if (overlayIndex == 9) {
                overlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/boundary_overlay"));
                upOverlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/boundary_overlay"));
                downOverlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/boundary_overlay"));
            }
            if (overlayIndex == 10) {
                overlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chiseled_stone_overlay"));
                upOverlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chiseled_stone_overlay"));
                downOverlay = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chiseled_stone_overlay"));
            }
        }
        /*int xStartInt = (int) xl*16;
        int xEndInt = (int) xh*16;
        int yStartInt = (int) yl*16;
        int yEndInt = (int) yh*16;
        int zStartInt = (int) zl*16;
        int zEndInt = (int) zh*16;
        return ModelHelper.createSixFaceCuboidAsVoxels(xStartInt, xEndInt, yStartInt, yEndInt, zStartInt, zEndInt, tintIndex, north, south, east, west, up, down, overlay, overlay, overlay, overlay, upOverlay, downOverlay, doNotMoveOverlay);*/
        return ModelHelper.createSixFaceCuboid(xl, xh, yl, yh, zl, zh, tintIndex, north, south, east, west, up, down, overlay, overlay, overlay, overlay, upOverlay, downOverlay, doNotMoveOverlay, 0);
    }

    public static List<BakedQuad> createSixFaceCuboidAsVoxels(int xl, int xh, int yl, int yh, int zl, int zh, int tintIndex, boolean north, boolean south, boolean east, boolean west, boolean up, boolean down, TextureAtlasSprite textureNorth, TextureAtlasSprite textureSouth, TextureAtlasSprite textureEast, TextureAtlasSprite textureWest, TextureAtlasSprite textureUp, TextureAtlasSprite textureDown, Boolean moveOverlay) {
        List<BakedQuad> quads = new ArrayList<>();
        for (int x = xl; x < xh; x++) {
            for (int y = yl; y < yh; y++) {
                for (int z = zl; z < zh; z++) {
                    quads.addAll(createSixFaceVoxel(x, y, z, tintIndex, z == zl, z == zh - 1, x == xh - 1, x == xl, y == yh - 1, y == yl, textureNorth, textureSouth, textureEast, textureWest, textureUp, textureDown, moveOverlay, 0));
                }
            }
        }
        return quads;
    }
}
//========SOLI DEO GLORIA========//