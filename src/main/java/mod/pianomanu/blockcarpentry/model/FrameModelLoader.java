package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class FrameModelLoader implements IGeometryLoader<FrameModelGeometry> {

    @Override
    public FrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new FrameModelGeometry();
    }
}
