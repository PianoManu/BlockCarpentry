package mod.pianomanu.blockcarpentry.datagen;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.util.Tags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

/**
 * Just some tag stuff regarding block tags
 *
 * @author PianoManu
 * @version 1.0 10/07/23
 */
public class BCTagProvider extends BlockTagsProvider {
    private static final Logger LOGGER = LogManager.getLogger();

    public BCTagProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, BlockCarpentryMain.MOD_ID, existingFileHelper);
    }


    @Override
    protected void addTags() {
        Tags.init();
        for (Block b :
                Tags.getFrameBlocks()) {
            this.tag(BlockTags.MINEABLE_WITH_AXE).add(b);
        }
        for (Block b :
                Tags.getIllusionBlocks()) {
            this.tag(BlockTags.MINEABLE_WITH_AXE).add(b);
        }
        LOGGER.info("Added " + Tags.getFrameBlocks().size() + " Frame Blocks and " + Tags.getIllusionBlocks().size() + " Illusion Blocks to the MINEABLE_WITH_AXE tag");
    }
}
//========SOLI DEO GLORIA========//