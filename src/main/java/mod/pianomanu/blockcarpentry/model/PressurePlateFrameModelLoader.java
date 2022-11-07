package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class PressurePlateFrameModelLoader implements IGeometryLoader<PressurePlateFrameModelGeometry> {

    @Override
    public PressurePlateFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new PressurePlateFrameModelGeometry();
    }
}
