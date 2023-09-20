package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.LayeredBlock;
import mod.pianomanu.blockcarpentry.block.SixWaySlabFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
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
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains all information for the block model
 * See {@link ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.3 09/20/23
 */
public class LayeredBlockBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, RenderType renderType) {

        //1st block in slab
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
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
    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, BakedModel model) {
        if (side == null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        int tex = extraData.get(FrameBlockTile.TEXTURE);
        if (mimic != null && state != null) {
            TextureAtlasSprite texture = QuadUtils.getTexture(model, rand, extraData, FrameBlockTile.TEXTURE);
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);

            int layers = state.getValue(LayeredBlock.LAYERS);
            float layerHeight = layers / 8f;
            boolean renderNorth = side == Direction.NORTH && extraData.get(FrameBlockTile.NORTH_VISIBLE);
            boolean renderEast = side == Direction.EAST && extraData.get(FrameBlockTile.EAST_VISIBLE);
            boolean renderSouth = side == Direction.SOUTH && extraData.get(FrameBlockTile.SOUTH_VISIBLE);
            boolean renderWest = side == Direction.WEST && extraData.get(FrameBlockTile.WEST_VISIBLE);
            boolean renderUp = side == Direction.UP && extraData.get(FrameBlockTile.UP_VISIBLE);
            boolean renderDown = side == Direction.DOWN && extraData.get(FrameBlockTile.DOWN_VISIBLE);
            List<BakedQuad> quads = new ArrayList<>();
            switch (state.getValue(SixWaySlabFrameBlock.FACING)) {
                case UP -> quads.addAll(ModelHelper.createCuboid(0f, 1f, 1f - layerHeight, 1f, 0f, 1f, texture, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown));
                case DOWN -> quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, layerHeight, 0f, 1f, texture, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown));
                case WEST -> quads.addAll(ModelHelper.createCuboid(0f, layerHeight, 0f, 1f, 0f, 1f, texture, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown));
                case SOUTH -> quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 1f - layerHeight, 1f, texture, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown));
                case NORTH -> quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0f, layerHeight, texture, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown));
                case EAST -> quads.addAll(ModelHelper.createCuboid(1f - layerHeight, 1f, 0f, 1f, 0f, 1f, texture, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown));
            }
            int overlayIndex = extraData.get(FrameBlockTile.OVERLAY);
            if (extraData.get(FrameBlockTile.OVERLAY) != 0) {
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
    public List<BakedQuad> getMimicQuadsEmpty(@Nullable BlockState state, @Nullable Direction side, @Nonnull ModelData extraData) {
        if (side == null) {
            return Collections.emptyList();
        }
        if (state != null) {
            int layers = state.getValue(LayeredBlock.LAYERS);
            boolean renderNorth = side == Direction.NORTH && extraData.get(FrameBlockTile.NORTH_VISIBLE);
            boolean renderEast = side == Direction.EAST && extraData.get(FrameBlockTile.EAST_VISIBLE);
            boolean renderSouth = side == Direction.SOUTH && extraData.get(FrameBlockTile.SOUTH_VISIBLE);
            boolean renderWest = side == Direction.WEST && extraData.get(FrameBlockTile.WEST_VISIBLE);
            boolean renderUp = side == Direction.UP && extraData.get(FrameBlockTile.UP_VISIBLE);
            boolean renderDown = side == Direction.DOWN && extraData.get(FrameBlockTile.DOWN_VISIBLE);
            TextureAtlasSprite textureUp = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/oak_planks"));
            TextureAtlasSprite textureDown = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/oak_planks"));
            TextureAtlasSprite textureNorth = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/spruce_trapdoor"));
            TextureAtlasSprite textureSouth = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/spruce_trapdoor"));
            TextureAtlasSprite textureEast = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/spruce_trapdoor"));
            TextureAtlasSprite textureWest = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/spruce_trapdoor"));
            List<BakedQuad> quads = new ArrayList<>();
            switch (state.getValue(SixWaySlabFrameBlock.FACING)) {
                case UP:
                    for (int i = 0; i < layers; i++) {
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 1f - (i + 1) / 8f, 1 - (i + 0.5f) / 8f, 0f, 1f, -1, textureNorth, textureSouth, textureEast, textureWest, textureUp, textureDown, 0));
                        quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 15 / 16f, 1f - (i + 0.5f) / 8f, 1 - i / 8f, 1 / 16f, 15 / 16f, -1, textureNorth, textureSouth, textureEast, textureWest, textureUp, textureDown, 0));
                    }
                    break;
                case DOWN:
                    for (int i = 0; i < layers; i++) {
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, (i + 0.5f) / 8f, (i + 1) / 8f, 0f, 1f, -1, textureNorth, textureSouth, textureEast, textureWest, textureUp, textureDown, 0));
                        quads.addAll(ModelHelper.createSixFaceCuboid(1 / 15f, 15 / 16f, i / 8f, (i + 0.5f) / 8f, 1 / 16f, 15 / 16f, -1, textureNorth, textureSouth, textureEast, textureWest, textureUp, textureDown, 0));
                    }
                    break;
                case WEST:
                    for (int i = 0; i < layers; i++) {
                        quads.addAll(ModelHelper.createSixFaceCuboid((i + 0.5f) / 8f, (i + 1) / 8f, 0f, 1f, 0f, 1f, -1, textureUp, textureDown, textureEast, textureWest, textureNorth, textureSouth, 0));
                        quads.addAll(ModelHelper.createSixFaceCuboid(i / 8f, (i + 0.5f) / 8f, 1 / 15f, 15 / 16f, 1 / 15f, 15 / 16f, -1, textureUp, textureDown, textureEast, textureWest, textureNorth, textureSouth, 0));
                    }
                    break;
                case SOUTH:
                    for (int i = 0; i < layers; i++) {
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 1f - (i + 1) / 8f, 1 - (i + 0.5f) / 8f, -1, textureNorth, textureSouth, textureUp, textureDown, textureEast, textureWest, 0));
                        quads.addAll(ModelHelper.createSixFaceCuboid(1 / 15f, 15 / 16f, 1 / 15f, 15 / 16f, 1f - (i + 0.5f) / 8f, 1 - i / 8f, -1, textureNorth, textureSouth, textureUp, textureDown, textureEast, textureWest, 0));
                    }
                    break;
                case NORTH:
                    for (int i = 0; i < layers; i++) {
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, (i + 0.5f) / 8f, (i + 1) / 8f, -1, textureNorth, textureSouth, textureUp, textureDown, textureEast, textureWest, 0));
                        quads.addAll(ModelHelper.createSixFaceCuboid(1 / 15f, 15 / 16f, 1 / 15f, 15 / 16f, i / 8f, (i + 0.5f) / 8f, -1, textureNorth, textureSouth, textureUp, textureDown, textureEast, textureWest, 0));
                    }
                    break;
                case EAST:
                    for (int i = 0; i < layers; i++) {
                        quads.addAll(ModelHelper.createSixFaceCuboid(1f - (i + 1) / 8f, 1f - (i + 0.5f) / 8f, 0f, 1f, 0f, 1f, -1, textureUp, textureDown, textureEast, textureWest, textureNorth, textureSouth, 0));
                        quads.addAll(ModelHelper.createSixFaceCuboid(1f - (i + 0.5f) / 8f, 1f - i / 8f, 1 / 15f, 15 / 16f, 1 / 15f, 15 / 16f, -1, textureUp, textureDown, textureEast, textureWest, textureNorth, textureSouth, 0));
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

    @Override
    @NotNull
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return ChunkRenderTypeSet.of(RenderType.translucent());
    }
}
//========SOLI DEO GLORIA========//