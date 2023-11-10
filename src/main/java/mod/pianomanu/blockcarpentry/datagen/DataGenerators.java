package mod.pianomanu.blockcarpentry.datagen;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Basic class for data generation. Standard Forge stuff
 *
 * @author PianoManu
 * @version 1.0 10/07/23
 */
@Mod.EventBusSubscriber(modid = BlockCarpentryMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        generator.addProvider(true, new BCTagProvider(packOutput, event.getLookupProvider(), fileHelper));
        generator.addProvider(true, new BCItemModelProvider(packOutput, fileHelper));
    }
}
//========SOLI DEO GLORIA========//