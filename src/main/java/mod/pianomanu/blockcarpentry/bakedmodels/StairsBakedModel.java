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
 * @version 1.3 09/20/23
 */
public class StairsBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, RenderType renderType) {
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getMimicQuads(state, side, rand, extraData, model);
        }
        return Collections.emptyList();
    }

    /**
     * create quads for stairs model using the texture from the block of the provided state - completely too long and could be compressed, but it works, and maybe I'll rewrite it for later versions, but for now it's okay
     *
     * @param state     state of frame stair
     * @param side      unused
     * @param rand      unused
     * @param extraData contains data from tile entity (in this case only for the contained block)
     * @return baked quads, that will be used to display the model
     */
    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, BakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        if (mimic != null && state != null) {
            TextureAtlasSprite texture = QuadUtils.getTexture(model, rand, extraData, FrameBlockTile.TEXTURE);
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            List<BakedQuad> quads = new ArrayList<>();
            float yl = 0f;
            float yh = 0.5f;
            boolean cullUpDown = false;
            if (state.getValue(StairBlock.HALF).equals(Half.TOP)) {
                yl = 0.5f;
                yh = 1f;
                cullUpDown = true;
            }
            switch (state.getValue(StairBlock.SHAPE)) {
                case STRAIGHT:
                    switch (state.getValue(StairBlock.FACING)) {
                        case NORTH -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                        }
                        case SOUTH -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                        }
                        case WEST -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 1f, texture, tintIndex, true, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 1f, texture, tintIndex, true, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 1f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                        }
                        case EAST -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 1f, texture, tintIndex, true, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 1f, texture, tintIndex, true, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 1f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                        }
                    }
                    break;
                case INNER_LEFT:
                    switch (state.getValue(StairBlock.FACING)) {
                        case NORTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, true, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, true, false, !cullUpDown, cullUpDown));
                        }
                        case SOUTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, true, false, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, true, true, !cullUpDown, cullUpDown));
                        }
                        case WEST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, true, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, true, false, !cullUpDown, cullUpDown));
                        }
                        case EAST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, true, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, true, false, !cullUpDown, cullUpDown));
                        }
                    }
                    break;
                case INNER_RIGHT:
                    switch (state.getValue(StairBlock.FACING)) {
                        case WEST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, true, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, true, false, !cullUpDown, cullUpDown));
                        }
                        case EAST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, true, false, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, true, true, !cullUpDown, cullUpDown));
                        }
                        case SOUTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, true, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, true, false, !cullUpDown, cullUpDown));
                        }
                        case NORTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, true, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, true, false, !cullUpDown, cullUpDown));
                        }
                    }
                    break;
                case OUTER_LEFT:
                    switch (state.getValue(StairBlock.FACING)) {
                        case NORTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                        }
                        case SOUTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                        }
                        case WEST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                        }
                        case EAST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                        }
                    }
                    break;
                case OUTER_RIGHT:
                    switch (state.getValue(StairBlock.FACING)) {
                        case WEST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                        }
                        case EAST -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                        }
                        case SOUTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                        }
                        case NORTH -> {
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
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
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, false, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                            }
                            case SOUTH -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, false, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                            }
                            case WEST -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, false, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                            }
                            case EAST -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, false, true, false, true, true, cullUpDown, false));
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
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, false, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                            }
                            case EAST -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, false, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                            }
                            case SOUTH -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, false, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                            }
                            case NORTH -> {
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, false, true, false, true, true, cullUpDown, false));
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
        return getTexture();
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