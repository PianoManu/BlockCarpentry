package mod.pianomanu.blockcarpentry.util;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for NBT stuff
 *
 * @author PianoManu
 * @version 1.0 10/23/23
 */
public class BCNBTUtils {
    public static Vec3 readVec(String tagElement) {
        try {
            int firstComma = tagElement.indexOf(',');
            int secondComma = tagElement.indexOf(',', firstComma + 1);
            if (tagElement.charAt(0) != '(' || firstComma == -1 || secondComma == -1)
                return Vec3.ZERO;
            double x = Double.parseDouble(tagElement.substring(1, firstComma));
            double y = Double.parseDouble(tagElement.substring(firstComma + 1, secondComma));
            double z = Double.parseDouble(tagElement.substring(secondComma + 1, tagElement.length() - 2));
            return new Vec3(x, y, z);

        } catch (Exception e) {
            return Vec3.ZERO;
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

    public static CompoundTag writeDirectionList(CompoundTag tag, List<?> list) {
        List<Integer> directionsToInt = new ArrayList<>();
        for (Object o : list) {
            if (o instanceof Direction d) {
                directionsToInt.add(d.ordinal());
            }
        }
        tag.putIntArray("directions", directionsToInt);
        return tag;
    }
}
//========SOLI DEO GLORIA========//