package mod.pianomanu.blockcarpentry.util;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;

public class BCBlockStateProperties {
    public static final BooleanProperty CONTAINS_BLOCK = BooleanProperty.create("contains_block");
    //public static final BlockContainerProperty CONTAINS = BlockContainerProperty.create("contains");
    public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("light_level",0,15);
    public static final IntegerProperty TEXTURE = IntegerProperty.create("texture",0,3);
    public static final IntegerProperty DESIGN = IntegerProperty.create("design",0,3);
    //public static final IntegerProperty DESIGN_TEXTURE = IntegerProperty.create("design_texture",0,3);
    public static final IntegerProperty PILLOW_COLOR = IntegerProperty.create("pillow_color",0,15);
}
