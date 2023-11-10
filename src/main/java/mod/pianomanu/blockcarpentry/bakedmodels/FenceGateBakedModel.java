package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
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
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
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
 * See {@link ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.3 09/20/23
 */
public class FenceGateBakedModel implements IDynamicBakedModel {

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
            if (state != null) {
                BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                return getMimicQuads(state, side, rand, extraData, model);
            }
        }
        return Collections.emptyList();
    }

    @Nonnull
    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, ModelData extraData, BakedModel model) {
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        Integer design = extraData.get(FrameBlockTile.DESIGN);
        if (side != null) {
            return Collections.emptyList();
        }
        if (mimic != null && state != null) {
            TextureAtlasSprite texture = TextureHelper.getTexture(model, rand, extraData, FrameBlockTile.TEXTURE);
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            float w = 0;
            if (state.getValue(FenceGateBlock.IN_WALL)) {
                w = -3 / 16f;
            }
            List<BakedQuad> quads = new ArrayList<>();
            if (design == 0 || design == 3) {
                if (state.getValue(FenceGateBlock.OPEN)) {
                    switch (state.getValue(FenceGateBlock.FACING)) {
                        case NORTH -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            //0-0 post
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 6 / 16f + w, 15 / 16f + w, 1 / 16f, 3 / 16f, texture, tintIndex));
                            //1-0 post
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 6 / 16f + w, 15 / 16f + w, 1 / 16f, 3 / 16f, texture, tintIndex));
                            //4 Crossbars
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 6 / 16f + w, 9 / 16f + w, 3 / 16f, 7 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 12 / 16f + w, 15 / 16f + w, 3 / 16f, 7 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 6 / 16f + w, 9 / 16f + w, 3 / 16f, 7 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 12 / 16f + w, 15 / 16f + w, 3 / 16f, 7 / 16f, texture, tintIndex));
                        }
                        case SOUTH -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            //0-1 post
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 6 / 16f + w, 15 / 16f + w, 13 / 16f, 15 / 16f, texture, tintIndex));
                            //1-1 post
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 6 / 16f + w, 15 / 16f + w, 13 / 16f, 15 / 16f, texture, tintIndex));
                            //4 Crossbars
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 6 / 16f + w, 9 / 16f + w, 9 / 16f, 15 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 12 / 16f + w, 15 / 16f + w, 9 / 16f, 15 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 6 / 16f + w, 9 / 16f + w, 9 / 16f, 15 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 12 / 16f + w, 15 / 16f + w, 9 / 16f, 15 / 16f, texture, tintIndex));
                        }
                        case EAST -> {
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 14 / 16f, 1f, texture, tintIndex));
                            //1-0 post
                            quads.addAll(ModelHelper.createCuboid(13 / 16f, 15 / 16f, 6 / 16f + w, 15 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            //1-1 post
                            quads.addAll(ModelHelper.createCuboid(13 / 16f, 15 / 16f, 6 / 16f + w, 15 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                            //4 Crossbars
                            quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 6 / 16f + w, 9 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 12 / 16f + w, 15 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 6 / 16f + w, 9 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 12 / 16f + w, 15 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                        }
                        case WEST -> {
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 14 / 16f, 1f, texture, tintIndex));
                            //0-0 post
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 3 / 16f, 6 / 16f + w, 15 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            //0-1 post
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 3 / 16f, 6 / 16f + w, 15 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                            //4 Crossbars
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 6 / 16f + w, 9 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 12 / 16f + w, 15 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 6 / 16f + w, 9 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 12 / 16f + w, 15 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                        }
                    }
                } else {
                    switch (state.getValue(FenceGateBlock.FACING)) {
                        case NORTH, SOUTH -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 6 / 16f + w, 9 / 16f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 12 / 16f + w, 15 / 16f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(6 / 16f, 10 / 16f, 9 / 16f + w, 12 / 16f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                        }
                        case EAST, WEST -> {
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 14 / 16f, 1f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 6 / 16f + w, 9 / 16f + w, 2 / 16f, 14 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 12 / 16f + w, 15 / 16f + w, 2 / 16f, 14 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 9 / 16f + w, 12 / 16f + w, 6 / 16f, 10 / 16f, texture, tintIndex));
                        }
                    }
                }
            }
            if (design == 1) {
                if (state.getValue(FenceGateBlock.OPEN)) {
                    switch (state.getValue(FenceGateBlock.FACING)) {
                        case NORTH -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            //4 Crossbars
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 6 / 16f + w, 9 / 16f + w, 1 / 16f, 7 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 12 / 16f + w, 15 / 16f + w, 1 / 16f, 7 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 6 / 16f + w, 9 / 16f + w, 1 / 16f, 7 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 12 / 16f + w, 15 / 16f + w, 1 / 16f, 7 / 16f, texture, tintIndex));
                        }
                        case SOUTH -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            //4 Crossbars
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 6 / 16f + w, 9 / 16f + w, 9 / 16f, 15 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 12 / 16f + w, 15 / 16f + w, 9 / 16f, 15 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 6 / 16f + w, 9 / 16f + w, 9 / 16f, 15 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 12 / 16f + w, 15 / 16f + w, 9 / 16f, 15 / 16f, texture, tintIndex));
                        }
                        case EAST -> {
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 14 / 16f, 1f, texture, tintIndex));
                            //4 Crossbars
                            quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 6 / 16f + w, 9 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 12 / 16f + w, 15 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 6 / 16f + w, 9 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 12 / 16f + w, 15 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                        }
                        case WEST -> {
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 14 / 16f, 1f, texture, tintIndex));
                            //4 Crossbars
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 6 / 16f + w, 9 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 12 / 16f + w, 15 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 6 / 16f + w, 9 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 12 / 16f + w, 15 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                        }
                    }
                } else {
                    switch (state.getValue(FenceGateBlock.FACING)) {
                        case NORTH, SOUTH -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 6 / 16f + w, 9 / 16f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 12 / 16f + w, 15 / 16f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                        }
                        case EAST, WEST -> {
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 14 / 16f, 1f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 6 / 16f + w, 9 / 16f + w, 2 / 16f, 14 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 12 / 16f + w, 15 / 16f + w, 2 / 16f, 14 / 16f, texture, tintIndex));
                        }
                    }
                }
            }
            if (design == 2) {
                if (state.getValue(FenceGateBlock.OPEN)) {
                    switch (state.getValue(FenceGateBlock.FACING)) {
                        case NORTH -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            //0-0 post
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 6 / 16f + w, 13 / 16f + w, 1 / 16f, 3 / 16f, texture, tintIndex));
                            //1-0 post
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 6 / 16f + w, 13 / 16f + w, 1 / 16f, 3 / 16f, texture, tintIndex));
                            //2 Crossbars
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 6 / 16f + w, 15 / 16f + w, 3 / 16f, 7 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 6 / 16f + w, 15 / 16f + w, 3 / 16f, 7 / 16f, texture, tintIndex));
                        }
                        case SOUTH -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            //0-1 post
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 6 / 16f + w, 13 / 16f + w, 13 / 16f, 15 / 16f, texture, tintIndex));
                            //1-1 post
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 6 / 16f + w, 13 / 16f + w, 13 / 16f, 15 / 16f, texture, tintIndex));
                            //2 Crossbars
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 6 / 16f + w, 15 / 16f + w, 9 / 16f, 13 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 6 / 16f + w, 15 / 16f + w, 9 / 16f, 13 / 16f, texture, tintIndex));
                        }
                        case EAST -> {
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 14 / 16f, 1f, texture, tintIndex));
                            //1-0 post
                            quads.addAll(ModelHelper.createCuboid(13 / 16f, 15 / 16f, 6 / 16f + w, 13 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            //1-1 post
                            quads.addAll(ModelHelper.createCuboid(13 / 16f, 15 / 16f, 6 / 16f + w, 13 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                            //2 Crossbars
                            quads.addAll(ModelHelper.createCuboid(9 / 16f, 13 / 16f, 6 / 16f + w, 15 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(9 / 16f, 13 / 16f, 6 / 16f + w, 15 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                        }
                        case WEST -> {
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 14 / 16f, 1f, texture, tintIndex));
                            //0-0 post
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 3 / 16f, 6 / 16f + w, 13 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            //0-1 post
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 3 / 16f, 6 / 16f + w, 13 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                            //2 Crossbars
                            quads.addAll(ModelHelper.createCuboid(3 / 16f, 7 / 16f, 6 / 16f + w, 15 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(3 / 16f, 7 / 16f, 6 / 16f + w, 15 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                        }
                    }
                } else {
                    switch (state.getValue(FenceGateBlock.FACING)) {
                        case NORTH, SOUTH -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(2 / 16f, 6 / 16f, 6 / 16f + w, 15 / 16f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(10 / 16f, 14 / 16f, 6 / 16f + w, 15 / 16f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(6 / 16f, 10 / 16f, 6 / 16f + w, 13 / 16f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                        }
                        case EAST, WEST -> {
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 14 / 16f, 1f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 6 / 16f + w, 15 / 16f + w, 2 / 16f, 6 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 6 / 16f + w, 15 / 16f + w, 10 / 16f, 14 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 6 / 16f + w, 13 / 16f + w, 6 / 16f, 10 / 16f, texture, tintIndex));
                        }
                    }
                }
            }
            if (design == 3) {
                switch (state.getValue(FenceGateBlock.FACING)) {
                    case NORTH, SOUTH -> {
                        quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 1f, 6 / 16f, 10 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 1f, 6 / 16f, 10 / 16f, texture, tintIndex));
                    }
                    case EAST, WEST -> {
                        quads.addAll(ModelHelper.createCuboid(6 / 16f, 10 / 16f, 0f, 1f, 0f, 2 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(6 / 16f, 10 / 16f, 0f, 1f, 14 / 16f, 1f, texture, tintIndex));
                    }
                }
            }
            if (design == 4) {
                //inverts gate height when connected with walls
                w = -3 / 16f;
                if (state.getValue(FenceGateBlock.IN_WALL)) {
                    w = 0;
                }
                if (state.getValue(FenceGateBlock.OPEN)) {
                    switch (state.getValue(FenceGateBlock.FACING)) {
                        case NORTH -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            //0-0 post
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 6 / 16f + w, 15 / 16f + w, 1 / 16f, 3 / 16f, texture, tintIndex));
                            //1-0 post
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 6 / 16f + w, 15 / 16f + w, 1 / 16f, 3 / 16f, texture, tintIndex));
                            //4 Crossbars
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 6 / 16f + w, 9 / 16f + w, 3 / 16f, 7 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 12 / 16f + w, 15 / 16f + w, 3 / 16f, 7 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 6 / 16f + w, 9 / 16f + w, 3 / 16f, 7 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 12 / 16f + w, 15 / 16f + w, 3 / 16f, 7 / 16f, texture, tintIndex));
                        }
                        case SOUTH -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            //0-1 post
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 6 / 16f + w, 15 / 16f + w, 13 / 16f, 15 / 16f, texture, tintIndex));
                            //1-1 post
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 6 / 16f + w, 15 / 16f + w, 13 / 16f, 15 / 16f, texture, tintIndex));
                            //4 Crossbars
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 6 / 16f + w, 9 / 16f + w, 9 / 16f, 15 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 12 / 16f + w, 15 / 16f + w, 9 / 16f, 15 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 6 / 16f + w, 9 / 16f + w, 9 / 16f, 15 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 12 / 16f + w, 15 / 16f + w, 9 / 16f, 15 / 16f, texture, tintIndex));
                        }
                        case EAST -> {
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 14 / 16f, 1f, texture, tintIndex));
                            //1-0 post
                            quads.addAll(ModelHelper.createCuboid(13 / 16f, 15 / 16f, 6 / 16f + w, 15 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            //1-1 post
                            quads.addAll(ModelHelper.createCuboid(13 / 16f, 15 / 16f, 6 / 16f + w, 15 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                            //4 Crossbars
                            quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 6 / 16f + w, 9 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 12 / 16f + w, 15 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 6 / 16f + w, 9 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 12 / 16f + w, 15 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                        }
                        case WEST -> {
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 14 / 16f, 1f, texture, tintIndex));
                            //0-0 post
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 3 / 16f, 6 / 16f + w, 15 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            //0-1 post
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 3 / 16f, 6 / 16f + w, 15 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                            //4 Crossbars
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 6 / 16f + w, 9 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 12 / 16f + w, 15 / 16f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 6 / 16f + w, 9 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 12 / 16f + w, 15 / 16f + w, 14 / 16f, 1f, texture, tintIndex));
                        }
                    }
                } else {
                    switch (state.getValue(FenceGateBlock.FACING)) {
                        case NORTH, SOUTH -> {
                            quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 5 / 16f + w, 1f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 6 / 16f + w, 9 / 16f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 12 / 16f + w, 15 / 16f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(6 / 16f, 10 / 16f, 9 / 16f + w, 12 / 16f + w, 7 / 16f, 9 / 16f, texture, tintIndex));
                        }
                        case EAST, WEST -> {
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 0f, 2 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 5 / 16f + w, 1f + w, 14 / 16f, 1f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 6 / 16f + w, 9 / 16f + w, 2 / 16f, 14 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 12 / 16f + w, 15 / 16f + w, 2 / 16f, 14 / 16f, texture, tintIndex));
                            quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 9 / 16f + w, 12 / 16f + w, 6 / 16f, 10 / 16f, texture, tintIndex));
                        }
                    }
                }
            }
            return quads;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
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