package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.WoodButtonBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.state.properties.AttachFace;
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
 * @version 1.4 05/28/21
 */
public class IllusionButtonPoweredBakedModel implements IDynamicBakedModel {
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
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            int rotation = extraData.getData(FrameBlockTile.ROTATION);
            float yl = 0f;
            float yh = 1 / 16f;
            if (state.get(WoodButtonBlock.FACE).equals(AttachFace.CEILING)) {
                yl = 15 / 16f;
                yh = 1f;
            }
            List<BakedQuad> quads = new ArrayList<>();
            switch (state.get(WoodButtonBlock.FACE)) {
                case WALL:
                    switch (state.get(WoodButtonBlock.HORIZONTAL_FACING)) {
                        case NORTH:
                            quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 15 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                            break;
                        case EAST:
                            quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createSixFaceCuboid(0f, 1 / 16f, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                            break;
                        case WEST:
                            quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createSixFaceCuboid(15 / 16f, 1f, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                            break;
                        case SOUTH:
                            quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 0f, 1 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                            break;
                    }
                    break;
                case FLOOR:
                case CEILING:
                    switch (state.get(WoodButtonBlock.HORIZONTAL_FACING)) {
                        case EAST:
                        case WEST:
                            quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createSixFaceCuboid(6 / 16f, 10 / 16f, yl, yh, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                            break;
                        case SOUTH:
                        case NORTH:
                            quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, yl, yh, 6 / 16f, 10 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                            break;
                    }
            }
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                switch (state.get(WoodButtonBlock.FACE)) {
                    case WALL:
                        switch (state.get(WoodButtonBlock.HORIZONTAL_FACING)) {
                            case NORTH:
                                quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlay(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 15 / 16f, 1f, overlayIndex));
                                break;
                            case EAST:
                                quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlay(0f, 1 / 16f, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, overlayIndex));
                                break;
                            case WEST:
                                quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlay(15 / 16f, 1f, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, overlayIndex));
                                break;
                            case SOUTH:
                                quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlay(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 0f, 1 / 16f, overlayIndex));
                                break;
                        }
                        break;
                    case FLOOR:
                    case CEILING:
                        switch (state.get(WoodButtonBlock.HORIZONTAL_FACING)) {
                            case EAST:
                            case WEST:
                                quads.addAll(ModelHelper.createOverlay(6 / 16f, 10 / 16f, yl, yh, 5 / 16f, 11 / 16f, overlayIndex));
                                break;
                            case SOUTH:
                            case NORTH:
                                quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, yl, yh, 6 / 16f, 10 / 16f, overlayIndex));
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