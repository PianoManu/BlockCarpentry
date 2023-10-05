package mod.pianomanu.blockcarpentry.item;

import net.minecraft.block.Block;
import net.minecraft.item.SignItem;

public class FrameSignItem extends SignItem {
    public FrameSignItem(Properties propertiesIn, Block floorBlockIn, Block wallBlockIn) {
        super(propertiesIn, floorBlockIn, wallBlockIn);
    }

    /*protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        boolean flag = super.onBlockPlaced(pos, worldIn, player, stack, state);
        if (!worldIn.isClientSide && !flag && player != null) {
            System.out.println("platziert");
            player.openSignEditor((SignFrameTile) worldIn.getTileEntity(pos));
        }

        return flag;
    }*/
}
//========SOLI DEO GLORIA========//