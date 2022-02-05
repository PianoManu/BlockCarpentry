package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.block.SixWaySlabFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.TwoBlocksFrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
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
 * @version 1.8 08/13/21
 */
@SuppressWarnings("deprecation")
public class SlabFrameBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {

        //1st block in slab
        BlockState mimic = extraData.getData(TwoBlocksFrameBlockTile.MIMIC_1);
        if (mimic != null && !(mimic.getBlock() instanceof FrameBlock)) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            //model.getBakedModel().getQuads(mimic, side, rand, extraData);
            return getMimicQuads(state, side, rand, extraData, model);
        }

        return Collections.emptyList();
    }

    //supresses "Unboxing of "extraData..." may produce NullPointerException
    @SuppressWarnings("all")
    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, BakedModel model) {
        if (side == null) {
            return Collections.emptyList();
        }
        BlockState mimic_1 = extraData.getData(TwoBlocksFrameBlockTile.MIMIC_1);
        BlockState mimic_2 = extraData.getData(TwoBlocksFrameBlockTile.MIMIC_2);
        int tex_1 = extraData.getData(TwoBlocksFrameBlockTile.TEXTURE_1);
        int tex_2 = extraData.getData(TwoBlocksFrameBlockTile.TEXTURE_2);
        if (mimic_1 != null && state != null) {
            List<TextureAtlasSprite> textureList_1 = TextureHelper.getTextureFromModel(model, extraData, rand);
            List<TextureAtlasSprite> textureList_2 = new ArrayList<>();

            if (mimic_2 != null) {
                ModelResourceLocation location_2 = BlockModelShaper.stateToModelLocation(mimic_2);
                BakedModel model_2 = Minecraft.getInstance().getModelManager().getModel(location_2);
                textureList_2 = TextureHelper.getTextureFromModel(model_2, extraData, rand);
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
                    Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.block_not_available"), true);
                }
                return Collections.emptyList();
            }
            texture_1 = textureList_1.get(tex_1);
            if (textureList_2.size() > 0)
                texture_2 = textureList_2.get(tex_2);
            else texture_2 = null;
            int tintIndex_1 = BlockAppearanceHelper.setTintIndex(mimic_1);
            int tintIndex_2 = mimic_2 == null ? -1 : BlockAppearanceHelper.setTintIndex(mimic_2);
            boolean isDouble = state.getValue(SixWaySlabFrameBlock.DOUBLE_SLAB);
            boolean renderNorth = side == Direction.NORTH && extraData.getData(TwoBlocksFrameBlockTile.NORTH_VISIBLE);
            boolean renderEast = side == Direction.EAST && extraData.getData(TwoBlocksFrameBlockTile.EAST_VISIBLE);
            boolean renderSouth = side == Direction.SOUTH && extraData.getData(TwoBlocksFrameBlockTile.SOUTH_VISIBLE);
            boolean renderWest = side == Direction.WEST && extraData.getData(TwoBlocksFrameBlockTile.WEST_VISIBLE);
            boolean renderUp = side == Direction.UP && extraData.getData(TwoBlocksFrameBlockTile.UP_VISIBLE);
            boolean renderDown = side == Direction.DOWN && extraData.getData(TwoBlocksFrameBlockTile.DOWN_VISIBLE);
            List<BakedQuad> quads = new ArrayList<>();
            switch (state.getValue(SixWaySlabFrameBlock.FACING)) {
                case UP:
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 0.5f, 0f, 1f, texture_1, tintIndex_1, renderNorth, renderSouth, renderEast, renderWest, renderUp && !isDouble, renderDown));
                    break;
                case DOWN:
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0.5f, 1f, 0f, 1f, texture_1, tintIndex_1, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown && !isDouble));
                    break;
                case WEST:
                    quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 0f, 1f, 0f, 1f, texture_1, tintIndex_1, renderNorth, renderSouth, renderEast, renderWest && !isDouble, renderUp, renderDown));
                    break;
                case SOUTH:
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0f, 0.5f, texture_1, tintIndex_1, renderNorth, renderSouth && !isDouble, renderEast, renderWest, renderUp, renderDown));
                    break;
                case NORTH:
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0.5f, 1f, texture_1, tintIndex_1, renderNorth && !isDouble, renderSouth, renderEast, renderWest, renderUp, renderDown));
                    break;
                case EAST:
                    quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 0f, 1f, 0f, 1f, texture_1, tintIndex_1, renderNorth, renderSouth, renderEast && !isDouble, renderWest, renderUp, renderDown));
                    break;
            }
            if (state.getValue(SixWaySlabFrameBlock.DOUBLE_SLAB) && texture_2 != null) {
                switch (state.getValue(SixWaySlabFrameBlock.FACING)) {
                    case UP:
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 0.5f, 1f, 0f, 1f, texture_2, tintIndex_2, renderNorth, renderSouth, renderEast, renderWest, renderUp, false));
                        break;
                    case DOWN:
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 0.5f, 0f, 1f, texture_2, tintIndex_2, renderNorth, renderSouth, renderEast, renderWest, false, renderDown));
                        break;
                    case WEST:
                        quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 0f, 1f, 0f, 1f, texture_2, tintIndex_2, renderNorth, renderSouth, false, renderWest, renderUp, renderDown));
                        break;
                    case SOUTH:
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0.5f, 1f, texture_2, tintIndex_2, false, renderSouth, renderEast, renderWest, renderUp, renderDown));
                        break;
                    case NORTH:
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0f, 0.5f, texture_2, tintIndex_2, renderNorth, false, renderEast, renderWest, renderUp, renderDown));
                        break;
                    case EAST:
                        quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 0f, 1f, 0f, 1f, texture_2, tintIndex_2, renderNorth, renderSouth, renderEast, false, renderUp, renderDown));
                        break;
                }
            }
            int overlayIndex_1 = extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_1);
            if (extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_1) != 0) {
                switch (state.getValue(SixWaySlabFrameBlock.FACING)) {
                    case UP:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 0.5f, 0f, 1f, overlayIndex_1, renderWest, renderEast, renderSouth, renderNorth, renderUp && !isDouble, renderDown, true));
                        break;
                    case DOWN:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0.5f, 1f, 0f, 1f, overlayIndex_1, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown && !isDouble, true));
                        break;
                    case WEST:
                        quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0f, 1f, 0f, 1f, overlayIndex_1, renderWest && !isDouble, renderEast, renderSouth, renderNorth, renderUp && !isDouble, renderDown, true));
                        break;
                    case SOUTH:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 0.5f, overlayIndex_1, renderWest, renderEast, renderSouth && !isDouble, renderNorth, renderUp && !isDouble, renderDown, true));
                        break;
                    case NORTH:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0.5f, 1f, overlayIndex_1, renderWest, renderEast, renderSouth, renderNorth && !isDouble, renderUp && !isDouble, renderDown, true));
                        break;
                    case EAST:
                        quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0f, 1f, 0f, 1f, overlayIndex_1, renderWest, renderEast && !isDouble, renderSouth, renderNorth, renderUp && !isDouble, renderDown, true));
                        break;
                }
            }
            if (state.getValue(SixWaySlabFrameBlock.DOUBLE_SLAB)) {
                int overlayIndex_2 = extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_2);
                if (extraData.getData(TwoBlocksFrameBlockTile.OVERLAY_2) != 0) {
                    switch (state.getValue(SixWaySlabFrameBlock.FACING)) {
                        case UP:
                            quads.addAll(ModelHelper.createOverlay(0f, 1f, 0.5f, 1f, 0f, 1f, overlayIndex_2, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown && !isDouble, true));
                            break;
                        case DOWN:
                            quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 0.5f, 0f, 1f, overlayIndex_2, renderWest, renderEast, renderSouth, renderNorth, renderUp && !isDouble, renderDown, true));
                            break;
                        case WEST:
                            quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0f, 1f, 0f, 1f, overlayIndex_2, renderWest, renderEast && !isDouble, renderSouth, renderNorth, renderUp, renderDown, true));
                            break;
                        case SOUTH:
                            quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0.5f, 1f, overlayIndex_2, renderWest, renderEast, renderSouth, renderNorth && !isDouble, renderUp, renderDown, true));
                            break;
                        case NORTH:
                            quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 0.5f, overlayIndex_2, renderWest, renderEast, renderSouth && !isDouble, renderNorth, renderUp, renderDown, true));
                            break;
                        case EAST:
                            quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0f, 1f, 0f, 1f, overlayIndex_2, renderWest && !isDouble, renderEast, renderSouth, renderNorth, renderUp, renderDown, true));
                            break;
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