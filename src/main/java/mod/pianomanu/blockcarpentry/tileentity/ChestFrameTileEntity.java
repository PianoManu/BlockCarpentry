package mod.pianomanu.blockcarpentry.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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

public class ChestFrameTileEntity extends LockableLootTileEntity implements IChestLid, ITickableTileEntity {

    private NonNullList<ItemStack> chestContents = NonNullList.withSize(27, ItemStack.EMPTY);
    /**
     * The current angle of the lid (between 0 and 1)
     */
    protected float lidAngle;
    /**
     * The angle of the lid last tick
     */
    protected float prevLidAngle;
    /**
     * The number of players currently using this chest
     */
    protected int numPlayersUsing;
    /**
     * A counter that is incremented once each tick. Used to determine when to recompute ; this is done every 200 ticks
     * (but staggered between different chests). However, the new value isn't actually sent to clients when it is
     * changed.
     */
    private int ticksSinceSync;
    private net.minecraftforge.common.util.LazyOptional<net.minecraftforge.items.IItemHandlerModifiable> chestHandler;

    public ChestFrameTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.frame_chest");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }

    //===========================================FRAME STUFF END
    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
    public static final ModelProperty<Integer> TEXTURE = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_TEXTURE = new ModelProperty<>();
    //currently only for doors and trapdoors
    public static final ModelProperty<Integer> GLASS_COLOR = new ModelProperty<>();

    public final int maxTextures = 8;
    public final int maxDesignTextures = 4;
    public final int maxDesigns = 4;

    private BlockState mimic;
    private Integer texture = 0;
    private Integer design = 0;
    private Integer designTexture = 0;
    private Integer glassColor = 0;

    private static final Logger LOGGER = LogManager.getLogger();

    public void setMimic(BlockState mimic) {
        this.mimic = mimic;
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public BlockState getMimic() {
        return this.mimic;
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
        Integer oldTexture = texture;
        Integer oldDesign = design;
        Integer oldDesignTexture = designTexture;
        Integer oldGlassColor = glassColor;
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

        this.chestContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(tag)) {
            ItemStackHelper.loadAllItems(tag, this.chestContents);
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

        if (!this.checkLootAndWrite(tag)) {
            ItemStackHelper.saveAllItems(tag, this.chestContents);
        }
        return super.write(tag);
    }

    public void clear() {
        this.setMimic(null);
        this.setDesign(0);
        this.setDesign(0);
        this.setDesign(0);
        this.setGlassColor(0);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return null;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {

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
                LOGGER.error("Not a valid Number Format: " + tag.getString("number"));
                return 0;
            }
        }
    }

    //===========================================FRAME STUFF END
    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public float getLidAngle(float partialTicks) {
        return 0;
    }

    @Override
    public void tick() {

    }

    /*public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);


    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);


        return compound;
    }*/
}
//========SOLI DEO GLORIA========//