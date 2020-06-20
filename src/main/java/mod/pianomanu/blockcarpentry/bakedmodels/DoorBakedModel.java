package mod.pianomanu.blockcarpentry.bakedmodels;

import com.google.common.collect.ImmutableList;
import mod.pianomanu.blockcarpentry.block.DoorFrameBlock;
import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
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

public class DoorBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    private void putVertex(BakedQuadBuilder builder, Vec3d normal,
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

    //Top part of door
    private BakedQuad createTopPartQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite, Direction facing, AttachFace face) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        //builder.setQuadOrientation(facing);
        builder.setApplyDiffuseLighting(true);
        float uShort = 5;
        float uLong = 11;
        float vShort = 14;
        float vLong = 16;
        if (face==AttachFace.WALL && (facing==Direction.SOUTH || facing==Direction.NORTH)) {
            uShort = 6;
            uLong = 10;
            vShort = 6;
            vLong = 10;
        }
        if (face==AttachFace.WALL && (facing==Direction.WEST || facing==Direction.EAST)) {
            //uShort = 5;
            //uLong = 11; //already set
            vShort = 6;
            vLong = 10;
        }
        if (face!=AttachFace.WALL && (facing==Direction.NORTH || facing==Direction.SOUTH)) {
            uShort = 6;
            uLong = 10;
            vShort = 7;
            vLong = 9;
        }
        putVertex(builder, normal, v1.x, v1.y, v1.z, uShort, vShort, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, uShort, vLong, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, uLong, vLong, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, uLong, vShort, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    //Bottom part of door
    private BakedQuad createBottomPartQuad(TextureAtlasSprite sprite, int flag) {
        //NWD = north west up etc.
        Vec3d NWD= v(0,0,0);
        Vec3d NWU= v(0,16,0);
        Vec3d NED= v(3,0,0);
        Vec3d NEU= v(3,16,0);
        Vec3d SWD= v(0,0,16);
        Vec3d SWU= v(0,16,16);
        Vec3d SED= v(3,0,16);
        Vec3d SEU= v(3,16,16);
        Vec3d v1, v2, v3, v4;
        if (flag==0) {
            v1=NWD;
            v2=NWU;
            v3=NEU;
            v4=NED;
        }
        else {
            v1=SED;
            v2=SEU;
            v3=SWU;
            v4=SWD;
        }
        Vec3d normal1 = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();
        Vec3d normal2 = v3.subtract(v1).crossProduct(v2.subtract(v1)).normalize();
        Vec3d normal3 = v2.subtract(v3).crossProduct(v1.subtract(v3)).normalize();
        Vec3d normal4 = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();
        System.out.println(v1.toString()+", "+v2.toString()+", "+v3.toString()+", "+v4.toString());
        System.out.println(normal1.toString()+normal2.toString()+normal3.toString()+normal4.toString());

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal1.x, normal1.y, normal1.z));
        //builder.setQuadOrientation(facing);
        builder.setApplyDiffuseLighting(true);
        float uShort = 6;
        float uLong = 10;
        float vShort = 14;
        float vLong = 16;
        /*if (face==AttachFace.WALL) {
            float tmp = uShort;
            uShort = vLong;
            vLong = tmp;
            tmp = vShort;
            vShort = uLong;
            uLong = tmp;
        }*/
        putVertex(builder, normal1, v1.x, v1.y, v1.z, uShort, vShort, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal1, v2.x, v2.y, v2.z, uShort, vLong, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal1, v3.x, v3.y, v3.z, uLong, vLong, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal1, v4.x, v4.y, v4.z, uLong, vShort, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    private static Vec3d v(double x, double y, double z) {
        return new Vec3d(x, y, z);
    }

    private BakedQuad createSquareQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);

        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 16, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 16, 16, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 16, 0, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    private BakedQuad create3x16SideQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite, int flag) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        float ul = 0;
        float uh = 3;
        if(flag == 0 || flag == 2) {
            ul = 13;
            uh =16;
        }


        putVertex(builder, normal, v1.x, v1.y, v1.z, ul, 0, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, ul, 16, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, uh, 16, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, uh, 0, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    private BakedQuad create3x16TopQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite, int flag) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setApplyDiffuseLighting(true);
        float ul = 0;
        float uh = 16;
        float vl = 0;
        float vh = 3;
        if (flag == 1) {
            vl = 13;
            vh = 16;
            ul = 0;
            uh = 16;
        }
        if (flag == 2) {
            vl = 0;
            vh = 16;
            ul = 13;
            uh = 16;
        }
        if (flag == 3) {
            ul = 0;
            uh = 3;
            vl = 0;
            vh = 16;
        }


        putVertex(builder, normal, v1.x, v1.y, v1.z, ul, vl, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, ul, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, uh, vh, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, uh, vl, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
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
            //TextureAtlasSprite texture = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(mimic.getBlock().getRegistryName().getNamespace(), "block/" + mimic.getBlock().getRegistryName().getPath()));
            List<TextureAtlasSprite> textureList = TextureHelper.getTextureListFromBlock(mimic.getBlock());
            TextureAtlasSprite texture;
            if(textureList.size()>state.get(FrameBlock.TEXTURE)) {
                texture = textureList.get(state.get(FrameBlock.TEXTURE));
            }
            else {
                texture = textureList.get(0);
            }
            List<BakedQuad> quads = new ArrayList<>();
            Direction dir = state.get(DoorFrameBlock.FACING);
            boolean open = state.get(DoorFrameBlock.OPEN);
            DoorHingeSide hinge = state.get(DoorFrameBlock.HINGE);
            Direction west = Direction.WEST;
            Direction east = Direction.EAST;
            Direction north = Direction.NORTH;
            Direction south = Direction.SOUTH;
            DoorHingeSide left = DoorHingeSide.LEFT;
            DoorHingeSide right = DoorHingeSide.RIGHT;
            DoubleBlockHalf half = state.get(DoorBlock.HALF);
            DoubleBlockHalf upper = DoubleBlockHalf.UPPER;
            DoubleBlockHalf lower = DoubleBlockHalf.LOWER;
            //BOTTOM, BOTTOM HINGE, TOP, TOP HINGE
            //PAIRS OF MODELS:
            //  WEST/OPEN/RIGHT SOUTH/CLOSED/RIGHT SOUTH/CLOSED/LEFT EAST/OPEN/LEFT ==> flag = 0
            //  NORTH/CLOSED/RIGHT EAST/OPEN/RIGHT WEST/OPEN/LEFT NORTH/CLOSED/LEFT ==> flag = 1
            //  WEST/CLOSED/RIGHT NORTH/OPEN/RIGHT SOUTH/OPEN/LEFT WEST/CLOSED/LEFT ==> flag = 2
            //  SOUTH/OPEN/RIGHT EAST/CLOSED/RIGHT EAST/CLOSED/LEFT NORTH/OPEN/LEFT ==> flag = 3
            //if(half==lower) {
                int flag = 0;
                if ((dir == north && !open && hinge == right) || (dir == east && open && hinge == right) || (dir == west && open && hinge == left) || (dir == north && !open && hinge == left)) {
                    flag = 1;
                    quads.add(create3x16SideQuad(v(0, 1, 13/16f), v(0, 0, 13/16f), v(0, 0, 1), v(0, 1, 1), texture, flag));
                    quads.add(create3x16SideQuad(v(1, 1, 1), v(1, 0, 1), v(1, 0, 13/16f), v(1, 1, 13/16f), texture, flag));
                    quads.add(createSquareQuad(v(1, 1, 13/16f), v(1, 0, 13/16f), v(0, 0, 13/16f), v(0, 1, 13/16f), texture));
                    quads.add(createSquareQuad(v(0, 1, 1), v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), texture));
                    quads.add(create3x16TopQuad(v(0, 0, 1),v(0,0,13/16f),v(1,0,13/16f),v(1,0,1), texture, flag));
                    quads.add(create3x16TopQuad(v(0, 1, 13/16f),v(0,1,1),v(1,1,1),v(1,1,13/16f), texture, flag));

                }
                else if ((dir == west && !open && hinge == right) || (dir == north && open && hinge == right) || (dir == south && open && hinge == left) || (dir == west && !open && hinge == left)) {
                    flag = 2;
                    quads.add(createSquareQuad(v(13 / 16f, 1, 0), v(13 / 16f, 0, 0), v(13 / 16f, 0, 1), v(13 / 16f, 1, 1), texture));
                    quads.add(createSquareQuad(v(1, 1, 1), v(1, 0, 1), v(1, 0, 0), v(1, 1, 0), texture));
                    quads.add(create3x16SideQuad(v(13 / 16f, 0, 0), v(13/16f, 1, 0), v(1, 1, 0), v(1, 0, 0), texture, flag));
                    quads.add(create3x16SideQuad(v(13/16f, 1, 1), v(13/16f, 0, 1), v(1, 0, 1), v(1, 1, 1), texture, flag));
                    quads.add(create3x16TopQuad(v(13/16f, 0, 1),v(13/16f,0,0),v(1,0,0),v(1,0,1), texture, flag));
                    quads.add(create3x16TopQuad(v(13/16f, 1, 0),v(13/16f,1,1),v(1,1,1),v(1,1,0), texture, flag));
                }
                else if ((dir == south && open && hinge == right) || (dir == east && !open && hinge == right) || (dir == east && !open && hinge == left) || (dir == north && open && hinge == left)) {
                    flag = 3;
                    quads.add(createSquareQuad(v(0, 1, 0), v(0, 0, 0), v(0, 0, 1), v(0, 1, 1), texture));
                    quads.add(createSquareQuad(v(3 / 16f, 1, 1), v(3 / 16f, 0, 1), v(3 / 16f, 0, 0), v(3 / 16f, 1, 0), texture));
                    quads.add(create3x16SideQuad(v(3 / 16f, 1, 0), v(3 / 16f, 0, 0), v(0, 0, 0), v(0, 1, 0), texture, flag));
                    quads.add(create3x16SideQuad(v(0, 1, 1), v(0, 0, 1), v(3/16f, 0, 1), v(3/16f, 1, 1), texture, flag));
                    quads.add(create3x16TopQuad(v(0, 0, 1),v(0,0,0),v(3/16f,0,0),v(3/16f,0,1), texture, flag));
                    quads.add(create3x16TopQuad(v(0, 1, 0),v(0,1,1),v(3/16f,1,1),v(3/16f,1,0), texture, flag));
                } else {
                    quads.add(create3x16SideQuad(v(0, 1, 0), v(0, 0, 0), v(0, 0, 3/16f), v(0, 1, 3/16f), texture, flag));
                    quads.add(create3x16SideQuad(v(1, 1, 3/16f), v(1, 0, 3/16f), v(1, 0, 0), v(1, 1, 0), texture, flag));
                    quads.add(createSquareQuad(v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), v(0, 1, 0), texture));
                    quads.add(createSquareQuad(v(0, 1, 3/16f), v(0, 0, 3/16f), v(1, 0, 3/16f), v(1, 1, 3/16f), texture));
                    quads.add(create3x16TopQuad(v(0, 0, 3/16f),v(0,0,0),v(1,0,0),v(1,0,3/16f), texture, flag));
                    quads.add(create3x16TopQuad(v(0, 1, 0),v(0,1,3/16f),v(1,1,3/16f),v(1,1,0), texture, flag));
                }
            //}
            /*if(half==upper) {
                int flag = 0;
                if ((dir == north && !open && hinge == right) || (dir == east && open && hinge == right) || (dir == west && open && hinge == left) || (dir == north && !open && hinge == left)) {
                    flag = 1;

                }
                if ((dir == west && !open && hinge == right) || (dir == north && open && hinge == right) || (dir == south && open && hinge == left) || (dir == west && !open && hinge == left)) {
                    flag = 2;
                    quads.add(createSquareQuad(v(13 / 16f, 1, 0), v(13 / 16f, 0, 0), v(13 / 16f, 0, 1), v(13 / 16f, 1, 1), texture));
                    quads.add(createSquareQuad(v(1, 1, 1), v(1, 0, 1), v(1, 0, 0), v(1, 1, 0), texture));
                    quads.add(create3x16x16Quad(v(13 / 16f, 0, 0), v(1, 0, 0), v(1, 1, 0), v(13 / 16f, 1, 0), texture, flag));
                    quads.add(create3x16x16Quad(v(13 / 16f, 1, 1), v(13 / 16f, 0, 1), v(1, 0, 1), v(1, 1, 1), texture, flag));
                }
                if ((dir == south && open && hinge == right) || (dir == east && !open && hinge == right) || (dir == east && !open && hinge == left) || (dir == north && open && hinge == left)) {
                    flag = 3;
                    quads.add(createSquareQuad(v(0, 1, 0), v(0, 0, 0), v(0, 0, 1), v(0, 1, 1), texture));
                    quads.add(createSquareQuad(v(3 / 16f, 1, 1), v(3 / 16f, 0, 1), v(3 / 16f, 0, 0), v(3 / 16f, 1, 0), texture));
                    quads.add(create3x16x16Quad(v(3 / 16f, 1, 0), v(3 / 16f, 0, 0), v(0, 0, 0), v(0, 1, 0), texture, flag));
                    quads.add(create3x16x16Quad(v(3 / 16f, 0, 1), v(3 / 16f, 1, 1), v(0, 1, 1), v(0, 0, 1), texture, flag));
                }
            }*/
            //down
            //quads.add(createUpDownQuad(v(x(direction,face), y(direction,face), z(direction,face)), v(x(direction,face)+xr(direction, face), y(direction,face), z(direction,face)), v(x(direction,face)+xr(direction, face), y(direction,face), z(direction,face)+zr(direction, face)), v(x(direction,face), y(direction,face), z(direction,face)+zr(direction, face)), texture, direction, face));
            //up
            //quads.add(createUpDownQuad(v(x(direction,face), y2(direction, face), z(direction,face)+zr(direction, face)), v(x(direction,face)+xr(direction, face), y2(direction,face), z(direction,face)+zr(direction, face)), v(x(direction,face)+xr(direction, face), y2(direction,face), z(direction,face)), v(x(direction,face), y2(direction, face), z(direction,face)), texture, direction, face));
            //sides
            //quads.add(createLongSideQuad(v(x(direction,face), y(direction,face), z(direction, face)+zr(direction, face)), v(x(direction,face), y2(direction,face), z(direction, face)+zr(direction, face)), v(x(direction,face), y2(direction,face), z(direction, face)), v(x(direction,face), y(direction,face), z(direction, face)), texture, direction, face));
            //quads.add(createLongSideQuad(v(x(direction,face)+xr(direction,face), y(direction,face), z(direction, face)), v(x(direction,face)+xr(direction,face), y2(direction,face), z(direction, face)), v(x(direction,face)+xr(direction,face), y2(direction,face), z(direction, face)+zr(direction, face)), v(x(direction,face)+xr(direction,face), y(direction,face), z(direction, face)+zr(direction, face)), texture, direction, face));
            //quads.add(createShortSideQuad(v(x(direction,face), y(direction,face), z(direction, face)), v(x(direction,face), y2(direction,face), z(direction, face)), v(x(direction,face)+xr(direction,face), y2(direction,face), z(direction, face)), v(x(direction,face)+xr(direction,face), y(direction,face), z(direction, face)), texture, direction, face));
            //quads.add(createShortSideQuad(v(x(direction,face)+xr(direction,face), y(direction,face), z(direction, face)+zr(direction, face)), v(x(direction,face)+xr(direction,face), y2(direction,face), z(direction, face)+zr(direction, face)), v(x(direction,face), y2(direction,face), z(direction, face)+zr(direction, face)), v(x(direction,face), y(direction,face), z(direction, face)+zr(direction, face)), texture, direction, face));
            //quads.add(createBottomPartQuad(texture, flag));

            return quads;
        }
        return Collections.emptyList();
    }

    //getXPositionForQuad
    private float x(Direction direction, AttachFace face) {
        float x;
        switch (face) {
            case CEILING:
            case FLOOR:
                System.out.println("x  "+direction);
                switch (direction) {
                    case NORTH:
                    case SOUTH:
                        x = 5/16f;
                        break;
                    default:
                        x = 6/16f;
                        break;
            }
            break;
            default:
                System.out.println("x2  "+direction);
                switch (direction) {
                    case EAST:
                        x = -2/16f;
                        break;
                    case WEST:
                        x = 14/16f;
                        break;
                    case NORTH:
                    case SOUTH:
                        x = 5/16f;
                        break;
                    default:
                        x = 0.5f;
                        break;
                }
        }
        System.out.println("final x  "+x);
        return x;
    }

    //getYPositionForQuad (bottom side)
    private float y(Direction direction, AttachFace face) {
        float y;
        System.out.println("y  "+direction+" "+face);
        switch (face) {
            case CEILING:
                y=14/16f;
                break;
            case FLOOR:
                y=0;
                break;
            default:
                y = 6/16f;
        }
        System.out.println("final y  "+y);
        return y;
    }

    //getYPositionForQuad (top side)
    private float y2(Direction direction, AttachFace face) {
        float y;
        System.out.println("y  "+direction+" "+face);
        switch (face) {
            case CEILING:
                y=1;
                break;
            case FLOOR:
                y=2/16f;
                break;
            default:
                y = 10/16f;
        }
        System.out.println("final y  "+y);
        return y;
    }

    //getZPositionForQuad
    private float z(Direction direction, AttachFace face) {
        float z;
        switch (face) {
            case CEILING:
            case FLOOR:
                System.out.println("z  "+direction);
                switch (direction) {
                    case NORTH:
                    case SOUTH:
                        z = 6/16f;
                        break;
                    default:
                        z = 5/16f;
                        break;
                }
                break;
            default:
                System.out.println("z2  "+direction);
                switch (direction) {
                    case NORTH:
                        z = 14/16f;
                        break;
                    case SOUTH:
                        z = -2/16f;
                        break;
                    case EAST:
                    case WEST:
                        z = 5/16f;
                        break;
                    default:
                        z = 0.5f;
                        break;
                }
        }
        System.out.println("final z  "+z);
        return z;
    }

    //"x-rotation" of quad (by 90degrees)
    private float xr(Direction direction, AttachFace face) {
        float r;
        switch (direction) {
            case SOUTH:
            case NORTH:
                r=6/16f;
                break;
            case WEST:
            case EAST:
                r=4/16f;
                break;
            default:
                r=0;
        }
        return r;
    }

    //"z-rotation" of quad (by 90degrees)
    private float zr(Direction direction, AttachFace face) {
        float r;
        switch (direction) {
            case SOUTH:
            case NORTH:
                r=4/16f;
                break;
            case WEST:
            case EAST:
                r=6/16f;
                break;
            default:
                r=0;
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
