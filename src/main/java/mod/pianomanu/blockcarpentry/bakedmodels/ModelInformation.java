package mod.pianomanu.blockcarpentry.bakedmodels;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Small class for saving model information easily and handy
 *
 * @author PianoManu
 * @version 1.0 09/20/23
 */
public class ModelInformation {
    private static final Logger LOGGER = LogManager.getLogger();

    public final TextureAtlasSprite northTexture;
    public final TextureAtlasSprite eastTexture;
    public final TextureAtlasSprite southTexture;
    public final TextureAtlasSprite westTexture;
    public final TextureAtlasSprite upTexture;
    public final TextureAtlasSprite downTexture;
    public final int tintIndex;
    public final int overlayIndex;

    public ModelInformation(List<TextureAtlasSprite> textures, int tintIndex, int overlayIndex) {
        if (textures.size() != 6)
            LOGGER.error("Texture list should have 6 textures but does have " + textures.size() + " textures!");
        this.northTexture = textures.get(0);
        this.eastTexture = textures.get(1);
        this.southTexture = textures.get(2);
        this.westTexture = textures.get(3);
        this.upTexture = textures.get(4);
        this.downTexture = textures.get(5);
        this.tintIndex = tintIndex;
        this.overlayIndex = overlayIndex;
    }

}
//========SOLI DEO GLORIA========//