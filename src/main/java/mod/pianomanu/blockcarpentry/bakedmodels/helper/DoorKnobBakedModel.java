package mod.pianomanu.blockcarpentry.bakedmodels.helper;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

import java.util.ArrayList;
import java.util.List;

public class DoorKnobBakedModel {
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
        builder.setApplyDiffuseLighting(true);
    }

    private static BakedQuad createSquareQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, int flag) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getNearest(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        float ul = 7;
        float uh = 9;
        float vl = 7;
        float vh = 9;
        putVertex(builder, normal, v1.x, v1.y, v1.z, ul, vl, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, ul, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, uh, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, uh, vl, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    private static BakedQuad create1x4Quad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, int flag) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getNearest(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        float ul = 0;
        float uh = 4;
        float vl = 0;
        float vh = 1;
        if (flag == 3) {
            uh = 5;
            vh = 1;
        } else if(flag == 1) {
            uh = 4;
            vh = 5;
        } else if(flag == 2) {
            uh = 4;
            vh = 1;
        } else if(flag == 0) {
            uh = 1;
            vh = 5;
        }
        putVertex(builder, normal, v1.x, v1.y, v1.z, ul, vl, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, ul, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, uh, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, uh, vl, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    private static BakedQuad create1x5Quad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, int flag) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getNearest(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        float ul = 0;
        float uh = 1;
        float vl = 0;
        float vh = 5;
        if (flag == 3) {
            vh = 4;
        } else if(flag == 1) {
            uh = 4;
            vh = 1;
        } else if(flag == 0) {
            vh = 4;
            uh = 5;
        }
        putVertex(builder, normal, v1.x, v1.y, v1.z, ul, vl, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, ul, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, uh, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, uh, vl, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    public static List<BakedQuad> createDoorKnob(float xl, float xh, float yl, float yh, float zl, float zh, int flag, int design_texture) {
        TextureAtlasSprite texture;
        if (design_texture == 0) {
            texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/iron_block"));
        } else if (design_texture == 1) {
            texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/obsidian"));
        } else if (design_texture == 2) {
            texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/stone"));
        } else {
            texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/gold_block"));
        }
        List<BakedQuad> quads = new ArrayList<>();
        Vec3 NWU = v(xl, yh, zl);
        Vec3 NEU = v(xl, yh, zh);
        Vec3 NWD = v(xl, yl, zl);
        Vec3 NED = v(xl, yl, zh);
        Vec3 SWU = v(xh, yh, zl);
        Vec3 SEU = v(xh, yh, zh);
        Vec3 SWD = v(xh, yl, zl);
        Vec3 SED = v(xh, yl, zh);
        quads.add(createSquareQuad(NWU, NEU, SEU, SWU, texture, flag));
        quads.add(createSquareQuad(SWD, SED, NED, NWD, texture, flag));
        quads.add(createSquareQuad(NWD, NWU, SWU, SWD, texture, flag));
        quads.add(createSquareQuad(SED, SEU, NEU, NED, texture, flag));
        quads.add(createSquareQuad(NWD, NED, NEU, NWU, texture, flag));
        quads.add(createSquareQuad(SWU, SEU, SED, SWD, texture, flag));
        return quads;
    }

    private static Vec3 v(double x, double y, double z) {
        return new Vec3(x, y, z);
    }
}
//========SOLI DEO GLORIA========//