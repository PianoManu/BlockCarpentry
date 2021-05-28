package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Contains all information for the block model
 * See {@link mod.pianomanu.blockcarpentry.util.ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.6 05/28/21
 */
public class FrameBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {

        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        Integer design = extraData.getData(FrameBlockTile.DESIGN);
        Integer desTex = extraData.getData(FrameBlockTile.DESIGN_TEXTURE);
        if (side != null) {
            return Collections.emptyList();
        }
        if (mimic != null && !(mimic.getBlock() instanceof FrameBlock)) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            if (location != null && state != null) {
                IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                if (model != null) {
                    //TODO what about full blocks with different side textures -> IllusionBlock
                    List<TextureAtlasSprite> textureList = TextureHelper.getTextureFromModel(model, extraData, rand);
                    TextureAtlasSprite texture;
                    Integer tex = extraData.getData(FrameBlockTile.TEXTURE);
                    if (textureList.size() <= tex) {
                        extraData.setData(FrameBlockTile.TEXTURE, 0);
                        tex = 0;
                    }
                    if (textureList.size() == 0) {
                        if (Minecraft.getInstance().player != null) {
                            Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.block_not_available"), true);
                        }
                        return Collections.emptyList();
                    }
                    texture = textureList.get(tex);
                    int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
                    List<BakedQuad> quads = new ArrayList<>(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0f, 1f, texture, tintIndex));
                    int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
                    if (overlayIndex != 0) {
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 1f, overlayIndex));
                    }
                    return quads;
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return getTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }
}
//========SOLI DEO GLORIA========//