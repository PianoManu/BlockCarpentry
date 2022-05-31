package mod.pianomanu.blockcarpentry.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * BlockEntity for blocks that can be opened or closed, e.g. {@link mod.pianomanu.blockcarpentry.block.DoorFrameBlock}.
 * Contains all information about the block and the mimicked block, plus information whether players or redstone signals
 * can change the state of the block.
 *
 * @author PianoManu
 * @version 1.0 05/31/22
 */
public class LockableFrameTile extends FrameBlockTile {
    private boolean canBeOpenedByPlayers;
    private boolean canBeOpenedByRedstoneSignal;

    public LockableFrameTile(BlockPos pos, BlockState state) {
        super(pos, state);
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
}
//========SOLI DEO GLORIA========//