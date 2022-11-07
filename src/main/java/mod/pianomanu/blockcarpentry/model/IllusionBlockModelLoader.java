package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionBlockModelLoader implements IGeometryLoader<IllusionBlockModelGeometry> {

    @Override
    public IllusionBlockModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionBlockModelGeometry();
    }
}
