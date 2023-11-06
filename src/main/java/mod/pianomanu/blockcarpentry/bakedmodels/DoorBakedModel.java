package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.DoorFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
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
 * @version 1.6 09/20/23
 */
public class DoorBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        //get block saved in frame tile
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getMimicQuads(state, side, rand, extraData, model);
        }
        return Collections.emptyList();
    }

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, IBakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }

        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && state != null && extraData.getData(FrameBlockTile.DESIGN) != null && extraData.getData(FrameBlockTile.DESIGN_TEXTURE) != null) {
            //get texture from block in tile entity and apply it to the quads
            List<TextureAtlasSprite> glassBlockList = TextureHelper.getGlassTextures();
            TextureAtlasSprite glass = glassBlockList.get(extraData.getData(FrameBlockTile.GLASS_COLOR));
            TextureAtlasSprite texture = TextureHelper.getTexture(model, rand, extraData, FrameBlockTile.TEXTURE);
            List<BakedQuad> quads = new ArrayList<>();
            Direction dir = state.get(DoorBlock.FACING);
            boolean open = state.get(DoorFrameBlock.OPEN);
            DoorHingeSide hinge = state.get(DoorFrameBlock.HINGE);
            Direction west = Direction.WEST;
            Direction east = Direction.EAST;
            Direction north = Direction.NORTH;
            Direction south = Direction.SOUTH;
            DoorHingeSide left = DoorHingeSide.LEFT;
            DoorHingeSide right = DoorHingeSide.RIGHT;
            int design = extraData.getData(FrameBlockTile.DESIGN);//int design = state.get(DoorFrameBlock.DESIGN);
            DoubleBlockHalf half = state.get(DoorBlock.HALF);
            DoubleBlockHalf lower = DoubleBlockHalf.LOWER;
            DoubleBlockHalf upper = DoubleBlockHalf.UPPER;
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            TextureAtlasSprite innerTexture = design != 1 ? glass : texture;

            boolean upVisible, downVisible, nVisible, eVisible, sVisible, wVisible;
            boolean xStripe, yStripe, zStripe;

            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            boolean northSide = (dir == north && !open) || (dir == east && open && hinge == right) || (dir == west && open && hinge == left);
            boolean westSide = (dir == west && !open) || (dir == north && open && hinge == right) || (dir == south && open && hinge == left);
            boolean eastSide = (dir == south && open && hinge == right) || (dir == east && !open) || (dir == north && open && hinge == left);
            boolean southSide = (dir == east && open && hinge == left) || (dir == west && open && hinge == right) || (dir == south && !open);
            int xOffset = eastSide ? 0 : 13;
            int zOffset = southSide ? 0 : 13;
            if (design == 0 || design == 1) {
                if (northSide || southSide) {
                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 3; z++) {
                                xStripe = half == lower ? y < 4 : y > 11;
                                yStripe = x < 4 || x > 11;
                                upVisible = half == lower ? (y == 3 && !yStripe) : y == 15;
                                downVisible = half == upper ? (y == 12 && !yStripe) : y == 0;
                                wVisible = x == 0 || (x == 12 && !xStripe);
                                eVisible = x == 15 || (x == 3 && !xStripe);
                                nVisible = z == 0;
                                sVisible = z == 2;
                                if (xStripe || yStripe)
                                    quads.addAll(ModelHelper.createVoxel(x, y, z + zOffset, texture, tintIndex, nVisible, sVisible, eVisible, wVisible, upVisible, downVisible));
                                if ((xStripe || yStripe) && overlayIndex > 0)
                                    quads.addAll(ModelHelper.createOverlayVoxel(x, y, z + zOffset, overlayIndex, nVisible, sVisible, eVisible, wVisible, upVisible && y == 15, downVisible, false));
                                if (!xStripe && !yStripe && z == 1)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createVoxel(x, y, z + zOffset, innerTexture, tintIndex, true, true, false, false, false, false));
                                if (!xStripe && !yStripe && z == 1 && overlayIndex > 0)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlayVoxel(x, y, z + zOffset, overlayIndex, true, true, false, false, false, false, false));
                            }
                        }
                    }
                } else if (westSide || eastSide) {
                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 16; z++) {
                                zStripe = half == lower ? y < 4 : y > 11;
                                yStripe = z < 4 || z > 11;
                                upVisible = half == lower ? (y == 3 && !yStripe) : y == 15;
                                downVisible = half == upper ? (y == 12 && !yStripe) : y == 0;
                                nVisible = z == 0 || (z == 12 && !zStripe);
                                sVisible = z == 15 || (z == 3 && !zStripe);
                                wVisible = x == 0;
                                eVisible = x == 2;
                                if (zStripe || yStripe)
                                    quads.addAll(ModelHelper.createVoxel(x + xOffset, y, z, texture, tintIndex, nVisible, sVisible, eVisible, wVisible, upVisible, downVisible));
                                if ((zStripe || yStripe) && overlayIndex > 0)
                                    quads.addAll(ModelHelper.createOverlayVoxel(x + xOffset, y, z, overlayIndex, nVisible, sVisible, eVisible, wVisible, upVisible && y == 15, downVisible, false));
                                if (!zStripe && !yStripe && x == 1)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createVoxel(x + xOffset, y, z, innerTexture, tintIndex, false, false, true, true, false, false));
                                if (!zStripe && !yStripe && x == 1 && overlayIndex > 0)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlayVoxel(x + xOffset, y, z, overlayIndex, false, false, true, true, false, false, false));
                            }
                        }
                    }
                }
            }
            if (design == 2) {
                if (northSide) {
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 13 / 16f, 1f, texture, tintIndex, true, true, true, true, half == upper, half == lower));
                    if (overlayIndex > 0)
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 13 / 16f, 1f, overlayIndex, true, true, true, true, half == upper, half == lower, false));
                } else if (westSide) {
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0f, 1f, 0f, 1f, texture, tintIndex, true, true, true, true, half == upper, half == lower));
                    if (overlayIndex > 0)
                        quads.addAll(ModelHelper.createOverlay(13 / 16f, 1f, 0f, 1f, 0f, 1f, overlayIndex, true, true, true, true, half == upper, half == lower, false));
                } else if (eastSide) {
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0f, 1f, 0f, 1f, texture, tintIndex, true, true, true, true, half == upper, half == lower));
                    if (overlayIndex > 0)
                        quads.addAll(ModelHelper.createOverlay(0f, 3 / 16f, 0f, 1f, 0f, 1f, overlayIndex, true, true, true, true, half == upper, half == lower, false));
                } else {
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0f, 3 / 16f, texture, tintIndex, true, true, true, true, half == upper, half == lower));
                    if (overlayIndex > 0)
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 3 / 16f, overlayIndex, true, true, true, true, half == upper, half == lower, false));
                }
            }
            if (design == 3) {
                if (northSide || southSide) {
                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 3; z++) {
                                xStripe = y < 4 || y > 11;
                                yStripe = x < 4 || x > 11;
                                upVisible = half == lower ? (y == 3 && !yStripe) : y == 15 || (y == 3 && !yStripe);
                                downVisible = half == upper ? (y == 12 && !yStripe) : y == 0 || (y == 12 && !yStripe);
                                wVisible = x == 0 || (x == 12 && !xStripe);
                                eVisible = x == 15 || (x == 3 && !xStripe);
                                nVisible = z == 0;
                                sVisible = z == 2;
                                if (xStripe || yStripe)
                                    quads.addAll(ModelHelper.createVoxel(x, y, z + zOffset, texture, tintIndex, nVisible, sVisible, eVisible, wVisible, upVisible, downVisible));
                                if ((xStripe || yStripe) && overlayIndex > 0)
                                    quads.addAll(ModelHelper.createOverlayVoxel(x, y, z + zOffset, overlayIndex, nVisible, sVisible, eVisible, wVisible, upVisible && y == 15, downVisible, false));
                                if (!xStripe && !yStripe && z == 1)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createVoxel(x, y, z + zOffset, innerTexture, tintIndex, true, true, false, false, false, false));
                                if (!xStripe && !yStripe && z == 1 && overlayIndex > 0)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlayVoxel(x, y, z + zOffset, overlayIndex, true, true, false, false, false, false, false));
                            }
                        }
                    }
                } else if (westSide || eastSide) {
                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 16; z++) {
                                zStripe = y < 4 || y > 11;
                                yStripe = z < 4 || z > 11;
                                upVisible = half == lower ? (y == 3 && !yStripe) : y == 15 || (y == 3 && !yStripe);
                                downVisible = half == upper ? (y == 12 && !yStripe) : y == 0 || (y == 12 && !yStripe);
                                nVisible = z == 0 || (z == 12 && !zStripe);
                                sVisible = z == 15 || (z == 3 && !zStripe);
                                wVisible = x == 0;
                                eVisible = x == 2;
                                if (zStripe || yStripe)
                                    quads.addAll(ModelHelper.createVoxel(x + xOffset, y, z, texture, tintIndex, nVisible, sVisible, eVisible, wVisible, upVisible, downVisible));
                                if ((zStripe || yStripe) && overlayIndex > 0)
                                    quads.addAll(ModelHelper.createOverlayVoxel(x + xOffset, y, z, overlayIndex, nVisible, sVisible, eVisible, wVisible, upVisible && y == 15, downVisible, false));
                                if (!zStripe && !yStripe && x == 1)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createVoxel(x + xOffset, y, z, innerTexture, tintIndex, false, false, true, true, false, false));
                                if (!zStripe && !yStripe && x == 1 && overlayIndex > 0)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlayVoxel(x + xOffset, y, z, overlayIndex, false, false, true, true, false, false, false));
                            }
                        }
                    }
                }
            }
            if (design == 4) {
                if (northSide || southSide) {
                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 3; z++) {
                                xStripe = y < 3 || y > 12 || y == 7 || y == 8;
                                yStripe = x < 3 || x > 12 || x == 7 || x == 8;
                                upVisible = half == lower ? ((y == 2 || y == 8) && !yStripe) : y == 15 || ((y == 2 || y == 8) && !yStripe);
                                downVisible = half == upper ? ((y == 7 || y == 13) && !yStripe) : y == 0 || ((y == 7 || y == 13) && !yStripe);
                                wVisible = x == 0 || ((x == 7 || x == 13) && !xStripe);
                                eVisible = x == 15 || ((x == 2 || x == 8) && !xStripe);
                                nVisible = z == 0;
                                sVisible = z == 2;
                                if (xStripe || yStripe)
                                    quads.addAll(ModelHelper.createVoxel(x, y, z + zOffset, texture, tintIndex, nVisible, sVisible, eVisible, wVisible, upVisible, downVisible));
                                if ((xStripe || yStripe) && overlayIndex > 0)
                                    quads.addAll(ModelHelper.createOverlayVoxel(x, y, z + zOffset, overlayIndex, nVisible, sVisible, eVisible, wVisible, upVisible && y == 15, downVisible, false));
                                if (!xStripe && !yStripe && z == 1)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createVoxel(x, y, z + zOffset, innerTexture, tintIndex, true, true, false, false, false, false));
                                if (!xStripe && !yStripe && z == 1 && overlayIndex > 0)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlayVoxel(x, y, z + zOffset, overlayIndex, true, true, false, false, false, false, false));
                            }
                        }
                    }
                }
                if (westSide || eastSide) {
                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 16; z++) {
                                zStripe = y < 3 || y > 12 || y == 7 || y == 8;
                                yStripe = z < 3 || z > 12 || z == 7 || z == 8;
                                upVisible = half == lower ? ((y == 2 || y == 8) && !yStripe) : y == 15 || ((y == 2 || y == 8) && !yStripe);
                                downVisible = half == upper ? ((y == 7 || y == 13) && !yStripe) : y == 0 || ((y == 7 || y == 13) && !yStripe);
                                nVisible = z == 0 || ((z == 7 || z == 13) && !zStripe);
                                sVisible = z == 15 || ((z == 2 || z == 8) && !zStripe);
                                wVisible = x == 0;
                                eVisible = x == 2;
                                if (zStripe || yStripe)
                                    quads.addAll(ModelHelper.createVoxel(x + xOffset, y, z, texture, tintIndex, nVisible, sVisible, eVisible, wVisible, upVisible, downVisible));
                                if ((zStripe || yStripe) && overlayIndex > 0)
                                    quads.addAll(ModelHelper.createOverlayVoxel(x + xOffset, y, z, overlayIndex, nVisible, sVisible, eVisible, wVisible, upVisible && y == 15, downVisible, false));
                                if (!zStripe && !yStripe && x == 1)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createVoxel(x + xOffset, y, z, innerTexture, tintIndex, false, false, true, true, false, false));
                                if (!zStripe && !yStripe && x == 1 && overlayIndex > 0)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlayVoxel(x + xOffset, y, z, overlayIndex, false, false, true, true, false, false, false));
                            }
                        }
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
    public boolean func_230044_c_() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    @Nonnull
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