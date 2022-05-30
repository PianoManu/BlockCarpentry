package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.SixWaySlabFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.TwoBlocksFrameBlockTile;
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
 * @version 1.13 05/30/22
 */
public class SlabFrameBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {

        //1st block in slab
        BlockState mimic = extraData.getData(TwoBlocksFrameBlockTile.MIMIC_1);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getMimicQuads(state, side, rand, extraData, model);
        }

        return Collections.emptyList();
    }

    //supresses "Unboxing of "extraData..." may produce NullPointerException
    @SuppressWarnings("all")
    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, IBakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic_1 = extraData.getData(TwoBlocksFrameBlockTile.MIMIC_1);
        BlockState mimic_2 = extraData.getData(TwoBlocksFrameBlockTile.MIMIC_2);
        boolean sameBlocks;
        if (mimic_1 != null && mimic_2 != null)
            sameBlocks = mimic_1.getBlock().equals(mimic_2.getBlock());
        else
            sameBlocks = false; //no second block in slab: not the same, we can render the face between the two slabs - prevents crash, if only one slab is filled
        int tex_1 = extraData.getData(TwoBlocksFrameBlockTile.TEXTURE_1);
        int tex_2 = extraData.getData(TwoBlocksFrameBlockTile.TEXTURE_2);
        if (mimic_1 != null && state != null) {
            List<TextureAtlasSprite> textureList_1 = TextureHelper.getTextureFromModel(model, extraData, rand);
            List<TextureAtlasSprite> textureList_2 = new ArrayList<>();

            if (mimic_2 != null) {
                ModelResourceLocation location_2 = BlockModelShapes.getModelLocation(mimic_2);
                IBakedModel model_2 = Minecraft.getInstance().getModelManager().getModel(location_2);
                textureList_2 = TextureHelper.getSlabTextureFromModel2(model_2, extraData, rand);
            }

            TextureAtlasSprite texture_1;
            TextureAtlasSprite texture_2;
            if (textureList_1.size() <= tex_1) {
                extraData.setData(TwoBlocksFrameBlockTile.TEXTURE_1, 0);
                tex_1 = 0;
            }
            if (textureList_2.size() <= tex_2) {
                extraData.setData(TwoBlocksFrameBlockTile.TEXTURE_2, 0);
                tex_2 = 0;
            }
            if (textureList_1.size() == 0) {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.block_not_available"), true);
                }
                return Collections.emptyList();
            }
            texture_1 = textureList_1.get(tex_1);
            if (textureList_2.size() > 0)
                texture_2 = textureList_2.get(tex_2);
            else texture_2 = null;
            int tintIndex_1 = BlockAppearanceHelper.setTintIndex(mimic_1);
            int tintIndex_2 = mimic_2 == null ? -1 : BlockAppearanceHelper.setTintIndex(mimic_2);
            boolean renderNorth = extraData.getData(TwoBlocksFrameBlockTile.NORTH_VISIBLE);
            boolean renderEast = extraData.getData(TwoBlocksFrameBlockTile.EAST_VISIBLE);
            boolean renderSouth = extraData.getData(TwoBlocksFrameBlockTile.SOUTH_VISIBLE);
            boolean renderWest = extraData.getData(TwoBlocksFrameBlockTile.WEST_VISIBLE);
            boolean renderUp = extraData.getData(TwoBlocksFrameBlockTile.UP_VISIBLE);
            boolean renderDown = extraData.getData(TwoBlocksFrameBlockTile.DOWN_VISIBLE);
            List<BakedQuad> quads = new ArrayList<>();
            switch (state.get(SixWaySlabFrameBlock.FACING)) {
                case UP:
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 0.5f, 0f, 1f, texture_1, tintIndex_1, renderNorth, renderSouth, renderEast, renderWest, renderUp && !sameBlocks, renderDown));
                    break;
                case DOWN:
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0.5f, 1f, 0f, 1f, texture_1, tintIndex_1, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown && !sameBlocks));
                    break;
                case WEST:
                    quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 0f, 1f, 0f, 1f, texture_1, tintIndex_1, renderNorth, renderSouth, renderEast, renderWest && !sameBlocks, renderUp, renderDown));
                    break;
                case SOUTH:
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0f, 0.5f, texture_1, tintIndex_1, renderNorth, renderSouth && !sameBlocks, renderEast, renderWest, renderUp, renderDown));
                    break;
                case NORTH:
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0.5f, 1f, texture_1, tintIndex_1, renderNorth && !sameBlocks, renderSouth, renderEast, renderWest, renderUp, renderDown));
                    break;
                case EAST:
                    quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 0f, 1f, 0f, 1f, texture_1, tintIndex_1, renderNorth, renderSouth, renderEast && !sameBlocks, renderWest, renderUp, renderDown));
                    break;
            }
            if (state.get(SixWaySlabFrameBlock.DOUBLE_SLAB) && texture_2 != null) {
                switch (state.get(SixWaySlabFrameBlock.FACING)) {
                    case UP:
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 0.5f, 1f, 0f, 1f, texture_2, tintIndex_2, renderNorth, renderSouth, renderEast, renderWest, renderUp, !sameBlocks));
                        break;
                    case DOWN:
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 0.5f, 0f, 1f, texture_2, tintIndex_2, renderNorth, renderSouth, renderEast, renderWest, !sameBlocks, renderDown));
                        break;
                    case WEST:
                        quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 0f, 1f, 0f, 1f, texture_2, tintIndex_2, renderNorth, renderSouth, !sameBlocks, renderWest, renderUp, renderDown));
                        break;
                    case SOUTH:
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0.5f, 1f, texture_2, tintIndex_2, !sameBlocks, renderSouth, renderEast, renderWest, renderUp, renderDown));
                        break;
                    case NORTH:
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0f, 0.5f, texture_2, tintIndex_2, renderNorth, !sameBlocks, renderEast, renderWest, renderUp, renderDown));
                        break;
                    case EAST:
                        quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 0f, 1f, 0f, 1f, texture_2, tintIndex_2, renderNorth, renderSouth, renderEast, !sameBlocks, renderUp, renderDown));
                        break;
                }
            }
            int overlayIndex_1 = extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_1);
            if (extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_1) != 0) {
                switch (state.get(SixWaySlabFrameBlock.FACING)) {
                    case UP:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 0.5f, 0f, 1f, overlayIndex_1, renderNorth, renderSouth, renderEast, renderWest, renderUp && !sameBlocks, renderDown, true));
                        break;
                    case DOWN:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0.5f, 1f, 0f, 1f, overlayIndex_1, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown && !sameBlocks, true));
                        break;
                    case WEST:
                        quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0f, 1f, 0f, 1f, overlayIndex_1, renderNorth && !sameBlocks, renderSouth, renderEast, renderWest, renderUp && !sameBlocks, renderDown, true));
                        break;
                    case SOUTH:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 0.5f, overlayIndex_1, renderNorth, renderSouth, renderEast && !sameBlocks, renderWest, renderUp && !sameBlocks, renderDown, true));
                        break;
                    case NORTH:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0.5f, 1f, overlayIndex_1, renderNorth, renderSouth, renderEast, renderWest && !sameBlocks, renderUp && !sameBlocks, renderDown, true));
                        break;
                    case EAST:
                        quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0f, 1f, 0f, 1f, overlayIndex_1, renderNorth, renderSouth && !sameBlocks, renderEast, renderWest, renderUp && !sameBlocks, renderDown, true));
                        break;
                }
            }
            if (state.get(SixWaySlabFrameBlock.DOUBLE_SLAB)) {
                int overlayIndex_2 = extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_2);
                if (extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_2) != 0) {
                    switch (state.get(SixWaySlabFrameBlock.FACING)) {
                        case UP:
                            quads.addAll(ModelHelper.createOverlay(0f, 1f, 0.5f, 1f, 0f, 1f, overlayIndex_2, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown && !sameBlocks, false));
                            break;
                        case DOWN:
                            quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 0.5f, 0f, 1f, overlayIndex_2, renderNorth, renderSouth, renderEast, renderWest, renderUp && !sameBlocks, renderDown, false));
                            break;
                        case WEST:
                            quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0f, 1f, 0f, 1f, overlayIndex_2, renderNorth, renderSouth && !sameBlocks, renderEast, renderWest, renderUp, renderDown, false));
                            break;
                        case SOUTH:
                            quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0.5f, 1f, overlayIndex_2, renderNorth, renderSouth, renderEast, renderWest && !sameBlocks, renderUp, renderDown, false));
                            break;
                        case NORTH:
                            quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 0.5f, overlayIndex_2, renderNorth, renderSouth, renderEast && !sameBlocks, renderWest, renderUp, renderDown, false));
                            break;
                        case EAST:
                            quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0f, 1f, 0f, 1f, overlayIndex_2, renderNorth && !sameBlocks, renderSouth, renderEast, renderWest, renderUp, renderDown, false));
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
    public TextureAtlasSprite getParticleTexture() {
        return getTexture();
    }

    @Override
    @Nonnull
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }

    @Override
    @Nonnull
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }
}
//========SOLI DEO GLORIA========//