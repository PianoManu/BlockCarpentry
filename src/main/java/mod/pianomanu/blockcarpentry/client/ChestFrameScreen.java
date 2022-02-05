package mod.pianomanu.blockcarpentry.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mod.pianomanu.blockcarpentry.container.ChestFrameContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Here's where you can find all information about the Frame Chest Screen (GUI)
 * The constructor takes some parameters like the Container of the Chest (containing all information about itemStacks stored in the chest), the PlayerInventory (self-explanatory) and the Title of the Chest (what to write at the top, when the chest is opened)
 * The constructor sets values like the vertical size, rows of the chest and where to put the title
 *
 * @author PianoManu
 * @version 1.0 08/15/21
 */
@OnlyIn(Dist.CLIENT)
public class ChestFrameScreen extends AbstractContainerScreen<ChestFrameContainer> implements MenuAccess<ChestFrameContainer> {

    /**
     * The ResourceLocation containing the chest GUI texture.
     */
    //private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    /**
     * Window height is calculated with these values; the more rows, the higher
     */
    //private final int inventoryRows;

    /*public ChestFrameScreen(ChestFrameContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.passEvents = false;
        int i = 222;
        int j = 114;
        this.inventoryRows = 3;
        this.ySize = 114 + this.inventoryRows * 18;
        this.playerInventoryTitleY = this.ySize - 94;
    }*/

    /**
     * Used to draw background and tooltips (items are highlighted, when hovering over them)
     */
    /*@Override
    public void render(MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrixStack, mouseX, mouseY);
    }*/

    /**
     * Yeah, it draws the foreground layer of the GUI
     */
    /*@Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
        this.font.drawString(matrixStack, this.title.getString(), 8.0f, 6.0f, 4210752);
    }*/

    /* *
     * I just took this from the Vanilla Chest Screen {@link net.minecraft.client.gui.screen.inventory.ChestScreen}
     */
    /*@Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.blit(matrixStack, i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }*/

    //Its not a shulker box, but it's the same texture, and I couldn't find the original chest texture
    private static final ResourceLocation CONTAINER_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");

    private final int containerRows;
    //private final int textureXSize;
    //private final int textureYSize;

    public ChestFrameScreen(ChestFrameContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);

        this.containerRows = container.getRowCount();
        this.imageWidth = 184;
        this.imageHeight = 114 + this.containerRows * 18;
        //this.textureXSize = 256;
        //this.textureYSize = 256;

        this.passEvents = false;
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, this.title, 8.0F, 6.0F, 4210752);

        this.font.draw(matrixStack, this.playerInventoryTitle, 8.0F, (float) (this.imageHeight - 96 + 2), 4210752);
    }

    public void render(PoseStack p_98418_, int p_98419_, int p_98420_, float p_98421_) {
        this.renderBackground(p_98418_);
        super.render(p_98418_, p_98419_, p_98420_, p_98421_);
        this.renderTooltip(p_98418_, p_98419_, p_98420_);
    }

    protected void renderBg(PoseStack p_98413_, float p_98414_, int p_98415_, int p_98416_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CONTAINER_TEXTURE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(p_98413_, i, j, 0, 0, this.imageWidth, this.containerRows * 18 + 17);
        this.blit(p_98413_, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }
}
//========SOLI DEO GLORIA========//