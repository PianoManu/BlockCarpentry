package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.block.ChestFrameBlock;
import mod.pianomanu.blockcarpentry.container.ChestFrameContainer;
import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * BlockEntity for {@link mod.pianomanu.blockcarpentry.block.ChestFrameBlock} and all sorts of frame/illusion chest blocks
 * Contains all information about the block and the mimicked block, as well as the inventory size and stored items
 *
 * @author PianoManu
 * @version 1.0 08/15/21
 */
public class ChestFrameBlockEntity extends ChestBlockEntity {

    private NonNullList<ItemStack> chestContents = NonNullList.withSize(27, ItemStack.EMPTY);
    /**
     * The number of players currently using this chest
     */
    protected int numPlayersUsing;
    private final IItemHandlerModifiable items = createHandler();
    private LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);

    public ChestFrameBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public ChestFrameBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.CHEST_FRAME_TILE.get(), pos, state);
    }

    /*public ChestFrameBlockEntity() {
        this(Registration.CHEST_FRAME_TILE.get());
    }*/

    public static void swapContents(ChestFrameBlockEntity te, ChestFrameBlockEntity otherTe) {
        NonNullList<ItemStack> list = te.getItems();
        te.setItems(otherTe.getItems());
        otherTe.setItems(list);
    }

    private static Integer readInteger(CompoundTag tag) {
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

    private static CompoundTag writeInteger(Integer tag) {
        CompoundTag compoundnbt = new CompoundTag();
        compoundnbt.putString("number", tag.toString());
        return compoundnbt;
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.chestContents;
    }

    @Override
    public void setItems(NonNullList<ItemStack> itemsIn) {
        this.chestContents = itemsIn;
    }

    @Override
    public TranslatableComponent getCustomName() {
        return new TranslatableComponent("container.frame_chest");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_59637_, Inventory inventory, Player player) {
        return super.createMenu(p_59637_, inventory, player);
    }

    public static final ModelProperty<Integer> ROTATION = new ModelProperty<>();
    private Integer rotation = 0;

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return ChestFrameContainer.createFrameContainerMenu(id, inventory, this);
    }

    /*@Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }*/

    private void playSound(SoundEvent sound) {
        double dx = (double) this.worldPosition.getX() + 0.5D;
        double dy = (double) this.worldPosition.getY() + 0.5D;
        double dz = (double) this.worldPosition.getZ() + 0.5D;
        this.level.playSound(null, dx, dy, dz, sound, SoundSource.BLOCKS, 0.5f,
                this.level.random.nextFloat() * 0.1f + 0.9f);
    }

    /*public static int getOpenCount(BlockGetter getter, BlockPos pos) {
        BlockState blockstate = getter.getBlockState(pos);
        if (blockstate.hasBlockEntity()) {
            BlockEntity tileentity = getter.getBlockEntity(pos);
            if (tileentity instanceof ChestFrameBlockEntity) {
                return ((ChestFrameBlockEntity) tileentity).numPlayersUsing;
            }
        }
        return 0;
    }*/

    @Override
    public void startOpen(Player player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.onOpenOrClose();
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!player.isSpectator()) {
            --this.numPlayersUsing;
            this.onOpenOrClose();
        }
    }

    protected void onOpenOrClose() {
        Block block = this.getBlockState().getBlock();
        if (block instanceof ChestFrameBlock) {
            this.level.blockEvent(this.worldPosition, block, 1, this.numPlayersUsing);
            this.level.updateNeighborsAt(this.worldPosition, block);
        }
    }

    private IItemHandlerModifiable createHandler() {
        return new InvWrapper(this);
    }



    //=======================================FRAME STUFF=======================================//



    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
    public static final ModelProperty<Integer> TEXTURE = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_TEXTURE = new ModelProperty<>();
    //currently only for doors and trapdoors
    public static final ModelProperty<Integer> GLASS_COLOR = new ModelProperty<>();

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (this.itemHandler != null) {
            this.itemHandler.invalidate();
            this.itemHandler = null;
        }
    }

    public final int maxTextures = 8;
    public final int maxDesignTextures = 4;
    public final int maxDesigns = 4;

    private BlockState mimic;
    private Integer texture = 0;
    private Integer design = 0;
    private Integer designTexture = 0;
    private Integer glassColor = 0;

    @Override
    public CompoundTag save(CompoundTag compound) {
        //FRAME BEGIN
        if (mimic != null) {
            compound.put("mimic", NbtUtils.writeBlockState(mimic));
        }
        if (texture != null) {
            compound.put("texture", writeInteger(texture));
        }
        if (design != null) {
            compound.put("design", writeInteger(design));
        }
        if (designTexture != null) {
            compound.put("design_texture", writeInteger(designTexture));
        }
        if (glassColor != null) {
            compound.put("glass_color", writeInteger(glassColor));
        }
        if (rotation != null) {
            compound.put("rotation", writeInteger(rotation));
        }
        //FRAME END
        super.save(compound);
        if (!this.trySaveLootTable(compound)) {
            ContainerHelper.saveAllItems(compound, this.chestContents);
        }
        return compound;
    }

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        //FRAME BEGIN
        if (compound.contains("mimic")) {
            mimic = NbtUtils.readBlockState(compound.getCompound("mimic"));
        }
        if (compound.contains("texture")) {
            texture = readInteger(compound.getCompound("texture"));
        }
        if (compound.contains("design")) {
            design = readInteger(compound.getCompound("design"));
        }
        if (compound.contains("design_texture")) {
            designTexture = readInteger(compound.getCompound("design_texture"));
        }
        if (compound.contains("glass_color")) {
            glassColor = readInteger(compound.getCompound("glass_color"));
        }
        if (compound.contains("rotation")) {
            rotation = readInteger(compound.getCompound("rotation"));
        }
        //FRAME END
        this.chestContents = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compound)) {
            ContainerHelper.loadAllItems(compound, this.chestContents);
        }
    }

    public BlockState getMimic() {
        return this.mimic;
    }

    public Integer getDesign() {
        return this.design;
    }

    public void setMimic(BlockState mimic) {
        this.mimic = mimic;
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getDesignTexture() {
        return this.designTexture;
    }

    public void setDesign(Integer design) {
        this.design = design;
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getTexture() {
        return this.texture;
    }

    public void setDesignTexture(Integer designTexture) {
        this.designTexture = designTexture;
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getGlassColor() {
        return this.glassColor;
    }

    public void setTexture(Integer texture) {
        this.texture = texture;
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setGlassColor(Integer colorNumber) {
        this.glassColor = colorNumber;
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
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
        if (design != null) {
            tag.put("design", writeInteger(design));
        }
        if (designTexture != null) {
            tag.put("design_texture", writeInteger(designTexture));
        }
        if (glassColor != null) {
            tag.put("glass_color", writeInteger(glassColor));
        }
        if (rotation != null) {
            tag.put("rotation", writeInteger(rotation));
        }
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        BlockState oldMimic = mimic;
        Integer oldTexture = texture;
        Integer oldDesign = design;
        Integer oldDesignTexture = designTexture;
        Integer oldGlassColor = glassColor;
        Integer oldRotation = rotation;
        CompoundTag tag = pkt.getTag();
        if (tag.contains("mimic")) {
            mimic = NbtUtils.readBlockState(tag.getCompound("mimic"));
            if (!Objects.equals(oldMimic, mimic)) {
                ModelDataManager.requestModelDataRefresh(this);
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("texture")) {
            texture = readInteger(tag.getCompound("texture"));
            if (!Objects.equals(oldTexture, texture)) {
                ModelDataManager.requestModelDataRefresh(this);
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("design")) {
            design = readInteger(tag.getCompound("design"));
            if (!Objects.equals(oldDesign, design)) {
                ModelDataManager.requestModelDataRefresh(this);
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("design_texture")) {
            designTexture = readInteger(tag.getCompound("design_texture"));
            if (!Objects.equals(oldDesignTexture, designTexture)) {
                ModelDataManager.requestModelDataRefresh(this);
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("glass_color")) {
            glassColor = readInteger(tag.getCompound("glass_color"));
            if (!Objects.equals(oldGlassColor, glassColor)) {
                ModelDataManager.requestModelDataRefresh(this);
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
        if (tag.contains("rotation")) {
            rotation = readInteger(tag.getCompound("rotation"));
            if (!Objects.equals(oldRotation, rotation)) {
                ModelDataManager.requestModelDataRefresh(this);
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
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
                .withInitial(ROTATION, rotation)
                .build();
    }

    public void clear() {
        this.setMimic(null);
        this.setDesign(0);
        this.setDesign(0);
        this.setDesign(0);
        this.setGlassColor(0);
        this.setRotation(0);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 1, getUpdateTag());
    }

    /*@Override
    public void remove() {
        super.remove();
        if(itemHandler != null) {
            itemHandler.invalidate();
        }
    }*/
}
//========SOLI DEO GLORIA========//