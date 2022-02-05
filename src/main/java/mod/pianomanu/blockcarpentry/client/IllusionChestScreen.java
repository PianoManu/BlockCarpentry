package mod.pianomanu.blockcarpentry.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Here's where you can find all information about the Illusion Chest Screen (GUI)
 * The constructor takes some parameters like the Container of the Chest (containing all information about itemStacks stored in the chest), the PlayerInventory (self-explanatory) and the Title of the Chest (what to write at the top, when the chest is opened)
 * The constructor sets values like the vertical size, rows of the chest and where to put the title
 *
 * @author PianoManu
 * @version 1.0 08/15/21
 */
@OnlyIn(Dist.CLIENT)
public class IllusionChestScreen {//extends ContainerScreen<IllusionChestContainer> implements IHasContainer<IllusionChestContainer> {
    /** The ResourceLocation containing the chest GUI texture. */
    //private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    /** Window height is calculated with these values; the more rows, the higher */
    //private final int inventoryRows;

    /*public IllusionChestScreen(IllusionChestContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.passEvents = false;
        int i = 222;
        int j = 114;
        this.inventoryRows = 3;
        this.ySize = 114 + this.inventoryRows * 18;
        this.playerInventoryTitleY = this.ySize - 94;
    }

    @Override
    public void render(MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
        this.font.drawString(matrixStack, this.title.getString(), 8.0f, 6.0f, 4210752);
        //this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(), 8.0f, 90.0f, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.blit(matrixStack, i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }*/
}
//========SOLI DEO GLORIA========//