package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static mod.pianomanu.blockcarpentry.setup.Registration.SLAB_FRAME_TILE;

/**
 * BlockEntity for {@link mod.pianomanu.blockcarpentry.block.SixWaySlabFrameBlock} and slopes (not yet implemented)
 * Contains all information about the block and the mimicked block
 *
 * @author PianoManu
 * @version 1.3 09/27/23
 */
public class TwoBlocksFrameBlockTile extends BlockEntity implements IFrameTile {
    public static final ModelProperty<BlockState> MIMIC_1 = new ModelProperty<>();
    public static final ModelProperty<Integer> TEXTURE_1 = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_1 = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_TEXTURE_1 = new ModelProperty<>();
    public static final ModelProperty<Integer> OVERLAY_1 = new ModelProperty<>();
    public static final ModelProperty<Integer> ROTATION_1 = new ModelProperty<>();

    public static final ModelProperty<BlockState> MIMIC_2 = new ModelProperty<>();
    public static final ModelProperty<Integer> TEXTURE_2 = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_2 = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_TEXTURE_2 = new ModelProperty<>();
    public static final ModelProperty<Integer> OVERLAY_2 = new ModelProperty<>();
    public static final ModelProperty<Integer> ROTATION_2 = new ModelProperty<>();

    public static final ModelProperty<Boolean> NORTH_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> EAST_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> SOUTH_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> WEST_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> UP_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> DOWN_VISIBLE = new ModelProperty<>();

    private static final Logger LOGGER = LogManager.getLogger();
    public final int maxTextures = 8;
    public final int maxDesignTextures = 4;
    public final int maxDesigns = 4;
    public BlockState mimic;
    public Integer texture = 0;
    public Integer design = 0;
    public Integer designTexture = 0;
    public Integer overlay = 0;
    public Integer rotation = 0;
    public Float friction = Registration.FRAMEBLOCK.get().getFriction();
    public Float explosionResistance = Registration.FRAMEBLOCK.get().getExplosionResistance();
    public Boolean canSustainPlant = false;
    public Integer enchantPowerBonus = 0;
    public Boolean canEntityDestroy = true;
    public BlockState mimic_2;
    public Integer texture_2 = 0;
    public Integer design_2 = 0;
    public Integer designTexture_2 = 0;
    public Integer overlay_2 = 0;
    public Integer rotation_2 = 0;
    public Float friction_2 = Registration.FRAMEBLOCK.get().getFriction();
    public Float explosionResistance_2 = Registration.FRAMEBLOCK.get().getExplosionResistance();
    public Boolean canSustainPlant_2 = false;
    public Integer enchantPowerBonus_2 = 0;

    public Boolean northVisible = true;
    public Boolean eastVisible = true;
    public Boolean southVisible = true;
    public Boolean westVisible = true;
    public Boolean upVisible = true;
    public Boolean downVisible = true;

    List<FrameBlockTile.TagPacket<?>> TAG_PACKETS = initTagPackets();

    private static List<FrameBlockTile.TagPacket<?>> initTagPackets() {
        List<FrameBlockTile.TagPacket<?>> packets = IFrameTile.TAG_PACKETS;
        packets.add(new FrameBlockTile.TagPacket<>("mimic_2", BlockState.class, Blocks.AIR.defaultBlockState()));
        packets.add(new FrameBlockTile.TagPacket<>("texture_2", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("design_2", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("designTexture_2", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("glassColor_2", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("overlay_2", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("rotation_2", Integer.class, 0));
        return packets;
    }

    public TwoBlocksFrameBlockTile(BlockPos pos, BlockState state) {
        super(SLAB_FRAME_TILE.get(), pos, state);
    }

    public <V> V set(V newValue) {
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
        return newValue;
    }

    public BlockState getMimic() {
        return mimic;
    }

    public void setMimic(BlockState mimic) {
        this.mimic = set(mimic);
    }

    public Integer getTexture() {
        return texture;
    }

    public void setTexture(Integer texture) {
        this.texture = set(texture);
    }

    public Integer getDesign() {
        return design;
    }

    public void setDesign(Integer design) {
        this.design = set(design);
    }

    public Integer getDesignTexture() {
        return designTexture;
    }

    public void setDesignTexture(Integer designTexture) {
        this.designTexture = set(designTexture);
    }

    @Override
    public Integer getGlassColor() {
        return 0;
    }

    @Override
    public void setGlassColor(Integer glassColor) {

    }

    public Integer getOverlay() {
        return overlay;
    }

    public void setOverlay(Integer overlay) {
        this.overlay = set(overlay);
    }

    public Integer getRotation() {
        return rotation;
    }

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
        return this.canEntityDestroy;
    }

    @Override
    public void setCanEntityDestroy(Boolean canEntityDestroy) {
        this.canEntityDestroy = set(canEntityDestroy);
    }

    public BlockState getMimic_2() {
        return mimic_2;
    }

    public void setMimic_2(BlockState mimic_2) {
        this.mimic_2 = set(mimic_2);
    }

    public Integer getTexture_2() {
        return texture_2;
    }

    public void setTexture_2(Integer texture_2) {
        this.texture_2 = set(texture_2);
    }

    public Integer getDesign_2() {
        return design_2;
    }

    public void setDesign_2(Integer design_2) {
        this.design_2 = set(design_2);
    }

    public Integer getDesignTexture_2() {
        return designTexture_2;
    }

    public void setDesignTexture_2(Integer designTexture_2) {
        this.designTexture_2 = set(designTexture_2);
    }

    public Integer getOverlay_2() {
        return overlay_2;
    }

    public void setOverlay_2(Integer overlay_2) {
        this.overlay_2 = set(overlay_2);
    }

    public Integer getRotation_2() {
        return rotation_2;
    }

    public void setRotation_2(Integer rotation_2) {
        this.rotation_2 = set(rotation_2);
    }

    public Float getFriction_2() {
        return friction_2;
    }

    public void setFriction_2(Float friction_2) {
        this.friction_2 = set(friction_2);
    }

    public Float getExplosionResistance_2() {
        return explosionResistance_2;
    }

    public void setExplosionResistance_2(Float explosionResistance_2) {
        this.explosionResistance_2 = set(explosionResistance_2);
    }

    public Boolean getCanSustainPlant_2() {
        return canSustainPlant_2;
    }

    public void setCanSustainPlant_2(Boolean canSustainPlant_2) {
        this.canSustainPlant_2 = set(canSustainPlant_2);
    }

    public Integer getEnchantPowerBonus_2() {
        return enchantPowerBonus_2;
    }

    public void setEnchantPowerBonus_2(Integer enchantPowerBonus_2) {
        this.enchantPowerBonus_2 = set(enchantPowerBonus_2);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    @Nonnull
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        return getUpdateTag(tag, TwoBlocksFrameBlockTile.class);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        onDataPacket(pkt, TwoBlocksFrameBlockTile.class, level, this.worldPosition, getBlockState());
    }

    @Nonnull
    @Override
    public ModelData getModelData() {
        return ModelData.builder()
                .with(MIMIC_1, mimic)
                .with(TEXTURE_1, texture)
                .with(DESIGN_1, design)
                .with(DESIGN_TEXTURE_1, designTexture)
                .with(OVERLAY_1, overlay)
                .with(ROTATION_1, rotation)

                .with(MIMIC_2, mimic_2)
                .with(TEXTURE_2, texture_2)
                .with(DESIGN_2, design_2)
                .with(DESIGN_TEXTURE_2, designTexture_2)
                .with(OVERLAY_2, overlay_2)
                .with(ROTATION_2, rotation_2)

                .with(NORTH_VISIBLE, northVisible)
                .with(EAST_VISIBLE, eastVisible)
                .with(SOUTH_VISIBLE, southVisible)
                .with(WEST_VISIBLE, westVisible)
                .with(UP_VISIBLE, upVisible)
                .with(DOWN_VISIBLE, downVisible)
                .build();
    }

    @Override
    public void load(@Nonnull CompoundTag tag) {
        super.load(tag);
        IFrameTile.super.load(tag, TwoBlocksFrameBlockTile.class);
        copyOutdatedData(tag);
    }

    @Override
    @Nonnull
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        IFrameTile.super.saveAdditional(tag, TwoBlocksFrameBlockTile.class);
        tag.put("mimic_2", NbtUtils.writeBlockState(Objects.requireNonNullElseGet(mimic_2, Blocks.AIR::defaultBlockState)));
    }

    public boolean applyToUpper() {
        return this.getMimic_2() != null && this.getMimic_2() != Blocks.AIR.defaultBlockState();
    }

    public void clear() {
        IFrameTile.super.clear();
        this.setMimic_2(null);
    }

    private void copyOutdatedData(CompoundTag tag) {
        if (tag.contains("mimic_1"))
            this.mimic = read(tag, "mimic_1", BlockState.class, Blocks.AIR.defaultBlockState());
        if (tag.contains("texture_1"))
            this.texture = read(tag, "texture_1", Integer.class, 0);
        if (tag.contains("design_1"))
            this.design = read(tag, "design_1", Integer.class, 0);
        if (tag.contains("design_texture_1"))
            this.designTexture = read(tag, "design_texture_1", Integer.class, 0);
        if (tag.contains("overlay_1"))
            this.overlay = read(tag, "overlay_1", Integer.class, 0);
        if (tag.contains("rotation_1"))
            this.rotation = read(tag, "rotation_1", Integer.class, 0);
    }
}
//========SOLI DEO GLORIA========//