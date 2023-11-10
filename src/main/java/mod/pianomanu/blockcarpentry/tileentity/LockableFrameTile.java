package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * BlockEntity for blocks that can be opened or closed, e.g. {@link mod.pianomanu.blockcarpentry.block.DoorFrameBlock}.
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

    public LockableFrameTile(BlockPos pos, BlockState state) {
        super(Registration.DOOR_FRAME_TILE.get(), pos, state);
        this.canBeOpenedByPlayers = true;
        this.canBeOpenedByRedstoneSignal = true;
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

    private static CompoundTag writeBool(boolean bool) {
        CompoundTag compoundnbt = new CompoundTag();
        compoundnbt.putString("boolean", String.valueOf(bool));
        return compoundnbt;
    }

    private static boolean readBool(CompoundTag tag, boolean defaultReturnValue) {
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

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        boolean oldCanBeOpenedByPlayers = this.canBeOpenedByPlayers;
        boolean oldCanBeOpenedByRedstoneSignal = this.canBeOpenedByRedstoneSignal;
        CompoundTag tag = pkt.getTag();
        if (tag.contains("canBeOpenedByPlayers")) {
            canBeOpenedByPlayers = readBool(tag.getCompound("canBeOpenedByPlayers"), true);
            if (!Objects.equals(oldCanBeOpenedByPlayers, canBeOpenedByPlayers)) {
                this.requestModelDataUpdate();
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
            }
        }
        if (tag.contains("canBeOpenedByRedstoneSignal")) {
            canBeOpenedByRedstoneSignal = readBool(tag.getCompound("canBeOpenedByRedstoneSignal"), true);
            if (!Objects.equals(oldCanBeOpenedByRedstoneSignal, canBeOpenedByRedstoneSignal)) {
                this.requestModelDataUpdate();
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
            }
        }
        super.onDataPacket(net, pkt);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("canBeOpenedByPlayers", writeBool(this.canBeOpenedByPlayers));
        tag.put("canBeOpenedByRedstoneSignal", writeBool(this.canBeOpenedByRedstoneSignal));
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("canBeOpenedByPlayers")) {
            this.canBeOpenedByPlayers = readBool(tag.getCompound("canBeOpenedByPlayers"), true);
        }
        if (tag.contains("canBeOpenedByRedstoneSignal")) {
            this.canBeOpenedByRedstoneSignal = readBool(tag.getCompound("canBeOpenedByRedstoneSignal"), true);
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("canBeOpenedByPlayers", writeBool(canBeOpenedByPlayers));
        tag.put("canBeOpenedByRedstoneSignal", writeBool(canBeOpenedByRedstoneSignal));
    }

    public void addFromOutdatedTileEntity(FrameBlockTile tile) {
        if (level == null)
            this.level = tile.getLevel();
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