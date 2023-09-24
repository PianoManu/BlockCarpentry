package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Util class for certain frame block entity things like friction, explosion
 * resistance, etc.
 *
 * @author PianoManu
 * @version 1.0 09/24/23
 */
public class BlockModificationHelper {
    private static final float FRICTION_MAX_BOUNDARY = BCModConfig.FRICTION_MAX_BOUNDARY.get().floatValue();
    private static final float FRICTION_MIN_BOUNDARY = BCModConfig.FRICTION_MIN_BOUNDARY.get().floatValue();
    private static final float FRICTION_MODIFIER = BCModConfig.FRICTION_MODIFIER.get().floatValue();
    private static final float EXPLOSION_RESISTANCE_MAX = BCModConfig.EXPLOSION_RESISTANCE_MAX.get().floatValue();
    private static final float EXPLOSION_RESISTANCE_MODIFIER = BCModConfig.EXPLOSION_RESISTANCE_MODIFIER.get().floatValue();

    public static boolean setAll(ItemStack itemStack, FrameBlockTile frameBlockTile, Player player) {
        return setFriction(itemStack, frameBlockTile, player)
                || setExplosionResistance(itemStack, frameBlockTile, player)
                || setSustainability(itemStack, frameBlockTile, player)
                || setEnchantingPower(itemStack, frameBlockTile, player);
    }

    public static boolean setFriction(ItemStack itemStack, FrameBlockTile frameBlockTile, Player player) {
        if (itemStack.getItem() == Items.BLUE_ICE) {
            return setFriction(itemStack, frameBlockTile, player, false);
        }
        if (itemStack.getItem() == Items.HONEY_BLOCK) {
            return setFriction(itemStack, frameBlockTile, player, true);
        }
        return false;
    }

    private static boolean setFriction(ItemStack itemStack, FrameBlockTile fte, Player player, boolean increaseFriction) {
        if (fte.getFriction() > FRICTION_MIN_BOUNDARY && fte.getFriction() < FRICTION_MAX_BOUNDARY)
            itemStack.setCount(itemStack.getCount() - 1);
        fte.setFriction(newFrictionValue(fte.getFriction(), increaseFriction));
        player.displayClientMessage(Component.translatable("message.blockcarpentry.friction", (Math.round(fte.getFriction() * 1000) / 1000f)), true);
        return true;
    }

    private static float newFrictionValue(float currValue, boolean increaseFriction) {
        if (increaseFriction) {
            return Math.min(currValue * FRICTION_MODIFIER, FRICTION_MAX_BOUNDARY);
        }
        return Math.max(currValue / FRICTION_MODIFIER, FRICTION_MIN_BOUNDARY);
    }

    public static boolean setExplosionResistance(ItemStack itemStack, FrameBlockTile fte, Player player) {
        if (itemStack.getItem() == Items.FLINT) {
            if (fte.getExplosionResistance() < EXPLOSION_RESISTANCE_MAX)
                itemStack.setCount(itemStack.getCount() - 1);
            float min = Math.min(fte.getExplosionResistance() * EXPLOSION_RESISTANCE_MODIFIER, EXPLOSION_RESISTANCE_MAX);
            fte.setExplosionResistance(min);
            player.displayClientMessage(Component.translatable("message.blockcarpentry.strength", (Math.round(fte.getExplosionResistance() * 1000) / 1000f)), true);
            return true;
        }
        if (itemStack.getItem() == Registration.EXPLOSION_RESISTANCE_BALL.get()) {
            if (fte.getExplosionResistance() < EXPLOSION_RESISTANCE_MAX)
                itemStack.setCount(itemStack.getCount() - 1);
            fte.setExplosionResistance(EXPLOSION_RESISTANCE_MAX);
            player.displayClientMessage(Component.translatable("message.blockcarpentry.strength_max", (Math.round(fte.getExplosionResistance() * 1000) / 1000f)), true);
            return true;
        }
        return false;
    }

    public static boolean setSustainability(ItemStack itemStack, FrameBlockTile fte, Player player) {
        if (itemStack.getItem() == Items.BONE_MEAL) {
            if (!fte.getCanSustainPlant())
                itemStack.setCount(itemStack.getCount() - 1);
            fte.setCanSustainPlant(true);
            player.displayClientMessage(Component.translatable("message.blockcarpentry.sustainability"), true);
            return true;
        }
        return false;
    }

    public static boolean setEnchantingPower(ItemStack itemStack, FrameBlockTile fte, Player player) {
        if (itemStack.getItem() == Items.EXPERIENCE_BOTTLE) {
            if (fte.getEnchantPowerBonus() != 1)
                itemStack.setCount(itemStack.getCount() - 1);
            fte.setEnchantPowerBonus(1);
            player.displayClientMessage(Component.translatable("message.blockcarpentry.enchanting_power"), true);
            return true;
        }
        return false;
    }
}
//========SOLI DEO GLORIA========//