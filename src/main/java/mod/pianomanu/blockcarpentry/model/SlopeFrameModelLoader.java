package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class SlopeFrameModelLoader implements IGeometryLoader<SlopeFrameModelGeometry> {

    @Override
    public SlopeFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new SlopeFrameModelGeometry();
    }
}
