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
 * @version 1.0 05/23/22
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
            //model.getBakedModel().getQuads(mimic, side, rand, extraData);
            //only if model (from block saved in tile entity) exists:
            return getMimicQuads(state, side, rand, extraData, model);
        }
        return Collections.emptyList();
    }

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, BakedModel model) {
        List<BakedQuad> quads = new ArrayList<>();
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        List<TextureAtlasSprite> texture = TextureHelper.getTextureFromModel(model, extraData, rand);
        int index = extraData.getData(FrameBlockTile.TEXTURE);
        if (index >= texture.size()) {
            index = 0;
        }
        if (texture.size() == 0) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.block_not_available"), true);
            }
            return Collections.emptyList();
        }
        //TODO Remove when slopes are fixed
        /*if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("We're sorry, but Slopes do not work at the moment"), true);
        }*/
        int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
        double w = 0.5;
        if (state.getValue(StairBlock.HALF) == net.minecraft.world.level.block.state.properties.Half.TOP) {
            w = -0.5;
        }
        //Eight corners of the block
        Vec3 NWU = v(0, 0.5 + w, 0); //North-West-Up
        Vec3 NEU = v(1, 0.5 + w, 0); //...
        Vec3 NWD = v(0, 0.5 - w, 0);
        Vec3 NED = v(1, 0.5 - w, 0);
        Vec3 SWU = v(0, 0.5 + w, 1);
        Vec3 SEU = v(1, 0.5 + w, 1);
        Vec3 SWD = v(0, 0.5 - w, 1);
        Vec3 SED = v(1, 0.5 - w, 1); //South-East-Down
        //bottom face
        /*quads.add(ModelHelper.createQuad(SED, SWD, NWD, NED, texture.get(index), 0, 16, 0, 16, tintIndex));
        switch (state.getValue(StairBlock.FACING)) {
            case NORTH:
                SWU = v(0,0.5-w,1);
                SEU = v(1,0.5-w,1);
                SWD = v(0,0.5+w,1);
                SED = v(1,0.5+w,1);
                //back face
                quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture.get(index), 0, 16, 0, 16, tintIndex));

                quads.add(ModelHelper.createQuad(SWU, NWU, NWD, SWU, texture.get(index), 0, 16, 0, 16, tintIndex));
                quads.add(ModelHelper.createQuad(NEU, SEU, NED, NEU, texture.get(index), 0, 16, 0, 16, tintIndex));


                break;
            case WEST:
                NED = v(1,0.5+w,0);
                NEU = v(1,0.5-w,0);
                SEU = v(1,0.5-w,1);
                SED = v(1,0.5+w,1);
                //back face
                quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture.get(index), 0, 16, 0, 16, tintIndex));
                break;
            case SOUTH:
                NWU = v(0,0.5-w,0);
                NEU = v(1,0.5-w,0);
                NWD = v(0,0.5+w,0);
                NED = v(1,0.5+w,0);
                //back face
                quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture.get(index), 0, 16, 0, 16,tintIndex));
                break;
            case EAST:
                SWU = v(0,0.5-w,1);
                SWD = v(0,0.5+w,1);
                NWU = v(0,0.5-w,0);
                NWD = v(0,0.5+w,0);
                //back face
                quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture.get(index), 0, 16, 0, 16, tintIndex));
                break;
        }
        //top face
        quads.add(ModelHelper.createQuad(SWU, SEU, NEU, NWU, texture.get(index), 0, 16, 0, 16, tintIndex));
        //quads.addAll(ModelHelper.createQuad(0,1,0,1,0,1, texture.get(index), tintIndex,state.getValue(StairBlock.FACING), state.getValue(StairBlock.HALF))); //TODO remove or fix
        */
        quads.addAll(createSlope(0, 1, 0, 1, 0, 1, texture.get(index), tintIndex, state.getValue(StairBlock.FACING), state.getValue(StairBlock.SHAPE), state.getValue(StairBlock.HALF)));
        return quads;
    }

    public List<BakedQuad> createSlope(float xl, float xh, float yl, float yh, float zl, float zh, TextureAtlasSprite texture, int tintIndex, Direction direction, StairsShape shape, Half half) {
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
        /*if (half == Half.TOP) {
            Vec3 tmp = NWD;
            NWD = NWU;
            NWU = tmp;

            tmp = SWD;
            SWD = SWU;
            SWU = tmp;

            tmp = NED;
            NED= NEU;
            NEU = tmp;

            tmp = SED;
            SED = SEU;
            SEU = tmp;
        }*/
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
                            quads.add(ModelHelper.createQuad(NEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, NWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, SWD, SED, NEU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case EAST -> {
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWD, NEU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(SED, SEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWD, SEU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case SOUTH -> {
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NED, SEU, SED, NED, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(SWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, SWU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case WEST -> {
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NED, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SWU, SWU, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWU, SED, NED, NWU, texture, 0, 16, 0, 16, tintIndex));
                        }
                    }
                    break;
                case OUTER_LEFT:
                    switch (direction) {
                        case NORTH -> {
                            //back face
                            quads.add(ModelHelper.createQuad(NED, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, NWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, NWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(NWU, SED, NED, NWU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case EAST -> {
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWD, NEU, texture, 0, 16, 16, 0, tintIndex));

                            //top faces
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, NEU, NEU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(SED, NEU, NEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case SOUTH -> {
                            //back face
                            quads.add(ModelHelper.createQuad(SED, SEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuadInverted(NED, SEU, SED, NED, texture, 16, 0, 0, 16, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWD, SEU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(SEU, NWD, SWD, SEU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case WEST -> {
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SWU, SWU, texture, 0, 16, 16, 0, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuadInverted(NED, SWU, SED, NED, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(NED, NED, NWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                        }
                    }
                    break;
                case OUTER_RIGHT:
                    switch (direction) {
                        case NORTH -> {
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWD, NEU, texture, 0, 16, 16, 0, tintIndex));

                            //top faces
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, NEU, NEU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(SED, NEU, NEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case EAST -> {
                            //back face
                            quads.add(ModelHelper.createQuad(SED, SEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuadInverted(NED, SEU, SED, NED, texture, 16, 0, 0, 16, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWD, SEU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(SEU, NWD, SWD, SEU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case SOUTH -> {
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SWU, SWU, texture, 0, 16, 16, 0, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuadInverted(NED, SWU, SED, NED, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(NED, NED, NWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case WEST -> {
                            //back face
                            quads.add(ModelHelper.createQuad(NED, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted face
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, NWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            //top faces
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, NWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(NWU, SED, NED, NWU, texture, 0, 16, 0, 16, tintIndex));
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
                            quads.add(ModelHelper.createQuad(NEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //quads.add(ModelHelper.createQuadInverted(NWD, SWD, NWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, SWD, SED, NEU, texture, 0, 16, 0, 16, tintIndex));

                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuad(NED, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SWU, SWU, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWU, SED, NED, NWU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case EAST -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuadInverted(NED, NWD, NWD, NEU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(SED, SEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWD, SEU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));

                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuad(NEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, NWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, SWD, SED, NEU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case SOUTH -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuadInverted(NED, SEU, SED, NED, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(SWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, SWU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));

                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWD, NEU, texture, 0, 16, 16, 0, tintIndex));
                            //quads.add(ModelHelper.createQuad(SED, SEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWD, SEU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case WEST -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NED, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //quads.add(ModelHelper.createQuadInverted(SWD, SED, SWU, SWU, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWU, SED, NED, NWU, texture, 0, 16, 0, 16, tintIndex));

                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NED, SEU, SED, NED, texture, 16, 0, 0, 16, tintIndex));
                            //quads.add(ModelHelper.createQuad(SWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, SWU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                        }
                    }
                    break;
                case INNER_RIGHT:
                    switch (direction) {
                        case NORTH -> {
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuad(NEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NWD, SWD, NWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, SWD, SED, NEU, texture, 0, 16, 0, 16, tintIndex));

                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuadInverted(NED, NWD, NWD, NEU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(SED, SEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWD, SEU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case EAST -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NED, NWD, NWD, NEU, texture, 0, 16, 16, 0, tintIndex));
                            //quads.add(ModelHelper.createQuad(SED, SEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWD, SEU, NEU, NWD, texture, 16, 0, 16, 0, tintIndex));

                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuadInverted(NED, SEU, SED, NED, texture, 16, 0, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(SWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, SWU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case SOUTH -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NED, SEU, SED, NED, texture, 16, 0, 0, 16, tintIndex));
                            //quads.add(ModelHelper.createQuad(SWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, SWU, SEU, NED, texture, 16, 0, 16, 0, tintIndex));

                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NED, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //quads.add(ModelHelper.createQuadInverted(SWD, SED, SWU, SWU, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWU, SED, NED, NWU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case WEST -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuad(NED, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SWD, SED, SWU, SWU, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWU, SED, NED, NWU, texture, 0, 16, 0, 16, tintIndex));

                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //quads.add(ModelHelper.createQuadInverted(NWD, SWD, NWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, SWD, SED, NEU, texture, 0, 16, 0, 16, tintIndex));
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
                            quads.add(ModelHelper.createQuadInverted(SEU, NED, NEU, SEU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(SWU, SWU, NWU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NED, SEU, SWU, NWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case EAST -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWU, SED, SEU, SWU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(NWU, NWU, NEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, NED, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case SOUTH -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWU, SWD, SWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(NEU, NEU, SEU, SED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NEU, SED, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case WEST -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NEU, NWD, NWU, NEU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(SEU, SEU, SWU, SWD, texture, 16, 0, 16, 0, tintIndex));
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
                            quads.add(ModelHelper.createQuadInverted(NEU, NWD, NWU, NEU, texture, 0, 16, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SWU, SWU, NWU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SEU, SEU, SWU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SEU, NWD, NEU, SEU, texture, 0, 16, 16, 0, tintIndex));
                        }
                        case EAST -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SEU, NED, NEU, SEU, texture, 0, 16, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NWU, NWU, NEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWU, SWU, NWU, NED, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SWU, NED, SEU, SWU, texture, 0, 16, 16, 0, tintIndex));
                        }
                        case SOUTH -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SWU, SED, SEU, SWU, texture, 0, 16, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NEU, NEU, SEU, SED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            //quads.add(ModelHelper.createQuad(NEU, SED, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(NWU, NWU, NEU, SED, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NWU, SED, SWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                        }
                        case WEST -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuadInverted(NWU, SWD, SWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SEU, SEU, SWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NEU, NEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NEU, SWD, NWU, NEU, texture, 0, 16, 16, 0, tintIndex));
                        }
                    }
                    break;
                case OUTER_RIGHT:
                    switch (direction) {
                        case NORTH -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SEU, NED, NEU, SEU, texture, 0, 16, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NWU, NWU, NEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SWU, SWU, NWU, NED, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SWU, NED, SEU, SWU, texture, 0, 16, 16, 0, tintIndex));
                        }
                        case EAST -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuadInverted(SWU, SED, SEU, SWU, texture, 0, 16, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(NEU, NEU, SEU, SED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            //quads.add(ModelHelper.createQuad(NEU, SED, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            quads.add(ModelHelper.createQuad(NWU, NWU, NEU, SED, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NWU, SED, SWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                        }
                        case SOUTH -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuadInverted(NWU, SWD, SWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SEU, SEU, SWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NEU, NEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(NEU, SWD, NWU, NEU, texture, 0, 16, 16, 0, tintIndex));
                        }
                        case WEST -> {
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuadInverted(NEU, NWD, NWU, NEU, texture, 0, 16, 16, 0, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuad(SWU, SWU, NWU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(SEU, SEU, SWU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuadInverted(SEU, NWD, NEU, SEU, texture, 0, 16, 16, 0, tintIndex));
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
                            quads.add(ModelHelper.createQuadInverted(SEU, NED, NEU, SEU, texture, 0, 16, 16, 0, tintIndex));
                            //quads.add(ModelHelper.createQuad(SWU, SWU, NWU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NED, SEU, SWU, NWD, texture, 16, 0, 16, 0, tintIndex));

                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuadInverted(NEU, NWD, NWU, NEU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(SEU, SEU, SWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, NEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case EAST -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWU, SED, SEU, SWU, texture, 0, 16, 16, 0, tintIndex));
                            //quads.add(ModelHelper.createQuad(NWU, NWU, NEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, NED, SED, SWU, texture, 0, 16, 0, 16, tintIndex));

                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuadInverted(SEU, NED, NEU, SEU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(SWU, SWU, NWU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NED, SEU, SWU, NWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case SOUTH -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWU, SWD, SWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            //quads.add(ModelHelper.createQuad(NEU, NEU, SEU, SED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NEU, SED, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));

                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuadInverted(SWU, SED, SEU, SWU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(NWU, NWU, NEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, NED, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case WEST -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NEU, NWD, NWU, NEU, texture, 0, 16, 16, 0, tintIndex));
                            //quads.add(ModelHelper.createQuad(SEU, SEU, SWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, NEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));

                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuadInverted(NWU, SWD, SWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(NEU, NEU, SEU, SED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NEU, SED, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                        }
                    }
                    break;
                case INNER_RIGHT:
                    switch (direction) {
                        case NORTH -> {
                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuadInverted(SEU, NED, NEU, SEU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(SWU, SWU, NWU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NED, SEU, SWU, NWD, texture, 16, 0, 16, 0, tintIndex));

                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SWU, SED, SEU, SWU, texture, 0, 16, 16, 0, tintIndex));
                            //quads.add(ModelHelper.createQuad(NWU, NWU, NEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, NED, SED, SWU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case EAST -> {
                            //EAST PART
                            //back face
                            quads.add(ModelHelper.createQuad(SEU, SED, NED, NEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuadInverted(SWU, SED, SEU, SWU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(NWU, NWU, NEU, NED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWU, NED, SED, SWU, texture, 0, 16, 0, 16, tintIndex));

                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NWU, SWD, SWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            //quads.add(ModelHelper.createQuad(NEU, NEU, SEU, SED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NEU, SED, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                        }
                        case SOUTH -> {
                            //SOUTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(SWU, SWD, SED, SEU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuadInverted(NWU, SWD, SWU, NWU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(NEU, NEU, SEU, SED, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NEU, SED, SWD, NWU, texture, 0, 16, 0, 16, tintIndex));

                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(NEU, NWD, NWU, NEU, texture, 0, 16, 16, 0, tintIndex));
                            //quads.add(ModelHelper.createQuad(SEU, SEU, SWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, NEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));
                        }
                        case WEST -> {
                            //WEST PART
                            //back face
                            quads.add(ModelHelper.createQuad(NWU, NWD, SWD, SWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            //quads.add(ModelHelper.createQuadInverted(NEU, NWD, NWU, NEU, texture, 0, 16, 16, 0, tintIndex));
                            quads.add(ModelHelper.createQuad(SEU, SEU, SWU, SWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NWD, NEU, SEU, SWD, texture, 16, 0, 16, 0, tintIndex));

                            //NORTH PART
                            //back face
                            quads.add(ModelHelper.createQuad(NEU, NED, NWD, NWU, texture, 0, 16, 0, 16, tintIndex));
                            //slanted faces
                            quads.add(ModelHelper.createQuadInverted(SEU, NED, NEU, SEU, texture, 0, 16, 16, 0, tintIndex));
                            //quads.add(ModelHelper.createQuad(SWU, SWU, NWU, NWD, texture, 16, 0, 16, 0, tintIndex));
                            //top face
                            quads.add(ModelHelper.createQuad(NED, SEU, SWU, NWD, texture, 16, 0, 16, 0, tintIndex));
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