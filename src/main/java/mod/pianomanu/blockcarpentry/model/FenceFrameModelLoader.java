package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class FenceFrameModelLoader implements IGeometryLoader<FenceFrameModelGeometry> {

    @Override
    public FenceFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new FenceFrameModelGeometry();
    }
}
