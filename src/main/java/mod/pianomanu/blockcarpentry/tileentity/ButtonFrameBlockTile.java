package mod.pianomanu.blockcarpentry.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ButtonFrameBlockTile extends FrameBlockTile {
    private AttachFace face;
    private Direction facing;
    public ButtonFrameBlockTile() {
        face = AttachFace.WALL;
        facing = Direction.NORTH;
    }
    public ButtonFrameBlockTile(AttachFace face, Direction facing) {
        this.facing = facing;
        this.face = face;
    }

    public Direction getFacing() {
        switch (face) {
            case CEILING:
                return Direction.UP;
            case FLOOR:
                return Direction.DOWN;
            default:
                return facing;
        }
    }
    public void setFacing(Direction facing) {
        this.facing=facing;
    }

    @Override
    public void setMimic(BlockState mimic) {
        super.setMimic(mimic);
    }

    @Override
    public BlockState getMimic() {
        return super.getMimic();
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return super.getUpdateTag();
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return super.getUpdatePacket();
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return super.getModelData();
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        return super.write(tag);
    }

    @Override
    public void clear() {
        super.clear();
    }
}
