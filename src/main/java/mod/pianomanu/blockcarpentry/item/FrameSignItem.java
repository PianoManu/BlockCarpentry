package mod.pianomanu.blockcarpentry.item;

import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;

public class FrameSignItem extends SignItem {
    public FrameSignItem(Properties propertiesIn, Block floorBlockIn, Block wallBlockIn) {
        super(propertiesIn, floorBlockIn, wallBlockIn);
    }

    /*protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable Player player, ItemStack stack, BlockState state) {
        boolean flag = super.onBlockPlaced(pos, worldIn, player, stack, state);
        if (!worldIn.isClientSide && !flag && player != null) {
            System.out.println("platziert");
            player.openSignEditor((SignFrameTile) worldIn.getBlockEntity(pos));
        }

        return flag;
    }*/
}
//========SOLI DEO GLORIA========//