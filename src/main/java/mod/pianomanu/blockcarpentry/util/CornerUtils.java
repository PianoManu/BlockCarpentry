package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Some utility functions for messing around with the 8 corners of a block
 *
 * @author PianoManu
 * @version 1.1 10/30/23
 */
public class CornerUtils {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void changeBoxSize(BlockState state, World level, BlockPos pos, PlayerEntity player, Vector3d location, Direction clicked_face, boolean shrink) {
        Vector3d corner = getNearestCorner(location);

        TileEntity entity = level.getTileEntity(pos);
        if (entity instanceof FrameBlockTile) {
            FrameBlockTile fte = (FrameBlockTile) entity;
            Vector3d[] cornerPlusAdjacents = getCorrespondingCornerList(fte, corner, getVec(pos));
            Vector3d old_corner = cornerPlusAdjacents[0];
            if (checkCanChange(cornerPlusAdjacents, clicked_face, shrink)) {
                setCorrespondingCorner(fte, corner, getVec(pos), modify(old_corner, clicked_face, shrink));
                fte.updateVecList();
                writeNBT(fte);
                fte.requestModelDataUpdate();
                level.setTileEntity(fte.getPos(), fte);
            }
        }
    }

    private static Vector3d getNearestCorner(Vector3d vec) {
        return new Vector3d(Math.round(vec.x), Math.round(vec.y), Math.round(vec.z));
    }

    private static Vector3d[] getCorrespondingCornerList(FrameBlockTile tile, Vector3d corner, Vector3d blockpos) {
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
        return new Vector3d[]{getNearestCorner(corner)};
    }

    private static void setCorrespondingCorner(FrameBlockTile tile, Vector3d corner, Vector3d blockpos, Vector3d new_corner) {
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

    private static Vector3d getVec(BlockPos pos) {
        return new Vector3d((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
    }

    private static Vector3d modify(Vector3d vec, Direction direction, boolean shrink) {
        double change = shrink ? -1 : 1;
        switch (direction) {
            case DOWN:
                return new Vector3d(vec.x, vec.y - change, vec.z);
            case UP:
                return new Vector3d(vec.x, vec.y + change, vec.z);
            case NORTH:
                return new Vector3d(vec.x, vec.y, vec.z - change);
            case WEST:
                return new Vector3d(vec.x - change, vec.y, vec.z);
            case SOUTH:
                return new Vector3d(vec.x, vec.y, vec.z + change);
            case EAST:
                return new Vector3d(vec.x + change, vec.y, vec.z);
            default:
                throw new IllegalArgumentException();
        }
    }

    private static boolean checkCanChange(Vector3d[] cornerVec, Direction direction, boolean shrink) {
        int min = shrink ? 0 : 1;
        int max = shrink ? 15 : 16;
        try {
            Vector3d opposing = getOpposingCorner(cornerVec, direction);
            return checkVectorOverlap(cornerVec[0], opposing, min, max, direction);
        } catch (Exception e) {
            LOGGER.error("Unable to check changeability in FrameBlockTile with corners " + Arrays.toString(cornerVec));
            e.printStackTrace();
            return false;
        }
    }

    private static boolean checkVectorOverlap(Vector3d vec1, Vector3d vec2, int min, int max, Direction direction) {
        double v1, v2;
        switch (direction) {
            case EAST:
            case WEST:
                v1 = vec1.x;
                v2 = vec2.x;
                break;
            case UP:
            case DOWN:
                v1 = vec1.y;
                v2 = vec2.y;
                break;
            case NORTH:
            case SOUTH:
                v1 = vec1.z;
                v2 = vec2.z;
                break;
            default:
                v1 = 0;
                v2 = 0;
        }
        double dif = Math.abs(v1) + Math.abs(v2);
        return dif >= min && dif <= max;
    }

    private static Vector3d getOpposingCorner(Vector3d[] corners, Direction direction) {
        switch (direction) {
            case EAST:
            case WEST:
                return corners[1];
            case UP:
            case DOWN:
                return corners[2];
            case NORTH:
            case SOUTH:
                return corners[3];
            default:
                throw new IllegalArgumentException();
        }
    }

    private static void writeNBT(FrameBlockTile fte) {
        CompoundNBT tag = fte.getUpdateTag();
        for (int i = 0; i < fte.corners.size(); i++) {
            fte.write(tag, FrameBlockTile.TAG_PACKETS.get(i).TAG_ELEMENT, fte.corners.get(i)[0]);
        }
        fte.getUpdateTag(tag, FrameBlockTile.class);
    }

    public static Vector3d[] rotateVector3ds(Vector3d NWU, Vector3d NEU, Vector3d SWU, Vector3d SEU, Vector3d NWD, Vector3d NED, Vector3d SWD, Vector3d SED, Direction direction) {
        // EAST/WEST: x
        // UP/DOWN: y
        // NORTH/SOUTH: z
        switch (direction) {
            case UP:
                return new Vector3d[]{NEU, SEU, NWU, SWU, NED, SED, NWD, SWD};
            case DOWN:
                return new Vector3d[]{SWU, NWU, SEU, NEU, SWD, NWD, SED, NED};
            case EAST:
                return new Vector3d[]{SWU, SEU, SWD, SED, NWU, NEU, NWD, NED};
            case WEST:
                return new Vector3d[]{NWD, NED, NWU, NEU, SWD, SED, SWU, SEU};
            case SOUTH:
                return new Vector3d[]{NWD, NWU, SWD, SWU, NED, NEU, SED, SEU};
            case NORTH:
                return new Vector3d[]{NEU, NED, SEU, SED, NWU, NWD, SWU, SWD};
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Direction[] getRotatedDirection(Direction up, Direction down, Direction north, Direction east, Direction south, Direction west, Direction direction) {
        //original: {UP, DOWN, NORTH, EAST, SOUTH, WEST}
        switch (direction) {
            case UP:
                return new Direction[]{up, down, east, south, west, north};
            case DOWN:
                return new Direction[]{up, down, west, north, east, south};
            case NORTH:
                return new Direction[]{west, east, north, up, south, down};
            case EAST:
                return new Direction[]{south, north, up, east, down, west};
            case SOUTH:
                return new Direction[]{east, west, north, down, south, up};
            case WEST:
                return new Direction[]{north, south, down, east, up, west};
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean[] getRotatedVisibility(boolean up, boolean down, boolean north, boolean east, boolean south, boolean west, Direction direction) {
        //original: {UP, DOWN, NORTH, EAST, SOUTH, WEST}
        switch (direction) {
            case UP:
                return new boolean[]{up, down, east, south, west, north};
            case DOWN:
                return new boolean[]{up, down, west, north, east, south};
            case NORTH:
                return new boolean[]{west, east, north, up, south, down};
            case EAST:
                return new boolean[]{south, north, up, east, down, west};
            case SOUTH:
                return new boolean[]{east, west, north, down, south, up};
            case WEST:
                return new boolean[]{north, south, down, east, up, west};
            default:
                throw new IllegalArgumentException();
        }
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
}
