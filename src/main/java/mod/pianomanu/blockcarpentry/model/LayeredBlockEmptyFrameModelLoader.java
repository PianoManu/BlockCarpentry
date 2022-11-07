package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class LayeredBlockEmptyFrameModelLoader implements IGeometryLoader<LayeredBlockEmptyFrameModelGeometry> {

    @Override
    public LayeredBlockEmptyFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new LayeredBlockEmptyFrameModelGeometry();
    }
}
