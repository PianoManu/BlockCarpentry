package mod.pianomanu.blockcarpentry.bakedmodels;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.pianomanu.blockcarpentry.util.MathUtils;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains helper methods to ease the quad population process
 * See {@link mod.pianomanu.blockcarpentry.util.ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.4 11/08/23
 */
public class QuadUtils {

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

    private static QuadBakingVertexConsumer prepare(BakedQuad[] quad, TextureAtlasSprite sprite, Vec3 normal, int tintIndex) {
        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer(q -> quad[0] = q);
        builder.setSprite(sprite);
        builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
        builder.setTintIndex(tintIndex);
        builder.setShade(true);
        return builder;
    }

    private static Vec3[] rotateFace(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, int rotation) {
        Vec3 tmp;
        for (int i = 0; i < rotation; i++) {
            tmp = v4;
            v4 = v3;
            v3 = v2;
            v2 = v1;
            v1 = tmp;
        }
        return new Vec3[]{v1, v2, v3, v4};
    }

    public static float[] rotateUV(float v1u, float v1v, float v2u, float v2v, float v3u, float v3v, float v4u, float v4v, int rotation) {
        float tmpU;
        float tmpV;
        for (int i = 0; i < rotation; i++) {
            tmpU = v1u;
            tmpV = v1v;
            v1u = v2u;
            v1v = v2v;
            v2u = v3u;
            v2v = v3v;
            v3u = v4u;
            v3v = v4v;
            v4u = tmpU;
            v4v = tmpV;
        }
        return new float[]{v1u, v1v, v2u, v2v, v3u, v3v, v4u, v4v};
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
        return createQuad(v1, v2, v3, v4, sprite, ulow, uhigh, vlow, vhigh, tintIndex, false);
    }

    public static BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, int tintIndex, int rotation) {
        return createQuad(v1, v2, v3, v4, sprite, 0, 0, 0, 16, 16, 16, 16, 0, tintIndex, false, rotation);
    }

    public static BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex, int rotation) {
        Vec3[] vs = rotateFace(v1, v2, v3, v4, rotation);
        v1 = vs[0];
        v2 = vs[1];
        v3 = vs[2];
        v4 = vs[3];
        return createQuad(v1, v2, v3, v4, sprite, ulow, uhigh, vlow, vhigh, tintIndex, false);
    }

    public static BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, int tintIndex) {
        return createQuad(v1, v2, v3, v4, sprite, 0, 16, 0, 16, tintIndex);
    }

    public static BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, Direction direction, int tintIndex, int rotation, boolean keepUV, boolean invert) {
        return createQuad(v1, v2, v3, v4, sprite, direction, tintIndex, rotation, keepUV, invert, false);
    }

    public static BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, Direction direction, int tintIndex, int rotation, boolean keepUV, boolean invert, boolean moveVertically) {
        int verticalOffset = moveVertically ? 16 : 0;
        if (keepUV)
            return createQuad(v1, v2, v3, v4, sprite, 0, 0, 0, 16, 16, 16, 16, 0, tintIndex, invert, rotation);
        Vec3[] vs = rotateFace(v1, v2, v3, v4, rotation);
        v1 = vs[0];
        v2 = vs[1];
        v3 = vs[2];
        v4 = vs[3];
        float[] uvs = rotateUVcomplex(v1, v2, v3, v4, direction, rotation, verticalOffset);
        return createQuad(v1, v2, v3, v4, sprite, uvs[0], uvs[1], uvs[2], uvs[3], uvs[4], uvs[5], uvs[6], uvs[7], tintIndex, invert, 0);
    }

    public static BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex, boolean invert) {
        return createQuad(v1, v2, v3, v4, sprite, ulow, vlow, ulow, vhigh, uhigh, vhigh, uhigh, vlow, tintIndex, invert, 0);
    }

    public static BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, float v1u, float v1v, float v2u, float v2v, float v3u, float v3v, float v4u, float v4v, int tintIndex, boolean invert, int rotation) {
        Vec3[] vs = rotateFace(v1, v2, v3, v4, rotation);
        v1 = vs[0];
        v2 = vs[1];
        v3 = vs[2];
        v4 = vs[3];

        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();
        BakedQuad[] quad = new BakedQuad[1];
        QuadBakingVertexConsumer builder = prepare(quad, sprite, normal, tintIndex);

        if (invert) {
            normal = normal.reverse();
            putVertex(builder, normal, v4.x, v4.y, v4.z, v4u, v4v, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v3.x, v3.y, v3.z, v3u, v3v, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v2.x, v2.y, v2.z, v2u, v2v, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v1.x, v1.y, v1.z, v1u, v1v, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
        } else {
            putVertex(builder, normal, v1.x, v1.y, v1.z, v1u, v1v, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v2.x, v2.y, v2.z, v2u, v2v, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v3.x, v3.y, v3.z, v3u, v3v, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
            putVertex(builder, normal, v4.x, v4.y, v4.z, v4u, v4v, sprite, 1.0f, 1.0f, 1.0f, 1.0f);
        }
        return quad[0];
    }

    public static BakedQuad createQuadInverted(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex) {
        return createQuad(v1, v2, v3, v4, sprite, tintIndex);
    }

    public static List<BakedQuad> createQuadComplex(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, Direction direction, int tintIndex, int rotation, boolean keepUV, boolean invert) {
        List<BakedQuad> quads = new ArrayList<>();
        Vec3 COGVec = centerOfGravityVec(v1, v2, v3, v4);
        Vec3[] vs = rotateFace(v1, v2, v3, v4, rotation);
        v1 = vs[0];
        v2 = vs[1];
        v3 = vs[2];
        v4 = vs[3];
        if (!keepUV) {
            float[] uvs = rotateUVcomplex(v1, v2, v3, v4, direction, rotation);
            float cogU = (float) (direction.getAxis() == Direction.Axis.X ? COGVec.z * 16f : COGVec.x * 16f);
            float cogV = (float) (direction.getAxis().isHorizontal() ? COGVec.y * 16f : COGVec.x * 16f);
            quads.add(createQuad(v1, v2, COGVec, COGVec, sprite, uvs[0], uvs[1], uvs[2], uvs[3], cogU, cogV, cogU, cogV, tintIndex, invert, 0));
            quads.add(createQuad(v2, v3, COGVec, COGVec, sprite, uvs[2], uvs[3], uvs[4], uvs[5], cogU, cogV, cogU, cogV, tintIndex, invert, 0));
            quads.add(createQuad(v3, v4, COGVec, COGVec, sprite, uvs[4], uvs[5], uvs[6], uvs[7], cogU, cogV, cogU, cogV, tintIndex, invert, 0));
            quads.add(createQuad(COGVec, v4, v1, COGVec, sprite, cogU, cogV, uvs[6], uvs[7], uvs[0], uvs[1], cogU, cogV, tintIndex, invert, 0));
        } else {
            quads.add(createQuad(v1, v2, COGVec, COGVec, sprite, 0, 0, 0, 16, 8, 8, 8, 8, tintIndex, invert, 0));
            quads.add(createQuad(v2, v3, COGVec, COGVec, sprite, 0, 16, 16, 16, 8, 8, 8, 8, tintIndex, invert, 0));
            quads.add(createQuad(v3, v4, COGVec, COGVec, sprite, 16, 16, 16, 0, 8, 8, 8, 8, tintIndex, invert, 0));
            quads.add(createQuad(COGVec, v4, v1, COGVec, sprite, 8, 8, 16, 0, 0, 0, 8, 8, tintIndex, invert, 0));
        }
        return quads;
    }

    public static float[] rotateUVcomplex(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, Direction direction, int rotation) {
        return rotateUVcomplex(v1, v2, v3, v4, direction, rotation, 0);
    }

    public static float[] rotateUVcomplex(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, Direction direction, int rotation, int verticalOffset) {
        return switch (direction) {
            case UP -> rotateUV((float) v1.x * 16f, (float) v1.z * 16f, (float) v2.x * 16f, (float) v2.z * 16f, (float) v3.x * 16f, (float) v3.z * 16f, (float) v4.x * 16f, (float) v4.z * 16f, rotation);
            case DOWN -> rotateUV((float) v1.x * 16f, 16 - (float) v1.z * 16f, (float) v2.x * 16f, 16 - (float) v2.z * 16f, (float) v3.x * 16f, 16 - (float) v3.z * 16f, (float) v4.x * 16f, 16 - (float) v4.z * 16f, rotation);
            case WEST -> rotateUV((float) v1.z * 16f, 16 - (float) v1.y * 16f + verticalOffset, (float) v2.z * 16f, 16 - (float) v2.y * 16f + verticalOffset, (float) v3.z * 16f, 16 - (float) v3.y * 16f + verticalOffset, (float) v4.z * 16f, 16 - (float) v4.y * 16f + verticalOffset, rotation);
            case EAST -> rotateUV(16 - (float) v1.z * 16f, 16 - (float) v1.y * 16f + verticalOffset, 16 - (float) v2.z * 16f, 16 - (float) v2.y * 16f + verticalOffset, 16 - (float) v3.z * 16f, 16 - (float) v3.y * 16f + verticalOffset, 16 - (float) v4.z * 16f, 16 - (float) v4.y * 16f + verticalOffset, rotation);
            case SOUTH -> rotateUV((float) v1.x * 16f, 16 - (float) v1.y * 16f + verticalOffset, (float) v2.x * 16f, 16 - (float) v2.y * 16f + verticalOffset, (float) v3.x * 16f, 16 - (float) v3.y * 16f + verticalOffset, (float) v4.x * 16f, 16 - (float) v4.y * 16f + verticalOffset, rotation);
            case NORTH -> rotateUV(16 - (float) v1.x * 16f, 16 - (float) v1.y * 16f + verticalOffset, 16 - (float) v2.x * 16f, 16 - (float) v2.y * 16f + verticalOffset, 16 - (float) v3.x * 16f, 16 - (float) v3.y * 16f + verticalOffset, 16 - (float) v4.x * 16f, 16 - (float) v4.y * 16f + verticalOffset, rotation);
        };
    }

    private static Vec3 centerOfGravityVec(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4) {
        Vec3 interpol13 = MathUtils.interpolate(v1, v3, 0.5, false);
        Vec3 interpol24 = MathUtils.interpolate(v2, v4, 0.5, false);
        return MathUtils.interpolate(interpol13, interpol24, 0.5, false);
    }
}
//========SOLI DEO GLORIA========//