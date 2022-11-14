package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.DoorFrameBlock;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
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
 * @version 1.4 11/14/22
 */
public class DoorBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, RenderType renderType) {
        //get block saved in frame tile
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getMimicQuads(state, side, rand, extraData, model);
        }
        return Collections.emptyList();
    }

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, BakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }

        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        int tex = extraData.get(FrameBlockTile.TEXTURE);
        if (mimic != null && state != null && extraData.get(FrameBlockTile.DESIGN) != null && extraData.get(FrameBlockTile.DESIGN_TEXTURE) != null) {
            //get texture from block in tile entity and apply it to the quads
            List<TextureAtlasSprite> glassBlockList = TextureHelper.getGlassTextures();
            TextureAtlasSprite glass = glassBlockList.get(extraData.get(FrameBlockTile.GLASS_COLOR));
            List<TextureAtlasSprite> textureList = TextureHelper.getTextureFromModel(model, extraData, rand);
            TextureAtlasSprite texture;
            if (textureList.size() <= tex) {
                extraData.derive().with(FrameBlockTile.TEXTURE, 0);
                tex = 0;
            }
            if (textureList.size() == 0) {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.displayClientMessage(Component.translatable("message.blockcarpentry.block_not_available"), true);
                }
                return Collections.emptyList();
            }
            texture = textureList.get(tex);
            List<BakedQuad> quads = new ArrayList<>();
            Direction dir = state.getValue(DoorBlock.FACING);
            boolean open = state.getValue(DoorFrameBlock.OPEN);
            DoorHingeSide hinge = state.getValue(DoorFrameBlock.HINGE);
            Direction west = Direction.WEST;
            Direction east = Direction.EAST;
            Direction north = Direction.NORTH;
            Direction south = Direction.SOUTH;
            DoorHingeSide left = DoorHingeSide.LEFT;
            DoorHingeSide right = DoorHingeSide.RIGHT;
            int design = extraData.get(FrameBlockTile.DESIGN);//int design = state.getValue(DoorFrameBlock.DESIGN);
            DoubleBlockHalf half = state.getValue(DoorBlock.HALF);
            DoubleBlockHalf lower = DoubleBlockHalf.LOWER;
            DoubleBlockHalf upper = DoubleBlockHalf.UPPER;
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            TextureAtlasSprite innerTexture = design != 1 ? glass : texture;
            boolean northSide = (dir == north && !open && hinge == right) || (dir == east && open && hinge == right) || (dir == west && open && hinge == left) || (dir == north && !open && hinge == left);
            boolean westSide = (dir == west && !open && hinge == right) || (dir == north && open && hinge == right) || (dir == south && open && hinge == left) || (dir == west && !open && hinge == left);
            boolean eastSide = (dir == south && open && hinge == right) || (dir == east && !open && hinge == right) || (dir == east && !open && hinge == left) || (dir == north && open && hinge == left);
            if (design == 0 || design == 1) {
                if (northSide) {
                    if (half == lower) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 4 / 16f, 1f, 13 / 16f, 1f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 4 / 16f, 1f, 13 / 16f, 1f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 0, 4 / 16f, 13 / 16f, 1f, texture, tintIndex, true, true, false, true, false, true));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 0, 4 / 16f, 13 / 16f, 1f, texture, tintIndex, true, true, true, false, false, true));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 4 / 16f, 1f, 14 / 16f, 15 / 16f, innerTexture, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 4 / 16f, 13 / 16f, 1f, texture, tintIndex, true, true, false, false, true, true));
                    }
                    if (half == upper) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 12 / 16f, 1f, 13 / 16f, 1f, texture, tintIndex, true, true, false, true, true, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 12 / 16f, 1f, 13 / 16f, 1f, texture, tintIndex, true, true, true, false, true, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 0, 12 / 16f, 13 / 16f, 1f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 0, 12 / 16f, 13 / 16f, 1f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 12 / 16f, 14 / 16f, 15 / 16f, innerTexture, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 12 / 16f, 1f, 13 / 16f, 1f, texture, tintIndex, true, true, false, false, true, true));
                    }
                } else if (westSide) {
                    if (half == lower) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 4 / 16f, 1f, 12 / 16f, 1f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 4 / 16f, 1f, 0f, 4 / 16f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0, 4 / 16f, 12 / 16f, 1f, texture, tintIndex, false, true, true, true, false, true));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0, 4 / 16f, 0f, 4 / 16f, texture, tintIndex, true, false, true, true, false, true));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(14 / 16f, 15 / 16f, 4 / 16f, 1f, 4 / 16f, 12 / 16f, innerTexture, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0f, 4 / 16f, 4 / 16f, 12 / 16f, texture, tintIndex, false, false, true, true, true, true));
                    }
                    if (half == upper) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 12 / 16f, 1f, 12 / 16f, 1f, texture, tintIndex, false, true, true, true, true, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 12 / 16f, 1f, 0f, 4 / 16f, texture, tintIndex, true, false, true, true, true, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0, 12 / 16f, 12 / 16f, 1f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0, 12 / 16f, 0f, 4 / 16f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(14 / 16f, 15 / 16f, 0f, 12 / 16f, 4 / 16f, 12 / 16f, innerTexture, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 12 / 16f, 1f, 4 / 16f, 12 / 16f, texture, tintIndex, false, false, true, true, true, true));
                    }
                } else if (eastSide) {
                    if (half == lower) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 4 / 16f, 1f, 12 / 16f, 1f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 4 / 16f, 1f, 0f, 4 / 16f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0, 4 / 16f, 12 / 16f, 1f, texture, tintIndex, false, true, true, true, false, true));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0, 4 / 16f, 0f, 4 / 16f, texture, tintIndex, true, false, true, true, false, true));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(1 / 16f, 2 / 16f, 4 / 16f, 1f, 4 / 16f, 12 / 16f, innerTexture, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0f, 4 / 16f, 4 / 16f, 12 / 16f, texture, tintIndex, false, false, true, true, true, true));
                    }
                    if (half == upper) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 12 / 16f, 1f, 12 / 16f, 1f, texture, tintIndex, false, true, true, true, true, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 12 / 16f, 1f, 0f, 4 / 16f, texture, tintIndex, true, false, true, true, true, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0, 12 / 16f, 12 / 16f, 1f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0, 12 / 16f, 0f, 4 / 16f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(1 / 16f, 2 / 16f, 0f, 12 / 16f, 4 / 16f, 12 / 16f, innerTexture, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 12 / 16f, 1f, 4 / 16f, 12 / 16f, texture, tintIndex, false, false, true, true, true, true));
                    }
                } else {
                    if (half == lower) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 4 / 16f, 1f, 0f, 3 / 16f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 4 / 16f, 1f, 0f, 3 / 16f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 0, 4 / 16f, 0f, 3 / 16f, texture, tintIndex, true, true, false, true, false, true));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 0, 4 / 16f, 0f, 3 / 16f, texture, tintIndex, true, true, true, false, false, true));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 4 / 16f, 1f, 1 / 16f, 2 / 16f, innerTexture, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 4 / 16f, 0f, 3 / 16f, texture, tintIndex, true, true, false, false, true, true));
                    }
                    if (half == upper) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 12 / 16f, 1f, 0f, 3 / 16f, texture, tintIndex, true, true, false, true, true, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 12 / 16f, 1f, 0f, 3 / 16f, texture, tintIndex, true, true, true, false, true, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 0, 12 / 16f, 0f, 3 / 16f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 0, 12 / 16f, 0f, 3 / 16f, texture, tintIndex, true, true, true, true, false, false));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 12 / 16f, 1 / 16f, 2 / 16f, innerTexture, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 12 / 16f, 1f, 0f, 3 / 16f, texture, tintIndex, true, true, false, false, true, true));
                    }
                }
            }
            if (design == 3) {
                if (northSide) {
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 0, 4 / 16f, 13 / 16f, 1f, texture, tintIndex, true, true, false, true, false, half == lower));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 4 / 16f, 12 / 16f, 13 / 16f, 1f, texture, tintIndex, true, true, true, true, false, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 12 / 16f, 1f, 13 / 16f, 1f, texture, tintIndex, true, true, false, true, half == upper, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 0, 4 / 16f, 13 / 16f, 1f, texture, tintIndex, true, true, true, false, false, half == lower));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 4 / 16f, 12 / 16f, 13 / 16f, 1f, texture, tintIndex, true, true, true, true, false, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 12 / 16f, 1f, 13 / 16f, 1f, texture, tintIndex, true, true, true, false, half == upper, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 4 / 16f, 12 / 16f, 14 / 16f, 15 / 16f, innerTexture, -1));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 4 / 16f, 13 / 16f, 1f, texture, tintIndex, true, true, false, false, true, half == lower));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 12 / 16f, 1f, 13 / 16f, 1f, texture, tintIndex, true, true, false, false, half == upper, true));
                } else if (westSide) {
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0, 4 / 16f, 12 / 16f, 1f, texture, tintIndex, false, true, true, true, false, half == lower));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 4 / 16f, 12 / 16f, 12 / 16f, 1f, texture, tintIndex, true, true, true, true, false, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 12 / 16f, 1f, 12 / 16f, 1f, texture, tintIndex, false, true, true, true, half == upper, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0, 4 / 16f, 0f, 4 / 16f, texture, tintIndex, true, false, true, true, false, half == lower));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 4 / 16f, 12 / 16f, 0f, 4 / 16f, texture, tintIndex, true, true, true, true, false, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 12 / 16f, 1f, 0f, 4 / 16f, texture, tintIndex, true, false, true, true, half == upper, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(14 / 16f, 15 / 16f, 4 / 16f, 12 / 16f, 4 / 16f, 12 / 16f, innerTexture, -1));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0f, 4 / 16f, 4 / 16f, 12 / 16f, texture, tintIndex, false, false, true, true, true, half == lower));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 12 / 16f, 1f, 4 / 16f, 12 / 16f, texture, tintIndex, false, false, true, true, half == upper, true));
                } else if (eastSide) {
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0, 4 / 16f, 12 / 16f, 1f, texture, tintIndex, false, true, true, true, false, half == lower));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 4 / 16f, 12 / 16f, 12 / 16f, 1f, texture, tintIndex, true, true, true, true, false, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 12 / 16f, 1f, 12 / 16f, 1f, texture, tintIndex, false, true, true, true, half == upper, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0, 4 / 16f, 0f, 4 / 16f, texture, tintIndex, true, false, true, true, false, half == lower));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 4 / 16f, 12 / 16f, 0f, 4 / 16f, texture, tintIndex, true, true, true, true, false, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 12 / 16f, 1f, 0f, 4 / 16f, texture, tintIndex, true, false, true, true, half == upper, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(1 / 16f, 2 / 16f, 4 / 16f, 12 / 16f, 4 / 16f, 12 / 16f, innerTexture, -1));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0f, 4 / 16f, 4 / 16f, 12 / 16f, texture, tintIndex, false, false, true, true, true, half == lower));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 12 / 16f, 1f, 4 / 16f, 12 / 16f, texture, tintIndex, false, false, true, true, half == upper, true));
                } else {
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 0, 4 / 16f, 0f, 3 / 16f, texture, tintIndex, true, true, false, true, false, half == lower));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 4 / 16f, 12 / 16f, 0f, 3 / 16f, texture, tintIndex, true, true, true, true, false, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 12 / 16f, 1f, 0f, 3 / 16f, texture, tintIndex, true, true, false, true, half == upper, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 0, 4 / 16f, 0f, 3 / 16f, texture, tintIndex, true, true, true, false, false, half == lower));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 4 / 16f, 12 / 16f, 0f, 3 / 16f, texture, tintIndex, true, true, true, true, false, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 12 / 16f, 1f, 0f, 3 / 16f, texture, tintIndex, true, true, true, false, half == upper, false));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 4 / 16f, 12 / 16f, 1 / 16f, 2 / 16f, innerTexture, -1));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 4 / 16f, 0f, 3 / 16f, texture, tintIndex, true, true, false, false, true, half == lower));
                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 12 / 16f, 1f, 0f, 3 / 16f, texture, tintIndex, true, true, false, false, half == upper, true));
                }
            }
            if (design == 2) {
                if (northSide) {
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 13 / 16f, 1f, texture, tintIndex, true, true, true, true, half == upper, half == lower));
                } else if (westSide) {
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0f, 1f, 0f, 1f, texture, tintIndex, true, true, true, true, half == upper, half == lower));
                } else if (eastSide) {
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0f, 1f, 0f, 1f, texture, tintIndex, true, true, true, true, half == upper, half == lower));
                } else {
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0f, 3 / 16f, texture, tintIndex, true, true, true, true, half == upper, half == lower));
                }
            }
            if (design == 4) {
                if (northSide) {
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, 14 / 16f, 15 / 16f, innerTexture, -1));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 0f, 3 / 16f, 13 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 13 / 16f, 1f, 13 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 3 / 16f, 13 / 16f, 13 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 7 / 16f, 9 / 16f, 13 / 16f, 1f, texture, tintIndex));
                } else if (westSide) {
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(14 / 16f, 15 / 16f, 3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, innerTexture, -1));
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0f, 3 / 16f, 3 / 16f, 13 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 13 / 16f, 1f, 3 / 16f, 13 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 3 / 16f, 13 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 7 / 16f, 9 / 16f, 3 / 16f, 13 / 16f, texture, tintIndex));
                } else if (eastSide) {
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(1 / 16f, 2 / 16f, 3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, innerTexture, -1));
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0f, 3 / 16f, 3 / 16f, 13 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 13 / 16f, 1f, 3 / 16f, 13 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 3 / 16f, 13 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 7 / 16f, 9 / 16f, 3 / 16f, 13 / 16f, texture, tintIndex));
                } else {
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, 1 / 16f, 2 / 16f, innerTexture, -1));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 0f, 3 / 16f, 0f, 3 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 13 / 16f, 1f, 0f, 3 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 3 / 16f, 13 / 16f, 0f, 3 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 7 / 16f, 9 / 16f, 0f, 3 / 16f, texture, tintIndex));
                }
            }
            int overlayIndex = extraData.get(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                if (northSide) {
                    quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 13 / 16f, 1f, overlayIndex));
                } else if (westSide) {
                    quads.addAll(ModelHelper.createOverlay(13 / 16f, 1f, 0f, 1f, 0f, 1f, overlayIndex));
                } else if (eastSide) {
                    quads.addAll(ModelHelper.createOverlay(0f, 3 / 16f, 0f, 1f, 0f, 1f, overlayIndex));
                } else {
                    quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 3 / 16f, overlayIndex));
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