package mod.pianomanu.blockcarpentry.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static mod.pianomanu.blockcarpentry.util.BCBlockStateProperties.LIGHT_LEVEL;

public class LightLevelHelper {
    public static int setLightLevel(ItemStack item, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        int lightLevel=0;
        if (item.getItem()== Items.GLOWSTONE_DUST && state.get(LIGHT_LEVEL)<13) {
            int count = player.getHeldItem(hand).getCount();
            lightLevel=lightLevel+3;
            world.setBlockState(pos,state.with(LIGHT_LEVEL, state.getLightValue()+3));
            player.getHeldItem(hand).setCount(count-1);
        }
        if ((item.getItem() == Items.COAL || item.getItem() == Items.CHARCOAL) && state.get(LIGHT_LEVEL)<15) {
            int count = player.getHeldItem(hand).getCount();
            lightLevel=lightLevel+1;
            world.setBlockState(pos,state.with(LIGHT_LEVEL, state.getLightValue()+1));
            player.getHeldItem(hand).setCount(count-1);
        }
        return lightLevel;
    }
}
