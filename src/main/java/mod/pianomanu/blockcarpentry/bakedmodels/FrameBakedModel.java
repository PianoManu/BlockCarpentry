package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrassBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
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
public class FrameBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {

        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        Integer design = extraData.getData(FrameBlockTile.DESIGN);
        Integer desTex = extraData.getData(FrameBlockTile.DESIGN_TEXTURE);
        if (side != null) {
            return Collections.emptyList();
        }
        if (mimic != null && !(mimic.getBlock() instanceof FrameBlock)) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            if (location != null && state != null) {
                IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                if (model != null) {
                    //TODO what about full blocks with different side textures -> IllusionBlock
                    List<TextureAtlasSprite> textureList = TextureHelper.getTextureFromModel(model, extraData, rand);
                    TextureAtlasSprite texture;
                    Integer tex = extraData.getData(FrameBlockTile.TEXTURE);
                    if (textureList.size() <= tex) {
                        extraData.setData(FrameBlockTile.TEXTURE, 0);
                        tex = 0;
                    }
                    if (textureList.size() == 0) {
                        return Collections.emptyList();
                    }
                    texture = textureList.get(tex);
                    int tintIndex = -1;
                    if (mimic.getBlock() instanceof GrassBlock) {
                        tintIndex = 1;
                    }
                    List<BakedQuad> quads = new ArrayList<>(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0f, 1f, texture, tintIndex));
                    if (extraData.getData(FrameBlockTile.OVERLAY) == 1) {
                        BlockState grassBlock = Blocks.GRASS_BLOCK.getDefaultState();
                        ModelResourceLocation locationGrass = BlockModelShapes.getModelLocation(grassBlock);
                        IBakedModel modelGrass = Minecraft.getInstance().getModelManager().getModel(locationGrass);
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, 1f, grassBlock, modelGrass, extraData, rand, 1));
                        return quads;
                    }
                    if (extraData.getData(FrameBlockTile.OVERLAY) == 2) {
                        TextureAtlasSprite overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_side_overlay_large"));
                        TextureAtlasSprite grass = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/grass_block_top"));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, 1f, 1, overlay, overlay, overlay, overlay, grass, null));
                        return quads;
                    }
                    if (extraData.getData(FrameBlockTile.OVERLAY) == 3) {
                        TextureAtlasSprite overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_snow_overlay"));
                        TextureAtlasSprite snow = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/snow"));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, 1f, -1, overlay, overlay, overlay, overlay, snow, null));
                        return quads;
                    }
                    if (extraData.getData(FrameBlockTile.OVERLAY) == 4) {
                        TextureAtlasSprite overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/grass_block_snow_overlay_small"));
                        TextureAtlasSprite snow = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/snow"));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, 1f, -1, overlay, overlay, overlay, overlay, snow, null));
                        return quads;
                    }
                    if (extraData.getData(FrameBlockTile.OVERLAY) == 5) {
                        TextureAtlasSprite overlay = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/vine"));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, 1f, 1, overlay, overlay, overlay, overlay, null, null));
                        return quads;
                    }
                    return quads;
                }
            }
        }

        if (side != null) {
            return Collections.emptyList();
        }

        return new ArrayList<>(); //collapse with if (side!=null)?
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