package mod.pianomanu.blockcarpentry.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> component, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            component.add(new TranslatableComponent("tooltip.blockcarpentry.chisel"));
        } else {
            component.add(new TranslatableComponent("tooltip.blockcarpentry.shift"));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.isCrouching()) {
            this.shrink = !this.shrink;
            player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.chisel.mode", this.shrink ? new TranslatableComponent("message.blockcarpentry.chisel.shrink") : new TranslatableComponent("message.blockcarpentry.chisel.enlarge")), true);
        }
        return super.use(level, player, hand);
    }

    public boolean shouldShrink() {
        return this.shrink;
    }
}
//========SOLI DEO GLORIA========//