package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class PressurePlatePressedFrameModelLoader implements IGeometryLoader<PressurePlatePressedFrameModelGeometry> {

    @Override
    public PressurePlatePressedFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new PressurePlatePressedFrameModelGeometry();
    }
}
