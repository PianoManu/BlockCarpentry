package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class StairsFrameModelLoader implements IModelLoader<StairsFrameModelGeometry> {
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {

    }

    @Override
    public StairsFrameModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new StairsFrameModelGeometry();
    }
}
