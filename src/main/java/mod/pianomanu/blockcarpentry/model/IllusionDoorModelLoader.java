package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class IllusionDoorModelLoader implements IModelLoader<IllusionDoorModelGeometry> {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public IllusionDoorModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new IllusionDoorModelGeometry();
    }
}
