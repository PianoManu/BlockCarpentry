package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class IllusionBlockModelLoader implements IModelLoader<IllusionBlockModelGeometry> {
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {

    }

    @Override
    public IllusionBlockModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new IllusionBlockModelGeometry();
    }
}
