package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.extensions.IForgeBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static mod.pianomanu.blockcarpentry.setup.Registration.FRAMEBLOCK_TILE;

/**
 * BlockEntity for {@link mod.pianomanu.blockcarpentry.block.FrameBlock} and all sorts of frame blocks
 * Contains all information about the block and the mimicked block
 *
 * @author PianoManu
 * @version 1.5 09/27/23
 */
public class FrameBlockTile extends BlockEntity implements IForgeBlockEntity, IFrameTile {
    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
    public static final ModelProperty<Integer> TEXTURE = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_TEXTURE = new ModelProperty<>();
    //currently only for doors and trapdoors
    public static final ModelProperty<Integer> GLASS_COLOR = new ModelProperty<>();
    public static final ModelProperty<Integer> OVERLAY = new ModelProperty<>();
    public static final ModelProperty<Integer> ROTATION = new ModelProperty<>();
    public static final ModelProperty<Boolean> NORTH_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> EAST_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> SOUTH_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> WEST_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> UP_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> DOWN_VISIBLE = new ModelProperty<>();

    public final int maxTextures = 8;
    public final int maxDesignTextures = 4;
    public final int maxDesigns = 4;

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

    public Boolean northVisible = true;
    public Boolean eastVisible = true;
    public Boolean southVisible = true;
    public Boolean westVisible = true;
    public Boolean upVisible = true;
    public Boolean downVisible = true;

    public FrameBlockTile(BlockPos pos, BlockState state) {
        super(FRAMEBLOCK_TILE.get(), pos, state);
    }

    public FrameBlockTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public <V> V set(V newValue) {
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
        return newValue;
    }

    public BlockState getMimic() {
        return this.mimic;
    }

    public Integer getDesign() {
        return this.design;
    }

    public Integer getDesignTexture() {
        return this.designTexture;
    }

    public void setMimic(BlockState mimic) {
        this.mimic = set(mimic);
    }

    public Integer getTexture() {
        return this.texture;
    }

    public void setDesign(Integer design) {
        this.design = set(design);
    }

    public Integer getGlassColor() {
        return this.glassColor;
    }

    public void setDesignTexture(Integer designTexture) {
        this.designTexture = set(designTexture);
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setTexture(Integer texture) {
        this.texture = set(texture);
    }

    public void setVisibleSides(Direction dir, boolean isVisible) {
        switch (dir) {
            case DOWN:
                downVisible = isVisible;
                break;
            case UP:
                upVisible = isVisible;
                break;
            case NORTH:
                northVisible = isVisible;
                break;
            case WEST:
                westVisible = isVisible;
                break;
            case SOUTH:
                southVisible = isVisible;
                break;
            case EAST:
                eastVisible = isVisible;
                break;
            default:
                break;
        }
    }

    public List<Direction> getVisibleSides() {
        List<Direction> dir = new ArrayList<>();
        if (northVisible)
            dir.add(Direction.NORTH);
        if (eastVisible)
            dir.add(Direction.EAST);
        if (southVisible)
            dir.add(Direction.SOUTH);
        if (westVisible)
            dir.add(Direction.WEST);
        if (upVisible)
            dir.add(Direction.UP);
        if (downVisible)
            dir.add(Direction.DOWN);
        return dir;
    }

    public void setGlassColor(Integer colorNumber) {
        this.glassColor = set(colorNumber);
    }

    public Integer getOverlay() {
        return this.overlay;
    }

    public void setRotation(Integer rotation) {
        this.rotation = set(rotation);
    }

    public void setOverlay(Integer overlay) {
        this.overlay = set(overlay);
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

    public Boolean getCanSustainPlant() {
        return canSustainPlant;
    }

    public void setCanSustainPlant(Boolean canSustainPlant) {
        this.canSustainPlant = set(canSustainPlant);
    }

    public Integer getEnchantPowerBonus() {
        return enchantPowerBonus;
    }

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

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        onDataPacket(pkt, FrameBlockTile.class, level, this.worldPosition, getBlockState());
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
                .withInitial(NORTH_VISIBLE, northVisible)
                .withInitial(EAST_VISIBLE, eastVisible)
                .withInitial(SOUTH_VISIBLE, southVisible)
                .withInitial(WEST_VISIBLE, westVisible)
                .withInitial(UP_VISIBLE, upVisible)
                .withInitial(DOWN_VISIBLE, downVisible)
                .build();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        return getUpdateTag(tag, FrameBlockTile.class);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        IFrameTile.super.load(tag, FrameBlockTile.class);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        IFrameTile.super.saveAdditional(tag, FrameBlockTile.class);
    }

}
//========SOLI DEO GLORIA========//