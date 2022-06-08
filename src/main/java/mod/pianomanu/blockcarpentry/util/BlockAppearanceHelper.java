package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.block.BedFrameBlock;
import mod.pianomanu.blockcarpentry.block.SixWaySlabFrameBlock;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import java.util.ArrayList;
import java.util.List;

import static mod.pianomanu.blockcarpentry.util.BCBlockStateProperties.CONTAINS_BLOCK;
import static mod.pianomanu.blockcarpentry.util.BCBlockStateProperties.LIGHT_LEVEL;

/**
 * Util class for certain frame block things like light level and textures
 *
 * @author PianoManu
 * @version 1.0 05/23/22
 */
public class BlockAppearanceHelper {
    public static boolean setLightLevel(ItemStack item, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand) {
        if (item.getItem() == Items.GLOWSTONE_DUST && state.getValue(LIGHT_LEVEL) < 13) {
            int count = player.getItemInHand(hand).getCount();
            world.setBlock(pos, state.setValue(LIGHT_LEVEL, state.getBlock().getLightEmission(state, world, pos) + 3), 3);
            player.getItemInHand(hand).setCount(count - 1);
            player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.light_level", (state.getValue(LIGHT_LEVEL) + 3)), true);
            return true;
        }
        if ((item.getItem() == Items.COAL || item.getItem() == Items.CHARCOAL) && state.getValue(LIGHT_LEVEL) < 15) {
            int count = player.getItemInHand(hand).getCount();
            world.setBlock(pos, state.setValue(LIGHT_LEVEL, state.getBlock().getLightEmission(state, world, pos) + 1), 3);
            player.getItemInHand(hand).setCount(count - 1);
            player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.light_level", (state.getValue(LIGHT_LEVEL) + 1)), true);
            return true;
        }
        if (item.getItem() == Items.GLOWSTONE_DUST && state.getValue(LIGHT_LEVEL) >= 13) {
            player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.light_level", state.getValue(LIGHT_LEVEL)), true);
        }
        if ((item.getItem() == Items.COAL || item.getItem() == Items.CHARCOAL) && state.getValue(LIGHT_LEVEL) == 15) {
            player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.light_level", state.getValue(LIGHT_LEVEL)), true);
        }
        return false;
    }

    public static boolean setTexture(ItemStack item, BlockState state, Level world, Player player, BlockPos pos) {
        if (item.getItem() == Registration.TEXTURE_WRENCH.get() && !player.isCrouching() && state.getValue(CONTAINS_BLOCK) && mod.pianomanu.blockcarpentry.util.Tags.isFrameBlock(state.getBlock())) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof FrameBlockTile fte) {
                if (fte.getTexture() < 5) { //six sides possible
                    fte.setTexture(fte.getTexture() + 1);
                } else {
                    fte.setTexture(0);
                }
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.texture", fte.getTexture()), true);
            }
            if (tileEntity instanceof BedFrameTile fte) {
                if (fte.getTexture() < 5) { //six sides possible
                    fte.setTexture(fte.getTexture() + 1);
                } else {
                    fte.setTexture(0);
                }
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.texture", fte.getTexture()), true);
            }
            if (tileEntity instanceof ChestFrameBlockEntity fte) {
                if (fte.getTexture() < 5) { //six sides possible
                    fte.setTexture(fte.getTexture() + 1);
                } else {
                    fte.setTexture(0);
                }
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.texture", fte.getTexture()), true);
            }
            if (tileEntity instanceof TwoBlocksFrameBlockTile fte) {
                if (!state.getValue(SixWaySlabFrameBlock.DOUBLE_SLAB)) {
                    if (fte.getTexture_1() < 5) {
                        fte.setTexture_1(fte.getTexture_1() + 1);
                    } else {
                        fte.setTexture_1(0);
                    }
                    player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.texture", fte.getTexture_1()), true);
                } else {
                    if (fte.getTexture_2() < 5) {
                        fte.setTexture_2(fte.getTexture_2() + 1);
                    } else {
                        fte.setTexture_2(0);
                    }
                    player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.texture", fte.getTexture_2()), true);
                }
            }
            if (tileEntity instanceof DaylightDetectorFrameTileEntity fte) {
                if (fte.getTexture() < 5) { //six sides possible
                    fte.setTexture(fte.getTexture() + 1);
                } else {
                    fte.setTexture(0);
                }
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.texture", fte.getTexture()), true);
            }
            return true;
        }
        return false;
    }

    public static boolean setDesign(Level world, BlockPos pos, Player player, ItemStack item) {
        if (item.getItem() == Registration.CHISEL.get() && !player.isCrouching()) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof FrameBlockTile fte) {
                if (fte.getDesign() < fte.maxDesigns) {
                    fte.setDesign(fte.getDesign() + 1);
                } else {
                    fte.setDesign(0);
                }
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.design", fte.getDesign()), true);
            }
            if (tileEntity instanceof BedFrameTile fte) {
                if (fte.getDesign() < fte.maxDesigns) {
                    fte.setDesign(fte.getDesign() + 1);
                } else {
                    fte.setDesign(0);
                }
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.design", fte.getDesign()), true);
            }
            if (tileEntity instanceof ChestFrameBlockEntity fte) {
                if (fte.getDesign() < fte.maxDesigns) {
                    fte.setDesign(fte.getDesign() + 1);
                } else {
                    fte.setDesign(0);
                }
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.design", fte.getDesign()), true);
            }
            if (tileEntity instanceof TwoBlocksFrameBlockTile fte) {
                BlockState state = world.getBlockState(pos);
                if (!state.getValue(SixWaySlabFrameBlock.DOUBLE_SLAB)) {
                    if (fte.getDesign_1() < fte.maxDesigns) {
                        fte.setDesign_1(fte.getDesign_1() + 1);
                    } else {
                        fte.setDesign_1(0);
                    }
                    player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.design", fte.getDesign_1()), true);
                } else {
                    if (fte.getDesign_2() < fte.maxDesigns) {
                        fte.setDesign_2(fte.getDesign_2() + 1);
                    } else {
                        fte.setDesign_2(0);
                    }
                    player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.design", fte.getDesign_2()), true);
                }
            }
            if (tileEntity instanceof DaylightDetectorFrameTileEntity fte) {
                if (fte.getDesign() < fte.maxDesigns) {
                    fte.setDesign(fte.getDesign() + 1);
                } else {
                    fte.setDesign(0);
                }
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.design", fte.getDesign()), true);
            }
            return true;
        }
        return false;
    }

    public static boolean setDesignTexture(Level world, BlockPos pos, Player player, ItemStack item) {
        if (item.getItem() == Registration.PAINTBRUSH.get() && !player.isCrouching()) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof FrameBlockTile fte) {
                if (fte.getDesignTexture() < fte.maxDesignTextures) {
                    fte.setDesignTexture(fte.getDesignTexture() + 1);
                } else {
                    fte.setDesignTexture(0);
                }
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.design_texture", fte.getDesignTexture()), true);
            }
            if (tileEntity instanceof BedFrameTile fte) {
                if (fte.getDesignTexture() < 7) {
                    fte.setDesignTexture(fte.getDesignTexture() + 1);
                } else {
                    fte.setDesignTexture(0);
                }
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.design_texture", fte.getDesignTexture()), true);
            }
            if (tileEntity instanceof ChestFrameBlockEntity fte) {
                if (fte.getDesignTexture() < fte.maxDesignTextures) {
                    fte.setDesignTexture(fte.getDesignTexture() + 1);
                } else {
                    fte.setDesignTexture(0);
                }
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.design_texture", fte.getDesignTexture()), true);
            }
            return true;
        }
        return false;
    }

    public static boolean setGlassColor(Level world, BlockPos pos, Player player, InteractionHand hand) {
        if (BlockSavingHelper.isDyeItem(player.getItemInHand(hand).getItem())) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof FrameBlockTile fte) {
                fte.setGlassColor(dyeItemToInt(player.getItemInHand(hand).getItem()) + 1); //plus 1, because 0 is undyed glass
            }
            if (tileEntity instanceof DaylightDetectorFrameTileEntity fte) {
                fte.setGlassColor(dyeItemToInt(player.getItemInHand(hand).getItem()) + 1); //plus 1, because 0 is undyed glass
            }
            return true;
        }
        return false;
    }

    public static boolean setWoolColor(Level world, BlockPos pos, Player player, InteractionHand hand) {
        if (BlockSavingHelper.isDyeItem(player.getItemInHand(hand).getItem())) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof BedFrameTile fte) {
                if (world.getBlockState(pos).getValue(BedFrameBlock.PART) == BedPart.FOOT) {
                    fte.setBlanketColor(dyeItemToInt(player.getItemInHand(hand).getItem()));
                }
                if (world.getBlockState(pos).getValue(BedFrameBlock.PART) == BedPart.HEAD) {
                    fte.setPillowColor(dyeItemToInt(player.getItemInHand(hand).getItem()));
                }
                //player.displayClientMessage(new TranslatableComponent("Glass Color: " + glassColorToString(fte.getGlassColor()-1)), true);
            }
            return true;
        }
        return false;
    }

    //reminder to myself: DO NOT USE, CAUSES SERVER CRASHES, fix or remove
    /*private static String glassColorToString(int glassColor) {
        List<String> colors = new ArrayList<>();
        for (Item item : Tags.Items.DYES) {
            colors.add(item.getDescription().getString());
        }
        return colors.get(glassColor);
    }*/

    public static Integer dyeItemToInt(Item item) {
        List<Item> colors = new ArrayList<>(BlockSavingHelper.getDyeItems());
        if (colors.contains(item)) {
            return colors.indexOf(item);
        }
        return 0;
    }

    public static boolean setOverlay(Level world, BlockPos pos, Player player, ItemStack itemStack) {
        if (itemStack.getItem().equals(Items.GRASS)) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof FrameBlockTile fte) {
                if (fte.getOverlay() == 1) {
                    fte.setOverlay(2);
                    player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.grass_overlay_large"), true);
                } else {
                    fte.setOverlay(1);
                    player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.grass_overlay"), true);
                }
            }
            if (tileEntity instanceof TwoBlocksFrameBlockTile fte) {
                BlockState state = world.getBlockState(pos);
                if (!state.getValue(SixWaySlabFrameBlock.DOUBLE_SLAB)) {
                    if (fte.getOverlay_1() == 1) {
                        fte.setOverlay_1(2);
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.grass_overlay_large"), true);
                    } else {
                        fte.setOverlay_1(1);
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.grass_overlay"), true);
                    }
                } else {
                    if (fte.getOverlay_2() == 1) {
                        fte.setOverlay_2(2);
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.grass_overlay_large"), true);
                    } else {
                        fte.setOverlay_2(1);
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.grass_overlay"), true);
                    }
                }
            }
            return true;
        }
        if (itemStack.getItem().equals(Items.SNOWBALL)) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof FrameBlockTile fte) {
                if (fte.getOverlay() == 3) {
                    fte.setOverlay(4);
                    player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.snow_overlay_small"), true);
                } else {
                    fte.setOverlay(3);
                    player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.snow_overlay"), true);
                }
            }
            if (tileEntity instanceof TwoBlocksFrameBlockTile) {
                BlockState state = world.getBlockState(pos);
                if (!state.getValue(SixWaySlabFrameBlock.DOUBLE_SLAB)) {
                    TwoBlocksFrameBlockTile fte = (TwoBlocksFrameBlockTile) tileEntity;
                    if (fte.getOverlay_1() == 3) {
                        fte.setOverlay_1(4);
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.snow_overlay_small"), true);
                    } else {
                        fte.setOverlay_1(3);
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.snow_overlay"), true);
                    }
                } else {
                    TwoBlocksFrameBlockTile fte = (TwoBlocksFrameBlockTile) tileEntity;
                    if (fte.getOverlay_2() == 3) {
                        fte.setOverlay_2(4);
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.snow_overlay_small"), true);
                    } else {
                        fte.setOverlay_2(3);
                        player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.snow_overlay"), true);
                    }
                }
            }
            return true;
        }
        if (itemStack.getItem().equals(Items.VINE)) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof FrameBlockTile fte) {
                fte.setOverlay(5);
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.vine_overlay"), true);
            }
            if (tileEntity instanceof TwoBlocksFrameBlockTile) {
                BlockState state = world.getBlockState(pos);
                if (!state.getValue(SixWaySlabFrameBlock.DOUBLE_SLAB)) {
                    TwoBlocksFrameBlockTile fte = (TwoBlocksFrameBlockTile) tileEntity;
                    fte.setOverlay_1(5);
                    player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.vine_overlay"), true);
                } else {
                    TwoBlocksFrameBlockTile fte = (TwoBlocksFrameBlockTile) tileEntity;
                    fte.setOverlay_2(5);
                    player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.vine_overlay"), true);
                }
            }
            return true;
        }
        if (itemStack.getItem().equals(Items.GUNPOWDER)) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof FrameBlockTile fte) {
                if (fte.getOverlay() > 5 && fte.getOverlay() < 10) {
                    fte.setOverlay(fte.getOverlay() + 1);
                } else fte.setOverlay(6);
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.special_overlay", (fte.getOverlay() - 5)), true);
            }
            if (tileEntity instanceof TwoBlocksFrameBlockTile) {
                BlockState state = world.getBlockState(pos);
                if (!state.getValue(SixWaySlabFrameBlock.DOUBLE_SLAB)) {
                    TwoBlocksFrameBlockTile fte = (TwoBlocksFrameBlockTile) tileEntity;
                    if (fte.getOverlay_1() > 5 && fte.getOverlay_1() < 10) {
                        fte.setOverlay_1(fte.getOverlay_1() + 1);
                    } else fte.setOverlay_1(6);
                    player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.special_overlay", (fte.getOverlay_1() - 5)), true);
                } else {
                    TwoBlocksFrameBlockTile fte = (TwoBlocksFrameBlockTile) tileEntity;
                    if (fte.getOverlay_2() > 5 && fte.getOverlay_2() < 10) {
                        fte.setOverlay_2(fte.getOverlay_2() + 1);
                    } else fte.setOverlay_2(6);
                    player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.special_overlay", (fte.getOverlay_2() - 5)), true);
                }
            }
            return true;
        }
        return false;
    }

    public static int setTintIndex(BlockState state) {
        Block b = state.getBlock();
        if (b instanceof GrassBlock || b instanceof LeavesBlock) {
            return 1;
        }
        return -1;
    }

    public static boolean setRotation(Level world, BlockPos pos, Player player, ItemStack itemStack) {
        if (itemStack.getItem() == Registration.TEXTURE_WRENCH.get() && !player.isCrouching() && mod.pianomanu.blockcarpentry.util.Tags.isIllusionBlock(world.getBlockState(pos).getBlock())) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof FrameBlockTile fte) {
                if (fte.getRotation() < 11) {
                    fte.setRotation(fte.getRotation() + 1);
                } else {
                    fte.setRotation(0);
                }
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.rotation", fte.getRotation()), true);
            }
            if (tileEntity instanceof BedFrameTile fte) {
                if (fte.getRotation() < 11) {
                    fte.setRotation(fte.getRotation() + 1);
                } else {
                    fte.setRotation(0);
                }
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.rotation", fte.getRotation()), true);
            }
            if (tileEntity instanceof ChestFrameBlockEntity fte) {
                if (fte.getRotation() < 11) {
                    fte.setRotation(fte.getRotation() + 1);
                } else {
                    fte.setRotation(0);
                }
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.rotation", fte.getRotation()), true);
            }
            if (tileEntity instanceof TwoBlocksFrameBlockTile fte) {
                BlockState state = world.getBlockState(pos);
                if (!state.getValue(SixWaySlabFrameBlock.DOUBLE_SLAB)) {
                    if (fte.getRotation_1() < 11) {
                        fte.setRotation_1(fte.getRotation_1() + 1);
                    } else {
                        fte.setRotation_1(0);
                    }
                    player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.rotation", fte.getRotation_1()), true);
                } else {
                    if (fte.getRotation_2() < 11) {
                        fte.setRotation_2(fte.getRotation_2() + 1);
                    } else {
                        fte.setRotation_2(0);
                    }
                    player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.rotation", fte.getRotation_2()), true);
                }
            }
            if (tileEntity instanceof DaylightDetectorFrameTileEntity fte) {
                if (fte.getRotation() < 7) {
                    fte.setRotation(fte.getRotation() + 1);
                } else {
                    fte.setRotation(0);
                }
                player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.rotation", fte.getRotation()), true);
            }
            return true;
        }
        return false;
    }
}
//========SOLI DEO GLORIA========//