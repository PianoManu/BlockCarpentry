package mod.pianomanu.blockcarpentry.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

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
}
//========SOLI DEO GLORIA========//