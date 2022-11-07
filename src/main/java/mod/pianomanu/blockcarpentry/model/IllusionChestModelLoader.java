package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionChestModelLoader implements IGeometryLoader<IllusionChestModelGeometry> {

    @Override
    public IllusionChestModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionChestModelGeometry();
    }
}
