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

public class DoorKnobBakedModel {

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
        quads.add(ModelHelper.createQuad(NWU, NEU, SEU, SWU, texture, 7, 9, 7, 9, -1));
        quads.add(ModelHelper.createQuad(SWD, SED, NED, NWD, texture, 7, 9, 7, 9, -1));
        quads.add(ModelHelper.createQuad(NWD, NWU, SWU, SWD, texture, 7, 9, 7, 9, -1));
        quads.add(ModelHelper.createQuad(SED, SEU, NEU, NED, texture, 7, 9, 7, 9, -1));
        quads.add(ModelHelper.createQuad(NWD, NED, NEU, NWU, texture, 7, 9, 7, 9, -1));
        quads.add(ModelHelper.createQuad(SWU, SEU, SED, SWD, texture, 7, 9, 7, 9, -1));
        return quads;
    }

    private static Vec3 v(double x, double y, double z) {
        return new Vec3(x, y, z);
    }
}
//========SOLI DEO GLORIA========//