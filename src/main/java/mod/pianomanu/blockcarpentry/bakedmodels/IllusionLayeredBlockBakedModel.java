package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.block.LayeredBlock;
import mod.pianomanu.blockcarpentry.block.SixWaySlabFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
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
 * See {@link ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.0 02/07/22
 */
@SuppressWarnings("deprecation")
public class IllusionLayeredBlockBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {

        //1st block in slab
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
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
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        int tex = extraData.getData(FrameBlockTile.TEXTURE);
        if (mimic != null && state != null) {
            List<TextureAtlasSprite> textureList = TextureHelper.getTextureFromModel(model, extraData, rand);
            TextureAtlasSprite texture;
            if (textureList.size() <= tex) {
                extraData.setData(FrameBlockTile.TEXTURE, 0);
                tex = 0;
            }
            if (textureList.size() == 0) {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.block_not_available"), true);
                }
                return Collections.emptyList();
            }
            texture = textureList.get(tex);
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
                case UP:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 1f - layerHeight, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, rotation));
                    break;
                case DOWN:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, layerHeight, 0f, 1f, mimic, model, extraData, rand, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, rotation));
                    break;
                case WEST:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, layerHeight, 0f, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, rotation));
                    break;
                case SOUTH:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 1f - layerHeight, 1f, mimic, model, extraData, rand, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, rotation));
                    break;
                case NORTH:
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, layerHeight, mimic, model, extraData, rand, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, rotation));
                    break;
                case EAST:
                    quads.addAll(ModelHelper.createSixFaceCuboid(1f - layerHeight, 1f, 0f, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, rotation));
                    break;
            }
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (extraData.getData(FrameBlockTile.OVERLAY) != 0) {
                switch (state.getValue(SixWaySlabFrameBlock.FACING)) {
                    case UP:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 1f - layerHeight, 1f, 0f, 1f, overlayIndex, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, true));
                        break;
                    case DOWN:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, layerHeight, 0f, 1f, overlayIndex, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, true));
                        break;
                    case WEST:
                        quads.addAll(ModelHelper.createOverlay(0f, layerHeight, 0f, 1f, 0f, 1f, overlayIndex, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, true));
                        break;
                    case SOUTH:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 1f - layerHeight, 1f, overlayIndex, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, true));
                        break;
                    case NORTH:
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, layerHeight, overlayIndex, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, true));
                        break;
                    case EAST:
                        quads.addAll(ModelHelper.createOverlay(1f - layerHeight, 1f, 0f, 1f, 0f, 1f, overlayIndex, renderWest, renderEast, renderSouth, renderNorth, renderUp, renderDown, true));
                        break;
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