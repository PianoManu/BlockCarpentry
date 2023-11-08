package mod.pianomanu.blockcarpentry.item;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This class is used to add a tooltip to the paintbrush item
 *
 * @author PianoManu
 * @version 1.4 11/08/23
 */
public class PaintbrushItem extends BCToolItem {

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
            component.add(Component.translatable("tooltip.blockcarpentry.paintbrush"));
        } else {
            component.add(Component.translatable("tooltip.blockcarpentry.shift"));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity entity = level.getBlockEntity(pos);
        Player player = context.getPlayer();
        if (!level.isClientSide && (state.getBlock().equals(Registration.ILLUSION_BLOCK.get()) || state.getBlock().equals(Registration.FRAMEBLOCK.get()))) {
            if (entity instanceof FrameBlockTile fte) {
                if (player == null || player.isCrouching()) {
                    boolean keepUV = fte.getKeepUV();
                    fte.setKeepUV(!keepUV);
                    if (player != null) {
                        player.displayClientMessage(keepUV ? Component.translatable("message.blockcarpentry.paintbrush.relaxed") : Component.translatable("message.blockcarpentry.paintbrush.fixed"), true);
                    }
                }
            }
        }
        return super.useOn(context);
    }

    public static boolean setRectangleCustom(Level level, Player player, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof FrameBlockTile fte) {
            fte.setFaceRemainRectangle(!fte.getFaceRemainRectangle());
            player.displayClientMessage(fte.getFaceRemainRectangle() ? Component.translatable("message.blockcarpentry.paintbrush.rectangle") : Component.translatable("message.blockcarpentry.paintbrush.custom"), true);
            return true;
        }
        return false;
    }
}
//========SOLI DEO GLORIA========//