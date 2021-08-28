package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.block.SixWaySlabFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.TwoBlocksFrameBlockTile;
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
 * @version 1.9 08/28/21
 */
public class IllusionSlabBakedModel implements IDynamicBakedModel {
    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(TwoBlocksFrameBlockTile.MIMIC_1);
        if (mimic != null && !(mimic.getBlock() instanceof FrameBlock)) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getIllusionQuads(state, side, rand, extraData, model);
        }
        return Collections.emptyList();
    }

    //supresses "Unboxing of "extraData..." may produce NullPointerException
    @SuppressWarnings("all")
    private List<BakedQuad> getIllusionQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, IBakedModel model) {
        if (side == null) {
            return Collections.emptyList();
        }
        BlockState mimic_1 = extraData.getData(TwoBlocksFrameBlockTile.MIMIC_1);
        BlockState mimic_2 = extraData.getData(TwoBlocksFrameBlockTile.MIMIC_2);
        if (mimic_1 != null && state != null) {
            int tintIndex_1 = BlockAppearanceHelper.setTintIndex(mimic_1);
            int tintIndex_2 = mimic_2 == null ? -1 : BlockAppearanceHelper.setTintIndex(mimic_2);
            int rotation_1 = extraData.getData(TwoBlocksFrameBlockTile.ROTATION_1);
            int rotation_2 = extraData.getData(TwoBlocksFrameBlockTile.ROTATION_2);
            boolean isDouble = state.get(SixWaySlabFrameBlock.DOUBLE_SLAB);
            boolean renderNorth = side == Direction.NORTH && extraData.getData(TwoBlocksFrameBlockTile.NORTH_VISIBLE);
            boolean renderEast = side == Direction.EAST && extraData.getData(TwoBlocksFrameBlockTile.EAST_VISIBLE);
            boolean renderSouth = side == Direction.SOUTH && extraData.getData(TwoBlocksFrameBlockTile.SOUTH_VISIBLE);
            boolean renderWest = side == Direction.WEST && extraData.getData(TwoBlocksFrameBlockTile.WEST_VISIBLE);
            boolean renderUp = side == Direction.UP && extraData.getData(TwoBlocksFrameBlockTile.UP_VISIBLE);
            boolean renderDown = side == Direction.DOWN && extraData.getData(TwoBlocksFrameBlockTile.DOWN_VISIBLE);
            List<BakedQuad> quads = new ArrayList<>();
            switch (state.get(SixWaySlabFrameBlock.FACING)) {
                case UP:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 0.5f, 0f, 1f, mimic_1, model, extraData, rand, tintIndex_1, renderNorth, renderSouth, renderEast, renderWest, side == Direction.UP, renderDown, rotation_1));
                    break;
                case DOWN:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0.5f, 1f, 0f, 1f, mimic_1, model, extraData, rand, tintIndex_1, renderNorth, renderSouth, renderEast, renderWest, renderUp, side == Direction.DOWN, rotation_1));
                    break;
                case WEST:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 0f, 1f, 0f, 1f, mimic_1, model, extraData, rand, tintIndex_1, renderNorth, renderSouth, renderEast, side == Direction.WEST, renderUp, renderDown, rotation_1));
                    break;
                case SOUTH:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, 0.5f, mimic_1, model, extraData, rand, tintIndex_1, renderNorth, side == Direction.SOUTH, renderEast, renderWest, renderUp, renderDown, rotation_1));
                    break;
                case NORTH:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0.5f, 1f, mimic_1, model, extraData, rand, tintIndex_1, side == Direction.NORTH, renderSouth, renderEast, renderWest, renderUp, renderDown, rotation_1));
                    break;
                case EAST:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 0f, 1f, 0f, 1f, mimic_1, model, extraData, rand, tintIndex_1, renderNorth, renderSouth, side == Direction.EAST, renderWest, renderUp, renderDown, rotation_1));
                    break;
            }
            if (state.get(SixWaySlabFrameBlock.DOUBLE_SLAB) && mimic_2 != null) {
                ModelResourceLocation location_2 = BlockModelShapes.getModelLocation(mimic_2);
                IBakedModel model_2 = Minecraft.getInstance().getModelManager().getModel(location_2);
                switch (state.get(SixWaySlabFrameBlock.FACING)) {
                    case UP:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0.5f, 1f, 0f, 1f, mimic_2, model_2, extraData, rand, tintIndex_2, renderNorth, renderSouth, renderEast, renderWest, renderUp, side == Direction.DOWN, rotation_2));
                        break;
                    case DOWN:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 0.5f, 0f, 1f, mimic_2, model_2, extraData, rand, tintIndex_2, renderNorth, renderSouth, renderEast, renderWest, side == Direction.UP, renderDown, rotation_2));
                        break;
                    case WEST:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 0f, 1f, 0f, 1f, mimic_2, model_2, extraData, rand, tintIndex_2, renderNorth, renderSouth, side == Direction.EAST, renderWest, renderUp, renderDown, rotation_2));
                        break;
                    case SOUTH:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0.5f, 1f, mimic_2, model_2, extraData, rand, tintIndex_2, side == Direction.NORTH, renderSouth, renderEast, renderWest, renderUp, renderDown, rotation_2));
                        break;
                    case NORTH:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, 0.5f, mimic_2, model_2, extraData, rand, tintIndex_2, renderNorth, side == Direction.SOUTH, renderEast, renderWest, renderUp, renderDown, rotation_2));
                        break;
                    case EAST:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 0f, 1f, 0f, 1f, mimic_2, model_2, extraData, rand, tintIndex_2, renderNorth, renderSouth, renderEast, side == Direction.WEST, renderUp, renderDown, rotation_2));
                        break;
                }
            }
            int overlayIndex_1 = extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_1);
            if (extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_1) != 0) {
                switch (state.get(SixWaySlabFrameBlock.FACING)) {
                    case UP:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 0.5f, 0f, 1f, overlayIndex_1, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, false));
                        break;
                    case DOWN:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0.5f, 1f, 0f, 1f, overlayIndex_1, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, false));
                        break;
                    case WEST:
                        quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0f, 1f, 0f, 1f, overlayIndex_1, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, false));
                        break;
                    case SOUTH:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 0.5f, overlayIndex_1, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, false));
                        break;
                    case NORTH:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0.5f, 1f, overlayIndex_1, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, false));
                        break;
                    case EAST:
                        quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0f, 1f, 0f, 1f, overlayIndex_1, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, false));
                        break;
                }
            }
            if (state.get(SixWaySlabFrameBlock.DOUBLE_SLAB)) {
                int overlayIndex_2 = extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_2);
                if (extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_2) != 0) {
                    switch (state.get(SixWaySlabFrameBlock.FACING)) {
                        case UP:
                            quads.addAll(ModelHelper.createOverlay(0f, 1f, 0.5f, 1f, 0f, 1f, overlayIndex_2, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, false));
                            break;
                        case DOWN:
                            quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 0.5f, 0f, 1f, overlayIndex_2, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, false));
                            break;
                        case WEST:
                            quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0f, 1f, 0f, 1f, overlayIndex_2, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, false));
                            break;
                        case SOUTH:
                            quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0.5f, 1f, overlayIndex_2, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, false));
                            break;
                        case NORTH:
                            quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 0.5f, overlayIndex_2, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, false));
                            break;
                        case EAST:
                            quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0f, 1f, 0f, 1f, overlayIndex_2, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, false));
                            break;
                    }
                }
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
    @Nonnull
    @SuppressWarnings("deprecation")
    public TextureAtlasSprite getParticleTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/oak_planks"));
    }

    @Override
    @Nonnull
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }
}
//========SOLI DEO GLORIA========//