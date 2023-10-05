package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class IllusionLayeredBlockEmptyModelLoader implements IModelLoader<IllusionLayeredBlockEmptyModelGeometry> {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public IllusionLayeredBlockEmptyModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new IllusionLayeredBlockEmptyModelGeometry();
    }
}
