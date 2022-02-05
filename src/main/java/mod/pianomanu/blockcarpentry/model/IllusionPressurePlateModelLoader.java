package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class IllusionPressurePlateModelLoader implements IModelLoader<IllusionPressurePlateModelGeometry> {
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {

    }

    @Override
    public IllusionPressurePlateModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new IllusionPressurePlateModelGeometry();
    }
}
