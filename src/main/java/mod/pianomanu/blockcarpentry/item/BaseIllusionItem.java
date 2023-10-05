package mod.pianomanu.blockcarpentry.item;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This class is used to add a tooltip to all illusion blocks that can be seen when hovering over the associated item
 *
 * @author PianoManu
 * @version 1.0 05/31/22
 */
public class BaseIllusionItem extends BlockItem {
    /**
     * Standard constructor for Minecraft block items
     *
     * @param block      the block associated to this item
     * @param properties the item's properties
     */
    public BaseIllusionItem(Block block, Properties properties) {
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
            component.add(new TranslationTextComponent("tooltip.blockcarpentry.base_illusion"));
            if (!this.getBlock().equals(Registration.ILLUSION_BLOCK.get()) && !this.getBlock().equals(Registration.LAYERED_ILLUSIONBLOCK.get())) {
                component.add(new TranslationTextComponent("tooltip.blockcarpentry.vanilla_like"));
            }
            if (this.getBlock().equals(Registration.LAYERED_ILLUSIONBLOCK.get())) {
                component.add(new TranslationTextComponent("tooltip.blockcarpentry.layered"));
            }
            if (this.getBlock().equals(Registration.DOOR_ILLUSIONBLOCK.get()) || this.getBlock().equals(Registration.TRAPDOOR_ILLUSIONBLOCK.get()) || this.getBlock().equals(Registration.FENCE_GATE_ILLUSIONBLOCK.get())) {
                component.add(new TranslationTextComponent("tooltip.blockcarpentry.locking"));
            }
        } else {
            component.add(new TranslationTextComponent("tooltip.blockcarpentry.shift"));
        }
    }
}
//========SOLI DEO GLORIA========//