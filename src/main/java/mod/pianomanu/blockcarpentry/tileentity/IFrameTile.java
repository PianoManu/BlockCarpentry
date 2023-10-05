package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.extensions.IForgeTileEntity;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Basic interface for all Frame BlockEntities mainly containing methods that
 * are called during certain events by the TileEntity implementing this
 * interface.
 *
 * @author PianoManu
 * @version 1.1 10/03/23
 */
public interface IFrameTile extends IForgeTileEntity {
    Logger LOGGER = LogManager.getLogger();

    List<FrameBlockTile.TagPacket<?>> TAG_PACKETS = initTagPackets();

    static List<FrameBlockTile.TagPacket<?>> initTagPackets() {
        List<FrameBlockTile.TagPacket<?>> packets = new ArrayList<>();
        packets.add(new FrameBlockTile.TagPacket<>("mimic", BlockState.class, Blocks.AIR.getDefaultState()));
        packets.add(new FrameBlockTile.TagPacket<>("texture", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("design", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("designTexture", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("glassColor", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("overlay", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("rotation", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("friction", Float.class, Registration.FRAMEBLOCK.get().getSlipperiness()));
        packets.add(new FrameBlockTile.TagPacket<>("explosionResistance", Float.class, Registration.FRAMEBLOCK.get().getExplosionResistance()));
        packets.add(new FrameBlockTile.TagPacket<>("canSustainPlant", Boolean.class, false));
        packets.add(new FrameBlockTile.TagPacket<>("enchantPowerBonus", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("color", DyeColor.class, DyeColor.BLACK));
        packets.add(new FrameBlockTile.TagPacket<>("hasGlowingText", Boolean.class, false));
        return packets;
    }

    static <V> V readDataType(CompoundNBT tag, String tagElement, Class<V> classType, V defaultValue) {
        if (classType == BlockState.class) {
            return (V) NBTUtil.readBlockState(tag.getCompound(tagElement));
        }
        if (classType == Integer.class) {
            if (readInteger(tag) != 0)
                return (V) readInteger(tag);
            return (V) (Integer) tag.getInt(tagElement);
        }
        if (classType == Float.class) {
            return (V) (Float) tag.getFloat(tagElement);
        }
        if (classType == Boolean.class) {
            return (V) (Boolean) tag.getBoolean(tagElement);
        }
        if (classType == DyeColor.class) {
            return (V) DyeColor.valueOf(tag.getString(tagElement).toUpperCase());
        }
        return defaultValue;
    }

    //TODO LEGACY METHOD -> remove in 1.20
    static Integer readInteger(CompoundNBT tag) {
        if (!tag.contains("number", 8)) {
            return 0;
        } else {
            try {
                return Integer.parseInt(tag.getString("number"));
            } catch (NumberFormatException e) {
                LOGGER.error("Not a valid Number Format: " + tag.getString("number"));
                return 0;
            }
        }
    }

    BlockState getMimic();

    void setMimic(BlockState mimic);

    Integer getTexture();

    void setTexture(Integer texture);

    Integer getDesign();

    void setDesign(Integer design);

    Integer getDesignTexture();

    void setDesignTexture(Integer designTexture);

    Integer getGlassColor();

    void setGlassColor(Integer glassColor);

    Integer getRotation();

    void setRotation(Integer rotation);

    Integer getOverlay();

    void setOverlay(Integer overlay);

    Float getSlipperiness();

    void setSlipperiness(Float slipperiness);

    Float getExplosionResistance();

    void setExplosionResistance(Float explosionResistance);

    Boolean getCanSustainPlant();

    void setCanSustainPlant(Boolean canSustainPlant);

    Integer getEnchantPowerBonus();

    void setEnchantPowerBonus(Integer enchantPowerBonus);

    Boolean getCanEntityDestroy();

    void setCanEntityDestroy(Boolean canEntityDestroy);

    default <V> V read(CompoundNBT tag, String tagElement, Class<V> classType, V defaultValue) {
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

    default <V> V read(CompoundNBT tag, FrameBlockTile.TagPacket<V> tagPacket) {
        return read(tag, tagPacket.TAG_ELEMENT, tagPacket.CLASS_TYPE, tagPacket.DEFAULT);
    }

    default <V> void write(CompoundNBT tag, String tagElement, V newElement) {
        if (newElement != null) {
            if (newElement.getClass() == Integer.class)
                tag.putInt(tagElement, (Integer) newElement);
            if (newElement.getClass() == Float.class)
                tag.putFloat(tagElement, (Float) newElement);
            if (newElement.getClass() == Boolean.class)
                tag.putBoolean(tagElement, (Boolean) newElement);
            if (newElement.getClass() == BlockState.class)
                tag.put(tagElement, NBTUtil.writeBlockState((BlockState) newElement));
            if (newElement.getClass() == DyeColor.class)
                tag.putString(tagElement, ((DyeColor) newElement).name());
        }
    }

    default <V> void onDataPacket(SUpdateTileEntityPacket pkt, Class<?> cls, World level, BlockPos pos, BlockState state) {
        CompoundNBT tag = pkt.getNbtCompound();
        for (FrameBlockTile.TagPacket<?> tagPacket : TAG_PACKETS) {
            List<Field> fs = Arrays.stream(cls.getFields()).filter(f -> (!Modifier.isStatic(f.getModifiers()) && Modifier.isPublic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()))).collect(Collectors.toList());
            for (Field f : fs) {
                if (f.getName().equals(tagPacket.TAG_ELEMENT)) {
                    try {
                        V oldValue = (V) f.get(this);
                        V newValue = update(tag, tagPacket.TAG_ELEMENT, oldValue, (Class<V>) tagPacket.CLASS_TYPE, (V) tagPacket.DEFAULT, level, pos, state);
                        f.set(this, newValue);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    default <V> V update(CompoundNBT tag, String tagElement, V oldValue, Class<V> classType, V defaultValue, World level, BlockPos pos, BlockState state) {
        if (tag.contains(tagElement)) {
            V newValue = read(tag, tagElement, classType, defaultValue);
            if (!Objects.equals(oldValue, newValue)) {
                this.requestModelDataUpdate();
                level.notifyBlockUpdate(pos, state, state, Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
            return newValue;
        }
        return null;
    }

    default CompoundNBT getUpdateTag(CompoundNBT tag, Class<?> cls) {
        for (FrameBlockTile.TagPacket<?> tagPacket : TAG_PACKETS) {
            List<Field> fs = Arrays.stream(cls.getFields()).filter(f -> (!Modifier.isStatic(f.getModifiers()) && Modifier.isPublic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()))).collect(Collectors.toList());
            for (Field f : fs) {
                if (f.getName().equals(tagPacket.TAG_ELEMENT)) {
                    try {
                        write(tag, tagPacket.TAG_ELEMENT, f.get(this));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return tag;
    }

    default void read(CompoundNBT tag, Class<?> cls) {
        for (FrameBlockTile.TagPacket<?> tagPacket : TAG_PACKETS) {
            List<Field> fs = Arrays.stream(cls.getFields()).filter(f -> (!Modifier.isStatic(f.getModifiers()) && Modifier.isPublic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()))).collect(Collectors.toList());
            for (Field f : fs) {
                if (f.getName().equals(tagPacket.TAG_ELEMENT)) {
                    try {
                        f.set(this, read(tag, tagPacket));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    default void write(CompoundNBT tag, Class<?> cls) {
        for (FrameBlockTile.TagPacket<?> tagPacket : TAG_PACKETS) {
            List<Field> fs = Arrays.stream(cls.getFields()).filter(f -> (!Modifier.isStatic(f.getModifiers()) && Modifier.isPublic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()))).collect(Collectors.toList());
            for (Field f : fs) {
                if (f.getName().equals(tagPacket.TAG_ELEMENT)) {
                    try {
                        write(tag, tagPacket.TAG_ELEMENT, f.get(this));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    default void clear() {
        this.setMimic(null);
    }

    class TagPacket<V> {
        public final String TAG_ELEMENT;
        public final Class<V> CLASS_TYPE;
        public final V DEFAULT;

        public TagPacket(String tagElement, Class<V> classType, V defaultValue) {
            this.TAG_ELEMENT = tagElement;
            this.CLASS_TYPE = classType;
            this.DEFAULT = defaultValue;
        }
    }
}
//========SOLI DEO GLORIA========//