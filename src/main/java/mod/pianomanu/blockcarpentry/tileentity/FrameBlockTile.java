package mod.pianomanu.blockcarpentry.tileentity;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.util.VoxelUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.extensions.IForgeBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static mod.pianomanu.blockcarpentry.setup.Registration.FRAMEBLOCK_TILE;

/**
 * BlockEntity for {@link mod.pianomanu.blockcarpentry.block.FrameBlock} and all sorts of frame blocks
 * Contains all information about the block and the mimicked block
 *
 * @author PianoManu
 * @version 1.9 11/08/23
 */
public class FrameBlockTile extends BlockEntity implements IForgeBlockEntity, IFrameTile {
    public static final List<IFrameTile.TagPacket<?>> TAG_PACKETS = initTagPackets();

    private static List<FrameBlockTile.TagPacket<?>> initTagPackets() {
        List<FrameBlockTile.TagPacket<?>> packets = new ArrayList<>();
        packets.add(new FrameBlockTile.TagPacket<>("NWD", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("NWU", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("NED", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("NEU", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("SWD", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("SWU", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("SED", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("SEU", Vec3.class, Vec3.ZERO));
        packets.add(new FrameBlockTile.TagPacket<>("directions", List.class, Collections.emptyList()));
        return packets;
    }

    private VoxelShape shape = Shapes.block();

    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
    public static final ModelProperty<Integer> TEXTURE = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN = new ModelProperty<>();
    public static final ModelProperty<Integer> DESIGN_TEXTURE = new ModelProperty<>();
    //currently only for doors and trapdoors
    public static final ModelProperty<Integer> GLASS_COLOR = new ModelProperty<>();
    public static final ModelProperty<Integer> OVERLAY = new ModelProperty<>();
    public static final ModelProperty<Integer> ROTATION = new ModelProperty<>();
    public static final ModelProperty<Boolean> KEEP_UV = new ModelProperty<>();
    public static final ModelProperty<Boolean> REMAIN_RECTANGLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> NORTH_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> EAST_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> SOUTH_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> WEST_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> UP_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> DOWN_VISIBLE = new ModelProperty<>();
    public static final ModelProperty<List<Direction>> DIRECTIONS = new ModelProperty<>();

    public static final ModelProperty<Vec3> NWU_prop = new ModelProperty<>();
    public static final ModelProperty<Vec3> NEU_prop = new ModelProperty<>();
    public static final ModelProperty<Vec3> NWD_prop = new ModelProperty<>();
    public static final ModelProperty<Vec3> NED_prop = new ModelProperty<>();
    public static final ModelProperty<Vec3> SWU_prop = new ModelProperty<>();
    public static final ModelProperty<Vec3> SEU_prop = new ModelProperty<>();
    public static final ModelProperty<Vec3> SWD_prop = new ModelProperty<>();
    public static final ModelProperty<Vec3> SED_prop = new ModelProperty<>();

    public static final ModelProperty<List<Integer>> ROTATIONS = new ModelProperty<>();

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
    public Boolean keepUV = true;
    public Boolean faceRemainRectangle = true;
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

    public List<Integer> rotations = Arrays.asList(0, 0, 0, 0, 0, 0);

    public Vec3 NWU = new Vec3(0, 0, 0);
    public Vec3 NEU = new Vec3(0, 0, 0);
    public Vec3 NWD = new Vec3(0, 0, 0);
    public Vec3 NED = new Vec3(0, 0, 0);
    public Vec3 SWU = new Vec3(0, 0, 0);
    public Vec3 SEU = new Vec3(0, 0, 0);
    public Vec3 SWD = new Vec3(0, 0, 0);
    public Vec3 SED = new Vec3(0, 0, 0);

    private Vec3 oldNWU = new Vec3(0, 0, 0);
    private Vec3 oldNEU = new Vec3(0, 0, 0);
    private Vec3 oldNWD = new Vec3(0, 0, 0);
    private Vec3 oldNED = new Vec3(0, 0, 0);
    private Vec3 oldSWU = new Vec3(0, 0, 0);
    private Vec3 oldSEU = new Vec3(0, 0, 0);
    private Vec3 oldSWD = new Vec3(0, 0, 0);
    private Vec3 oldSED = new Vec3(0, 0, 0);

    public List<Direction> directions = new ArrayList<>();

    public List<Vec3[]> corners = new ArrayList<>();

    public FrameBlockTile(BlockPos pos, BlockState state) {
        super(FRAMEBLOCK_TILE.get(), pos, state);

        updateVecList();
    }

    public FrameBlockTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void updateVecList() {
        this.corners = new ArrayList<>();
        this.corners.add(new Vec3[]{this.NWD, this.NED, this.NWU, this.SWD});
        this.corners.add(new Vec3[]{this.NWU, this.NEU, this.NWD, this.SWU});
        this.corners.add(new Vec3[]{this.NED, this.NWD, this.NEU, this.SED});
        this.corners.add(new Vec3[]{this.NEU, this.NWU, this.NED, this.SEU});
        this.corners.add(new Vec3[]{this.SWD, this.SED, this.SWU, this.NWD});
        this.corners.add(new Vec3[]{this.SWU, this.SEU, this.SWD, this.NWU});
        this.corners.add(new Vec3[]{this.SED, this.SWD, this.SEU, this.NED});
        this.corners.add(new Vec3[]{this.SEU, this.SWU, this.SED, this.NEU});
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

    public void setNWU(Vec3 NWU) {
        if (inRange(NWU.x, 16) && inRange(NWU.y, -16) && inRange(NWU.z, 16))
            this.NWU = set(NWU);
    }

    public void setNEU(Vec3 NEU) {
        if (inRange(NEU.x, -16) && inRange(NEU.y, -16) && inRange(NEU.z, 16))
            this.NEU = set(NEU);
    }

    public void setNWD(Vec3 NWD) {
        if (inRange(NWD.x, 16) && inRange(NWD.y, 16) && inRange(NWD.z, 16))
            this.NWD = set(NWD);
    }

    public void setNED(Vec3 NED) {
        if (inRange(NED.x, -16) && inRange(NED.y, 16) && inRange(NED.z, 16))
            this.NED = set(NED);
    }

    public void setSWU(Vec3 SWU) {
        if (inRange(SWU.x, 16) && inRange(SWU.y, -16) && inRange(SWU.z, -16))
            this.SWU = set(SWU);
    }

    public void setSEU(Vec3 SEU) {
        if (inRange(SEU.x, -16) && inRange(SEU.y, -16) && inRange(SEU.z, -16))
            this.SEU = set(SEU);
    }

    public void setSWD(Vec3 SWD) {
        if (inRange(SWD.x, 16) && inRange(SWD.y, 16) && inRange(SWD.z, -16))
            this.SWD = set(SWD);
    }

    public void setSED(Vec3 SED) {
        if (inRange(SED.x, -16) && inRange(SED.y, 16) && inRange(SED.z, -16))
            this.SED = set(SED);
    }

    private boolean inRange(double val, int limit) {
        int min = Math.min(0, limit);
        int max = Math.max(0, limit);
        return val >= min && val <= max;
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

    public void addDirection(Direction direction) {
        this.directions.add(set(direction));
        this.trySimplifyDirections();
    }

    private void trySimplifyDirections() {
        int i = 0;
        Direction prev = null;
        List<Direction> newDirections = new ArrayList<>();
        for (Direction d :
                this.directions) {
            newDirections.add(d);
            if (d != prev) {
                if (d.getOpposite() == prev) {
                    this.removeLastNEntries(newDirections, 2);
                    prev = newDirections.get(newDirections.size() - 1);
                } else {
                    prev = d;
                }
                i = 0;
            } else {
                i++;
                if (i == 3) {
                    this.removeLastNEntries(newDirections, 4);
                    i = 0;
                }
            }
        }
        this.directions = set(newDirections);
    }

    private void removeLastNEntries(List<Direction> directions, int iters) {
        int size = directions.size();
        for (int i = 1; i <= iters; i++) {
            directions.remove(size - iters);
        }
    }

    public Integer getRotation(Direction direction) {
        return rotations.get(direction.ordinal());
    }

    public void addRotation(Direction direction) {
        if (this.rotations.size() != 6) {
            this.rotations = new ArrayList<>();
            this.rotations.addAll(Arrays.asList(0, 0, 0, 0, 0, 0));
        }
        if (this.rotations.get(direction.ordinal()) >= 3)
            this.rotations.set(direction.ordinal(), set(0));
        else
            this.rotations.set(direction.ordinal(), set(this.rotations.get(direction.ordinal()) + 1));
    }

    public Boolean getKeepUV() {
        return this.keepUV;
    }

    public void setKeepUV(Boolean keepUV) {
        this.keepUV = set(keepUV);
    }

    public Boolean getFaceRemainRectangle() {
        return this.faceRemainRectangle;
    }

    public void setFaceRemainRectangle(Boolean faceRemainRectangle) {
        this.faceRemainRectangle = set(faceRemainRectangle);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.updateShape();
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
                .withInitial(KEEP_UV, keepUV)
                .withInitial(REMAIN_RECTANGLE, faceRemainRectangle)
                .withInitial(NORTH_VISIBLE, northVisible)
                .withInitial(EAST_VISIBLE, eastVisible)
                .withInitial(SOUTH_VISIBLE, southVisible)
                .withInitial(WEST_VISIBLE, westVisible)
                .withInitial(UP_VISIBLE, upVisible)
                .withInitial(DOWN_VISIBLE, downVisible)
                .withInitial(NWU_prop, NWU)
                .withInitial(NEU_prop, NEU)
                .withInitial(NWD_prop, NWD)
                .withInitial(NED_prop, NED)
                .withInitial(SWU_prop, SWU)
                .withInitial(SEU_prop, SEU)
                .withInitial(SWD_prop, SWD)
                .withInitial(SED_prop, SED)
                .withInitial(DIRECTIONS, directions)
                .withInitial(ROTATIONS, rotations)
                .build();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        this.updateShape();
        return getUpdateTag(tag, FrameBlockTile.class);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.updateShape();
        IFrameTile.super.load(tag, FrameBlockTile.class);
        this.updateVecList();
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        IFrameTile.super.saveAdditional(tag, FrameBlockTile.class);
    }

    public VoxelShape getShape() {
        return shape;
    }

    public void updateShape() {
        if (shapeChanged())
            this.shape = set(VoxelUtils.getShape(this));
    }

    public boolean shapeUnmodified() {
        return this.NWD.equals(Vec3.ZERO)
                && this.NWU.equals(Vec3.ZERO)
                && this.NED.equals(Vec3.ZERO)
                && this.NEU.equals(Vec3.ZERO)
                && this.SWD.equals(Vec3.ZERO)
                && this.SWU.equals(Vec3.ZERO)
                && this.SED.equals(Vec3.ZERO)
                && this.SEU.equals(Vec3.ZERO);
    }

    public boolean shapeChanged() {
        if (this.NWD.equals(this.oldNWD)
                && this.NWU.equals(this.oldNWU)
                && this.NED.equals(this.oldNED)
                && this.NEU.equals(this.oldNEU)
                && this.SWD.equals(this.oldSWD)
                && this.SWU.equals(this.oldSWU)
                && this.SED.equals(this.oldSED)
                && this.SEU.equals(this.oldSEU)) {
            return false;
        }
        this.oldNWD = this.NWD;
        this.oldNWU = this.NWU;
        this.oldNED = this.NED;
        this.oldNEU = this.NEU;
        this.oldSWD = this.SWD;
        this.oldSWU = this.SWU;
        this.oldSED = this.SED;
        this.oldSEU = this.SEU;
        return true;
    }
}
//========SOLI DEO GLORIA========//