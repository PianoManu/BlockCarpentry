package mod.pianomanu.blockcarpentry.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static mod.pianomanu.blockcarpentry.setup.Registration.BED_FRAME_TILE;

/**
 * BlockEntity for frame beds, you can customize both pillow and blanket
 *
 * @author PianoManu
 * @version 1.1 11/07/22
 */
public class BedFrameTile extends BlockEntity {
    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
    public static final ModelProperty<Integer> TEXTURE = new ModelProperty<>();
    public static final ModelProperty<Integer> PILLOW = new ModelProperty<>();
    public static final ModelProperty<Integer> BLANKET = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_TEXTURE = new ModelProperty<>();
    public static final ModelProperty<Integer> ROTATION = new ModelProperty<>();

    public final int maxTextures = 6;
    public final int maxDesigns = 4;

    private BlockState mimic;
    private Integer texture = 0;
    private Integer pillowColor = 0;
    private Integer blanketColor = 0;
    private Integer design = 0;
    private Integer designTexture = 0;
    private Integer rotation = 0;

    private static final Logger LOGGER = LogManager.getLogger();

    public BedFrameTile(BlockPos pos, BlockState state) {
        super(BED_FRAME_TILE.get(), pos, state);
    }

    private static Integer readInteger(CompoundTag tag) {
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

    public BlockState getMimic() {
        return this.mimic;
    }

    private static CompoundTag writeInteger(Integer tag) {
        CompoundTag compoundnbt = new CompoundTag();
        compoundnbt.putString("number", tag.toString());
        return compoundnbt;
    }

    public Integer getPillowColor() {
        return this.pillowColor;
    }

    public void setMimic(BlockState mimic) {
        this.mimic = mimic;
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
    }

    public Integer getBlanketColor() {
        return this.blanketColor;
    }

    public void setPillowColor(Integer pillowColor) {
        this.pillowColor = pillowColor;
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
    }

    public Integer getDesign() {
        return this.design;
    }

    public void setBlanketColor(Integer blanketColor) {
        this.blanketColor = blanketColor;
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
    }

    public Integer getDesignTexture() {
        return this.designTexture;
    }

    public void setDesign(Integer design) {
        this.design = design;
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
    }

    public Integer getTexture() {
        return texture;
    }

    public void setTexture(Integer texture) {
        this.texture = texture;
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    public void setDesignTexture(Integer designTexture) {
        this.designTexture = designTexture;
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    //TODO
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        BlockState oldMimic = mimic;
        Integer oldTexture = texture;
        Integer oldPillow = pillowColor;
        Integer oldBlanket = blanketColor;
        Integer oldDesign = design;
        Integer oldDesignTexture = designTexture;
        Integer oldRotation = rotation;
        CompoundTag tag = pkt.getTag();
        if (tag.contains("mimic")) {
            mimic = NbtUtils.readBlockState(tag.getCompound("mimic"));
            if (!Objects.equals(oldMimic, mimic)) {
                this.requestModelDataUpdate();
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
            }
        }
        if (tag.contains("texture")) {
            texture = readInteger(tag.getCompound("texture"));
            if (!Objects.equals(oldTexture, texture)) {
                this.requestModelDataUpdate();
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
            }
        }
        if (tag.contains("blanket")) {
            blanketColor = readInteger(tag.getCompound("blanket"));
            if (!Objects.equals(oldBlanket, blanketColor)) {
                this.requestModelDataUpdate();
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
            }
        }
        if (tag.contains("pillow")) {
            pillowColor = readInteger(tag.getCompound("pillow"));
            if (!Objects.equals(oldPillow, pillowColor)) {
                this.requestModelDataUpdate();
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
            }
        }
        if (tag.contains("design")) {
            design = readInteger(tag.getCompound("design"));
            if (!Objects.equals(oldDesign, design)) {
                this.requestModelDataUpdate();
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
            }
        }
        if (tag.contains("design_texture")) {
            designTexture = readInteger(tag.getCompound("design_texture"));
            if (!Objects.equals(oldDesignTexture, designTexture)) {
                this.requestModelDataUpdate();
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
            }
        }
        if (tag.contains("rotation")) {
            rotation = readInteger(tag.getCompound("rotation"));
            if (!Objects.equals(oldRotation, rotation)) {
                this.requestModelDataUpdate();
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
            }
        }
    }

    @Nonnull
    @Override
    public ModelData getModelData() {
        return ModelData.builder()
                .with(MIMIC, mimic)
                .with(TEXTURE, texture)
                .with(BLANKET, blanketColor)
                .with(PILLOW, pillowColor)
                .with(DESIGN, design)
                .with(DESIGN_TEXTURE, designTexture)
                .with(ROTATION, rotation)
                .build();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        if (mimic != null) {
            tag.put("mimic", NbtUtils.writeBlockState(mimic));
        }
        if (texture != null) {
            tag.put("texture", writeInteger(texture));
        }
        if (blanketColor != null) {
            tag.put("blanket", writeInteger(blanketColor));
        }
        if (pillowColor != null) {
            tag.put("pillow", writeInteger(pillowColor));
        }
        if (design != null) {
            tag.put("design", writeInteger(design));
        }
        if (designTexture != null) {
            tag.put("design_texture", writeInteger(designTexture));
        }
        if (rotation != null) {
            tag.put("rotation", writeInteger(rotation));
        }
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("mimic")) {
            mimic = NbtUtils.readBlockState(tag.getCompound("mimic"));
        }
        if (tag.contains("texture")) {
            texture = readInteger(tag.getCompound("texture"));
        }
        if (tag.contains("blanket")) {
            blanketColor = readInteger(tag.getCompound("blanket"));
        }
        if (tag.contains("pillow")) {
            pillowColor = readInteger(tag.getCompound("pillow"));
        }
        if (tag.contains("design")) {
            design = readInteger(tag.getCompound("design"));
        }
        if (tag.contains("design_texture")) {
            designTexture = readInteger(tag.getCompound("design_texture"));
        }
        if (tag.contains("rotation")) {
            rotation = readInteger(tag.getCompound("rotation"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (mimic != null) {
            tag.put("mimic", NbtUtils.writeBlockState(mimic));
        }
        if (texture != null) {
            tag.put("texture", writeInteger(texture));
        }
        if (blanketColor != null) {
            tag.put("blanket", writeInteger(blanketColor));
        }
        if (pillowColor != null) {
            tag.put("pillow", writeInteger(pillowColor));
        }
        if (design != null) {
            tag.put("design", writeInteger(design));
        }
        if (designTexture != null) {
            tag.put("design_texture", writeInteger(designTexture));
        }
        if (rotation != null) {
            tag.put("rotation", writeInteger(rotation));
        }
    }

    public void clear() {
        this.setMimic(null);
        this.setTexture(0);
        this.setBlanketColor(0);
        this.setPillowColor(0);
        this.setDesign(0);
        this.setDesignTexture(0);
        this.setRotation(0);
    }
}
//========SOLI DEO GLORIA========//