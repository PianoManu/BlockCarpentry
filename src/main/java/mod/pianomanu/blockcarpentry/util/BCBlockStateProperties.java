package mod.pianomanu.blockcarpentry.util;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;

/**
 * Some properties needed for frame blocks
 *
 * @author PianoManu
 * @version 1.0 05/23/22
 */
public class BCBlockStateProperties {
    /**
     * Whether a frame block holds a block
     */
    public static final BooleanProperty CONTAINS_BLOCK = BooleanProperty.create("contains_block");

    /**
     * Whether a frame block holds a second block, only needed for slabs and slopes
     */
    public static final BooleanProperty CONTAINS_2ND_BLOCK = BooleanProperty.create("contains_2nd_block");

    /**
     * Whether a block emits light
     * 0  : no light
     * ...
     * 15 : full light
     */
    public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("light_level", 0, 15);

    /**
     * Whether a frame slab or slope block contains of two slabs or slopes
     */
    public static final BooleanProperty DOUBLE = BooleanProperty.create("double");

    /**
     * Amount of layers a {@link mod.pianomanu.blockcarpentry.block.LayeredBlock} can have at most
     */
    public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 8);
}
//========SOLI DEO GLORIA========//