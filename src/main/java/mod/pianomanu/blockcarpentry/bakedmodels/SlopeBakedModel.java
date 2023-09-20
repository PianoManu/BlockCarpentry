package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains all information for the block model
 * See {@link mod.pianomanu.blockcarpentry.util.ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.3 09/20/23
 */
public class SlopeBakedModel implements IDynamicBakedModel {
    private static Vec3 v(double x, double y, double z) {
        return new Vec3(x, y, z);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, RenderType renderType) {
        //get block saved in frame tile
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        if (mimic != null) {
            net.minecraft.client.resources.model.ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            //model.getBakedModel().getQuads(mimic, side, rand, extraData);
            //only if model (from block saved in tile entity) exists:
            return getMimicQuads(state, side, rand, extraData, model);
        }
        return Collections.emptyList();
    }

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, BakedModel model) {
        if (side != null)
            return new ArrayList<>();
        List<BakedQuad> quads = new ArrayList<>();
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        TextureAtlasSprite texture = QuadUtils.getTexture(model, rand, extraData, FrameBlockTile.TEXTURE);
        int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
        quads.addAll(createSlope(0, 1, 0, 1, 0, 1, texture, tintIndex, state.getValue(StairBlock.FACING), state.getValue(StairBlock.SHAPE), state.getValue(StairBlock.HALF)));
        return quads;
    }

    private List<BakedQuad> createSlope(float xl, float xh, float yl, float yh, float zl, float zh, TextureAtlasSprite texture, int tintIndex, Direction direction, StairsShape shape, Half half) {
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
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("An error occured with this block, please report to the mod author (PianoManu)"), true);
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
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NEU, SED, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, SWD, SED, NEU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case EAST -> {
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWD, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWD, SEU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case SOUTH -> {
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SWU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, SWU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case WEST -> {
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWU, SED, NED, NWU, texture, 0, 16, 0, 16, tintIndex));
                        }
                    }
                    break;
                case OUTER_LEFT:
                    switch (direction) {
                        case NORTH -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuad(NWU, SWD, SED, NWU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NWU, SED, texture, 0, 16, 16, 0, tintIndex));
                        }
                        case EAST -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NEU, SED, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NEU, texture, 0, 16, 0, 16, tintIndex));

                            //top faces
                            quads.add(ModelHelper.createQuad(NEU, NWD, SWD, NEU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, NEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                        }
                        case SOUTH -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuad(SEU, NED, NWD, SEU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SEU, NWD, texture, 0, 16, 16, 0, tintIndex));
                        }
                        case WEST -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SWU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuad(SWU, SED, NED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, SWU, NED, texture, 0, 16, 16, 0, tintIndex));
                        }
                    }
                    break;
                case OUTER_RIGHT:
                    switch (direction) {
                        case WEST -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWU, NED, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuad(NWU, SWD, SED, NWU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NWU, SED, texture, 0, 16, 16, 0, tintIndex));
                        }
                        case NORTH -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SED, NED, NEU, SED, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NEU, texture, 0, 16, 0, 16, tintIndex));

                            //top faces
                            quads.add(ModelHelper.createQuad(NEU, NWD, SWD, NEU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, NEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                        }
                        case EAST -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SEU, SWD, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuad(SEU, NED, NWD, SEU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SEU, NWD, texture, 0, 16, 16, 0, tintIndex));
                        }
                        case SOUTH -> {
                            //back face
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, SWU, NWD, texture, 0, 16, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuad(SWU, SED, NED, SWU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, SWU, NED, texture, 0, 16, 16, 0, tintIndex));
                        }
                    }
                    break;
                case INNER_LEFT:
                    switch (direction) {
                        case NORTH -> {
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
                        }
                        case EAST -> {
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
                        }
                        case SOUTH -> {
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
                        }
                        case WEST -> {
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
                        }
                    }
                    break;
                case INNER_RIGHT:
                    switch (direction) {
                        case WEST -> {
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
                        }
                        case NORTH -> {
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
                        }
                        case EAST -> {
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
                        }
                        case SOUTH -> {
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
                        }
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
                        case NORTH -> {
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NED, NEU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NWD, SWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NED, SEU, SWU, NWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case EAST -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SED, SEU, SWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, NED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, NED, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case SOUTH -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SWD, SWU, NWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SED, NEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NEU, SED, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case WEST -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NWD, NWU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, SWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, NEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                    }
                    break;
                case OUTER_LEFT:
                    switch (direction) {
                        case NORTH -> {
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWD, NWU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NWD, SWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, NWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(NWD, NEU, SEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case EAST -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NED, NEU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, NED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NED, SWU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(NED, SEU, SWU, NED, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case SOUTH -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SED, SEU, SWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SED, NEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, SED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(SED, SWU, NWU, SED, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case WEST -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWD, SWU, NWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, SWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SWD, NEU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(SWD, NWU, NEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                    }
                    break;
                case OUTER_RIGHT:
                    switch (direction) {
                        case WEST -> {
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWD, NWU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NWD, SWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, NWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(NWD, NEU, SEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case NORTH -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NED, NEU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, NED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(SWU, NWU, NED, SWU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(NED, SEU, SWU, NED, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case EAST -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SED, SEU, SWU, SED, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SED, NEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            //quads.add(ModelHelper.createQuad(NEU, SED, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NWU, NEU, SED, NWU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(SED, SWU, NWU, SED, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case SOUTH -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWD, SWU, NWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SEU, SWU, SWD, SEU, texture, 16, 0, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuadInverted(NEU, SEU, SWD, NEU, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(SWD, NWU, NEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                    }
                    break;
                case INNER_LEFT:
                    switch (direction) {
                        case NORTH -> {
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
                        }
                        case EAST -> {
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
                        }
                        case SOUTH -> {
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
                        }
                        case WEST -> {
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
                        }
                    }
                    break;
                case INNER_RIGHT:
                    switch (direction) {
                        case WEST -> {
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
                        }
                        case NORTH -> {
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
                        }
                        case EAST -> {
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
                        }
                        case SOUTH -> {
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

    @Override
    @NotNull
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return ChunkRenderTypeSet.of(RenderType.translucent());
    }
}
//========SOLI DEO GLORIA========//