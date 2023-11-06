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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This class is used to add a tooltip to the texture wrench item
 *
 * @author PianoManu
 * @version 1.5 11/03/23
 */
public class TextureWrenchItem extends BCToolItem {

    /**
     * Standard constructor for Minecraft items
     *
     * @param properties the item's properties
     */
    public TextureWrenchItem(Properties properties) {
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
            component.add(new TranslationTextComponent("tooltip.blockcarpentry.texture_wrench"));
        } else {
            component.add(new TranslationTextComponent("tooltip.blockcarpentry.shift"));
        }
    }

    @Override
    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        super.onUse(worldIn, livingEntityIn, stack, count);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World level = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState state = level.getBlockState(pos);
        TileEntity entity = level.getTileEntity(pos);
        PlayerEntity player = context.getPlayer();
        if (!level.isRemote && (state.getBlock().equals(Registration.FRAMEBLOCK.get()) || state.getBlock().equals(Registration.ILLUSION_BLOCK.get()))) {
            if (entity instanceof FrameBlockTile) {
                FrameBlockTile fte = (FrameBlockTile) entity;
                Direction direction = context.getFace();
                Direction.Axis axis = direction.getAxis() == Direction.Axis.Y ? direction.getAxis() : direction.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
                String axisStr = axis.name().toUpperCase();
                if (player != null) {
                    if (player.isCrouching()) {
                        fte.addRotation(direction);
                        player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.rotation.face", axisStr), true);
                    } else {
                        fte.addDirection(direction);
                        player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.rotation.axis", axisStr), true);
                        return ActionResultType.SUCCESS;
                    }
                }
            }
        }
        return super.onItemUse(context);
    }
}
//========SOLI DEO GLORIA========//