package mod.pianomanu.blockcarpentry.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * This class is used as a base class for the block entity debug item
 *
 * @author PianoManu
 * @version 1.1 10/02/23
 */
public class BlockEntityDebugItem extends BCToolItem {
    public BlockEntityDebugItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(context.getClickedPos());
            String blockName = level.getBlockState(context.getClickedPos()).getBlock().getName().getString();
            if (blockEntity != null) {
                Objects.requireNonNull(context.getPlayer()).displayClientMessage(new TranslatableComponent(formatText(blockEntity.serializeNBT(), blockName)), false);
            } else {
                Objects.requireNonNull(context.getPlayer()).displayClientMessage(new TranslatableComponent("§bBlock \"§c" + blockName + "§b\" does not have a BlockEntity"), false);
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.FAIL;
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
            component.add(new TranslatableComponent("tooltip.blockcarpentry.debug_item"));
        } else {
            component.add(new TranslatableComponent("tooltip.blockcarpentry.shift"));
        }
    }

    private String formatText(CompoundTag tag, String blockName) {
        StringBuilder formattedText = new StringBuilder("§bBlock \"§c" + blockName + "§b\" has following BlockEntityData:");
        for (String s : tag.getAllKeys()) {
            formattedText.append("\n§d    ").append(s).append("§f: §6").append(tag.get(s));
        }
        return formattedText.toString();
    }
}
//========SOLI DEO GLORIA========//