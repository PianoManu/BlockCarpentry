package mod.pianomanu.blockcarpentry.renderer.block;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import mod.pianomanu.blockcarpentry.block.StandingSignFrameBlock;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.SignFrameTile;
import mod.pianomanu.blockcarpentry.util.BCWoodType;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * This class is needed for rendering the sign (empty and filled) as well as
 * the text displayed on the sign.
 *
 * @author PianoManu
 * @version 1.0 10/03/23
 */
public class FrameSignRenderer implements BlockEntityRenderer<SignFrameTile> {
    public static final int MAX_LINE_WIDTH = 90;
    private static final int LINE_HEIGHT = 10;
    private static final String STICK = "stick";
    private static final int BLACK_TEXT_OUTLINE_COLOR = -988212;
    private static final int OUTLINE_RENDER_DISTANCE = Mth.square(16);
    private final BlockEntityRendererProvider.Context context;
    private final Font font;

    public FrameSignRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.font = context.getFont();
    }

    private static LayerDefinition createSignLayer(BlockState tileState) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        if (tileState.hasProperty(StandingSignBlock.ROTATION))
            partdefinition.addOrReplaceChild(STICK, CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 14.0F, 2.0F), PartPose.ZERO);
        else
            partdefinition.addOrReplaceChild(STICK, CubeListBuilder.create(), PartPose.ZERO);
        for (int i = 0; i < 24; i++) {
            String partName = "sign" + i;
            partdefinition.addOrReplaceChild(partName, CubeListBuilder.create().texOffs(i % 10, 0).addBox(-12.0F + i, -14.0F, -1.0F, 1.0F, 12.0F, 2.0F), PartPose.ZERO);
        }
        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    private static boolean isOutlineVisible(SignFrameTile blockEntity, int textColor) {
        if (textColor == DyeColor.BLACK.getTextColor()) {
            return true;
        } else {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer localplayer = minecraft.player;
            if (localplayer != null && minecraft.options.getCameraType().isFirstPerson() && localplayer.isScoping()) {
                return true;
            } else {
                Entity entity = minecraft.getCameraEntity();
                return entity != null && entity.distanceToSqr(Vec3.atCenterOf(blockEntity.getBlockPos())) < (double) OUTLINE_RENDER_DISTANCE;
            }
        }
    }

    private static int getDarkColor(SignFrameTile blockEntity) {
        int textColor = blockEntity.getColor().getTextColor();
        double d0 = 0.4D;
        int r = (int) ((double) NativeImage.getR(textColor) * d0);
        int g = (int) ((double) NativeImage.getG(textColor) * d0);
        int b = (int) ((double) NativeImage.getB(textColor) * d0);
        return textColor == DyeColor.BLACK.getTextColor() && blockEntity.hasGlowingText() ? BLACK_TEXT_OUTLINE_COLOR : NativeImage.combine(0, b, g, r);
    }

    @Override
    public void render(SignFrameTile signFrameTile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        BlockState mimic = signFrameTile.getMimic();
        if (signFrameTile.getBlockState().getValue(StandingSignFrameBlock.CONTAINS_BLOCK) && mimic != null) {
            renderSign(signFrameTile, poseStack, buffer, combinedOverlay, packedLight);
        } else {
            renderEmpty(signFrameTile, poseStack, buffer, combinedOverlay, packedLight);
        }
    }

    private void renderSign(SignFrameTile tile, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        prepareStack(tile.getBlockState(), stack);

        BlockState mimic = tile.getMimic();
        Material material = new Material(TextureAtlas.LOCATION_BLOCKS, TextureHelper.textureLocation(mimic));
        LayerDefinition layerDefinition = createSignLayer(tile.getBlockState());
        FrameSignRenderer.FrameSignModel signrenderer$signmodel = new FrameSignRenderer.FrameSignModel(layerDefinition.bakeRoot());
        VertexConsumer vertexconsumer = material.buffer(buffer, RenderType::entityTranslucent);
        signrenderer$signmodel.root.render(stack, vertexconsumer, combinedOverlay, packedLight);

        try {
            drawText(tile, stack, buffer, combinedOverlay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderEmpty(SignFrameTile tile, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        WoodType woodtype = tile.getBlockState().getBlock().equals(Registration.STANDING_SIGN_FRAMEBLOCK.get()) ? BCWoodType.FRAME : BCWoodType.ILLUSION;
        SignRenderer.SignModel signrenderer$signmodel = new SignRenderer.SignModel(context.bakeLayer(ModelLayers.createSignModelName(woodtype)));
        prepareStack(tile.getBlockState(), stack);

        Material material = Sheets.getSignMaterial(woodtype);
        VertexConsumer vertexconsumer = material.buffer(buffer, signrenderer$signmodel::renderType);
        signrenderer$signmodel.root.render(stack, vertexconsumer, combinedOverlay, packedLight);

        try {
            drawText(tile, stack, buffer, combinedOverlay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareStack(BlockState state, PoseStack stack) {
        stack.pushPose();
        float scaleFactor = 0.6666667F;
        if (state.getBlock() instanceof StandingSignBlock) {
            stack.translate(0.5D, 0.5D, 0.5D);
            float f1 = -((float) (state.getValue(StandingSignBlock.ROTATION) * 360) / 16.0F);
            stack.mulPose(Vector3f.YP.rotationDegrees(f1));
        } else {
            stack.translate(0.5D, 0.5D, 0.5D);
            float f4 = -state.getValue(WallSignBlock.FACING).toYRot();
            stack.mulPose(Vector3f.YP.rotationDegrees(f4));
            stack.translate(0.0D, -0.3125D, -0.4375D);
        }
        stack.pushPose();
        stack.scale(scaleFactor, -scaleFactor, -scaleFactor);
    }

    private void drawText(SignFrameTile tile, PoseStack stack, MultiBufferSource buffer, int combinedOverlay) {
        stack.popPose();
        float scaleFactor = 0.010416667F;
        stack.translate(0.0D, (double) 0.33333334F, (double) 0.046666667F);
        stack.scale(scaleFactor, -scaleFactor, scaleFactor);

        int darkColor = getDarkColor(tile);
        int j = 20;
        FormattedCharSequence[] formattedCharSequences = tile.getRenderMessages(Minecraft.getInstance().isTextFilteringEnabled(), (component) -> {
            List<FormattedCharSequence> list = this.font.split(component, 90);
            return list.isEmpty() ? FormattedCharSequence.EMPTY : list.get(0);
        });
        int textColor;
        boolean flag;
        int newCombinedOverlay;
        if (tile.hasGlowingText()) {
            textColor = tile.getColor().getTextColor();
            flag = isOutlineVisible(tile, textColor);
            newCombinedOverlay = 15728880;
        } else {
            textColor = darkColor;
            flag = false;
            newCombinedOverlay = combinedOverlay;
        }
        for (int line = 0; line < 4; ++line) {
            FormattedCharSequence formattedcharsequence = formattedCharSequences[line];
            float f3 = (float) (-this.font.width(formattedcharsequence) / 2);
            if (flag) {
                this.font.drawInBatch8xOutline(formattedcharsequence, f3, (float) (line * LINE_HEIGHT - j), textColor, darkColor, stack.last().pose(), buffer, newCombinedOverlay);
            } else {
                this.font.drawInBatch(formattedcharsequence, f3, (float) (line * LINE_HEIGHT - j), textColor, false, stack.last().pose(), buffer, false, 0, newCombinedOverlay);
            }
        }
        stack.popPose();
    }

    public static final class FrameSignModel extends Model {
        public final ModelPart root;
        public final ModelPart stick;

        public FrameSignModel(ModelPart part) {
            super(RenderType::entityTranslucent);
            this.root = part;
            this.stick = part.getChild(STICK);
        }

        @Override
        public void renderToBuffer(PoseStack p_112510_, VertexConsumer p_112511_, int p_112512_, int p_112513_, float p_112514_, float p_112515_, float p_112516_, float p_112517_) {
            this.root.render(p_112510_, p_112511_, p_112512_, p_112513_, p_112514_, p_112515_, p_112516_, p_112517_);
        }
    }
}
//========SOLI DEO GLORIA========//