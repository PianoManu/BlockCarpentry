package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionBedModelLoader implements IGeometryLoader<IllusionBedModelGeometry> {

    @Override
    public IllusionBedModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionBedModelGeometry();
    }
}
