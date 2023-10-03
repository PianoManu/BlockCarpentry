package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * BlockEntity for {@link mod.pianomanu.blockcarpentry.block.StandingSignFrameBlock} and {@link mod.pianomanu.blockcarpentry.block.WallSignFrameBlock}
 * Contains all information about the block and the mimicked block
 *
 * @author PianoManu
 * @version 1.1 10/03/23
 */
public class SignFrameTile extends SignBlockEntity implements IFrameTile {
    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
    public static final ModelProperty<Integer> TEXTURE = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_TEXTURE = new ModelProperty<>();
    public static final ModelProperty<Integer> GLASS_COLOR = new ModelProperty<>();
    public static final ModelProperty<Integer> OVERLAY = new ModelProperty<>();
    public static final ModelProperty<Integer> ROTATION = new ModelProperty<>();

    public BlockState mimic;
    public Integer texture = 0;
    public Integer design = 0;
    public Integer designTexture = 0;
    public Integer glassColor = 0;
    public Integer overlay = 0;
    public Integer rotation = 0;
    public Float friction = Registration.FRAMEBLOCK.get().getFriction();
    public Float explosionResistance = Registration.FRAMEBLOCK.get().getExplosionResistance();
    public Boolean canSustainPlant = false;
    public Integer enchantPowerBonus = 0;
    public Boolean canEntityDestroy = true;

    public DyeColor color = DyeColor.BLACK;
    public Boolean hasGlowingText;

    public SignFrameTile(BlockPos pos, BlockState state) {
        super(pos, state);
        //super(state.getBlock().equals(Registration.STANDING_SIGN_FRAMEBLOCK.get()) || state.getBlock().equals(Registration.WALL_SIGN_FRAMEBLOCK.get()) ? Registration.SIGN_FRAME_TILE.get() : Registration.SIGN_ILLUSION_TILE.get(), pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        boolean isFrameBlock = this.getBlockState().getBlock().equals(Registration.STANDING_SIGN_FRAMEBLOCK.get()) || this.getBlockState().getBlock().equals(Registration.WALL_SIGN_FRAMEBLOCK.get());
        return isFrameBlock ? Registration.SIGN_FRAME_TILE.get() : Registration.SIGN_ILLUSION_TILE.get();
    }

    public <V> V set(V newValue) {
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
        return newValue;
    }

    @Override
    public BlockState getMimic() {
        return mimic;
    }

    @Override
    public void setMimic(BlockState mimic) {
        this.mimic = set(mimic);
    }

    @Override
    public Integer getTexture() {
        return texture;
    }

    @Override
    public void setTexture(Integer texture) {
        this.texture = set(texture);
    }

    @Override
    public Integer getDesign() {
        return design;
    }

    @Override
    public void setDesign(Integer design) {
        this.design = set(design);
    }

    @Override
    public Integer getDesignTexture() {
        return designTexture;
    }

    @Override
    public void setDesignTexture(Integer designTexture) {
        this.designTexture = set(designTexture);
    }

    @Override
    public Integer getGlassColor() {
        return glassColor;
    }

    @Override
    public void setGlassColor(Integer glassColor) {
        this.glassColor = set(glassColor);
    }

    @Override
    public Integer getOverlay() {
        return overlay;
    }

    @Override
    public void setOverlay(Integer overlay) {
        this.overlay = set(overlay);
    }

    @Override
    public Integer getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(Integer rotation) {
        this.rotation = set(rotation);
    }

    @Override
    public Float getFriction() {
        return friction;
    }

    @Override
    public void setFriction(Float friction) {
        this.friction = set(friction);
    }

    @Override
    public Float getExplosionResistance() {
        return explosionResistance;
    }

    @Override
    public void setExplosionResistance(Float explosionResistance) {
        this.explosionResistance = set(explosionResistance);
    }

    @Override
    public Boolean getCanSustainPlant() {
        return canSustainPlant;
    }

    @Override
    public void setCanSustainPlant(Boolean canSustainPlant) {
        this.canSustainPlant = set(canSustainPlant);
    }

    @Override
    public Integer getEnchantPowerBonus() {
        return enchantPowerBonus;
    }

    @Override
    public void setEnchantPowerBonus(Integer enchantPowerBonus) {
        this.enchantPowerBonus = set(enchantPowerBonus);
    }

    @Override
    public Boolean getCanEntityDestroy() {
        return canEntityDestroy;
    }

    @Override
    public void setCanEntityDestroy(Boolean canEntityDestroy) {
        this.canEntityDestroy = set(canEntityDestroy);
    }

    public boolean setColor(DyeColor color) {
        if (color != this.getColor()) {
            this.color = color;
            this.setChanged();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public DyeColor getColor() {
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        return color;
    }

    public boolean hasGlowingText() {
        return this.hasGlowingText;
    }

    public boolean setHasGlowingText(boolean hasGlowingText) {
        if (this.hasGlowingText != hasGlowingText) {
            this.hasGlowingText = hasGlowingText;
            this.setChanged();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        onDataPacket(pkt, SignFrameTile.class, level, this.worldPosition, getBlockState());
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
                .build();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        this.color = DyeColor.byName(tag.getString("Color"), DyeColor.BLACK);
        this.hasGlowingText = tag.getBoolean("GlowingText");
        return getUpdateTag(tag, SignFrameTile.class);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        IFrameTile.super.load(tag, SignFrameTile.class);
        this.color = DyeColor.byName(tag.getString("Color"), DyeColor.BLACK);
        this.hasGlowingText = tag.getBoolean("GlowingText");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        IFrameTile.super.saveAdditional(tag, SignFrameTile.class);

        tag.putString("Color", this.color.getName());
        tag.putBoolean("GlowingText", this.hasGlowingText);
    }
}
//========SOLI DEO GLORIA========//