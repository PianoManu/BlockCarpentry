package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.block.DaylightDetectorFrameBlock;
import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TileEntity for {@link mod.pianomanu.blockcarpentry.block.DaylightDetectorFrameBlock}
 * Contains all information about the block and the mimicked block
 *
 * @author PianoManu
 * @version 1.0 08/20/21
 */
public class DaylightDetectorFrameTileEntity extends TileEntity implements ITickableTileEntity {
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
    private BlockState mimic;
    private Integer texture = 0;
    private Integer design = 0;
    private Integer designTexture = 0;
    private Integer glassColor = 0;
    private Integer overlay = 0;
    private Integer rotation = 0;
    private Boolean northVisible = true;
    private Boolean eastVisible = true;
    private Boolean southVisible = true;
    private Boolean westVisible = true;
    private Boolean upVisible = true;
    private Boolean downVisible = true;

    public DaylightDetectorFrameTileEntity() {
        super(Registration.DAYLIGHT_DETECTOR_FRAME_TILE.get());
    }

    private static Integer readInteger(CompoundNBT tag) {
        if (!tag.contains("number", 8)) {
            return 0;
        } else {
            try {
                return Integer.parseInt(tag.getString("number"));
            } catch (NumberFormatException e) {
                LOGGER.error("Not a valid Number Format: " + tag.getString("number"));
                return 0;
            }
        }
    }
    //======================FRAME STUFF======================//

    private static CompoundNBT writeInteger(Integer tag) {
        CompoundNBT compoundnbt = new CompoundNBT();
        compoundnbt.putString("number", tag.toString());
        return compoundnbt;
    }

    public void tick() {
        if (this.world != null && !this.world.isRemote && this.world.getGameTime() % 20L == 0L) {
            BlockState blockstate = this.getBlockState();
            Block block = blockstate.getBlock();
            if (block instanceof DaylightDetectorFrameBlock) {
                DaylightDetectorFrameBlock.updatePower(blockstate, this.world, this.pos);
            }
        }

    }

    public BlockState getMimic() {
        return this.mimic;
    }

    public void setMimic(BlockState mimic) {
        this.mimic = mimic;
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getDesign() {
        return this.design;
    }

    public void setDesign(Integer design) {
        this.design = design;
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getDesignTexture() {
        return this.designTexture;
    }

    public void setDesignTexture(Integer designTexture) {
        this.designTexture = designTexture;
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getTexture() {
        return this.texture;
    }

    public void setTexture(Integer texture) {
        this.texture = texture;
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getGlassColor() {
        return this.glassColor;
    }

    public void setGlassColor(Integer colorNumber) {
        this.glassColor = colorNumber;
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public void setVisibileSides(Direction dir, boolean isVisible) {
        switch (dir) {
            case DOWN:
                downVisible = isVisible;
                break;
            case UP:
                upVisible = isVisible;
                break;
            case NORTH:
                northVisible = isVisible;
                break;
            case WEST:
                westVisible = isVisible;
                break;
            case SOUTH:
                southVisible = isVisible;
                break;
            case EAST:
                eastVisible = isVisible;
                break;
            default:
                break;
        }
    }

    public List<Direction> getVisibleSides() {
        List<Direction> dir = new ArrayList<>();
        if (northVisible)
            dir.add(Direction.NORTH);
        if (eastVisible)
            dir.add(Direction.EAST);
        if (southVisible)
            dir.add(Direction.SOUTH);
        if (westVisible)
            dir.add(Direction.WEST);
        if (upVisible)
            dir.add(Direction.UP);
        if (downVisible)
            dir.add(Direction.DOWN);
        return dir;
    }

    public Integer getOverlay() {
        return this.overlay;
    }

    public void setOverlay(Integer overlay) {
        this.overlay = overlay;
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        if (mimic != null) {
            tag.put("mimic", NBTUtil.writeBlockState(mimic));
        }
        if (texture != null) {
            tag.put("texture", writeInteger(texture));
        }
        if (design != null) {
            tag.put("design", writeInteger(design));
        }
        if (designTexture != null) {
            tag.put("design_texture", writeInteger(designTexture));
        }
        if (glassColor != null) {
            tag.put("glass_color", writeInteger(glassColor));
        }
        if (overlay != null) {
            tag.put("overlay", writeInteger(overlay));
        }
        if (rotation != null) {
            tag.put("rotation", writeInteger(rotation));
        }
        return tag;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState oldMimic = mimic;
        Integer oldTexture = texture;
        Integer oldDesign = design;
        Integer oldDesignTexture = designTexture;
        Integer oldGlassColor = glassColor;
        Integer oldOverlay = overlay;
        Integer oldRotation = rotation;
        CompoundNBT tag = pkt.getNbtCompound();
        if (tag.contains("mimic")) {
            mimic = NBTUtil.readBlockState(tag.getCompound("mimic"));
            if (!Objects.equals(oldMimic, mimic)) {
                ModelDataManager.requestModelDataRefresh(this);
                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("texture")) {
            texture = readInteger(tag.getCompound("texture"));
            if (!Objects.equals(oldTexture, texture)) {
                ModelDataManager.requestModelDataRefresh(this);
                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("design")) {
            design = readInteger(tag.getCompound("design"));
            if (!Objects.equals(oldDesign, design)) {
                ModelDataManager.requestModelDataRefresh(this);
                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("design_texture")) {
            designTexture = readInteger(tag.getCompound("design_texture"));
            if (!Objects.equals(oldDesignTexture, designTexture)) {
                ModelDataManager.requestModelDataRefresh(this);
                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("glass_color")) {
            glassColor = readInteger(tag.getCompound("glass_color"));
            if (!Objects.equals(oldGlassColor, glassColor)) {
                ModelDataManager.requestModelDataRefresh(this);
                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("overlay")) {
            overlay = readInteger(tag.getCompound("overlay"));
            if (!Objects.equals(oldOverlay, overlay)) {
                ModelDataManager.requestModelDataRefresh(this);
                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("rotation")) {
            rotation = readInteger(tag.getCompound("rotation"));
            if (!Objects.equals(oldRotation, rotation)) {
                ModelDataManager.requestModelDataRefresh(this);
                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(MIMIC, mimic)
                .withInitial(TEXTURE, texture)
                .withInitial(DESIGN, design)
                .withInitial(DESIGN_TEXTURE, designTexture)
                .withInitial(GLASS_COLOR, glassColor)
                .withInitial(OVERLAY, overlay)
                .withInitial(ROTATION, rotation)
                .withInitial(NORTH_VISIBLE, northVisible)
                .withInitial(EAST_VISIBLE, eastVisible)
                .withInitial(SOUTH_VISIBLE, southVisible)
                .withInitial(WEST_VISIBLE, westVisible)
                .withInitial(UP_VISIBLE, upVisible)
                .withInitial(DOWN_VISIBLE, downVisible)
                .build();
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        if (tag.contains("mimic")) {
            mimic = NBTUtil.readBlockState(tag.getCompound("mimic"));
        }
        if (tag.contains("texture")) {
            texture = readInteger(tag.getCompound("texture"));
        }
        if (tag.contains("design")) {
            design = readInteger(tag.getCompound("design"));
        }
        if (tag.contains("design_texture")) {
            designTexture = readInteger(tag.getCompound("design_texture"));
        }
        if (tag.contains("glass_color")) {
            glassColor = readInteger(tag.getCompound("glass_color"));
        }
        if (tag.contains("overlay")) {
            overlay = readInteger(tag.getCompound("overlay"));
        }
        if (tag.contains("rotation")) {
            rotation = readInteger(tag.getCompound("rotation"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        if (mimic != null) {
            tag.put("mimic", NBTUtil.writeBlockState(mimic));
        }
        if (texture != null) {
            tag.put("texture", writeInteger(texture));
        }
        if (design != null) {
            tag.put("design", writeInteger(design));
        }
        if (designTexture != null) {
            tag.put("design_texture", writeInteger(designTexture));
        }
        if (glassColor != null) {
            tag.put("glass_color", writeInteger(glassColor));
        }
        if (overlay != null) {
            tag.put("overlay", writeInteger(overlay));
        }
        if (rotation != null) {
            tag.put("rotation", writeInteger(rotation));
        }
        return super.write(tag);
    }

    public void clear() {
        this.setMimic(null);
        this.setDesign(0);
        this.setDesign(0);
        this.setDesign(0);
        this.setGlassColor(0);
        this.setOverlay(0);
        this.setRotation(0);
    }

}
//========SOLI DEO GLORIA========//