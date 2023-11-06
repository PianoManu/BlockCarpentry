package mod.pianomanu.blockcarpentry.item;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This class is used to add a tooltip to the paintbrush item
 *
 * @author PianoManu
 * @version 1.3 10/31/23
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
    public void addInformation(ItemStack itemStack, @Nullable World level, List<ITextComponent> component, ITooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            component.add(new TranslationTextComponent("tooltip.blockcarpentry.paintbrush"));
        } else {
            component.add(new TranslationTextComponent("tooltip.blockcarpentry.shift"));
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World level = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState state = level.getBlockState(pos);
        TileEntity entity = level.getTileEntity(pos);
        PlayerEntity player = context.getPlayer();
        if (!level.isRemote && (state.getBlock().equals(Registration.ILLUSION_BLOCK.get()) || state.getBlock().equals(Registration.FRAMEBLOCK.get()))) {
            if (entity instanceof FrameBlockTile) {
                FrameBlockTile fte = (FrameBlockTile) entity;
                boolean keepUV = fte.getKeepUV();
                fte.setKeepUV(!keepUV);
                if (player != null) {
                    player.sendStatusMessage(keepUV ? new TranslationTextComponent("message.blockcarpentry.paintbrush.relaxed") : new TranslationTextComponent("message.blockcarpentry.paintbrush.fixed"), true);
                }
            }
        }
        return super.onItemUse(context);
    }
}
//========SOLI DEO GLORIA========//