package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionDoorModelLoader implements IGeometryLoader<IllusionDoorModelGeometry> {

    @Override
    public IllusionDoorModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionDoorModelGeometry();
    }
}
