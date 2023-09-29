package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
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
        return setAll(itemStack, blockEntity, player, true, true);
    }

    public static <V extends IFrameTile> boolean setAll(ItemStack itemStack, V blockEntity, Player player, boolean applyFriction, boolean applySustainability) {
        return setAll(itemStack, blockEntity, player, applyFriction, applySustainability, true);
    }


    public static <V extends IFrameTile> boolean setAll(ItemStack itemStack, V blockEntity, Player player, boolean applyFriction, boolean applySustainability, boolean applyEnchantingPower) {
        if (IFrameTile.class.isAssignableFrom(blockEntity.getClass())) {
            boolean set = false;
            if (applyFriction)
                set = setFriction(itemStack, blockEntity, player);
            set |= setExplosionResistance(itemStack, blockEntity, player);
            if (applySustainability)
                set |= setSustainability(itemStack, blockEntity, player);
            if (applyEnchantingPower)
                set |= setEnchantingPower(itemStack, blockEntity, player);
            set |= setCanEntityDestroy(itemStack, blockEntity, player);
            return set;
        }
        return false;
    }

    public static <V extends IFrameTile> boolean setFriction(ItemStack itemStack, V frameBlockTile, Player player) {
        if (BCModConfig.FRICTION_ENABLED.get()) {
            if (FrameInteractionItems.isFrictionModifierNegative(itemStack.getItem())) {
                return setFriction(itemStack, frameBlockTile, player, false);
            }
            if (FrameInteractionItems.isFrictionModifierPositive(itemStack.getItem())) {
                return setFriction(itemStack, frameBlockTile, player, true);
            }
        }
        return false;
    }

    private static <V extends IFrameTile> boolean setFriction(ItemStack itemStack, V fte, Player player, boolean increaseFriction) {
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

    public static <V extends IFrameTile> boolean setExplosionResistance(ItemStack itemStack, V fte, Player player) {
        if (BCModConfig.EXPLOSION_RESISTANCE_ENABLED.get()) {
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
        }
        return false;
    }

    public static <V extends IFrameTile> boolean setSustainability(ItemStack itemStack, V fte, Player player) {
        if (BCModConfig.SUSTAINABILITY_ENABLED.get()) {
            if (FrameInteractionItems.isSustainabilityModifier(itemStack.getItem())) {
                if (!fte.getCanSustainPlant())
                    itemStack.setCount(itemStack.getCount() - 1);
                fte.setCanSustainPlant(true);
                player.displayClientMessage(Component.translatable("message.blockcarpentry.sustainability"), true);
                return true;
            }
        }
        return false;
    }

    public static <V extends IFrameTile> boolean setEnchantingPower(ItemStack itemStack, V fte, Player player) {
        if (BCModConfig.ENCHANT_POWER_ENABLED.get()) {
            if (FrameInteractionItems.isEnchantingPowerModifier(itemStack.getItem())) {
                if (fte.getEnchantPowerBonus() != 1)
                    itemStack.setCount(itemStack.getCount() - 1);
                fte.setEnchantPowerBonus(1);
                player.displayClientMessage(Component.translatable("message.blockcarpentry.enchanting_power"), true);
                return true;
            }
        }
        return false;
    }

    public static <V extends IFrameTile> boolean setCanEntityDestroy(ItemStack itemStack, V fte, Player player) {
        if (BCModConfig.CAN_ENTITY_DESTROY_ENABLED.get()) {
            if (FrameInteractionItems.isEntityDestroyModifier(itemStack.getItem())) {
                if (fte.getCanEntityDestroy()) {
                    itemStack.setCount(itemStack.getCount() - 1);
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.can_entity_destroy"), true);
                } else
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.can_entity_destroy_already"), true);
                fte.setCanEntityDestroy(false);

                return true;
            }
        }
        return false;
    }
}
//========SOLI DEO GLORIA========//