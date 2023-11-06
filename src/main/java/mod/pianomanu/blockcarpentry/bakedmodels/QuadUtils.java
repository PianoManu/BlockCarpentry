package mod.pianomanu.blockcarpentry.bakedmodels;

import com.google.common.collect.ImmutableList;
import mod.pianomanu.blockcarpentry.util.MathUtils;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contains helper methods to ease the quad population process
 * See {@link mod.pianomanu.blockcarpentry.util.ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.4 11/03/23
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
    private static void putVertex(BakedQuadBuilder builder, Vector3d normal,
                                  double x, double y, double z, float u, float v, TextureAtlasSprite sprite, float r, float g, float b, float a) {

        ImmutableList<VertexFormatElement> elements = builder.getVertexFormat().getElements().asList();
        for (int j = 0; j < elements.size(); j++) {
            VertexFormatElement e = elements.get(j);
            switch (e.getUsage()) {
                case POSITION:
                    builder.put(j, (float) x, (float) y, (float) z, 1.0f);
                    break;
                case COLOR:
                    builder.put(j, r, g, b, a);
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

    private static BakedQuadBuilder prepare(TextureAtlasSprite sprite, Vector3d normal, int tintIndex) {
        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        builder.setQuadTint(tintIndex);
        return builder;
    }

    private static Vector3d[] rotateFace(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, int rotation) {
        Vector3d tmp;
        for (int i = 0; i < rotation; i++) {
            tmp = v1;
            v1 = v2;
            v2 = v3;
            v3 = v4;
            v4 = tmp;
        }
        return new Vector3d[]{v1, v2, v3, v4};
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
        return createQuad(v1, v2, v3, v4, sprite, ulow, uhigh, vlow, vhigh, tintIndex, false);
    }

    public static BakedQuad createQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, int tintIndex, int rotation) {
        Vector3d[] vs = rotateFace(v1, v2, v3, v4, rotation);
        v1 = vs[0];
        v2 = vs[1];
        v3 = vs[2];
        v4 = vs[3];
        return createQuad(v1, v2, v3, v4, sprite, 0, 0, 0, 16, 16, 16, 16, 0, tintIndex, false);
    }

    public static BakedQuad createQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex, int rotation) {
        Vector3d[] vs = rotateFace(v1, v2, v3, v4, rotation);
        v1 = vs[0];
        v2 = vs[1];
        v3 = vs[2];
        v4 = vs[3];
        return createQuad(v1, v2, v3, v4, sprite, ulow, uhigh, vlow, vhigh, tintIndex, false);
    }

    public static BakedQuad createQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, int tintIndex) {
        return createQuad(v1, v2, v3, v4, sprite, 0, 16, 0, 16, tintIndex);
    }

    public static BakedQuad createQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, Direction direction, int tintIndex, int rotation, boolean keepUV, boolean invert) {
        return createQuad(v1, v2, v3, v4, sprite, direction, tintIndex, rotation, keepUV, invert, false);
    }

    public static BakedQuad createQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, Direction direction, int tintIndex, int rotation, boolean keepUV, boolean invert, boolean moveVertically) {
        int verticalOffset = moveVertically ? 16 : 0;
        if (keepUV)
            return createQuad(v1, v2, v3, v4, sprite, 0, 0, 0, 16, 16, 16, 16, 0, tintIndex, invert);
        switch (direction) {
            case UP:
                return createQuad(v1, v2, v3, v4, sprite, (float) v1.x * 16f, (float) v1.z * 16f, (float) v2.x * 16f, (float) v2.z * 16f, (float) v3.x * 16f, (float) v3.z * 16f, (float) v4.x * 16f, (float) v4.z * 16f, tintIndex, invert);
            case DOWN:
                return createQuad(v1, v2, v3, v4, sprite, (float) v1.x * 16f, 16 - (float) v1.z * 16f, (float) v2.x * 16f, 16 - (float) v2.z * 16f, (float) v3.x * 16f, 16 - (float) v3.z * 16f, (float) v4.x * 16f, 16 - (float) v4.z * 16f, tintIndex, invert);
            case WEST:
                return createQuad(v1, v2, v3, v4, sprite, (float) v1.z * 16f, 16 - (float) v1.y * 16f + verticalOffset, (float) v2.z * 16f, 16 - (float) v2.y * 16f + verticalOffset, (float) v3.z * 16f, 16 - (float) v3.y * 16f + verticalOffset, (float) v4.z * 16f, 16 - (float) v4.y * 16f + verticalOffset, tintIndex, invert);
            case EAST:
                return createQuad(v1, v2, v3, v4, sprite, 16 - (float) v1.z * 16f, 16 - (float) v1.y * 16f + verticalOffset, 16 - (float) v2.z * 16f, 16 - (float) v2.y * 16f + verticalOffset, 16 - (float) v3.z * 16f, 16 - (float) v3.y * 16f + verticalOffset, 16 - (float) v4.z * 16f, 16 - (float) v4.y * 16f + verticalOffset, tintIndex, invert);
            case SOUTH:
                return createQuad(v1, v2, v3, v4, sprite, (float) v1.x * 16f, 16 - (float) v1.y * 16f + verticalOffset, (float) v2.x * 16f, 16 - (float) v2.y * 16f + verticalOffset, (float) v3.x * 16f, 16 - (float) v3.y * 16f + verticalOffset, (float) v4.x * 16f, 16 - (float) v4.y * 16f + verticalOffset, tintIndex, invert);
            case NORTH:
                return createQuad(v1, v2, v3, v4, sprite, 16 - (float) v1.x * 16f, 16 - (float) v1.y * 16f + verticalOffset, 16 - (float) v2.x * 16f, 16 - (float) v2.y * 16f + verticalOffset, 16 - (float) v3.x * 16f, 16 - (float) v3.y * 16f + verticalOffset, 16 - (float) v4.x * 16f, 16 - (float) v4.y * 16f + verticalOffset, tintIndex, invert);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static BakedQuad createQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex, boolean invert) {
        return createQuad(v1, v2, v3, v4, sprite, ulow, vlow, ulow, vhigh, uhigh, vhigh, uhigh, vlow, tintIndex, invert);
    }

    public static BakedQuad createQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, float v1u, float v1v, float v2u, float v2v, float v3u, float v3v, float v4u, float v4v, int tintIndex, boolean invert) {
        Vector3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();
        BakedQuadBuilder builder = prepare(sprite, normal, tintIndex);

        if (invert) {
            normal = normal.inverse();
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
        return builder.build();
    }

    public static BakedQuad createQuadInverted(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex) {
        return createQuad(v1, v2, v3, v4, sprite, tintIndex);
    }

    public static List<BakedQuad> createQuadComplex(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, Direction direction, int tintIndex, int rotation, boolean keepUV, boolean invert) {
        List<BakedQuad> quads = new ArrayList<>();
        Vector3d COGVec = centerOfGravityVec(v1, v2, v3, v4);
        Vector3d[] vs = rotateFace(v1, v2, v3, v4, rotation);
        v1 = vs[0];
        v2 = vs[1];
        v3 = vs[2];
        v4 = vs[3];
        if (!keepUV) {
            quads.add(createQuad(v1, v2, COGVec, COGVec, sprite, direction, tintIndex, rotation, keepUV, invert));
            quads.add(createQuad(v2, v3, COGVec, COGVec, sprite, direction, tintIndex, rotation, keepUV, invert));
            quads.add(createQuad(v3, v4, COGVec, COGVec, sprite, direction, tintIndex, rotation, keepUV, invert));
            quads.add(createQuad(COGVec, v4, v1, COGVec, sprite, direction, tintIndex, rotation, keepUV, invert));
        } else {
            quads.add(createQuad(v1, v2, COGVec, COGVec, sprite, 0, 0, 0, 16, 8, 8, 8, 8, tintIndex, invert));
            quads.add(createQuad(v2, v3, COGVec, COGVec, sprite, 0, 16, 16, 16, 8, 8, 8, 8, tintIndex, invert));
            quads.add(createQuad(v3, v4, COGVec, COGVec, sprite, 16, 16, 16, 0, 8, 8, 8, 8, tintIndex, invert));
            quads.add(createQuad(COGVec, v4, v1, COGVec, sprite, 8, 8, 16, 0, 0, 0, 8, 8, tintIndex, invert));
        }
        return quads;
    }

    private static Vector3d centerOfGravityVec(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4) {
        Vector3d interpol13 = MathUtils.interpolate(v1, v3, 0.5, false);
        Vector3d interpol24 = MathUtils.interpolate(v2, v4, 0.5, false);
        return MathUtils.interpolate(interpol13, interpol24, 0.5, false);
    }
}
//========SOLI DEO GLORIA========//