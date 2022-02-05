package mod.pianomanu.blockcarpentry.tileentity;

/**
 * Currently unused - BlockEntity for falling blocks, which are not implemented right now (may be removed or rewritten in future)
 *
 * @author PianoManu
 * @version 1.0 08/15/21
 */
public class FallingFrameBlockTile {//extends FrameBlockTile {
    /*public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();

    private BlockState mimic;

    public FallingFrameBlockTile(BlockState mimic) {
        super();
        this.mimic = mimic;
    }

    public void setMimic(BlockState mimic) {
        this.mimic = mimic;
        setChanged();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public BlockState getMimic() {
        return this.mimic;
    }


    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        if (mimic != null) {
            tag.put("mimic", NbtUtils.writeBlockState(mimic));
        }
        return tag;
    }

    @Nullable
    @Override
    public SUpdateBlockEntityPacket getUpdatePacket() {
        return new SUpdateBlockEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateBlockEntityPacket pkt) {
        BlockState oldMimic = mimic;
        CompoundTag tag = pkt.getNbtCompound();
        if (tag.contains("mimic")) {
            mimic = NbtUtils.readBlockState(tag.getCompound("mimic"));
            if (!Objects.equals(oldMimic, mimic)) {
                ModelDataManager.requestModelDataRefresh(this);
                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(MIMIC, mimic)
                .build();
    }*/

    /*@Override
    public void read(CompoundTag tag) {
        super.read(tag);
        if (tag.contains("mimic")) {
            mimic = NbtUtils.readBlockState(tag.getCompound("mimic"));
        }
    }*/

    /*@Override
    public CompoundTag write(CompoundTag tag) {
        if (mimic != null) {
            tag.put("mimic", NbtUtils.writeBlockState(mimic));
        }
        return super.write(tag);
    }

    public void clear() {
        this.setMimic(null);
    }*/
}
//========SOLI DEO GLORIA========//