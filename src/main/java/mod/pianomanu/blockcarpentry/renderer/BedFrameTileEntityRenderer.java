package mod.pianomanu.blockcarpentry.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mod.pianomanu.blockcarpentry.renderer.model.BedModel;
import mod.pianomanu.blockcarpentry.tileentity.BedFrameTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//UNUSED
@OnlyIn(Dist.CLIENT)
public class BedFrameTileEntityRenderer extends TileEntityRenderer<BedFrameTile> {
    private final ModelRenderer BED_FOOT;
    private final BedModel bedModel = new BedModel();
    private BlockState mimic;

    public BedFrameTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        BED_FOOT = new ModelRenderer(64,64,0,0);
        BED_FOOT.addBox(0f,0f,0f,16f,16f,6f);
    }

    @Override
    public void render(BedFrameTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        this.mimic = tileEntityIn.getMimic();
        if(tileEntityIn.getMimic()!=null) {
            renderBlock(this.mimic,matrixStackIn,bufferIn,combinedLightIn,combinedOverlayIn);
        }
        matrixStackIn.pop();
    }

    private void renderBlock(BlockState blockState, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(blockState, matrixStack, buffer, combinedLight, combinedOverlay);
    }


}
