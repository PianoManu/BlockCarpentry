package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.block.ChestFrameBlock;
import mod.pianomanu.blockcarpentry.container.ChestFrameContainer;
import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * BlockEntity for {@link mod.pianomanu.blockcarpentry.block.ChestFrameBlock} and all sorts of frame/illusion chest blocks
 * Contains all information about the block and the mimicked block, as well as the inventory size and stored items
 *
 * @author PianoManu
 * @version 1.4 09/27/23
 */
public class ChestFrameBlockEntity extends ChestBlockEntity implements IFrameTile {

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
    public Component getCustomName() {
        return Component.translatable("container.frame_chest");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_59637_, Inventory inventory, Player player) {
        return super.createMenu(p_59637_, inventory, player);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return ChestFrameContainer.createFrameContainerMenu(id, inventory, this);
    }

    public static final ModelProperty<Integer> ROTATION = new ModelProperty<>();
    public BlockState mimic;
    public Integer texture = 0;

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
    public Integer design = 0;

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
    public Integer designTexture = 0;
    public Integer glassColor = 0;
    public Integer overlay = 0;
    public Integer rotation = 0;
    public Float friction = Registration.FRAMEBLOCK.get().getFriction();
    public Float explosionResistance = Registration.FRAMEBLOCK.get().getExplosionResistance();
    public Boolean canSustainPlant = false;
    public Integer enchantPowerBonus = 0;

    @Override
    public void startOpen(Player player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.onInteraction(true);
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!player.isSpectator()) {
            --this.numPlayersUsing;
            this.onInteraction(false);
        }
    }

    protected void onInteraction(boolean startOpen) {
        Block block = this.getBlockState().getBlock();
        if (block instanceof ChestFrameBlock) {
            this.level.blockEvent(this.worldPosition, block, 1, this.numPlayersUsing);
            this.level.playSound(null, this.worldPosition, startOpen ? SoundEvents.BARREL_OPEN : SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 1f, 1f);
            this.level.updateNeighborsAt(this.worldPosition, block);
        }
    }

    public <V> V set(V newValue) {
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
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
    public Float getFriction() {
        return this.friction;
    }

    @Override
    public void setFriction(Float friction) {
        this.friction = set(friction);
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
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        onDataPacket(pkt, ChestFrameBlockEntity.class, level, this.worldPosition, getBlockState());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        return getUpdateTag(tag, ChestFrameBlockEntity.class);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        //FRAME BEGIN
        IFrameTile.super.load(tag, ChestFrameBlockEntity.class);
        //FRAME END
        this.chestContents = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.chestContents);
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        //FRAME BEGIN
        IFrameTile.super.saveAdditional(tag, ChestFrameBlockEntity.class);
        //FRAME END
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.chestContents);
        }
    }

    @Nonnull
    @Override
    public ModelData getModelData() {
        return ModelData.builder()
                .with(MIMIC, mimic)
                .with(TEXTURE, texture)
                .with(DESIGN, design)
                .with(DESIGN_TEXTURE, designTexture)
                .with(GLASS_COLOR, glassColor)
                .with(ROTATION, rotation)
                .build();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
//========SOLI DEO GLORIA========//