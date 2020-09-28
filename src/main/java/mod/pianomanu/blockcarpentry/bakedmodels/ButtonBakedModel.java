package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.WoodButtonBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.state.properties.AttachFace;
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
 * @version 1.4 09/28/20
 */
public class ButtonBakedModel implements IDynamicBakedModel {

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
                    Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("We're sorry, but this block can't be displayed"), true);
                }
                return Collections.emptyList();
            }
            texture = textureList.get(tex);
            int tintIndex = -1;
            if (mimic.getBlock() instanceof GrassBlock) {
                tintIndex = 1;
            }
            float yl = 0f;
            float yh = 2 / 16f;
            if (state.get(WoodButtonBlock.FACE).equals(AttachFace.CEILING)) {
                yl = 14 / 16f;
                yh = 1f;
            }
            List<BakedQuad> quads = new ArrayList<>();
            switch (state.get(WoodButtonBlock.FACE)) {
                case WALL:
                    switch (state.get(WoodButtonBlock.HORIZONTAL_FACING)) {
                        case NORTH:
                            quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 14 / 16f, 1f, texture, tintIndex));
                            break;
                        case EAST:
                            quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 2 / 16f, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, texture, tintIndex));
                            break;
                        case WEST:
                            quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(14 / 16f, 1f, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, texture, tintIndex));
                            break;
                        case SOUTH:
                            quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 0f, 2 / 16f, texture, tintIndex));
                            break;
                    }
                    break;
                case FLOOR:
                case CEILING:
                    switch (state.get(WoodButtonBlock.HORIZONTAL_FACING)) {
                        case EAST:
                        case WEST:
                            quads.addAll(ModelHelper.createCuboid(6 / 16f, 10 / 16f, yl, yh, 5 / 16f, 11 / 16f, texture, tintIndex));
                            break;
                        case SOUTH:
                        case NORTH:
                            quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, yl, yh, 6 / 16f, 10 / 16f, texture, tintIndex));
                            break;
                    }
            }
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                switch (state.get(WoodButtonBlock.FACE)) {
                    case WALL:
                        switch (state.get(WoodButtonBlock.HORIZONTAL_FACING)) {
                            case NORTH:
                                quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlay(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 14 / 16f, 1f, overlayIndex));
                                break;
                            case EAST:
                                quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlay(0f, 2 / 16f, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, overlayIndex));
                                break;
                            case WEST:
                                quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlay(14 / 16f, 1f, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, overlayIndex));
                                break;
                            case SOUTH:
                                quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlay(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 0f, 2 / 16f, overlayIndex));
                                break;
                        }
                        break;
                    case FLOOR:
                    case CEILING:
                        switch (state.get(WoodButtonBlock.HORIZONTAL_FACING)) {
                            case EAST:
                            case WEST:
                                quads.addAll(ModelHelper.createOverlay(6 / 16f, 10 / 16f, yl, yh, 5 / 16f, 11 / 16f, overlayIndex));
                                break;
                            case SOUTH:
                            case NORTH:
                                quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, yl, yh, 6 / 16f, 10 / 16f, overlayIndex));
                                break;
                        }
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