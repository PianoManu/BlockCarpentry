package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.BedFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.BedFrameTile;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.state.properties.BedPart;
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
 * See {@link ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.1 09/21/20
 */
@SuppressWarnings("deprecation")
public class IllusionBedBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(BedFrameTile.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            if (location != null) {
                IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                if (model != null) {
                    return getMimicQuads(state, side, rand, extraData, model);
                }
            }
        }

        return Collections.emptyList();
    }

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, IBakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.getData(BedFrameTile.MIMIC);
        if (mimic != null && state != null) {
            int tintIndex = -1;
            if (mimic.getBlock() instanceof GrassBlock) {
                tintIndex = 1;
            }
            List<BakedQuad> quads = new ArrayList<>(ModelHelper.createSixFaceCuboid(0f, 1f, 3 / 16f, 5 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
            TextureAtlasSprite pillow;
            TextureAtlasSprite blanket;
            if (extraData.getData(BedFrameTile.PILLOW) > 0) {
                pillow = TextureHelper.getWoolTextures().get(extraData.getData(BedFrameTile.PILLOW)-1);
            } else  {
                pillow = TextureHelper.getWoolTextures().get(0);
            }
            if (extraData.getData(BedFrameTile.BLANKET) > 0) {
                blanket = TextureHelper.getWoolTextures().get(extraData.getData(BedFrameTile.BLANKET)-1);
            } else  {
                blanket = TextureHelper.getWoolTextures().get(0);
            }
            Integer design = extraData.getData(BedFrameTile.DESIGN);
            if (design == null) {
                return quads;
            }
            List<TextureAtlasSprite> planksList = TextureHelper.getPlanksTextures();
            TextureAtlasSprite planks;
            Integer desTex = extraData.getData(BedFrameTile.DESIGN_TEXTURE);
            if (desTex == null || desTex < 0 || desTex > 7) {
                return quads;
            } else {
                planks = planksList.get(desTex);
            }
            //four bed support cubes (bed feet)
            if (state.get(BedFrameBlock.PART) == BedPart.FOOT) {
                switch (state.get(BedBlock.HORIZONTAL_FACING)) {
                    case NORTH:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 0f, 3 / 16f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                        quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 3 / 16f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                        break;
                    case EAST:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 0f, 3 / 16f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 0f, 3 / 16f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                        break;
                    case SOUTH:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 0f, 3 / 16f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex));
                        quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 3 / 16f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex));
                        break;
                    case WEST:
                        quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 3 / 16f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex));
                        quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 3 / 16f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                        break;
                }

            }
            if (state.get(BedFrameBlock.PART) == BedPart.HEAD) {
                switch (state.get(BedBlock.HORIZONTAL_FACING)) {
                    case SOUTH:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 0f, 3 / 16f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                        quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 3 / 16f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                        break;
                    case WEST:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 0f, 3 / 16f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 0f, 3 / 16f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                        break;
                    case NORTH:
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 0f, 3 / 16f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex));
                        quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 3 / 16f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex));
                        break;
                    case EAST:
                        quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 3 / 16f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex));
                        quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 3 / 16f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                        break;
                }
            }
            if (design == 0 || design == 1) {
                if (state.get(BedFrameBlock.PART) == BedPart.FOOT) {
                    switch (state.get(BedBlock.HORIZONTAL_FACING)) {
                        case NORTH:
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 9 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 9 / 16f, -8 / 16f, 0f, blanket, tintIndex));
                            break;
                        case EAST:
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 9 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1f, 24 / 16f, 5 / 16f, 9 / 16f, 0f, 1f, blanket, tintIndex));
                            break;
                        case SOUTH:
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 9 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 9 / 16f, 1f, 24 / 16f, blanket, tintIndex));
                            break;
                        case WEST:
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 9 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(-8 / 16f, 0f, 5 / 16f, 9 / 16f, 0f, 1f, blanket, tintIndex));
                            break;
                    }
                }
                if (state.get(BedFrameBlock.PART) == BedPart.HEAD) {
                    switch (state.get(BedBlock.HORIZONTAL_FACING)) {
                        case SOUTH:
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 10 / 16f, 8 / 16f, 1f, pillow, tintIndex));
                            break;
                        case WEST:
                            quads.addAll(ModelHelper.createCuboid(0f, 8 / 16f, 5 / 16f, 10 / 16f, 0f, 1f, pillow, tintIndex));
                            break;
                        case NORTH:
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 10 / 16f, 0f, 8 / 16f, pillow, tintIndex));
                            break;
                        case EAST:
                            quads.addAll(ModelHelper.createCuboid(8 / 16f, 1f, 5 / 16f, 10 / 16f, 0f, 1f, pillow, tintIndex));
                            break;
                    }
                }
            }
            if (design == 1) {
                if (state.get(BedFrameBlock.PART) == BedPart.FOOT) {
                    switch (state.get(BedBlock.HORIZONTAL_FACING)) {
                        case NORTH:
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 3 / 16f, 9 / 16f, 1f, 17 / 16f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(-1 / 16f, 0f, 3 / 16f, 9 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(1f, 17 / 16f, 3 / 16f, 9 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            break;
                        case EAST:
                            quads.addAll(ModelHelper.createSixFaceCuboid(-1 / 16f, 0f, 3 / 16f, 9 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 3 / 16f, 9 / 16f, -1 / 16f, 0f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 3 / 16f, 9 / 16f, 1f, 17 / 16f, mimic, model, extraData, rand, tintIndex));
                            break;
                        case SOUTH:
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 3 / 16f, 9 / 16f, -1 / 16f, 0f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(-1 / 16f, 0f, 3 / 16f, 9 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(1f, 17 / 16f, 3 / 16f, 9 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            break;
                        case WEST:
                            quads.addAll(ModelHelper.createSixFaceCuboid(1f, 17 / 16f, 3 / 16f, 9 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 3 / 16f, 9 / 16f, -1 / 16f, 0f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 3 / 16f, 9 / 16f, 1f, 17 / 16f, mimic, model, extraData, rand, tintIndex));
                            break;
                    }

                }
                if (state.get(BedFrameBlock.PART) == BedPart.HEAD) {
                    switch (state.get(BedBlock.HORIZONTAL_FACING)) {
                        case SOUTH:
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 3 / 16f, 9 / 16f, 1f, 17 / 16f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(-1 / 16f, 0f, 3 / 16f, 9 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(1f, 17 / 16f, 3 / 16f, 9 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            break;
                        case WEST:
                            quads.addAll(ModelHelper.createSixFaceCuboid(-1 / 16f, 0f, 3 / 16f, 9 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 3 / 16f, 9 / 16f, -1 / 16f, 0f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 3 / 16f, 9 / 16f, 1f, 17 / 16f, mimic, model, extraData, rand, tintIndex));
                            break;
                        case NORTH:
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 3 / 16f, 9 / 16f, -1 / 16f, 0f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(-1 / 16f, 0f, 3 / 16f, 9 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(1f, 17 / 16f, 3 / 16f, 9 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            break;
                        case EAST:
                            quads.addAll(ModelHelper.createSixFaceCuboid(1f, 17 / 16f, 3 / 16f, 9 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 3 / 16f, 9 / 16f, -1 / 16f, 0f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 3 / 16f, 9 / 16f, 1f, 17 / 16f, mimic, model, extraData, rand, tintIndex));
                            break;
                    }
                }
            }
            if (design == 2) {
                if (state.get(BedFrameBlock.PART) == BedPart.FOOT) {
                    switch (state.get(BedBlock.HORIZONTAL_FACING)) {
                        case NORTH:
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 9 / 16f, 0f, 14 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 9 / 16f, -8 / 16f, 0f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1 / 16f, 5 / 16f, 10 / 16f, 0f, 14 / 16f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(15 / 16f, 1f, 5 / 16f, 10 / 16f, 0f, 14 / 16f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 5 / 16f, 12 / 16f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                            break;
                        case EAST:
                            quads.addAll(ModelHelper.createCuboid(2 / 16f, 1f, 5 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1f, 24 / 16f, 5 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 1f, 5 / 16f, 10 / 16f, 0f, 1 / 16f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 1f, 5 / 16f, 10 / 16f, 15 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 5 / 16f, 12 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            break;
                        case SOUTH:
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 9 / 16f, 2 / 16f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 9 / 16f, 1f, 24 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1 / 16f, 5 / 16f, 10 / 16f, 2 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(15 / 16f, 1f, 5 / 16f, 10 / 16f, 2 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 5 / 16f, 12 / 16f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex));
                            break;
                        case WEST:
                            quads.addAll(ModelHelper.createCuboid(0f, 14 / 16f, 5 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(-8 / 16f, 0f, 5 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 14 / 16f, 5 / 16f, 10 / 16f, 0f, 1 / 16f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 14 / 16f, 5 / 16f, 10 / 16f, 15 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 1f, 5 / 16f, 12 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            break;
                    }
                }
                if (state.get(BedFrameBlock.PART) == BedPart.HEAD) {
                    switch (state.get(BedBlock.HORIZONTAL_FACING)) {
                        case SOUTH:
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 10 / 16f, 8 / 16f, 14 / 16f, pillow, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1 / 16f, 5 / 16f, 10 / 16f, 0f, 14 / 16f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(15 / 16f, 1f, 5 / 16f, 10 / 16f, 0f, 14 / 16f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 5 / 16f, 12 / 16f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                            break;
                        case WEST:
                            quads.addAll(ModelHelper.createCuboid(2 / 16f, 8 / 16f, 5 / 16f, 10 / 16f, 1 / 16f, 15 / 16f, pillow, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 1f, 5 / 16f, 10 / 16f, 0f, 1 / 16f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 1f, 5 / 16f, 10 / 16f, 15 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 5 / 16f, 12 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            break;
                        case NORTH:
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 10 / 16f, 2 / 16f, 8 / 16f, pillow, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1 / 16f, 5 / 16f, 10 / 16f, 2 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(15 / 16f, 1f, 5 / 16f, 10 / 16f, 2 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 5 / 16f, 12 / 16f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex));
                            break;
                        case EAST:
                            quads.addAll(ModelHelper.createCuboid(8 / 16f, 14 / 16f, 5 / 16f, 10 / 16f, 1 / 16f, 15 / 16f, pillow, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 14 / 16f, 5 / 16f, 10 / 16f, 0f, 1 / 16f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 14 / 16f, 5 / 16f, 10 / 16f, 15 / 16f, 1f, mimic, model, extraData, rand, tintIndex));
                            quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 1f, 5 / 16f, 12 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex));
                            break;
                    }
                }
            }
            if (design == 3) {
                if (state.get(BedFrameBlock.PART) == BedPart.FOOT) {
                    switch (state.get(BedBlock.HORIZONTAL_FACING)) {
                        case NORTH:
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 9 / 16f, 0f, 14 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 9 / 16f, -8 / 16f, 0f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1 / 16f, 5 / 16f, 10 / 16f, 0f, 14 / 16f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 5 / 16f, 10 / 16f, 0f, 14 / 16f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 12 / 16f, 14 / 16f, 1f, planks, tintIndex));
                            break;
                        case EAST:
                            quads.addAll(ModelHelper.createCuboid(2 / 16f, 1f, 5 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1f, 24 / 16f, 5 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(2 / 16f, 1f, 5 / 16f, 10 / 16f, 0f, 1 / 16f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(2 / 16f, 1f, 5 / 16f, 10 / 16f, 15 / 16f, 1f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 5 / 16f, 12 / 16f, 0f, 1f, planks, tintIndex));
                            break;
                        case SOUTH:
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 9 / 16f, 2 / 16f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 9 / 16f, 1f, 24 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1 / 16f, 5 / 16f, 10 / 16f, 2 / 16f, 1f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 5 / 16f, 10 / 16f, 2 / 16f, 1f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 12 / 16f, 0f, 2 / 16f, planks, tintIndex));
                            break;
                        case WEST:
                            quads.addAll(ModelHelper.createCuboid(0f, 14 / 16f, 5 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(-8 / 16f, 0f, 5 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 14 / 16f, 5 / 16f, 10 / 16f, 0f, 1 / 16f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 14 / 16f, 5 / 16f, 10 / 16f, 15 / 16f, 1f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 5 / 16f, 12 / 16f, 0f, 1f, planks, tintIndex));
                            break;
                    }
                }
                if (state.get(BedFrameBlock.PART) == BedPart.HEAD) {
                    switch (state.get(BedBlock.HORIZONTAL_FACING)) {
                        case SOUTH:
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 10 / 16f, 8 / 16f, 14 / 16f, pillow, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1 / 16f, 5 / 16f, 10 / 16f, 0f, 14 / 16f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 5 / 16f, 10 / 16f, 0f, 14 / 16f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 12 / 16f, 14 / 16f, 1f, planks, tintIndex));
                            break;
                        case WEST:
                            quads.addAll(ModelHelper.createCuboid(2 / 16f, 8 / 16f, 5 / 16f, 10 / 16f, 1 / 16f, 15 / 16f, pillow, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(2 / 16f, 1f, 5 / 16f, 10 / 16f, 0f, 1 / 16f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(2 / 16f, 1f, 5 / 16f, 10 / 16f, 15 / 16f, 1f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 5 / 16f, 12 / 16f, 0f, 1f, planks, tintIndex));
                            break;
                        case NORTH:
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 10 / 16f, 2 / 16f, 8 / 16f, pillow, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1 / 16f, 5 / 16f, 10 / 16f, 2 / 16f, 1f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 5 / 16f, 10 / 16f, 2 / 16f, 1f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 12 / 16f, 0f, 2 / 16f, planks, tintIndex));
                            break;
                        case EAST:
                            quads.addAll(ModelHelper.createCuboid(8 / 16f, 14 / 16f, 5 / 16f, 10 / 16f, 1 / 16f, 15 / 16f, pillow, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 14 / 16f, 5 / 16f, 10 / 16f, 0f, 1 / 16f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 14 / 16f, 5 / 16f, 10 / 16f, 15 / 16f, 1f, planks, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 5 / 16f, 12 / 16f, 0f, 1f, planks, tintIndex));
                            break;
                    }
                }
            }
            if (design == 4) {
                if (state.get(BedFrameBlock.PART) == BedPart.FOOT) {
                    switch (state.get(BedBlock.HORIZONTAL_FACING)) {
                        case NORTH:
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 10 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 10 / 16f, -8 / 16f, 0f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 3 / 16f, 9 / 16f, 1f, 17 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(-1 / 16f, 0f, 3 / 16f, 9 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1f, 17 / 16f, 3 / 16f, 9 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(-1 / 16f, 0f, 3 / 16f, 9 / 16f, -8 / 16f, 0f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1f, 17 / 16f, 3 / 16f, 9 / 16f, -8 / 16f, 0f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 9 / 16f, -9 / 16f, -8 / 16f, blanket, tintIndex));
                            break;
                        case EAST:
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 10 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1f, 24 / 16f, 5 / 16f, 10 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(-1 / 16f, 0f, 3 / 16f, 9 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 3 / 16f, 9 / 16f, -1 / 16f, 0f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 3 / 16f, 9 / 16f, 1f, 17 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1f, 24 / 16f, 3 / 16f, 9 / 16f, -1 / 16f, 0f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1f, 24 / 16f, 3 / 16f, 9 / 16f, 1f, 17 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(24 / 16f, 25 / 16f, 5 / 16f, 9 / 16f, 0f, 1f, blanket, tintIndex));
                            break;
                        case SOUTH:
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 10 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 10 / 16f, 1f, 24 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 3 / 16f, 9 / 16f, -1 / 16f, 0f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(-1 / 16f, 0f, 3 / 16f, 9 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1f, 17 / 16f, 3 / 16f, 9 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(-1 / 16f, 0f, 3 / 16f, 9 / 16f, 1f, 24 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1f, 17 / 16f, 3 / 16f, 9 / 16f, 1f, 24 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 9 / 16f, 24 / 16f, 25 / 16f, blanket, tintIndex));
                            break;
                        case WEST:
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 10 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(-8 / 16f, 0f, 5 / 16f, 10 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1f, 17 / 16f, 3 / 16f, 9 / 16f, 0f, 1f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 3 / 16f, 9 / 16f, -1 / 16f, 0f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 3 / 16f, 9 / 16f, 1f, 17 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(-8 / 16f, 0f, 3 / 16f, 9 / 16f, -1 / 16f, 0f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(-8 / 16f, 0f, 3 / 16f, 9 / 16f, 1f, 17 / 16f, blanket, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(-9 / 16f, -8 / 16f, 5 / 16f, 9 / 16f, 0f, 1f, blanket, tintIndex));
                            break;
                    }
                }
                if (state.get(BedFrameBlock.PART) == BedPart.HEAD) {
                    switch (state.get(BedBlock.HORIZONTAL_FACING)) {
                        case SOUTH:
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 10 / 16f, 9 / 16f, 1f, pillow, tintIndex));
                            break;
                        case WEST:
                            quads.addAll(ModelHelper.createCuboid(0f, 7 / 16f, 5 / 16f, 10 / 16f, 0f, 1f, pillow, tintIndex));
                            break;
                        case NORTH:
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 10 / 16f, 0f, 7 / 16f, pillow, tintIndex));
                            break;
                        case EAST:
                            quads.addAll(ModelHelper.createCuboid(9 / 16f, 1f, 5 / 16f, 10 / 16f, 0f, 1f, pillow, tintIndex));
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