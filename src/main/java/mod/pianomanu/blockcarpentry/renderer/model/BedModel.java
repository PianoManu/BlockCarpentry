package mod.pianomanu.blockcarpentry.renderer.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

//UNUSED
public class BedModel extends Model {
    private ModelRenderer bedBlanket = (new ModelRenderer(64, 64, 0, 0)).addBox(0.0F, 4.0F, 0.0F, 16.0F, 12.0F, 16.0F);
    public BedModel() {
        super(RenderType::getEntityTranslucent);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.bedBlanket.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }
}
