package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Utility class for Bounding-Box related functions and calculations
 *
 * @author PianoManu
 * @version 1.1 11/03/23
 */
public class VoxelUtils {
    public static VoxelShape getShape(FrameBlockTile fte) {
        if (fte.shapeUnmodified())
            return VoxelShapes.fullCube();
        Vector3d NWU = new Vector3d(fte.NWU.x, 16 + fte.NWU.y, fte.NWU.z);
        Vector3d NEU = new Vector3d(16 + fte.NEU.x, 16 + fte.NEU.y, fte.NEU.z);
        Vector3d NWD = new Vector3d(fte.NWD.x, fte.NWD.y, fte.NWD.z);
        Vector3d NED = new Vector3d(16 + fte.NED.x, fte.NED.y, fte.NED.z);
        Vector3d SWU = new Vector3d(fte.SWU.x, 16 + fte.SWU.y, 16 + fte.SWU.z);
        Vector3d SEU = new Vector3d(16 + fte.SEU.x, 16 + fte.SEU.y, 16 + fte.SEU.z);
        Vector3d SWD = new Vector3d(fte.SWD.x, fte.SWD.y, 16 + fte.SWD.z);
        Vector3d SED = new Vector3d(16 + fte.SED.x, fte.SED.y, 16 + fte.SED.z);

        return getFreeShape(NWU, SWU, NWD, SWD, NEU, SEU, NED, SED);
    }

    private static VoxelShape getFreeShape(Vector3d NWU, Vector3d SWU, Vector3d NWD, Vector3d SWD, Vector3d NEU, Vector3d SEU, Vector3d NED, Vector3d SED) {
        int maxX = (int) MathUtils.max(NWU.x, SWU.x, NWD.x, SWD.x, NEU.x, SEU.x, NED.x, SED.x);
        int minX = (int) MathUtils.min(NWU.x, SWU.x, NWD.x, SWD.x, NEU.x, SEU.x, NED.x, SED.x);
        int maxY = (int) MathUtils.max(NWU.y, SWU.y, NWD.y, SWD.y, NEU.y, SEU.y, NED.y, SED.y);
        int minY = (int) MathUtils.min(NWU.y, SWU.y, NWD.y, SWD.y, NEU.y, SEU.y, NED.y, SED.y);
        int maxZ = (int) MathUtils.max(NWU.z, SWU.z, NWD.z, SWD.z, NEU.z, SEU.z, NED.z, SED.z);
        int minZ = (int) MathUtils.min(NWU.z, SWU.z, NWD.z, SWD.z, NEU.z, SEU.z, NED.z, SED.z);
        if (maxX == minX)
            maxX++;
        if (maxY == minY)
            maxY++;
        if (maxZ == minZ)
            maxZ++;
        minX = MathUtils.max(minX, 0);
        minY = MathUtils.max(minY, 0);
        minZ = MathUtils.max(minZ, 0);
        maxX = MathUtils.min(maxX, 16);
        maxY = MathUtils.min(maxY, 16);
        maxZ = MathUtils.min(maxZ, 16);

        VoxelShape combined = VoxelShapes.empty();
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    if (insideBoundaries(new Vector3d(x, y, z), NWU, SWU, NWD, SWD, NEU, SEU, NED, SED))
                        combined = VoxelShapes.combine(combined, Block.makeCuboidShape(x, y, z, x + 1, y + 1, z + 1), IBooleanFunction.OR);
                }
            }
        }
        return combined.simplify();
    }

    private static boolean insideBoundaries(Vector3d currVec, Vector3d NWU, Vector3d SWU, Vector3d NWD, Vector3d SWD, Vector3d NEU, Vector3d SEU, Vector3d NED, Vector3d SED) {
        Vector3d interpolYhXl = MathUtils.interpolate(NWU, NEU, (currVec.x + 0.5) / 16, false);
        Vector3d interpolYhXh = MathUtils.interpolate(SWU, SEU, (currVec.x + 0.5) / 16, false);
        Vector3d interpolYhz = MathUtils.interpolate(interpolYhXl, interpolYhXh, (currVec.z + 0.5) / 16, false);
        boolean up = interpolYhz.y >= currVec.y + 0.5;
        Vector3d interpolYlXl = MathUtils.interpolate(NWD, NED, (currVec.x + 0.5) / 16, false);
        Vector3d interpolYlXh = MathUtils.interpolate(SWD, SED, (currVec.x + 0.5) / 16, false);
        Vector3d interpolYlz = MathUtils.interpolate(interpolYlXl, interpolYlXh, (currVec.z + 0.5) / 16, false);
        boolean down = interpolYlz.y < currVec.y + 0.5;

        Vector3d interpolZlYl = MathUtils.interpolate(NWD, NWU, (currVec.y + 0.5) / 16, false);
        Vector3d interpolZlYh = MathUtils.interpolate(NED, NEU, (currVec.y + 0.5) / 16, false);
        Vector3d interpolZlx = MathUtils.interpolate(interpolZlYl, interpolZlYh, (currVec.x + 0.5) / 16, false);
        boolean north = interpolZlx.z < currVec.z + 0.5;
        Vector3d interpolZhYl = MathUtils.interpolate(SWD, SWU, (currVec.y + 0.5) / 16, false);
        Vector3d interpolZhYh = MathUtils.interpolate(SED, SEU, (currVec.y + 0.5) / 16, false);
        Vector3d interpolZhx = MathUtils.interpolate(interpolZhYl, interpolZhYh, (currVec.x + 0.5) / 16, false);
        boolean south = interpolZhx.z >= currVec.z + 0.5;
        Vector3d interpolXlYl = MathUtils.interpolate(NWD, NWU, (currVec.y + 0.5) / 16, false);
        Vector3d interpolXlYh = MathUtils.interpolate(SWD, SWU, (currVec.y + 0.5) / 16, false);
        Vector3d interpolXlz = MathUtils.interpolate(interpolXlYl, interpolXlYh, (currVec.z + 0.5) / 16, false);
        boolean west = interpolXlz.x < currVec.x + 0.5;
        Vector3d interpolXhYl = MathUtils.interpolate(NED, NEU, (currVec.y + 0.5) / 16, false);
        Vector3d interpolXhYh = MathUtils.interpolate(SED, SEU, (currVec.y + 0.5) / 16, false);
        Vector3d interpolXhz = MathUtils.interpolate(interpolXhYl, interpolXhYh, (currVec.z + 0.5) / 16, false);
        boolean east = interpolXhz.x >= currVec.x + 0.5;

        return up && down && north && south && west && east;
    }
}
//========SOLI DEO GLORIA========//