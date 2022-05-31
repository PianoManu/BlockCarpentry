package mod.pianomanu.blockcarpentry.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This class is used to add a tooltip to the paintbrush item
 *
 * @author PianoManu
 * @version 1.0 05/31/22
 */
public class PaintbrushItem extends Item {

    /**
     * Standard constructor for Minecraft items
     *
     * @param properties the item's properties
     */
    public PaintbrushItem(Properties properties) {
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
            component.add(new TranslatableComponent("tooltip.blockcarpentry.paintbrush"));
        } else {
            component.add(new TranslatableComponent("tooltip.blockcarpentry.tool_shift"));
        }
    }
}
//========SOLI DEO GLORIA========//