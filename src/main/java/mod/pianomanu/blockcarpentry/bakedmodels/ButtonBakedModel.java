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
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.tileentity.TileEntity;
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

public class ButtonBakedModel implements IDynamicBakedModel {
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
    private BakedQuad createUpDownQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        //builder.setQuadOrientation(facing);
        builder.setApplyDiffuseLighting(true);
        putVertex(builder, normal, v1.x, v1.y, v1.z, 5, 6, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 5, 10, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 11, 10, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 11, 6, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    //North/South
    private BakedQuad createLongSideQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        //builder.setQuadOrientation(facing);
        builder.setApplyDiffuseLighting(true);
        putVertex(builder, normal, v1.x, v1.y, v1.z, 5, 14, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 5, 16, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 11, 16, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 11, 14, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    //East/West
    private BakedQuad createShortSideQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        //builder.setQuadOrientation(facing);
        builder.setApplyDiffuseLighting(true);
        putVertex(builder, normal, v1.x, v1.y, v1.z, 6, 14, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 6, 16, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 10, 16, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 10, 14, sprite, 1.0f, 1.0f, 1.0f);
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
        if (mimic != null && !(mimic.getBlock() instanceof FrameBlock)) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            if (location != null) {
                IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                model.getBakedModel().getQuads(mimic, side, rand, extraData);
                if (model != null) {
                    //only if model (from block saved in tile entity) exists:
                    return getMimicQuads(mimic, side, rand, extraData);
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
        if (mimic!=null) {
            //get texture from block in tile entity and apply it to the quads
            TextureAtlasSprite texture = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(mimic.getBlock().getRegistryName().getNamespace(), "block/" + mimic.getBlock().getRegistryName().getPath()));
            List<BakedQuad> quads = new ArrayList<>();
            //down
            quads.add(createUpDownQuad(v(5 / 16f, 0, 6 / 16f), v(11 / 16f, 0, 6 / 16f), v(11 / 16f, 0, 10 / 16f), v(5 / 16f, 0, 10 / 16f), texture));
            //up
            quads.add(createUpDownQuad(v(5 / 16f, 2/16f, 10 / 16f), v(11 / 16f, 2/16f, 10 / 16f), v(11 / 16f, 2/16f, 6 / 16f), v(5 / 16f, 2/16f, 6 / 16f), texture));
            //sides
            quads.add(createLongSideQuad(v(5/16f, 0, 10/16f), v(5/16f, 2/16f, 10/16f), v(5/16f, 2/16f, 6/16f), v(5/16f, 0, 6/16f), texture));
            quads.add(createLongSideQuad(v(11/16f, 0, 6/16f), v(11/16f, 2/16f, 6/16f), v(11/16f, 2/16f, 10/16f), v(11/16f, 0, 10/16f), texture));
            quads.add(createShortSideQuad(v(5/16f, 0, 6/16f), v(5/16f, 2/16f, 6/16f), v(11/16f, 2/16f, 6/16f), v(11/16f, 0, 6/16f), texture));
            quads.add(createShortSideQuad(v(11/16f, 0, 10/16f), v(11/16f, 2/16f, 10/16f), v(5/16f, 2/16f, 10/16f), v(5/16f, 0, 10/16f), texture));

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
