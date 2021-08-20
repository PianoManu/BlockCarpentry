package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class DaylightDetectorFrameModelLoader implements IModelLoader<DaylightDetectorFrameModelGeometry> {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public DaylightDetectorFrameModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new DaylightDetectorFrameModelGeometry();
    }
}