package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.item.DyeColor;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.block.BlockState;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * TileEntity for {@link mod.pianomanu.blockcarpentry.block.StandingSignFrameBlock} and {@link mod.pianomanu.blockcarpentry.block.WallSignFrameBlock}
 * Contains all information about the block and the mimicked block
 *
 * @author PianoManu
 * @version 1.1 10/03/23
 */
public class SignFrameTile extends SignTileEntity implements IFrameTile {
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
    public Float friction = Registration.FRAMEBLOCK.get().getSlipperiness();
    public Float explosionResistance = Registration.FRAMEBLOCK.get().getExplosionResistance();
    public Boolean canSustainPlant = false;
    public Integer enchantPowerBonus = 0;
    public Boolean canEntityDestroy = true;

    public SignFrameTile() {
        super();
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public IReorderingProcessor func_242686_a(int line, Function<ITextComponent, IReorderingProcessor> text) {
        return super.func_242686_a(line, text);
    }

    @Override
    public TileEntityType<?> getType() {
        boolean isFrameBlock = this.getBlockState().getBlock().equals(Registration.STANDING_SIGN_FRAMEBLOCK.get()) || this.getBlockState().getBlock().equals(Registration.WALL_SIGN_FRAMEBLOCK.get());
        return isFrameBlock ? Registration.SIGN_FRAME_TILE.get() : Registration.SIGN_ILLUSION_TILE.get();
    }

    public <V> V set(V newValue) {
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
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
    public Float getSlipperiness() {
        return friction;
    }

    @Override
    public void setSlipperiness(Float friction) {
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

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        onDataPacket(pkt, SignFrameTile.class, world, this.pos, getBlockState());
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
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        return getUpdateTag(tag, SignFrameTile.class);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        IFrameTile.super.read(tag, SignFrameTile.class);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        IFrameTile.super.write(tag, SignFrameTile.class);
        return tag;
    }
}
//========SOLI DEO GLORIA========//