package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionWallModelLoader implements IGeometryLoader<IllusionWallModelGeometry> {

    @Override
    public IllusionWallModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionWallModelGeometry();
    }
}
