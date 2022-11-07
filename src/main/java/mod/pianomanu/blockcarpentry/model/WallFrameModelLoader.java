package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class WallFrameModelLoader implements IGeometryLoader<WallFrameModelGeometry> {

    @Override
    public WallFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new WallFrameModelGeometry();
    }
}
