package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Some utility functions for messing around with the 8 corners of a block
 *
 * @author PianoManu
 * @version 1.2 11/08/23
 */
public class CornerUtils {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void changeBoxSize(BlockState state, Level level, BlockPos pos, Player player, Vec3 location, Direction clicked_face, boolean shrink) {
        Vec3 corner = getNearestCorner(location);

        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof FrameBlockTile fte) {
            Vec3[] cornerPlusAdjacents = getCorrespondingCornerList(fte, corner, getVec(pos));
            Vec3 old_corner = cornerPlusAdjacents[0];
            if (checkCanChange(cornerPlusAdjacents, clicked_face, shrink)) {
                setCorrespondingCorner(fte, corner, getVec(pos), modify(old_corner, clicked_face, shrink));
                fte.updateVecList();
                writeNBT(fte);
                fte.requestModelDataUpdate();
                level.setBlockEntity(fte);
            }
        }
    }

    private static Vec3 getNearestCorner(Vec3 vec) {
        return new Vec3(Math.round(vec.x), Math.round(vec.y), Math.round(vec.z));
    }

    private static Vec3[] getCorrespondingCornerList(FrameBlockTile tile, Vec3 corner, Vec3 blockpos) {
        if (corner.x == blockpos.x && corner.y == blockpos.y && corner.z == blockpos.z)
            return tile.corners.get(0); //NWD
        if (corner.x == blockpos.x && corner.y == blockpos.y + 1 && corner.z == blockpos.z)
            return tile.corners.get(1);//NWU
        if (corner.x == blockpos.x + 1 && corner.y == blockpos.y && corner.z == blockpos.z)
            return tile.corners.get(2);//NED
        if (corner.x == blockpos.x + 1 && corner.y == blockpos.y + 1 && corner.z == blockpos.z)
            return tile.corners.get(3);//NEU
        if (corner.x == blockpos.x && corner.y == blockpos.y && corner.z == blockpos.z + 1)
            return tile.corners.get(4);//SWD
        if (corner.x == blockpos.x && corner.y == blockpos.y + 1 && corner.z == blockpos.z + 1)
            return tile.corners.get(5);//SWU
        if (corner.x == blockpos.x + 1 && corner.y == blockpos.y && corner.z == blockpos.z + 1)
            return tile.corners.get(6);//SED
        if (corner.x == blockpos.x + 1 && corner.y == blockpos.y + 1 && corner.z == blockpos.z + 1)
            return tile.corners.get(7);//SEU
        return new Vec3[]{getNearestCorner(corner)};
    }

    private static void setCorrespondingCorner(FrameBlockTile tile, Vec3 corner, Vec3 blockpos, Vec3 new_corner) {
        if (corner.x == blockpos.x && corner.y == blockpos.y && corner.z == blockpos.z)
            tile.setNWD(new_corner);
        if (corner.x == blockpos.x && corner.y == blockpos.y + 1 && corner.z == blockpos.z)
            tile.setNWU(new_corner);
        if (corner.x == blockpos.x + 1 && corner.y == blockpos.y && corner.z == blockpos.z)
            tile.setNED(new_corner);
        if (corner.x == blockpos.x + 1 && corner.y == blockpos.y + 1 && corner.z == blockpos.z)
            tile.setNEU(new_corner);
        if (corner.x == blockpos.x && corner.y == blockpos.y && corner.z == blockpos.z + 1)
            tile.setSWD(new_corner);
        if (corner.x == blockpos.x && corner.y == blockpos.y + 1 && corner.z == blockpos.z + 1)
            tile.setSWU(new_corner);
        if (corner.x == blockpos.x + 1 && corner.y == blockpos.y && corner.z == blockpos.z + 1)
            tile.setSED(new_corner);
        if (corner.x == blockpos.x + 1 && corner.y == blockpos.y + 1 && corner.z == blockpos.z + 1)
            tile.setSEU(new_corner);
    }

    private static Vec3 getVec(BlockPos pos) {
        return new Vec3((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
    }

    private static Vec3 modify(Vec3 vec, Direction direction, boolean shrink) {
        double change = shrink ? -1 : 1;
        return switch (direction) {
            case DOWN -> new Vec3(vec.x, vec.y - change, vec.z);
            case UP -> new Vec3(vec.x, vec.y + change, vec.z);
            case NORTH -> new Vec3(vec.x, vec.y, vec.z - change);
            case WEST -> new Vec3(vec.x - change, vec.y, vec.z);
            case SOUTH -> new Vec3(vec.x, vec.y, vec.z + change);
            case EAST -> new Vec3(vec.x + change, vec.y, vec.z);
        };
    }

    private static boolean checkCanChange(Vec3[] cornerVec, Direction direction, boolean shrink) {
        int min = shrink ? 0 : 1;
        int max = shrink ? 15 : 16;
        try {
            Vec3 opposing = getOpposingCorner(cornerVec, direction);
            return checkVectorOverlap(cornerVec[0], opposing, min, max, direction);
        } catch (Exception e) {
            LOGGER.error("Unable to check changeability in FrameBlockTile with corners " + Arrays.toString(cornerVec));
            ExceptionHandler.handleException(e);
            return false;
        }
    }

    private static boolean checkVectorOverlap(Vec3 vec1, Vec3 vec2, int min, int max, Direction direction) {
        double v1, v2;
        switch (direction) {
            case EAST, WEST -> {
                v1 = vec1.x;
                v2 = vec2.x;
            }
            case UP, DOWN -> {
                v1 = vec1.y;
                v2 = vec2.y;
            }
            case NORTH, SOUTH -> {
                v1 = vec1.z;
                v2 = vec2.z;
            }
            default -> {
                v1 = 0;
                v2 = 0;
            }
        }
        double dif = Math.abs(v1) + Math.abs(v2);
        return dif >= min && dif <= max;
    }

    private static Vec3 getOpposingCorner(Vec3[] corners, Direction direction) {
        return switch (direction) {
            case EAST, WEST -> corners[1];
            case UP, DOWN -> corners[2];
            case NORTH, SOUTH -> corners[3];
        };
    }

    private static void writeNBT(FrameBlockTile fte) {
        CompoundTag tag = fte.getUpdateTag();
        for (int i = 0; i < fte.corners.size(); i++) {
            fte.write(tag, FrameBlockTile.TAG_PACKETS.get(i).TAG_ELEMENT, fte.corners.get(i)[0]);
        }
        fte.getUpdateTag(tag, FrameBlockTile.class);
    }

    public static Vec3[] rotateVec3s(Vec3 NWU, Vec3 NEU, Vec3 SWU, Vec3 SEU, Vec3 NWD, Vec3 NED, Vec3 SWD, Vec3 SED, Direction direction) {
        // EAST/WEST: x
        // UP/DOWN: y
        // NORTH/SOUTH: z
        return switch (direction) {
            case UP -> new Vec3[]{NEU, SEU, NWU, SWU, NED, SED, NWD, SWD};
            case DOWN -> new Vec3[]{SWU, NWU, SEU, NEU, SWD, NWD, SED, NED};
            case EAST -> new Vec3[]{SWU, SEU, SWD, SED, NWU, NEU, NWD, NED};
            case WEST -> new Vec3[]{NWD, NED, NWU, NEU, SWD, SED, SWU, SEU};
            case SOUTH -> new Vec3[]{NWD, NWU, SWD, SWU, NED, NEU, SED, SEU};
            case NORTH -> new Vec3[]{NEU, NED, SEU, SED, NWU, NWD, SWU, SWD};
        };
    }

    public static Direction[] getRotatedDirection(Direction up, Direction down, Direction north, Direction east, Direction south, Direction west, Direction direction) {
        //original: {UP, DOWN, NORTH, EAST, SOUTH, WEST}
        return switch (direction) {
            case UP -> new Direction[]{up, down, east, south, west, north};
            case DOWN -> new Direction[]{up, down, west, north, east, south};
            case NORTH -> new Direction[]{west, east, north, up, south, down};
            case EAST -> new Direction[]{south, north, up, east, down, west};
            case SOUTH -> new Direction[]{east, west, north, down, south, up};
            case WEST -> new Direction[]{north, south, down, east, up, west};
        };
    }

    public static boolean[] getRotatedVisibility(boolean up, boolean down, boolean north, boolean east, boolean south, boolean west, Direction direction) {
        //original: {UP, DOWN, NORTH, EAST, SOUTH, WEST}
        return switch (direction) {
            case UP -> new boolean[]{up, down, east, south, west, north};
            case DOWN -> new boolean[]{up, down, west, north, east, south};
            case NORTH -> new boolean[]{west, east, north, up, south, down};
            case EAST -> new boolean[]{south, north, up, east, down, west};
            case SOUTH -> new boolean[]{east, west, north, down, south, up};
            case WEST -> new boolean[]{north, south, down, east, up, west};
        };
    }

    public static List<Direction> legacyRotation(int rotation) {
        List<Direction> rotations = new ArrayList<>();
        if (rotation < 4) {
            for (int i = 0; i < rotation; i++) {
                rotations.add(Direction.UP);
            }
        } else if (rotation < 8) {
            rotations.add(Direction.WEST);
            for (int i = 4; i < rotation; i++) {
                rotations.add(Direction.NORTH);
            }
        } else {
            rotations.add(Direction.NORTH);
            rotations.add(Direction.NORTH);
            rotations.add(Direction.WEST);
            for (int i = 8; i < rotation; i++) {
                rotations.add(Direction.SOUTH);
            }
        }
        return rotations;
    }

    public static boolean isCuboid(Vec3 NWU, Vec3 NEU, Vec3 SWU, Vec3 SEU, Vec3 NWD, Vec3 NED, Vec3 SWD, Vec3 SED) {
        boolean xlEqual = NWU.x == SWU.x && SWU.x == NWD.x && NWD.x == SWD.x;
        boolean xhEqual = NEU.x == SEU.x && SEU.x == NED.x && NED.x == SED.x;
        boolean ylEqual = NWD.y == NED.y && NED.y == SWD.y && SWD.y == SED.y;
        boolean yhEqual = NWU.y == NEU.y && NEU.y == SWU.y && SWU.y == SEU.y;
        boolean zlEqual = NWU.z == NEU.z && NEU.z == NWD.z && NWD.z == NED.z;
        boolean zhEqual = SWU.z == SEU.z && SEU.z == SWD.z && SWD.z == SED.z;
        return xlEqual && xhEqual && ylEqual && yhEqual && zlEqual && zhEqual;
    }
}
