package mod.pianomanu.blockcarpentry.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

import java.util.ArrayList;
import java.util.List;

public class ModelHelper {

    private static void putVertex(BakedQuadBuilder builder, Vec3d normal,
                                  double x, double y, double z, float u, float v, TextureAtlasSprite sprite, float r, float g, float b) {

        ImmutableList<VertexFormatElement> elements = builder.getVertexFormat().getElements().asList();
        for (int j = 0 ; j < elements.size() ; j++) {
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
        builder.setQuadTint(1);
    }

    private static BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite, float ulow, float uhigh, float vlow, float vhigh, int tintIndex) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        if (tintIndex>-1) {
            builder.setQuadTint(tintIndex);
        }
        putVertex(builder, normal, v1.x, v1.y, v1.z, ulow, vlow, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, ulow, vhigh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, uhigh, vhigh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, uhigh, vlow, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    public static List<BakedQuad> createCuboid(float xl, float xh, float yl, float yh, float zl, float zh, TextureAtlasSprite texture, int tintIndex) {
        List<BakedQuad> quads = new ArrayList<>();
        //Eight corners of the block
        Vec3d NWU = v(xl,yh,zl); //North-West-Up
        Vec3d NEU = v(xl,yh,zh); //...
        Vec3d NWD = v(xl,yl,zl);
        Vec3d NED = v(xl,yl,zh);
        Vec3d SWU = v(xh,yh,zl);
        Vec3d SEU = v(xh,yh,zh);
        Vec3d SWD = v(xh,yl,zl);
        Vec3d SED = v(xh,yl,zh); //South-East-Down
        quads.add(createQuad(NWU, NEU, SEU, SWU, texture, xl*16, xh*16, zl*16, zh*16, tintIndex));
        quads.add(createQuad(SWD, SED, NED, NWD, texture, xl*16, xh*16, zl*16, zh*16, tintIndex));
        quads.add(createQuad(SWU, SWD, NWD, NWU, texture, xl*16, xh*16, 16-yh*16, 16-yl*16, tintIndex));
        quads.add(createQuad(NEU, NED, SED, SEU, texture, xl*16, xh*16, 16-yh*16, 16-yl*16, tintIndex));
        quads.add(createQuad(NWU, NWD, NED, NEU, texture, zl*16, zh*16, 16-yh*16, 16-yl*16, tintIndex));
        quads.add(createQuad(SEU, SED, SWD, SWU, texture, zl*16, zh*16, 16-yh*16, 16-yl*16, tintIndex));
        return quads;
    }

    private static Vec3d v(double x, double y, double z) {
        return new Vec3d(x, y, z);
    }
}
