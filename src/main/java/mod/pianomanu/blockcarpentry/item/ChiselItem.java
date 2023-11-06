package mod.pianomanu.blockcarpentry.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * This class is used to add a tooltip to the chisel item
 *
 * @author PianoManu
 * @version 1.3 10/23/23
 */
public class ChiselItem extends BCToolItem {

    private boolean shrink = true;

    /**
     * Standard constructor for Minecraft items
     *
     * @param properties the item's properties
     */
    public ChiselItem(Properties properties) {
        super(properties);
    }

    /**
     * This method adds a tooltip to the item shown when hovering over the item in an inventory
     *
     * @param itemStack the itemStack where the text shall be appended
     * @param level     the current level
     * @param component list of text components; for this item, we append either the item description or the reference to press shift
     * @param flag      I don't really know what this does, I did not need it
     */
    @Override
    public void addInformation(ItemStack itemStack, @Nullable World level, List<ITextComponent> component, ITooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            component.add(new TranslationTextComponent("tooltip.blockcarpentry.chisel"));
        } else {
            component.add(new TranslationTextComponent("tooltip.blockcarpentry.shift"));
        }
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player) {
        return false;
    }

    @Override
    public void onUse(World worldIn, LivingEntity entity, ItemStack stack, int count) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (player.isCrouching()) {
                this.shrink = !this.shrink;
                player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.chisel.mode", this.shrink ? new TranslationTextComponent("message.blockcarpentry.chisel.shrink") : new TranslationTextComponent("message.blockcarpentry.chisel.enlarge")), true);
            }
        }
        super.onUse(worldIn, entity, stack, count);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getWorld().isRemote)
            onUse(context.getWorld(), context.getPlayer(), context.getItem(), context.getItem().getCount());
        return super.onItemUse(context);
    }

    public boolean shouldShrink() {
        return this.shrink;
    }
}
//========SOLI DEO GLORIA========//