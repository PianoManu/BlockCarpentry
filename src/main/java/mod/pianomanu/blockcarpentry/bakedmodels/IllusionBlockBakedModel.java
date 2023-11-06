package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.vector.Vector3d;
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
 * See {@link mod.pianomanu.blockcarpentry.util.ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.3 10/23/23
 */
public class IllusionBlockBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {

        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && side == null) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            boolean keepUV = extraData.getData(FrameBlockTile.KEEP_UV);
            boolean renderNorth = extraData.getData(FrameBlockTile.NORTH_VISIBLE);
            boolean renderEast = extraData.getData(FrameBlockTile.EAST_VISIBLE);
            boolean renderSouth = extraData.getData(FrameBlockTile.SOUTH_VISIBLE);
            boolean renderWest = extraData.getData(FrameBlockTile.WEST_VISIBLE);
            boolean renderUp = extraData.getData(FrameBlockTile.UP_VISIBLE);
            boolean renderDown = extraData.getData(FrameBlockTile.DOWN_VISIBLE);
            Vector3d NWU;
            Vector3d SWU;
            Vector3d NEU;
            Vector3d SEU;
            Vector3d NWD;
            Vector3d SWD;
            Vector3d NED;
            Vector3d SED;
            try {
                NWU = new Vector3d(0, 1, 0).add(extraData.getData(FrameBlockTile.NWU_prop).mul(1 / 16d, 1 / 16d, 1 / 16d)); //North-West-Up
                SWU = new Vector3d(0, 1, 1).add(extraData.getData(FrameBlockTile.SWU_prop).mul(1 / 16d, 1 / 16d, 1 / 16d)); //...
                NWD = new Vector3d(0, 0, 0).add(extraData.getData(FrameBlockTile.NWD_prop).mul(1 / 16d, 1 / 16d, 1 / 16d));
                SWD = new Vector3d(0, 0, 1).add(extraData.getData(FrameBlockTile.SWD_prop).mul(1 / 16d, 1 / 16d, 1 / 16d));
                NEU = new Vector3d(1, 1, 0).add(extraData.getData(FrameBlockTile.NEU_prop).mul(1 / 16d, 1 / 16d, 1 / 16d));
                SEU = new Vector3d(1, 1, 1).add(extraData.getData(FrameBlockTile.SEU_prop).mul(1 / 16d, 1 / 16d, 1 / 16d));
                NED = new Vector3d(1, 0, 0).add(extraData.getData(FrameBlockTile.NED_prop).mul(1 / 16d, 1 / 16d, 1 / 16d));
                SED = new Vector3d(1, 0, 1).add(extraData.getData(FrameBlockTile.SED_prop).mul(1 / 16d, 1 / 16d, 1 / 16d)); //South-East-Down
            } catch (NullPointerException e) {
                NWU = new Vector3d(0, 1, 0); //North-West-Up
                SWU = new Vector3d(0, 1, 1); //...
                NWD = new Vector3d(0, 0, 0);
                SWD = new Vector3d(0, 0, 1);
                NEU = new Vector3d(1, 1, 0);
                SEU = new Vector3d(1, 1, 1);
                NED = new Vector3d(1, 0, 0);
                SED = new Vector3d(1, 0, 1); //South-East-Down
            }
            List<Direction> directions = extraData.getData(FrameBlockTile.DIRECTIONS);
            List<Integer> rotations = extraData.getData(FrameBlockTile.ROTATIONS);
            List<BakedQuad> quads = new ArrayList<>(ModelHelper.createSixFaceCuboid(NWU, SWU, NWD, SWD, NEU, SEU, NED, SED, mimic, model, extraData, rand, tintIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, directions, rotations, keepUV, false));
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 1f, overlayIndex, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown, true));
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
    @Nonnull
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
//========SOLI DEO GLORIA========//