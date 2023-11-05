package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {

        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && side == null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            boolean keepUV = extraData.getData(FrameBlockTile.KEEP_UV);
            boolean renderNorth = extraData.getData(FrameBlockTile.NORTH_VISIBLE);
            boolean renderEast = extraData.getData(FrameBlockTile.EAST_VISIBLE);
            boolean renderSouth = extraData.getData(FrameBlockTile.SOUTH_VISIBLE);
            boolean renderWest = extraData.getData(FrameBlockTile.WEST_VISIBLE);
            boolean renderUp = extraData.getData(FrameBlockTile.UP_VISIBLE);
            boolean renderDown = extraData.getData(FrameBlockTile.DOWN_VISIBLE);
            Vec3 NWU;
            Vec3 SWU;
            Vec3 NEU;
            Vec3 SEU;
            Vec3 NWD;
            Vec3 SWD;
            Vec3 NED;
            Vec3 SED;
            try {
                NWU = new Vec3(0, 1, 0).add(extraData.getData(FrameBlockTile.NWU_prop).multiply(1 / 16d, 1 / 16d, 1 / 16d)); //North-West-Up
                SWU = new Vec3(0, 1, 1).add(extraData.getData(FrameBlockTile.SWU_prop).multiply(1 / 16d, 1 / 16d, 1 / 16d)); //...
                NWD = new Vec3(0, 0, 0).add(extraData.getData(FrameBlockTile.NWD_prop).multiply(1 / 16d, 1 / 16d, 1 / 16d));
                SWD = new Vec3(0, 0, 1).add(extraData.getData(FrameBlockTile.SWD_prop).multiply(1 / 16d, 1 / 16d, 1 / 16d));
                NEU = new Vec3(1, 1, 0).add(extraData.getData(FrameBlockTile.NEU_prop).multiply(1 / 16d, 1 / 16d, 1 / 16d));
                SEU = new Vec3(1, 1, 1).add(extraData.getData(FrameBlockTile.SEU_prop).multiply(1 / 16d, 1 / 16d, 1 / 16d));
                NED = new Vec3(1, 0, 0).add(extraData.getData(FrameBlockTile.NED_prop).multiply(1 / 16d, 1 / 16d, 1 / 16d));
                SED = new Vec3(1, 0, 1).add(extraData.getData(FrameBlockTile.SED_prop).multiply(1 / 16d, 1 / 16d, 1 / 16d)); //South-East-Down
            } catch (NullPointerException e) {
                NWU = new Vec3(0, 1, 0); //North-West-Up
                SWU = new Vec3(0, 1, 1); //...
                NWD = new Vec3(0, 0, 0);
                SWD = new Vec3(0, 0, 1);
                NEU = new Vec3(1, 1, 0);
                SEU = new Vec3(1, 1, 1);
                NED = new Vec3(1, 0, 0);
                SED = new Vec3(1, 0, 1); //South-East-Down
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