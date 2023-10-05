package mod.pianomanu.blockcarpentry.renderer.block;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mod.pianomanu.blockcarpentry.block.StandingSignFrameBlock;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.SignFrameTile;
import mod.pianomanu.blockcarpentry.util.BCWoodType;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Vector3f;

import java.util.List;
import java.util.Objects;

/**
 * This class is needed for rendering the sign (empty and filled) as well as
 * the text displayed on the sign.
 *
 * @author PianoManu
 * @version 1.0 10/03/23
 */
public class FrameSignRenderer extends TileEntityRenderer<SignFrameTile> {
    private final FrameSignRenderer.FrameSignModel model = new FrameSignRenderer.FrameSignModel();
    private final SignTileEntityRenderer.SignModel empty = new SignTileEntityRenderer.SignModel();

    public FrameSignRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(SignFrameTile signFrameTile, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packedLight, int combinedOverlay) {
        BlockState mimic = signFrameTile.getMimic();
        if (signFrameTile.getBlockState().get(StandingSignFrameBlock.CONTAINS_BLOCK) && mimic != null) {
            renderSign(signFrameTile, stack, buffer, combinedOverlay, packedLight);
        } else {
            renderEmpty(signFrameTile, stack, buffer, combinedOverlay, packedLight);
        }
    }

    private void renderSign(SignFrameTile tile, MatrixStack stack, IRenderTypeBuffer buffer, int combinedOverlay, int packedLight) {
        prepareStack(tile.getBlockState(), stack);


        TextureAtlasSprite sprite = TextureHelper.getTextureFromTileEntity(tile);
        RenderMaterial material = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, sprite.getName());
        IVertexBuilder vertexBuilder = material.getBuffer(buffer, RenderType::getEntityTranslucent);
        this.model.signBoard.render(stack, vertexBuilder, packedLight, combinedOverlay);
        this.model.signStick.render(stack, vertexBuilder, packedLight, combinedOverlay);
        stack.pop();

        try {
            drawText(tile, stack, buffer, combinedOverlay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderEmpty(SignFrameTile tile, MatrixStack stack, IRenderTypeBuffer buffer, int combinedOverlay, int packedLight) {
        WoodType woodtype = WoodType.OAK;
        prepareStack(tile.getBlockState(), stack);

        RenderMaterial rendermaterial = Atlases.getSignMaterial(woodtype);
        IVertexBuilder ivertexbuilder = rendermaterial.getBuffer(buffer, this.empty::getRenderType);
        this.empty.signBoard.render(stack, ivertexbuilder, packedLight, combinedOverlay);
        this.empty.signStick.render(stack, ivertexbuilder, packedLight, combinedOverlay);
        stack.pop();

        try {
            drawText(tile, stack, buffer, combinedOverlay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareStack(BlockState state, MatrixStack stack) {
        stack.push();
        float scaleFactor = 0.6666667F;
        if (state.getBlock() instanceof StandingSignBlock) {
            stack.translate(0.5D, 0.5D, 0.5D);
            float f1 = -((float) (state.get(StandingSignBlock.ROTATION) * 360) / 16.0F);
            stack.rotate(Vector3f.YP.rotationDegrees(f1));
            this.model.signStick.showModel = true;
        } else {
            stack.translate(0.5D, 0.5D, 0.5D);
            float f4 = -state.get(WallSignBlock.FACING).getHorizontalAngle();
            stack.rotate(Vector3f.YP.rotationDegrees(f4));
            stack.translate(0.0D, -0.3125D, -0.4375D);
            this.model.signStick.showModel = false;
        }
        stack.push();
        stack.scale(scaleFactor, -scaleFactor, -scaleFactor);
    }

    private void drawText(SignFrameTile tile, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
        FontRenderer fontrenderer = this.renderDispatcher.getFontRenderer();
        float f2 = 0.010416667F;
        stack.translate(0.0D, 0.33333334F, 0.046666667F);
        stack.scale(0.010416667F, -0.010416667F, 0.010416667F);
        int i = tile.getTextColor().getTextColor();
        double d0 = 0.4D;
        int j = (int) ((double) NativeImage.getRed(i) * 0.4D);
        int k = (int) ((double) NativeImage.getGreen(i) * 0.4D);
        int l = (int) ((double) NativeImage.getBlue(i) * 0.4D);
        int i1 = NativeImage.getCombined(0, l, k, j);
        int j1 = 20;

        for (int k1 = 0; k1 < 4; ++k1) {
            IReorderingProcessor ireorderingprocessor = tile.func_242686_a(k1, (p_243502_1_) -> {
                List<IReorderingProcessor> list = fontrenderer.func_238425_b_(p_243502_1_, 90);
                return list.isEmpty() ? IReorderingProcessor.field_242232_a : list.get(0);
            });
            if (ireorderingprocessor != null) {
                float f3 = (float) (-fontrenderer.func_243245_a(ireorderingprocessor) / 2);
                fontrenderer.func_238416_a_(ireorderingprocessor, f3, (float) (k1 * 10 - 20), i1, false, stack.getLast().getMatrix(), buffer, false, 0, light);
            }
        }
        stack.pop();
    }

    public static final class FrameSignModel extends Model {
        public final ModelRenderer signBoard = new ModelRenderer(16, 16, 0, 0);
        public final ModelRenderer signStick;

        public FrameSignModel() {
            super(RenderType::getEntityTranslucent);
            for (int i = 0; i < 24; i++) {
                this.signBoard.setTextureOffset(i % 10, 0).addBox(-12.0F + i, -14.0F, -1.0F, 1.0F, 12.0F, 2.0F);
            }
            this.signStick = new ModelRenderer(64, 32, 0, 14);
            this.signStick.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 14.0F, 2.0F, 0.0F);
        }

        @Override
        public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
            this.signBoard.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.signStick.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }
}
//========SOLI DEO GLORIA========//