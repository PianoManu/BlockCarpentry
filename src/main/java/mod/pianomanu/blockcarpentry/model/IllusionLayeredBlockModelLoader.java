package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionLayeredBlockModelLoader implements IGeometryLoader<IllusionLayeredBlockModelGeometry> {

    @Override
    public IllusionLayeredBlockModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionLayeredBlockModelGeometry();
    }
}
