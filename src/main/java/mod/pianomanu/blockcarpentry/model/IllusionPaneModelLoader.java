package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class IllusionPaneModelLoader implements IGeometryLoader<IllusionPaneModelGeometry> {

    @Override
    public IllusionPaneModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new IllusionPaneModelGeometry();
    }

}
