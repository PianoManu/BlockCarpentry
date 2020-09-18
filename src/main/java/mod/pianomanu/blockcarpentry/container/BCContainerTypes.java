package mod.pianomanu.blockcarpentry.container;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

public class BCContainerTypes <T extends Container> extends ContainerType<T> {
    public BCContainerTypes(IFactory<T> factory) {
        super(factory);
    }
}
//========SOLI DEO GLORIA========//