package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FenceFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains all information for the block model
 * See {@link mod.pianomanu.blockcarpentry.util.ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.1 06/11/22
 */
public class IllusionFenceBakedModel implements IDynamicBakedModel {
    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getIllusionQuads(state, side, rand, extraData, model);
        }
        return Collections.emptyList();
    }

    private List<BakedQuad> getIllusionQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull IModelData extraData, BakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        Integer design = extraData.getData(FrameBlockTile.DESIGN);
        if (mimic != null && state != null) {
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            int rotation = extraData.getData(FrameBlockTile.ROTATION);
            List<BakedQuad> quads = new ArrayList<>(ModelHelper.createSixFaceCuboid(6 / 16f, 10 / 16f, 0f, 1f, 6 / 16f, 10 / 16f, mimic, model, extraData, rand, tintIndex, rotation));

            if (design == 0) {
                if (state.getValue(FenceFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 6 / 16f, 9 / 16f, 0f, 6 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 12 / 16f, 15 / 16f, 0f, 6 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.getValue(FenceFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 6 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 12 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.getValue(FenceFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 6 / 16f, 9 / 16f, 10 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 12 / 16f, 15 / 16f, 10 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.getValue(FenceFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 6 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 12 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
            }
            if (design == 1) {
                if (state.getValue(FenceFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 3 / 16f, 6 / 16f, 0f, 6 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 9 / 16f, 12 / 16f, 0f, 6 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.getValue(FenceFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 3 / 16f, 6 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 9 / 16f, 12 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.getValue(FenceFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 3 / 16f, 6 / 16f, 10 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 9 / 16f, 12 / 16f, 10 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.getValue(FenceFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 3 / 16f, 6 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 9 / 16f, 12 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
            }
            if (design == 2) {
                if (state.getValue(FenceFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 4 / 16f, 11 / 16f, 0f, 6 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    //quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 9 / 16f, 12 / 16f, 0f, 6 / 16f, mimic,model,extraData,rand, tintIndex, rotation));
                }
                if (state.getValue(FenceFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 4 / 16f, 11 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    //quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 9 / 16f, 12 / 16f, 7 / 16f, 9 / 16f, mimic,model,extraData,rand, tintIndex, rotation));
                }
                if (state.getValue(FenceFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 4 / 16f, 11 / 16f, 10 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    //quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 9 / 16f, 12 / 16f, 10 / 16f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                }
                if (state.getValue(FenceFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 4 / 16f, 11 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 9 / 16f, 12 / 16f, 7 / 16f, 9 / 16f, mimic,model,extraData,rand, tintIndex, rotation));
                }
            }
            if (design == 3) {
                if (state.getValue(FenceFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 6 / 16f, 9 / 16f, 0f, 6 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 12 / 16f, 15 / 16f, 0f, 6 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(6 / 16f, 10 / 16f, 0f, 1f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.getValue(FenceFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 6 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 1f, 12 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 16 / 16f, 0f, 1f, 6 / 16f, 10 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.getValue(FenceFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 6 / 16f, 9 / 16f, 10 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 12 / 16f, 15 / 16f, 10 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(6 / 16f, 10 / 16f, 0f, 1f, 14 / 16f, 16 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.getValue(FenceFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 6 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 6 / 16f, 12 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 0f, 1f, 6 / 16f, 10 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
            }
            if (design == 4) {
                if (state.getValue(FenceFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, 2 / 16f, 6 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 13 / 16f, 15 / 16f, 2 / 16f, 6 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 6 / 16f, 8 / 16f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 12 / 16f, 14 / 16f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.getValue(FenceFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 13 / 16f, 7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 13 / 16f, 13 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 6 / 16f, 8 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 12 / 16f, 14 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.getValue(FenceFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, 10 / 16f, 14 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 13 / 16f, 15 / 16f, 10 / 16f, 14 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 6 / 16f, 8 / 16f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 12 / 16f, 14 / 16f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.getValue(FenceFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(3 / 16f, 6 / 16f, 7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(3 / 16f, 6 / 16f, 13 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 6 / 16f, 8 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 12 / 16f, 14 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
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
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    @Nonnull
    public TextureAtlasSprite getParticleIcon() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/oak_planks"));
    }

    @Override
    @Nonnull
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}
//========SOLI DEO GLORIA========//