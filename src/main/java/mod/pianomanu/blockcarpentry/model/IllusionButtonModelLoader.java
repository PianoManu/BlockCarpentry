package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionButtonModelLoader implements IGeometryLoader<IllusionButtonModelGeometry> {

    @Override
    public IllusionButtonModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionButtonModelGeometry();
    }
}
