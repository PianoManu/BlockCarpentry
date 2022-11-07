package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class ChestFrameModelLoader implements IGeometryLoader<ChestFrameModelGeometry> {

    @Override
    public ChestFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new ChestFrameModelGeometry();
    }
}
