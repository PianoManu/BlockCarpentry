package mod.pianomanu.blockcarpentry.container;

/**
 * Here you can find all information about the Chest Illusion Container, like number of slots and stored itemStacks
 *
 * @author PianoManu
 * @version 1.0 08/15/21
 */
public class IllusionChestContainer {//extends Container {

    /*public final ChestFrameBlockEntity tileEntity;
    private final IWorldPosCallable canInteractWithCallable;

    public IllusionChestContainer(final int windowId, final PlayerInventory playerInventory, final ChestFrameBlockEntity tileEntity) {
        super(Registration.CHEST_ILLUSION_CONTAINER.get(), windowId);
        this.tileEntity = tileEntity;
        this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());

        // Main Inventory
        int startX = 8;
        int startY = 18;
        int slotSizePlus2 = 18;
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(tileEntity, (row * 9) + column, startX + (column * slotSizePlus2),
                        startY + (row * slotSizePlus2)));
            }
        }

        // Main Player Inventory
        int startPlayerInvY = startY * 4 + 13;
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column, startX + (column * slotSizePlus2),
                        startPlayerInvY + (row * slotSizePlus2)));
            }
        }

        // Hotbar
        int hotbarY = startPlayerInvY + (startPlayerInvY / 2) + 16;
        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(playerInventory, column, startX + (column * slotSizePlus2), hotbarY));
        }
    }

    public IllusionChestContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId,playerInventory,getBlockEntity(playerInventory, data));
    }

    private static ChestFrameBlockEntity getBlockEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final BlockEntity tileAtPos = playerInventory.player.world.getBlockEntity(data.readBlockPos());
        if (tileAtPos instanceof  ChestFrameBlockEntity) {
            return (ChestFrameBlockEntity) tileAtPos;
        }
        throw new IllegalStateException("BlockEntity should be of type ChestFrameBlockEntity but is "+tileAtPos);
    }

    @Override
    public boolean canInteractWith(Player playerIn) {
        return isWithinUsableDistance(canInteractWithCallable, playerIn, Registration.CHEST_ILLUSIONBLOCK.get());
    }

    public ItemStack transferStackInSlot(Player playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
            if (index < 27) {
                if (this.mergeItemStack(itemStack1, 27, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemStack1, 0, 27, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemStack;
    }*/
}
//========SOLI DEO GLORIA========//