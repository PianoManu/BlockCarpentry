package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class StairsFrameModelLoader implements IGeometryLoader<StairsFrameModelGeometry> {

    @Override
    public StairsFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new StairsFrameModelGeometry();
    }
}
