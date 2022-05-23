package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.WallFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WallSide;
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
 * @version 1.0 05/23/22
 */
public class WallBakedModel implements IDynamicBakedModel {

    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
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
    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, IModelData extraData, BakedModel model) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        Integer design = extraData.getData(FrameBlockTile.DESIGN);
        if (side != null) {
            return Collections.emptyList();
        }
        if (mimic != null && state != null) {
            int index = extraData.getData(FrameBlockTile.TEXTURE);
            List<TextureAtlasSprite> texture = TextureHelper.getTextureFromModel(model, extraData, rand);
            if (texture.size() <= index) {
                extraData.setData(FrameBlockTile.TEXTURE, 0);
                index = 0;
            }
            if (texture.size() == 0) {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.block_not_available"), true);
                }
                return Collections.emptyList();
            }
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            List<BakedQuad> quads = new ArrayList<>();

            //Create middle post
            if (state.getValue(WallFrameBlock.UP)) {
                quads.addAll(ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 1f, 4 / 16f, 12 / 16f, texture.get(index), tintIndex));
            } else {
                quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 14 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
            }
            if (state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.TALL && state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.TALL || state.getValue(WallFrameBlock.EAST_WALL) == WallSide.TALL && state.getValue(WallFrameBlock.WEST_WALL) == WallSide.TALL) {
                quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 1f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
            } else if (state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.NONE && state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.NONE || state.getValue(WallFrameBlock.EAST_WALL) == WallSide.NONE && state.getValue(WallFrameBlock.WEST_WALL) == WallSide.NONE) {
                quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 14 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
            }

            //determine wall height - if block above this block is also a wall with connections, the wall height of this block right here needs to be a full block - otherwise just 14/16
            float height_north = 1f;
            float height_east = 1f;
            float height_south = 1f;
            float height_west = 1f;
            if (state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.LOW)
                height_north = 14 / 16f;
            if (state.getValue(WallFrameBlock.EAST_WALL) == WallSide.LOW)
                height_east = 14 / 16f;
            if (state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.LOW)
                height_south = 14 / 16f;
            if (state.getValue(WallFrameBlock.WEST_WALL) == WallSide.LOW)
                height_west = 14 / 16f;

            //classic wall design
            if (design == 0) {
                if (state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, height_north, 0f, 5 / 16f, texture.get(index), tintIndex));
                }
                if (state.getValue(WallFrameBlock.EAST_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.EAST_WALL) == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 1f, 0f, height_east, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
                if (state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, height_south, 11 / 16f, 1f, texture.get(index), tintIndex));
                }
                if (state.getValue(WallFrameBlock.WEST_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.WEST_WALL) == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(0f, 5 / 16f, 0f, height_west, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
            }
            //wall with hole
            if (design == 1) {
                if (state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 4 / 16f, 0f, 5 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_north, 0f, 5 / 16f, texture.get(index), tintIndex));
                }
                if (state.getValue(WallFrameBlock.EAST_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.EAST_WALL) == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 1f, 0f, 4 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 1f, 10 / 16f, height_east, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
                if (state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 4 / 16f, 11 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_south, 11 / 16f, 1f, texture.get(index), tintIndex));
                }
                if (state.getValue(WallFrameBlock.WEST_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.WEST_WALL) == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(0f, 5 / 16f, 0f, 4 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 5 / 16f, 10 / 16f, height_west, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
            }
            //fence-like design
            if (design == 2) {
                if (state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 3 / 16f, 7 / 16f, 0f, 5 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_north, 0f, 5 / 16f, texture.get(index), tintIndex));
                }
                if (state.getValue(WallFrameBlock.EAST_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.EAST_WALL) == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 1f, 3 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 1f, 10 / 16f, height_east, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
                if (state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 3 / 16f, 7 / 16f, 11 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_south, 11 / 16f, 1f, texture.get(index), tintIndex));
                }
                if (state.getValue(WallFrameBlock.WEST_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.WEST_WALL) == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(0f, 5 / 16f, 3 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 5 / 16f, 10 / 16f, height_west, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
            }
            //heart shaped holes in wall
            if (design == 3) {
                if (state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.TALL) {
                    //Heart form
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 12 / 16f, height_north, 0f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 3 / 16f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 0f, 1 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 8 / 16f, 9 / 16f, 3 / 16f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 7 / 16f, 8 / 16f, 2 / 16f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 6 / 16f, 7 / 16f, 1 / 16f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 6 / 16f, 0f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, height_north, 4 / 16f, 5 / 16f, texture.get(index), tintIndex));

                }
                if (state.getValue(WallFrameBlock.EAST_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.EAST_WALL) == WallSide.TALL) {
                    //Heart form
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 1f, 12 / 16f, height_east, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 13 / 16f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 13 / 16f, 8 / 16f, 9 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 14 / 16f, 7 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 15 / 16f, 6 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 1f, 0f, 6 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 12 / 16f, 0f, height_east, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
                if (state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.TALL) {
                    //Heart form
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 12 / 16f, height_south, 12 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 15 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 12 / 16f, 13 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 8 / 16f, 9 / 16f, 12 / 16f, 13 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 7 / 16f, 8 / 16f, 12 / 16f, 14 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 6 / 16f, 7 / 16f, 12 / 16f, 15 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 6 / 16f, 12 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, height_south, 11 / 16f, 12 / 16f, texture.get(index), tintIndex));
                }
                if (state.getValue(WallFrameBlock.WEST_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.WEST_WALL) == WallSide.TALL) {
                    //Heart form
                    quads.addAll(ModelHelper.createCuboid(0f, 4 / 16f, 12 / 16f, height_west, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 4 / 16f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 1 / 16f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 4 / 16f, 8 / 16f, 9 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(2 / 16f, 4 / 16f, 7 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(1 / 16f, 4 / 16f, 6 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 4 / 16f, 0f, 6 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(4 / 16f, 5 / 16f, 0f, height_west, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
            }
            //cross shaped holes in wall
            if (design == 4) {
                if (state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.TALL) {
                    //Cross form
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 12 / 16f, height_north, 0f, 5 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, 12 / 16f, 1 / 16f, 5 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 8 / 16f, 10 / 16f, 3 / 16f, 5 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 2 / 16f, 8 / 16f, 1 / 16f, 5 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 2 / 16f, 0f, 5 / 16f, texture.get(index), tintIndex));
                }
                if (state.getValue(WallFrameBlock.EAST_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.EAST_WALL) == WallSide.TALL) {
                    //Cross form
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 1f, 12 / 16f, height_east, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 15 / 16f, 10 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 13 / 16f, 8 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 15 / 16f, 2 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 1f, 0f, 2 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
                if (state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.TALL) {
                    //Cross form
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 12 / 16f, height_south, 11 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, 12 / 16f, 11 / 16f, 15 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 8 / 16f, 10 / 16f, 11 / 16f, 13 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 2 / 16f, 8 / 16f, 11 / 16f, 15 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 2 / 16f, 11 / 16f, 1f, texture.get(index), tintIndex));
                }
                if (state.getValue(WallFrameBlock.WEST_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.WEST_WALL) == WallSide.TALL) {
                    //Cross form
                    quads.addAll(ModelHelper.createCuboid(0f, 5 / 16f, 12 / 16f, height_west, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(1 / 16f, 5 / 16f, 10 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 5 / 16f, 8 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(1 / 16f, 5 / 16f, 2 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 5 / 16f, 0f, 2 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
            }
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                if (state.getValue(WallFrameBlock.UP) && !(state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.TALL || state.getValue(WallFrameBlock.EAST_WALL) == WallSide.TALL || state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.TALL || state.getValue(WallFrameBlock.WEST_WALL) == WallSide.TALL)) {
                    quads.addAll(ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 1f, 4 / 16f, 12 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createOverlay(4 / 16f, 12 / 16f, 0f, 1f, 4 / 16f, 12 / 16f, overlayIndex));
                } else {
                    if (state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.EAST_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.LOW || state.getValue(WallFrameBlock.WEST_WALL) == WallSide.LOW)
                        quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 0f, 14 / 16f, 5 / 16f, 11 / 16f, overlayIndex));
                }
                if (state.getValue(WallFrameBlock.NORTH_WALL) == WallSide.LOW) {
                    //quads.retainAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, height_north, 0f, 5 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 0f, height_north, 0f, 5 / 16f, overlayIndex));
                }
                if (state.getValue(WallFrameBlock.EAST_WALL) == WallSide.LOW) {
                    //quads.retainAll(ModelHelper.createCuboid(11 / 16f, 1f, 0f, height_east, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createOverlay(11 / 16f, 1f, 0f, height_east, 5 / 16f, 11 / 16f, overlayIndex));
                }
                if (state.getValue(WallFrameBlock.SOUTH_WALL) == WallSide.LOW) {
                    quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 0f, height_south, 11 / 16f, 1f, overlayIndex));
                }
                if (state.getValue(WallFrameBlock.WEST_WALL) == WallSide.LOW) {
                    quads.addAll(ModelHelper.createOverlay(0f, 5 / 16f, 0f, height_west, 5 / 16f, 11 / 16f, overlayIndex));
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
}
//========SOLI DEO GLORIA========//