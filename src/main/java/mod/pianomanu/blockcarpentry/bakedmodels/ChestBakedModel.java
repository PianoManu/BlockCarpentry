package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.ChestFrameTileEntity;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.state.properties.BlockStateProperties;
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
 * See {@link ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.0 09/22/20
 */
@SuppressWarnings("deprecation")
public class ChestBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(ChestFrameTileEntity.MIMIC);
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
        BlockState mimic = extraData.getData(ChestFrameTileEntity.MIMIC);
        int tex = extraData.getData(ChestFrameTileEntity.TEXTURE);
        if (mimic != null && state != null) {
            List<TextureAtlasSprite> textureList = TextureHelper.getTextureFromModel(model, extraData, rand);
            List<TextureAtlasSprite> designTextureList = new ArrayList<>(TextureHelper.getMetalTextures());
            designTextureList.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/shulker_box")));
            TextureAtlasSprite chestFront = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chest_front"));
            TextureAtlasSprite chestSide = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chest_side"));
            TextureAtlasSprite chestTop = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chest_top"));
            TextureAtlasSprite texture;
            if (textureList.size() <= tex) {
                extraData.setData(ChestFrameTileEntity.TEXTURE, 0);
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
            int design = extraData.getData(ChestFrameTileEntity.DESIGN);
            int desTex = extraData.getData(ChestFrameTileEntity.DESIGN_TEXTURE);
            TextureAtlasSprite designTexture = designTextureList.get(desTex);
            List<BakedQuad> quads = new ArrayList<>();
            if (design == 0) {
                return new ArrayList<>(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0f, 1f, texture, tintIndex));
            }
            if (design == 1 || design == 2) {
                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 2 / 16f, 14 / 16f, 2 / 16f, 14 / 16f, texture, tintIndex));
            }
            if (design == 3) {
                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 2 / 16f, 14 / 16f, 2 / 16f, 14 / 16f, designTexture, tintIndex));
            }
            if (design == 1 || design == 3 || design == 4) {
                //vertical
                quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 1f, 0f, 2 / 16f, texture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 1f, 14 / 16f, 1f, texture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 1f, 0f, 2 / 16f, texture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 1f, 14 / 16f, 1f, texture, tintIndex));
                //horizontal down
                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 0f, 2 / 16f, 0f, 2 / 16f, texture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 0f, 2 / 16f, 14 / 16f, 1f, texture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 2 / 16f, 2 / 16f, 14 / 16f, texture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 2 / 16f, 2 / 16f, 14 / 16f, texture, tintIndex));
                //horizontal up
                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 14 / 16f, 1f, 0f, 2 / 16f, texture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 14 / 16f, 1f, 14 / 16f, 1f, texture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 14 / 16f, 1f, 2 / 16f, 14 / 16f, texture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 14 / 16f, 1f, 2 / 16f, 14 / 16f, texture, tintIndex));
            }
            if (design == 2) {
                //vertical
                quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 1f, 0f, 2 / 16f, designTexture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 1f, 14 / 16f, 1f, designTexture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 1f, 0f, 2 / 16f, designTexture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 1f, 14 / 16f, 1f, designTexture, tintIndex));
                //horizontal down
                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 0f, 2 / 16f, 0f, 2 / 16f, designTexture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 0f, 2 / 16f, 14 / 16f, 1f, designTexture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 2 / 16f, 2 / 16f, 14 / 16f, designTexture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 2 / 16f, 2 / 16f, 14 / 16f, designTexture, tintIndex));
                //horizontal up
                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 14 / 16f, 1f, 0f, 2 / 16f, designTexture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 14 / 16f, 1f, 14 / 16f, 1f, designTexture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 14 / 16f, 1f, 2 / 16f, 14 / 16f, designTexture, tintIndex));
                quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 14 / 16f, 1f, 2 / 16f, 14 / 16f, designTexture, tintIndex));
            }
            if (design == 4) {
                quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 2 / 16f, 1 / 16f, 15 / 16f, chestTop, tintIndex));
                quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 14 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestTop, tintIndex));
                //has to be inverted because chest knob-thing is also inverted
                int[] ulow = {7, 7, 7, 9, 8, 9};
                int[] uhigh = {9, 9, 8, 7, 9, 7};
                int[] vlow = {4, 7, 4, 4, 4, 4};
                int[] vhigh = {5, 8, 8, 8, 8, 8};
                switch (state.get(BlockStateProperties.HORIZONTAL_FACING)) {
                    case NORTH:
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 2 / 16f, chestFront, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 14 / 16f, 15 / 16f, chestSide, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 2 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestSide, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestSide, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 8 / 16f, 12 / 16f, 0 / 16f, 1 / 16f, chestFront, tintIndex, ulow, uhigh, vlow, vhigh));
                        break;
                    case EAST:
                        ulow = new int[]{7, 8, 9, 8, 9, 7};
                        uhigh = new int[]{8, 9, 7, 9, 7, 8};
                        vlow = new int[]{6, 4, 4, 4, 4, 4};
                        vhigh = new int[]{4, 6, 8, 8, 8, 8};
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 2 / 16f, chestSide, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 14 / 16f, 15 / 16f, chestSide, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 2 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestSide, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestFront, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 8 / 16f, 12 / 16f, 7 / 16f, 9 / 16f, chestFront, tintIndex, ulow, uhigh, vlow, vhigh));
                        break;
                    case SOUTH:
                        ulow = new int[]{9, 9, 8, 9, 7, 9};
                        uhigh = new int[]{7, 7, 9, 7, 8, 7};
                        vlow = new int[]{4, 8, 4, 4, 4, 4};
                        vhigh = new int[]{5, 7, 8, 8, 8, 8};
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 2 / 16f, chestSide, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 14 / 16f, 15 / 16f, chestFront, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 2 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestSide, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestSide, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 8 / 16f, 12 / 16f, 15 / 16f, 1f, chestFront, tintIndex, ulow, uhigh, vlow, vhigh));
                        break;
                    case WEST:
                        ulow = new int[]{7, 7, 9, 7, 9, 8};
                        uhigh = new int[]{8, 8, 7, 8, 7, 9};
                        vlow = new int[]{4, 6, 4, 4, 4, 4};
                        vhigh = new int[]{6, 4, 8, 8, 8, 8};
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 2 / 16f, chestSide, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 14 / 16f, 15 / 16f, chestSide, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 2 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestFront, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestSide, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0 / 16f, 1 / 16f, 8 / 16f, 12 / 16f, 7 / 16f, 9 / 16f, chestFront, tintIndex, ulow, uhigh, vlow, vhigh));
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