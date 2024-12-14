package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.PaneFrameBlock;
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
 * @version 1.2 11/07/22
 */
public class IllusionPaneBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {

        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        Integer design = extraData.getData(FrameBlockTile.DESIGN);
        if (side != null) {
            return Collections.emptyList();
        }
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            if (state != null) {
                BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                List<TextureAtlasSprite> textureList = TextureHelper.getTextureFromModel(model, extraData, rand);
                if (textureList.size() == 0) {
                    //if (Minecraft.getInstance().player != null) {
                        //Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.block_not_available"), true);
                    //}
                    for (int i = 0; i < 6; i++) {
                        textureList.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("missing")));
                    }
                    //return Collections.emptyList();
                }
                TextureAtlasSprite glass = TextureHelper.getGlassTextures().get(extraData.getData(FrameBlockTile.GLASS_COLOR));
                int rotation = extraData.getData(FrameBlockTile.ROTATION);

                boolean north = state.getValue(PaneFrameBlock.NORTH);
                boolean east = state.getValue(PaneFrameBlock.EAST);
                boolean south = state.getValue(PaneFrameBlock.SOUTH);
                boolean west = state.getValue(PaneFrameBlock.WEST);
                boolean renderNorth = extraData.getData(FrameBlockTile.NORTH_VISIBLE);
                boolean renderEast = extraData.getData(FrameBlockTile.EAST_VISIBLE);
                boolean renderSouth = extraData.getData(FrameBlockTile.SOUTH_VISIBLE);
                boolean renderWest = extraData.getData(FrameBlockTile.WEST_VISIBLE);
                boolean renderUp = extraData.getData(FrameBlockTile.UP_VISIBLE);
                boolean renderDown = extraData.getData(FrameBlockTile.DOWN_VISIBLE);
                int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
                List<BakedQuad> quads = new ArrayList<>();
                if (design == 0) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, !north, !south, !east, !west, renderUp, renderDown, rotation));
                    if (north)
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 1f, 0f, 7 / 16f, mimic, model, extraData, rand, tintIndex, renderNorth, false, true, true, true, true, rotation)); //TODO rendering top and bottom
                    if (east)
                        quads.addAll(ModelHelper.createSixFaceCuboid(9 / 16f, 1f, 0f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, renderEast, false, true, true, rotation));
                    if (south)
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 1f, 9 / 16f, 1f, mimic, model, extraData, rand, tintIndex, false, renderSouth, true, true, true, true, rotation)); //TODO rendering top and bottom
                    if (west)
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 7 / 16f, 0f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, renderWest, true, true, rotation));
                } else if (design == 1) {
                    //Middle Post
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, !north, !south, !east, !west, false, renderDown, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 2 / 16f, 14 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, true, true, false, false, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, !north, !south, !east, !west, renderUp, false, rotation));
                    if (north) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 2 / 16f, 7 / 16f, mimic, model, extraData, rand, tintIndex, false, false, true, true, true, true, rotation)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 2 / 16f, 7 / 16f, mimic, model, extraData, rand, tintIndex, false, false, true, true, true, true, rotation)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex, renderNorth, false, true, true, false, renderDown, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 2 / 16f, 14 / 16f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex, renderNorth, true, true, true, false, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex, renderNorth, false, true, true, renderUp, false, rotation));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 2 / 16f, 7 / 16f, glass, -1, false, false, true, true, false, false));
                    }
                    if (east) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(9 / 16f, 14 / 16f, 0f, 2 / 14f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, false, true, true, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(9 / 16f, 14 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, false, true, true, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 1f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, renderEast, false, false, renderDown, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 1f, 2 / 16f, 14 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, renderEast, true, false, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 1f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, renderEast, false, renderUp, false, rotation));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(9 / 16f, 14 / 16f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, false, false, false, false));
                    }
                    if (south) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 9 / 16f, 14 / 16f, mimic, model, extraData, rand, tintIndex, false, false, true, true, true, true, rotation)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 9 / 16f, 14 / 16f, mimic, model, extraData, rand, tintIndex, false, false, true, true, true, true, rotation)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex, false, renderSouth, true, true, false, renderDown, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 2 / 16f, 14 / 16f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex, false, renderSouth, true, true, false, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex, false, renderSouth, true, true, renderUp, false, rotation));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 9 / 16f, 14 / 16f, glass, -1, false, false, true, true, false, false));
                    }
                    if (west) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 7 / 16f, 0f, 2 / 14f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, false, true, true, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 7 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, false, true, true, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, renderWest, false, renderDown, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 2 / 16f, 14 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, true, renderWest, false, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, renderWest, renderUp, false, rotation));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 7 / 16f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, false, false, false, false));
                    }
                } else if (design == 2) {
                    //Middle Post
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, !north, !south, !east, !west, true, renderDown, rotation));
                    quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, !north, !south, !east, !west, false, false));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, !north, !south, !east, !west, renderUp, true, rotation));
                    if (north) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 0f, 7 / 16f, mimic, model, extraData, rand, tintIndex, renderNorth, false, true, true, true, true, rotation)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 0f, 7 / 16f, mimic, model, extraData, rand, tintIndex, renderNorth, false, true, true, true, true, rotation)); //TODO rendering top and bottom
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 0f, 7.5f / 16f, glass, -1, renderNorth, false, true, true, false, false));
                    }
                    if (east) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(9 / 16f, 1f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, renderEast, false, true, true, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(9 / 16f, 1f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, renderEast, false, true, true, rotation));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(8.5f / 16f, 1f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, renderEast, false, false, false));
                    }
                    if (south) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 9 / 16f, 1f, mimic, model, extraData, rand, tintIndex, false, renderSouth, true, true, true, true, rotation)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 9 / 16f, 1f, mimic, model, extraData, rand, tintIndex, false, renderSouth, true, true, true, true, rotation)); //TODO rendering top and bottom
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 8.5f / 16f, 1f, glass, -1, false, renderSouth, true, true, false, false));
                    }
                    if (west) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 7 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, renderWest, true, true, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 7 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, renderWest, true, true, rotation));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(0f, 7.5f / 16f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, false, false, false, false));
                    }
                } else if (design == 3) {
                    //Middle Post
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, !north, !south, !east, !west, true, renderDown, rotation));
                    quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, !north, !south, !east, !west, false, false));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, !north, !south, !east, !west, renderUp, true, rotation));
                    if (north) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 2 / 16f, 7 / 16f, mimic, model, extraData, rand, tintIndex, false, false, true, true, true, true, rotation)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 2 / 16f, 7 / 16f, mimic, model, extraData, rand, tintIndex, false, false, true, true, true, true, rotation)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex, renderNorth, false, true, true, false, renderDown, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 2 / 16f, 14 / 16f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex, renderNorth, true, true, true, false, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex, renderNorth, false, true, true, renderUp, false, rotation));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 2 / 16f, 7.5f / 16f, glass, -1, false, false, true, true, false, false));
                    }
                    if (east) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(9 / 16f, 14 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, false, true, true, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(9 / 16f, 14 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, false, true, true, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 1f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, renderEast, false, false, renderDown, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 1f, 2 / 16f, 14 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, renderEast, true, false, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 1f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, renderEast, false, renderUp, false, rotation));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(8.5f / 16f, 14 / 16f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, false, false, false, false));
                    }
                    if (south) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 9 / 16f, 14 / 16f, mimic, model, extraData, rand, tintIndex, false, false, true, true, true, true, rotation)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 9 / 16f, 14 / 16f, mimic, model, extraData, rand, tintIndex, false, false, true, true, true, true, rotation)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex, false, renderSouth, true, true, false, renderDown, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 2 / 16f, 14 / 16f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex, false, renderSouth, true, true, false, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex, false, renderSouth, true, true, renderUp, false, rotation));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 8.5f / 16f, 14 / 16f, glass, -1, false, false, true, true, false, false));
                    }
                    if (west) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 7 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, false, true, true, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 7 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, false, true, true, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, renderWest, false, renderDown, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 2 / 16f, 14 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, true, renderWest, false, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, renderWest, renderUp, false, rotation));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 7.5f / 16f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, false, false, false, false));
                    }
                } else if (design == 4) {
                    //Middle Post
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 1 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, !north, !south, !east, !west, true, renderDown, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, 7f / 16f, 9f / 16f, mimic, model, extraData, rand, tintIndex, true, true, true, true, false, false, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 15 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, !north, !south, !east, !west, renderUp, true, rotation));
                    if (north) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 1 / 16f, 1 / 16f, 7 / 16f, mimic, model, extraData, rand, tintIndex, false, false, true, true, true, true, rotation)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 15 / 16f, 1f, 1 / 16f, 7 / 16f, mimic, model, extraData, rand, tintIndex, false, false, true, true, true, true, rotation)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 1 / 16f, 0f, 1 / 16f, mimic, model, extraData, rand, tintIndex, renderNorth, false, true, true, false, renderDown, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, 0f, 1 / 16f, mimic, model, extraData, rand, tintIndex, renderNorth, true, true, true, false, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 15 / 16f, 1f, 0f, 1 / 16f, mimic, model, extraData, rand, tintIndex, renderNorth, false, true, true, renderUp, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, 1 / 16f, 7 / 16f, mimic, model, extraData, rand, tintIndex, false, false, true, true, true, true, rotation));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 7 / 16f, glass, -1, false, false, true, true, false, false));
                    }
                    if (east) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(9 / 16f, 15 / 16f, 0f, 1 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, false, true, true, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(9 / 16f, 15 / 16f, 15 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, false, true, true, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(15 / 16f, 1f, 0f, 1 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, renderEast, false, false, renderDown, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(15 / 16f, 1f, 1 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, renderEast, true, false, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(15 / 16f, 1f, 15 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, renderEast, false, renderUp, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(9 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, false, true, true, rotation));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, false, false, false, false));
                    }
                    if (south) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 1 / 16f, 9 / 16f, 15 / 16f, mimic, model, extraData, rand, tintIndex, false, false, true, true, true, true, rotation)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 15 / 16f, 1f, 9 / 16f, 15 / 16f, mimic, model, extraData, rand, tintIndex, false, false, true, true, true, true, rotation)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 1 / 16f, 15 / 16f, 1f, mimic, model, extraData, rand, tintIndex, false, renderSouth, true, true, false, renderDown, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, 15 / 16f, 1f, mimic, model, extraData, rand, tintIndex, false, renderSouth, true, true, false, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 15 / 16f, 1f, 15 / 16f, 1f, mimic, model, extraData, rand, tintIndex, false, renderSouth, true, true, renderUp, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, 9 / 16f, 15 / 16f, mimic, model, extraData, rand, tintIndex, false, false, true, true, true, true, rotation));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 1 / 16f, 15 / 16f, 9 / 16f, 15 / 16f, glass, -1, false, false, true, true, false, false));
                    }
                    if (west) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 7 / 16f, 0f, 1 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, false, true, true, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 7 / 16f, 15 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, false, true, true, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1 / 16f, 0f, 1 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, renderWest, false, renderDown, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1 / 16f, 1 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, true, renderWest, false, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1 / 16f, 15 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, renderWest, renderUp, false, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 7 / 16f, 7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, true, true, false, false, true, true, rotation));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 1 / 16f, 15 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, false, false, false, false));
                    }
                }

                int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
                if (overlayIndex != 0) {
                    //TODO fix overlay for transparent blocks - then also use transparent overlay
                    quads.addAll(ModelHelper.createOverlay(7 / 16f, 9 / 16f, 0f, 1f, 7 / 16f, 9 / 16f, overlayIndex, !north, !south, !east, !west, renderUp, renderDown, true));
                    if (north)
                        quads.addAll(ModelHelper.createOverlay(7 / 16f, 9 / 16f, 0f, 1f, 0f, 7 / 16f, overlayIndex, renderNorth, false, true, true, renderUp, renderDown, true));
                    if (east)
                        quads.addAll(ModelHelper.createOverlay(9 / 16f, 1f, 0f, 1f, 7 / 16f, 9 / 16f, overlayIndex, true, true, renderEast, false, renderUp, renderDown, true));
                    if (south)
                        quads.addAll(ModelHelper.createOverlay(7 / 16f, 9 / 16f, 0f, 1f, 9 / 16f, 1f, overlayIndex, false, renderSouth, true, true, renderUp, renderDown, true));
                    if (west)
                        quads.addAll(ModelHelper.createOverlay(0f, 7 / 16f, 0f, 1f, 7 / 16f, 9 / 16f, overlayIndex, true, true, false, renderWest, renderUp, renderDown, true));
                }
                return quads;
            }
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
}
//========SOLI DEO GLORIA========//
