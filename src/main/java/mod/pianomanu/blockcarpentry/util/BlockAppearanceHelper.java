package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.block.BedFrameBlock;
import mod.pianomanu.blockcarpentry.block.SixWaySlabFrameBlock;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
 * @version 1.6 10/02/23
 */
public class BlockAppearanceHelper {
    public static boolean setAll(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player) {
        return BlockAppearanceHelper.setLightLevel(itemStack, state, level, pos, player) ||
                BlockAppearanceHelper.setTexture(itemStack, state, level, player, pos) ||
                BlockAppearanceHelper.setDesign(level, pos, player, itemStack) ||
                BlockAppearanceHelper.setDesignTexture(level, pos, player, itemStack) ||
                BlockAppearanceHelper.setColor(level, pos, itemStack) ||
                BlockAppearanceHelper.setOverlay(level, pos, player, itemStack) ||
                BlockAppearanceHelper.setRotation(level, pos, player, itemStack);
    }

    public static boolean setLightLevel(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player) {
        if (BCModConfig.LIGHTING_ENABLED.get()) {
            if (itemStack.getItem() == Items.GLOWSTONE_DUST && state.getValue(LIGHT_LEVEL) < 13) {
                int count = itemStack.getCount();
                level.setBlock(pos, state.setValue(LIGHT_LEVEL, state.getBlock().getLightEmission(state, level, pos) + 3), 3);
                itemStack.setCount(count - 1);
                player.displayClientMessage(Component.translatable("message.blockcarpentry.light_level", (state.getValue(LIGHT_LEVEL) + 3)), true);
                return true;
            }
            if ((itemStack.getItem() == Items.COAL || itemStack.getItem() == Items.CHARCOAL) && state.getValue(LIGHT_LEVEL) < 15) {
                int count = itemStack.getCount();
                level.setBlock(pos, state.setValue(LIGHT_LEVEL, state.getBlock().getLightEmission(state, level, pos) + 1), 3);
                itemStack.setCount(count - 1);
                player.displayClientMessage(Component.translatable("message.blockcarpentry.light_level", (state.getValue(LIGHT_LEVEL) + 1)), true);
                return true;
            }
            if (itemStack.getItem() == Items.GLOWSTONE_DUST && state.getValue(LIGHT_LEVEL) >= 13) {
                player.displayClientMessage(Component.translatable("message.blockcarpentry.light_level", state.getValue(LIGHT_LEVEL)), true);
            }
            if ((itemStack.getItem() == Items.COAL || itemStack.getItem() == Items.CHARCOAL) && state.getValue(LIGHT_LEVEL) == 15) {
                player.displayClientMessage(Component.translatable("message.blockcarpentry.light_level", state.getValue(LIGHT_LEVEL)), true);
            }
        }
        return false;
    }

    public static boolean setTexture(ItemStack itemStack, BlockState state, Level level, Player player, BlockPos pos) {
        if (itemStack.getItem() == Registration.TEXTURE_WRENCH.get() && !player.isCrouching() && state.getValue(CONTAINS_BLOCK) && mod.pianomanu.blockcarpentry.util.Tags.isFrameBlock(state.getBlock())) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof FrameBlockTile fte) {
                if (fte.getTexture() < 5) { //six sides possible
                    fte.setTexture(fte.getTexture() + 1);
                } else {
                    fte.setTexture(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.texture", fte.getTexture()), true);
            }
            if (tileEntity instanceof BedFrameTile fte) {
                if (fte.getTexture() < 5) { //six sides possible
                    fte.setTexture(fte.getTexture() + 1);
                } else {
                    fte.setTexture(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.texture", fte.getTexture()), true);
            }
            if (tileEntity instanceof ChestFrameBlockEntity fte) {
                if (fte.getTexture() < 5) { //six sides possible
                    fte.setTexture(fte.getTexture() + 1);
                } else {
                    fte.setTexture(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.texture", fte.getTexture()), true);
            }
            if (tileEntity instanceof TwoBlocksFrameBlockTile fte) {
                if (!state.getValue(SixWaySlabFrameBlock.DOUBLE_SLAB)) {
                    if (fte.getTexture() < 5) {
                        fte.setTexture(fte.getTexture() + 1);
                    } else {
                        fte.setTexture(0);
                    }
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.texture", fte.getTexture()), true);
                } else {
                    if (fte.getTexture_2() < 5) {
                        fte.setTexture_2(fte.getTexture_2() + 1);
                    } else {
                        fte.setTexture_2(0);
                    }
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.texture", fte.getTexture_2()), true);
                }
            }
            if (tileEntity instanceof DaylightDetectorFrameTileEntity fte) {
                if (fte.getTexture() < 5) { //six sides possible
                    fte.setTexture(fte.getTexture() + 1);
                } else {
                    fte.setTexture(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.texture", fte.getTexture()), true);
            }
            if (tileEntity instanceof SignFrameTile fte) {
                if (fte.getTexture() < 5) { //six sides possible
                    fte.setTexture(fte.getTexture() + 1);
                } else {
                    fte.setTexture(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.texture", fte.getTexture()), true);
            }
            return true;
        }
        return false;
    }

    public static boolean setDesign(Level level, BlockPos pos, Player player, ItemStack itemStack) {
        if (itemStack.getItem() == Registration.CHISEL.get() && !player.isCrouching()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof FrameBlockTile fte) {
                if (fte.getDesign() < fte.maxDesigns) {
                    fte.setDesign(fte.getDesign() + 1);
                } else {
                    fte.setDesign(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.design", fte.getDesign()), true);
            }
            if (tileEntity instanceof BedFrameTile fte) {
                if (fte.getDesign() < fte.maxDesigns) {
                    fte.setDesign(fte.getDesign() + 1);
                } else {
                    fte.setDesign(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.design", fte.getDesign()), true);
            }
            if (tileEntity instanceof ChestFrameBlockEntity fte) {
                if (fte.getDesign() < fte.maxDesigns) {
                    fte.setDesign(fte.getDesign() + 1);
                } else {
                    fte.setDesign(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.design", fte.getDesign()), true);
            }
            if (tileEntity instanceof TwoBlocksFrameBlockTile fte) {
                BlockState state = level.getBlockState(pos);
                if (!state.getValue(SixWaySlabFrameBlock.DOUBLE_SLAB)) {
                    if (fte.getDesign() < fte.maxDesigns) {
                        fte.setDesign(fte.getDesign() + 1);
                    } else {
                        fte.setDesign(0);
                    }
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.design", fte.getDesign()), true);
                } else {
                    if (fte.getDesign_2() < fte.maxDesigns) {
                        fte.setDesign_2(fte.getDesign_2() + 1);
                    } else {
                        fte.setDesign_2(0);
                    }
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.design", fte.getDesign_2()), true);
                }
            }
            if (tileEntity instanceof DaylightDetectorFrameTileEntity fte) {
                if (fte.getDesign() < fte.maxDesigns) {
                    fte.setDesign(fte.getDesign() + 1);
                } else {
                    fte.setDesign(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.design", fte.getDesign()), true);
            }
            return true;
        }
        return false;
    }

    public static boolean setDesignTexture(Level level, BlockPos pos, Player player, ItemStack itemStack) {
        if (itemStack.getItem() == Registration.PAINTBRUSH.get() && !player.isCrouching()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof FrameBlockTile fte) {
                if (fte.getDesignTexture() < fte.maxDesignTextures) {
                    fte.setDesignTexture(fte.getDesignTexture() + 1);
                } else {
                    fte.setDesignTexture(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.design_texture", fte.getDesignTexture()), true);
            }
            if (tileEntity instanceof BedFrameTile fte) {
                if (fte.getDesignTexture() < 7) {
                    fte.setDesignTexture(fte.getDesignTexture() + 1);
                } else {
                    fte.setDesignTexture(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.design_texture", fte.getDesignTexture()), true);
            }
            if (tileEntity instanceof ChestFrameBlockEntity fte) {
                if (fte.getDesignTexture() < fte.maxDesignTextures) {
                    fte.setDesignTexture(fte.getDesignTexture() + 1);
                } else {
                    fte.setDesignTexture(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.design_texture", fte.getDesignTexture()), true);
            }
            return true;
        }
        return false;
    }

    public static boolean setColor(Level level, BlockPos pos, ItemStack itemStack) {
        if (FrameInteractionItems.isDyeItem(itemStack.getItem())) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof FrameBlockTile fte) {
                fte.setGlassColor(dyeItemToInt(itemStack.getItem()) + 1); //plus 1, because 0 is undyed glass
                return true;
            }
            if (tileEntity instanceof DaylightDetectorFrameTileEntity fte) {
                fte.setGlassColor(dyeItemToInt(itemStack.getItem()) + 1); //plus 1, because 0 is undyed glass
                return true;
            }
            if (tileEntity instanceof BedFrameTile fte) {
                if (level.getBlockState(pos).getValue(BedFrameBlock.PART) == BedPart.FOOT) {
                    fte.setBlanketColor(dyeItemToInt(itemStack.getItem()));
                    return true;
                }
                if (level.getBlockState(pos).getValue(BedFrameBlock.PART) == BedPart.HEAD) {
                    fte.setPillowColor(dyeItemToInt(itemStack.getItem()));
                    return true;
                }
            }

        }
        return false;
    }

    public static Integer dyeItemToInt(Item item) {
        List<Item> colors = new ArrayList<>(FrameInteractionItems.getDyeItems());
        if (colors.contains(item)) {
            return colors.indexOf(item);
        }
        return 0;
    }

    public static boolean setOverlay(Level level, BlockPos pos, Player player, ItemStack itemStack) {
        if (itemStack.getItem().equals(Items.GRASS)) {
            return OverlayHelper.setOverlay(level, pos, player, 1, 2);
        }
        if (itemStack.getItem().equals(Items.SNOWBALL)) {
            return OverlayHelper.setOverlay(level, pos, player, 3, 4);
        }
        if (itemStack.getItem().equals(Items.VINE)) {
            return OverlayHelper.setOverlay(level, pos, player, 5, 5);
        }
        if (itemStack.getItem().equals(Items.GUNPOWDER)) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof FrameBlockTile fte) {
                return OverlayHelper.setOverlay(level, pos, player, 6, 10, 5);
            }
            if (tileEntity instanceof TwoBlocksFrameBlockTile fte) {
                BlockState state = level.getBlockState(pos);
                return OverlayHelper.setOverlay(level, pos, player, 6, 10, 5);
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

    public static boolean setRotation(Level level, BlockPos pos, Player player, ItemStack itemStack) {
        if (itemStack.getItem() == Registration.TEXTURE_WRENCH.get() && !player.isCrouching() && mod.pianomanu.blockcarpentry.util.Tags.isIllusionBlock(level.getBlockState(pos).getBlock())) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof FrameBlockTile fte) {
                if (fte.getRotation() < 11) {
                    fte.setRotation(fte.getRotation() + 1);
                } else {
                    fte.setRotation(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.rotation", fte.getRotation()), true);
            }
            if (tileEntity instanceof BedFrameTile fte) {
                if (fte.getRotation() < 11) {
                    fte.setRotation(fte.getRotation() + 1);
                } else {
                    fte.setRotation(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.rotation", fte.getRotation()), true);
            }
            if (tileEntity instanceof ChestFrameBlockEntity fte) {
                if (fte.getRotation() < 11) {
                    fte.setRotation(fte.getRotation() + 1);
                } else {
                    fte.setRotation(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.rotation", fte.getRotation()), true);
            }
            if (tileEntity instanceof TwoBlocksFrameBlockTile fte) {
                BlockState state = level.getBlockState(pos);
                if (!state.getValue(SixWaySlabFrameBlock.DOUBLE_SLAB)) {
                    if (fte.getRotation() < 11) {
                        fte.setRotation(fte.getRotation() + 1);
                    } else {
                        fte.setRotation(0);
                    }
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.rotation", fte.getRotation()), true);
                } else {
                    if (fte.getRotation_2() < 11) {
                        fte.setRotation_2(fte.getRotation_2() + 1);
                    } else {
                        fte.setRotation_2(0);
                    }
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.rotation", fte.getRotation_2()), true);
                }
            }
            if (tileEntity instanceof DaylightDetectorFrameTileEntity fte) {
                if (fte.getRotation() < 7) {
                    fte.setRotation(fte.getRotation() + 1);
                } else {
                    fte.setRotation(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.rotation", fte.getRotation()), true);
            }
            if (tileEntity instanceof SignFrameTile fte) {
                if (fte.getRotation() < 7) {
                    fte.setRotation(fte.getRotation() + 1);
                } else {
                    fte.setRotation(0);
                }
                player.displayClientMessage(Component.translatable("message.blockcarpentry.rotation", fte.getRotation()), true);
            }
            return true;
        }
        return false;
    }
}
//========SOLI DEO GLORIA========//