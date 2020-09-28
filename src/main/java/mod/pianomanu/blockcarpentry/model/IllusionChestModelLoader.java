package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class IllusionChestModelLoader implements IModelLoader<IllusionChestModelGeometry> {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public IllusionChestModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new IllusionChestModelGeometry();
    }
}
