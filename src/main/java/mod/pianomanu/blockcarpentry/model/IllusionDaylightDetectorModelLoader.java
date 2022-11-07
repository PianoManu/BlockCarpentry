package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionDaylightDetectorModelLoader implements IGeometryLoader<IllusionDaylightDetectorModelGeometry> {

    @Override
    public IllusionDaylightDetectorModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionDaylightDetectorModelGeometry();
    }
}