package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
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
 * @version 1.1 02/07/22
 */
public class IllusionCarpetBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        Integer design = extraData.getData(FrameBlockTile.DESIGN);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            TextureAtlasSprite glass = TextureHelper.getGlassTextures().get(extraData.getData(FrameBlockTile.GLASS_COLOR));
            int woolInt = extraData.getData(FrameBlockTile.GLASS_COLOR) - 1;
            if (woolInt < 0)
                woolInt = 0;
            TextureAtlasSprite wool = TextureHelper.getWoolTextures().get(woolInt);
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            boolean renderNorth = extraData.getData(FrameBlockTile.NORTH_VISIBLE);
            boolean renderEast = extraData.getData(FrameBlockTile.EAST_VISIBLE);
            boolean renderSouth = extraData.getData(FrameBlockTile.SOUTH_VISIBLE);
            boolean renderWest = extraData.getData(FrameBlockTile.WEST_VISIBLE);
            int rotation = extraData.getData(FrameBlockTile.ROTATION);
            List<BakedQuad> quads = new ArrayList<>();
            if (design == 0) {
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
            } else if (design == 1) {
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1 / 16f, 0f, 1 / 16f, 0f, 15 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 1, 0f, 1 / 16f, 0f, 1 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(15 / 16f, 1f, 0f, 1 / 16f, 1 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 15 / 16f, 0f, 1 / 16f, 15 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));

                quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 0f, 1 / 16f, 1 / 16f, 15 / 16f, glass, -1, renderNorth, renderSouth, renderEast, renderWest, true, true));
            } else if (design == 2) {
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 0f, 1 / 16f, 0f, 14 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 1, 0f, 1 / 16f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 1f, 0f, 1 / 16f, 2 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 14 / 16f, 0f, 1 / 16f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));

                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 0f, 1 / 16f, 2 / 16f, 14 / 16f, glass, -1, renderNorth, renderSouth, renderEast, renderWest, true, true));
            } else if (design == 3) {
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1 / 16f, 0f, 1 / 16f, 0f, 15 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 1, 0f, 1 / 16f, 0f, 1 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(15 / 16f, 1f, 0f, 1 / 16f, 1 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 15 / 16f, 0f, 1 / 16f, 15 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));

                quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 0f, 1 / 16f, 1 / 16f, 15 / 16f, wool, -1, renderNorth, renderSouth, renderEast, renderWest, true, true));
            } else if (design == 4) {
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 0f, 1 / 16f, 0f, 14 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 1, 0f, 1 / 16f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 1f, 0f, 1 / 16f, 2 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 14 / 16f, 0f, 1 / 16f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));

                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 0f, 1 / 16f, 2 / 16f, 14 / 16f, wool, -1, renderNorth, renderSouth, renderEast, renderWest, true, true));
            }
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1 / 16f, 0f, 1f, overlayIndex, true, true, true, true, true, true, false));
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