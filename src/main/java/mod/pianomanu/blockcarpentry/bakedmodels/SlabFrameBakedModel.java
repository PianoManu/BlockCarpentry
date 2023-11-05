package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.SixWaySlabFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.TwoBlocksFrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
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
 * @version 1.4 09/23/23
 */
public class SlabFrameBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {

        //1st block in slab
        BlockState mimic = extraData.getData(TwoBlocksFrameBlockTile.MIMIC_1);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getMimicQuads(state, side, rand, extraData, model);
        }

        return Collections.emptyList();
    }

    //supresses "Unboxing of "extraData..." may produce NullPointerException
    //@SuppressWarnings("all")
    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, BakedModel model) {
        if (side == null) {
            return Collections.emptyList();
        }
        BlockState mimic_1 = extraData.getData(TwoBlocksFrameBlockTile.MIMIC_1);
        BlockState mimic_2 = extraData.getData(TwoBlocksFrameBlockTile.MIMIC_2);
        boolean sameBlocks;
        if (mimic_1 != null && mimic_2 != null && mimic_2 != Blocks.AIR.defaultBlockState())
            sameBlocks = mimic_1.is(mimic_2.getBlock());
        else
            sameBlocks = false; //no second block in slab: not the same, we can render the face between the two slabs - prevents crash, if only one slab is filled
        if (mimic_1 != null && state != null) {
            TextureAtlasSprite texture_1 = TextureHelper.getTexture(model, rand, extraData, TwoBlocksFrameBlockTile.TEXTURE_1);
            TextureAtlasSprite texture_2 = null;

            if (mimic_2 != null && mimic_2 != Blocks.AIR.defaultBlockState()) {
                ModelResourceLocation location_2 = BlockModelShaper.stateToModelLocation(mimic_2);
                BakedModel model_2 = Minecraft.getInstance().getModelManager().getModel(location_2);
                texture_2 = TextureHelper.getTexture(model_2, rand, extraData, TwoBlocksFrameBlockTile.TEXTURE_2);
            }
            int tintIndex_1 = BlockAppearanceHelper.setTintIndex(mimic_1);
            int tintIndex_2 = mimic_2 == null ? -1 : BlockAppearanceHelper.setTintIndex(mimic_2);
            boolean renderNorth = side == Direction.NORTH && extraData.getData(TwoBlocksFrameBlockTile.NORTH_VISIBLE);
            boolean renderEast = side == Direction.EAST && extraData.getData(TwoBlocksFrameBlockTile.EAST_VISIBLE);
            boolean renderSouth = side == Direction.SOUTH && extraData.getData(TwoBlocksFrameBlockTile.SOUTH_VISIBLE);
            boolean renderWest = side == Direction.WEST && extraData.getData(TwoBlocksFrameBlockTile.WEST_VISIBLE);
            boolean renderUp = side == Direction.UP && extraData.getData(TwoBlocksFrameBlockTile.UP_VISIBLE);
            boolean renderDown = side == Direction.DOWN && extraData.getData(TwoBlocksFrameBlockTile.DOWN_VISIBLE);
            List<BakedQuad> quads = new ArrayList<>();
            switch (state.getValue(SixWaySlabFrameBlock.FACING)) {
                case UP -> quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 0.5f, 0f, 1f, texture_1, tintIndex_1, renderNorth, renderSouth, renderEast, renderWest, renderUp && !sameBlocks, renderDown));
                case DOWN -> quads.addAll(ModelHelper.createCuboid(0f, 1f, 0.5f, 1f, 0f, 1f, texture_1, tintIndex_1, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown && !sameBlocks));
                case WEST -> quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 0f, 1f, 0f, 1f, texture_1, tintIndex_1, renderNorth, renderSouth, renderEast, renderWest && !sameBlocks, renderUp, renderDown));
                case SOUTH -> quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0f, 0.5f, texture_1, tintIndex_1, renderNorth, renderSouth && !sameBlocks, renderEast, renderWest, renderUp, renderDown));
                case NORTH -> quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0.5f, 1f, texture_1, tintIndex_1, renderNorth && !sameBlocks, renderSouth, renderEast, renderWest, renderUp, renderDown));
                case EAST -> quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 0f, 1f, 0f, 1f, texture_1, tintIndex_1, renderNorth, renderSouth, renderEast && !sameBlocks, renderWest, renderUp, renderDown));
            }
            if (state.getValue(SixWaySlabFrameBlock.DOUBLE_SLAB) && texture_2 != null) {
                switch (state.getValue(SixWaySlabFrameBlock.FACING)) {
                    case UP -> quads.addAll(ModelHelper.createCuboid(0f, 1f, 0.5f, 1f, 0f, 1f, texture_2, tintIndex_2, renderNorth, renderSouth, renderEast, renderWest, renderUp, !sameBlocks));
                    case DOWN -> quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 0.5f, 0f, 1f, texture_2, tintIndex_2, renderNorth, renderSouth, renderEast, renderWest, !sameBlocks, renderDown));
                    case WEST -> quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 0f, 1f, 0f, 1f, texture_2, tintIndex_2, renderNorth, renderSouth, !sameBlocks, renderWest, renderUp, renderDown));
                    case SOUTH -> quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0.5f, 1f, texture_2, tintIndex_2, !sameBlocks, renderSouth, renderEast, renderWest, renderUp, renderDown));
                    case NORTH -> quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0f, 0.5f, texture_2, tintIndex_2, renderNorth, !sameBlocks, renderEast, renderWest, renderUp, renderDown));
                    case EAST -> quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 0f, 1f, 0f, 1f, texture_2, tintIndex_2, renderNorth, renderSouth, renderEast, !sameBlocks, renderUp, renderDown));
                }
            }
            int overlayIndex_1 = extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_1);
            if (extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_1) != 0) {
                switch (state.getValue(SixWaySlabFrameBlock.FACING)) {
                    case UP -> quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 0.5f, 0f, 1f, overlayIndex_1, renderNorth, renderSouth, renderEast, renderWest, renderUp && !sameBlocks, renderDown, true));
                    case DOWN -> quads.addAll(ModelHelper.createOverlay(0f, 1f, 0.5f, 1f, 0f, 1f, overlayIndex_1, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown && !sameBlocks, true));
                    case WEST -> quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0f, 1f, 0f, 1f, overlayIndex_1, renderNorth && !sameBlocks, renderSouth, renderEast, renderWest, renderUp && !sameBlocks, renderDown, true));
                    case SOUTH -> quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 0.5f, overlayIndex_1, renderNorth, renderSouth, renderEast && !sameBlocks, renderWest, renderUp && !sameBlocks, renderDown, true));
                    case NORTH -> quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0.5f, 1f, overlayIndex_1, renderNorth, renderSouth, renderEast, renderWest && !sameBlocks, renderUp && !sameBlocks, renderDown, true));
                    case EAST -> quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0f, 1f, 0f, 1f, overlayIndex_1, renderNorth, renderSouth && !sameBlocks, renderEast, renderWest, renderUp && !sameBlocks, renderDown, true));
                }
            }
            if (state.getValue(SixWaySlabFrameBlock.DOUBLE_SLAB)) {
                int overlayIndex_2 = extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_2);
                if (extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_2) != 0) {
                    switch (state.getValue(SixWaySlabFrameBlock.FACING)) {
                        case UP -> quads.addAll(ModelHelper.createOverlay(0f, 1f, 0.5f, 1f, 0f, 1f, overlayIndex_2, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown && !sameBlocks, false));
                        case DOWN -> quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 0.5f, 0f, 1f, overlayIndex_2, renderNorth, renderSouth, renderEast, renderWest, renderUp && !sameBlocks, renderDown, false));
                        case WEST -> quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0f, 1f, 0f, 1f, overlayIndex_2, renderNorth, renderSouth && !sameBlocks, renderEast, renderWest, renderUp, renderDown, false));
                        case SOUTH -> quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0.5f, 1f, overlayIndex_2, renderNorth, renderSouth, renderEast, renderWest && !sameBlocks, renderUp, renderDown, false));
                        case NORTH -> quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 0.5f, overlayIndex_2, renderNorth, renderSouth, renderEast && !sameBlocks, renderWest, renderUp, renderDown, false));
                        case EAST -> quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0f, 1f, 0f, 1f, overlayIndex_2, renderNorth && !sameBlocks, renderSouth, renderEast, renderWest, renderUp, renderDown, false));
                    }
                }
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
        return getTexture();
    }

    @Override
    @Nonnull
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}
//========SOLI DEO GLORIA========//