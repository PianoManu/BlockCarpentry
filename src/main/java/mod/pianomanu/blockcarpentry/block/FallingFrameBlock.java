package mod.pianomanu.blockcarpentry.block;

/**
 * Nothing important to see here, this class is currently unused, visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.0 08/15/21
 */
public class FallingFrameBlock {//extends FallingBlock {

    //TODO fix falling block losing tile entity
    /*public static final BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;
    //public static final BlockContainerProperty CONTAINS = BCBlockStateProperties.CONTAINS;

    public FallingFrameBlock(Properties properties) {
        super(properties);
        //this.registerDefaultState(this.defaultBlockState().setValue(CONTAINS_BLOCK, false).setValue(CONTAINS, "empty").setValue(LIGHT_LEVEL, 0).setValue(TEXTURE,0));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        //builder.add(CONTAINS_BLOCK, CONTAINS, LIGHT_LEVEL, TEXTURE);
    }

    @Override
    public boolean hasBlockEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FrameBlockTile(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitresult) {
        ItemStack item = player.getItemInHand(hand);
        if (!world.isClientSide) {
            if (state.getValue(CONTAINS_BLOCK)) {
                if (!player.isCreative())
                    this.dropContainedBlock(world, pos);
                state = state.setValue(CONTAINS_BLOCK, Boolean.FALSE);
                world.setBlock(pos, state, 2);
            } else {
                if (item.getItem() instanceof BlockItem) {
                    BlockEntity tileEntity = world.getBlockEntity(pos);
                    int count = player.getItemInHand(hand).getCount();
                    if (tileEntity instanceof FrameBlockTile && !item.isEmpty() && BlockSavingHelper.isValidBlock(((BlockItem) item.getItem()).getBlock()) && !state.getValue(CONTAINS_BLOCK)) {
                        ((FrameBlockTile) tileEntity).clear();
                        BlockState handBlockState = ((BlockItem) item.getItem()).getBlock().defaultBlockState();
                        ((FrameBlockTile) tileEntity).setMimic(handBlockState);
                        insertBlock(world, pos, state, handBlockState);
                        if (!player.isCreative())
                            player.getItemInHand(hand).setCount(count - 1);
                    }
                }
            }
            BlockAppearanceHelper.setLightLevel(item, state, world, pos, player, hand);
            BlockAppearanceHelper.setTexture(item, state, world, player, pos);
            if (item.getItem() == Registration.TEXTURE_WRENCH.get() && player.isCrouching()) {
                //System.out.println("You should rotate now!");
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void dropContainedBlock(Level worldIn, BlockPos pos) {
        if (!worldIn.isClientSide) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof FrameBlockTile) {
                FrameBlockTile frameBlockEntity = (FrameBlockTile) tileentity;
                BlockState blockState = frameBlockEntity.getMimic();
                if (!(blockState == null)) {
                    worldIn.levelEvent(1010, pos, 0);
                    frameBlockEntity.clear();
                    float f = 0.7F;
                    double d0 = (double) (worldIn.random.nextFloat() * 0.7F) + (double) 0.15F;
                    double d1 = (worldIn.random.nextFloat() * 0.7F) + (double) 0.060000002F + 0.6D;
                    double d2 = (double) (worldIn.random.nextFloat() * 0.7F) + (double) 0.15F;
                    ItemStack itemstack1 = new ItemStack(blockState.getBlock());
                    ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, itemstack1);
                    itementity.setDefaultPickUpDelay();
                    worldIn.addFreshEntity(itementity);
                    frameBlockEntity.clear();
                }
            }
        }
    }

    public void insertBlock(Level worldIn, BlockPos pos, BlockState state, BlockState handBlock) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof FrameBlockTile) {
            FrameBlockTile frameBlockEntity = (FrameBlockTile) tileentity;
            frameBlockEntity.clear();
            frameBlockEntity.setMimic(handBlock);
            worldIn.setBlock(pos, state.setValue(CONTAINS_BLOCK, Boolean.TRUE), 2);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level levelIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(worldIn, pos);

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    protected void onStartFalling(FallingBlockEntity fallingEntity) {
        super.onStartFalling(fallingEntity);
    }

    @Override
    public void onEndFalling(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity entity) {
        super.onEndFalling(worldIn, pos, fallingState, hitState, entity);

    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down())) && pos.getY() >= 0) {
            if (worldIn.getBlockEntity(pos) instanceof FrameBlockTile) {
                FrameBlockTile tileEntity = (FrameBlockTile) worldIn.getBlockEntity(pos);
                if (tileEntity.getMimic() != null) {
                    //FallingFrameBlockEntity fallingFrameBlockEntity = new FallingFrameBlockEntity(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, worldIn.getBlockState(pos), tileEntity.getMimic());
                    //this.onStartFalling(fallingFrameBlockEntity);
                    //worldIn.addFreshEntity(fallingFrameBlockEntity);
                }
            }
        }
    }

    private BlockEntity createFallingFrameBlockEntity(BlockState mimic) {
        return new FallingFrameBlockTile(mimic);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        if (state.getValue(LIGHT_LEVEL) > 15) {
            return 15;
        }
        return state.getValue(LIGHT_LEVEL);
    }*/
}
//========SOLI DEO GLORIA========//