package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class FenceGateFrameModelLoader implements IGeometryLoader<FenceGateFrameModelGeometry> {

    @Override
    public FenceGateFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new FenceGateFrameModelGeometry();
    }
}
