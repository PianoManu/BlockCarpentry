package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FenceFrameBlock;
import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
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
 * @version 1.4 05/01/21
 */
public class IllusionFenceBakedModel implements IDynamicBakedModel {
    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && !(mimic.getBlock() instanceof FrameBlock)) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            if (location != null) {
                IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                if (model != null) {
                    return getIllusionQuads(state, side, rand, extraData, model);
                }
            }
        }
        return Collections.emptyList();
    }

    private List<BakedQuad> getIllusionQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, IBakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        Integer design = extraData.getData(FrameBlockTile.DESIGN);
        if (mimic != null && state != null) {
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            List<BakedQuad> quads = new ArrayList<>(ModelHelper.createSixFaceCuboid(6 / 16f, 10 / 16f, 0f, 1f, 6 / 16f, 10 / 16f, mimic, model, extraData, rand, tintIndex));

            if (design == 0) {
                if (state.get(FenceFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 6 / 16f, 9 / 16f, 0f, 6 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 12 / 16f, 15 / 16f, 0f, 6 / 16f, mimic, model, extraData, rand, tintIndex));
                }
                if (state.get(FenceFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 6 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 12 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                }
                if (state.get(FenceFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 6 / 16f, 9 / 16f, 10 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 12 / 16f, 15 / 16f, 10 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                }
                if (state.get(FenceFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 6 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 12 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                }
            }
            if (design == 1) {
                if (state.get(FenceFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 3 / 16f, 6 / 16f, 0f, 6 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 9 / 16f, 12 / 16f, 0f, 6 / 16f, mimic, model, extraData, rand, tintIndex));
                }
                if (state.get(FenceFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 3 / 16f, 6 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 9 / 16f, 12 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                }
                if (state.get(FenceFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 3 / 16f, 6 / 16f, 10 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 9 / 16f, 12 / 16f, 10 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                }
                if (state.get(FenceFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 3 / 16f, 6 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 9 / 16f, 12 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                }
            }
            if (design == 2) {
                if (state.get(FenceFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 4 / 16f, 11 / 16f, 0f, 6 / 16f, mimic, model, extraData, rand, tintIndex));
                    //quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 9 / 16f, 12 / 16f, 0f, 6 / 16f, mimic,model,extraData,rand, tintIndex));
                }
                if (state.get(FenceFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 4 / 16f, 11 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    //quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 9 / 16f, 12 / 16f, 7 / 16f, 9 / 16f, mimic,model,extraData,rand, tintIndex));
                }
                if (state.get(FenceFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 4 / 16f, 11 / 16f, 10 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                    //quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 9 / 16f, 12 / 16f, 10 / 16f, 1f, mimic,model,extraData,rand, tintIndex));
                }
                if (state.get(FenceFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 4 / 16f, 11 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 9 / 16f, 12 / 16f, 7 / 16f, 9 / 16f, mimic,model,extraData,rand, tintIndex));
                }
            }
            if (design == 3) {
                if (state.get(FenceFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 6 / 16f, 9 / 16f, 0f, 6 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 12 / 16f, 15 / 16f, 0f, 6 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(6 / 16f, 10 / 16f, 0f, 1f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex));
                }
                if (state.get(FenceFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 6 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 12 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 16 / 16f, 0f, 1f, 6 / 16f, 10 / 16f, mimic, model, extraData, rand, tintIndex));
                }
                if (state.get(FenceFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 6 / 16f, 9 / 16f, 10 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 12 / 16f, 15 / 16f, 10 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(6 / 16f, 10 / 16f, 0f, 1f, 14 / 16f, 16 / 16f, mimic, model, extraData, rand, tintIndex));
                }
                if (state.get(FenceFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 6 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 12 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 0f, 1f, 6 / 16f, 10 / 16f, mimic, model, extraData, rand, tintIndex));
                }
            }
            if (design == 4) {
                if (state.get(FenceFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, 2 / 16f, 6 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 13 / 16f, 15 / 16f, 2 / 16f, 6 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 6 / 16f, 8 / 16f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 12 / 16f, 14 / 16f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex));
                }
                if (state.get(FenceFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 13 / 16f, 7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 13 / 16f, 13 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 6 / 16f, 8 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 12 / 16f, 14 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                }
                if (state.get(FenceFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, 10 / 16f, 14 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 13 / 16f, 15 / 16f, 10 / 16f, 14 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 6 / 16f, 8 / 16f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 12 / 16f, 14 / 16f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                }
                if (state.get(FenceFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(3 / 16f, 6 / 16f, 7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(3 / 16f, 6 / 16f, 13 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 6 / 16f, 8 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 12 / 16f, 14 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex));
                }
            }
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                quads.addAll(ModelHelper.createOverlay(6 / 16f, 10 / 16f, 0f, 1f, 6 / 16f, 10 / 16f, overlayIndex));
            }
            return quads;
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
    public boolean func_230044_c_() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/oak_planks"));
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