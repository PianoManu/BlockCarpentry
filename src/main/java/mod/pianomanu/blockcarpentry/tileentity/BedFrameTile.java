package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static mod.pianomanu.blockcarpentry.setup.Registration.BED_FRAME_TILE;

/**
 * TileEntity for frame beds, you can customize both pillow and blanket
 *
 * @author PianoManu
 * @version 1.3 09/27/23
 */
public class BedFrameTile extends TileEntity implements IFrameTile {
    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
    public static final ModelProperty<Integer> TEXTURE = new ModelProperty<>();
    public static final ModelProperty<Integer> PILLOW = new ModelProperty<>();
    public static final ModelProperty<Integer> BLANKET = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_TEXTURE = new ModelProperty<>();
    public static final ModelProperty<Integer> ROTATION = new ModelProperty<>();

    public final int maxTextures = 6;
    public final int maxDesigns = 4;

    public static final List<FrameBlockTile.TagPacket<?>> TAG_PACKETS = initTagPackets();
    public BlockState mimic;
    public Integer texture = 0;
    public Integer pillowColor = 0;
    public Integer blanketColor = 0;
    public Integer design = 0;
    public Integer designTexture = 0;
    public Integer rotation = 0;
    public Float friction = Registration.FRAMEBLOCK.get().getSlipperiness();
    public Float explosionResistance = Registration.FRAMEBLOCK.get().getExplosionResistance();
    public Boolean canEntityDestroy = true;

    private static List<FrameBlockTile.TagPacket<?>> initTagPackets() {
        List<FrameBlockTile.TagPacket<?>> packets = new ArrayList<>(IFrameTile.TAG_PACKETS);
        packets.add(new FrameBlockTile.TagPacket<>("pillowColor", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("blanketColor", Integer.class, 0));
        return packets;
    }

    public BedFrameTile() {
        super(BED_FRAME_TILE.get());
    }

    public <V> V set(V newValue) {
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
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

    public Float getSlipperiness() {
        return friction;
    }

    public void setSlipperiness(Float friction) {
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
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        onDataPacket(pkt, BedFrameTile.class, world, this.pos, getBlockState());
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
                .withInitial(ROTATION, rotation)
                .build();
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        return getUpdateTag(tag, BedFrameTile.class);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        IFrameTile.super.read(tag, BedFrameTile.class);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        IFrameTile.super.write(tag, BedFrameTile.class);
        return tag;
    }
}
//========SOLI DEO GLORIA========//