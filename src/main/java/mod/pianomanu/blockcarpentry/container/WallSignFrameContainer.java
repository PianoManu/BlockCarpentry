package mod.pianomanu.blockcarpentry.container;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.SignFrameTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

import java.util.Objects;

/**
 * Do we need this?
 */
public class WallSignFrameContainer extends Container {
    public final SignFrameTile tileEntity;
    private final IWorldPosCallable canInteractWithCallable;

    public WallSignFrameContainer(final int windowId, final PlayerInventory playerInventory, final SignFrameTile tileEntity) {
        super(Registration.WALL_SIGN_FRAME_CONTAINER.get(), windowId);
        this.tileEntity = tileEntity;
        this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());
    }

    public WallSignFrameContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId,playerInventory,getTileEntity(playerInventory, data));
        System.out.println("new container");
    }

    private static SignFrameTile getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final TileEntity tileAtPos = playerInventory.player.world.getTileEntity(data.readBlockPos());
        System.out.println(tileAtPos.getType());
        if (tileAtPos instanceof  SignFrameTile) {
            return (SignFrameTile) tileAtPos;
        }
        throw new IllegalStateException("TileEntity should be of type SignFrameTile but is "+tileAtPos);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        System.out.println("interact");
        return isWithinUsableDistance(canInteractWithCallable, playerIn, Registration.WALL_SIGN_FRAMEBLOCK.get());
    }
}
//========SOLI DEO GLORIA========//