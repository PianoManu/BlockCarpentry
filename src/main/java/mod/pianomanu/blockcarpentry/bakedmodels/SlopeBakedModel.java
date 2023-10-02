package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.Vec3;
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
 * @version 1.4 09/21/23
 */
public class SlopeBakedModel implements IDynamicBakedModel {
    private static Vec3 v(double x, double y, double z) {
        return new Vec3(x, y, z);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        //get block saved in frame tile
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null) {
            net.minecraft.client.resources.model.ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            //only if model (from block saved in tile entity) exists:
            return getMimicQuads(state, side, rand, extraData, model);
        }
        return Collections.emptyList();
    }

    private static void o(List<BakedQuad> quads, Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, float ulow, float uhigh, float vlow, float vhigh, boolean invert, int overlayIndex, int overlaySideIndex) {
        TextureAtlasSprite texture = null;
        ModelInformation m = ModelHelper.getOverlayModelInformation(overlayIndex);
        if (overlaySideIndex == 0)
            texture = m.northTexture;
        if (overlaySideIndex == 1)
            texture = m.eastTexture;
        if (overlaySideIndex == 2)
            texture = m.southTexture;
        if (overlaySideIndex == 3)
            texture = m.westTexture;
        if (overlaySideIndex == 4)
            texture = m.upTexture;
        if (overlaySideIndex == 5)
            texture = m.downTexture;
        if (overlayIndex > 0 && texture != null) {
            if (invert) {
                quads.add(ModelHelper.createQuadInverted(v1, v2, v3, v4, texture, ulow, uhigh, vlow, vhigh, m.tintIndex));
            } else {
                quads.add(ModelHelper.createQuad(v1, v2, v3, v4, texture, ulow, uhigh, vlow, vhigh, m.tintIndex));
            }
        }
    }

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, BakedModel model) {
        if (side != null)
            return new ArrayList<>();
        List<BakedQuad> quads = new ArrayList<>();
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        TextureAtlasSprite texture = QuadUtils.getTexture(model, rand, extraData, FrameBlockTile.TEXTURE);
        int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
        int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
        quads.addAll(createSlope(0, 1, 0, 1, 0, 1, texture, tintIndex, state.getValue(StairBlock.FACING), state.getValue(StairBlock.SHAPE), state.getValue(StairBlock.HALF), overlayIndex));
        return quads;
    }

    private List<BakedQuad> createSlope(float xl, float xh, float yl, float yh, float zl, float zh, TextureAtlasSprite texture, int tintIndex, Direction direction, StairsShape shape, Half half, int overlayIndex) {
        List<BakedQuad> quads = new ArrayList<>();
        //Eight corners of the block
        Vec3 NWU = v(xl, yh, zl); //North-West-Up
        Vec3 SWU = v(xl, yh, zh); //...
        Vec3 NWD = v(xl, yl, zl);
        Vec3 SWD = v(xl, yl, zh);
        Vec3 NEU = v(xh, yh, zl);
        Vec3 SEU = v(xh, yh, zh);
        Vec3 NED = v(xh, yl, zl);
        Vec3 SED = v(xh, yl, zh); //South-East-Down
        if (xh - xl > 1 || yh - yl > 1 || zh - zl > 1) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("An error occured with this block, please report to the mod author (PianoManu)"), true);
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
                        case NORTH -> {
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NED, NWD, NWU, 0, 16, 0, 16, false, overlayIndex, 0);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NEU, SED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SED, NED, NEU, SED, 0, 16, 16, 0, true, overlayIndex, 1);
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NWD, SWD, NWU, 0, 16, 0, 16, false, overlayIndex, 3);
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, SWD, SED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, SWD, SED, NEU, 0, 16, 0, 16, false, overlayIndex, 4);
                        }
                        case EAST -> {
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SED, NED, NEU, 0, 16, 0, 16, false, overlayIndex, 1);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWD, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NED, NWD, NWD, 0, 16, 0, 16, false, overlayIndex, 0);
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SWD, SED, SEU, SWD, 0, 16, 16, 0, true, overlayIndex, 2);
                            //top face
                            quads.add(ModelHelper.createQuad(SWD, SEU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SWD, SEU, NEU, NWD, 16, 0, 16, 0, false, overlayIndex, 4);
                        }
                        case SOUTH -> {
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SWD, SED, SEU, 0, 16, 0, 16, false, overlayIndex, 2);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SED, NED, SEU, 0, 16, 0, 16, false, overlayIndex, 1);
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SWU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NWD, SWD, SWU, NWD, 0, 16, 16, 0, true, overlayIndex, 3);
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, SWU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NWD, SWU, SEU, NED, 16, 0, 16, 0, false, overlayIndex, 4);
                        }
                        case WEST -> {
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NWD, SWD, SWU, 0, 16, 0, 16, false, overlayIndex, 3);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NED, NWD, NWU, NED, 0, 16, 16, 0, true, overlayIndex, 0);
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SWD, SED, SWU, 0, 16, 0, 16, false, overlayIndex, 2);
                            //top face
                            quads.add(ModelHelper.createQuad(SWU, SED, NED, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SED, NED, NWU, 0, 16, 0, 16, false, overlayIndex, 4);
                        }
                    }
                    break;
                case OUTER_LEFT:
                    switch (direction) {
                        case NORTH -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NED, NWD, NWU, NED, 0, 16, 16, 0, true, overlayIndex, 0);
                            //slanted face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NWD, SWD, NWU, 0, 16, 0, 16, false, overlayIndex, 3);
                            //top faces
                            quads.add(ModelHelper.createQuad(NWU, SWD, SED, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, SWD, SED, NWU, 0, 16, 0, 16, false, overlayIndex, 4);
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NWU, SED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SED, NED, NWU, SED, 0, 16, 16, 0, true, overlayIndex, 4);
                        }
                        case EAST -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NEU, SED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SED, NED, NEU, SED, 0, 16, 16, 0, true, overlayIndex, 1);
                            //slanted face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NED, NWD, NEU, 0, 16, 0, 16, false, overlayIndex, 0);

                            //top faces
                            quads.add(ModelHelper.createQuad(NEU, NWD, SWD, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NWD, SWD, NEU, 0, 16, 0, 16, false, overlayIndex, 4);
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, NEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SWD, SED, NEU, SWD, 0, 16, 16, 0, true, overlayIndex, 4);
                        }
                        case SOUTH -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SWD, SED, SEU, SWD, 0, 16, 16, 0, true, overlayIndex, 2);
                            //slanted face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SED, NED, SEU, 0, 16, 0, 16, false, overlayIndex, 1);
                            //top faces
                            quads.add(ModelHelper.createQuad(SEU, NED, NWD, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, NED, NWD, SEU, 0, 16, 0, 16, false, overlayIndex, 4);
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SEU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NWD, SWD, SEU, NWD, 0, 16, 16, 0, true, overlayIndex, 4);
                        }
                        case WEST -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SWU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NWD, SWD, SWU, NWD, 0, 16, 16, 0, true, overlayIndex, 3);
                            //slanted face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SWD, SED, SWU, 0, 16, 0, 16, false, overlayIndex, 2);
                            //top faces
                            quads.add(ModelHelper.createQuad(SWU, SED, NED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SED, NED, SWU, 0, 16, 0, 16, false, overlayIndex, 4);
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, SWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NED, NWD, SWU, NED, 0, 16, 16, 0, true, overlayIndex, 4);
                        }
                    }
                    break;
                case OUTER_RIGHT:
                    switch (direction) {
                        case WEST -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NED, NWD, NWU, NED, 0, 16, 16, 0, true, overlayIndex, 0);
                            //slanted face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NWD, SWD, NWU, 0, 16, 0, 16, false, overlayIndex, 3);
                            //top faces
                            quads.add(ModelHelper.createQuad(NWU, SWD, SED, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, SWD, SED, NWU, 0, 16, 0, 16, false, overlayIndex, 4);
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NWU, SED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SED, NED, NWU, SED, 0, 16, 16, 0, true, overlayIndex, 4);
                        }
                        case NORTH -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NEU, SED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SED, NED, NEU, SED, 0, 16, 16, 0, true, overlayIndex, 1);
                            //slanted face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NED, NWD, NEU, 0, 16, 0, 16, false, overlayIndex, 0);

                            //top faces
                            quads.add(ModelHelper.createQuad(NEU, NWD, SWD, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NWD, SWD, NEU, 0, 16, 0, 16, false, overlayIndex, 4);
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, NEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SWD, SED, NEU, SWD, 0, 16, 16, 0, true, overlayIndex, 4);
                        }
                        case EAST -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SWD, SED, SEU, SWD, 0, 16, 16, 0, true, overlayIndex, 2);
                            //slanted face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SED, NED, SEU, 0, 16, 0, 16, false, overlayIndex, 1);
                            //top faces
                            quads.add(ModelHelper.createQuad(SEU, NED, NWD, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, NED, NWD, SEU, 0, 16, 0, 16, false, overlayIndex, 4);
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SEU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NWD, SWD, SEU, NWD, 0, 16, 16, 0, true, overlayIndex, 4);
                        }
                        case SOUTH -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SWU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NWD, SWD, SWU, NWD, 0, 16, 16, 0, true, overlayIndex, 3);
                            //slanted face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SWD, SED, SWU, 0, 16, 0, 16, false, overlayIndex, 2);
                            //top faces
                            quads.add(ModelHelper.createQuad(SWU, SED, NED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SED, NED, SWU, 0, 16, 0, 16, false, overlayIndex, 4);
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, SWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NED, NWD, SWU, NED, 0, 16, 16, 0, true, overlayIndex, 4);
                        }
                    }
                    break;
                case INNER_LEFT:
                    switch (direction) {
                        case NORTH -> {
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NED, NWD, NWU, 0, 16, 0, 16, false, overlayIndex, 0);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NEU, SED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SED, NED, NEU, SED, 0, 16, 16, 0, true, overlayIndex, 1);
                            //top face
                            quads.add(ModelHelper.createQuad(SED, NEU, NWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SED, NEU, NWU, SED, 16, 0, 16, 0, false, overlayIndex, 4);

                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NWD, SWD, SWU, 0, 16, 0, 16, false, overlayIndex, 1);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SWD, SED, SWU, 0, 16, 0, 16, false, overlayIndex, 2);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NWU, SWU, SED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NWU, SWU, SED, NWU, 16, 0, 0, 16, true, overlayIndex, 4);
                        }
                        case EAST -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SED, NED, NEU, 0, 16, 0, 16, false, overlayIndex, 1);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SWD, SED, SEU, SWD, 0, 16, 16, 0, true, overlayIndex, 2);
                            //top face
                            quads.add(ModelHelper.createQuad(SWD, SEU, NEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SWD, SEU, NEU, SWD, 16, 0, 16, 0, false, overlayIndex, 4);

                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NED, NWD, NWU, 0, 16, 0, 16, false, overlayIndex, 0);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NWD, SWD, NWU, 0, 16, 0, 16, false, overlayIndex, 3);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NEU, NWU, SWD, NEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NEU, NWU, SWD, NEU, 16, 0, 0, 16, true, overlayIndex, 4);
                        }
                        case SOUTH -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SWD, SED, SEU, 0, 16, 0, 16, false, overlayIndex, 2);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SWU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NWD, SWD, SWU, NWD, 0, 16, 16, 0, true, overlayIndex, 3);
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, SWU, SEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NWD, SWU, SEU, NWD, 16, 0, 16, 0, false, overlayIndex, 4);

                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SED, NED, NEU, 0, 16, 0, 16, false, overlayIndex, 1);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWD, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NED, NWD, NWD, 0, 16, 0, 16, false, overlayIndex, 0);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SEU, NEU, NWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SEU, NEU, NWD, SEU, 16, 0, 0, 16, true, overlayIndex, 4);
                        }
                        case WEST -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NWD, SWD, SWU, 0, 16, 0, 16, false, overlayIndex, 3);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NED, NWD, NWU, NED, 0, 16, 16, 0, true, overlayIndex, 0);
                            //top face
                            quads.add(ModelHelper.createQuad(NED, NWU, SWU, NED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NED, NWU, SWU, NED, 16, 0, 16, 0, false, overlayIndex, 4);

                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SWD, SED, SEU, 0, 16, 0, 16, false, overlayIndex, 2);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SED, NED, SEU, 0, 16, 0, 16, false, overlayIndex, 1);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SWU, SEU, NED, SWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SWU, SEU, NED, SWU, 16, 0, 0, 16, true, overlayIndex, 4);
                        }
                    }
                    break;
                case INNER_RIGHT:
                    switch (direction) {
                        case WEST -> {
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NED, NWD, NWU, 0, 16, 0, 16, false, overlayIndex, 0);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NEU, SED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SED, NED, NEU, SED, 0, 16, 16, 0, true, overlayIndex, 1);
                            //top face
                            quads.add(ModelHelper.createQuad(SED, NEU, NWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SED, NEU, NWU, SED, 16, 0, 16, 0, false, overlayIndex, 4);

                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NWD, SWD, SWU, 0, 16, 0, 16, false, overlayIndex, 3);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SWD, SED, SWU, 0, 16, 0, 16, false, overlayIndex, 2);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NWU, SWU, SED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NWU, SWU, SED, NWU, 16, 0, 0, 16, true, overlayIndex, 4);
                        }
                        case NORTH -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SED, NED, NEU, 0, 16, 0, 16, false, overlayIndex, 1);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SWD, SED, SEU, SWD, 0, 16, 16, 0, true, overlayIndex, 2);
                            //top face
                            quads.add(ModelHelper.createQuad(SWD, SEU, NEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SWD, SEU, NEU, SWD, 16, 0, 16, 0, false, overlayIndex, 4);

                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NED, NWD, NWU, 0, 16, 0, 16, false, overlayIndex, 0);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NWD, SWD, NWU, 0, 16, 0, 16, false, overlayIndex, 3);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NEU, NWU, SWD, NEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NEU, NWU, SWD, NEU, 16, 0, 0, 16, true, overlayIndex, 4);
                        }
                        case EAST -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SWD, SED, SEU, 0, 16, 0, 16, false, overlayIndex, 2);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SWU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NWD, SWD, SWU, NWD, 0, 16, 16, 0, true, overlayIndex, 3);
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, SWU, SEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NWD, SWU, SEU, NWD, 16, 0, 16, 0, false, overlayIndex, 4);

                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SED, NED, NEU, 0, 16, 0, 16, false, overlayIndex, 1);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWD, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NED, NWD, NWD, 0, 16, 0, 16, false, overlayIndex, 0);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SEU, NEU, NWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SEU, NEU, NWD, SEU, 16, 0, 0, 16, true, overlayIndex, 4);
                        }
                        case SOUTH -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NWD, SWD, SWU, 0, 16, 0, 16, false, overlayIndex, 3);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NED, NWD, NWU, NED, 0, 16, 16, 0, true, overlayIndex, 0);
                            //top face
                            quads.add(ModelHelper.createQuad(NED, NWU, SWU, NED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NED, NWU, SWU, NED, 16, 0, 16, 0, false, overlayIndex, 4);

                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SWD, SED, SEU, 0, 16, 0, 16, false, overlayIndex, 2);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SED, NED, SEU, 0, 16, 0, 16, false, overlayIndex, 1);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SWU, SEU, NED, SWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SWU, SEU, NED, SWU, 16, 0, 0, 16, true, overlayIndex, 4);
                        }
                    }
                    break;
            }
        }
        else {
            //top face
            quads.add(ModelHelper.createQuad(NWU, SWU, SEU, NEU, texture, 0, 16, 0, 16, tintIndex));
            o(quads, NWU, SWU, SEU, NEU, 0, 16, 0, 16, false, overlayIndex, 4);
            switch (shape) {
                case STRAIGHT:
                    switch (direction) {
                        case NORTH -> {
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NED, NWD, NWU, 0, 16, 0, 16, false, overlayIndex, 0);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NED, NEU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NED, NEU, SEU, NED, 16, 0, 16, 0, false, overlayIndex, 1);
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NWD, SWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SWU, NWU, NWD, SWU, 16, 0, 0, 16, true, overlayIndex, 3);
                            //top face
                            quads.add(ModelHelper.createQuad(NED, SEU, SWU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NED, SEU, SWU, NWD, 16, 0, 16, 0, false, overlayIndex, 0);
                        }
                        case EAST -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SED, NED, NEU, 0, 16, 0, 16, false, overlayIndex, 1);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SED, SEU, SWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SED, SEU, SWU, SED, 16, 0, 16, 0, false, overlayIndex, 2);
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, NED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NWU, NEU, NED, NWU, 16, 0, 0, 16, true, overlayIndex, 4);
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, NED, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NED, SED, SWU, 0, 16, 0, 16, false, overlayIndex, 0);
                        }
                        case SOUTH -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SWD, SED, SEU, 0, 16, 0, 16, false, overlayIndex, 2);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SWD, SWU, NWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SWD, SWU, NWU, SWD, 16, 0, 16, 0, false, overlayIndex, 3);
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SED, NEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NEU, SEU, SED, NEU, 16, 0, 0, 16, true, overlayIndex, 1);
                            //top face
                            quads.add(ModelHelper.createQuad(NEU, SED, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, SED, SWD, NWU, 0, 16, 0, 16, false, overlayIndex, 0);
                        }
                        case WEST -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NWD, SWD, SWU, 0, 16, 0, 16, false, overlayIndex, 3);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NWD, NWU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NWD, NWU, NEU, NWD, 16, 0, 16, 0, false, overlayIndex, 0);
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, SWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SEU, SWU, SWD, SEU, 16, 0, 0, 16, true, overlayIndex, 2);
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, NEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NWD, NEU, SEU, SWD, 16, 0, 16, 0, false, overlayIndex, 0);
                        }
                    }
                    break;
                case OUTER_LEFT:
                    switch (direction) {
                        case NORTH -> {
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWD, NWU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NWD, NWU, NEU, NWD, 16, 0, 16, 0, false, overlayIndex, 0);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NWD, SWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SWU, NWU, NWD, SWU, 16, 0, 0, 16, true, overlayIndex, 3);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, NWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SEU, SWU, NWD, SEU, 16, 0, 0, 16, true, overlayIndex, 2);
                            quads.add(ModelHelper.createQuad(NWD, NEU, SEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NWD, NEU, SEU, NWD, 16, 0, 16, 0, false, overlayIndex, 0);
                        }
                        case EAST -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NED, NEU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NED, NEU, SEU, NED, 16, 0, 16, 0, false, overlayIndex, 1);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, NED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NWU, NEU, NED, NWU, 16, 0, 0, 16, true, overlayIndex, 0);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NED, SWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SWU, NWU, NED, SWU, 16, 0, 0, 16, true, overlayIndex, 3);
                            quads.add(ModelHelper.createQuad(NED, SEU, SWU, NED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NED, SEU, SWU, NED, 16, 0, 16, 0, false, overlayIndex, 1);
                        }
                        case SOUTH -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SED, SEU, SWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SED, SEU, SWU, SED, 16, 0, 16, 0, false, overlayIndex, 2);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SED, NEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NEU, SEU, SED, NEU, 16, 0, 0, 16, true, overlayIndex, 1);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, SED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NWU, NEU, SED, NWU, 16, 0, 0, 16, true, overlayIndex, 0);
                            quads.add(ModelHelper.createQuad(SED, SWU, NWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SED, SWU, NWU, SED, 16, 0, 16, 0, false, overlayIndex, 2);
                        }
                        case WEST -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWD, SWU, NWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SWD, SWU, NWU, SWD, 16, 0, 16, 0, false, overlayIndex, 3);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, SWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SEU, SWU, SWD, SEU, 16, 0, 0, 16, true, overlayIndex, 2);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SWD, NEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NEU, SEU, SWD, NEU, 16, 0, 0, 16, true, overlayIndex, 1);
                            quads.add(ModelHelper.createQuad(SWD, NWU, NEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SWD, NWU, NEU, SWD, 16, 0, 16, 0, false, overlayIndex, 3);
                        }
                    }
                    break;
                case OUTER_RIGHT:
                    switch (direction) {
                        case WEST -> {
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWD, NWU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NWD, NWU, NEU, NWD, 16, 0, 16, 0, false, overlayIndex, 0);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NWD, SWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SWU, NWU, NWD, SWU, 16, 0, 0, 16, true, overlayIndex, 3);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, NWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SEU, SWU, NWD, SEU, 16, 0, 0, 16, true, overlayIndex, 2);
                            quads.add(ModelHelper.createQuad(NWD, NEU, SEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NWD, NEU, SEU, NWD, 16, 0, 16, 0, false, overlayIndex, 0);
                        }
                        case NORTH -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NED, NEU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NED, NEU, SEU, NED, 16, 0, 16, 0, false, overlayIndex, 1);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, NED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NWU, NEU, NED, NWU, 16, 0, 0, 16, true, overlayIndex, 0);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NED, SWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SWU, NWU, NED, SWU, 16, 0, 0, 16, true, overlayIndex, 3);
                            quads.add(ModelHelper.createQuad(NED, SEU, SWU, NED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NED, SEU, SWU, NED, 16, 0, 16, 0, false, overlayIndex, 1);
                        }
                        case EAST -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SED, SEU, SWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SED, SEU, SWU, SED, 16, 0, 16, 0, false, overlayIndex, 2);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SED, NEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NEU, SEU, SED, NEU, 16, 0, 0, 16, true, overlayIndex, 1);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, SED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NWU, NEU, SED, NWU, 16, 0, 0, 16, true, overlayIndex, 0);
                            quads.add(ModelHelper.createQuad(SED, SWU, NWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SED, SWU, NWU, SED, 16, 0, 16, 0, false, overlayIndex, 2);
                        }
                        case SOUTH -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWD, SWU, NWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SWD, SWU, NWU, SWD, 16, 0, 16, 0, false, overlayIndex, 3);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, SWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SEU, SWU, SWD, SEU, 16, 0, 0, 16, true, overlayIndex, 2);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SWD, NEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NEU, SEU, SWD, NEU, 16, 0, 0, 16, true, overlayIndex, 1);
                            quads.add(ModelHelper.createQuad(SWD, NWU, NEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SWD, NWU, NEU, SWD, 16, 0, 16, 0, false, overlayIndex, 3);
                        }
                    }
                    break;
                case INNER_LEFT:
                    switch (direction) {
                        case NORTH -> {
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NED, NWD, NWU, 0, 16, 0, 16, false, overlayIndex, 0);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NED, NEU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NED, NEU, SEU, NED, 16, 0, 16, 0, false, overlayIndex, 1);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NWD, NED, SEU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NWD, NED, SEU, NWD, 0, 16, 16, 0, true, overlayIndex, 0);

                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NWD, SWD, SWU, 0, 16, 0, 16, false, overlayIndex, 3);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, SWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SEU, SWU, SWD, SEU, 16, 0, 0, 16, true, overlayIndex, 2);
                            //top face
                            quads.add(ModelHelper.createQuad(SEU, SWD, NWD, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SWD, NWD, SEU, 0, 16, 0, 16, false, overlayIndex, 2);
                        }
                        case EAST -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SED, NED, NEU, 0, 16, 0, 16, false, overlayIndex, 1);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SED, SEU, SWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SED, SEU, SWU, SED, 16, 0, 16, 0, false, overlayIndex, 2);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NED, SED, SWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NED, SED, SWU, NED, 0, 16, 16, 0, true, overlayIndex, 1);

                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NED, NWD, NWU, 0, 16, 0, 16, false, overlayIndex, 0);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NWD, SWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SWU, NWU, NWD, SWU, 16, 0, 0, 16, true, overlayIndex, 3);
                            //top face
                            quads.add(ModelHelper.createQuad(SWU, NWD, NED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, NWD, NED, SWU, 0, 16, 0, 16, false, overlayIndex, 3);
                        }
                        case SOUTH -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SWD, SED, SEU, 0, 16, 0, 16, false, overlayIndex, 2);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SWD, SWU, NWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SWD, SWU, NWU, SWD, 16, 0, 16, 0, false, overlayIndex, 3);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SED, SWD, NWU, SED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SED, SWD, NWU, SED, 0, 16, 16, 0, true, overlayIndex, 2);

                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SED, NED, NEU, 0, 16, 0, 16, false, overlayIndex, 1);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, NED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NWU, NEU, NED, NWU, 16, 0, 0, 16, true, overlayIndex, 0);
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, NED, SED, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NED, SED, NWU, 0, 16, 0, 16, false, overlayIndex, 0);
                        }
                        case WEST -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NWD, SWD, SWU, 0, 16, 0, 16, false, overlayIndex, 3);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NWD, NWU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NWD, NWU, NEU, NWD, 16, 0, 16, 0, false, overlayIndex, 0);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SWD, NWD, NEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SWD, NWD, NEU, SWD, 0, 16, 16, 0, true, overlayIndex, 3);

                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SWD, SED, SEU, 0, 16, 0, 16, false, overlayIndex, 2);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SED, NEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NEU, SEU, SED, NEU, 16, 0, 0, 16, true, overlayIndex, 1);
                            //top face
                            quads.add(ModelHelper.createQuad(NEU, SED, SWD, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, SED, SWD, NEU, 0, 16, 0, 16, false, overlayIndex, 1);
                        }
                    }
                    break;
                case INNER_RIGHT:
                    switch (direction) {
                        case WEST -> {
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NED, NWD, NWU, 0, 16, 0, 16, false, overlayIndex, 0);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NED, NEU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NED, NEU, SEU, NED, 16, 0, 16, 0, false, overlayIndex, 1);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NWD, NED, SEU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NWD, NED, SEU, NWD, 0, 16, 16, 0, true, overlayIndex, 0);

                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NWD, SWD, SWU, 0, 16, 0, 16, false, overlayIndex, 3);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, SWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SEU, SWU, SWD, SEU, 16, 0, 0, 16, true, overlayIndex, 2);
                            //top face
                            quads.add(ModelHelper.createQuad(SEU, SWD, NWD, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SWD, NWD, SEU, 0, 16, 0, 16, false, overlayIndex, 2);
                        }
                        case NORTH -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SED, NED, NEU, 0, 16, 0, 16, false, overlayIndex, 1);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SED, SEU, SWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SED, SEU, SWU, SED, 16, 0, 16, 0, false, overlayIndex, 2);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NED, SED, SWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, NED, SED, SWU, NED, 0, 16, 16, 0, true, overlayIndex, 1);

                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, NED, NWD, NWU, 0, 16, 0, 16, false, overlayIndex, 0);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NWD, SWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, SWU, NWU, NWD, SWU, 16, 0, 0, 16, true, overlayIndex, 3);
                            //top face
                            quads.add(ModelHelper.createQuad(SWU, NWD, NED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, NWD, NED, SWU, 0, 16, 0, 16, false, overlayIndex, 3);
                        }
                        case EAST -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SWD, SED, SEU, 0, 16, 0, 16, false, overlayIndex, 2);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SWD, SWU, NWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, SWD, SWU, NWU, SWD, 16, 0, 16, 0, false, overlayIndex, 3);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SED, SWD, NWU, SED, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SED, SWD, NWU, SED, 0, 16, 16, 0, true, overlayIndex, 2);

                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SEU, SED, NED, NEU, 0, 16, 0, 16, false, overlayIndex, 1);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, NED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NWU, NEU, NED, NWU, 16, 0, 0, 16, true, overlayIndex, 0);
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, NED, SED, NWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NED, SED, NWU, 0, 16, 0, 16, false, overlayIndex, 0);
                        }
                        case SOUTH -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NWU, NWD, SWD, SWU, 0, 16, 0, 16, false, overlayIndex, 3);
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NWD, NWU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            o(quads, NWD, NWU, NEU, NWD, 16, 0, 16, 0, false, overlayIndex, 0);
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SWD, NWD, NEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            o(quads, SWD, NWD, NEU, SWD, 0, 16, 16, 0, true, overlayIndex, 3);

                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, SWU, SWD, SED, SEU, 0, 16, 0, 16, false, overlayIndex, 2);
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SED, NEU, texture, 16, 0, 0, 16, tintIndex));
                            o(quads, NEU, SEU, SED, NEU, 16, 0, 0, 16, true, overlayIndex, 1);
                            //top face
                            quads.add(ModelHelper.createQuad(NEU, SED, SWD, NEU, texture, 0, 16, 0, 16, tintIndex));
                            o(quads, NEU, SED, SWD, NEU, 0, 16, 0, 16, false, overlayIndex, 1);
                        }
                    }
                    break;
            }
        }
        return quads;
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
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/oak_planks"));
    }

    @Override
    @Nonnull
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}
//========SOLI DEO GLORIA========//