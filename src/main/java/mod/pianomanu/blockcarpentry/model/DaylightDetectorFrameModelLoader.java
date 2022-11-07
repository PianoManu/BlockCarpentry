package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class DaylightDetectorFrameModelLoader implements IGeometryLoader<DaylightDetectorFrameModelGeometry> {

    @Override
    public DaylightDetectorFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new DaylightDetectorFrameModelGeometry();
    }
}