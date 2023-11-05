package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * This class ensures that blocks of grass take on the correct color
 *
 * @author PianoManu
 * @version 1.2 10/05/23
 */
public class BlockColorHandler implements BlockColor {
    public static final BlockColor INSTANCE = new BlockColorHandler();
    private static final Logger LOGGER = LogManager.getLogger();

    public static void registerBlockColors() {
        // DEBUG
        LOGGER.info("Registering block color handler");

        //Causing green color bug...
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.SLAB_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.STAIRS_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.BUTTON_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.DOOR_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.PRESSURE_PLATE_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.TRAPDOOR_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.FENCE_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.BED_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.WALL_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.LADDER_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.CHEST_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.FENCE_GATE_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.CARPET_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.PANE_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.DAYLIGHT_DETECTOR_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.LAYERED_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.STANDING_SIGN_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.WALL_SIGN_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.SLOPE_FRAMEBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.EDGED_SLOPE_FRAMEBLOCK.get());

        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.ILLUSION_BLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.SLAB_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.STAIRS_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.BUTTON_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.DOOR_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.PRESSURE_PLATE_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.TRAPDOOR_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.FENCE_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.BED_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.WALL_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.LADDER_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.CHEST_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.FENCE_GATE_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.CARPET_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.PANE_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.DAYLIGHT_DETECTOR_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.LAYERED_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.STANDING_SIGN_ILLUSIONBLOCK.get());
        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.WALL_SIGN_ILLUSION_BLOCK.get());

        LOGGER.info("Registered block color handler");
    }

    @Override
    public int getColor(@Nonnull BlockState state, @Nullable BlockAndTintGetter blockAndTint, @Nullable BlockPos pos, int tintIndex) {
        if (blockAndTint != null && pos != null) {
            BlockEntity te = blockAndTint.getBlockEntity(pos);
            if (te instanceof FrameBlockTile && state.getValue(BCBlockStateProperties.CONTAINS_BLOCK)) {
                BlockState containedBlock = ((FrameBlockTile) te).getMimic();
                try {
                    if (containedBlock.getBlock() instanceof GrassBlock) {
                        return BiomeColors.getAverageGrassColor(blockAndTint, pos);
                    } else if (containedBlock.getBlock() instanceof LeavesBlock) {
                        return BiomeColors.getAverageFoliageColor(blockAndTint, pos);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        return BiomeColors.getAverageGrassColor(Objects.requireNonNull(blockAndTint), Objects.requireNonNull(pos));
    }
}
//========SOLI DEO GLORIA========//