package mod.pianomanu.blockcarpentry.bakedmodels;

import com.google.common.collect.ImmutableList;
import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Contains all information for the block model
 * See {@link mod.pianomanu.blockcarpentry.util.ModelHelper} for more information
 * @author PianoManu
 * @version 1.0 08/29/20
 */
public class StairsBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    private void putVertex(BakedQuadBuilder builder, Vector3d normal,
                           double x, double y, double z, float u, float v, TextureAtlasSprite sprite, float r, float g, float b) {

        ImmutableList<VertexFormatElement> elements = builder.getVertexFormat().getElements().asList();
        for (int j = 0 ; j < elements.size() ; j++) {
            VertexFormatElement e = elements.get(j);
            switch (e.getUsage()) {
                case POSITION:
                    builder.put(j, (float) x, (float) y, (float) z, 1.0f);
                    break;
                case COLOR:
                    builder.put(j, r, g, b, 1.0f);
                    break;
                case UV:
                    switch (e.getIndex()) {
                        case 0:
                            float iu = sprite.getInterpolatedU(u);
                            float iv = sprite.getInterpolatedV(v);
                            builder.put(j, iu, iv);
                            break;
                        case 2:
                            builder.put(j, (short) 0, (short) 0);
                            break;
                        default:
                            builder.put(j);
                            break;
                    }
                    break;
                case NORMAL:
                    builder.put(j, (float) normal.x, (float) normal.y, (float) normal.z);
                    break;
                default:
                    builder.put(j);
                    break;
            }
        }
        builder.setApplyDiffuseLighting(true);
    }

    private BakedQuad createSmallSquareQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, boolean isUOffset, boolean isVOffset) {
        Vector3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        float ul=0;
        float uh=8;
        float vl=0;
        float vh=8;
        if(isVOffset) {
            vl=8;
            vh=16;
        }
        if(isUOffset) {
            ul=8;
            uh=16;
        }
        putVertex(builder, normal, v1.x, v1.y, v1.z, ul, vl, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, ul, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, uh, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, uh, vl, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    //this one is for the slab-like half of the stair, which is 1x0.5x1
    private BakedQuad createSideQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, Half half) {
        Vector3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        float vl=0;
        float vh=8;
        if(half==Half.BOTTOM) {
            vl=8;
            vh=16;
        }
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, vl, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 16, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 16, vl, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    //East/West
    private BakedQuad createBigQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite) {
        Vector3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 16, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 16, 16, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 16, 0, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    private static Vector3d v(double x, double y, double z) {
        return new Vector3d(x, y, z);
    }


    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        //get block saved in frame tile
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && !(mimic.getBlock() instanceof FrameBlock)) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            if (location != null) {
                IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                model.getBakedModel().getQuads(mimic, side, rand, extraData);
                if (model != null) {
                    //only if model (from block saved in tile entity) exists:
                    return getMimicQuads(state, side, rand, extraData);
                }
            }
        }
        return Collections.emptyList();
    }

    /**
     * create quads for stairs model using the texture from the block of the provided state - completely too long and could be compressed, but it works, and maybe I'll rewrite it for later versions, but for now it's okay
     * @param state state of frame stair
     * @param side unused
     * @param rand unused
     * @param extraData contains data from tile entity (in this case only for the contained block)
     * @return baked quads, that will be used to display the model
     */
    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        if (side != null) {
            return Collections.emptyList();
        }

        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        int tex = extraData.getData(FrameBlockTile.TEXTURE);
        if (mimic!=null && state != null) {
            //get texture from block in tile entity and apply it to the quads
            List<TextureAtlasSprite> textureList = TextureHelper.getTextureListFromBlock(mimic.getBlock());
            TextureAtlasSprite texture;
            if(textureList.size()>tex) {
                texture = textureList.get(tex);
            }
            else {
                texture = textureList.get(0);
            }
            List<BakedQuad> quads = new ArrayList<>();
            Half half = state.get(StairsBlock.HALF);
            if (state.get(StairsBlock.HALF)==Half.BOTTOM) {
                //bottom half bottom side
                quads.add(createBigQuad(v(0,0,0), v(1,0,0), v(1,0,1), v(0,0,1), texture));
                //bottom half top side
                quads.add(createBigQuad(v(0,0.5,0), v(0,0.5,1), v(1,0.5,1), v(1,0.5,0), texture));
                //bottom half east/south/west/north
                quads.add(createSideQuad(v(0,0.5,0),v(0,0,0), v(0,0,1), v(0,0.5,1), texture, half));
                quads.add(createSideQuad(v(1,0.5,0),v(1,0,0), v(0,0,0), v(0,0.5,0), texture, half));
                quads.add(createSideQuad(v(1,0.5,1),v(1,0,1), v(1,0,0), v(1,0.5,0), texture, half));
                quads.add(createSideQuad(v(0,0.5,1),v(0,0,1), v(1,0,1), v(1,0.5,1), texture, half));
                if(state.get(StairsBlock.SHAPE)== StairsShape.STRAIGHT) {
                    if(state.get(StairsBlock.FACING)==Direction.WEST || state.get(StairsBlock.FACING)==Direction.NORTH) {
                        //NW cube
                        //TODO change 2nd and 4th false and true - fixed?? -> testing!
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0), v(0,y2(half),0.5), v(0.5,y2(half),0.5), v(0.5,y2(half),0), texture, false, false));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.EAST || state.get(StairsBlock.FACING)==Direction.NORTH) {
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0), v(0.5,y2(half),0.5), v(1,y2(half),0.5), v(1,y2(half),0), texture, true, false));

                    }
                    if(state.get(StairsBlock.FACING)==Direction.WEST || state.get(StairsBlock.FACING)==Direction.SOUTH) {
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5), v(0,y2(half),1), v(0.5,y2(half),1), v(0.5,y2(half),0.5), texture, false, true));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.EAST || state.get(StairsBlock.FACING)==Direction.SOUTH) {
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5), v(0.5,y2(half),1), v(1,y2(half),1), v(1,y2(half),0.5), texture, true, true));

                    }
                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.INNER_LEFT) {
                    //L-Shape
                    if(state.get(StairsBlock.FACING)==Direction.WEST) {
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0), v(0,y2(half),0.5), v(0.5,y2(half),0.5), v(0.5,y2(half),0), texture, false, false));
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5), v(0.5,y2(half),1), v(1,y2(half),1), v(1,y2(half),0.5), texture, true, true));
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5), v(0,y2(half),1), v(0.5,y2(half),1), v(0.5,y2(half),0.5), texture, false, true));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.EAST) {
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0), v(0.5,y2(half),0.5), v(1,y2(half),0.5), v(1,y2(half),0), texture, true, false));
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0), v(0,y2(half),0.5), v(0.5,y2(half),0.5), v(0.5,y2(half),0), texture, false, false));
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5), v(0.5,y2(half),1), v(1,y2(half),1), v(1,y2(half),0.5), texture, true, true));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.NORTH) {
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5), v(0,y2(half),1), v(0.5,y2(half),1), v(0.5,y2(half),0.5), texture, false, true));
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0), v(0.5,y2(half),0.5), v(1,y2(half),0.5), v(1,y2(half),0), texture, true, false));
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0), v(0,y2(half),0.5), v(0.5,y2(half),0.5), v(0.5,y2(half),0), texture, false, false));

                    }
                    if(state.get(StairsBlock.FACING)==Direction.SOUTH) {
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5), v(0.5,y2(half),1), v(1,y2(half),1), v(1,y2(half),0.5), texture, true, true));
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5), v(0,y2(half),1), v(0.5,y2(half),1), v(0.5,y2(half),0.5), texture, false, true));
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0), v(0.5,y2(half),0.5), v(1,y2(half),0.5), v(1,y2(half),0), texture, true, false));

                    }

                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.INNER_RIGHT) {
                    if(state.get(StairsBlock.FACING)==Direction.WEST) {
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0), v(0,y2(half),0.5), v(0.5,y2(half),0.5), v(0.5,y2(half),0), texture, false, false));
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0), v(0.5,y2(half),0.5), v(1,y2(half),0.5), v(1,y2(half),0), texture, true, false));
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5), v(0,y2(half),1), v(0.5,y2(half),1), v(0.5,y2(half),0.5), texture, false, true));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.EAST) {
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0), v(0.5,y2(half),0.5), v(1,y2(half),0.5), v(1,y2(half),0), texture, true, false));
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5), v(0.5,y2(half),1), v(1,y2(half),1), v(1,y2(half),0.5), texture, true, true));
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5), v(0,y2(half),1), v(0.5,y2(half),1), v(0.5,y2(half),0.5), texture, false, true));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.NORTH) {
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0), v(0.5,y2(half),0.5), v(1,y2(half),0.5), v(1,y2(half),0), texture, true, false));
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0), v(0,y2(half),0.5), v(0.5,y2(half),0.5), v(0.5,y2(half),0), texture, false, false));
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5), v(0.5,y2(half),1), v(1,y2(half),1), v(1,y2(half),0.5), texture, true, true));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.SOUTH) {
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5), v(0.5,y2(half),1), v(1,y2(half),1), v(1,y2(half),0.5), texture, true, true));
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5), v(0,y2(half),1), v(0.5,y2(half),1), v(0.5,y2(half),0.5), texture, false, true));
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0), v(0,y2(half),0.5), v(0.5,y2(half),0.5), v(0.5,y2(half),0), texture, false, false));
                    }
                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.OUTER_LEFT) {
                    if(state.get(StairsBlock.FACING)==Direction.NORTH) {
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0), v(0,y2(half),0.5), v(0.5,y2(half),0.5), v(0.5,y2(half),0), texture, false, false));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.SOUTH) {
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5), v(0.5,y2(half),1), v(1,y2(half),1), v(1,y2(half),0.5), texture, true, true));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.EAST) {
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0), v(0.5,y2(half),0.5), v(1,y2(half),0.5), v(1,y2(half),0), texture, true, false));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.WEST) {
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5), v(0,y2(half),1), v(0.5,y2(half),1), v(0.5,y2(half),0.5), texture, false, true));
                    }
                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.OUTER_RIGHT) {
                    if(state.get(StairsBlock.FACING)==Direction.WEST) {
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0), v(0,y2(half),0.5), v(0.5,y2(half),0.5), v(0.5,y2(half),0), texture, false, false));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.EAST) {
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5), v(0.5,y2(half),1), v(1,y2(half),1), v(1,y2(half),0.5), texture, true, true));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.NORTH) {
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, false));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0), v(0.5,y2(half),0.5), v(1,y2(half),0.5), v(1,y2(half),0), texture, true, false));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.SOUTH) {
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, false));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, false));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5), v(0,y2(half),1), v(0.5,y2(half),1), v(0.5,y2(half),0.5), texture, false, true));
                    }
                }
            } else  {
                //top half bottom side
                quads.add(createBigQuad(v(0,0.5,0), v(1,0.5,0), v(1,0.5,1), v(0,0.5,1), texture));
                //top half top side
                quads.add(createBigQuad(v(0,1,0), v(0,1,1), v(1,1,1), v(1,1,0), texture));
                //top half east/south/west/north
                quads.add(createSideQuad(v(0,1,0),v(0,0.5,0), v(0,0.5,1), v(0,1,1), texture, half));
                quads.add(createSideQuad(v(1,1,0),v(1,0.5,0), v(0,0.5,0), v(0,1,0), texture, half));
                quads.add(createSideQuad(v(1,1,1),v(1,0.5,1), v(1,0.5,0), v(1,1,0), texture, half));
                quads.add(createSideQuad(v(0,1,1),v(0,0.5,1), v(1,0.5,1), v(1,1,1), texture, half));
                if(state.get(StairsBlock.SHAPE)== StairsShape.STRAIGHT) {
                    if(state.get(StairsBlock.FACING)==Direction.WEST || state.get(StairsBlock.FACING)==Direction.NORTH) {
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0), v(0.5,y(half),0), v(0.5,y(half),0.5), v(0,y(half),0.5),texture, false, true));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.EAST || state.get(StairsBlock.FACING)==Direction.NORTH) {
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0), v(1,y(half),0), v(1,y(half),0.5), v(0.5,y(half),0.5),texture, true, true));

                    }
                    if(state.get(StairsBlock.FACING)==Direction.WEST || state.get(StairsBlock.FACING)==Direction.SOUTH) {
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0.5), v(0.5,y(half),0.5), v(0.5,y(half),1), v(0,y(half),1),texture, false, false));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.EAST || state.get(StairsBlock.FACING)==Direction.SOUTH) {
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0.5), v(1,y(half),0.5), v(1,y(half),1), v(0.5,y(half),1),texture, true, false));

                    }
                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.INNER_LEFT) {
                    //L-Shape
                    if(state.get(StairsBlock.FACING)==Direction.WEST) {
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0), v(0.5,y(half),0), v(0.5,y(half),0.5), v(0,y(half),0.5),texture, false, true));
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0.5), v(1,y(half),0.5), v(1,y(half),1), v(0.5,y(half),1),texture, true, false));
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0.5), v(0.5,y(half),0.5), v(0.5,y(half),1), v(0,y(half),1),texture, false, false));

                    }
                    if(state.get(StairsBlock.FACING)==Direction.EAST) {
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0), v(1,y(half),0), v(1,y(half),0.5), v(0.5,y(half),0.5),texture, true, true));
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0), v(0.5,y(half),0), v(0.5,y(half),0.5), v(0,y(half),0.5),texture, false, true));
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0.5), v(1,y(half),0.5), v(1,y(half),1), v(0.5,y(half),1),texture, true, false));

                    }
                    if(state.get(StairsBlock.FACING)==Direction.NORTH) {
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0.5), v(0.5,y(half),0.5), v(0.5,y(half),1), v(0,y(half),1),texture, false, false));
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0), v(1,y(half),0), v(1,y(half),0.5), v(0.5,y(half),0.5),texture, true, true));
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0), v(0.5,y(half),0), v(0.5,y(half),0.5), v(0,y(half),0.5),texture, false, true));


                    }
                    if(state.get(StairsBlock.FACING)==Direction.SOUTH) {
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0.5), v(1,y(half),0.5), v(1,y(half),1), v(0.5,y(half),1),texture, true, false));
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0.5), v(0.5,y(half),0.5), v(0.5,y(half),1), v(0,y(half),1),texture, false, false));
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0), v(1,y(half),0), v(1,y(half),0.5), v(0.5,y(half),0.5),texture, true, true));


                    }

                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.INNER_RIGHT) {
                    if(state.get(StairsBlock.FACING)==Direction.WEST) {
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0), v(0.5,y(half),0), v(0.5,y(half),0.5), v(0,y(half),0.5),texture, false, true));
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0), v(1,y(half),0), v(1,y(half),0.5), v(0.5,y(half),0.5),texture, true, true));
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0.5), v(0.5,y(half),0.5), v(0.5,y(half),1), v(0,y(half),1),texture, false, false));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.EAST) {
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0), v(1,y(half),0), v(1,y(half),0.5), v(0.5,y(half),0.5),texture, true, true));
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0.5), v(1,y(half),0.5), v(1,y(half),1), v(0.5,y(half),1),texture, true, false));
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0.5), v(0.5,y(half),0.5), v(0.5,y(half),1), v(0,y(half),1),texture, false, false));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.NORTH) {
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0), v(1,y(half),0), v(1,y(half),0.5), v(0.5,y(half),0.5),texture, true, true));
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0), v(0.5,y(half),0), v(0.5,y(half),0.5), v(0,y(half),0.5),texture, false, true));
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0.5), v(1,y(half),0.5), v(1,y(half),1), v(0.5,y(half),1),texture, true, false));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.SOUTH) {
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0.5), v(1,y(half),0.5), v(1,y(half),1), v(0.5,y(half),1),texture, true, false));
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0.5), v(0.5,y(half),0.5), v(0.5,y(half),1), v(0,y(half),1),texture, false, false));
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0), v(0.5,y(half),0), v(0.5,y(half),0.5), v(0,y(half),0.5),texture, false, true));
                    }
                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.OUTER_LEFT) {
                    if(state.get(StairsBlock.FACING)==Direction.NORTH) {
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0), v(0.5,y(half),0), v(0.5,y(half),0.5), v(0,y(half),0.5),texture, false, true));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.SOUTH) {
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0.5), v(1,y(half),0.5), v(1,y(half),1), v(0.5,y(half),1),texture, true, false));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.EAST) {
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0), v(1,y(half),0), v(1,y(half),0.5), v(0.5,y(half),0.5),texture, true, true));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.WEST) {
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0.5), v(0.5,y(half),0.5), v(0.5,y(half),1), v(0,y(half),1),texture, false, false));
                    }
                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.OUTER_RIGHT) {
                    if(state.get(StairsBlock.FACING)==Direction.WEST) {
                        //NW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0),v(0,y(half),0),v(0,y(half),0.5),v(0,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0,y(half),0),v(0,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),0),v(0.5,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0), v(0.5,y(half),0), v(0.5,y(half),0.5), v(0,y(half),0.5),texture, false, true));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.EAST) {
                        //SE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0.5,y(half),1),v(0.5,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),1),v(1,y(half),1),v(1,y(half),0.5),v(1,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(1,y(half),1),v(1,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0.5), v(1,y(half),0.5), v(1,y(half),1), v(0.5,y(half),1),texture, true, false));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.NORTH) {
                        //NE cube
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0),v(0.5,y(half),0),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0),v(1,y(half),0),v(0.5,y(half),0),v(0.5,y2(half),0),texture, false, true));
                        quads.add(createSmallSquareQuad(v(1,y2(half),0.5),v(1,y(half),0.5),v(1,y(half),0),v(1,y2(half),0),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(1,y(half),0.5),v(1,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y(half),0), v(1,y(half),0), v(1,y(half),0.5), v(0.5,y(half),0.5),texture, true, true));
                    }
                    if(state.get(StairsBlock.FACING)==Direction.SOUTH) {
                        //SW cube
                        quads.add(createSmallSquareQuad(v(0,y2(half),0.5),v(0,y(half),0.5),v(0,y(half),1),v(0,y2(half),1),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),0.5),v(0.5,y(half),0.5),v(0,y(half),0.5),v(0,y2(half),0.5),texture, true, true));
                        quads.add(createSmallSquareQuad(v(0.5,y2(half),1),v(0.5,y(half),1),v(0.5,y(half),0.5),v(0.5,y2(half),0.5),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y2(half),1),v(0,y(half),1),v(0.5,y(half),1),v(0.5,y2(half),1),texture, false, true));
                        quads.add(createSmallSquareQuad(v(0,y(half),0.5), v(0.5,y(half),0.5), v(0.5,y(half),1), v(0,y(half),1),texture, false, false));
                    }
                }
            }

            return quads;
        }
        return Collections.emptyList();
    }

    //getYPositionForQuad (bottom side)
    private float y(Half half) {
        float y=0;
        if(half==Half.BOTTOM) y=0.5f;
        return y;
    }

    //getYPositionForQuad (top side)
    private float y2(Half half) {
        float y=0.5f;
        if(half==Half.BOTTOM) y=1;
        return y;
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
