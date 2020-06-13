package mod.pianomanu.blockcarpentry.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;

@OnlyIn(Dist.CLIENT)
public class ButtonFrameTileEntityRenderer extends TileEntityRenderer<FrameBlockTile> {
    public ButtonFrameTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    //Always start rendering code with push and end with pop
    @Override
    public void render(FrameBlockTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockState blockStateInButton = tileEntityIn.getMimic();
        BlockPos tilePos = tileEntityIn.getPos();
        Block blockInButton = blockStateInButton.getBlock();
        TextureAtlasSprite blockInButtonSprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(blockInButton.getRegistryName().getNamespace(), "block/" + blockInButton.getRegistryName().getPath()));
        matrixStackIn.push();
        IModelData modelData = tileEntityIn.getModelData();
        renderBlock(blockStateInButton, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, modelData);
        matrixStackIn.pop();
    }

    private void renderBlock(BlockState blockState, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, IModelData modelData) {
        Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(blockState, matrixStack, buffer, light, overlay, modelData);
    }
}
