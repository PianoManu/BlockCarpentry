package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.tileentity.TwoBlocksFrameBlockTile;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;

/**
 * Util class for frame block overlays
 *
 * @author PianoManu
 * @version 1.3 09/27/23
 */
public class OverlayHelper {
    private static final String[] messageStrings = {
            "",
            "message.blockcarpentry.grass_overlay",
            "message.blockcarpentry.grass_overlay_large",
            "message.blockcarpentry.snow_overlay",
            "message.blockcarpentry.snow_overlay_small",
            "message.blockcarpentry.vine_overlay",
            "message.blockcarpentry.special_overlay",
            "message.blockcarpentry.special_overlay",
            "message.blockcarpentry.special_overlay",
            "message.blockcarpentry.special_overlay",
            "message.blockcarpentry.special_overlay"
    };

    public static boolean setOverlay(World level, BlockPos pos, PlayerEntity player, int lowerBound, int upperBound) {
        return setOverlay(level, pos, player, lowerBound, upperBound, 0);
    }

    public static boolean setOverlay(World level, BlockPos pos, PlayerEntity player, int lowerBound, int upperBound, int messageIndexOffset) {
        TileEntity tileEntity = level.getTileEntity(pos);
        if (tileEntity instanceof FrameBlockTile) {
            setOverlayBaseFrameCycle((FrameBlockTile) tileEntity, player, lowerBound, upperBound, messageIndexOffset);
            return true;
        }
        if (tileEntity instanceof TwoBlocksFrameBlockTile) {
            TwoBlocksFrameBlockTile fte = (TwoBlocksFrameBlockTile) tileEntity;
            boolean condition = fte.getOverlay() < upperBound && fte.getOverlay() >= lowerBound && !fte.applyToUpper() || fte.getOverlay_2() < upperBound && fte.getOverlay_2() >= lowerBound && fte.applyToUpper();
            setOverlayTwoBlocksFrameCycle(condition, fte, player, lowerBound, messageIndexOffset);
            return true;
        }
        return false;
    }

    private static void setOverlayBaseFrameCycle(FrameBlockTile fte, PlayerEntity player, int lowerBound, int upperBound, int messageIndexOffset) {
        if (fte.getOverlay() < upperBound && fte.getOverlay() >= lowerBound) {
            setOverlayBaseFrame(fte, player, fte.getOverlay() + 1, messageIndexOffset);
        } else {
            setOverlayBaseFrame(fte, player, lowerBound, messageIndexOffset);
        }
    }

    private static void setOverlayTwoBlocksFrameCycle(boolean condition, TwoBlocksFrameBlockTile fte, PlayerEntity player, int lowerBound, int messageIndexOffset) {
        if (condition) {
            setOverlayTwoBlocksFrame(fte, player, !fte.applyToUpper() ? fte.getOverlay() + 1 : fte.getOverlay_2() + 1, messageIndexOffset);
        } else {
            setOverlayTwoBlocksFrame(fte, player, lowerBound, messageIndexOffset);
        }
    }

    private static void setOverlayBaseFrame(FrameBlockTile fte, PlayerEntity player, int newOverlay, int messageIndexOffset) {
        fte.setOverlay(newOverlay);
        int messageIndex = messageIndexOffset == 0 ? 0 : newOverlay - messageIndexOffset;
        player.sendStatusMessage(new TranslationTextComponent(messageStrings[newOverlay], messageIndex), true);
    }

    private static void setOverlayTwoBlocksFrame(TwoBlocksFrameBlockTile fte, PlayerEntity player, int newOverlay, int messageIndexOffset) {
        if (!fte.applyToUpper()) {
            fte.setOverlay(newOverlay);
        } else {
            fte.setOverlay_2(newOverlay);
        }
        int messageIndex = messageIndexOffset == 0 ? 0 : newOverlay - messageIndexOffset;
        player.sendStatusMessage(new TranslationTextComponent(messageStrings[newOverlay], messageIndex), true);
    }
}
//========SOLI DEO GLORIA========//