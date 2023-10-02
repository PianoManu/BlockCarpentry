package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.ButtonFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.WoodButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
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
 * @version 1.3 09/20/23
 */
public class ButtonBakedModel implements IDynamicBakedModel {

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getMimicQuads(state, side, rand, extraData, model);
        }
        return Collections.emptyList();
    }

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, BakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && state != null) {
            TextureAtlasSprite texture = QuadUtils.getTexture(model, rand, extraData, FrameBlockTile.TEXTURE);
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            boolean isPowered = state.getValue(ButtonFrameBlock.POWERED);
            float thickness = isPowered ? 1 / 16f : 2 / 16f;

            float yl = 0f;
            float yh = thickness;
            if (state.getValue(WoodButtonBlock.FACE).equals(AttachFace.CEILING)) {
                yl = 1 - thickness;
                yh = 1f;
            }

            List<BakedQuad> quads = new ArrayList<>();
            switch (state.getValue(WoodButtonBlock.FACE)) {
                case WALL:
                    switch (state.getValue(WoodButtonBlock.FACING)) {
                        case NORTH -> quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 1 - thickness, 1f, texture, tintIndex));
                        case EAST -> quads.addAll(ModelHelper.createCuboid(0f, thickness, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, texture, tintIndex));
                        case WEST -> quads.addAll(ModelHelper.createCuboid(1 - thickness, 1f, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, texture, tintIndex));
                        case SOUTH -> quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 0f, thickness, texture, tintIndex));
                    }
                    break;
                case FLOOR:
                case CEILING:
                    switch (state.getValue(WoodButtonBlock.FACING)) {
                        case EAST, WEST -> quads.addAll(ModelHelper.createCuboid(6 / 16f, 10 / 16f, yl, yh, 5 / 16f, 11 / 16f, texture, tintIndex));
                        case SOUTH, NORTH -> quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, yl, yh, 6 / 16f, 10 / 16f, texture, tintIndex));
                    }
            }
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                switch (state.getValue(WoodButtonBlock.FACE)) {
                    case WALL:
                        switch (state.getValue(WoodButtonBlock.FACING)) {
                            case NORTH -> quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 1 - thickness, 1f, overlayIndex));
                            case EAST -> quads.addAll(ModelHelper.createOverlay(0f, thickness, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, overlayIndex));
                            case WEST -> quads.addAll(ModelHelper.createOverlay(1 - thickness, 1f, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, overlayIndex));
                            case SOUTH -> quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 0f, thickness, overlayIndex));
                        }
                        break;
                    case FLOOR:
                    case CEILING:
                        switch (state.getValue(WoodButtonBlock.FACING)) {
                            case EAST, WEST -> quads.addAll(ModelHelper.createOverlay(6 / 16f, 10 / 16f, yl, yh, 5 / 16f, 11 / 16f, overlayIndex));
                            case SOUTH, NORTH -> quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, yl, yh, 6 / 16f, 10 / 16f, overlayIndex));
                        }
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
}
//========SOLI DEO GLORIA========//