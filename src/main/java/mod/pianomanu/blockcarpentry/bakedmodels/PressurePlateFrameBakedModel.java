package mod.pianomanu.blockcarpentry.bakedmodels;

import com.google.common.collect.ImmutableList;
import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
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

public class PressurePlateFrameBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/frameblock");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    //@Override
    public boolean func_230044_c_() {
        return false;
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
    }

    private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        putVertex(builder, normal, v1.x, v1.y, v1.z, 1, 1, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 1, 15, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 15, 15, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 15, 1, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    private BakedQuad createFlatQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        putVertex(builder, normal, v1.x, v1.y, v1.z, 1, 1, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 1, 2, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 15, 2, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 15, 1, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    private static Vec3d v(double x, double y, double z) {
        return new Vec3d(x, y, z);
    }


    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {

        //Block im Slab
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        //Es existiert ein block im slab und dieser ist nicht der slab selbst
        if (mimic != null && !(mimic.getBlock() instanceof FrameBlock)) {
            //location des BlockModels im Slab
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            //wenn location existiert...
            if (location != null) {
                //neues Bakedmodel des Models von der gegebenen location
                IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                model.getBakedModel().getQuads(mimic, side, rand, extraData);
                //wenn dieses model existiert...
                if (model != null) {
                    //neues quad-model mit den blockdaten aus dem slab
                    //return model.getQuads(mimic, side, rand, extraData);
                    return getMimicQuads(mimic, side, rand, extraData);
                }
            }
        }

        //if (side != null) {
        return Collections.emptyList();
        //}

        //TextureAtlasSprite texture = getTexture();
        /*List<BakedQuad> quads = new ArrayList<>();

        //down
        quads.add(createQuad(v(1,0,0), v(1,0,1), v(0,0,1), v(0,0,0),texture));
        //up
        quads.add(createQuad(v(0,0.5,0), v(0,0.5,1), v(1,0.5,1), v(1,0.5,0),texture));
        //sides
        quads.add(createQuad(v(0,0,0), v(0,0,1), v(0,0.5,1), v(0,0.5,0),texture));
        quads.add(createQuad(v(0,0,0), v(0,0.5,0), v(1,0.5,0), v(1,0,0),texture));
        quads.add(createQuad(v(0,0,1), v(1,0,1), v(1,0.5,1), v(0,0.5,1),texture));
        quads.add(createQuad(v(1,0.5,0), v(1,0.5,1), v(1,0,1), v(1,0,0),texture));/

        return quads;*/
    }

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic!=null) {
            TextureAtlasSprite texture = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(mimic.getBlock().getRegistryName().getNamespace(), "block/"+mimic.getBlock().getRegistryName().getPath()));
            List<BakedQuad> quads = new ArrayList<>();
            //down
            quads.add(createQuad(v(15/16f, 0, 1/16f), v(15/16f, 0, 15/16f), v(1/16f, 0, 15/16f), v(1/16f, 0, 1/16f), texture));
            //up
            quads.add(createQuad(v(1/16f, 1/16f, 1/16f), v(1/16f, 1/16f, 15/16f), v(15/16f, 1/16f, 15/16f), v(15/16f, 1/16f, 1/16f), texture));
            //sides
            quads.add(createFlatQuad(v(1/16f, 0, 15/16f), v(1/16f, 1/16f, 15/16f), v(1/16f, 1/16f, 1/16f), v(1/16f, 0, 1/16f), texture));
            quads.add(createFlatQuad(v(1/16f, 0, 1/16f), v(1/16f, 1/16f, 1/16f), v(15/16f, 1/16f, 1/16f), v(15/16f, 0, 1/16f), texture));
            quads.add(createFlatQuad(v(15/16f, 0, 15/16f), v(15/16f, 1/16f, 15/16f), v(1/16f, 1/16f, 15/16f), v(1/16f, 0, 15/16f), texture));
            quads.add(createFlatQuad(v(15/16f, 1/16f, 15/16f), v(15/16f, 0, 15/16f), v(15/16f, 0, 1/16f), v(15/16f, 1/16f, 1/16f), texture));

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
