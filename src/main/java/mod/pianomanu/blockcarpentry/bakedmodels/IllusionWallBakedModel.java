package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.WallFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import net.minecraft.block.WallHeight;
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
 * @version 1.3 09/18/23
 */
public class IllusionWallBakedModel implements IDynamicBakedModel {
    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getIllusionQuads(state, side, rand, extraData, model);
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

            WallHeight north = state.get(WallFrameBlock.WALL_HEIGHT_NORTH);
            WallHeight east = state.get(WallFrameBlock.WALL_HEIGHT_EAST);
            WallHeight south = state.get(WallFrameBlock.WALL_HEIGHT_SOUTH);
            WallHeight west = state.get(WallFrameBlock.WALL_HEIGHT_WEST);

            boolean isThickPost = state.get(WallFrameBlock.UP);
            //determine wall height - if block above this block is also a wall with connections, the wall height of this block right here needs to be a full block - otherwise just 14/16
            float height_north = 1f;
            float height_east = 1f;
            float height_south = 1f;
            float height_west = 1f;
            float height_post = 1f;
            if (north != WallHeight.TALL)
                height_north = 14 / 16f;
            if (east != WallHeight.TALL)
                height_east = 14 / 16f;
            if (south != WallHeight.TALL)
                height_south = 14 / 16f;
            if (west != WallHeight.TALL)
                height_west = 14 / 16f;

            boolean lowNorth = height_north == 14 / 16f;
            boolean lowEast = height_east == 14 / 16f;
            boolean lowSouth = height_south == 14 / 16f;
            boolean lowWest = height_west == 14 / 16f;

            boolean tallNorth = height_north == 1;
            boolean tallEast = height_east == 1;
            boolean tallSouth = height_south == 1;
            boolean tallWest = height_west == 1;
            boolean somethingOnTop = tallNorth || tallEast || tallSouth || tallWest;

            boolean lowMiddle = lowNorth && lowEast && lowSouth && lowWest;
            if (lowMiddle)
                height_post = 14 / 16f;

            boolean renderNorth = north == WallHeight.NONE;
            boolean renderEast = east == WallHeight.NONE;
            boolean renderSouth = south == WallHeight.NONE;
            boolean renderWest = west == WallHeight.NONE;

            //Create thin middle post
            if (!isThickPost) {
                quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, height_post, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, renderNorth, renderSouth, renderEast, renderWest, !somethingOnTop, true, rotation));
            }
            // Create thick middle post
            else {
                quads.addAll(ModelHelper.createSixFaceCuboid(4 / 16f, 12 / 16f, 0f, 1, 4 / 16f, 12 / 16f, mimic, model, extraData, rand, tintIndex, true, true, true, true, true, true, rotation));
            }

            //classic wall design
            if (design == 0) {
                if (north == WallHeight.LOW || north == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, height_north, 0f, 5 / 16f, mimic, model, extraData, rand, tintIndex, true, false, true, true, lowNorth, true, rotation));
                }
                if (east == WallHeight.LOW || east == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 1f, 0f, height_east, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, true, true, true, false, lowEast, true, rotation));
                }
                if (south == WallHeight.LOW || south == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, height_south, 11 / 16f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, true, lowSouth, true, rotation));
                }
                if (west == WallHeight.LOW || west == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 5 / 16f, 0f, height_west, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, true, lowWest, true, rotation));
                }
            }
            //wall with hole
            if (design == 1) {
                if (state.get(WallFrameBlock.WALL_HEIGHT_NORTH) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_NORTH) == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, 4 / 16f, 0f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_north, 0f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.WALL_HEIGHT_EAST) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_EAST) == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 1f, 0f, 4 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 1f, 10 / 16f, height_east, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.WALL_HEIGHT_SOUTH) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_SOUTH) == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, 4 / 16f, 11 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_south, 11 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.WALL_HEIGHT_WEST) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_WEST) == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 5 / 16f, 0f, 4 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 5 / 16f, 10 / 16f, height_west, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
            }
            //fence-like design
            if (design == 2) {
                if (state.get(WallFrameBlock.WALL_HEIGHT_NORTH) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_NORTH) == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 3 / 16f, 7 / 16f, 0f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_north, 0f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.WALL_HEIGHT_EAST) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_EAST) == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 1f, 3 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 1f, 10 / 16f, height_east, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.WALL_HEIGHT_SOUTH) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_SOUTH) == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 3 / 16f, 7 / 16f, 11 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_south, 11 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.WALL_HEIGHT_WEST) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_WEST) == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 5 / 16f, 3 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 5 / 16f, 10 / 16f, height_west, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
            }
            //heart shaped holes in wall
            if (design == 3) {
                if (state.get(WallFrameBlock.WALL_HEIGHT_NORTH) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_NORTH) == WallHeight.TALL) {
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
                if (state.get(WallFrameBlock.WALL_HEIGHT_EAST) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_EAST) == WallHeight.TALL) {
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
                if (state.get(WallFrameBlock.WALL_HEIGHT_SOUTH) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_SOUTH) == WallHeight.TALL) {
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
                if (state.get(WallFrameBlock.WALL_HEIGHT_WEST) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_WEST) == WallHeight.TALL) {
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
                if (state.get(WallFrameBlock.WALL_HEIGHT_NORTH) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_NORTH) == WallHeight.TALL) {
                    //Cross form
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 12 / 16f, height_north, 0f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 10 / 16f, 12 / 16f, 1 / 16f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 8 / 16f, 10 / 16f, 3 / 16f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 2 / 16f, 8 / 16f, 1 / 16f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, 2 / 16f, 0f, 5 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.WALL_HEIGHT_EAST) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_EAST) == WallHeight.TALL) {
                    //Cross form
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 1f, 12 / 16f, height_east, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 15 / 16f, 10 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 13 / 16f, 8 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 15 / 16f, 2 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(11 / 16f, 1f, 0f, 2 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.WALL_HEIGHT_SOUTH) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_SOUTH) == WallHeight.TALL) {
                    //Cross form
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 12 / 16f, height_south, 11 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 10 / 16f, 12 / 16f, 11 / 16f, 15 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 8 / 16f, 10 / 16f, 11 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 2 / 16f, 8 / 16f, 11 / 16f, 15 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, 2 / 16f, 11 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                }
                if (state.get(WallFrameBlock.WALL_HEIGHT_WEST) == WallHeight.LOW || state.get(WallFrameBlock.WALL_HEIGHT_WEST) == WallHeight.TALL) {
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
                //Create thin middle post
                if (!isThickPost) {
                    quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 0f, height_post, 5 / 16f, 11 / 16f, overlayIndex, renderNorth, renderSouth, renderEast, renderWest, !somethingOnTop, true, true));
                }
                // Create thick middle post
                else {
                    quads.addAll(ModelHelper.createOverlay(4 / 16f, 12 / 16f, 0f, 1, 4 / 16f, 12 / 16f, overlayIndex, true, true, true, true, true, true, true));
                }

                if (north == WallHeight.LOW || north == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 0f, height_north, 0f, 5 / 16f, overlayIndex, true, false, true, true, lowNorth, true, true));
                }
                if (east == WallHeight.LOW || east == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createOverlay(11 / 16f, 1f, 0f, height_east, 5 / 16f, 11 / 16f, overlayIndex, true, true, true, false, lowEast, true, true));
                }
                if (south == WallHeight.LOW || south == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 0f, height_south, 11 / 16f, 1f, overlayIndex, false, true, true, true, lowSouth, true, true));
                }
                if (west == WallHeight.LOW || west == WallHeight.TALL) {
                    quads.addAll(ModelHelper.createOverlay(0f, 5 / 16f, 0f, height_west, 5 / 16f, 11 / 16f, overlayIndex, true, true, false, true, lowWest, true, true));
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
    @Nonnull
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