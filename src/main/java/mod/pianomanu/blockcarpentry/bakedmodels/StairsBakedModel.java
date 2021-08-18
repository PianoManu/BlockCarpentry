package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FrameBlock;
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
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
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
 * @version 1.7 08/18/21
 */
public class StairsBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && !(mimic.getBlock() instanceof FrameBlock)) {
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

    /**
     * create quads for stairs model using the texture from the block of the provided state - completely too long and could be compressed, but it works, and maybe I'll rewrite it for later versions, but for now it's okay
     *
     * @param state     state of frame stair
     * @param side      unused
     * @param rand      unused
     * @param extraData contains data from tile entity (in this case only for the contained block)
     * @return baked quads, that will be used to display the model
     */
    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, IBakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        int tex = extraData.getData(FrameBlockTile.TEXTURE);
        if (mimic != null && state != null) {
            List<TextureAtlasSprite> textureList = TextureHelper.getTextureFromModel(model, extraData, rand);
            TextureAtlasSprite texture;
            if (textureList.size() <= tex) {
                extraData.setData(FrameBlockTile.TEXTURE, 0);
                tex = 0;
            }
            if (textureList.size() == 0) {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.block_not_available"), true);
                }
                texture = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("missing"));
                //return Collections.emptyList();
            } else {
                texture = textureList.get(tex);
            }
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            List<BakedQuad> quads = new ArrayList<>();
            float yl = 0f;
            float yh = 0.5f;
            boolean cullUpDown = false;
            if (state.get(StairsBlock.HALF).equals(Half.TOP)) {
                yl = 0.5f;
                yh = 1f;
                cullUpDown = true;
            }
            switch (state.get(StairsBlock.SHAPE)) {
                case STRAIGHT:
                    switch (state.get(StairsBlock.FACING)) {
                        case NORTH:
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                            break;
                        case SOUTH:
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                            break;
                        case WEST:
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 1f, texture, tintIndex, true, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 1f, texture, tintIndex, true, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 1f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                            break;
                        case EAST:
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 1f, texture, tintIndex, true, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 1f, texture, tintIndex, true, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 1f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                            break;
                    }
                    break;
                case INNER_LEFT:
                    switch (state.get(StairsBlock.FACING)) {
                        case NORTH:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, true, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, true, false, !cullUpDown, cullUpDown));
                            break;
                        case SOUTH:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, true, false, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, true, true, !cullUpDown, cullUpDown));
                            break;
                        case WEST:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, true, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, true, false, !cullUpDown, cullUpDown));
                            break;
                        case EAST:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, true, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, true, false, !cullUpDown, cullUpDown));
                            break;
                    }
                    break;
                case INNER_RIGHT:
                    switch (state.get(StairsBlock.FACING)) {
                        case WEST:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, true, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, true, false, !cullUpDown, cullUpDown));
                            break;
                        case EAST:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, true, false, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, true, true, !cullUpDown, cullUpDown));
                            break;
                        case SOUTH:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, true, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, true, false, !cullUpDown, cullUpDown));
                            break;
                        case NORTH:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, false, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, false, true, true, true, !cullUpDown, cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, false, true, false, !cullUpDown, cullUpDown));
                            break;
                    }
                    break;
                case OUTER_LEFT:
                    switch (state.get(StairsBlock.FACING)) {
                        case NORTH:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                            break;
                        case SOUTH:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                            break;
                        case WEST:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                            break;
                        case EAST:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                            break;
                    }
                    break;
                case OUTER_RIGHT:
                    switch (state.get(StairsBlock.FACING)) {
                        case WEST:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                            break;
                        case EAST:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                            break;
                        case SOUTH:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, cullUpDown, !cullUpDown));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, true, true));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                            break;
                        case NORTH:
                            //bottom part
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0f, 0.5f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, false, true, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0.5f, 1f, texture, tintIndex, false, true, true, false, true, true));
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, yl, yh, 0f, 0.5f, texture, tintIndex, true, false, true, false, cullUpDown, !cullUpDown));
                            //upper part
                            quads.addAll(ModelHelper.createCuboid(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, texture, tintIndex, true, true, true, true, !cullUpDown, cullUpDown));
                            break;
                    }
                    break;
            }
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                switch (state.get(StairsBlock.SHAPE)) {
                    case STRAIGHT:
                        switch (state.get(StairsBlock.FACING)) {
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
                        switch (state.get(StairsBlock.FACING)) {
                            case NORTH:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, false, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                                break;
                            case SOUTH:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, false, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                                break;
                            case WEST:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, false, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                                break;
                            case EAST:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, false, true, false, true, true, cullUpDown, false));
                                break;
                        }
                        break;
                    case INNER_RIGHT:
                        switch (state.get(StairsBlock.FACING)) {
                            case WEST:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, false, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                                break;
                            case EAST:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, false, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                                break;
                            case SOUTH:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, true, true, !cullUpDown));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, true, false, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, false, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, false, true, true, true, true, cullUpDown, false));
                                break;
                            case NORTH:
                                //bottom part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0f, 0.5f, overlayIndex, true, false, false, true, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, yl, yh, 0.5f, 1f, overlayIndex, true, false, true, false, true, true, !cullUpDown));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0.5f, 1f, overlayIndex, false, true, true, false, cullUpDown, !cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, yl, yh, 0f, 0.5f, overlayIndex, false, true, false, true, cullUpDown, !cullUpDown, false));
                                //upper part
                                quads.addAll(ModelHelper.createOverlay(0f, 0.5f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, true, false, true, true, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0.5f, 1f, overlayIndex, true, true, true, false, true, cullUpDown, false));
                                quads.addAll(ModelHelper.createOverlay(0.5f, 1f, 1 - yh, 1 - yl, 0f, 0.5f, overlayIndex, false, true, false, true, true, cullUpDown, false));
                                break;
                        }
                        break;
                    case OUTER_LEFT:
                        switch (state.get(StairsBlock.FACING)) {
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
                        switch (state.get(StairsBlock.FACING)) {
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