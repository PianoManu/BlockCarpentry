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
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
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
 * @version 1.2 11/07/22
 */
public class IllusionStairsBakedModel implements IDynamicBakedModel {
    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, RenderType renderType) {
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getIllusionQuads(state, side, rand, extraData, model);
        }
        return Collections.emptyList();
    }

    private List<BakedQuad> getIllusionQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, BakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        if (mimic != null && state != null) {
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            int rotation = extraData.get(FrameBlockTile.ROTATION);
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
                        case NORTH -> {
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                        }
                        case SOUTH -> {
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                        }
                        case WEST -> {
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 1f, mimic, model, extraData, rand, tintIndex, true, true, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                        }
                        case EAST -> {
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 1f, mimic, model, extraData, rand, tintIndex, true, true, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                        }
                    }
                    break;
                case INNER_LEFT:
                    switch (state.getValue(StairBlock.FACING)) {
                        case NORTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, true, false, !cullUpDown, cullUpDown, rotation));
                        }
                        case SOUTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, false, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, true, !cullUpDown, cullUpDown, rotation));
                        }
                        case WEST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, false, !cullUpDown, cullUpDown, rotation));
                        }
                        case EAST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, false, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, !cullUpDown, cullUpDown, rotation));
                        }
                    }
                    break;
                case INNER_RIGHT:
                    switch (state.getValue(StairBlock.FACING)) {
                        case WEST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, true, false, !cullUpDown, cullUpDown, rotation));
                        }
                        case EAST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, false, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, true, !cullUpDown, cullUpDown, rotation));
                        }
                        case SOUTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, false, !cullUpDown, cullUpDown, rotation));
                        }
                        case NORTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, false, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, true, !cullUpDown, cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, !cullUpDown, cullUpDown, rotation));
                        }
                    }
                    break;
                case OUTER_LEFT:
                    switch (state.getValue(StairBlock.FACING)) {
                        case NORTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                        }
                        case SOUTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                        }
                        case WEST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                        }
                        case EAST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                        }
                    }
                    break;
                case OUTER_RIGHT:
                    switch (state.getValue(StairBlock.FACING)) {
                        case WEST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                        }
                        case EAST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                        }
                        case SOUTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, cullUpDown, !cullUpDown, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, true, true, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                        }
                        case NORTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, false, true, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, mimic, model, extraData, rand, tintIndex, false, true, true, false, true, true, rotation));
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, false, true, false, cullUpDown, !cullUpDown, rotation));
                            //upper part
                            quads.addAll(ModelHelper.createSixFaceCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, mimic, model, extraData, rand, tintIndex, true, true, true, true, !cullUpDown, cullUpDown, rotation));
                        }
                    }
                    break;
            }
            int overlayIndex = extraData.get(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                switch (state.getValue(StairBlock.SHAPE)) {
                    case STRAIGHT:
                        switch (state.getValue(StairBlock.FACING)) {
                            case NORTH -> {
                                quads.addAll(ModelHelper.createOverlay(0f, 1f, yl, yh, 0f, 0.5f, overlayIndex, true, true, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 1f, yl, yh, 0.5f, 1f, overlayIndex, true, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                            }
                            case SOUTH -> {
                                quads.addAll(ModelHelper.createOverlay(0f, 1f, yl, yh, 0f, 0.5f, overlayIndex, true, true, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 1f, yl, yh, 0.5f, 1f, overlayIndex, true, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                            }
                            case WEST -> {
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 1f, overlayIndex, true, false, true, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 1f, overlayIndex, false, true, true, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 1f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                            }
                            case EAST -> {
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 1f, overlayIndex, true, false, true, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 1f, overlayIndex, false, true, true, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 1f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                            }
                        }
                        break;
                    case INNER_LEFT:
                        switch (state.getValue(StairBlock.FACING)) {
                            case NORTH -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0f, 0.5f, overlayIndex, true, false, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f, 1f, 0f, 0.5f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                            }
                            case SOUTH -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0.5f, 1f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f, 1f, 0.5f, 1f, overlayIndex, false, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f, 1f, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                            }
                            case WEST -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0.5f, 1f, overlayIndex, true, false, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f, 1f, 0.5f, 1f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                            }
                            case EAST -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0f, 0.5f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f, 1f, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f, 1f, 0f, 0.5f, overlayIndex, false, true, false, true, true, cullUpDown, false));
                            }
                        }
                        break;
                    case INNER_RIGHT:
                        switch (state.getValue(StairBlock.FACING)) {
                            case WEST -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0f, 0.5f, overlayIndex, true, false, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f, 1f, 0f, 0.5f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                            }
                            case EAST -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0.5f, 1f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f, 1f, 0.5f, 1f, overlayIndex, false, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f, 1f, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                            }
                            case SOUTH -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0.5f, 1f, overlayIndex, true, false, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f, 1f, 0.5f, 1f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                            }
                            case NORTH -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 0.5f, 1f, 0f, 0.5f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f, 1f, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 0.5f, 1f, 0f, 0.5f, overlayIndex, false, true, false, true, true, cullUpDown, false));
                            }
                        }
                        break;
                    case OUTER_LEFT:
                        switch (state.getValue(StairBlock.FACING)) {
                            case NORTH -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                            }
                            case SOUTH -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                            }
                            case WEST -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                            }
                            case EAST -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                            }
                        }
                        break;
                    case OUTER_RIGHT:
                        switch (state.getValue(StairBlock.FACING)) {
                            case WEST -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                            }
                            case EAST -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                            }
                            case SOUTH -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                            }
                            case NORTH -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, true, true, !cullUpDown, cullUpDown, false));
                            }
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