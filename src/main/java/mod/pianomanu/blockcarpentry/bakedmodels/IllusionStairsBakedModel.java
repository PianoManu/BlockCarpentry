package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
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
 * @version 1.4 05/01/21
 */
public class IllusionStairsBakedModel implements IDynamicBakedModel {
    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && !(mimic.getBlock() instanceof FrameBlock)) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            if (location != null) {
                BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                if (model != null) {
                    return getIllusionQuads(state, side, rand, extraData, model);
                }
            }
        }
        return Collections.emptyList();
    }

    private List<BakedQuad> getIllusionQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, BakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && state != null) {
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            int rotation = extraData.getData(FrameBlockTile.ROTATION);
            float yl = 0f;
            float yh = 0.5f;
            boolean cullUpDown = false;
            if (state.getValue(StairBlock.HALF).equals(Half.TOP)) {
                yl = 0.5f;
                yh = 1f;
                cullUpDown = true;
            }
            List<BakedQuad> quads = new ArrayList<>();
            switch (state.getValue(StairBlock.SHAPE)) {
                case STRAIGHT:
                    switch (state.getValue(StairBlock.FACING)) {
                        case NORTH:
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                        case SOUTH:
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                        case WEST:
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                        case EAST:
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                    }
                    break;
                case INNER_LEFT:
                    switch (state.getValue(StairBlock.FACING)) {
                        case NORTH:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, false, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                        case SOUTH:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, false, true, !cullUpDown, cullUpDown, rotation));
                            break;
                        case WEST:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, false, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                        case EAST:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, false, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, !cullUpDown, cullUpDown, rotation));
                            break;
                    }
                    break;
                case INNER_RIGHT:
                    switch (state.getValue(StairBlock.FACING)) {
                        case WEST:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, false, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                        case EAST:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, false, true, !cullUpDown, cullUpDown, rotation));
                            break;
                        case SOUTH:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, false, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                        case NORTH:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, false, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, !cullUpDown, cullUpDown, rotation));
                            break;
                    }
                    break;
                case OUTER_LEFT:
                    switch (state.getValue(StairBlock.FACING)) {
                        case NORTH:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                        case SOUTH:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                        case WEST:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                        case EAST:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                    }
                    break;
                case OUTER_RIGHT:
                    switch (state.getValue(StairBlock.FACING)) {
                        case WEST:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                        case EAST:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                        case SOUTH:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                        case NORTH:
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                            break;
                    }
                    break;
            }
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                switch (state.getValue(StairBlock.SHAPE)) {
                    case STRAIGHT:
                        switch (state.getValue(StairBlock.FACING)) {
                            case NORTH:
                                quads.addAll(ModelHelper.createOverlay(0f, 1f, yl, yh, 0f, 0.5f, overlayIndex, true, true, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 1f, yl, yh, 0.5f, 1f, overlayIndex, true, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                                break;
                            case SOUTH:
                                quads.addAll(ModelHelper.createOverlay(0f, 1f, yl, yh, 0f, 0.5f, overlayIndex, true, true, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 1f, yl, yh, 0.5f, 1f, overlayIndex, true, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                                break;
                            case WEST:
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 1f, overlayIndex, true, false, true, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 1f, overlayIndex, false, true, true, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 1f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                                break;
                            case EAST:
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 1f, overlayIndex, true, false, true, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 1f, overlayIndex, false, true, true, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 1f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                                break;
                        }
                        break;
                    case INNER_LEFT:
                        switch (state.getValue(StairBlock.FACING)) {
                            case NORTH:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0f, 0.5f, overlayIndex, true, false, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f, 1f, 0f, 0.5f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                                break;
                            case SOUTH:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f,1f, 0.5f, 1f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f,1f, 0.5f, 1f, overlayIndex, false, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f,1f, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                                break;
                            case WEST:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f,1f, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f,1f, 0.5f, 1f, overlayIndex, true, false, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f,1f, 0.5f, 1f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                                break;
                            case EAST:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f,1f, 0f, 0.5f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f,1f, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f,1f, 0f, 0.5f, overlayIndex, false, true, false, true, true, cullUpDown, false));
                                break;
                        }
                        break;
                    case INNER_RIGHT:
                        switch (state.getValue(StairBlock.FACING)) {
                            case WEST:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0f, 0.5f, overlayIndex, true, false, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f, 1f, 0f, 0.5f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                                break;
                            case EAST:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f,1f, 0.5f, 1f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f,1f, 0.5f, 1f, overlayIndex, false, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f,1f, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                                break;
                            case SOUTH:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f,1f, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f,1f, 0.5f, 1f, overlayIndex, true, false, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f,1f, 0.5f, 1f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                                break;
                            case NORTH:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f,1f, 0f, 0.5f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f,1f, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f,1f, 0f, 0.5f, overlayIndex, false, true, false, true, true, cullUpDown, false));
                                break;
                        }
                        break;
                    case OUTER_LEFT:
                        switch (state.getValue(StairBlock.FACING)) {
                            case NORTH:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                                break;
                            case SOUTH:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                                break;
                            case WEST:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                                break;
                            case EAST:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                                break;
                        }
                        break;
                    case OUTER_RIGHT:
                        switch (state.getValue(StairBlock.FACING)) {
                            case WEST:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                                break;
                            case EAST:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                                break;
                            case SOUTH:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                                break;
                            case NORTH:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                                break;
                        }
                        break;
                }
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
    public TextureAtlasSprite getParticleIcon() {
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation("minecraft", "block/oak_planks"));
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}
//========SOLI DEO GLORIA========//