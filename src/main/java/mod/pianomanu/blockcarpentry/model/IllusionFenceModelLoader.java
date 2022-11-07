package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionFenceModelLoader implements IGeometryLoader<IllusionFenceModelGeometry> {

    @Override
    public IllusionFenceModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionFenceModelGeometry();
    }
}
