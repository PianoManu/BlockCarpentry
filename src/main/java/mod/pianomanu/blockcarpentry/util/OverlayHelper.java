package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.tileentity.TwoBlocksFrameBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

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

    public static boolean setOverlay(Level level, BlockPos pos, Player player, int lowerBound, int upperBound) {
        return setOverlay(level, pos, player, lowerBound, upperBound, 0);
    }

    public static boolean setOverlay(Level level, BlockPos pos, Player player, int lowerBound, int upperBound, int messageIndexOffset) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof FrameBlockTile fte) {
            setOverlayBaseFrameCycle(fte, player, lowerBound, upperBound, messageIndexOffset);
            return true;
        }
        if (tileEntity instanceof TwoBlocksFrameBlockTile fte) {
            boolean condition = fte.getOverlay() < upperBound && fte.getOverlay() >= lowerBound && !fte.applyToUpper() || fte.getOverlay_2() < upperBound && fte.getOverlay_2() >= lowerBound && fte.applyToUpper();
            setOverlayTwoBlocksFrameCycle(condition, fte, player, lowerBound, messageIndexOffset);
            return true;
        }
        return false;
    }

    private static void setOverlayBaseFrameCycle(FrameBlockTile fte, Player player, int lowerBound, int upperBound, int messageIndexOffset) {
        if (fte.getOverlay() < upperBound && fte.getOverlay() >= lowerBound) {
            setOverlayBaseFrame(fte, player, fte.getOverlay() + 1, messageIndexOffset);
        } else {
            setOverlayBaseFrame(fte, player, lowerBound, messageIndexOffset);
        }
    }

    private static void setOverlayTwoBlocksFrameCycle(boolean condition, TwoBlocksFrameBlockTile fte, Player player, int lowerBound, int messageIndexOffset) {
        if (condition) {
            setOverlayTwoBlocksFrame(fte, player, !fte.applyToUpper() ? fte.getOverlay() + 1 : fte.getOverlay_2() + 1, messageIndexOffset);
        } else {
            setOverlayTwoBlocksFrame(fte, player, lowerBound, messageIndexOffset);
        }
    }

    private static void setOverlayBaseFrame(FrameBlockTile fte, Player player, int newOverlay, int messageIndexOffset) {
        fte.setOverlay(newOverlay);
        int messageIndex = messageIndexOffset == 0 ? 0 : newOverlay - messageIndexOffset;
        player.displayClientMessage(new TranslatableComponent(messageStrings[newOverlay], messageIndex), true);
    }

    private static void setOverlayTwoBlocksFrame(TwoBlocksFrameBlockTile fte, Player player, int newOverlay, int messageIndexOffset) {
        if (!fte.applyToUpper()) {
            fte.setOverlay(newOverlay);
        } else {
            fte.setOverlay_2(newOverlay);
        }
        int messageIndex = messageIndexOffset == 0 ? 0 : newOverlay - messageIndexOffset;
        player.displayClientMessage(new TranslatableComponent(messageStrings[newOverlay], messageIndex), true);
    }
}
//========SOLI DEO GLORIA========//