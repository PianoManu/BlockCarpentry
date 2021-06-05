package mod.pianomanu.blockcarpentry.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static mod.pianomanu.blockcarpentry.setup.Registration.SLAB_FRAME_TILE;

/**
 * TileEntity for {@link mod.pianomanu.blockcarpentry.block.SixWaySlabFrameBlock} and slopes (not yet implemented)
 * Contains all information about the block and the mimicked block
 *
 * @author PianoManu
 * @version 1.0 06/05/21
 */
public class TwoBlocksFrameBlockTile extends TileEntity {
    public static final ModelProperty<BlockState> MIMIC_1 = new ModelProperty<>();
    public static final ModelProperty<Integer> TEXTURE_1 = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_1 = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_TEXTURE_1 = new ModelProperty<>();
    public static final ModelProperty<Integer> OVERLAY_1 = new ModelProperty<>();
    public static final ModelProperty<Integer> ROTATION_1 = new ModelProperty<>();

    public static final ModelProperty<BlockState> MIMIC_2 = new ModelProperty<>();
    public static final ModelProperty<Integer> TEXTURE_2 = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_2 = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_TEXTURE_2 = new ModelProperty<>();
    public static final ModelProperty<Integer> OVERLAY_2 = new ModelProperty<>();
    public static final ModelProperty<Integer> ROTATION_2 = new ModelProperty<>();
    private static final Logger LOGGER = LogManager.getLogger();
    public final int maxTextures = 8;
    public final int maxDesignTextures = 4;
    public final int maxDesigns = 4;
    private BlockState mimic_1;
    private Integer texture_1 = 0;
    private Integer design_1 = 0;
    private Integer designTexture_1 = 0;
    private Integer overlay_1 = 0;
    private Integer rotation_1 = 0;
    private BlockState mimic_2;
    private Integer texture_2 = 0;
    private Integer design_2 = 0;
    private Integer designTexture_2 = 0;
    private Integer overlay_2 = 0;
    private Integer rotation_2 = 0;

    public TwoBlocksFrameBlockTile() {
        super(SLAB_FRAME_TILE.get());
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

    private static CompoundNBT writeInteger(Integer tag) {
        CompoundNBT compoundnbt = new CompoundNBT();
        compoundnbt.putString("number", tag.toString());
        return compoundnbt;
    }

    public BlockState getMimic_1() {
        return mimic_1;
    }

    public void setMimic_1(BlockState mimic_1) {
        this.mimic_1 = mimic_1;
        markDirty();
        Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getTexture_1() {
        return texture_1;
    }

    public void setTexture_1(int texture_1) {
        this.texture_1 = texture_1;
        markDirty();
        Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getDesign_1() {
        return design_1;
    }

    public void setDesign_1(int design_1) {
        this.design_1 = design_1;
        markDirty();
        Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getDesignTexture_1() {
        return designTexture_1;
    }

    public void setDesignTexture_1(int designTexture_1) {
        this.designTexture_1 = designTexture_1;
        markDirty();
        Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getOverlay_1() {
        return overlay_1;
    }

    public void setOverlay_1(int overlay_1) {
        this.overlay_1 = overlay_1;
        markDirty();
        Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getRotation_1() {
        return rotation_1;
    }

    public void setRotation_1(int rotation_1) {
        this.rotation_1 = rotation_1;
        markDirty();
        Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public BlockState getMimic_2() {
        return mimic_2;
    }

    public void setMimic_2(BlockState mimic_2) {
        this.mimic_2 = mimic_2;
        markDirty();
        Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getTexture_2() {
        return texture_2;
    }

    public void setTexture_2(int texture_2) {
        this.texture_2 = texture_2;
        markDirty();
        Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getDesign_2() {
        return design_2;
    }

    public void setDesign_2(int design_2) {
        this.design_2 = design_2;
        markDirty();
        Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getDesignTexture_2() {
        return designTexture_2;
    }

    public void setDesignTexture_2(int designTexture_2) {
        this.designTexture_2 = designTexture_2;
        markDirty();
        Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getOverlay_2() {
        return overlay_2;
    }

    public void setOverlay_2(int overlay_2) {
        this.overlay_2 = overlay_2;
        markDirty();
        Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getRotation_2() {
        return rotation_2;
    }

    public void setRotation_2(int rotation_2) {
        this.rotation_2 = rotation_2;
        markDirty();
        Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        if (mimic_1 != null) {
            tag.put("mimic_1", NBTUtil.writeBlockState(mimic_1));
        }
        if (texture_1 != null) {
            tag.put("texture_1", writeInteger(texture_1));
        }
        if (design_1 != null) {
            tag.put("design_1", writeInteger(design_1));
        }
        if (designTexture_1 != null) {
            tag.put("design_texture_1", writeInteger(designTexture_1));
        }
        if (overlay_1 != null) {
            tag.put("overlay_1", writeInteger(overlay_1));
        }
        if (rotation_1 != null) {
            tag.put("rotation_1", writeInteger(rotation_1));
        }

        if (mimic_2 != null) {
            tag.put("mimic_2", NBTUtil.writeBlockState(mimic_2));
        }
        if (texture_2 != null) {
            tag.put("texture_2", writeInteger(texture_2));
        }
        if (design_2 != null) {
            tag.put("design_2", writeInteger(design_2));
        }
        if (designTexture_2 != null) {
            tag.put("design_texture_2", writeInteger(designTexture_2));
        }
        if (overlay_2 != null) {
            tag.put("overlay_2", writeInteger(overlay_2));
        }
        if (rotation_2 != null) {
            tag.put("rotation_2", writeInteger(rotation_2));
        }
        return tag;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState oldMimic_1 = mimic_1;
        Integer oldTexture_1 = texture_1;
        Integer oldDesign_1 = design_1;
        Integer oldDesignTexture_1 = designTexture_1;
        Integer oldOverlay_1 = overlay_1;
        Integer oldRotation_1 = rotation_1;
        BlockState oldMimic_2 = mimic_2;
        Integer oldTexture_2 = texture_2;
        Integer oldDesign_2 = design_2;
        Integer oldDesignTexture_2 = designTexture_2;
        Integer oldOverlay_2 = overlay_2;
        Integer oldRotation_2 = rotation_2;
        CompoundNBT tag = pkt.getNbtCompound();
        if (tag.contains("mimic_1")) {
            mimic_1 = NBTUtil.readBlockState(tag.getCompound("mimic_1"));
            if (!Objects.equals(oldMimic_1, mimic_1)) {
                ModelDataManager.requestModelDataRefresh(this);
                Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("texture_1")) {
            texture_1 = readInteger(tag.getCompound("texture_1"));
            if (!Objects.equals(oldTexture_1, texture_1)) {
                ModelDataManager.requestModelDataRefresh(this);
                Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("design_1")) {
            design_1 = readInteger(tag.getCompound("design_1"));
            if (!Objects.equals(oldDesign_1, design_1)) {
                ModelDataManager.requestModelDataRefresh(this);
                Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("design_texture_1")) {
            designTexture_1 = readInteger(tag.getCompound("design_texture_1"));
            if (!Objects.equals(oldDesignTexture_1, designTexture_1)) {
                ModelDataManager.requestModelDataRefresh(this);
                Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("overlay_1")) {
            overlay_1 = readInteger(tag.getCompound("overlay_1"));
            if (!Objects.equals(oldOverlay_1, overlay_1)) {
                ModelDataManager.requestModelDataRefresh(this);
                Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("rotation_1")) {
            rotation_1 = readInteger(tag.getCompound("rotation_1"));
            if (!Objects.equals(oldRotation_1, rotation_1)) {
                ModelDataManager.requestModelDataRefresh(this);
                Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }

        if (tag.contains("mimic_2")) {
            mimic_2 = NBTUtil.readBlockState(tag.getCompound("mimic_2"));
            if (!Objects.equals(oldMimic_2, mimic_2)) {
                ModelDataManager.requestModelDataRefresh(this);
                Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("texture_2")) {
            texture_2 = readInteger(tag.getCompound("texture_2"));
            if (!Objects.equals(oldTexture_2, texture_2)) {
                ModelDataManager.requestModelDataRefresh(this);
                Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("design_2")) {
            design_2 = readInteger(tag.getCompound("design_2"));
            if (!Objects.equals(oldDesign_2, design_2)) {
                ModelDataManager.requestModelDataRefresh(this);
                Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("design_texture_2")) {
            designTexture_2 = readInteger(tag.getCompound("design_texture_2"));
            if (!Objects.equals(oldDesignTexture_2, designTexture_2)) {
                ModelDataManager.requestModelDataRefresh(this);
                Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("overlay_2")) {
            overlay_2 = readInteger(tag.getCompound("overlay_2"));
            if (!Objects.equals(oldOverlay_2, overlay_2)) {
                ModelDataManager.requestModelDataRefresh(this);
                Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("rotation_2")) {
            rotation_2 = readInteger(tag.getCompound("rotation_2"));
            if (!Objects.equals(oldRotation_2, rotation_2)) {
                ModelDataManager.requestModelDataRefresh(this);
                Objects.requireNonNull(world).notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(MIMIC_1, mimic_1)
                .withInitial(TEXTURE_1, texture_1)
                .withInitial(DESIGN_1, design_1)
                .withInitial(DESIGN_TEXTURE_1, designTexture_1)
                .withInitial(OVERLAY_1, overlay_1)
                .withInitial(ROTATION_1, rotation_1)

                .withInitial(MIMIC_2, mimic_2)
                .withInitial(TEXTURE_2, texture_2)
                .withInitial(DESIGN_2, design_2)
                .withInitial(DESIGN_TEXTURE_2, designTexture_2)
                .withInitial(OVERLAY_2, overlay_2)
                .withInitial(ROTATION_2, rotation_2)
                .build();
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT tag) {
        super.read(state, tag);
        if (tag.contains("mimic_1")) {
            mimic_1 = NBTUtil.readBlockState(tag.getCompound("mimic_1"));
        }
        if (tag.contains("texture_1")) {
            texture_1 = readInteger(tag.getCompound("texture_1"));
        }
        if (tag.contains("design_1")) {
            design_1 = readInteger(tag.getCompound("design_1"));
        }
        if (tag.contains("design_texture_1")) {
            designTexture_1 = readInteger(tag.getCompound("design_texture_1"));
        }
        if (tag.contains("overlay_1")) {
            overlay_1 = readInteger(tag.getCompound("overlay_1"));
        }
        if (tag.contains("rotation_1")) {
            rotation_1 = readInteger(tag.getCompound("rotation_1"));
        }

        if (tag.contains("mimic_2")) {
            mimic_2 = NBTUtil.readBlockState(tag.getCompound("mimic_2"));
        }
        if (tag.contains("texture_2")) {
            texture_2 = readInteger(tag.getCompound("texture_2"));
        }
        if (tag.contains("design_2")) {
            design_2 = readInteger(tag.getCompound("design_2"));
        }
        if (tag.contains("design_texture_2")) {
            designTexture_2 = readInteger(tag.getCompound("design_texture_2"));
        }
        if (tag.contains("overlay_2")) {
            overlay_2 = readInteger(tag.getCompound("overlay_2"));
        }
        if (tag.contains("rotation_2")) {
            rotation_2 = readInteger(tag.getCompound("rotation_2"));
        }
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT tag) {
        if (mimic_1 != null) {
            tag.put("mimic_1", NBTUtil.writeBlockState(mimic_1));
        }
        if (texture_1 != null) {
            tag.put("texture_1", writeInteger(texture_1));
        }
        if (design_1 != null) {
            tag.put("design_1", writeInteger(design_1));
        }
        if (designTexture_1 != null) {
            tag.put("design_texture_1", writeInteger(designTexture_1));
        }
        if (overlay_1 != null) {
            tag.put("overlay_1", writeInteger(overlay_1));
        }
        if (rotation_1 != null) {
            tag.put("rotation_1", writeInteger(rotation_1));
        }

        if (mimic_2 != null) {
            tag.put("mimic_2", NBTUtil.writeBlockState(mimic_2));
        }
        if (texture_2 != null) {
            tag.put("texture_2", writeInteger(texture_2));
        }
        if (design_2 != null) {
            tag.put("design_2", writeInteger(design_2));
        }
        if (designTexture_2 != null) {
            tag.put("design_texture_2", writeInteger(designTexture_2));
        }
        if (overlay_2 != null) {
            tag.put("overlay_2", writeInteger(overlay_2));
        }
        if (rotation_2 != null) {
            tag.put("rotation_2", writeInteger(rotation_2));
        }
        return super.write(tag);
    }

    public void clear() {
        this.setMimic_1(null);
        this.setDesign_1(0);
        this.setDesign_1(0);
        this.setDesign_1(0);
        this.setOverlay_1(0);
        this.setRotation_1(0);

        this.setMimic_2(null);
        this.setDesign_2(0);
        this.setDesign_2(0);
        this.setDesign_2(0);
        this.setOverlay_2(0);
        this.setRotation_2(0);
    }
}
//========SOLI DEO GLORIA========//