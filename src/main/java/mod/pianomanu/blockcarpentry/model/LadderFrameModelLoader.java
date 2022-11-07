package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class LadderFrameModelLoader implements IGeometryLoader<LadderFrameModelGeometry> {

    @Override
    public LadderFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new LadderFrameModelGeometry();
    }
}
