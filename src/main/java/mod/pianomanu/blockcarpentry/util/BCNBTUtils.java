package mod.pianomanu.blockcarpentry.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for NBT stuff
 *
 * @author PianoManu
 * @version 1.1 10/30/23
 */
public class BCNBTUtils {
    public static Vector3d readVec(String tagElement) {
        try {
            int firstComma = tagElement.indexOf(',');
            int secondComma = tagElement.indexOf(',', firstComma + 1);
            if (tagElement.charAt(0) != '(' || firstComma == -1 || secondComma == -1)
                return Vector3d.ZERO;
            double x = Double.parseDouble(tagElement.substring(1, firstComma));
            double y = Double.parseDouble(tagElement.substring(firstComma + 1, secondComma));
            double z = Double.parseDouble(tagElement.substring(secondComma + 1, tagElement.length() - 2));
            return new Vector3d(x, y, z);

        } catch (Exception e) {
            return Vector3d.ZERO;
        }
    }

    public static List<?> readDirectionList(int[] tagList) {
        List<Direction> directions = new ArrayList<>();
        for (int i :
                tagList) {
            directions.add(Direction.values()[i]);
        }
        return directions;
    }

    public static List<?> readRotationsList(int[] tagList) {
        List<Integer> rotations = new ArrayList<>();
        for (int i :
                tagList) {
            rotations.add(i);
        }
        return rotations;
    }

    public static CompoundNBT writeDirectionList(CompoundNBT tag, List<?> list) {
        List<Integer> directionsToInt = new ArrayList<>();
        for (Object o : list) {
            if (o instanceof Direction) {
                Direction d = (Direction) o;
                directionsToInt.add(d.ordinal());
            }
        }
        tag.putIntArray("directions", directionsToInt);
        return tag;
    }

    public static CompoundNBT writeRotationsList(CompoundNBT tag, List<?> list) {
        List<Integer> rotations = new ArrayList<>();
        for (Object o : list) {
            if (o instanceof Integer) {
                Integer i = (Integer) o;
                rotations.add(i);
            }
        }
        tag.putIntArray("rotations", rotations);
        return tag;
    }
}
//========SOLI DEO GLORIA========//