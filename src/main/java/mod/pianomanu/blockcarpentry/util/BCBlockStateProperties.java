package mod.pianomanu.blockcarpentry.util;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;

/**
 * Some properties needed for frame blocks
 * @author PianoManu
 * @version 1.0
 */
public class BCBlockStateProperties {
    /**
     * Whether a frame block holds a block
     */
    public static final BooleanProperty CONTAINS_BLOCK = BooleanProperty.create("contains_block");

    /**
     * Whether a block emits light
     *      0  : no light
     *      ...
     *      15 : full light
     */
    public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("light_level",0,15);

    /**
     * For beds, currently unused (May be removed in the future and saved in the block-tile-entity)
     */
    public static final IntegerProperty PILLOW_COLOR = IntegerProperty.create("pillow_color",0,15);
}
