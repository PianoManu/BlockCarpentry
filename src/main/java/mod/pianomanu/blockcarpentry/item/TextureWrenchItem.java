package mod.pianomanu.blockcarpentry.item;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
 * This class is used to add a tooltip to the texture wrench item
 *
 * @author PianoManu
 * @version 1.6 11/08/23
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
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> component, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            component.add(new TranslatableComponent("tooltip.blockcarpentry.texture_wrench"));
        } else {
            component.add(new TranslatableComponent("tooltip.blockcarpentry.shift"));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity entity = level.getBlockEntity(pos);
        Player player = context.getPlayer();
        if (!level.isClientSide && (state.getBlock().equals(Registration.FRAMEBLOCK.get()) || state.getBlock().equals(Registration.ILLUSION_BLOCK.get()))) {
            if (entity instanceof FrameBlockTile fte) {
                Direction direction = context.getClickedFace();
                Direction.Axis axis = direction.getAxis() == Direction.Axis.Y ? direction.getAxis() : direction.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
                String axisStr = axis.getName().toUpperCase();
                if (player != null) {
                    if (player.isCrouching()) {
                        fte.addRotation(direction);
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.rotation.face"), true);
                    } else {
                        if (state.getBlock().equals(Registration.ILLUSION_BLOCK.get())) {
                            fte.addDirection(direction);
                            player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.rotation.axis", axisStr), true);
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return super.useOn(context);
    }
}
//========SOLI DEO GLORIA========//