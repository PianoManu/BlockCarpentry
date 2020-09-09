package mod.pianomanu.blockcarpentry.bakedmodels.helper;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

import java.util.ArrayList;
import java.util.List;

public class HandleBakedModel {

    private static void putVertex(BakedQuadBuilder builder, Vec3d normal,
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
        builder.setApplyDiffuseLighting(true);
    }

    private static BakedQuad create5x4Quad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite, int flag) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        float ul = 0;
        float uh = 5;
        float vl = 0;
        float vh = 4;
        if (flag == 0) {
            uh = 1;
        } else if (flag == 1) {
            uh = 4;
            vh = 1;
        } else if (flag == 2) {
            uh = 4;
            vh = 5;
        }
        putVertex(builder, normal, v1.x, v1.y, v1.z, ul, vl, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, ul, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, uh, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, uh, vl, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    private static BakedQuad create1x4Quad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite, int flag) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        float ul = 0;
        float uh = 4;
        float vl = 0;
        float vh = 1;
        if (flag == 3) {
            uh = 5;
            vh = 1;
        } else if (flag == 1) {
            uh = 4;
            vh = 5;
        } else if (flag == 2) {
            uh = 4;
            vh = 1;
        } else if (flag == 0) {
            uh = 1;
            vh = 5;
        }
        putVertex(builder, normal, v1.x, v1.y, v1.z, ul, vl, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, ul, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, uh, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, uh, vl, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    private static BakedQuad create1x5Quad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite, int flag) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        float ul = 0;
        float uh = 1;
        float vl = 0;
        float vh = 5;
        if (flag == 3) {
            vh = 4;
        } else if (flag == 1) {
            uh = 4;
            vh = 1;
        } else if (flag == 0) {
            vh = 4;
            uh = 5;
        }
        putVertex(builder, normal, v1.x, v1.y, v1.z, ul, vl, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, ul, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, uh, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, uh, vl, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    public static List<BakedQuad> createHandle(float xl, float xh, float yl, float yh, float zl, float zh, int flag, int design_texture) {
        TextureAtlasSprite texture;
        if (design_texture == 0) {
            texture = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/iron_block"));
        } else if (design_texture == 1) {
            texture = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/obsidian"));
        } else if (design_texture == 2) {
            texture = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/stone"));
        } else if (design_texture == 3) {
            texture = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/gold_block"));
        } else {
            texture = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/oak_log"));
        }
        List<BakedQuad> quads = new ArrayList<>();
        Vec3d NWU = v(xl, yh, zl);
        Vec3d NEU = v(xl, yh, zh);
        Vec3d NWD = v(xl, yl, zl);
        Vec3d NED = v(xl, yl, zh);
        Vec3d SWU = v(xh, yh, zl);
        Vec3d SEU = v(xh, yh, zh);
        Vec3d SWD = v(xh, yl, zl);
        Vec3d SED = v(xh, yl, zh);
        quads.add(create5x4Quad(NWU, NEU, SEU, SWU, texture, flag));
        quads.add(create5x4Quad(SWD, SED, NED, NWD, texture, flag));
        quads.add(create1x4Quad(NWD, NWU, SWU, SWD, texture, flag));
        quads.add(create1x4Quad(SED, SEU, NEU, NED, texture, flag));
        quads.add(create1x5Quad(NWD, NED, NEU, NWU, texture, flag));
        quads.add(create1x5Quad(SWU, SEU, SED, SWD, texture, flag));
        return quads;
    }

    private static Vec3d v(double x, double y, double z) {
        return new Vec3d(x, y, z);
    }
}
//========SOLI DEO GLORIA========//