package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
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
 * @version 1.8 05/30/22
 */
public class SlopeBakedModel implements IDynamicBakedModel {
    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        //get block saved in frame tile
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            //model.getBakedModel().getQuads(mimic, side, rand, extraData);
            //only if model (from block saved in tile entity) exists:
            return getMimicQuads(state, side, rand, extraData, model);
        }
        return Collections.emptyList();
    }

    private static Vector3d v(double x, double y, double z) {
        return new Vector3d(x, y, z);
    }

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, IBakedModel model) {
        if (side != null || state == null)
            return new ArrayList<>();
        List<BakedQuad> quads = new ArrayList<>();
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        List<TextureAtlasSprite> texture = TextureHelper.getTextureFromModel(model, extraData, rand);
        int index = extraData.getData(FrameBlockTile.TEXTURE);
        if (index >= texture.size()) {
            index = 0;
        }
        if (texture.size() == 0) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.block_not_available"), true);
            }
            return Collections.emptyList();
        }
        int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
        quads.addAll(createSlope(0, 1, 0, 1, 0, 1, texture.get(index), tintIndex, state.get(StairsBlock.FACING), state.get(StairsBlock.SHAPE), state.get(StairsBlock.HALF)));
        return quads;
    }

    private List<BakedQuad> createSlope(float xl, float xh, float yl, float yh, float zl, float zh, TextureAtlasSprite texture, int tintIndex, Direction direction, StairsShape shape, Half half) {
        List<BakedQuad> quads = new ArrayList<>();
        //Eight corners of the block
        Vector3d NWU = v(xl, yh, zl); //North-West-Up
        Vector3d SWU = v(xl, yh, zh); //...
        Vector3d NWD = v(xl, yl, zl);
        Vector3d SWD = v(xl, yl, zh);
        Vector3d NEU = v(xh, yh, zl);
        Vector3d SEU = v(xh, yh, zh);
        Vector3d NED = v(xh, yl, zl);
        Vector3d SED = v(xh, yl, zh); //South-East-Down
        if (xh - xl > 1 || yh - yl > 1 || zh - zl > 1) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("An error occured with this block, please report to the mod author (PianoManu)"), true);
            }
            return quads;
        }
        boolean isBottom = half == Half.BOTTOM;
        if (isBottom) {
            //bottom face
            quads.add(ModelHelper.createQuad(SWD, NWD, NED, SED, texture, 0, 16, 0, 16, tintIndex));
            switch (shape) {
                case STRAIGHT:
                    switch (direction) {
                        case NORTH:
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NEU, SED, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, SWD, SED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            break;
                        case EAST:
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWD, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWD, SEU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            break;
                        case SOUTH:
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SWU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, SWU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            break;
                        case WEST:
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWU, SED, NED, NWU, texture, 0, 16, 0, 16, tintIndex));
                            break;
                    }
                    break;
                case OUTER_LEFT:
                    switch (direction) {
                        case NORTH:
                            //back face
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuad(NWU, SWD, SED, NWU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NWU, SED, texture, 0, 16, 16, 0, tintIndex));
                            break;
                        case EAST:
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NEU, SED, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NEU, texture, 0, 16, 0, 16, tintIndex));

                            //top faces
                            quads.add(ModelHelper.createQuad(NEU, NWD, SWD, NEU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, NEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            break;
                        case SOUTH:
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuad(SEU, NED, NWD, SEU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SEU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            break;
                        case WEST:
                            //back face
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SWU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuad(SWU, SED, NED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, SWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            break;
                    }
                    break;
                case OUTER_RIGHT:
                    switch (direction) {
                        case WEST:
                            //back face
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuad(NWU, SWD, SED, NWU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NWU, SED, texture, 0, 16, 16, 0, tintIndex));
                            break;
                        case NORTH:
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NEU, SED, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NEU, texture, 0, 16, 0, 16, tintIndex));

                            //top faces
                            quads.add(ModelHelper.createQuad(NEU, NWD, SWD, NEU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, NEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            break;
                        case EAST:
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuad(SEU, NED, NWD, SEU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SEU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            break;
                        case SOUTH:
                            //back face
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SWU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuad(SWU, SED, NED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, SWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            break;
                    }
                    break;
                case INNER_LEFT:
                    switch (direction) {
                        case NORTH:
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NEU, SED, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SED, NEU, NWU, SED, texture, 16, 0, 16, 0, tintIndex));

                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NWU, SWU, SED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            break;
                        case EAST:
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWD, SEU, NEU, SWD, texture, 16, 0, 16, 0, tintIndex));

                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NEU, NWU, SWD, NEU, texture, 16, 0, 0, 16, tintIndex));
                            break;
                        case SOUTH:
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SWU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, SWU, SEU, NWD, texture, 16, 0, 16, 0, tintIndex));

                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWD, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SEU, NEU, NWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            break;
                        case WEST:
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NED, NWU, SWU, NED, texture, 16, 0, 16, 0, tintIndex));

                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SWU, SEU, NED, SWU, texture, 16, 0, 0, 16, tintIndex));
                            break;
                    }
                    break;
                case INNER_RIGHT:
                    switch (direction) {
                        case WEST:
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NEU, SED, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SED, NEU, NWU, SED, texture, 16, 0, 16, 0, tintIndex));

                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NWU, SWU, SED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            break;
                        case NORTH:
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWD, SEU, NEU, SWD, texture, 16, 0, 16, 0, tintIndex));

                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NEU, NWU, SWD, NEU, texture, 16, 0, 0, 16, tintIndex));
                            break;
                        case EAST:
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SWU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, SWU, SEU, NWD, texture, 16, 0, 16, 0, tintIndex));

                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWD, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SEU, NEU, NWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            break;
                        case SOUTH:
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NED, NWU, SWU, NED, texture, 16, 0, 16, 0, tintIndex));

                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SWU, SEU, NED, SWU, texture, 16, 0, 0, 16, tintIndex));
                            break;
                    }
                    break;
            }
        }
        else {
            //top face
            quads.add(ModelHelper.createQuad(NWU, SWU, SEU, NEU, texture, 0, 16, 0, 16, tintIndex));
            switch (shape) {
                case STRAIGHT:
                    switch (direction) {
                        case NORTH:
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NED, NEU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NWD, SWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NED, SEU, SWU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            break;
                        case EAST:
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SED, SEU, SWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, NED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, NED, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            break;
                        case SOUTH:
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SWD, SWU, NWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SED, NEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NEU, SED, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            break;
                        case WEST:
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NWD, NWU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, SWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, NEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            break;
                    }
                    break;
                case OUTER_LEFT:
                    switch (direction) {
                        case NORTH:
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWD, NWU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NWD, SWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, NWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(NWD, NEU, SEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            break;
                        case EAST:
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NED, NEU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, NED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NED, SWU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(NED, SEU, SWU, NED, texture, 16, 0, 16, 0, tintIndex));
                            break;
                        case SOUTH:
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SED, SEU, SWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SED, NEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, SED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(SED, SWU, NWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            break;
                        case WEST:
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWD, SWU, NWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, SWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SWD, NEU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(SWD, NWU, NEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            break;
                    }
                    break;
                case OUTER_RIGHT:
                    switch (direction) {
                        case WEST:
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWD, NWU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NWD, SWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, NWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(NWD, NEU, SEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            break;
                        case NORTH:
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NED, NEU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, NED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NED, SWU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(NED, SEU, SWU, NED, texture, 16, 0, 16, 0, tintIndex));
                            break;
                        case EAST:
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SED, SEU, SWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SED, NEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            //quads.add(ModelHelper.createQuad(NEU, SED, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, SED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(SED, SWU, NWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            break;
                        case SOUTH:
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWD, SWU, NWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, SWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SWD, NEU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(SWD, NWU, NEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            break;
                    }
                    break;
                case INNER_LEFT:
                    switch (direction) {
                        case NORTH:
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NED, NEU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NWD, NED, SEU, NWD, texture, 0, 16, 16, 0, tintIndex));

                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, SWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SEU, SWD, NWD, SEU, texture, 0, 16, 0, 16, tintIndex));
                            break;
                        case EAST:
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SED, SEU, SWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NED, SED, SWU, NED, texture, 0, 16, 16, 0, tintIndex));

                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NWD, SWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWU, NWD, NED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            break;
                        case SOUTH:
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SWD, SWU, NWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SED, SWD, NWU, SED, texture, 0, 16, 16, 0, tintIndex));

                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, NED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, NED, SED, NWU, texture, 0, 16, 0, 16, tintIndex));
                            break;
                        case WEST:
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NWD, NWU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SWD, NWD, NEU, SWD, texture, 0, 16, 16, 0, tintIndex));

                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SED, NEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NEU, SED, SWD, NEU, texture, 0, 16, 0, 16, tintIndex));
                            break;
                    }
                    break;
                case INNER_RIGHT:
                    switch (direction) {
                        case WEST:
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NED, NEU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NWD, NED, SEU, NWD, texture, 0, 16, 16, 0, tintIndex));

                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, SWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SEU, SWD, NWD, SEU, texture, 0, 16, 0, 16, tintIndex));
                            break;
                        case NORTH:
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SED, SEU, SWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NED, SED, SWU, NED, texture, 0, 16, 16, 0, tintIndex));

                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NWD, SWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWU, NWD, NED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            break;
                        case EAST:
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SWD, SWU, NWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SED, SWD, NWU, SED, texture, 0, 16, 16, 0, tintIndex));

                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, NED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, NED, SED, NWU, texture, 0, 16, 0, 16, tintIndex));
                            break;
                        case SOUTH:
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NWD, NWU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SWD, NWD, NEU, SWD, texture, 0, 16, 16, 0, tintIndex));

                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SED, NEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NEU, SED, SWD, NEU, texture, 0, 16, 0, 16, tintIndex));
                            break;
                    }
                    break;
            }
        }
        return quads;
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