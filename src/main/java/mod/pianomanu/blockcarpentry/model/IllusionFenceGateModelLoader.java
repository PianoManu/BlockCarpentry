package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionFenceGateModelLoader implements IGeometryLoader<IllusionFenceGateModelGeometry> {

    @Override
    public IllusionFenceGateModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionFenceGateModelGeometry();
    }
}
