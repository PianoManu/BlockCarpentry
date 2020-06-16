package mod.pianomanu.blockcarpentry.datagen.provider;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.data.DataGenerator;

public class BCLootTables extends  BCBaseLootTableProvider{
    public BCLootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        System.out.println("HELLLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        lootTables.put(Registration.FRAMEBLOCK.get(), createStandardTable("frameblock",Registration.FRAMEBLOCK.get()));
        lootTables.put(Registration.STAIRS_FRAMEBLOCK.get(), createStandardTable("frame_stairs",Registration.STAIRS_FRAMEBLOCK.get()));
        lootTables.put(Registration.SLAB_FRAMEBLOCK.get(), createStandardTable("frame_slab",Registration.SLAB_FRAMEBLOCK.get()));
        lootTables.put(Registration.BUTTON_FRAMEBLOCK.get(), createStandardTable("frame_button",Registration.BUTTON_FRAMEBLOCK.get()));
        lootTables.put(Registration.PRESSURE_PLATE_FRAMEBLOCK.get(), createStandardTable("frame_pressure_plate",Registration.PRESSURE_PLATE_FRAMEBLOCK.get()));
    }
}
