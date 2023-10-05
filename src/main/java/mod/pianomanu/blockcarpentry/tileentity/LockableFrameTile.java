package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * TileEntity for blocks that can be opened or closed, e.g. {@link mod.pianomanu.blockcarpentry.block.DoorFrameBlock}.
 * Contains all information about the block and the mimicked block, plus information whether players or redstone signals
 * can change the state of the block.
 *
 * @author PianoManu
 * @version 1.3 09/27/23
 */
public class LockableFrameTile extends FrameBlockTile {
    private static final Logger LOGGER = LogManager.getLogger();

    public boolean canBeOpenedByPlayers;
    public boolean canBeOpenedByRedstoneSignal;

    public LockableFrameTile() {
        super(Registration.DOOR_FRAME_TILE.get());
        this.canBeOpenedByPlayers = true;
        this.canBeOpenedByRedstoneSignal = true;
    }

    private static CompoundNBT writeBool(boolean bool) {
        CompoundNBT CompoundNBT = new CompoundNBT();
        CompoundNBT.putString("boolean", String.valueOf(bool));
        return CompoundNBT;
    }

    private static boolean readBool(CompoundNBT tag, boolean defaultReturnValue) {
        if (!tag.contains("boolean", 8)) {
            return defaultReturnValue;
        } else {
            try {
                return Boolean.parseBoolean(tag.getString("boolean"));
            } catch (NumberFormatException e) {
                LOGGER.error("Not a valid Boolean Value: " + tag.getString("boolean"));
                return defaultReturnValue;
            }
        }
    }

    public boolean canBeOpenedByPlayers() {
        return canBeOpenedByPlayers;
    }

    public void setCanBeOpenedByPlayers(boolean canBeOpenedByPlayers) {
        this.canBeOpenedByPlayers = canBeOpenedByPlayers;
    }

    public boolean canBeOpenedByRedstoneSignal() {
        return canBeOpenedByRedstoneSignal;
    }

    public void setCanBeOpenedByRedstoneSignal(boolean canBeOpenedByRedstoneSignal) {
        this.canBeOpenedByRedstoneSignal = canBeOpenedByRedstoneSignal;
    }

    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        boolean oldCanBeOpenedByPlayers = this.canBeOpenedByPlayers;
        boolean oldCanBeOpenedByRedstoneSignal = this.canBeOpenedByRedstoneSignal;
        CompoundNBT tag = pkt.getNbtCompound();
        if (tag.contains("canBeOpenedByPlayers")) {
            canBeOpenedByPlayers = readBool(tag.getCompound("canBeOpenedByPlayers"), true);
            if (!Objects.equals(oldCanBeOpenedByPlayers, canBeOpenedByPlayers)) {
                this.requestModelDataUpdate();
                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("canBeOpenedByRedstoneSignal")) {
            canBeOpenedByRedstoneSignal = readBool(tag.getCompound("canBeOpenedByRedstoneSignal"), true);
            if (!Objects.equals(oldCanBeOpenedByRedstoneSignal, canBeOpenedByRedstoneSignal)) {
                this.requestModelDataUpdate();
                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        super.onDataPacket(net, pkt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        tag.put("canBeOpenedByPlayers", writeBool(this.canBeOpenedByPlayers));
        tag.put("canBeOpenedByRedstoneSignal", writeBool(this.canBeOpenedByRedstoneSignal));
        return tag;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        if (tag.contains("canBeOpenedByPlayers")) {
            this.canBeOpenedByPlayers = readBool(tag.getCompound("canBeOpenedByPlayers"), true);
        }
        if (tag.contains("canBeOpenedByRedstoneSignal")) {
            this.canBeOpenedByRedstoneSignal = readBool(tag.getCompound("canBeOpenedByRedstoneSignal"), true);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.put("canBeOpenedByPlayers", writeBool(canBeOpenedByPlayers));
        tag.put("canBeOpenedByRedstoneSignal", writeBool(canBeOpenedByRedstoneSignal));
        return tag;
    }

    public void addFromOutdatedTileEntity(FrameBlockTile tile) {
        if (world == null)
            this.world = tile.getWorld();
        this.setDesign(tile.getDesign());
        this.setDesignTexture(tile.getDesignTexture());
        this.setMimic(tile.getMimic());
        this.setGlassColor(tile.getGlassColor());
        this.setOverlay(tile.getOverlay());
        this.setRotation(tile.getRotation());
        this.setTexture(tile.getTexture());
    }
}
//========SOLI DEO GLORIA========//