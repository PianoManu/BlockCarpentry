package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class BedFrameModelLoader implements IGeometryLoader<BedFrameModelGeometry> {

    @Override
    public BedFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new BedFrameModelGeometry();
    }
}
