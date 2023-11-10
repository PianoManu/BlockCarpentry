package mod.pianomanu.blockcarpentry.datagen;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

/**
 * Just some tag stuff regarding items and item models
 *
 * @author PianoManu
 * @version 1.0 10/07/23
 */
public class BCItemModelProvider extends ItemModelProvider {
    public BCItemModelProvider(PackOutput packOutput, ExistingFileHelper exFileHelper) {
        super(packOutput, BlockCarpentryMain.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerModels() {

    }

    private <T extends Item> void register(RegistryObject<T> itemRegistryObject) {
        withExistingParent(itemRegistryObject.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BlockCarpentryMain.MOD_ID, "item/" + itemRegistryObject.getId().getPath()));
    }
}
//========SOLI DEO GLORIA========//