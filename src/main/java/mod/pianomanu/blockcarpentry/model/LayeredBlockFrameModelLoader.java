package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class LayeredBlockFrameModelLoader implements IGeometryLoader<LayeredBlockFrameModelGeometry> {

    @Override
    public LayeredBlockFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new LayeredBlockFrameModelGeometry();
    }
}
