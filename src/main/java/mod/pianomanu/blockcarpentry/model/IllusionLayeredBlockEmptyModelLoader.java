package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionLayeredBlockEmptyModelLoader implements IGeometryLoader<IllusionLayeredBlockEmptyModelGeometry> {

    @Override
    public IllusionLayeredBlockEmptyModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionLayeredBlockEmptyModelGeometry();
    }
}
