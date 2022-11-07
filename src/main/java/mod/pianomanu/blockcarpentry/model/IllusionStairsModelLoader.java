package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionStairsModelLoader implements IGeometryLoader<IllusionStairsModelGeometry> {

    @Override
    public IllusionStairsModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionStairsModelGeometry();
    }
}
