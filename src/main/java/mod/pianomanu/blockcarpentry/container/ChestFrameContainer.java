package mod.pianomanu.blockcarpentry.container;

import mod.pianomanu.blockcarpentry.tileentity.ChestFrameTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class ChestFrameContainer extends TileEntityContainer<ChestFrameTileEntity> {
    //Client
    public ChestFrameContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(type, id, playerInventory, buffer);
    }

    //Server
    public ChestFrameContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, ChestFrameTileEntity tileEntity) {
        super(type, id, playerInventory, tileEntity);
    }

    @Override
    public void init() {
        //tileEntity.getInventory();
        //addPlayerInventory();
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        final Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (index < 18) {
                if (!mergeItemStack(slotStack, 18, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 0, 18, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return stack;
    }
}
//========SOLI DEO GLORIA========//