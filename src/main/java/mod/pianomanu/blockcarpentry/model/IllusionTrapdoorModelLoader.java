package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionTrapdoorModelLoader implements IGeometryLoader<IllusionTrapdoorModelGeometry> {

    @Override
    public IllusionTrapdoorModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionTrapdoorModelGeometry();
    }
}
