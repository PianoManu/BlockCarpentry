package mod.pianomanu.blockcarpentry.util;

import net.minecraft.world.phys.Vec3;

/**
 * Contains some math methods like linear interpolation and finding min/max
 * values in an array.
 *
 * @author PianoManu
 * @version 1.0 11/03/23
 */
public class MathUtils {
    public static Vec3 interpolate(Vec3 v1, Vec3 v2, double factor) {
        return interpolate(v1, v2, factor, true);
    }

    public static Vec3 interpolate(Vec3 v1, Vec3 v2, double factor, boolean round) {
        double x = Math.abs(v1.x + factor * (v2.x - v1.x));
        double y = Math.abs(v1.y + factor * (v2.y - v1.y));
        double z = Math.abs(v1.z + factor * (v2.z - v1.z));

        return round ? new Vec3(Math.round(x), Math.round(y), Math.round(z)) : new Vec3(x, y, z);
    }

    public static double max(double... ds) {
        double max = ds[0];
        for (double d : ds) {
            if (d > max)
                max = d;
        }
        return max;
    }

    public static double min(double... ds) {
        double min = ds[0];
        for (double d : ds) {
            if (d < min)
                min = d;
        }
        return min;
    }

    public static int max(int... is) {
        int max = is[0];
        for (int i : is) {
            if (i > max)
                max = i;
        }
        return max;
    }

    public static int min(int... is) {
        int min = is[0];
        for (int i : is) {
            if (i < min)
                min = i;
        }
        return min;
    }
}
//========SOLI DEO GLORIA========//