package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class IllusionWallModelLoader implements IModelLoader<IllusionWallModelGeometry> {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public IllusionWallModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new IllusionWallModelGeometry();
    }
}
