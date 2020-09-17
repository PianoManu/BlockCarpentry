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

import static mod.pianomanu.blockcarpentry.setup.Registration.BED_FRAME_TILE;

/**
 * TileEntity for frame beds, you can customize both pillow and blanket
 *
 * @author PianoManu
 * @version 1.1 09/17/20
 */
public class BedFrameTile extends TileEntity {
    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
    public static final ModelProperty<Integer> TEXTURE = new ModelProperty<>();
    public static final ModelProperty<Integer> PILLOW = new ModelProperty<>();
    public static final ModelProperty<Integer> BLANKET = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_TEXTURE = new ModelProperty<>();

    public final int maxTextures = 6;
    public final int maxDesigns = 4;

    private BlockState mimic;
    private Integer texture = 0;
    private Integer pillowColor = 0;
    private Integer blanketColor = 0;
    private Integer design = 0;
    private Integer designTexture = 0;

    private static final Logger LOGGER = LogManager.getLogger();

    public BedFrameTile() {
        super(BED_FRAME_TILE.get());
    }

    public void setMimic(BlockState mimic) {
        this.mimic = mimic;
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public BlockState getMimic() {
        return this.mimic;
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

    public Integer getPillowColor() {
        return this.pillowColor;
    }

    public void setPillowColor(Integer pillowColor) {
        this.pillowColor = pillowColor;
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getBlanketColor() {
        return this.blanketColor;
    }

    public void setBlanketColor(Integer blanketColor) {
        this.blanketColor = blanketColor;
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
        return tag;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState oldMimic = mimic;
        Integer oldTexture = texture;
        Integer oldPillow = pillowColor;
        Integer oldBlanket = blanketColor;
        Integer oldDesign = design;
        Integer oldDesignTexture = designTexture;
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
        if (tag.contains("blanket")) {
            blanketColor = readInteger(tag.getCompound("blanket"));
            if (!Objects.equals(oldBlanket, blanketColor)) {
                ModelDataManager.requestModelDataRefresh(this);
                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("pillow")) {
            pillowColor = readInteger(tag.getCompound("pillow"));
            if (!Objects.equals(oldPillow, pillowColor)) {
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
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(MIMIC, mimic)
                .withInitial(TEXTURE, texture)
                .withInitial(BLANKET, blanketColor)
                .withInitial(PILLOW, pillowColor)
                .withInitial(DESIGN, design)
                .withInitial(DESIGN_TEXTURE, designTexture)
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
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        if (mimic != null) {
            tag.put("mimic", NBTUtil.writeBlockState(mimic));
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
        return tag;
    }

    private static CompoundNBT writeInteger(Integer tag) {
        CompoundNBT compoundnbt = new CompoundNBT();
        compoundnbt.putString("number", tag.toString());
        return compoundnbt;
    }

    public void clear() {
        this.setMimic(null);
        this.setTexture(0);
        this.setBlanketColor(0);
        this.setPillowColor(0);
        this.setDesign(0);
        this.setDesignTexture(0);
    }

    public Integer getTexture() {
        return texture;
    }

    public void setTexture(Integer texture) {
        this.texture = texture;
    }
}
//========SOLI DEO GLORIA========//