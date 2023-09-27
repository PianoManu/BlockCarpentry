package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.BedFrameTile;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.tileentity.IFrameTile;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Util class for certain frame block entity things like friction, explosion
 * resistance, etc.
 *
 * @author PianoManu
 * @version 1.1 09/27/23
 */
public class BlockModificationHelper {
    private static final float FRICTION_MAX_BOUNDARY = BCModConfig.FRICTION_MAX_BOUNDARY.get().floatValue();
    private static final float FRICTION_MIN_BOUNDARY = BCModConfig.FRICTION_MIN_BOUNDARY.get().floatValue();
    private static final float FRICTION_MODIFIER = BCModConfig.FRICTION_MODIFIER.get().floatValue();
    private static final float EXPLOSION_RESISTANCE_MAX = BCModConfig.EXPLOSION_RESISTANCE_MAX.get().floatValue();
    private static final float EXPLOSION_RESISTANCE_MODIFIER = BCModConfig.EXPLOSION_RESISTANCE_MODIFIER.get().floatValue();

    public static <V extends IFrameTile> boolean setAll(ItemStack itemStack, V blockEntity, Player player) {
        if (IFrameTile.class.isAssignableFrom(blockEntity.getClass())) {
            return setFriction(itemStack, blockEntity, player)
                    || setExplosionResistance(itemStack, blockEntity, player)
                    || setSustainability(itemStack, blockEntity, player)
                    || setEnchantingPower(itemStack, blockEntity, player);
        }
        return false;
    }

    public static <V extends IFrameTile> boolean setFriction(ItemStack itemStack, V frameBlockTile, Player player) {
        if (FrameInteractionItems.isFrictionModifierNegative(itemStack.getItem())) {
            return setFriction(itemStack, frameBlockTile, player, false);
        }
        if (FrameInteractionItems.isFrictionModifierPositive(itemStack.getItem())) {
            return setFriction(itemStack, frameBlockTile, player, true);
        }
        return false;
    }

    private static <V extends IFrameTile> boolean setFriction(ItemStack itemStack, V blockEntity, Player player, boolean increaseFriction) {
        if (blockEntity instanceof FrameBlockTile fte) {
            if (fte.getFriction() > FRICTION_MIN_BOUNDARY && fte.getFriction() < FRICTION_MAX_BOUNDARY)
                itemStack.setCount(itemStack.getCount() - 1);
            fte.setFriction(newFrictionValue(fte.getFriction(), increaseFriction));
            player.displayClientMessage(Component.translatable("message.blockcarpentry.friction", (Math.round(fte.getFriction() * 1000) / 1000f)), true);
        } else if (blockEntity instanceof BedFrameTile fte) {
            if (fte.getFriction() > FRICTION_MIN_BOUNDARY && fte.getFriction() < FRICTION_MAX_BOUNDARY)
                itemStack.setCount(itemStack.getCount() - 1);
            fte.setFriction(newFrictionValue(fte.getFriction(), increaseFriction));
            player.displayClientMessage(Component.translatable("message.blockcarpentry.friction", (Math.round(fte.getFriction() * 1000) / 1000f)), true);
        }
        return true;
    }

    private static float newFrictionValue(float currValue, boolean increaseFriction) {
        if (increaseFriction) {
            return Math.min(currValue * FRICTION_MODIFIER, FRICTION_MAX_BOUNDARY);
        }
        return Math.max(currValue / FRICTION_MODIFIER, FRICTION_MIN_BOUNDARY);
    }

    public static <V extends IFrameTile> boolean setExplosionResistance(ItemStack itemStack, V fte, Player player) {
        if (FrameInteractionItems.isExplosionResistanceModifierSingle(itemStack.getItem())) {
            if (fte.getExplosionResistance() < EXPLOSION_RESISTANCE_MAX)
                itemStack.setCount(itemStack.getCount() - 1);
            float min = Math.min(fte.getExplosionResistance() * EXPLOSION_RESISTANCE_MODIFIER, EXPLOSION_RESISTANCE_MAX);
            fte.setExplosionResistance(min);
            player.displayClientMessage(Component.translatable("message.blockcarpentry.strength", (Math.round(fte.getExplosionResistance() * 1000) / 1000f)), true);
            return true;
        }
        if (FrameInteractionItems.isExplosionResistanceModifierUltra(itemStack.getItem())) {
            if (fte.getExplosionResistance() < EXPLOSION_RESISTANCE_MAX)
                itemStack.setCount(itemStack.getCount() - 1);
            fte.setExplosionResistance(EXPLOSION_RESISTANCE_MAX);
            player.displayClientMessage(Component.translatable("message.blockcarpentry.strength_max", (Math.round(fte.getExplosionResistance() * 1000) / 1000f)), true);
            return true;
        }
        return false;
    }

    public static <V extends IFrameTile> boolean setSustainability(ItemStack itemStack, V fte, Player player) {
        if (FrameInteractionItems.isSustainabilityModifier(itemStack.getItem())) {
            if (!fte.getCanSustainPlant())
                itemStack.setCount(itemStack.getCount() - 1);
            fte.setCanSustainPlant(true);
            player.displayClientMessage(Component.translatable("message.blockcarpentry.sustainability"), true);
            return true;
        }
        return false;
    }

    public static <V extends IFrameTile> boolean setEnchantingPower(ItemStack itemStack, V fte, Player player) {
        if (FrameInteractionItems.isEnchantingPowerModifier(itemStack.getItem())) {
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