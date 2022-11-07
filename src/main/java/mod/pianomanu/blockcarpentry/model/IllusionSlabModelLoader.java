package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionSlabModelLoader implements IGeometryLoader<IllusionSlabModelGeometry> {

    @Override
    public IllusionSlabModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionSlabModelGeometry();
    }
}
