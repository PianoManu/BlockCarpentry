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

    private static boolean insideBoundaries(Vec3 currVec, Vec3 NWU, Vec3 SWU, Vec3 NWD, Vec3 SWD, Vec3 NEU, Vec3 SEU, Vec3 NED, Vec3 SED) {
        Vec3 interpolYhXl = MathUtils.interpolate(NWU, NEU, (currVec.x + 0.5) / 16, false);
        Vec3 interpolYhXh = MathUtils.interpolate(SWU, SEU, (currVec.x + 0.5) / 16, false);
        Vec3 interpolYhz = MathUtils.interpolate(interpolYhXl, interpolYhXh, (currVec.z + 0.5) / 16, false);
        boolean up = interpolYhz.y >= currVec.y + 0.5;
        Vec3 interpolYlXl = MathUtils.interpolate(NWD, NED, (currVec.x + 0.5) / 16, false);
        Vec3 interpolYlXh = MathUtils.interpolate(SWD, SED, (currVec.x + 0.5) / 16, false);
        Vec3 interpolYlz = MathUtils.interpolate(interpolYlXl, interpolYlXh, (currVec.z + 0.5) / 16, false);
        boolean down = interpolYlz.y < currVec.y + 0.5;

        Vec3 interpolZlYl = MathUtils.interpolate(NWD, NWU, (currVec.y + 0.5) / 16, false);
        Vec3 interpolZlYh = MathUtils.interpolate(NED, NEU, (currVec.y + 0.5) / 16, false);
        Vec3 interpolZlx = MathUtils.interpolate(interpolZlYl, interpolZlYh, (currVec.x + 0.5) / 16, false);
        boolean north = interpolZlx.z < currVec.z + 0.5;
        Vec3 interpolZhYl = MathUtils.interpolate(SWD, SWU, (currVec.y + 0.5) / 16, false);
        Vec3 interpolZhYh = MathUtils.interpolate(SED, SEU, (currVec.y + 0.5) / 16, false);
        Vec3 interpolZhx = MathUtils.interpolate(interpolZhYl, interpolZhYh, (currVec.x + 0.5) / 16, false);
        boolean south = interpolZhx.z >= currVec.z + 0.5;
        Vec3 interpolXlYl = MathUtils.interpolate(NWD, NWU, (currVec.y + 0.5) / 16, false);
        Vec3 interpolXlYh = MathUtils.interpolate(SWD, SWU, (currVec.y + 0.5) / 16, false);
        Vec3 interpolXlz = MathUtils.interpolate(interpolXlYl, interpolXlYh, (currVec.z + 0.5) / 16, false);
        boolean west = interpolXlz.x < currVec.x + 0.5;
        Vec3 interpolXhYl = MathUtils.interpolate(NED, NEU, (currVec.y + 0.5) / 16, false);
        Vec3 interpolXhYh = MathUtils.interpolate(SED, SEU, (currVec.y + 0.5) / 16, false);
        Vec3 interpolXhz = MathUtils.interpolate(interpolXhYl, interpolXhYh, (currVec.z + 0.5) / 16, false);
        boolean east = interpolXhz.x >= currVec.x + 0.5;

        return up && down && north && south && west && east;
    }
}
//========SOLI DEO GLORIA========//