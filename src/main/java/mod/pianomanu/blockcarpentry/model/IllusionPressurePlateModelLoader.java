package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionPressurePlateModelLoader implements IGeometryLoader<IllusionPressurePlateModelGeometry> {

    @Override
    public IllusionPressurePlateModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionPressurePlateModelGeometry();
    }
}
