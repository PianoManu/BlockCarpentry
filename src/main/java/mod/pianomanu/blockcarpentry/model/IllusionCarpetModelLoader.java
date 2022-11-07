package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionCarpetModelLoader implements IGeometryLoader<IllusionCarpetModelGeometry> {

    @Override
    public IllusionCarpetModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionCarpetModelGeometry();
    }
}