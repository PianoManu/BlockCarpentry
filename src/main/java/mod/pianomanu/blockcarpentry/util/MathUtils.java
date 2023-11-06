package mod.pianomanu.blockcarpentry.util;

import net.minecraft.util.math.vector.Vector3d;

/**
 * Contains some math methods like linear interpolation and finding min/max
 * values in an array.
 *
 * @author PianoManu
 * @version 1.0 11/03/23
 */
public class MathUtils {
    public static Vector3d interpolate(Vector3d v1, Vector3d v2, double factor) {
        return interpolate(v1, v2, factor, true);
    }

    public static Vector3d interpolate(Vector3d v1, Vector3d v2, double factor, boolean round) {
        double x = Math.abs(v1.x + factor * (v2.x - v1.x));
        double y = Math.abs(v1.y + factor * (v2.y - v1.y));
        double z = Math.abs(v1.z + factor * (v2.z - v1.z));

        return round ? new Vector3d(Math.round(x), Math.round(y), Math.round(z)) : new Vector3d(x, y, z);
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