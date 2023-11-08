package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.bakedmodels.ModelInformation;
import mod.pianomanu.blockcarpentry.bakedmodels.QuadUtils;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.IModelData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Simple class representing a 3d object with 8 corners (e.g. a cube), which is
 * used for easier building of BakedModels without hundreds of helper methods.
 *
 * @author PianoManu
 * @version 1.2 11/08/23
 */
public class SimpleBox {
    private Vec3 NWU;
    private Vec3 NWD;
    private Vec3 NEU;
    private Vec3 NED;
    private Vec3 SWU;
    private Vec3 SWD;
    private Vec3 SEU;
    private Vec3 SED;
    private final IModelData extraData;
    private final BakedModel model;
    private final Random rand;
    private final TextureAtlasSprite sprite;
    private final boolean renderUp;
    private final boolean renderDown;
    private final boolean renderNorth;
    private final boolean renderEast;
    private final boolean renderSouth;
    private final boolean renderWest;
    private Direction newUp = Direction.UP;
    private Direction newDown = Direction.DOWN;
    private Direction newNorth = Direction.NORTH;
    private Direction newEast = Direction.EAST;
    private Direction newSouth = Direction.SOUTH;
    private Direction newWest = Direction.WEST;
    boolean invertUp = false;
    boolean invertDown = false;
    boolean invertWest = false;
    boolean invertEast = false;
    boolean invertNorth = false;
    boolean invertSouth = false;
    private final List<Direction> directions;
    private final List<Integer> rotations;
    private final int tintIndex;
    private final boolean keepDefaultUV;
    private int overlayIndex;
    private ModelInformation modelInformation;
    private boolean doNotMoveOverlay;

    public static SimpleBox create(IModelData extraData, BakedModel model, Random rand) {
        Vec3 NWU = new Vec3(0, 1, 0);
        Vec3 NWD = new Vec3(0, 0, 0);
        Vec3 NEU = new Vec3(1, 1, 0);
        Vec3 NED = new Vec3(1, 0, 0);
        Vec3 SWU = new Vec3(0, 1, 1);
        Vec3 SWD = new Vec3(0, 0, 1);
        Vec3 SEU = new Vec3(1, 1, 1);
        Vec3 SED = new Vec3(1, 0, 1);
        return create(NWU, NWD, NEU, NED, SWU, SWD, SEU, SED, extraData, model, rand);
    }

    public static SimpleBox create(Vec3 NWU, Vec3 NWD, Vec3 NEU, Vec3 NED, Vec3 SWU, Vec3 SWD, Vec3 SEU, Vec3 SED, IModelData extraData, BakedModel model, Random rand) {
        TextureAtlasSprite sprite = TextureHelper.getTexture(model, rand, extraData, FrameBlockTile.TEXTURE);
        return create(NWU, NWD, NEU, NED, SWU, SWD, SEU, SED, extraData, model, rand, sprite, -1);
    }

    public static SimpleBox create(Vec3 NWU, Vec3 NWD, Vec3 NEU, Vec3 NED, Vec3 SWU, Vec3 SWD, Vec3 SEU, Vec3 SED,
                                   IModelData extraData, BakedModel model, Random rand, TextureAtlasSprite sprite, int tintIndex) {
        return create(NWU, NWD, NEU, NED, SWU, SWD, SEU, SED, extraData, model, rand, sprite, Collections.emptyList(), Collections.emptyList(), tintIndex, true);
    }

    public static SimpleBox create(Vec3 NWU, Vec3 NWD, Vec3 NEU, Vec3 NED, Vec3 SWU, Vec3 SWD, Vec3 SEU, Vec3 SED,
                                   IModelData extraData, BakedModel model, Random rand, TextureAtlasSprite sprite,
                                   List<Direction> directions, List<Integer> rotations,
                                   int tintIndex, boolean keepDefaultUV) {
        return create(NWU, NWD, NEU, NED, SWU, SWD, SEU, SED, extraData, model, rand, sprite, true, true, true, true, true, true, directions, rotations, tintIndex, keepDefaultUV);
    }

    public static SimpleBox create(Vec3 NWU, Vec3 NWD, Vec3 NEU, Vec3 NED, Vec3 SWU, Vec3 SWD, Vec3 SEU, Vec3 SED,
                                   IModelData extraData, BakedModel model, Random rand, TextureAtlasSprite sprite,
                                   boolean renderUp, boolean renderDown, boolean renderNorth, boolean renderEast, boolean renderSouth, boolean renderWest,
                                   List<Direction> directions, List<Integer> rotations,
                                   int tintIndex, boolean keepDefaultUV) {
        return create(NWU, NWD, NEU, NED, SWU, SWD, SEU, SED, extraData, model, rand, sprite, renderUp, renderDown, renderNorth, renderEast, renderSouth, renderWest, directions, rotations, tintIndex, keepDefaultUV, 0, false);
    }

    public static SimpleBox create(Vec3 NWU, Vec3 NWD, Vec3 NEU, Vec3 NED, Vec3 SWU, Vec3 SWD, Vec3 SEU, Vec3 SED,
                                   IModelData extraData, BakedModel model, Random rand, TextureAtlasSprite sprite,
                                   boolean renderUp, boolean renderDown, boolean renderNorth, boolean renderEast, boolean renderSouth, boolean renderWest,
                                   List<Direction> directions, List<Integer> rotations,
                                   int tintIndex, boolean keepDefaultUV, int overlayIndex, boolean doNotMoveOverlay) {
        return new SimpleBox(NWU, NWD, NEU, NED, SWU, SWD, SEU, SED, extraData, model, rand, sprite, renderUp, renderDown, renderNorth, renderEast, renderSouth, renderWest, directions, rotations, tintIndex, keepDefaultUV, overlayIndex, doNotMoveOverlay);
    }

    private SimpleBox(Vec3 NWU, Vec3 NWD, Vec3 NEU, Vec3 NED, Vec3 SWU, Vec3 SWD, Vec3 SEU, Vec3 SED,
                      IModelData extraData, BakedModel model, Random rand, TextureAtlasSprite sprite,
                      boolean renderUp, boolean renderDown, boolean renderNorth, boolean renderEast, boolean renderSouth, boolean renderWest,
                      List<Direction> directions, List<Integer> rotations,
                      int tintIndex, boolean keepDefaultUV, int overlayIndex, boolean doNotMoveOverlay) {
        this.NWU = NWU;
        this.NWD = NWD;
        this.NEU = NEU;
        this.NED = NED;
        this.SWU = SWU;
        this.SWD = SWD;
        this.SEU = SEU;
        this.SED = SED;
        this.extraData = extraData;
        this.model = model;
        this.rand = rand;
        this.sprite = sprite;
        this.renderUp = renderUp;
        this.renderDown = renderDown;
        this.renderNorth = renderNorth;
        this.renderEast = renderEast;
        this.renderSouth = renderSouth;
        this.renderWest = renderWest;
        this.directions = directions;
        this.rotations = rotations;
        this.tintIndex = tintIndex;
        this.keepDefaultUV = keepDefaultUV;
        this.doNotMoveOverlay = doNotMoveOverlay;
        this.updateOverlay();
    }

    private void updateOverlay() {
        this.overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
        this.modelInformation = TextureHelper.getOverlayModelInformation(overlayIndex);
    }

    public List<BakedQuad> getQuads() {
        updateOverlay();
        List<BakedQuad> quads = new ArrayList<>();
        Float[] floats = ModelHelper.checkWithinBounds((float) NWD.x, (float) SEU.x, (float) NWD.y, (float) SEU.y, (float) NWD.z, (float) SEU.z);
        if (floats == null)
            return createQuadsEmpty();

        this.rotate();

        int upRotation = rotations.size() == 6 ? rotations.get(1) : 0;
        int downRotation = rotations.size() == 6 ? rotations.get(0) : 0;
        int westRotation = rotations.size() == 6 ? rotations.get(4) : 0;
        int eastRotation = rotations.size() == 6 ? rotations.get(5) : 0;
        int northRotation = rotations.size() == 6 ? rotations.get(2) : 0;
        int southRotation = rotations.size() == 6 ? rotations.get(3) : 0;
        boolean useSimpleQuadCalc = CornerUtils.isCuboid(NWU, NEU, SWU, SEU, NWD, NED, SWD, SED) || !BCModConfig.USE_COMPLEX_QUAD_CALCULATIONS.get() || Boolean.TRUE.equals(extraData.getData(FrameBlockTile.REMAIN_RECTANGLE));

        if (renderUp)
            createQuadFace(quads, NWU, SWU, SEU, NEU, Direction.UP, newUp, upRotation, invertUp, useSimpleQuadCalc);
        if (renderDown)
            createQuadFace(quads, NED, SED, SWD, NWD, Direction.DOWN, newDown, downRotation, invertDown, useSimpleQuadCalc);
        if (renderWest)
            createQuadFace(quads, NWU, NWD, SWD, SWU, Direction.WEST, newWest, westRotation, invertWest, useSimpleQuadCalc);
        if (renderEast)
            createQuadFace(quads, SEU, SED, NED, NEU, Direction.EAST, newEast, eastRotation, invertEast, useSimpleQuadCalc);
        if (renderNorth)
            createQuadFace(quads, NEU, NED, NWD, NWU, Direction.NORTH, newNorth, northRotation, invertNorth, useSimpleQuadCalc);
        if (renderSouth)
            createQuadFace(quads, SWU, SWD, SED, SEU, Direction.SOUTH, newSouth, southRotation, invertSouth, useSimpleQuadCalc);
        return quads;
    }

    private void rotate() {
        Vec3[] rotatedVecs;
        Direction[] rotatedDirs;
        for (Direction d : directions) {
            rotatedVecs = CornerUtils.rotateVec3s(NWU, NEU, SWU, SEU, NWD, NED, SWD, SED, d);
            rotatedDirs = CornerUtils.getRotatedDirection(newUp, newDown, newNorth, newEast, newSouth, newWest, d);
            NWU = rotatedVecs[0];
            NEU = rotatedVecs[1];
            SWU = rotatedVecs[2];
            SEU = rotatedVecs[3];
            NWD = rotatedVecs[4];
            NED = rotatedVecs[5];
            SWD = rotatedVecs[6];
            SED = rotatedVecs[7];
            newUp = rotatedDirs[0];
            newDown = rotatedDirs[1];
            newNorth = rotatedDirs[2];
            newEast = rotatedDirs[3];
            newSouth = rotatedDirs[4];
            newWest = rotatedDirs[5];
        }
    }

    private List<BakedQuad> createQuadsEmpty() {
        return ModelHelper.createCuboid(NWU, SWU, NWD, SWD, NEU, SEU, NED, SED, TextureHelper.getMissingTexture());
    }

    private void createQuadFace(List<BakedQuad> quads, Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, Direction originalDirection, Direction newDirection, int rotation, boolean invert, boolean useSimpleQuadCalculation) {
        if (useSimpleQuadCalculation) {
            quads.add(QuadUtils.createQuad(v1, v2, v3, v4, sprite, newDirection, tintIndex, rotation, keepDefaultUV, invert));
            if (overlayIndex > 0 && overlayTexture(originalDirection) != null)
                quads.add(QuadUtils.createQuad(v1, v2, v3, v4, overlayTexture(originalDirection), originalDirection, modelInformation.tintIndex, 0, doNotMoveOverlay, false));
        } else {
            quads.addAll(QuadUtils.createQuadComplex(v1, v2, v3, v4, sprite, newDirection, tintIndex, rotation, keepDefaultUV, invert));
            if (overlayIndex > 0 && overlayTexture(originalDirection) != null)
                quads.addAll(QuadUtils.createQuadComplex(v1, v2, v3, v4, overlayTexture(originalDirection), originalDirection, modelInformation.tintIndex, 0, doNotMoveOverlay, false));
        }
    }

    private TextureAtlasSprite overlayTexture(Direction direction) {
        return switch (direction) {
            case UP -> modelInformation.upTexture;
            case DOWN -> modelInformation.downTexture;
            case NORTH -> modelInformation.northTexture;
            case EAST -> modelInformation.eastTexture;
            case SOUTH -> modelInformation.southTexture;
            case WEST -> modelInformation.westTexture;
        };
    }
}
//========SOLI DEO GLORIA========//