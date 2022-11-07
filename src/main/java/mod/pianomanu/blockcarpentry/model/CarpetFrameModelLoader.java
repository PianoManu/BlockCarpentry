package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class CarpetFrameModelLoader implements IGeometryLoader<CarpetFrameModelGeometry> {

    @Override
    public CarpetFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new CarpetFrameModelGeometry();
    }
}
