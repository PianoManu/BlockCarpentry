package mod.pianomanu.blockcarpentry.block;

/**
 * Main class for standing frame signs - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.0 08/15/21
 */
public class StandingSignFrameBlock {//extends StandingSignBlock {
    /*public StandingSignFrameBlock(Properties properties) {
        super(properties, WoodType.OAK);
        this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 0).setValue(WATERLOGGED, Boolean.FALSE).setValue(CONTAINS_BLOCK, false).setValue(LIGHT_LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION, WATERLOGGED, CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Override
    public boolean hasBlockEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        System.out.println("new tile");
        return new SignFrameTile();
    }

    @Override
    public InteractionResult use(BlockState state, Level levelIn, BlockPos pos, Player player, Hand handIn, BlockRayTraceResult hit) {
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if (tile instanceof SignFrameTile) {
            SignFrameTile signTile = (SignFrameTile) tile;
            player.openSignEditor(signTile);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }*/
}
//========SOLI DEO GLORIA========//