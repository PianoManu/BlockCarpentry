package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.util.BCNBTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeBlockEntity;
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
 * are called during certain events by the BlockEntity implementing this
 * interface.
 *
 * @author PianoManu
 * @version 1.4 10/30/23
 */
public interface IFrameTile extends IForgeBlockEntity {
    Logger LOGGER = LogManager.getLogger();

    List<FrameBlockTile.TagPacket<?>> TAG_PACKETS = initTagPackets();

    private static List<FrameBlockTile.TagPacket<?>> initTagPackets() {
        List<FrameBlockTile.TagPacket<?>> packets = new ArrayList<>();
        packets.add(new FrameBlockTile.TagPacket<>("mimic", BlockState.class, Blocks.AIR.defaultBlockState()));
        packets.add(new FrameBlockTile.TagPacket<>("texture", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("design", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("designTexture", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("glassColor", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("overlay", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("rotation", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("keepUV", Boolean.class, true));
        packets.add(new FrameBlockTile.TagPacket<>("friction", Float.class, Registration.FRAMEBLOCK.get().getFriction()));
        packets.add(new FrameBlockTile.TagPacket<>("explosionResistance", Float.class, Registration.FRAMEBLOCK.get().getExplosionResistance()));
        packets.add(new FrameBlockTile.TagPacket<>("canSustainPlant", Boolean.class, false));
        packets.add(new FrameBlockTile.TagPacket<>("enchantPowerBonus", Integer.class, 0));
        packets.add(new FrameBlockTile.TagPacket<>("color", DyeColor.class, DyeColor.BLACK));
        packets.add(new FrameBlockTile.TagPacket<>("hasGlowingText", Boolean.class, false));
        packets.add(new FrameBlockTile.TagPacket<>("NWU", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("NEU", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("NWD", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("NED", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("SWU", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("SEU", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("SWD", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("SED", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("directions", List.class, new ArrayList<>()));
        packets.add(new FrameBlockTile.TagPacket<>("rotations", List.class, Arrays.asList(0, 0, 0, 0, 0, 0)));
        return packets;
    }

    private static <V> V readDataType(CompoundTag tag, String tagElement, Class<V> classType, V defaultValue) {
        try {
            if (classType == BlockState.class) {
                return (V) NbtUtils.readBlockState(tag.getCompound(tagElement));
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
            if (classType == Vec3.class) {
                return (V) BCNBTUtils.readVec(tag.getString(tagElement));
            }
            if (Objects.equals(tagElement, "directions")) {
                return (V) BCNBTUtils.readDirectionList(tag.getIntArray(tagElement));
            }
            if (Objects.equals(tagElement, "rotations")) {
                return (V) BCNBTUtils.readRotationsList(tag.getIntArray(tagElement));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    //TODO LEGACY METHOD -> remove in 1.20
    private static Integer readInteger(CompoundTag tag) {
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

    Float getFriction();

    void setFriction(Float friction);

    Float getExplosionResistance();

    void setExplosionResistance(Float explosionResistance);

    Boolean getCanSustainPlant();

    void setCanSustainPlant(Boolean canSustainPlant);

    Integer getEnchantPowerBonus();

    void setEnchantPowerBonus(Integer enchantPowerBonus);

    Boolean getCanEntityDestroy();

    void setCanEntityDestroy(Boolean canEntityDestroy);

    default <V> V read(CompoundTag tag, String tagElement, Class<V> classType, V defaultValue) {
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

    default <V> V read(CompoundTag tag, FrameBlockTile.TagPacket<V> tagPacket) {
        return read(tag, tagPacket.TAG_ELEMENT, tagPacket.CLASS_TYPE, tagPacket.DEFAULT);
    }

    default <V> void write(CompoundTag tag, String tagElement, V newElement) {
        if (newElement != null) {
            if (newElement.getClass() == Integer.class)
                tag.putInt(tagElement, (int) newElement);
            else if (newElement.getClass() == Float.class)
                tag.putFloat(tagElement, (float) newElement);
            else if (newElement.getClass() == Boolean.class)
                tag.putBoolean(tagElement, (boolean) newElement);
            else if (newElement.getClass() == BlockState.class)
                tag.put(tagElement, NbtUtils.writeBlockState((BlockState) newElement));
            else if (newElement.getClass() == DyeColor.class)
                tag.putString(tagElement, ((DyeColor) newElement).getName());
            else if (newElement.getClass() == Vec3.class)
                tag.putString(tagElement, ((Vec3) newElement).toString());
            else if (Objects.equals(tagElement, "directions"))
                BCNBTUtils.writeDirectionList(tag, (List<?>) newElement);
            else if (Objects.equals(tagElement, "rotations"))
                BCNBTUtils.writeRotationsList(tag, (List<?>) newElement);
        }
    }

    default <V> void onDataPacket(ClientboundBlockEntityDataPacket pkt, Class<?> cls, Level level, BlockPos pos, BlockState state) {
        CompoundTag tag = pkt.getTag();
        try {
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
        } catch (Exception e) {
            LOGGER.error("Critical BlockState found: " + state.toString());
            handleException(e, tag, "getUpdateTag");
        }
    }

    private <V> V update(CompoundTag tag, String tagElement, V oldValue, Class<V> classType, V defaultValue, Level level, BlockPos pos, BlockState state) {
        if (tag.contains(tagElement)) {
            V newValue = read(tag, tagElement, classType, defaultValue);
            if (!Objects.equals(oldValue, newValue)) {
                this.requestModelDataUpdate();
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
            }
            return newValue;
        }
        return null;
    }

    default CompoundTag getUpdateTag(CompoundTag tag, Class<?> cls) {
        try {
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
        } catch (Exception e) {
            handleException(e, tag, "getUpdateTag");
        }
        return tag;
    }

    default void load(CompoundTag tag, Class<?> cls) {
        try {
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
        } catch (Exception e) {
            handleException(e, tag, "load");
        }
    }

    default void saveAdditional(CompoundTag tag, Class<?> cls) {
        try {
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
        } catch (Exception e) {
            handleException(e, tag, "saveAdditional");
        }
    }

    default void clear() {
        this.setMimic(null);
    }

    private static void handleException(Exception e, CompoundTag tag, String phase) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.displayClientMessage(Component.translatable("message.blockcarpentry.exception"), false);
        }
        LOGGER.error("An exception occurred with CompoundTag " + tag.toString() + " during the " + phase + " phase!");
        e.printStackTrace();
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