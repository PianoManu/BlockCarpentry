package mod.pianomanu.blockcarpentry.bakedmodels.helper;

import mod.pianomanu.blockcarpentry.util.ModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class HandleBakedModel {

    public static List<BakedQuad> createHandle(float xl, float xh, float yl, float yh, float zl, float zh, int flag, int design_texture) {
        TextureAtlasSprite texture;
        if (design_texture == 0) {
            texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/iron_block"));
        } else if (design_texture == 1) {
            texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/obsidian"));
        } else if (design_texture == 2) {
            texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/stone"));
        } else if (design_texture == 3) {
            texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/gold_block"));
        } else {
            texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/oak_log"));
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
        /*quads.add(create5x4Quad(NWU, NEU, SEU, SWU, texture, flag));
        quads.add(create5x4Quad(SWD, SED, NED, NWD, texture, flag));
        quads.add(create1x4Quad(NWD, NWU, SWU, SWD, texture, flag));
        quads.add(create1x4Quad(SED, SEU, NEU, NED, texture, flag));
        quads.add(create1x5Quad(NWD, NED, NEU, NWU, texture, flag));
        quads.add(create1x5Quad(SWU, SEU, SED, SWD, texture, flag));*/
        ModelHelper.createCuboid(xl, xh, yl, yh, zl, zh, texture, -1);
        return quads;
    }

    private static Vec3 v(double x, double y, double z) {
        return new Vec3(x, y, z);
    }
}
//========SOLI DEO GLORIA========//