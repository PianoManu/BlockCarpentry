package mod.pianomanu.blockcarpentry.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
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
 * @version 1.0 09/24/23
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
            if (blockEntity != null) {
                Objects.requireNonNull(context.getPlayer()).displayClientMessage(Component.literal(blockEntity.serializeNBT().toString()), false);
            } else {
                Objects.requireNonNull(context.getPlayer()).displayClientMessage(Component.literal("Block \"" + level.getBlockState(context.getClickedPos()).getBlock().getName().getString() + "\" does not have a BlockEntity"), false);
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
            component.add(Component.translatable("tooltip.blockcarpentry.debug_item"));
        } else {
            component.add(Component.translatable("tooltip.blockcarpentry.shift"));
        }
    }
}
//========SOLI DEO GLORIA========//