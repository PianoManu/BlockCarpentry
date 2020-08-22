package mod.pianomanu.blockcarpentry.bakedmodels;

import com.google.common.collect.ImmutableList;
import mod.pianomanu.blockcarpentry.bakedmodels.helper.HandleBakedModel;
import mod.pianomanu.blockcarpentry.block.DoorFrameBlock;
import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.block.TrapdoorFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.DoorHingeSide;
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

public class TrapdoorBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    private void putVertex(BakedQuadBuilder builder, Vector3d normal,
                           double x, double y, double z, float u, float v, TextureAtlasSprite sprite, float r, float g, float b) {

        ImmutableList<VertexFormatElement> elements = builder.getVertexFormat().getElements().asList();
        for (int j = 0; j < elements.size(); j++) {
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
        if (flag == 0 || flag == 2) {
            ul = 13;
            uh = 16;
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
        if (mimic != null && state != null) {
            //get texture from block in tile entity and apply it to the quads
            List<TextureAtlasSprite> textureList = TextureHelper.getTextureListFromBlock(mimic.getBlock());
            TextureAtlasSprite texture;
            if (textureList.size() > tex) {
                texture = textureList.get(tex);
            } else {
                texture = textureList.get(0);
            }
            List<BakedQuad> quads = new ArrayList<>();
            Direction dir = state.get(DoorFrameBlock.FACING);
            boolean open = state.get(TrapdoorFrameBlock.OPEN);
            Half half = state.get(TrapDoorBlock.HALF);
            Half top = Half.TOP;
            Half bottom = Half.BOTTOM;
            Direction west = Direction.WEST;
            Direction east = Direction.EAST;
            Direction north = Direction.NORTH;
            Direction south = Direction.SOUTH;
            //int design = 0; //fixme
            int design = extraData.getData(FrameBlockTile.DESIGN);//int design = state.get(DoorFrameBlock.DESIGN);
            int desTex = extraData.getData(FrameBlockTile.DESIGN_TEXTURE); //state.get(DoorFrameBlock.DESIGN_TEXTURE);
            int flag = 0;

            if (dir == north && open) {
                flag = 1;
                quads.add(create3x16SideQuad(v(0, 1, 13 / 16f), v(0, 0, 13 / 16f), v(0, 0, 1), v(0, 1, 1), texture, flag));
                quads.add(create3x16SideQuad(v(1, 1, 1), v(1, 0, 1), v(1, 0, 13 / 16f), v(1, 1, 13 / 16f), texture, flag));
                quads.add(createSquareQuad(v(1, 1, 13 / 16f), v(1, 0, 13 / 16f), v(0, 0, 13 / 16f), v(0, 1, 13 / 16f), texture));
                quads.add(createSquareQuad(v(0, 1, 1), v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), texture));
                quads.add(create3x16TopQuad(v(0, 0, 1), v(0, 0, 13 / 16f), v(1, 0, 13 / 16f), v(1, 0, 1), texture, flag));
                quads.add(create3x16TopQuad(v(0, 1, 13 / 16f), v(0, 1, 1), v(1, 1, 1), v(1, 1, 13 / 16f), texture, flag));

            } else if (dir == west && open) {
                flag = 2;
                quads.add(createSquareQuad(v(13 / 16f, 1, 0), v(13 / 16f, 0, 0), v(13 / 16f, 0, 1), v(13 / 16f, 1, 1), texture));
                quads.add(createSquareQuad(v(1, 1, 1), v(1, 0, 1), v(1, 0, 0), v(1, 1, 0), texture));
                quads.add(create3x16SideQuad(v(13 / 16f, 0, 0), v(13 / 16f, 1, 0), v(1, 1, 0), v(1, 0, 0), texture, flag));
                quads.add(create3x16SideQuad(v(13 / 16f, 1, 1), v(13 / 16f, 0, 1), v(1, 0, 1), v(1, 1, 1), texture, flag));
                quads.add(create3x16TopQuad(v(13 / 16f, 0, 1), v(13 / 16f, 0, 0), v(1, 0, 0), v(1, 0, 1), texture, flag));
                quads.add(create3x16TopQuad(v(13 / 16f, 1, 0), v(13 / 16f, 1, 1), v(1, 1, 1), v(1, 1, 0), texture, flag));
            } else if (dir == east && open) {
                flag = 3;
                quads.add(createSquareQuad(v(0, 1, 0), v(0, 0, 0), v(0, 0, 1), v(0, 1, 1), texture));
                quads.add(createSquareQuad(v(3 / 16f, 1, 1), v(3 / 16f, 0, 1), v(3 / 16f, 0, 0), v(3 / 16f, 1, 0), texture));
                quads.add(create3x16SideQuad(v(3 / 16f, 1, 0), v(3 / 16f, 0, 0), v(0, 0, 0), v(0, 1, 0), texture, flag));
                quads.add(create3x16SideQuad(v(0, 1, 1), v(0, 0, 1), v(3 / 16f, 0, 1), v(3 / 16f, 1, 1), texture, flag));
                quads.add(create3x16TopQuad(v(0, 0, 1), v(0, 0, 0), v(3 / 16f, 0, 0), v(3 / 16f, 0, 1), texture, flag));
                quads.add(create3x16TopQuad(v(0, 1, 0), v(0, 1, 1), v(3 / 16f, 1, 1), v(3 / 16f, 1, 0), texture, flag));

            } else if (dir == south && open) {
                quads.add(create3x16SideQuad(v(0, 1, 0), v(0, 0, 0), v(0, 0, 3 / 16f), v(0, 1, 3 / 16f), texture, flag));
                quads.add(create3x16SideQuad(v(1, 1, 3 / 16f), v(1, 0, 3 / 16f), v(1, 0, 0), v(1, 1, 0), texture, flag));
                quads.add(createSquareQuad(v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), v(0, 1, 0), texture));
                quads.add(createSquareQuad(v(0, 1, 3 / 16f), v(0, 0, 3 / 16f), v(1, 0, 3 / 16f), v(1, 1, 3 / 16f), texture));
                quads.add(create3x16TopQuad(v(0, 0, 3 / 16f), v(0, 0, 0), v(1, 0, 0), v(1, 0, 3 / 16f), texture, flag));
                quads.add(create3x16TopQuad(v(0, 1, 0), v(0, 1, 3 / 16f), v(1, 1, 3 / 16f), v(1, 1, 0), texture, flag));

            } else if (half == bottom) {
                quads.add(create3x16TopQuad(v(0, 3 / 16f, 0), v(0, 0, 0), v(0, 0, 1), v(0, 3 / 16f, 1), texture, flag));
                quads.add(create3x16TopQuad(v(1, 3 / 16f, 1), v(1, 0, 1), v(1, 0, 0), v(1, 3 / 16f, 0), texture, flag));
                quads.add(create3x16TopQuad(v(1, 3 / 16f, 0), v(1, 0, 0), v(0, 0, 0), v(0, 3 / 16f, 0), texture, flag));
                quads.add(create3x16TopQuad(v(0, 3 / 16f, 1), v(0, 0, 1), v(1, 0, 1), v(1, 3 / 16f, 1), texture, flag));
                quads.add(createSquareQuad(v(0, 0, 1), v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), texture));
                quads.add(createSquareQuad(v(0, 3 / 16f, 0), v(0, 3 / 16f, 1), v(1, 3 / 16f, 1), v(1, 3 / 16f, 0), texture));
            } else if (half == top) {
                quads.add(create3x16TopQuad(v(0, 1, 0), v(0, 13 / 16f, 0), v(0, 13 / 16f, 1), v(0, 1, 1), texture, flag));
                quads.add(create3x16TopQuad(v(1, 1, 1), v(1, 13 / 16f, 1), v(1, 13 / 16f, 0), v(1, 1, 0), texture, flag));
                quads.add(create3x16TopQuad(v(1, 1, 0), v(1, 13 / 16f, 0), v(0, 13 / 16f, 0), v(0, 1, 0), texture, flag));
                quads.add(create3x16TopQuad(v(0, 1, 1), v(0, 13 / 16f, 1), v(1, 13 / 16f, 1), v(1, 1, 1), texture, flag));
                quads.add(createSquareQuad(v(0, 13 / 16f, 1), v(0, 13 / 16f, 0), v(1, 13 / 16f, 0), v(1, 13 / 16f, 1), texture));
                quads.add(createSquareQuad(v(0, 1, 0), v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), texture));

            }
            if (design == 1) {
                /*DIFFERENT DIRECTIONS = DIFFERENT QUADS
                MATCHING PAIRS:
                        rotationFlag == 0: WEST/UP/CLOSED EAST/UP/CLOSED WEST/DOWN/CLOSED EAST/DOWN/CLOSED
                        rotationFlag == 1: NORTH/UP/CLOSED SOUTH/UP/CLOSED NORTH/DOWN/CLOSED SOUTH/DOWN/CLOSED
                        rotationFlag == 2: NORTH/UP/OPEN NORTH/DOWN/OPEN SOUTH/UP/OPEN SOUTH/DOWN/OPEN
                        rotationFlag == 3: EAST/UP/OPEN EAST/DOWN/OPEN WEST/UP/OPEN WEST/DOWN/OPEN
                 */
                int rotationFlag = 0;
                if (open && half == bottom) {
                    if (dir == north) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 13 / 16f, 14 / 16f, 12 / 16f, 17 / 16f,2, desTex));
                    }
                    if (dir == east) {
                        quads.addAll(HandleBakedModel.createHandle(-1 / 16f, 4 / 16f, 13 / 16f, 14 / 16f, 6 / 16f, 10 / 16f,3, desTex));
                    }
                    if (dir == south) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 13 / 16f, 14 / 16f, -1 / 16f, 4 / 16f,2, desTex));
                    }
                    if (dir == west) {
                        quads.addAll(HandleBakedModel.createHandle(12 / 16f, 17 / 16f, 13 / 16f, 14 / 16f, 6 / 16f, 10 / 16f,3, desTex));
                    }
                } else if (open && half == top) {
                    if (dir == north) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 2 / 16f, 3 / 16f, 12 / 16f, 17 / 16f,2, desTex));
                    }
                    if (dir == east) {
                        quads.addAll(HandleBakedModel.createHandle(-1 / 16f, 4 / 16f, 2 / 16f, 3 / 16f, 6 / 16f, 10 / 16f,3, desTex));
                    }
                    if (dir == south) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 2 / 16f, 3 / 16f, -1 / 16f, 4 / 16f,2, desTex));
                    }
                    if (dir == west) {
                        quads.addAll(HandleBakedModel.createHandle(12 / 16f, 17 / 16f, 2 / 16f, 3 / 16f, 6 / 16f, 10 / 16f,3, desTex));
                    }
                }
                if (half == bottom && !open) {
                    if (dir == north) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, -1 / 16f, 4 / 16f, 2 / 16f, 3 / 16f,1, desTex));
                    }
                    if (dir == east) {
                        quads.addAll(HandleBakedModel.createHandle(2 / 16f, 3 / 16f, -1 / 16f, 4 / 16f, 6 / 16f, 10 / 16f,0, desTex));
                    }
                    if (dir == south) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, -1 / 16f, 4 / 16f, 13 / 16f, 14 / 16f,1, desTex));
                    }
                    if (dir == west) {
                        quads.addAll(HandleBakedModel.createHandle(13 / 16f, 14 / 16f, -1 / 16f, 4 / 16f, 6 / 16f, 10 / 16f,0, desTex));
                    }
                } else if (half == top && !open) {
                    if (dir == north) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 12 / 16f, 17 / 16f, 2 / 16f, 3 / 16f,1, desTex));
                    }
                    if (dir == west) {
                        quads.addAll(HandleBakedModel.createHandle(2 / 16f, 3 / 16f, 12 / 16f, 17 / 16f, 6 / 16f, 10 / 16f,0, desTex));
                    }
                    if (dir == south) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 12 / 16f, 17 / 16f, 13 / 16f, 14 / 16f,1, desTex));
                    }
                    if (dir == east) {
                        quads.addAll(HandleBakedModel.createHandle(13 / 16f, 14 / 16f, 12 / 16f, 17 / 16f, 6 / 16f, 10 / 16f,0, desTex));
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
