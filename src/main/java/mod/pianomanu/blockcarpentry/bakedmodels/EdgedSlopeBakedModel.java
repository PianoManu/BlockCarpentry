package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
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

public class EdgedSlopeBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

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

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, IModelData extraData) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        Integer design = extraData.getData(FrameBlockTile.DESIGN);
        if (mimic != null && state != null) {
            List<TextureAtlasSprite> texture = TextureHelper.getTextureListFromBlock(mimic.getBlock());
            int index = extraData.getData(FrameBlockTile.TEXTURE);
            if (index >= texture.size()) {
                index = 0;
            }
            int tintIndex = -1;
            if (mimic.getBlock() instanceof GrassBlock) {
                tintIndex = 1;
            }
            List<BakedQuad> quads = new ArrayList<>();
            Half half = state.get(StairsBlock.HALF);
            Half lower = Half.BOTTOM;
            Half upper = Half.TOP;
            Direction direction = state.get(StairsBlock.FACING);
            Direction north = Direction.NORTH;
            Direction east = Direction.EAST;
            Direction south = Direction.SOUTH;
            Direction west = Direction.WEST;
            StairsShape shape = state.get(StairsBlock.SHAPE);
            StairsShape straight = StairsShape.STRAIGHT;
            StairsShape innerLeft = StairsShape.INNER_LEFT;
            StairsShape innerRight = StairsShape.INNER_RIGHT;
            StairsShape outerLeft = StairsShape.OUTER_LEFT;
            StairsShape outerRight = StairsShape.OUTER_RIGHT;

            if (half==lower) {
                quads.addAll(ModelHelper.createCuboid(0f,1f,0f,1/16f, 0f, 1f, texture.get(index),tintIndex));
                if(shape == straight) {
                    if (direction == north) {
                        quads.addAll(ModelHelper.createCuboid(0f,1f,1/16f,2/16f,0f,15/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,2/16f,3/16f,0f,14/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,3/16f,4/16f,0f,13/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,4/16f,5/16f,0f,12/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,5/16f,6/16f,0f,11/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,6/16f,7/16f,0f,10/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,7/16f,8/16f,0f,9/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,8/16f,9/16f,0f,8/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,9/16f,10/16f,0f,7/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,10/16f,11/16f,0f,6/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,11/16f,12/16f,0f,5/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,12/16f,13/16f,0f,4/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,13/16f,14/16f,0f,3/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,14/16f,15/16f,0f,2/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,15/16f,1f,0f,1/16f, texture.get(index), tintIndex));
                    }
                    if (direction == east) {
                        quads.addAll(ModelHelper.createCuboid(1/16f,1f,1/16f,2/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2/16f,1f,2/16f,3/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(3/16f,1f,3/16f,4/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(4/16f,1f,4/16f,5/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(5/16f,1f,5/16f,6/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(6/16f,1f,6/16f,7/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(7/16f,1f,7/16f,8/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(8/16f,1f,8/16f,9/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(9/16f,1f,9/16f,10/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(10/16f,1f,10/16f,11/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(11/16f,1f,11/16f,12/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(12/16f,1f,12/16f,13/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13/16f,1f,13/16f,14/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14/16f,1f,14/16f,15/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15/16f,1f,15/16f,1f,0f,1f, texture.get(index), tintIndex));
                    }
                    if (direction == south) {
                        quads.addAll(ModelHelper.createCuboid(0f,1f,1/16f,2/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,2/16f,3/16f,2/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,3/16f,4/16f,3/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,4/16f,5/16f,4/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,5/16f,6/16f,5/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,6/16f,7/16f,6/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,7/16f,8/16f,7/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,8/16f,9/16f,8/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,9/16f,10/16f,9/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,10/16f,11/16f,10/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,11/16f,12/16f,11/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,12/16f,13/16f,12/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,13/16f,14/16f,13/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,14/16f,15/16f,14/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,15/16f,1f,15/16f,1f, texture.get(index), tintIndex));
                    }
                    if (direction == west) {
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,1/16f,2/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,14/16f,2/16f,3/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,13/16f,3/16f,4/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,12/16f,4/16f,5/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,11/16f,5/16f,6/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,10/16f,6/16f,7/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,9/16f,7/16f,8/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,8/16f,8/16f,9/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,7/16f,9/16f,10/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,6/16f,10/16f,11/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,5/16f,11/16f,12/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,4/16f,12/16f,13/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,3/16f,13/16f,14/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,2/16f,14/16f,15/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1/16f,15/16f,1f,0f,1f, texture.get(index), tintIndex));
                    }
                }
                if (shape == outerLeft || shape == outerRight) {
                    if ((direction == north && shape == outerLeft) || (direction == west && shape == outerRight)) {
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,1/16f,2/16f,0f,15/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,14/16f,2/16f,3/16f,0f,14/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,13/16f,3/16f,4/16f,0f,13/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,12/16f,4/16f,5/16f,0f,12/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,11/16f,5/16f,6/16f,0f,11/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,10/16f,6/16f,7/16f,0f,10/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,9/16f,7/16f,8/16f,0f,9/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,8/16f,8/16f,9/16f,0f,8/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,7/16f,9/16f,10/16f,0f,7/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,6/16f,10/16f,11/16f,0f,6/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,5/16f,11/16f,12/16f,0f,5/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,4/16f,12/16f,13/16f,0f,4/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,3/16f,13/16f,14/16f,0f,3/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,2/16f,14/16f,15/16f,0f,2/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1/16f,15/16f,1f,0f,1/16f, texture.get(index), tintIndex));
                    }
                    if ((direction == east && shape == outerLeft) || (direction == north && shape == outerRight)) {
                        quads.addAll(ModelHelper.createCuboid(1/16f,1f,1/16f,2/16f,0f,15/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2/16f,1f,2/16f,3/16f,0f,14/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(3/16f,1f,3/16f,4/16f,0f,13/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(4/16f,1f,4/16f,5/16f,0f,12/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(5/16f,1f,5/16f,6/16f,0f,11/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(6/16f,1f,6/16f,7/16f,0f,10/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(7/16f,1f,7/16f,8/16f,0f,9/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(8/16f,1f,8/16f,9/16f,0f,8/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(9/16f,1f,9/16f,10/16f,0f,7/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(10/16f,1f,10/16f,11/16f,0f,6/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(11/16f,1f,11/16f,12/16f,0f,5/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(12/16f,1f,12/16f,13/16f,0f,4/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13/16f,1f,13/16f,14/16f,0f,3/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14/16f,1f,14/16f,15/16f,0f,2/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15/16f,1f,15/16f,1f,0f,1/16f, texture.get(index), tintIndex));
                    }
                    if ((direction == south && shape == outerLeft) || (direction == east && shape == outerRight)) {
                        quads.addAll(ModelHelper.createCuboid(1/16f,1f,1/16f,2/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2/16f,1f,2/16f,3/16f,2/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(3/16f,1f,3/16f,4/16f,3/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(4/16f,1f,4/16f,5/16f,4/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(5/16f,1f,5/16f,6/16f,5/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(6/16f,1f,6/16f,7/16f,6/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(7/16f,1f,7/16f,8/16f,7/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(8/16f,1f,8/16f,9/16f,8/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(9/16f,1f,9/16f,10/16f,9/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(10/16f,1f,10/16f,11/16f,10/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(11/16f,1f,11/16f,12/16f,11/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(12/16f,1f,12/16f,13/16f,12/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13/16f,1f,13/16f,14/16f,13/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14/16f,1f,14/16f,15/16f,14/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15/16f,1f,15/16f,1f,15/16f,1f, texture.get(index), tintIndex));
                    }
                    if ((direction == west && shape == outerLeft) || (direction == south && shape == outerRight)) {
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,1/16f,2/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,14/16f,2/16f,3/16f,2/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,13/16f,3/16f,4/16f,3/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,12/16f,4/16f,5/16f,4/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,11/16f,5/16f,6/16f,5/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,10/16f,6/16f,7/16f,6/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,9/16f,7/16f,8/16f,7/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,8/16f,8/16f,9/16f,8/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,7/16f,9/16f,10/16f,9/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,6/16f,10/16f,11/16f,10/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,5/16f,11/16f,12/16f,11/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,4/16f,12/16f,13/16f,12/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,3/16f,13/16f,14/16f,13/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,2/16f,14/16f,15/16f,14/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1/16f,15/16f,1f,15/16f,1f, texture.get(index), tintIndex));
                    }
                }
                if (shape == innerLeft || shape == innerRight) {
                    //quads.addAll(ModelHelper.createCutoutCuboid(0f,1f,1/16f,2/16f, 0f, 1f, 14/16f,2/16f,14/16f, texture.get(index), tintIndex));
                    //well fuck it
                    if ((shape == innerLeft && direction == north) || shape == innerRight && direction == west) {
                        //North
                        quads.addAll(ModelHelper.createCuboid(0f,1f,1/16f,2/16f,0f,15/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,2/16f,3/16f,0f,14/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,3/16f,4/16f,0f,13/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,4/16f,5/16f,0f,12/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,5/16f,6/16f,0f,11/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,6/16f,7/16f,0f,10/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,7/16f,8/16f,0f,9/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,8/16f,9/16f,0f,8/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,9/16f,10/16f,0f,7/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,10/16f,11/16f,0f,6/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,11/16f,12/16f,0f,5/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,12/16f,13/16f,0f,4/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,13/16f,14/16f,0f,3/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,14/16f,15/16f,0f,2/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,15/16f,1f,0f,1/16f, texture.get(index), tintIndex));
                        //West
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,1/16f,2/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,14/16f,2/16f,3/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,13/16f,3/16f,4/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,12/16f,4/16f,5/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,11/16f,5/16f,6/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,10/16f,6/16f,7/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,9/16f,7/16f,8/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,8/16f,8/16f,9/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,7/16f,9/16f,10/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,6/16f,10/16f,11/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,5/16f,11/16f,12/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,4/16f,12/16f,13/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,3/16f,13/16f,14/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,2/16f,14/16f,15/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1/16f,15/16f,1f,1/16f,1f, texture.get(index), tintIndex));
                    }
                    if ((shape == innerLeft && direction == east) || shape == innerRight && direction == north) {
                        //East
                        quads.addAll(ModelHelper.createCuboid(1/16f,15/16f,1/16f,2/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2/16f,15/16f,2/16f,3/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(3/16f,15/16f,3/16f,4/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(4/16f,15/16f,4/16f,5/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(5/16f,15/16f,5/16f,6/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(6/16f,15/16f,6/16f,7/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(7/16f,15/16f,7/16f,8/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(8/16f,15/16f,8/16f,9/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(9/16f,15/16f,9/16f,10/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(10/16f,15/16f,10/16f,11/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(11/16f,15/16f,11/16f,12/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(12/16f,15/16f,12/16f,13/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13/16f,15/16f,13/16f,14/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14/16f,15/16f,14/16f,15/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15/16f,15/16f,15/16f,1f,1/16f,1f, texture.get(index), tintIndex));
                        //North
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,1/16f,2/16f,1/16f,15/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,2/16f,3/16f,1/16f,14/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,3/16f,4/16f,1/16f,13/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,4/16f,5/16f,1/16f,12/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,5/16f,6/16f,1/16f,11/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,6/16f,7/16f,1/16f,10/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,7/16f,8/16f,1/16f,9/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,8/16f,9/16f,1/16f,8/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,9/16f,10/16f,1/16f,7/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,10/16f,11/16f,1/16f,6/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,11/16f,12/16f,1/16f,5/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,12/16f,13/16f,1/16f,4/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,13/16f,14/16f,1/16f,3/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,14/16f,15/16f,1/16f,2/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,15/16f,1f,1/16f,1/16f, texture.get(index), tintIndex));
                        //Back sides - workaround to fix some weird rendering issues
                        quads.addAll(ModelHelper.createCuboid(0f,1f,0f,1f,0f,0f,texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1f,1f,0f,1f,0f,1f,texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,0f,0f,1f,0f,1/16f,texture.get(index),tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15/16f,1f,0f,1f,1f,1f,texture.get(index),tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15/16f,1f,1f,1f,0f,1f,texture.get(index),tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,1f,1f,0f,1/16f,texture.get(index),tintIndex));
                    }
                    if ((shape == innerLeft && direction == south) || shape == innerRight && direction == east) {
                        //South
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,1/16f,2/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,2/16f,3/16f,2/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,3/16f,4/16f,3/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,4/16f,5/16f,4/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,5/16f,6/16f,5/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,6/16f,7/16f,6/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,7/16f,8/16f,7/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,8/16f,9/16f,8/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,9/16f,10/16f,9/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,10/16f,11/16f,10/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,11/16f,12/16f,11/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,12/16f,13/16f,12/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,13/16f,14/16f,13/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,14/16f,15/16f,14/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,15/16f,1f,15/16f,1f, texture.get(index), tintIndex));
                        //East
                        quads.addAll(ModelHelper.createCuboid(1/16f,1f,1/16f,2/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2/16f,1f,2/16f,3/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(3/16f,1f,3/16f,4/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(4/16f,1f,4/16f,5/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(5/16f,1f,5/16f,6/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(6/16f,1f,6/16f,7/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(7/16f,1f,7/16f,8/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(8/16f,1f,8/16f,9/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(9/16f,1f,9/16f,10/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(10/16f,1f,10/16f,11/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(11/16f,1f,11/16f,12/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(12/16f,1f,12/16f,13/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13/16f,1f,13/16f,14/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14/16f,1f,14/16f,15/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15/16f,1f,15/16f,1f,0f,1f, texture.get(index), tintIndex));
                    }
                    if ((shape == innerLeft && direction == west) || shape == innerRight && direction == south) {
                        //West
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,1/16f,2/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,14/16f,2/16f,3/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,13/16f,3/16f,4/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,12/16f,4/16f,5/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,11/16f,5/16f,6/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,10/16f,6/16f,7/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,9/16f,7/16f,8/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,8/16f,8/16f,9/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,7/16f,9/16f,10/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,6/16f,10/16f,11/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,5/16f,11/16f,12/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,4/16f,12/16f,13/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,3/16f,13/16f,14/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,2/16f,14/16f,15/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1/16f,15/16f,1f,0f,1f, texture.get(index), tintIndex));
                        //South
                        quads.addAll(ModelHelper.createCuboid(0f,1f,1/16f,2/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,2/16f,3/16f,2/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,3/16f,4/16f,3/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,4/16f,5/16f,4/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,5/16f,6/16f,5/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,6/16f,7/16f,6/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,7/16f,8/16f,7/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,8/16f,9/16f,8/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,9/16f,10/16f,9/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,10/16f,11/16f,10/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,11/16f,12/16f,11/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,12/16f,13/16f,12/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,13/16f,14/16f,13/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,14/16f,15/16f,14/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,15/16f,1f,15/16f,1f, texture.get(index), tintIndex));
                    }
                }
            }



            if (half==upper) {
                quads.addAll(ModelHelper.createCuboid(0f,1f,15/16f,1f, 0f, 1f, texture.get(index),tintIndex));
                if(shape == straight) {
                    if (direction == north) {
                        quads.addAll(ModelHelper.createCuboid(0f,1f,14/16f,15/16f,0f,15/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,13/16f,14/16f,0f,14/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,12/16f,13/16f,0f,13/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,11/16f,12/16f,0f,12/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,10/16f,11/16f,0f,11/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,9/16f,10/16f,0f,10/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,8/16f,9/16f,0f,9/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,7/16f,8/16f,0f,8/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,6/16f,7/16f,0f,7/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,5/16f,6/16f,0f,6/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,4/16f,5/16f,0f,5/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,3/16f,4/16f,0f,4/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,2/16f,3/16f,0f,3/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,1/16f,2/16f,0f,2/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,0f,1/16f,0f,1/16f, texture.get(index), tintIndex));
                    }
                    if (direction == east) {
                        quads.addAll(ModelHelper.createCuboid(1/16f,1f,14/16f,15/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2/16f,1f,13/16f,14/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(3/16f,1f,12/16f,13/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(4/16f,1f,11/16f,12/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(5/16f,1f,10/16f,11/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(6/16f,1f,9/16f,10/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(7/16f,1f,8/16f,9/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(8/16f,1f,7/16f,8/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(9/16f,1f,6/16f,7/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(10/16f,1f,5/16f,6/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(11/16f,1f,4/16f,5/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(12/16f,1f,3/16f,4/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13/16f,1f,2/16f,3/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14/16f,1f,1/16f,2/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15/16f,1f,0f,1/16f,0f,1f, texture.get(index), tintIndex));
                    }
                    if (direction == south) {
                        quads.addAll(ModelHelper.createCuboid(0f,1f,14/16f,15/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,13/16f,14/16f,2/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,12/16f,13/16f,3/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,11/16f,12/16f,4/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,10/16f,11/16f,5/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,9/16f,10/16f,6/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,8/16f,9/16f,7/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,7/16f,8/16f,8/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,6/16f,7/16f,9/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,5/16f,6/16f,10/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,4/16f,5/16f,11/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,3/16f,4/16f,12/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,2/16f,3/16f,13/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,1/16f,2/16f,14/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,0f,1/16f,15/16f,1f, texture.get(index), tintIndex));
                    }
                    if (direction == west) {
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,14/16f,15/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,14/16f,13/16f,14/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,13/16f,12/16f,13/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,12/16f,11/16f,12/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,11/16f,10/16f,11/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,10/16f,9/16f,10/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,9/16f,8/16f,9/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,8/16f,7/16f,8/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,7/16f,6/16f,7/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,6/16f,5/16f,6/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,5/16f,4/16f,5/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,4/16f,3/16f,4/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,3/16f,2/16f,3/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,2/16f,1/16f,2/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1/16f,0f,1/16f,0f,1f, texture.get(index), tintIndex));
                    }
                }
                if (shape == outerLeft || shape == outerRight) {
                    if ((direction == north && shape == outerLeft) || (direction == west && shape == outerRight)) {
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,14/16f,15/16f,0f,15/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,14/16f,13/16f,14/16f,0f,14/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,13/16f,12/16f,13/16f,0f,13/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,12/16f,11/16f,12/16f,0f,12/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,11/16f,10/16f,11/16f,0f,11/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,10/16f,9/16f,10/16f,0f,10/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,9/16f,8/16f,9/16f,0f,9/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,8/16f,7/16f,8/16f,0f,8/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,7/16f,6/16f,7/16f,0f,7/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,6/16f,5/16f,6/16f,0f,6/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,5/16f,4/16f,5/16f,0f,5/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,4/16f,3/16f,4/16f,0f,4/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,3/16f,2/16f,3/16f,0f,3/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,2/16f,1/16f,2/16f,0f,2/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1/16f,0f,1/16f,0f,1/16f, texture.get(index), tintIndex));
                    }
                    if ((direction == east && shape == outerLeft) || (direction == north && shape == outerRight)) {
                        quads.addAll(ModelHelper.createCuboid(1/16f,1f,14/16f,15/16f,0f,15/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2/16f,1f,13/16f,14/16f,0f,14/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(3/16f,1f,12/16f,13/16f,0f,13/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(4/16f,1f,11/16f,12/16f,0f,12/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(5/16f,1f,10/16f,11/16f,0f,11/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(6/16f,1f,9/16f,10/16f,0f,10/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(7/16f,1f,8/16f,9/16f,0f,9/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(8/16f,1f,7/16f,8/16f,0f,8/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(9/16f,1f,6/16f,7/16f,0f,7/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(10/16f,1f,5/16f,6/16f,0f,6/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(11/16f,1f,4/16f,5/16f,0f,5/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(12/16f,1f,3/16f,4/16f,0f,4/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13/16f,1f,2/16f,3/16f,0f,3/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14/16f,1f,1/16f,2/16f,0f,2/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15/16f,1f,0f,1/16f,0f,1/16f, texture.get(index), tintIndex));
                    }
                    if ((direction == south && shape == outerLeft) || (direction == east && shape == outerRight)) {
                        quads.addAll(ModelHelper.createCuboid(1/16f,1f,14/16f,15/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2/16f,1f,13/16f,14/16f,2/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(3/16f,1f,12/16f,13/16f,3/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(4/16f,1f,11/16f,12/16f,4/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(5/16f,1f,10/16f,11/16f,5/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(6/16f,1f,9/16f,10/16f,6/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(7/16f,1f,8/16f,9/16f,7/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(8/16f,1f,7/16f,8/16f,8/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(9/16f,1f,6/16f,7/16f,9/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(10/16f,1f,5/16f,6/16f,10/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(11/16f,1f,4/16f,5/16f,11/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(12/16f,1f,3/16f,4/16f,12/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13/16f,1f,2/16f,3/16f,13/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14/16f,1f,1/16f,2/16f,14/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15/16f,1f,0f,1/16f,15/16f,1f, texture.get(index), tintIndex));
                    }
                    if ((direction == west && shape == outerLeft) || (direction == south && shape == outerRight)) {
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,14/16f,15/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,14/16f,13/16f,14/16f,2/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,13/16f,12/16f,13/16f,3/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,12/16f,11/16f,12/16f,4/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,11/16f,10/16f,11/16f,5/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,10/16f,9/16f,10/16f,6/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,9/16f,8/16f,9/16f,7/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,8/16f,7/16f,8/16f,8/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,7/16f,6/16f,7/16f,9/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,6/16f,5/16f,6/16f,10/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,5/16f,4/16f,5/16f,11/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,4/16f,3/16f,4/16f,12/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,3/16f,2/16f,3/16f,13/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,2/16f,1/16f,2/16f,14/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1/16f,0f,1/16f,15/16f,1f, texture.get(index), tintIndex));
                    }
                }
                if (shape == innerLeft || shape == innerRight) {
                    if ((shape == innerLeft && direction == north) || (shape == innerRight && direction == west)) {
                        //North
                        quads.addAll(ModelHelper.createCuboid(0f,1f,14/16f,15/16f,0f,15/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,13/16f,14/16f,0f,14/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,12/16f,13/16f,0f,13/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,11/16f,12/16f,0f,12/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,10/16f,11/16f,0f,11/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,9/16f,10/16f,0f,10/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,8/16f,9/16f,0f,9/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,7/16f,8/16f,0f,8/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,6/16f,7/16f,0f,7/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,5/16f,6/16f,0f,6/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,4/16f,5/16f,0f,5/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,3/16f,4/16f,0f,4/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,2/16f,3/16f,0f,3/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,1/16f,2/16f,0f,2/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,0f,1/16f,0f,1/16f, texture.get(index), tintIndex));
                        //West
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,14/16f,15/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,14/16f,13/16f,14/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,13/16f,12/16f,13/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,12/16f,11/16f,12/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,11/16f,10/16f,11/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,10/16f,9/16f,10/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,9/16f,8/16f,9/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,8/16f,7/16f,8/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,7/16f,6/16f,7/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,6/16f,5/16f,6/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,5/16f,4/16f,5/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,4/16f,3/16f,4/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,3/16f,2/16f,3/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,2/16f,1/16f,2/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1/16f,0f,1/16f,1/16f,1f, texture.get(index), tintIndex));
                    }
                    if ((shape == innerLeft && direction == east) || (shape == innerRight && direction == north)) {
                        //East
                        quads.addAll(ModelHelper.createCuboid(1/16f,15/16f,14/16f,15/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2/16f,15/16f,13/16f,14/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(3/16f,15/16f,12/16f,13/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(4/16f,15/16f,11/16f,12/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(5/16f,15/16f,10/16f,11/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(6/16f,15/16f,9/16f,10/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(7/16f,15/16f,8/16f,9/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(8/16f,15/16f,7/16f,8/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(9/16f,15/16f,6/16f,7/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(10/16f,15/16f,5/16f,6/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(11/16f,15/16f,4/16f,5/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(12/16f,15/16f,3/16f,4/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13/16f,15/16f,2/16f,3/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14/16f,15/16f,1/16f,2/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15/16f,15/16f,0f,1/16f,1/16f,1f, texture.get(index), tintIndex));
                        //North
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,14/16f,15/16f,1/16f,15/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,13/16f,14/16f,1/16f,14/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,12/16f,13/16f,1/16f,13/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,11/16f,12/16f,1/16f,12/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,10/16f,11/16f,1/16f,11/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,9/16f,10/16f,1/16f,10/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,8/16f,9/16f,1/16f,9/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,7/16f,8/16f,1/16f,8/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,6/16f,7/16f,1/16f,7/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,5/16f,6/16f,1/16f,6/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,4/16f,5/16f,1/16f,5/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,3/16f,4/16f,1/16f,4/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,2/16f,3/16f,1/16f,3/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,1/16f,2/16f,1/16f,2/16f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,0f,1/16f,1/16f,1/16f, texture.get(index), tintIndex));
                        //Back sides - workaround to fix some weird rendering issues
                        quads.addAll(ModelHelper.createCuboid(0f,1f,0f,1f,0f,0f,texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1f,1f,0f,1f,0f,1f,texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,0f,0f,1f,0f,1/16f,texture.get(index),tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15/16f,1f,0f,1f,1f,1f,texture.get(index),tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15/16f,1f,0f,0f,0f,1f,texture.get(index),tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,0f,0f,0f,1/16f,texture.get(index),tintIndex));
                    }
                    if ((shape == innerLeft && direction == south) || (shape == innerRight && direction == east)) {
                        //South
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,14/16f,15/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,13/16f,14/16f,2/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,12/16f,13/16f,3/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,11/16f,12/16f,4/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,10/16f,11/16f,5/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,9/16f,10/16f,6/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,8/16f,9/16f,7/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,7/16f,8/16f,8/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,6/16f,7/16f,9/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,5/16f,6/16f,10/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,4/16f,5/16f,11/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,3/16f,4/16f,12/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,2/16f,3/16f,13/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,1/16f,2/16f,14/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,0f,1/16f,15/16f,1f, texture.get(index), tintIndex));
                        //East
                        quads.addAll(ModelHelper.createCuboid(1/16f,1f,14/16f,15/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2/16f,1f,13/16f,14/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(3/16f,1f,12/16f,13/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(4/16f,1f,11/16f,12/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(5/16f,1f,10/16f,11/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(6/16f,1f,9/16f,10/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(7/16f,1f,8/16f,9/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(8/16f,1f,7/16f,8/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(9/16f,1f,6/16f,7/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(10/16f,1f,5/16f,6/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(11/16f,1f,4/16f,5/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(12/16f,1f,3/16f,4/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13/16f,1f,2/16f,3/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14/16f,1f,1/16f,2/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15/16f,1f,0f,1/16f,0f,1f, texture.get(index), tintIndex));
                    }
                    if ((shape == innerLeft && direction == west) || (shape == innerRight && direction == south)) {
                        //West
                        quads.addAll(ModelHelper.createCuboid(0f,15/16f,14/16f,15/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,14/16f,13/16f,14/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,13/16f,12/16f,13/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,12/16f,11/16f,12/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,11/16f,10/16f,11/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,10/16f,9/16f,10/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,9/16f,8/16f,9/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,8/16f,7/16f,8/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,7/16f,6/16f,7/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,6/16f,5/16f,6/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,5/16f,4/16f,5/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,4/16f,3/16f,4/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,3/16f,2/16f,3/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,2/16f,1/16f,2/16f,0f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1/16f,0f,1/16f,0f,1f, texture.get(index), tintIndex));
                        //South
                        quads.addAll(ModelHelper.createCuboid(0f,1f,14/16f,15/16f,1/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,13/16f,14/16f,2/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,12/16f,13/16f,3/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,11/16f,12/16f,4/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,10/16f,11/16f,5/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,9/16f,10/16f,6/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,8/16f,9/16f,7/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,7/16f,8/16f,8/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,6/16f,7/16f,9/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,5/16f,6/16f,10/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,4/16f,5/16f,11/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,3/16f,4/16f,12/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,2/16f,3/16f,13/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,1/16f,2/16f,14/16f,1f, texture.get(index), tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f,1f,0f,1/16f,15/16f,1f, texture.get(index), tintIndex));
                    }
                }
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
}
