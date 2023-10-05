package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.block.ChestFrameBlock;
import mod.pianomanu.blockcarpentry.container.ChestFrameContainer;
import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.audio.SoundSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
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
 * TileEntity for {@link mod.pianomanu.blockcarpentry.block.ChestFrameBlock} and all sorts of frame/illusion chest blocks
 * Contains all information about the block and the mimicked block, as well as the inventory size and stored items
 *
 * @author PianoManu
 * @version 1.2 05/01/21
 */
public class ChestFrameTileEntity extends ChestTileEntity implements IFrameTile {

    private NonNullList<ItemStack> chestContents = NonNullList.withSize(27, ItemStack.EMPTY);
    /**
     * The number of players currently using this chest
     */
    protected int numPlayersUsing;
    public static final ModelProperty<Integer> ROTATION = new ModelProperty<>();
    private final IItemHandlerModifiable items = createHandler();
    private LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);

    public ChestFrameTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    public ChestFrameTileEntity() {
        this(Registration.CHEST_FRAME_TILE.get());
    }

    public static void swapContents(ChestFrameTileEntity te, ChestFrameTileEntity otherTe) {
        NonNullList<ItemStack> list = te.getItems();
        te.setItems(otherTe.getItems());
        otherTe.setItems(list);
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

    @Override
    public int getSizeInventory() {
        return 27;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.frame_chest");
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
    protected Container createMenu(int id, PlayerInventory player) {
        return new ChestFrameContainer(id, player, this);
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    public BlockState mimic;

    public static int getPlayersUsing(IBlockReader reader, BlockPos pos) {
        BlockState blockstate = reader.getBlockState(pos);
        if (blockstate.hasTileEntity()) {
            TileEntity tileentity = reader.getTileEntity(pos);
            if (tileentity instanceof ChestFrameTileEntity) {
                return ((ChestFrameTileEntity) tileentity).numPlayersUsing;
            }
        }
        return 0;
    }

    public Integer texture = 0;
    public Integer design = 0;

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        if (this.itemHandler != null) {
            this.itemHandler.invalidate();
            this.itemHandler = null;
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
    public Integer designTexture = 0;
    public Integer glassColor = 0;

    public final int maxTextures = 8;
    public final int maxDesignTextures = 4;
    public final int maxDesigns = 4;
    public Integer overlay = 0;
    public Integer rotation = 0;
    public Float slipperiness = Registration.FRAMEBLOCK.get().getSlipperiness();
    public Float explosionResistance = Registration.FRAMEBLOCK.get().getExplosionResistance();
    public Boolean canSustainPlant = false;
    public Integer enchantPowerBonus = 0;
    public Boolean canEntityDestroy = true;

    private void playSound(SoundEvent sound) {
        double dx = (double) this.pos.getX() + 0.5D;
        double dy = (double) this.pos.getY() + 0.5D;
        double dz = (double) this.pos.getZ() + 0.5D;
        this.world.playSound(null, dx, dy, dz, sound, SoundCategory.BLOCKS, 0.5f,
                this.world.rand.nextFloat() * 0.1f + 0.9f);
    }

    @Override
    public void openInventory(PlayerEntity player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.onOpenOrClose(true);
        }
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        if (!player.isSpectator()) {
            --this.numPlayersUsing;
            this.onOpenOrClose(false);
        }
    }

    protected void onOpenOrClose(boolean startOpen) {
        Block block = this.getBlockState().getBlock();
        if (block instanceof ChestFrameBlock) {
            this.world.addBlockEvent(this.pos, block, 1, this.numPlayersUsing);
            this.world.playSound(null, this.pos, startOpen ? SoundEvents.BLOCK_BARREL_OPEN : SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.BLOCKS, 1f, 1f);
            this.world.notifyNeighborsOfStateChange(this.pos, block);
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (this.itemHandler != null) {
            this.itemHandler.invalidate();
            this.itemHandler = null;
        }
    }

    public <V> V set(V newValue) {
        markDirty();
        world.notifyBlockUpdate(this.pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
        return newValue;
    }

    private static final Logger LOGGER = LogManager.getLogger();

    public BlockState getMimic() {
        return this.mimic;
    }

    public Integer getDesign() {
        return this.design;
    }

    public void setMimic(BlockState mimic) {
        this.mimic = set(mimic);
    }

    public Integer getDesignTexture() {
        return this.designTexture;
    }

    public void setDesign(Integer design) {
        this.design = set(design);
    }

    public Integer getTexture() {
        return this.texture;
    }

    public void setDesignTexture(Integer designTexture) {
        this.designTexture = set(designTexture);
    }

    public Integer getGlassColor() {
        return this.glassColor;
    }

    public void setTexture(Integer texture) {
        this.texture = set(texture);
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setGlassColor(Integer colorNumber) {
        this.glassColor = set(colorNumber);
    }

    public void setRotation(Integer rotation) {
        this.rotation = set(rotation);
    }

    @Override
    public Integer getOverlay() {
        return this.overlay;
    }

    @Override
    public void setOverlay(Integer overlay) {
        this.overlay = set(overlay);
    }

    @Override
    public Float getSlipperiness() {
        return this.slipperiness;
    }

    @Override
    public void setSlipperiness(Float slipperiness) {
        this.slipperiness = set(slipperiness);
    }

    @Override
    public Float getExplosionResistance() {
        return this.explosionResistance;
    }

    @Override
    public void setExplosionResistance(Float explosionResistance) {
        this.explosionResistance = set(explosionResistance);
    }

    @Override
    public Boolean getCanSustainPlant() {
        return this.canSustainPlant;
    }

    @Override
    public void setCanSustainPlant(Boolean canSustainPlant) {
        this.canSustainPlant = set(canSustainPlant);
    }

    @Override
    public Integer getEnchantPowerBonus() {
        return this.enchantPowerBonus;
    }

    @Override
    public void setEnchantPowerBonus(Integer enchantPowerBonus) {
        this.enchantPowerBonus = set(enchantPowerBonus);
    }

    @Override
    public Boolean getCanEntityDestroy() {
        return this.canEntityDestroy;
    }

    @Override
    public void setCanEntityDestroy(Boolean canEntityDestroy) {
        this.canEntityDestroy = set(canEntityDestroy);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        onDataPacket(pkt, ChestFrameTileEntity.class, world, this.pos, getBlockState());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        return getUpdateTag(tag, ChestFrameTileEntity.class);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        //FRAME BEGIN
        IFrameTile.super.read(tag, ChestFrameTileEntity.class);
        //FRAME END
        this.chestContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        //FRAME BEGIN
        IFrameTile.super.write(tag, ChestFrameTileEntity.class);
        //FRAME END
        return tag;
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

    @Override
    public void remove() {
        super.remove();
        if(itemHandler != null) {
            itemHandler.invalidate();
        }
    }
}
//========SOLI DEO GLORIA========//