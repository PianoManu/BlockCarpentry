package mod.pianomanu.blockcarpentry.item;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This class is used to add a tooltip to all illusion blocks that can be seen when hovering over the associated item
 *
 * @author PianoManu
 * @version 1.1 06/11/22
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
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> component, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            component.add(Component.translatable("tooltip.blockcarpentry.base_illusion"));
            if (!this.getBlock().equals(Registration.ILLUSION_BLOCK.get()) && !this.getBlock().equals(Registration.LAYERED_ILLUSIONBLOCK.get())) {
                component.add(Component.translatable("tooltip.blockcarpentry.vanilla_like"));
            }
            if (this.getBlock().equals(Registration.LAYERED_ILLUSIONBLOCK.get())) {
                component.add(Component.translatable("tooltip.blockcarpentry.layered"));
            }
            if (this.getBlock().equals(Registration.DOOR_ILLUSIONBLOCK.get()) || this.getBlock().equals(Registration.TRAPDOOR_ILLUSIONBLOCK.get()) || this.getBlock().equals(Registration.FENCE_GATE_ILLUSIONBLOCK.get())) {
                component.add(Component.translatable("tooltip.blockcarpentry.locking"));
            }
        } else {
            component.add(Component.translatable("tooltip.blockcarpentry.shift"));
        }
    }
}
//========SOLI DEO GLORIA========//