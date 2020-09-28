package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.block.SixWaySlabFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassBlock;
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
 * @version 1.3 09/28/20
 */
public class IllusionSlabBakedModel implements IDynamicBakedModel {
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
        if (mimic != null && state != null) {
            int tintIndex = -1;
            if (mimic.getBlock() instanceof GrassBlock) {
                tintIndex = 1;
            }
            List<BakedQuad> quads = new ArrayList<>();
            switch (state.get(SixWaySlabFrameBlock.FACING)) {
                case UP:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 0.5f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                    break;
                case DOWN:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0.5f, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                    break;
                case WEST:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 0f, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                    break;
                case SOUTH:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, 0.5f, mimic, model, extraData, rand, tintIndex));
                    break;
                case NORTH:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0.5f, 1f, mimic, model, extraData, rand, tintIndex));
                    break;
                case EAST:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 0f, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                    break;
            }
            if (extraData.getData(FrameBlockTile.OVERLAY) != 0) {
                TextureAtlasSprite overlay = null;
                TextureAtlasSprite upOverlay = null;
                TextureAtlasSprite downOverlay = null;
                if (extraData.getData(FrameBlockTile.OVERLAY) == 1) {
                    tintIndex = 1;
                    overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/grass_block_side_overlay"));
                    upOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/grass_block_top"));
                }
                if (extraData.getData(FrameBlockTile.OVERLAY) == 2) {
                    tintIndex = 1;
                    overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_side_overlay_large"));
                    upOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/grass_block_top"));
                }
                if (extraData.getData(FrameBlockTile.OVERLAY) == 3) {
                    tintIndex = -1;
                    overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_snow_overlay"));
                    upOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/snow"));
                }
                if (extraData.getData(FrameBlockTile.OVERLAY) == 4) {
                    tintIndex = -1;
                    overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_snow_overlay_small"));
                    upOverlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/snow"));
                }
                if (extraData.getData(FrameBlockTile.OVERLAY) == 5) {
                    tintIndex = 1;
                    overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/vine"));
                }
                switch (state.get(SixWaySlabFrameBlock.FACING)) {
                    case UP:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 0.5f, 0f, 1f, tintIndex, overlay, overlay, overlay, overlay, upOverlay, downOverlay));
                        break;
                    case DOWN:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0.5f, 1f, 0f, 1f, tintIndex, overlay, overlay, overlay, overlay, upOverlay, downOverlay));
                        break;
                    case WEST:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 0f, 1f, 0f, 1f, tintIndex, overlay, overlay, overlay, overlay, upOverlay, downOverlay));
                        break;
                    case SOUTH:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, 0.5f, tintIndex, overlay, overlay, overlay, overlay, upOverlay, downOverlay));
                        break;
                    case NORTH:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0.5f, 1f, tintIndex, overlay, overlay, overlay, overlay, upOverlay, downOverlay));
                        break;
                    case EAST:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 0f, 1f, 0f, 1f, tintIndex, overlay, overlay, overlay, overlay, upOverlay, downOverlay));
                        break;
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