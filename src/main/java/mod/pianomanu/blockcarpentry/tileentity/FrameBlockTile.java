package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.extensions.IForgeBlockEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static mod.pianomanu.blockcarpentry.setup.Registration.FRAMEBLOCK_TILE;

/**
 * BlockEntity for {@link mod.pianomanu.blockcarpentry.block.FrameBlock} and all sorts of frame blocks
 * Contains all information about the block and the mimicked block
 *
 * @author PianoManu
 * @version 1.4 09/24/23
 */
public class FrameBlockTile extends BlockEntity implements IForgeBlockEntity {
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

    public static final List<TagPacket<?>> TAG_PACKETS = new ArrayList<>();

    private BlockState mimic;
    private Integer texture = 0;
    private Integer design = 0;
    private Integer designTexture = 0;
    private Integer glassColor = 0;
    private Integer overlay = 0;
    private Integer rotation = 0;
    private Float friction = Registration.FRAMEBLOCK.get().getFriction();
    private Float explosionResistance = Registration.FRAMEBLOCK.get().getExplosionResistance();
    private Boolean canSustainPlant = false;
    private Integer enchantPowerBonus = 0;

    private Boolean northVisible = true;
    private Boolean eastVisible = true;
    private Boolean southVisible = true;
    private Boolean westVisible = true;
    private Boolean upVisible = true;
    private Boolean downVisible = true;

    private static final Logger LOGGER = LogManager.getLogger();

    public FrameBlockTile(BlockPos pos, BlockState state) {
        super(FRAMEBLOCK_TILE.get(), pos, state);
        TAG_PACKETS.add(new TagPacket<>("mimic", BlockState.class, Blocks.AIR.defaultBlockState(), this.mimic));
        TAG_PACKETS.add(new TagPacket<>("texture", Integer.class, 0, this.texture));
        TAG_PACKETS.add(new TagPacket<>("design", Integer.class, 0, this.design));
        TAG_PACKETS.add(new TagPacket<>("designTexture", Integer.class, 0, this.designTexture));
        TAG_PACKETS.add(new TagPacket<>("glassColor", Integer.class, 0, this.glassColor));
        TAG_PACKETS.add(new TagPacket<>("overlay", Integer.class, 0, this.overlay));
        TAG_PACKETS.add(new TagPacket<>("rotation", Integer.class, 0, this.rotation));
        TAG_PACKETS.add(new TagPacket<>("friction", Float.class, Registration.FRAMEBLOCK.get().getFriction(), this.friction));
        TAG_PACKETS.add(new TagPacket<>("explosionResistance", Float.class, Registration.FRAMEBLOCK.get().getExplosionResistance(), this.explosionResistance));
        TAG_PACKETS.add(new TagPacket<>("canSustainPlant", Boolean.class, false, this.canSustainPlant));
        TAG_PACKETS.add(new TagPacket<>("enchantPowerBonus", Integer.class, 0, this.enchantPowerBonus));
    }

    public FrameBlockTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    private static <V> V readDataType(CompoundTag tag, TagPacket<V> tagPacket) {
        Class<V> classType = tagPacket.CLASS_TYPE;
        String tagElement = tagPacket.TAG_ELEMENT;
        if (classType == BlockState.class) {
            return (V) NbtUtils.readBlockState(tag.getCompound(tagElement));
        }
        if (classType == Integer.class) {
            return (V) (Integer) tag.getInt(tagElement);
        }
        if (classType == Float.class) {
            return (V) (Float) tag.getFloat(tagElement);
        }
        if (classType == Boolean.class) {
            return (V) (Boolean) tag.getBoolean(tagElement);
        }
        return tagPacket.DEFAULT;
    }

    private static <V> V readDataType(CompoundTag tag, String tagElement, Class<V> classType, V defaultValue) {
        if (classType == BlockState.class) {
            return (V) NbtUtils.readBlockState(tag.getCompound(tagElement));
        }
        if (classType == Integer.class) {
            return (V) (Integer) tag.getInt(tagElement);
        }
        if (classType == Float.class) {
            return (V) (Float) tag.getFloat(tagElement);
        }
        if (classType == Boolean.class) {
            return (V) (Boolean) tag.getBoolean(tagElement);
        }
        return defaultValue;
    }

    private static <V> void write(CompoundTag compoundNbt, V tagElement, String tagElementName) {
        if (tagElement.getClass() == Integer.class)
            compoundNbt.putInt(tagElementName, (int) tagElement);
        if (tagElement.getClass() == Float.class)
            compoundNbt.putFloat(tagElementName, (float) tagElement);
        if (tagElement.getClass() == Boolean.class)
            compoundNbt.putBoolean(tagElementName, (boolean) tagElement);
        if (tagElement.getClass() == BlockState.class)
            compoundNbt.put(tagElementName, NbtUtils.writeBlockState((BlockState) tagElement));
    }

    public BlockState getMimic() {
        return this.mimic;
    }

    public Integer getDesign() {
        return this.design;
    }

    private static CompoundTag writeInteger(Integer tag) {
        CompoundTag compoundnbt = new CompoundTag();
        compoundnbt.putString("number", tag.toString());
        return compoundnbt;
    }

    public <V> V set(V newValue) {
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
        return newValue;
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

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        BlockState oldMimic = mimic;
        Integer oldTexture = texture;
        Integer oldDesign = design;
        Integer oldDesignTexture = designTexture;
        Integer oldGlassColor = glassColor;
        Integer oldOverlay = overlay;
        Integer oldRotation = rotation;
        Float oldFriction = friction;
        Float oldExplosionResistance = explosionResistance;
        Boolean oldCanSustainPlant = canSustainPlant;
        Integer oldEnchantmentPowerBonus = enchantPowerBonus;
        CompoundTag tag = pkt.getTag();
        this.mimic = update(tag, "mimic", oldMimic, BlockState.class, Blocks.AIR.defaultBlockState());
        this.texture = update(tag, "texture", oldTexture, Integer.class, 0);
        this.design = update(tag, "design", oldDesign, Integer.class, 0);
        this.designTexture = update(tag, "designTexture", oldDesignTexture, Integer.class, 0);
        this.glassColor = update(tag, "glass_color", oldGlassColor, Integer.class, 0);
        this.overlay = update(tag, "overlay", oldOverlay, Integer.class, 0);
        this.rotation = update(tag, "rotation", oldRotation, Integer.class, 0);
        this.friction = update(tag, "friction", oldFriction, Float.class, Registration.FRAMEBLOCK.get().getFriction());
        this.explosionResistance = update(tag, "explosionResistance", oldExplosionResistance, Float.class, Registration.FRAMEBLOCK.get().getExplosionResistance());
        this.canSustainPlant = update(tag, "canSustainPlant", oldCanSustainPlant, Boolean.class, false);
        this.enchantPowerBonus = update(tag, "enchantmentPowerBonus", oldEnchantmentPowerBonus, Integer.class, 0);
    }

    private <V> V update(CompoundTag tag, TagPacket<V> tagPacket, V oldValue) {
        if (tag.contains(tagPacket.TAG_ELEMENT)) {
            V newValue = getNewValue(tag, tagPacket);
            if (!Objects.equals(oldValue, newValue)) {
                this.requestModelDataUpdate();
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
            }
            return newValue;
        }
        return null;
    }

    private <V> V update(CompoundTag tag, String tagElement, V oldValue, Class<V> classType, V defaultValue) {
        if (tag.contains(tagElement)) {
            V newValue = getNewValue(tag, tagElement, classType, defaultValue);
            if (!Objects.equals(oldValue, newValue)) {
                this.requestModelDataUpdate();
                level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
            }
            return newValue;
        }
        return null;
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
                .with(OVERLAY, overlay)
                .with(ROTATION, rotation)
                .with(NORTH_VISIBLE, northVisible)
                .with(EAST_VISIBLE, eastVisible)
                .with(SOUTH_VISIBLE, southVisible)
                .with(WEST_VISIBLE, westVisible)
                .with(UP_VISIBLE, upVisible)
                .with(DOWN_VISIBLE, downVisible)
                .build();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        setNewValue(tag, "mimic", this.mimic);
        setNewValue(tag, "texture", this.texture);
        setNewValue(tag, "design", this.design);
        setNewValue(tag, "design_texture", this.designTexture);
        setNewValue(tag, "glass_color", this.glassColor);
        setNewValue(tag, "overlay", this.overlay);
        setNewValue(tag, "rotation", this.rotation);
        setNewValue(tag, "friction", this.friction);
        setNewValue(tag, "explosionResistance", this.explosionResistance);
        setNewValue(tag, "canSustainPlant", this.canSustainPlant);
        setNewValue(tag, "enchantmentPowerBonus", this.enchantPowerBonus);
        /*Class cls = FrameBlockTile.class;
        for (TagPacket<?> tagPacket : TAG_PACKETS) {
            Field[] fs = cls.getDeclaredFields();
            for (Field f : fs) {
                if (f.getName().equals(tagPacket.TAG_ELEMENT)) {
                    try {
                        setNewValue(tag, tagPacket.TAG_ELEMENT, f.get(this));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }*/
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        //this.mimic = loadTag(tag, "mimic", BlockState.class);
        //this.texture = loadTag(tag, "texture", Integer.class);
        //this.design = loadTag(tag, "design", Integer.class);
        //this.designTexture = loadTag(tag, "design_texture", Integer.class);
        //this.glassColor = loadTag(tag, "glass_color", Integer.class);
        //this.overlay = loadTag(tag, "overlay", Integer.class);
        //this.rotation = loadTag(tag, "rotation", Integer.class);
        //this.friction = loadTag(tag, "friction", Float.class);
        //this.explosionResistance = loadTag(tag, "explosionResistance", Float.class);
        //this.canSustainPlant = loadTag(tag, "canSustainPlant", Boolean.class);
        //this.enchantPowerBonus = loadTag(tag, "enchantmentPowerBonus", Integer.class);
        Class cls = FrameBlockTile.class;
        for (TagPacket<?> tagPacket : TAG_PACKETS) {
            Field[] fs = cls.getDeclaredFields();
            for (Field f : fs) {
                if (f.getName().equals(tagPacket.TAG_ELEMENT)) {
                    try {
                        f.set(this, loadTag(tag, tagPacket));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        //setNewValue(tag, "mimic", this.mimic);
        //setNewValue(tag, "texture", this.texture);
        //setNewValue(tag, "design", this.design);
        //setNewValue(tag, "design_texture", this.designTexture);
        //setNewValue(tag, "glass_color", this.glassColor);
        //setNewValue(tag, "overlay", this.overlay);
        //setNewValue(tag, "rotation", this.rotation);
        //setNewValue(tag, "friction", this.friction);
        //setNewValue(tag, "explosionResistance", this.explosionResistance);
        //setNewValue(tag, "canSustainPlant", this.canSustainPlant);
        //setNewValue(tag, "enchantmentPowerBonus", this.enchantPowerBonus);
        Class cls = FrameBlockTile.class;
        for (TagPacket<?> tagPacket : TAG_PACKETS) {
            Field[] fs = cls.getDeclaredFields();
            for (Field f : fs) {
                if (f.getName().equals(tagPacket.TAG_ELEMENT)) {
                    try {
                        setNewValue(tag, tagPacket.TAG_ELEMENT, f.get(this));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private <V> V loadTag(CompoundTag tag, TagPacket<V> tagPacket) {
        if (tag.contains(tagPacket.TAG_ELEMENT)) {
            return getNewValue(tag, tagPacket);
        }
        return null;
    }

    private <V> V getNewValue(CompoundTag tag, TagPacket<V> tagPacket) {
        return getNewValue(tag, tagPacket.TAG_ELEMENT, tagPacket.CLASS_TYPE, tagPacket.DEFAULT);
    }

    private <V> V getNewValue(CompoundTag tag, String tagElement, Class<V> classType, V defaultValue) {
        if (!tag.contains(tagElement)) {
            return defaultValue;
        } else {
            try {
                return readDataType(tag, tagElement, classType, defaultValue);
            } catch (Exception e) {
                LOGGER.error("Not a valid " + tagElement + " Format: " + tag.getString(tagElement));
            }
        }
        return defaultValue;
    }

    private <V> void setNewValue(CompoundTag tag, String tagElement, V newElement) {
        if (newElement != null) {
            write(tag, newElement, tagElement);
        }
    }

    public void clear() {
        this.setMimic(null);
    }

    public static class TagPacket<V> {
        public final String TAG_ELEMENT;
        public final Class<V> CLASS_TYPE;
        public final V DEFAULT;
        public final V REFERENCE_VALUE;

        public TagPacket(String tagElement, Class<V> classType, V defaultValue, V referenceValue) {
            this.TAG_ELEMENT = tagElement;
            this.CLASS_TYPE = classType;
            this.DEFAULT = defaultValue;
            this.REFERENCE_VALUE = referenceValue;
        }
    }
}
//========SOLI DEO GLORIA========//