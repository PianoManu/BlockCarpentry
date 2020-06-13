package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class SlabFrameTopModelLoader implements IModelLoader<SlabFrameTopModelGeometry> {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public SlabFrameTopModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new SlabFrameTopModelGeometry();
    }
}
