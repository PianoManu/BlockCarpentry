package mod.pianomanu.blockcarpentry.util;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * Some properties needed for frame blocks
 *
 * @author PianoManu
 * @version 1.2 06/05/21
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
}
//========SOLI DEO GLORIA========//