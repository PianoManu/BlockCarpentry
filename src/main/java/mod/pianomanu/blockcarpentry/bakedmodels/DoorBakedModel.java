package mod.pianomanu.blockcarpentry.bakedmodels;

import com.google.common.collect.ImmutableList;
import mod.pianomanu.blockcarpentry.bakedmodels.helper.DoorKnobBakedModel;
import mod.pianomanu.blockcarpentry.bakedmodels.helper.HandleBakedModel;
import mod.pianomanu.blockcarpentry.block.DoorFrameBlock;
import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
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
import net.minecraft.state.properties.Half;
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

public class DoorBakedModel implements IDynamicBakedModel {
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
        builder.setQuadTint(1);
    }

    //Top part of door
    private BakedQuad createTopPartQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, Direction facing, AttachFace face) {
        Vector3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
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
        Vector3d NWD= v(0,0,0);
        Vector3d NWU= v(0,16,0);
        Vector3d NED= v(3,0,0);
        Vector3d NEU= v(3,16,0);
        Vector3d SWD= v(0,0,16);
        Vector3d SWU= v(0,16,16);
        Vector3d SED= v(3,0,16);
        Vector3d SEU= v(3,16,16);
        Vector3d v1, v2, v3, v4;
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
        Vector3d normal1 = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();
        Vector3d normal2 = v3.subtract(v1).crossProduct(v2.subtract(v1)).normalize();
        Vector3d normal3 = v2.subtract(v3).crossProduct(v1.subtract(v3)).normalize();
        Vector3d normal4 = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();
        System.out.println(v1.toString()+", "+v2.toString()+", "+v3.toString()+", "+v4.toString());
        System.out.println(normal1.toString()+normal2.toString()+normal3.toString()+normal4.toString());

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal1.x, normal1.y, normal1.z));
        builder.setApplyDiffuseLighting(true);
        float uShort = 6;
        float uLong = 10;
        float vShort = 14;
        float vLong = 16;
        putVertex(builder, normal1, v1.x, v1.y, v1.z, uShort, vShort, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal1, v2.x, v2.y, v2.z, uShort, vLong, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal1, v3.x, v3.y, v3.z, uLong, vLong, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal1, v4.x, v4.y, v4.z, uLong, vShort, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    private static Vector3d v(double x, double y, double z) {
        return new Vector3d(x, y, z);
    }

    private BakedQuad createSquareQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite) {
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

    private BakedQuad create3x16SideQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, int flag) {
        Vector3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

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

    private BakedQuad create3x16TopQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, int flag) {
        Vector3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

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
        int tex = extraData.getData(FrameBlockTile.TEXTURE);
        if (mimic!=null && state != null && extraData.getData(FrameBlockTile.DESIGN) != null && extraData.getData(FrameBlockTile.DESIGN_TEXTURE) != null) {
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
            Direction dir = state.get(DoorFrameBlock.FACING);
            boolean open = state.get(DoorFrameBlock.OPEN);
            DoorHingeSide hinge = state.get(DoorFrameBlock.HINGE);
            Direction west = Direction.WEST;
            Direction east = Direction.EAST;
            Direction north = Direction.NORTH;
            Direction south = Direction.SOUTH;
            DoorHingeSide left = DoorHingeSide.LEFT;
            DoorHingeSide right = DoorHingeSide.RIGHT;
            int design = extraData.getData(FrameBlockTile.DESIGN);//int design = state.get(DoorFrameBlock.DESIGN);
            int desTex = extraData.getData(FrameBlockTile.DESIGN_TEXTURE); //state.get(DoorFrameBlock.DESIGN_TEXTURE);
            DoubleBlockHalf half = state.get(DoorBlock.HALF);
            DoubleBlockHalf lower = DoubleBlockHalf.LOWER;
            DoubleBlockHalf upper = DoubleBlockHalf.UPPER;
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
                if(design==1) {
                    if(half == lower) {
                        if((dir==south && hinge==left && !open) || (dir==west && hinge==right && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(2/16f,4/16f,15/16f,17/16f,-1/16f,1/16f,flag,desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(2/16f,4/16f,15/16f,17/16f,2/16f,4/16f,flag,desTex));
                        } else if((dir==south && hinge==right && !open) || (dir==east && hinge==left && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(12/16f,14/16f,15/16f,17/16f,-1/16f,1/16f,flag,desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(12/16f,14/16f,15/16f,17/16f,2/16f,4/16f,flag,desTex));
                        }
                    }
                    if(half == lower) {
                        if((dir==north && hinge==right && !open) || (dir==west && hinge==left && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(2/16f,4/16f,15/16f,17/16f,15/16f,17/16f,flag,desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(2/16f,4/16f,15/16f,17/16f,12/16f,14/16f,flag,desTex));
                        } else if((dir==north && hinge==left && !open) || (dir==east && hinge==right && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(12/16f,14/16f,15/16f,17/16f,15/16f,17/16f,flag,desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(12/16f,14/16f,15/16f,17/16f,12/16f,14/16f,flag,desTex));
                        }
                    }
                    if(half == lower) {
                        if((dir==west && hinge==left && !open) || (dir==north && hinge == right && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(15/16f,17/16f,15/16f,17/16f,2/16f,4/16f,flag,desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(12/16f,14/16f,15/16f,17/16f,2/16f,4/16f,flag,desTex));
                        } else if((dir==west && hinge==right && !open) || (dir==south && hinge == left && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(15/16f,17/16f,15/16f,17/16f,12/16f,14/16f,flag,desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(12/16f,14/16f,15/16f,17/16f,12/16f,14/16f,flag,desTex));
                        }
                    }
                    if(half == lower) {
                        if((dir==east && hinge==right && !open) || (dir==north && hinge == left && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(-1/16f,1/16f,15/16f,17/16f,2/16f,4/16f,flag,desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(2/16f,4/16f,15/16f,17/16f,2/16f,4/16f,flag,desTex));
                        } else if((dir==east && hinge==left && !open) || (dir==south && hinge == right && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(-1/16f,1/16f,15/16f,17/16f,12/16f,14/16f,flag,desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(2/16f,4/16f,15/16f,17/16f,12/16f,14/16f,flag,desTex));
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
