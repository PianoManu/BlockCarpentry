package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Utility class for Bounding-Box related functions and calculations
 *
 * @author PianoManu
 * @version 1.1 11/03/23
 */
public class VoxelUtils {
    public static VoxelShape getShape(FrameBlockTile fte) {
        if (fte.shapeUnmodified())
            return Shapes.block();
        Vec3 NWU = new Vec3(fte.NWU.x, 16 + fte.NWU.y, fte.NWU.z);
        Vec3 NEU = new Vec3(16 + fte.NEU.x, 16 + fte.NEU.y, fte.NEU.z);
        Vec3 NWD = new Vec3(fte.NWD.x, fte.NWD.y, fte.NWD.z);
        Vec3 NED = new Vec3(16 + fte.NED.x, fte.NED.y, fte.NED.z);
        Vec3 SWU = new Vec3(fte.SWU.x, 16 + fte.SWU.y, 16 + fte.SWU.z);
        Vec3 SEU = new Vec3(16 + fte.SEU.x, 16 + fte.SEU.y, 16 + fte.SEU.z);
        Vec3 SWD = new Vec3(fte.SWD.x, fte.SWD.y, 16 + fte.SWD.z);
        Vec3 SED = new Vec3(16 + fte.SED.x, fte.SED.y, 16 + fte.SED.z);

        return getFreeShape(NWU, SWU, NWD, SWD, NEU, SEU, NED, SED);
    }

    private static VoxelShape getFreeShape(Vec3 NWU, Vec3 SWU, Vec3 NWD, Vec3 SWD, Vec3 NEU, Vec3 SEU, Vec3 NED, Vec3 SED) {
        int maxX = (int) max(NWU.x, SWU.x, NWD.x, SWD.x, NEU.x, SEU.x, NED.x, SED.x);
        int minX = (int) min(NWU.x, SWU.x, NWD.x, SWD.x, NEU.x, SEU.x, NED.x, SED.x);
        int maxY = (int) max(NWU.y, SWU.y, NWD.y, SWD.y, NEU.y, SEU.y, NED.y, SED.y);
        int minY = (int) min(NWU.y, SWU.y, NWD.y, SWD.y, NEU.y, SEU.y, NED.y, SED.y);
        int maxZ = (int) max(NWU.z, SWU.z, NWD.z, SWD.z, NEU.z, SEU.z, NED.z, SED.z);
        int minZ = (int) min(NWU.z, SWU.z, NWD.z, SWD.z, NEU.z, SEU.z, NED.z, SED.z);
        if (maxX == minX)
            maxX++;
        if (maxY == minY)
            maxY++;
        if (maxZ == minZ)
            maxZ++;
        minX = Math.max(minX, 0);
        minY = Math.max(minY, 0);
        minZ = Math.max(minZ, 0);
        maxX = Math.min(maxX, 16);
        maxY = Math.min(maxY, 16);
        maxZ = Math.min(maxZ, 16);

        VoxelShape combined = Shapes.empty();
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    if (insideBoundaries(new Vec3(x, y, z), NWU, SWU, NWD, SWD, NEU, SEU, NED, SED))
                        combined = Shapes.joinUnoptimized(combined, Block.box(x, y, z, x + 1, y + 1, z + 1), BooleanOp.OR);
                }
            }
        }
        return combined.optimize();
    }

    private static Vec3 interpolate(Vec3 v1, Vec3 v2, double factor) {
        double x = Math.round(Math.abs(v1.x + factor * (v2.x - v1.x)));
        double y = Math.round(Math.abs(v1.y + factor * (v2.y - v1.y)));
        double z = Math.round(Math.abs(v1.z + factor * (v2.z - v1.z)));

        return new Vec3(x, y, z);
    }

    private static double max(double... ds) {
        double max = ds[0];
        for (double d : ds) {
            if (d > max)
                max = d;
        }
        return max;
    }

    private static double min(double... ds) {
        double min = ds[0];
        for (double d : ds) {
            if (d < min)
                min = d;
        }
        return min;
    }

    private static boolean insideBoundaries(Vec3 currVec, Vec3 NWU, Vec3 SWU, Vec3 NWD, Vec3 SWD, Vec3 NEU, Vec3 SEU, Vec3 NED, Vec3 SED) {
        Vec3 interpolYhXl = interpolate(NWU, NEU, currVec.x / 16);
        Vec3 interpolYhXh = interpolate(SWU, SEU, currVec.x / 16);
        Vec3 interpolYhz = interpolate(interpolYhXl, interpolYhXh, currVec.z / 16);
        boolean up = interpolYhz.y >= currVec.y + 1;
        Vec3 interpolYlXl = interpolate(NWD, NED, currVec.x / 16);
        Vec3 interpolYlXh = interpolate(SWD, SED, currVec.x / 16);
        Vec3 interpolYlz = interpolate(interpolYlXl, interpolYlXh, currVec.z / 16);
        boolean down = interpolYlz.y <= currVec.y + 1;

        Vec3 interpolZlYl = interpolate(NWD, NWU, currVec.y / 16);
        Vec3 interpolZlYh = interpolate(NED, NEU, currVec.y / 16);
        Vec3 interpolZlx = interpolate(interpolZlYl, interpolZlYh, currVec.x / 16);
        boolean north = interpolZlx.z <= currVec.z + 1;
        Vec3 interpolZhYl = interpolate(SWD, SWU, currVec.y / 16);
        Vec3 interpolZhYh = interpolate(SED, SEU, currVec.y / 16);
        Vec3 interpolZhx = interpolate(interpolZhYl, interpolZhYh, currVec.x / 16);
        boolean south = interpolZhx.z >= currVec.z + 1;
        Vec3 interpolXlYl = interpolate(NWD, NWU, currVec.y / 16);
        Vec3 interpolXlYh = interpolate(SWD, SWU, currVec.y / 16);
        Vec3 interpolXlz = interpolate(interpolXlYl, interpolXlYh, currVec.z / 16);
        boolean west = interpolXlz.x <= currVec.x + 1;
        Vec3 interpolXhYl = interpolate(NED, NEU, currVec.y / 16);
        Vec3 interpolXhYh = interpolate(SED, SEU, currVec.y / 16);
        Vec3 interpolXhz = interpolate(interpolXhYl, interpolXhYh, currVec.z / 16);
        boolean east = interpolXhz.x >= currVec.x + 1;

        return up && down && north && south && west && east;
    }
}
//========SOLI DEO GLORIA========//