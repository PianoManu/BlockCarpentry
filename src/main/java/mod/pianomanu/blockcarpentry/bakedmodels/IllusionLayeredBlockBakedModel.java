package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.LayeredBlock;
import mod.pianomanu.blockcarpentry.block.SixWaySlabFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
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
import net.minecraft.world.inventory.InventoryMenu;
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
 * See {@link ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.3 09/20/23
 */
public class IllusionLayeredBlockBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        //1st block in slab
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (state != null) {
            if (state.getValue(BCBlockStateProperties.CONTAINS_BLOCK) && mimic != null) {
                ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
                BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                return getMimicQuads(state, side, rand, extraData, model);
            } else {
                return getMimicQuadsEmpty(state, side, extraData);
            }
        }

        return Collections.emptyList();
    }

    //supresses "Unboxing of "extraData..." may produce NullPointerException
    @SuppressWarnings("all")
    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, BakedModel model) {
        if (side == null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && state != null) {
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);

            int layers = state.getValue(LayeredBlock.LAYERS);
            float layerHeight = layers / 8f;
            boolean renderNorth = side == Direction.NORTH && extraData.getData(FrameBlockTile.NORTH_VISIBLE);
            boolean renderEast = side == Direction.EAST && extraData.getData(FrameBlockTile.EAST_VISIBLE);
            boolean renderSouth = side == Direction.SOUTH && extraData.getData(FrameBlockTile.SOUTH_VISIBLE);
            boolean renderWest = side == Direction.WEST && extraData.getData(FrameBlockTile.WEST_VISIBLE);
            boolean renderUp = side == Direction.UP && extraData.getData(FrameBlockTile.UP_VISIBLE);
            boolean renderDown = side == Direction.DOWN && extraData.getData(FrameBlockTile.DOWN_VISIBLE);
            int rotation = extraData.getData(FrameBlockTile.ROTATION);

            List<BakedQuad> quads = new ArrayList<>();
            switch (state.getValue(SixWaySlabFrameBlock.FACING)) {
                case UP -> quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 1f - layerHeight, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, rotation));
                case DOWN -> quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, layerHeight, 0f, 1f, mimic, model, extraData, rand, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, rotation));
                case WEST -> quads.addAll(ModelHelper.createSixFaceCuboid(0f, layerHeight, 0f, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, rotation));
                case SOUTH -> quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 1f - layerHeight, 1f, mimic, model, extraData, rand, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, rotation));
                case NORTH -> quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, layerHeight, mimic, model, extraData, rand, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, rotation));
                case EAST -> quads.addAll(ModelHelper.createSixFaceCuboid(1f - layerHeight, 1f, 0f, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, rotation));
            }
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (extraData.getData(FrameBlockTile.OVERLAY) != 0) {
                switch (state.getValue(SixWaySlabFrameBlock.FACING)) {
                    case UP -> quads.addAll(ModelHelper.createOverlay(0f, 1f, 1f - layerHeight, 1f, 0f, 1f, overlayIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, true));
                    case DOWN -> quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, layerHeight, 0f, 1f, overlayIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, true));
                    case WEST -> quads.addAll(ModelHelper.createOverlay(0f, layerHeight, 0f, 1f, 0f, 1f, overlayIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, true));
                    case SOUTH -> quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 1f - layerHeight, 1f, overlayIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, true));
                    case NORTH -> quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, layerHeight, overlayIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, true));
                    case EAST -> quads.addAll(ModelHelper.createOverlay(1f - layerHeight, 1f, 0f, 1f, 0f, 1f, overlayIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, true));
                }
            }
            return quads;
        }
        return Collections.emptyList();
    }

    //supresses "Unboxing of "extraData..." may produce NullPointerException
    @SuppressWarnings("all")
    public List<BakedQuad> getMimicQuadsEmpty(@Nullable BlockState state, @Nullable Direction side, @Nonnull IModelData extraData) {
        if (side == null) {
            return Collections.emptyList();
        }
        if (state != null) {
            int layers = state.getValue(LayeredBlock.LAYERS);
            boolean renderNorth = side == Direction.NORTH && extraData.getData(FrameBlockTile.NORTH_VISIBLE);
            boolean renderEast = side == Direction.EAST && extraData.getData(FrameBlockTile.EAST_VISIBLE);
            boolean renderSouth = side == Direction.SOUTH && extraData.getData(FrameBlockTile.SOUTH_VISIBLE);
            boolean renderWest = side == Direction.WEST && extraData.getData(FrameBlockTile.WEST_VISIBLE);
            boolean renderUp = side == Direction.UP && extraData.getData(FrameBlockTile.UP_VISIBLE);
            boolean renderDown = side == Direction.DOWN && extraData.getData(FrameBlockTile.DOWN_VISIBLE);
            TextureAtlasSprite textureUp = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/oak_planks"));
            TextureAtlasSprite textureDown = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/oak_planks"));
            TextureAtlasSprite textureNorth = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/spruce_trapdoor"));
            TextureAtlasSprite textureSouth = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/spruce_trapdoor"));
            TextureAtlasSprite textureEast = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/spruce_trapdoor"));
            TextureAtlasSprite textureWest = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/spruce_trapdoor"));
            TextureAtlasSprite slime = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/slime_block"));
            List<BakedQuad> quads = new ArrayList<>();
            switch (state.getValue(SixWaySlabFrameBlock.FACING)) {
                case UP:
                    for (int i = 0; i < layers; i++) {
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 1f - (i + 1) / 8f, 1 - (i + 0.5f) / 8f, 0f, 1f, -1, textureNorth, textureSouth, textureEast, textureWest, textureUp, textureDown, 0));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1f - (i + 0.5f) / 8f, 1 - i / 8f, 1 / 16f, 15 / 16f, slime, -1));
                    }
                    break;
                case DOWN:
                    for (int i = 0; i < layers; i++) {
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, (i + 0.5f) / 8f, (i + 1) / 8f, 0f, 1f, -1, textureNorth, textureSouth, textureEast, textureWest, textureUp, textureDown, 0));
                        quads.addAll(ModelHelper.createCuboid(1 / 15f, 15 / 16f, i / 8f, (i + 0.5f) / 8f, 1 / 16f, 15 / 16f, slime, -1));
                    }
                    break;
                case WEST:
                    for (int i = 0; i < layers; i++) {
                        quads.addAll(ModelHelper.createSixFaceCuboid((i + 0.5f) / 8f, (i + 1) / 8f, 0f, 1f, 0f, 1f, -1, textureUp, textureDown, textureEast, textureWest, textureNorth, textureSouth, 0));
                        quads.addAll(ModelHelper.createCuboid(i / 8f, (i + 0.5f) / 8f, 1 / 15f, 15 / 16f, 1 / 15f, 15 / 16f, slime, -1));
                    }
                    break;
                case SOUTH:
                    for (int i = 0; i < layers; i++) {
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 1f - (i + 1) / 8f, 1 - (i + 0.5f) / 8f, -1, textureNorth, textureSouth, textureUp, textureDown, textureEast, textureWest, 0));
                        quads.addAll(ModelHelper.createCuboid(1 / 15f, 15 / 16f, 1 / 15f, 15 / 16f, 1f - (i + 0.5f) / 8f, 1 - i / 8f, slime, -1));
                    }
                    break;
                case NORTH:
                    for (int i = 0; i < layers; i++) {
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, (i + 0.5f) / 8f, (i + 1) / 8f, -1, textureNorth, textureSouth, textureUp, textureDown, textureEast, textureWest, 0));
                        quads.addAll(ModelHelper.createCuboid(1 / 15f, 15 / 16f, 1 / 15f, 15 / 16f, i / 8f, (i + 0.5f) / 8f, slime, -1));
                    }
                    break;
                case EAST:
                    for (int i = 0; i < layers; i++) {
                        quads.addAll(ModelHelper.createSixFaceCuboid(1f - (i + 1) / 8f, 1f - (i + 0.5f) / 8f, 0f, 1f, 0f, 1f, -1, textureUp, textureDown, textureEast, textureWest, textureNorth, textureSouth, 0));
                        quads.addAll(ModelHelper.createCuboid(1f - (i + 0.5f) / 8f, 1f - i / 8f, 1 / 15f, 15 / 16f, 1 / 15f, 15 / 16f, slime, -1));
                    }
                    break;
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