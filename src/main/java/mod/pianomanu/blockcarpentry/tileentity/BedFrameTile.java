package mod.pianomanu.blockcarpentry.tileentity;

import com.google.common.collect.ImmutableMap;
import com.sun.jna.platform.win32.WinBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

public class BedFrameTile extends TileEntity {
    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
    public static final ModelProperty<BlockState> PILLOW = new ModelProperty<>();
    public static final ModelProperty<BlockState> BLANKET = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_TEXTURE = new ModelProperty<>();

    private BlockState mimic;
    private BlockState pillow;
    private BlockState blanket;
    private Integer design;
    private Integer designTexture;

    private static final Logger LOGGER = LogManager.getLogger();

    public BedFrameTile() {
        super(TileEntityType.BED);
    }

    public void setMimic(BlockState mimic) {
        this.mimic = mimic;
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public BlockState getMimic() {
        return this.mimic;
    }

    public void setPillow(BlockState pillow) {
        this.pillow = pillow;
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public BlockState getPillow() {
        return this.pillow;
    }

    public void setBlanket(BlockState blanket) {
        this.blanket = blanket;
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public BlockState getBlanket() {
        return this.blanket;
    }

    public void setDesign(Integer design) {
        this.design = design;
    }
    public Integer getDesign() {
        return this.design;
    }

    public void setDesignTexture(Integer designTexture) {
        this.designTexture = designTexture;
    }
    public Integer getDesignTexture() {
        return this.designTexture;
    }


    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        if (mimic != null) {
            tag.put("mimic", NBTUtil.writeBlockState(mimic));
        }
        if (blanket != null) {
            tag.put("blanket", NBTUtil.writeBlockState(blanket));
        }
        if (pillow != null) {
            tag.put("pillow", NBTUtil.writeBlockState(pillow));
        }
        if (design != null) {
            tag.put("design", writeInteger(design));
        }
        if (designTexture != null) {
            tag.put("design_Texture", writeInteger(designTexture));
        }
        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState oldMimic = mimic;
        BlockState oldPillow = pillow;
        BlockState oldBlanket = blanket;
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
        if (tag.contains("blanket")) {
            blanket = NBTUtil.readBlockState(tag.getCompound("blanket"));
            if (!Objects.equals(oldBlanket, blanket)) {
                ModelDataManager.requestModelDataRefresh(this);
                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("pillow")) {
            pillow = NBTUtil.readBlockState(tag.getCompound("pillow"));
            if (!Objects.equals(oldPillow, pillow)) {
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
            design = readInteger(tag.getCompound("design_texture"));
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
                .withInitial(BLANKET, blanket)
                .withInitial(PILLOW, pillow)
                .withProperty(DESIGN)
                .withProperty(DESIGN_TEXTURE)
                .build();
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        if (tag.contains("mimic")) {
            mimic = NBTUtil.readBlockState(tag.getCompound("mimic"));
        }
        if (tag.contains("blanket")) {
            blanket = NBTUtil.readBlockState(tag.getCompound("blanket"));
        }
        if (tag.contains("pillow")) {
            pillow = NBTUtil.readBlockState(tag.getCompound("pillow"));
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
        if (mimic != null) {
            tag.put("mimic", NBTUtil.writeBlockState(mimic));
        }
        if (blanket != null) {
            tag.put("blanket", NBTUtil.writeBlockState(blanket));
        }
        if (pillow != null) {
            tag.put("pillow", NBTUtil.writeBlockState(pillow));
        }
        if (design != null) {
            tag.put("design", writeInteger(design));
        }
        if (designTexture != null) {
            tag.put("design_texture", writeInteger(designTexture));
        }
        return super.write(tag);
    }

    public void clear() {
        this.setMimic(null);
        this.setBlanket(null);
        this.setPillow(null);
    }

    private static CompoundNBT writeInteger(Integer tag) {
        CompoundNBT compoundnbt = new CompoundNBT();
        compoundnbt.putString("number", tag.toString());
        return compoundnbt;
    }

    private static Integer readInteger(CompoundNBT tag) {
        if (!tag.contains("number", 8)) {
            return 0;
        } else {
            try {
                return Integer.parseInt(tag.getString("number"));
            } catch (NumberFormatException e) {
                LOGGER.error("Not a valid Number Format: "+tag.getString("number"));
                return 0;
            }
        }
    }
}
