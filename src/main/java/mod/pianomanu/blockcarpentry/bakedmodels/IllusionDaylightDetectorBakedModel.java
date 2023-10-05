package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.DaylightDetectorFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.DaylightDetectorFrameTileEntity;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.block.BlockState;
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
 * @version 1.3 09/21/23
 */
public class IllusionDaylightDetectorBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        if (side != null) {
            return Collections.emptyList();
        }

        BlockState mimic = extraData.getData(DaylightDetectorFrameTileEntity.MIMIC);
        Integer design = extraData.getData(DaylightDetectorFrameTileEntity.DESIGN);
        if (mimic != null && state != null) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            int rotation = extraData.getData(DaylightDetectorFrameTileEntity.ROTATION);

            TextureAtlasSprite sensor;
            TextureAtlasSprite sensor_side = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/daylight_detector_side"));
            TextureAtlasSprite glass = TextureHelper.getGlassTextures().get(extraData.getData(DaylightDetectorFrameTileEntity.GLASS_COLOR));
            if (state.get(DaylightDetectorFrameBlock.INVERTED)) {
                sensor = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/daylight_detector_inverted_top"));
            } else {
                sensor = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/daylight_detector_top"));
            }
            List<BakedQuad> quads = new ArrayList<>();
            if (design == 0) {
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 6 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
            } else if (design == 1) {
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));

                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 1 / 16f, 6 / 16f, 0f, 1 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(15 / 16f, 1f, 1 / 16f, 6 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 1 / 16f, 6 / 16f, 15 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1 / 16f, 1 / 16f, 6 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));

                quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 15 / 16f, 1 / 16f, 6 / 16f, 1 / 16f, 15 / 16f, -1, sensor_side, sensor_side, sensor_side, sensor_side, sensor, sensor_side, 0));
            } else if (design == 2) {
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 6 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 15 / 16f, 6 / 16f, 7 / 16f, 1 / 16f, 15 / 16f, -1, sensor_side, sensor_side, sensor_side, sensor_side, sensor, sensor_side, 0));
            } else if (design == 3) {
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));

                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 1 / 16f, 6 / 16f, 0f, 1 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(15 / 16f, 1f, 1 / 16f, 6 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 1 / 16f, 6 / 16f, 15 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1 / 16f, 1 / 16f, 6 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));

                quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 15 / 16f, 1 / 16f, 4 / 16f, 1 / 16f, 15 / 16f, -1, sensor_side, sensor_side, sensor_side, sensor_side, sensor, sensor_side, 0));

                quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 4 / 16f, 6 / 16f, 1 / 16f, 15 / 16f, glass, -1));
            } else if (design == 4) {
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));

                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 1 / 16f, 6 / 16f, 0f, 1 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(15 / 16f, 1f, 1 / 16f, 6 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 1 / 16f, 6 / 16f, 15 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1 / 16f, 1 / 16f, 6 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));

                quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 15 / 16f, 1 / 16f, 5 / 16f, 1 / 16f, 15 / 16f, -1, sensor_side, sensor_side, sensor_side, sensor_side, sensor, sensor_side, 0));

                quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 6 / 16f, 5 / 16f, 6 / 16f, 1 / 16f, 15 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(10 / 16f, 11 / 16f, 5 / 16f, 6 / 16f, 1 / 16f, 15 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 15 / 16f, 5 / 16f, 6 / 16f, 5 / 16f, 6 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 15 / 16f, 5 / 16f, 6 / 16f, 10 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
            }
            int overlayIndex = extraData.getData(DaylightDetectorFrameTileEntity.OVERLAY);
            if (overlayIndex != 0) {
                quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 6 / 16f, 0f, 1f, overlayIndex, true, true, true, true, true, true, true));
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
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }
}
//========SOLI DEO GLORIA========//