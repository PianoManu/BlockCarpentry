package mod.pianomanu.blockcarpentry.util;

import net.minecraft.block.Block;
import net.minecraft.state.Property;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.*;

public class BlockContainerProperty extends Property<String> {
    private static final IForgeRegistry<Block> BLOCKS = RegistryManager.ACTIVE.getRegistry(Block.class);
    private static List<String> BLOCKS_TO_STRING;
    protected BlockContainerProperty(String name) {
        super(name, String.class);
        BLOCKS_TO_STRING = new ArrayList<>();
        BLOCKS_TO_STRING.add("empty");
        for (Block b: BLOCKS) {
            BLOCKS_TO_STRING.add(Objects.requireNonNull(b.getRegistryName()).getPath().toLowerCase());
        }
    }

    @Override
    public Collection<String> getAllowedValues() {
        return BLOCKS_TO_STRING;
    }

    @Override
    public Optional<String> parseValue(String value) {
        if(this.getAllowedValues().contains(value)) {
            return Optional.of(value);
        }
        return Optional.empty();
    }

    @Override
    public String getName(String value) {
        return value;
    }

    public static BlockContainerProperty create(String name) {
        return new BlockContainerProperty(name);
    }
}
