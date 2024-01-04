package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.tileentity.IFrameTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Objects;

/**
 * Some methods for compatibility with other mods
 *
 * @author PianoManu
 * @version 1.7 01/04/24
 */
public class InterModCompat {
    public static float apotheosisEterna(BlockState state, LevelReader level, BlockPos pos) {
        Player p = net.minecraft.client.Minecraft.getInstance().player;
        HitResult r = p.pick(20.0D, 0.0F, false);
        if (r instanceof BlockHitResult bhr) {

            IFrameTile t = getTile(level, bhr.getBlockPos());
            if (t != null) {
                return t.getEnchantPowerBonus();
            }
        }
        return 0f;
    }

    private static <V extends IFrameTile> V getTile(BlockGetter level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        try {
            if (IFrameTile.class.isAssignableFrom(Objects.requireNonNull(be).getClass())) {
                return (V) be;
            }
        } catch (NullPointerException e) {
            ExceptionHandler.handleException(e);
        }
        return null;
    }
}

//========SOLI DEO GLORIA========//