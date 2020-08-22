package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class SlopeFrameModelLoader implements IModelLoader<SlopeFrameModelGeometry> {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public SlopeFrameModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new SlopeFrameModelGeometry();
    }
}
