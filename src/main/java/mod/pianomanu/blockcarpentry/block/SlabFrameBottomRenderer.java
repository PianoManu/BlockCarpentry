package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class SlabFrameBottomRenderer extends TileEntityRenderer<FrameBlockTile> {
    public SlabFrameBottomRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(FrameBlockTile tileEntityIn, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        //TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(MAGICBLOCK_TEXTURE);
        IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent());
        BlockState mimic = tileEntityIn.getMimic();
        //TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(mimic.getBlock().getRegistryName().getNamespace(), "block/"+mimic.getBlock().getRegistryName().getPath()));
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(mimic.getBlock().getRegistryName().getNamespace(), "block/"+mimic.getBlock().getRegistryName().getPath()));

        long time = System.currentTimeMillis();
        float dx1 = diffFunction(time, 1000, 0.0001f);
        float dx2 = diffFunction(time, 1500, 0.00005f);
        float dx3 = diffFunction(time, 1200, 0.00011f);
        float dx4 = diffFunction(time, 1300, 0.00006f);
        float dy1 = diffFunction(time, 1400, 0.00009f);
        float dy2 = diffFunction(time, 1600, 0.00007f);
        float dy3 = diffFunction(time, 1000, 0.00015f);
        float dy4 = diffFunction(time, 1200, 0.00003f);

        float angle = (time / 100) % 360;
        Quaternion rotation = Vector3f.YP.rotationDegrees(angle);
        float scale = 1.0f + diffFunction(time,1000, 0.001f);

        matrixStack.push();
        matrixStack.translate(.5, .5, .5);
        matrixStack.rotate(rotation);
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-.5, -.5, -.5);

        add(builder, matrixStack, 0, 0, 0, sprite.getMinU(), sprite.getMinV());
        add(builder, matrixStack, 0, 0, 0.5f, sprite.getMaxU(), sprite.getMinV());
        add(builder, matrixStack, 1, 1, 0.5f, sprite.getMaxU(), sprite.getMaxV());
        add(builder, matrixStack, 0, 1, 0.5f, sprite.getMinU(), sprite.getMaxV());

        add(builder, matrixStack, 0 + dx4, 1 - dy4, .5f, sprite.getMinU(), sprite.getMaxV());
        add(builder, matrixStack, 1 - dx3, 1 - dy3, .5f, sprite.getMaxU(), sprite.getMaxV());
        add(builder, matrixStack, 1 - dx2, 0 + dy2, .5f, sprite.getMaxU(), sprite.getMinV());
        add(builder, matrixStack, 0 + dx1, 0 + dy1, .5f, sprite.getMinU(), sprite.getMinV());

        matrixStack.pop();
    }

    private void add(IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float u, float v) {
        renderer.pos(stack.getLast().getMatrix(), x, y, z)
                .color(1.0f, 1.0f, 1.0f, 1.0f)
                .tex(u, v)
                .lightmap(0, 240)
                .normal(1, 0, 0)
                .endVertex();
    }

    private static float diffFunction(long time, long delta, float scale) {
        long dt = time % (delta * 2);
        if (dt > delta) {
            dt = 2*delta - dt;
        }
        return dt * scale;
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(Registration.FRAMEBLOCK_TILE.get(), SlabFrameBottomRenderer::new);
    }
}
