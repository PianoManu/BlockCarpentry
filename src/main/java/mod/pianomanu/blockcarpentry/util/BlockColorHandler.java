package mod.pianomanu.blockcarpentry.util;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GrassColors;
import net.minecraft.world.ILightReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockColorHandler implements IBlockColor {
    public static final IBlockColor INSTANCE =new BlockColorHandler();
    private static final Logger LOGGER = LogManager.getLogger();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerBlockColorHandlers(final ColorHandlerEvent.Block event) {
        registerBlockColors();
        event.getBlockColors().register((x, reader, pos, u) -> reader != null
                && pos != null ? BiomeColors.getGrassColor(reader, pos)
                : GrassColors.get(0.5D, 1.0D), Registration.FRAMEBLOCK.get());
    }

    @Override
    public int getColor(@Nonnull BlockState p_getColor_1_, @Nullable ILightReader lightReader, @Nullable BlockPos pos, int p_getColor_4_) {

        return BiomeColors.getGrassColor(lightReader,pos);
    }
    public static void registerBlockColors()
    {
        // DEBUG
        LOGGER.info("Registering block color handler");

        Minecraft.getInstance().getBlockColors().register(INSTANCE, Registration.FRAMEBLOCK.get());

        LOGGER.info("Registered block color handler");
    }
}
