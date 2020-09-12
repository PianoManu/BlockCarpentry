package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

import static mod.pianomanu.blockcarpentry.util.BCBlockStateProperties.CONTAINS_BLOCK;
import static mod.pianomanu.blockcarpentry.util.BCBlockStateProperties.LIGHT_LEVEL;

/**
 * Util class for certain frame block things like light level and textures
 *
 * @author PianoManu
 * @version 1.2 09/09/20
 */
public class BlockAppearanceHelper {
    public static int setLightLevel(ItemStack item, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        if (item.getItem() == Items.GLOWSTONE_DUST && state.get(LIGHT_LEVEL) < 13) {
            int count = player.getHeldItem(hand).getCount();
            world.setBlockState(pos, state.with(LIGHT_LEVEL, state.getBlock().getLightValue(state, world, pos) + 3));
            player.getHeldItem(hand).setCount(count - 1);
            player.sendStatusMessage(new TranslationTextComponent("Light Level: " + (state.get(LIGHT_LEVEL) + 3)), true);
        }
        if ((item.getItem() == Items.COAL || item.getItem() == Items.CHARCOAL) && state.get(LIGHT_LEVEL) < 15) {
            int count = player.getHeldItem(hand).getCount();
            world.setBlockState(pos, state.with(LIGHT_LEVEL, state.getBlock().getLightValue(state, world, pos) + 1));
            player.getHeldItem(hand).setCount(count - 1);
            player.sendStatusMessage(new TranslationTextComponent("Light Level: " + (state.get(LIGHT_LEVEL) + 1)), true);
        }
        if (item.getItem() == Items.GLOWSTONE_DUST && state.get(LIGHT_LEVEL) >= 13) {
            player.sendStatusMessage(new TranslationTextComponent("Light Level: " + state.get(LIGHT_LEVEL)), true);
        }
        if ((item.getItem() == Items.COAL || item.getItem() == Items.CHARCOAL) && state.get(LIGHT_LEVEL) == 15) {
            player.sendStatusMessage(new TranslationTextComponent("Light Level: " + state.get(LIGHT_LEVEL)), true);
        }
        return state.get(LIGHT_LEVEL);
    }

    public static void setTexture(ItemStack item, BlockState state, World world, PlayerEntity player, BlockPos pos) {

        if (item.getItem() == Registration.TEXTURE_WRENCH.get() && !player.isSneaking() && state.get(CONTAINS_BLOCK)) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof FrameBlockTile) {
                FrameBlockTile fte = (FrameBlockTile) tileEntity;
                if (fte.getTexture() < 5) { //six sides possible
                    fte.setTexture(fte.getTexture() + 1);
                } else {
                    fte.setTexture(0);
                }
                player.sendStatusMessage(new TranslationTextComponent("Texture: " + fte.getTexture()), true);
            }
        }
    }

    public static void setDesign(World world, BlockPos pos, PlayerEntity player, ItemStack item) {
        if (item.getItem() == Registration.CHISEL.get() && !player.isSneaking()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof FrameBlockTile) {
                FrameBlockTile fte = (FrameBlockTile) tileEntity;
                if (fte.getDesign() < fte.maxDesigns) {
                    fte.setDesign(fte.getDesign() + 1);
                } else {
                    fte.setDesign(0);
                }
                player.sendStatusMessage(new TranslationTextComponent("Design: " + fte.getDesign()), true);
            }
        }
    }

    public static void setDesignTexture(World world, BlockPos pos, PlayerEntity player, ItemStack item) {
        if (item.getItem() == Registration.PAINTBRUSH.get() && !player.isSneaking()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof FrameBlockTile) {
                FrameBlockTile fte = (FrameBlockTile) tileEntity;
                if (fte.getDesignTexture() < fte.maxDesignTextures) {
                    fte.setDesignTexture(fte.getDesignTexture() + 1);
                } else {
                    fte.setDesignTexture(0);
                }
                player.sendStatusMessage(new TranslationTextComponent("Design Texture: " + fte.getDesignTexture()), true);
            }
        }
    }

    public static void setGlassColor(World world, BlockPos pos, PlayerEntity player, Hand hand) {
        if (player.getHeldItem(hand).getItem().isIn(Tags.Items.DYES)) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof FrameBlockTile) {
                FrameBlockTile fte = (FrameBlockTile) tileEntity;
                fte.setGlassColor(dyeItemToInt(player.getHeldItem(hand).getItem()) + 1); //plus 1, because 0 is undyed glass
                //player.sendStatusMessage(new TranslationTextComponent("Glass Color: " + glassColorToString(fte.getGlassColor() - 1)), true);
            }
        }
    }

    private static String glassColorToString(int glassColor) {
        if (glassColor > 0 && glassColor < 17) {
            if (glassColor == 1) {
                return Items.WHITE_DYE.getName().getString();
            }
            if (glassColor == 2) {
                return Items.ORANGE_DYE.getName().getString();
            }
            if (glassColor == 3) {
                return Items.MAGENTA_DYE.getName().getString();
            }
            if (glassColor == 4) {
                return Items.LIGHT_BLUE_DYE.getName().getString();
            }
            if (glassColor == 5) {
                return Items.YELLOW_DYE.getName().getString();
            }
            if (glassColor == 6) {
                return Items.LIME_DYE.getName().getString();
            }
            if (glassColor == 7) {
                return Items.PINK_DYE.getName().getString();
            }
            if (glassColor == 8) {
                return Items.GRAY_DYE.getName().getString();
            }
            if (glassColor == 9) {
                return Items.LIGHT_GRAY_DYE.getName().getString();
            }
            if (glassColor == 10) {
                return Items.CYAN_DYE.getName().getString();
            }
            if (glassColor == 11) {
                return Items.PURPLE_DYE.getName().getString();
            }
            if (glassColor == 12) {
                return Items.BLUE_DYE.getName().getString();
            }
            if (glassColor == 13) {
                return Items.BROWN_DYE.getName().getString();
            }
            if (glassColor == 14) {
                return Items.GREEN_DYE.getName().getString();
            }
            if (glassColor == 15) {
                return Items.RED_DYE.getName().getString();
            }
            if (glassColor == 16) {
                return Items.BLACK_DYE.getName().getString();
            }
        }
        return Items.WHITE_DYE.getName().getString();
    }

    public static Integer dyeItemToInt(Item item) {
        List<Item> colors = new ArrayList<>(Tags.Items.DYES.getAllElements());
        if (colors.contains(item)) {
            if (item == Items.WHITE_DYE) {
                return 1;
            }
            if (item == Items.ORANGE_DYE) {
                return 2;
            }
            if (item == Items.MAGENTA_DYE) {
                return 3;
            }
            if (item == Items.LIGHT_BLUE_DYE) {
                return 4;
            }
            if (item == Items.YELLOW_DYE) {
                return 5;
            }
            if (item == Items.LIME_DYE) {
                return 6;
            }
            if (item == Items.PINK_DYE) {
                return 7;
            }
            if (item == Items.GRAY_DYE) {
                return 8;
            }
            if (item == Items.LIGHT_GRAY_DYE) {
                return 9;
            }
            if (item == Items.CYAN_DYE) {
                return 10;
            }
            if (item == Items.PURPLE_DYE) {
                return 11;
            }
            if (item == Items.BLUE_DYE) {
                return 12;
            }
            if (item == Items.BROWN_DYE) {
                return 13;
            }
            if (item == Items.GREEN_DYE) {
                return 14;
            }
            if (item == Items.RED_DYE) {
                return 15;
            }
            if (item == Items.BLACK_DYE) {
                return 16;
            }
            return colors.indexOf(item);
        }
        return 0;
    }
}
//========SOLI DEO GLORIA========//