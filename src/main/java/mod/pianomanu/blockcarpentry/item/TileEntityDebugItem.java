package mod.pianomanu.blockcarpentry.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * This class is used as a base class for the block entity debug item
 *
 * @author PianoManu
 * @version 1.1 10/02/23
 */
public class TileEntityDebugItem extends BCToolItem {
    public TileEntityDebugItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World level = context.getWorld();
        if (level.isRemote) {
            TileEntity TileEntity = level.getTileEntity(context.getPos());
            String blockName = level.getBlockState(context.getPos()).getBlock().getRegistryName().getPath();
            if (TileEntity != null) {
                Objects.requireNonNull(context.getPlayer()).sendStatusMessage(new TranslationTextComponent(formatText(TileEntity.serializeNBT(), blockName)), false);
            } else {
                Objects.requireNonNull(context.getPlayer()).sendStatusMessage(new TranslationTextComponent("\u00A7bBlock \"\u00A7c" + blockName + "\u00A7b\" does not have a TileEntity"), false);
            }
            return ActionResultType.CONSUME;
        }
        return ActionResultType.FAIL;
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
            component.add(new TranslationTextComponent("tooltip.blockcarpentry.debug_item"));
        } else {
            component.add(new TranslationTextComponent("tooltip.blockcarpentry.shift"));
        }
    }

    private String formatText(CompoundNBT tag, String blockName) {
        StringBuilder formattedText = new StringBuilder("\u00A7bBlock \"\u00A7c" + blockName + "\u00A7b\" has following TileEntityData:");
        for (String s : tag.keySet()) {
            formattedText.append("\n\u00A7d    ").append(s).append("\u00A7f: \u00A76").append(tag.get(s));
        }
        return formattedText.toString();
    }
}
//========SOLI DEO GLORIA========//