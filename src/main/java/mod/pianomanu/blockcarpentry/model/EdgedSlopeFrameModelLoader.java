package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class EdgedSlopeFrameModelLoader implements IGeometryLoader<EdgedSlopeFrameModelGeometry> {

    @Override
    public EdgedSlopeFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new EdgedSlopeFrameModelGeometry();
    }
}
