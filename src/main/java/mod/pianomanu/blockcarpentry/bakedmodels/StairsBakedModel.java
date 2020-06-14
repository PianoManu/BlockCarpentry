package mod.pianomanu.blockcarpentry.bakedmodels;

import com.google.common.collect.ImmutableList;
import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.block.ButtonFrameBlock;
import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StairsBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/frame_slab");
    public static final VertexFormat SLAB_BLOCK = new VertexFormat(ImmutableList.<VertexFormatElement>builder()
            .add(DefaultVertexFormats.POSITION_3F)
            .add(DefaultVertexFormats.COLOR_4UB)
            .add(DefaultVertexFormats.TEX_2F)
            .add(DefaultVertexFormats.TEX_2SB)
            .add(DefaultVertexFormats.NORMAL_3B)
            .add(DefaultVertexFormats.PADDING_1B)
            .build());

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    //@Override
    public boolean func_230044_c_() {
        return false;
    }

    private void putVertex(BakedQuadBuilder builder, Vec3d normal,
                           double x, double y, double z, float u, float v, TextureAtlasSprite sprite, float r, float g, float b) {

        //ImmutableList<VertexFormatElement> elements = builder.getVertexFormat().getElements().asList();
        ImmutableList<VertexFormatElement> elements = SLAB_BLOCK.getElements().asList();
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

    /*{   "from": [ 5, 0, 6 ],
        "to": [ 11, 2, 10 ],
        "faces": {
        "down":  { "uv": [ 5,  6, 11, 10 ], "texture": "#texture", "cullface": "down" },
        "up":    { "uv": [ 5, 10, 11,  6 ], "texture": "#texture" },
        "north": { "uv": [ 5, 14, 11, 16 ], "texture": "#texture" },
        "south": { "uv": [ 5, 14, 11, 16 ], "texture": "#texture" },
        "west":  { "uv": [ 6, 14, 10, 16 ], "texture": "#texture" },
        "east":  { "uv": [ 6, 14, 10, 16 ], "texture": "#texture" }
    */
    private BakedQuad createSmallSquareQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 8, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 8, 8, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 8, 0, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    //this one is for the "upper" half of the stair which is 0.5x0.5x1
    private BakedQuad createSideQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite, Direction direction) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 8, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 8, 8, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 8, 0, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    //this one is for the slab-like half of the stair, which is 1x0.5x1
    private BakedQuad createSideQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        //builder.setQuadOrientation(facing);
        builder.setApplyDiffuseLighting(true);
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 8, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 16, 8, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 16, 0, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    //East/West
    private BakedQuad createBigQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        //builder.setQuadOrientation(facing);
        builder.setApplyDiffuseLighting(true);
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 16, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 16, 16, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 16, 0, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    private static Vec3d v(double x, double y, double z) {
        return new Vec3d(x, y, z);
    }


    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        //get block saved in frame tile
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        //DEBUG
        /*if(state!=null) {
            System.out.println(state.toString());
            state.get(ButtonFrameBlock.HORIZONTAL_FACING);
            System.out.println(state.get(ButtonFrameBlock.HORIZONTAL_FACING).toString());
        } else System.out.println("state is null");*/
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

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        if (side != null) {
            return Collections.emptyList();
        }

        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic!=null && state != null) {
            //get texture from block in tile entity and apply it to the quads
            TextureAtlasSprite texture = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(mimic.getBlock().getRegistryName().getNamespace(), "block/" + mimic.getBlock().getRegistryName().getPath()));
            List<BakedQuad> quads = new ArrayList<>();
            Direction direction = state.get(StairsBlock.FACING);
            Half half = state.get(StairsBlock.HALF);
            if (state.get(StairsBlock.HALF)==Half.BOTTOM) {
                //down
                /*quads.add(createUpDownQuad(v(x(direction, face), y(direction, face), z(direction, face)), v(x(direction, face) + xr(direction, face), y(direction, face), z(direction, face)), v(x(direction, face) + xr(direction, face), y(direction, face), z(direction, face) + zr(direction, face)), v(x(direction, face), y(direction, face), z(direction, face) + zr(direction, face)), texture, direction, face));
                //up
                quads.add(createUpDownQuad(v(x(direction, face), y2(direction, face), z(direction, face) + zr(direction, face)), v(x(direction, face) + xr(direction, face), y2(direction, face), z(direction, face) + zr(direction, face)), v(x(direction, face) + xr(direction, face), y2(direction, face), z(direction, face)), v(x(direction, face), y2(direction, face), z(direction, face)), texture, direction, face));
                //sides
                quads.add(createLongSideQuad(v(x(direction, face), y(direction, face), z(direction, face) + zr(direction, face)), v(x(direction, face), y2(direction, face), z(direction, face) + zr(direction, face)), v(x(direction, face), y2(direction, face), z(direction, face)), v(x(direction, face), y(direction, face), z(direction, face)), texture, direction, face));
                quads.add(createLongSideQuad(v(x(direction, face) + xr(direction, face), y(direction, face), z(direction, face)), v(x(direction, face) + xr(direction, face), y2(direction, face), z(direction, face)), v(x(direction, face) + xr(direction, face), y2(direction, face), z(direction, face) + zr(direction, face)), v(x(direction, face) + xr(direction, face), y(direction, face), z(direction, face) + zr(direction, face)), texture, direction, face));
                quads.add(createShortSideQuad(v(x(direction, face), y(direction, face), z(direction, face)), v(x(direction, face), y2(direction, face), z(direction, face)), v(x(direction, face) + xr(direction, face), y2(direction, face), z(direction, face)), v(x(direction, face) + xr(direction, face), y(direction, face), z(direction, face)), texture, direction, face));
                quads.add(createShortSideQuad(v(x(direction, face) + xr(direction, face), y(direction, face), z(direction, face) + zr(direction, face)), v(x(direction, face) + xr(direction, face), y2(direction, face), z(direction, face) + zr(direction, face)), v(x(direction, face), y2(direction, face), z(direction, face) + zr(direction, face)), v(x(direction, face), y(direction, face), z(direction, face) + zr(direction, face)), texture, direction, face));*/
                //bottom half bottom side
                quads.add(createBigQuad(v(0,0,0), v(1,0,0), v(1,0,1), v(0,0,1), texture));
                //bottom half top side
                quads.add(createBigQuad(v(0,0.5,0), v(0,0.5,1), v(1,0.5,1), v(1,0.5,0), texture));
                //bottom half east/south/west/north
                quads.add(createSideQuad(v(0,0.5,0),v(0,0,0), v(0,0,1), v(0,0.5,1), texture));
                quads.add(createSideQuad(v(1,0.5,0),v(1,0,0), v(0,0,0), v(0,0.5,0), texture));
                quads.add(createSideQuad(v(1,0.5,1),v(1,0,1), v(1,0,0), v(1,0.5,0), texture));
                quads.add(createSideQuad(v(0,0.5,1),v(0,0,1), v(1,0,1), v(1,0.5,1), texture));
                if(state.get(StairsBlock.SHAPE)== StairsShape.STRAIGHT) {
                    System.out.println("straight "+direction+ ", half "+half);
                    quads.add(createSmallSquareQuad(v(x(direction),y2(half),z(direction)),v(x(direction),y(half),z(direction)),v(x(direction),y(half),z(direction)+0.5),v(x(direction),y2(half),z(direction)+0.5),texture));
                    quads.add(createSmallSquareQuad(v(x(direction)+0.5,y2(half),z(direction)),v(x(direction)+0.5,y(half),z(direction)),v(x(direction),y(half),z(direction)),v(x(direction),y2(half),z(direction)),texture));
                    quads.add(createSmallSquareQuad(v(x(direction)+0.5,y2(half),z(direction)+0.5),v(x(direction)+0.5,y(half),z(direction)+0.5),v(x(direction)+0.5,y(half),z(direction)),v(x(direction)+0.5,y2(half),z(direction)),texture));
                    quads.add(createSmallSquareQuad(v(x(direction),y2(half),z(direction)+0.5),v(x(direction),y(half),z(direction)+0.5),v(x(direction)+0.5,y(half),z(direction)+0.5),v(x(direction)+0.5,y2(half),z(direction)+0.5),texture));
                    quads.add(createSmallSquareQuad(v(x(direction),y2(half),z(direction)), v(x(direction),y2(half),z(direction)+0.5), v(x(direction)+0.5,y2(half),z(direction)+0.5), v(x(direction)+0.5,y2(half),z(direction)), texture));

                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.INNER_LEFT) {

                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.INNER_RIGHT) {

                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.OUTER_LEFT) {

                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.OUTER_RIGHT) {

                }
            } else  {
                //top half bottom side
                quads.add(createBigQuad(v(0,0.5,0), v(1,0.5,0), v(1,0.5,1), v(0,0.5,1), texture));
                //top half top side
                quads.add(createBigQuad(v(0,1,0), v(0,1,1), v(1,1,1), v(1,1,0), texture));
                //top half east/south/west/north
                quads.add(createSideQuad(v(0,1,0),v(0,0.5,0), v(0,0.5,1), v(0,1,1), texture));
                quads.add(createSideQuad(v(1,1,0),v(1,0.5,0), v(0,0.5,0), v(0,1,0), texture));
                quads.add(createSideQuad(v(1,1,1),v(1,0.5,1), v(1,0.5,0), v(1,1,0), texture));
                quads.add(createSideQuad(v(0,1,1),v(0,0.5,1), v(1,0.5,1), v(1,1,1), texture));
                if(state.get(StairsBlock.SHAPE)== StairsShape.STRAIGHT) {
                    System.out.println("straight "+direction+ ", half "+half);
                    quads.add(createSmallSquareQuad(v(x(direction),0,z(direction)),v(x(direction),0.5,z(direction)),v(x(direction),0.5,z(direction)+zo(direction)),v(x(direction),0,z(direction)+zo(direction)),texture));
                    quads.add(createSmallSquareQuad(v(x(direction)+xo(direction),0,z(direction)),v(x(direction)+xo(direction),0.5,z(direction)),v(x(direction),0.5,z(direction)),v(x(direction),0,z(direction)),texture));
                    quads.add(createSmallSquareQuad(v(x(direction)+xo(direction),0,z(direction)+zo(direction)),v(x(direction)+xo(direction),0.5,z(direction)+zo(direction)),v(x(direction)+xo(direction),0.5,z(direction)),v(x(direction)+xo(direction),0,z(direction)),texture));
                    quads.add(createSmallSquareQuad(v(x(direction),0,z(direction)+zo(direction)),v(x(direction),0.5,z(direction)+zo(direction)),v(x(direction)+xo(direction),0.5,z(direction)+zo(direction)),v(x(direction)+xo(direction),0,z(direction)+zo(direction)),texture));

                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.INNER_LEFT) {

                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.INNER_RIGHT) {

                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.OUTER_LEFT) {

                }
                if (state.get(StairsBlock.SHAPE)== StairsShape.OUTER_RIGHT) {

                }
            }

            return quads;
        }
        return Collections.emptyList();
    }

    //getXPositionForQuad
    private float x(Direction direction) {
        float x;
        switch (direction) {
            case EAST:
            case WEST:
                x = 0.5f;
                break;
            default:
                x = 0;
        }
        return x;
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

    //getZPositionForQuad
    private float z(Direction direction) {
        float z;
        switch (direction) {
            case WEST:
            case EAST:
                z = 0;
                break;
            default:
                z = 0.5f;
        }
        return z;
    }

    //"x-offset" of quad
    private float xo(Direction direction) {
        float r;
        switch (direction) {
            case WEST:
            case EAST:
                r=1;
                break;
            default:
                r=0.5f;
        }
        return r;
    }

    //"z-offset" of quad
    private float zo(Direction direction) {
        float r;
        switch (direction) {
            case SOUTH:
            case NORTH:
                r=1;
                break;
            default:
                r=0.5f;
        }
        return r;
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
