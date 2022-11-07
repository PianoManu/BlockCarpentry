package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionButtonPressedModelLoader implements IGeometryLoader<IllusionButtonPressedModelGeometry> {

    @Override
    public IllusionButtonPressedModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionButtonPressedModelGeometry();
    }
}
