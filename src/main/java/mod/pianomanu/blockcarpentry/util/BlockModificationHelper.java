package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.IFrameTile;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

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

    public static <V extends IFrameTile> boolean setAll(ItemStack itemStack, V TileEntity, PlayerEntity player) {
        return setAll(itemStack, TileEntity, player, true, true);
    }

    public static <V extends IFrameTile> boolean setAll(ItemStack itemStack, V TileEntity, PlayerEntity player, boolean applyFriction, boolean applySustainability) {
        return setAll(itemStack, TileEntity, player, applyFriction, applySustainability, true);
    }


    public static <V extends IFrameTile> boolean setAll(ItemStack itemStack, V TileEntity, PlayerEntity player, boolean applyFriction, boolean applySustainability, boolean applyEnchantingPower) {
        if (IFrameTile.class.isAssignableFrom(TileEntity.getClass())) {
            boolean set = false;
            if (applyFriction)
                set = setFriction(itemStack, TileEntity, player);
            set |= setExplosionResistance(itemStack, TileEntity, player);
            if (applySustainability)
                set |= setSustainability(itemStack, TileEntity, player);
            if (applyEnchantingPower)
                set |= setEnchantingPower(itemStack, TileEntity, player);
            set |= setCanEntityDestroy(itemStack, TileEntity, player);
            return set;
        }
        return false;
    }

    public static <V extends IFrameTile> boolean setFriction(ItemStack itemStack, V frameBlockTile, PlayerEntity player) {
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

    private static <V extends IFrameTile> boolean setFriction(ItemStack itemStack, V fte, PlayerEntity player, boolean increaseFriction) {
        if (fte.getSlipperiness() > FRICTION_MIN_BOUNDARY && fte.getSlipperiness() < FRICTION_MAX_BOUNDARY)
            itemStack.setCount(itemStack.getCount() - 1);
        fte.setSlipperiness(newFrictionValue(fte.getSlipperiness(), increaseFriction));
        player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.friction", (Math.round(fte.getSlipperiness() * 1000) / 1000f)), true);
        return true;
    }

    private static float newFrictionValue(float currValue, boolean increaseFriction) {
        if (increaseFriction) {
            return Math.min(currValue * FRICTION_MODIFIER, FRICTION_MAX_BOUNDARY);
        }
        return Math.max(currValue / FRICTION_MODIFIER, FRICTION_MIN_BOUNDARY);
    }

    public static <V extends IFrameTile> boolean setExplosionResistance(ItemStack itemStack, V fte, PlayerEntity player) {
        if (BCModConfig.EXPLOSION_RESISTANCE_ENABLED.get()) {
            if (FrameInteractionItems.isExplosionResistanceModifierSingle(itemStack.getItem())) {
                if (fte.getExplosionResistance() < EXPLOSION_RESISTANCE_MAX)
                    itemStack.setCount(itemStack.getCount() - 1);
                float min = Math.min(fte.getExplosionResistance() * EXPLOSION_RESISTANCE_MODIFIER, EXPLOSION_RESISTANCE_MAX);
                fte.setExplosionResistance(min);
                player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.strength", (Math.round(fte.getExplosionResistance() * 1000) / 1000f)), true);
                return true;
            }
            if (FrameInteractionItems.isExplosionResistanceModifierUltra(itemStack.getItem())) {
                if (fte.getExplosionResistance() < EXPLOSION_RESISTANCE_MAX)
                    itemStack.setCount(itemStack.getCount() - 1);
                fte.setExplosionResistance(EXPLOSION_RESISTANCE_MAX);
                player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.strength_max", (Math.round(fte.getExplosionResistance() * 1000) / 1000f)), true);
                return true;
            }
        }
        return false;
    }

    public static <V extends IFrameTile> boolean setSustainability(ItemStack itemStack, V fte, PlayerEntity player) {
        if (BCModConfig.SUSTAINABILITY_ENABLED.get()) {
            if (FrameInteractionItems.isSustainabilityModifier(itemStack.getItem())) {
                if (!fte.getCanSustainPlant())
                    itemStack.setCount(itemStack.getCount() - 1);
                fte.setCanSustainPlant(true);
                player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.sustainability"), true);
                return true;
            }
        }
        return false;
    }

    public static <V extends IFrameTile> boolean setEnchantingPower(ItemStack itemStack, V fte, PlayerEntity player) {
        if (BCModConfig.ENCHANT_POWER_ENABLED.get()) {
            if (FrameInteractionItems.isEnchantingPowerModifier(itemStack.getItem())) {
                if (fte.getEnchantPowerBonus() != 1)
                    itemStack.setCount(itemStack.getCount() - 1);
                fte.setEnchantPowerBonus(1);
                player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.enchanting_power"), true);
                return true;
            }
        }
        return false;
    }

    public static <V extends IFrameTile> boolean setCanEntityDestroy(ItemStack itemStack, V fte, PlayerEntity player) {
        if (BCModConfig.CAN_ENTITY_DESTROY_ENABLED.get()) {
            if (FrameInteractionItems.isEntityDestroyModifier(itemStack.getItem())) {
                if (fte.getCanEntityDestroy()) {
                    itemStack.setCount(itemStack.getCount() - 1);
                    player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.can_entity_destroy"), true);
                } else
                    player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.can_entity_destroy_already"), true);
                fte.setCanEntityDestroy(false);

                return true;
            }
        }
        return false;
    }
}
//========SOLI DEO GLORIA========//