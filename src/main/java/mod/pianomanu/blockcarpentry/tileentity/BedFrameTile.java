package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static mod.pianomanu.blockcarpentry.setup.Registration.BED_FRAME_TILE;

/**
 * BlockEntity for frame beds, you can customize both pillow and blanket
 *
 * @author PianoManu
 * @version 1.5 11/03/23
 */
public class BedFrameTile extends BlockEntity implements IFrameTile {
    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
    public static final ModelProperty<Integer> TEXTURE = new ModelProperty<>();
    public static final ModelProperty<Integer> PILLOW = new ModelProperty<>();
    public static final ModelProperty<Integer> BLANKET = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_TEXTURE = new ModelProperty<>();
    public static final ModelProperty<Integer> ROTATION = new ModelProperty<>();

    public final int maxTextures = 6;
    public final int maxDesigns = 4;

    public BlockState mimic;
    public Integer texture = 0;
    public Integer pillowColor = 0;
    public Integer blanketColor = 0;
    public Integer design = 0;
    public Integer designTexture = 0;
    public Integer rotation = 0;
    public Float friction = Registration.FRAMEBLOCK.get().getFriction();
    public Float explosionResistance = Registration.FRAMEBLOCK.get().getExplosionResistance();
    public Boolean canEntityDestroy = true;

    public BedFrameTile(BlockPos pos, BlockState state) {
        super(BED_FRAME_TILE.get(), pos, state);
    }

    public <V> V set(V newValue) {
        setChanged();
        if (level != null)
            level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
        return newValue;
    }

    public BlockState getMimic() {
        return this.mimic;
    }

    public Integer getPillowColor() {
        return this.pillowColor;
    }

    public void setMimic(BlockState mimic) {
        this.mimic = set(mimic);
    }

    public Integer getBlanketColor() {
        return this.blanketColor;
    }

    public void setPillowColor(Integer pillowColor) {
        this.pillowColor = set(pillowColor);
    }

    public Integer getDesign() {
        return this.design;
    }

    public void setBlanketColor(Integer blanketColor) {
        this.blanketColor = set(blanketColor);
    }

    public Integer getDesignTexture() {
        return this.designTexture;
    }

    public void setDesign(Integer design) {
        this.design = set(design);
    }

    public Integer getTexture() {
        return texture;
    }

    public void setTexture(Integer texture) {
        this.texture = set(texture);
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = set(rotation);
    }

    @Override
    public Integer getOverlay() {
        return 0;
    }

    @Override
    public void setOverlay(Integer newVal) {

    }

    public void setDesignTexture(Integer designTexture) {
        this.designTexture = set(designTexture);
    }

    @Override
    public Integer getGlassColor() {
        return 0;
    }

    @Override
    public void setGlassColor(Integer newVal) {

    }

    public Float getFriction() {
        return friction;
    }

    public void setFriction(Float friction) {
        this.friction = set(friction);
    }

    public Float getExplosionResistance() {
        return explosionResistance;
    }

    public void setExplosionResistance(Float explosionResistance) {
        this.explosionResistance = set(explosionResistance);
    }

    @Override
    public Boolean getCanSustainPlant() {
        return false;
    }

    @Override
    public void setCanSustainPlant(Boolean newVal) {

    }

    @Override
    public Integer getEnchantPowerBonus() {
        return 0;
    }

    @Override
    public void setEnchantPowerBonus(Integer newVal) {
    }

    @Override
    public Boolean getCanEntityDestroy() {
        return this.canEntityDestroy;
    }

    @Override
    public void setCanEntityDestroy(Boolean canEntityDestroy) {
        this.canEntityDestroy = set(canEntityDestroy);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        onDataPacket(pkt, BedFrameTile.class, level, this.worldPosition, getBlockState());
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
        return getUpdateTag(tag, BedFrameTile.class);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        IFrameTile.super.load(tag, BedFrameTile.class);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        IFrameTile.super.saveAdditional(tag, BedFrameTile.class);
    }
}
//========SOLI DEO GLORIA========//