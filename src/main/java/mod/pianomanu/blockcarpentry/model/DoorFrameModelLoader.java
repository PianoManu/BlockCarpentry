package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class DoorFrameModelLoader implements IGeometryLoader<DoorFrameModelGeometry> {

    @Override
    public DoorFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new DoorFrameModelGeometry();
    }
}
