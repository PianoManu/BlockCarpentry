package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionLadderModelLoader implements IGeometryLoader<IllusionLadderModelGeometry> {

    @Override
    public IllusionLadderModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionLadderModelGeometry();
    }
}
