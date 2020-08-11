package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

import static mod.pianomanu.blockcarpentry.util.BCBlockStateProperties.*;

public class BlockAppearanceHelper {
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

    public static void setTexture(ItemStack item, BlockState state, World world, PlayerEntity player, BlockPos pos) {
        if (item.getItem() == Registration.TEXTURE_WRENCH.get() && !player.isSneaking() && state.get(CONTAINS_BLOCK)) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof FrameBlockTile) {
                FrameBlockTile fte = (FrameBlockTile) tileEntity;
                List<TextureAtlasSprite> texture = TextureHelper.getTextureListFromBlock(fte.getMimic().getBlock());
                if (state.get(TEXTURE) < texture.size()-1 && state.get(TEXTURE) < 5) {
                    world.setBlockState(pos, state.with(TEXTURE, state.get(TEXTURE) + 1));
                } else {
                    world.setBlockState(pos, state.with(TEXTURE, 0));
                }
            }
        }
    }

    public static void setDesign() {

    }
}
