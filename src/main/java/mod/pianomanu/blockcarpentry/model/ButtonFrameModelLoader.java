package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class ButtonFrameModelLoader implements IGeometryLoader<ButtonFrameModelGeometry> {

    @Override
    public ButtonFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new ButtonFrameModelGeometry();
    }
}
