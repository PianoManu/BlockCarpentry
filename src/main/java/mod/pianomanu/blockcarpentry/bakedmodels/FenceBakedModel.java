package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FenceFrameBlock;
import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FenceBakedModel implements IDynamicBakedModel {
    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && !(mimic.getBlock() instanceof FrameBlock)) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            if (location != null && state!=null) {
                IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                if (model != null) {
                    return getMimicQuads(state, side, rand, extraData);
                }
            }
        }
        return Collections.emptyList();
    }

    @Nonnull
    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, IModelData extraData) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic!=null && state != null) {
            List<TextureAtlasSprite> texture = TextureHelper.getTextureListFromBlock(mimic.getBlock());
            int index = state.get(FrameBlock.TEXTURE);
            if (index >= texture.size()) {
                index = 0;
            }
            int tintIndex = -1;
            if (mimic.getBlock() instanceof GrassBlock) {
                tintIndex = 1;
            }
            List<BakedQuad> quads = new ArrayList<>(ModelHelper.createCuboid(6 / 16f, 10 / 16f, 0f, 1f, 6 / 16f, 10 / 16f, texture.get(index), tintIndex));
            if (state.get(FenceFrameBlock.NORTH)) {
                quads.addAll(ModelHelper.createCuboid(7/16f,9/16f,3/16f,6/16f,0f,6/16f, texture.get(index), tintIndex));
                quads.addAll(ModelHelper.createCuboid(7/16f,9/16f,9/16f,12/16f,0f,6/16f, texture.get(index), tintIndex));
            }
            if (state.get(FenceFrameBlock.EAST)) {
                quads.addAll(ModelHelper.createCuboid(10/16f,1f,3/16f,6/16f,7/16f,9/16f, texture.get(index), tintIndex));
                quads.addAll(ModelHelper.createCuboid(10/16f,1f,9/16f,12/16f,7/16f,9/16f, texture.get(index), tintIndex));
            }
            if (state.get(FenceFrameBlock.SOUTH)) {
                quads.addAll(ModelHelper.createCuboid(7/16f,9/16f,3/16f,6/16f,10/16f,1f, texture.get(index), tintIndex));
                quads.addAll(ModelHelper.createCuboid(7/16f,9/16f,9/16f,12/16f,10/16f,1f, texture.get(index), tintIndex));
            }
            if (state.get(FenceFrameBlock.WEST)) {
                quads.addAll(ModelHelper.createCuboid(0f,6/16f,3/16f,6/16f,7/16f,9/16f, texture.get(index), tintIndex));
                quads.addAll(ModelHelper.createCuboid(0f,6/16f,9/16f,12/16f,7/16f,9/16f, texture.get(index), tintIndex));
            }
            return quads;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
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
        return null;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }
}