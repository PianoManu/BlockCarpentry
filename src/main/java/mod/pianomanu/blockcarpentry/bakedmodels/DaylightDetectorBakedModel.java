package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.DaylightDetectorFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.DaylightDetectorFrameTileEntity;
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
 * @version 1.4 09/21/23
 */
public class DaylightDetectorBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {

        BlockState mimic = extraData.getData(DaylightDetectorFrameTileEntity.MIMIC);
        Integer design = extraData.getData(DaylightDetectorFrameTileEntity.DESIGN);
        if (side != null) {
            return Collections.emptyList();
        }
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            if (state != null) {
                BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                TextureAtlasSprite texture = QuadUtils.getTexture(model, rand, extraData, DaylightDetectorFrameTileEntity.TEXTURE);
                TextureAtlasSprite sensor;
                TextureAtlasSprite sensor_side = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/daylight_detector_side"));
                TextureAtlasSprite glass = TextureHelper.getGlassTextures().get(extraData.getData(DaylightDetectorFrameTileEntity.GLASS_COLOR));
                if (state.getValue(DaylightDetectorFrameBlock.INVERTED)) {
                    sensor = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/daylight_detector_inverted_top"));
                } else {
                    sensor = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/daylight_detector_top"));
                }
                int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
                List<BakedQuad> quads = new ArrayList<>();
                if (design == 0) {
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 6 / 16f, 0f, 1f, texture, tintIndex, true, true, true, true, true, true));
                } else if (design == 1) {
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1 / 16f, 0f, 1f, texture, tintIndex));

                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 1 / 16f, 6 / 16f, 0f, 1 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 1 / 16f, 6 / 16f, 0f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 1 / 16f, 6 / 16f, 15 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 1 / 16f, 1 / 16f, 6 / 16f, 0f, 1f, texture, tintIndex));

                    quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 15 / 16f, 1 / 16f, 6 / 16f, 1 / 16f, 15 / 16f, -1, sensor_side, sensor_side, sensor_side, sensor_side, sensor, sensor_side, 0));
                } else if (design == 2) {
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 6 / 16f, 0f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 15 / 16f, 6 / 16f, 7 / 16f, 1 / 16f, 15 / 16f, -1, sensor_side, sensor_side, sensor_side, sensor_side, sensor, sensor_side, 0));
                } else if (design == 3) {
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1 / 16f, 0f, 1f, texture, tintIndex));

                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 1 / 16f, 6 / 16f, 0f, 1 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 1 / 16f, 6 / 16f, 0f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 1 / 16f, 6 / 16f, 15 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 1 / 16f, 1 / 16f, 6 / 16f, 0f, 1f, texture, tintIndex));

                    quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 15 / 16f, 1 / 16f, 4 / 16f, 1 / 16f, 15 / 16f, -1, sensor_side, sensor_side, sensor_side, sensor_side, sensor, sensor_side, 0));

                    quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 4 / 16f, 6 / 16f, 1 / 16f, 15 / 16f, glass, -1));
                } else if (design == 4) {
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1 / 16f, 0f, 1f, texture, tintIndex));

                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 1 / 16f, 6 / 16f, 0f, 1 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 1 / 16f, 6 / 16f, 0f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 1 / 16f, 6 / 16f, 15 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 1 / 16f, 1 / 16f, 6 / 16f, 0f, 1f, texture, tintIndex));

                    quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 15 / 16f, 1 / 16f, 5 / 16f, 1 / 16f, 15 / 16f, -1, sensor_side, sensor_side, sensor_side, sensor_side, sensor, sensor_side, 0));

                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 6 / 16f, 5 / 16f, 6 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(10 / 16f, 11 / 16f, 5 / 16f, 6 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 6 / 16f, 5 / 16f, 6 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 6 / 16f, 10 / 16f, 11 / 16f, texture, tintIndex));
                }
                int overlayIndex = extraData.getData(DaylightDetectorFrameTileEntity.OVERLAY);
                if (overlayIndex != 0) {
                    quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 6 / 16f, 0f, 1f, overlayIndex, true, true, true, true, true, true, true));
                }
                return quads;
            }
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