package mod.pianomanu.blockcarpentry.bakedmodels;

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
import net.minecraft.util.RandomSource;
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

/**
 * Contains all information for the block model
 * See {@link mod.pianomanu.blockcarpentry.util.ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.1 06/11/22
 */
public class IllusionButtonPoweredBakedModel implements IDynamicBakedModel {
    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getIllusionQuads(state, side, rand, extraData, model);
        }
        return Collections.emptyList();
    }

    private List<BakedQuad> getIllusionQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull IModelData extraData, BakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && state != null) {
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            int rotation = extraData.getData(FrameBlockTile.ROTATION);
            float yl = 0f;
            float yh = 1 / 16f;
            if (state.getValue(WoodButtonBlock.FACE).equals(AttachFace.CEILING)) {
                yl = 15 / 16f;
                yh = 1f;
            }
            List<BakedQuad> quads = new ArrayList<>();
            switch (state.getValue(WoodButtonBlock.FACE)) {
                case WALL:
                    switch (state.getValue(WoodButtonBlock.FACING)) {
                        case NORTH -> quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 15 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                        case EAST -> quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1 / 16f, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                        case WEST -> quads.addAll(ModelHelper.createSixFaceCuboid(15 / 16f, 1f, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                        case SOUTH -> quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 0f, 1 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    }
                    break;
                case FLOOR:
                case CEILING:
                    switch (state.getValue(WoodButtonBlock.FACING)) {
                        case EAST, WEST -> quads.addAll(ModelHelper.createSixFaceCuboid(6 / 16f, 10 / 16f, yl, yh, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                        case SOUTH, NORTH -> quads.addAll(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, yl, yh, 6 / 16f, 10 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    }
            }
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                switch (state.getValue(WoodButtonBlock.FACE)) {
                    case WALL:
                        switch (state.getValue(WoodButtonBlock.FACING)) {
                            case NORTH -> quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 15 / 16f, 1f, overlayIndex));
                            case EAST -> quads.addAll(ModelHelper.createOverlay(0f, 1 / 16f, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, overlayIndex));
                            case WEST -> quads.addAll(ModelHelper.createOverlay(15 / 16f, 1f, 6 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, overlayIndex));
                            case SOUTH -> quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 6 / 16f, 10 / 16f, 0f, 1 / 16f, overlayIndex));
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