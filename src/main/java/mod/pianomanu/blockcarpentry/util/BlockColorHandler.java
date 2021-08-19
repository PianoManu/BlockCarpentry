package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.biome.BiomeColors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * This class ensures that blocks of grass take on the correct color
 *
 * @author PianoManu
 * @version 1.7 08/19/21
 */
public class BlockColorHandler implements IBlockColor {
    public static final IBlockColor INSTANCE = new BlockColorHandler();
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

        LOGGER.info("Registered block color handler");
    }

    @Override
    public int getColor(@Nonnull BlockState state, @Nullable IBlockDisplayReader lightReader, @Nullable BlockPos pos, int tintIndex) {
        //TODO does this work?
        if (Objects.requireNonNull(state.getBlock().getRegistryName()).getNamespace().equals(BlockCarpentryMain.MOD_ID) && lightReader != null && pos != null) {
            TileEntity te = lightReader.getTileEntity(pos);
            if (te instanceof FrameBlockTile && state.get(BCBlockStateProperties.CONTAINS_BLOCK)) {
                BlockState containedBlock = ((FrameBlockTile) te).getMimic();
                if (containedBlock != null) {
                    if (containedBlock.getBlock() instanceof GrassBlock) {
                        return BiomeColors.getGrassColor(lightReader, pos);
                    } else if (containedBlock.getBlock() instanceof LeavesBlock) {
                        return BiomeColors.getFoliageColor(lightReader, pos);
                    }
                }
                //return Minecraft.getInstance().getBlockColors().getColor(containedBlock, lightReader, pos, tintIndex);
            }
        }
        return BiomeColors.getGrassColor(Objects.requireNonNull(lightReader), Objects.requireNonNull(pos));
    }
}
//========SOLI DEO GLORIA========//