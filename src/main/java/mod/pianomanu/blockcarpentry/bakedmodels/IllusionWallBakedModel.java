package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.block.WallFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import net.minecraft.block.BlockState;
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
 * See {@link ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.3 05/28/21
 */
public class IllusionWallBakedModel implements IDynamicBakedModel {
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
        Integer design = extraData.getData(FrameBlockTile.DESIGN);
        if (mimic != null && state != null) {
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            int rotation = extraData.getData(FrameBlockTile.ROTATION);
            List<BakedQuad> quads = new ArrayList<>();

            //Create middle post
            if (state.get(WallFrameBlock.UP)) {
                quads.addAll(ModelHelper.createSixFaceCuboid(4 / 16f, 12 / 16f, 0f, 1f, 4 / 16f, 12 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
            } else {
                quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, 14 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
            }
            if (state.get(WallFrameBlock.NORTH) && state.get(WallFrameBlock.SOUTH) || state.get(WallFrameBlock.EAST) && state.get(WallFrameBlock.WEST)) {
                quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, 14/16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
            } else if (!state.get(WallFrameBlock.NORTH) && !state.get(WallFrameBlock.SOUTH) || !state.get(WallFrameBlock.EAST) && !state.get(WallFrameBlock.WEST)) {
                quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, 14 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
            }

            //determine wall height - if block above this block is also a wall with connections, the wall height of this block right here needs to be a full block - otherwise just 14/16
            float height_north = 1f;
            float height_east = 1f;
            float height_south = 1f;
            float height_west = 1f;
            if (state.get(WallFrameBlock.NORTH))
                height_north = 14 / 16f;
            if (state.get(WallFrameBlock.EAST))
                height_east = 14 / 16f;
            if (state.get(WallFrameBlock.SOUTH))
                height_south = 14 / 16f;
            if (state.get(WallFrameBlock.WEST))
                height_west = 14 / 16f;

            //classic wall design
            if (design == 0) {
                if (state.get(WallFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, height_north, 0f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 1f, 0f, height_east, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, height_south, 11 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 5 / 16f, 0f, height_west, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
            }
            //wall with hole
            if (design == 1) {
                if (state.get(WallFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, 4 / 16f, 0f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_north, 0f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 1f, 0f, 4 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 1f, 10 / 16f, height_east, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, 4 / 16f, 11 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_south, 11 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 5 / 16f, 0f, 4 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 5 / 16f, 10 / 16f, height_west, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
            }
            //fence-like design
            if (design == 2) {
                if (state.get(WallFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 3 / 16f, 7 / 16f, 0f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_north, 0f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 1f, 3 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 1f, 10 / 16f, height_east, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 3 / 16f, 7 / 16f, 11 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_south, 11 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 5 / 16f, 3 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 5 / 16f, 10 / 16f, height_west, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
            }
            //heart shaped holes in wall
            if (design == 3) {
                if (state.get(WallFrameBlock.NORTH)) {
                    //Heart form
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 12 / 16f, height_north, 0f, 4 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 3 / 16f, 4 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 0f, 1 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 8 / 16f, 9 / 16f, 3 / 16f, 4 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 7 / 16f, 8 / 16f, 2 / 16f, 4 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 6 / 16f, 7 / 16f, 1 / 16f, 4 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, 6 / 16f, 0f, 4 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, height_north, 4 / 16f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));

                }
                if (state.get(WallFrameBlock.EAST)) {
                    //Heart form
                    quads.addAll(ModelHelper.createSixFaceCuboid(12 / 16f, 1f, 12 / 16f, height_east, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(15 / 16f, 1f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(12 / 16f, 13 / 16f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(12 / 16f, 13 / 16f, 8 / 16f, 9 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(12 / 16f, 14 / 16f, 7 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(12 / 16f, 15 / 16f, 6 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(12 / 16f, 1f, 0f, 6 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 12 / 16f, 0f, height_east, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.SOUTH)) {
                    //Heart form
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 12 / 16f, height_south, 12 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 15 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 12 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 8 / 16f, 9 / 16f, 12 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 7 / 16f, 8 / 16f, 12 / 16f, 14 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 6 / 16f, 7 / 16f, 12 / 16f, 15 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, 6 / 16f, 12 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, height_south, 11 / 16f, 12 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.WEST)) {
                    //Heart form
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 4 / 16f, 12 / 16f, height_west, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(3 / 16f, 4 / 16f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1 / 16f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(3 / 16f, 4 / 16f, 8 / 16f, 9 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 4 / 16f, 7 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 4 / 16f, 6 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 4 / 16f, 0f, 6 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(4 / 16f, 5 / 16f, 0f, height_west, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
            }
            //cross shaped holes in wall
            if (design == 4) {
                if (state.get(WallFrameBlock.NORTH)) {
                    //Cross form
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 12 / 16f, height_north, 0f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 10 / 16f, 12 / 16f, 1 / 16f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 8 / 16f, 10 / 16f, 3 / 16f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 2 / 16f, 8 / 16f, 1 / 16f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, 2 / 16f, 0f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.EAST)) {
                    //Cross form
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 1f, 12 / 16f, height_east, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 15 / 16f, 10 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 13 / 16f, 8 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 15 / 16f, 2 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 1f, 0f, 2 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.SOUTH)) {
                    //Cross form
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 12 / 16f, height_south, 11 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 10 / 16f, 12 / 16f, 11 / 16f, 15 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 8 / 16f, 10 / 16f, 11 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 2 / 16f, 8 / 16f, 11 / 16f, 15 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, 2 / 16f, 11 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.WEST)) {
                    //Cross form
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 5 / 16f, 12 / 16f, height_west, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 5 / 16f, 10 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(3 / 16f, 5 / 16f, 8 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 5 / 16f, 2 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 5 / 16f, 0f, 2 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
            }
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                if (state.get(WallFrameBlock.UP) && !(state.get(WallFrameBlock.NORTH) || state.get(WallFrameBlock.EAST) || state.get(WallFrameBlock.SOUTH) || state.get(WallFrameBlock.WEST))) {
                    quads.addAll(ModelHelper.createOverlay(4 / 16f, 12 / 16f, 0f, 1f, 4 / 16f, 12 / 16f, overlayIndex));
                } else {
                    if (state.get(WallFrameBlock.NORTH) || state.get(WallFrameBlock.EAST) || state.get(WallFrameBlock.SOUTH) || state.get(WallFrameBlock.WEST))
                        quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 0f, 14/16f, 5 / 16f, 11 / 16f, overlayIndex));
                }
                if (state.get(WallFrameBlock.NORTH)) {
                    //quads.retainAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, height_north, 0f, 5 / 16f, texture.get(index), tintIndex, rotation));
                    quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 0f, height_north, 0f, 5 / 16f, overlayIndex));
                }
                if (state.get(WallFrameBlock.EAST)) {
                    //quads.retainAll(ModelHelper.createCuboid(11 / 16f, 1f, 0f, height_east, 5 / 16f, 11 / 16f, texture.get(index), tintIndex, rotation));
                    quads.addAll(ModelHelper.createOverlay(11 / 16f, 1f, 0f, height_east, 5 / 16f, 11 / 16f, overlayIndex));
                }
                if (state.get(WallFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 0f, height_south, 11 / 16f, 1f, overlayIndex));
                }
                if (state.get(WallFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createOverlay(0f, 5 / 16f, 0f, height_west, 5 / 16f, 11 / 16f, overlayIndex));
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