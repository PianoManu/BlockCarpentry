package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class PaneFrameModelLoader implements IGeometryLoader<PaneFrameModelGeometry> {

    @Override
    public PaneFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new PaneFrameModelGeometry();
    }
}
