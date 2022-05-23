package mod.pianomanu.blockcarpentry.container;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Here you can find all information about the Chest Frame Container, like number of slots and stored itemStacks
 *
 * @author PianoManu
 * @version 1.0 05/23/22
 */
public class ChestFrameContainer extends AbstractContainerMenu {
    private final Container container;
    private final int rows = 3;
    private final int columns = 9;

    public ChestFrameContainer(@Nullable MenuType<?> menuType, int containerId, Inventory playerInventory, Container inventory) {
        super(menuType, containerId);

        this.container = inventory;

        inventory.startOpen(playerInventory.player);

        int i = (this.rows - 4) * 18;


        for (int j = 0; j < this.rows; ++j) {
            for (int k = 0; k < this.columns; ++k) {
                this.addSlot(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
        }
    }

    public static ChestFrameContainer createFrameContainerMenu(int containerId, Inventory playerInventory, Container inventory) {
        return new ChestFrameContainer(Registration.CHEST_FRAME_CONTAINER.get(), containerId, playerInventory, inventory);
    }

    public static ChestFrameContainer createFrameContainerMenu(int containerId, Inventory playerInventory) {
        return new ChestFrameContainer(Registration.CHEST_FRAME_CONTAINER.get(), containerId, playerInventory, new SimpleContainer(3 * 9));
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int slotNumber) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotNumber);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (slotNumber < this.rows * 9) {
                if (!this.moveItemStackTo(itemstack1, this.rows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.rows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    public Container getContainer() {
        return this.container;
    }

    public int getRowCount() {
        return this.rows;
    }
}
//========SOLI DEO GLORIA========//