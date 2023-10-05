package mod.pianomanu.blockcarpentry.item;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.block.Block;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This class is used to add a tooltip to all frame blocks that can be seen when hovering over the associated item
 *
 * @author PianoManu
 * @version 1.0 05/31/22
 */
public class BaseFrameItem extends BlockItem {
    /**
     * Standard constructor for Minecraft block items
     *
     * @param block      the block associated to this item
     * @param properties the item's properties
     */
    public BaseFrameItem(Block block, Properties properties) {
        super(block, properties);
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
            component.add(new TranslationTextComponent("tooltip.blockcarpentry.base_frame"));
            if (!this.getBlock().equals(Registration.FRAMEBLOCK.get()) && !this.getBlock().equals(Registration.LAYERED_FRAMEBLOCK.get()) && !this.getBlock().equals(Registration.SLOPE_FRAMEBLOCK.get()) && !this.getBlock().equals(Registration.EDGED_SLOPE_FRAMEBLOCK.get())) {
                component.add(new TranslationTextComponent("tooltip.blockcarpentry.vanilla_like"));
            }
            if (this.getBlock().equals(Registration.LAYERED_FRAMEBLOCK.get())) {
                component.add(new TranslationTextComponent("tooltip.blockcarpentry.layered"));
            }
            if (this.getBlock().equals(Registration.SLOPE_FRAMEBLOCK.get()) || this.getBlock().equals(Registration.EDGED_SLOPE_FRAMEBLOCK.get())) {
                component.add(new TranslationTextComponent("tooltip.blockcarpentry.slope"));
            }
            if (this.getBlock().equals(Registration.DOOR_FRAMEBLOCK.get()) || this.getBlock().equals(Registration.TRAPDOOR_FRAMEBLOCK.get()) || this.getBlock().equals(Registration.FENCE_GATE_FRAMEBLOCK.get())) {
                component.add(new TranslationTextComponent("tooltip.blockcarpentry.locking"));
            }
        } else {
            component.add(new TranslationTextComponent("tooltip.blockcarpentry.shift"));
        }
    }
}
//========SOLI DEO GLORIA========//