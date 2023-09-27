package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TileEntity for {@link mod.pianomanu.blockcarpentry.block.DaylightDetectorFrameBlock}
 * Contains all information about the block and the mimicked block
 *
 * @author PianoManu
 * @version 1.4 09/27/23
 */
public class DaylightDetectorFrameTileEntity extends FrameBlockTile {
    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
    public static final ModelProperty<Integer> TEXTURE = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_TEXTURE = new ModelProperty<>();
    //currently only for doors and trapdoors
    public static final ModelProperty<Integer> GLASS_COLOR = new ModelProperty<>();
    public static final ModelProperty<Integer> OVERLAY = new ModelProperty<>();
    public static final ModelProperty<Integer> ROTATION = new ModelProperty<>();
    public static final ModelProperty<Boolean> NORTH_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> EAST_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> SOUTH_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> WEST_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> UP_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> DOWN_VISIBLE = new ModelProperty<>();
    private static final Logger LOGGER = LogManager.getLogger();
    public final int maxTextures = 8;
    public final int maxDesignTextures = 4;
    public final int maxDesigns = 4;

    public DaylightDetectorFrameTileEntity(BlockPos pos, BlockState state) {
        super(state.getBlock().getName().equals(Registration.DAYLIGHT_DETECTOR_FRAMEBLOCK.get().getName()) ? Registration.DAYLIGHT_DETECTOR_FRAME_TILE.get() : Registration.DAYLIGHT_DETECTOR_ILLUSION_TILE.get(), pos, state);
    }

    //======================FRAME STUFF======================//

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        return getUpdateTag(tag, DaylightDetectorFrameTileEntity.class);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        onDataPacket(pkt, DaylightDetectorFrameTileEntity.class, level, this.worldPosition, getBlockState());
    }

}
//========SOLI DEO GLORIA========//