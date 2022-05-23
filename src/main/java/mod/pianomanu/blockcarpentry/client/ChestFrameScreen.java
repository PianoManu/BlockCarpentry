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
 * @version 1.0 05/23/22
 */
@OnlyIn(Dist.CLIENT)
public class ChestFrameScreen extends AbstractContainerScreen<ChestFrameContainer> implements MenuAccess<ChestFrameContainer> {

    /**
     * The ResourceLocation containing the chest GUI texture.
     */
    //Its not a shulker box, but it's the same texture, and I couldn't find the original chest texture
    private static final ResourceLocation CONTAINER_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");

    /**
     * Window height is calculated with these values; the more rows, the higher
     */
    private final int containerRows;
    private final int textureXSize;
    private final int textureYSize;

    public ChestFrameScreen(ChestFrameContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);

        this.containerRows = container.getRowCount();
        this.imageWidth = 184;
        this.imageHeight = 114 + this.containerRows * 18;
        this.textureXSize = 256;
        this.textureYSize = 256;

        this.passEvents = false;
    }

    /**
     * This writes "Inventory" above the player inventory and the name of the container above
     * the container inventory
     */
    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, this.title, 8.0F, 6.0F, 4210752);

        this.font.draw(matrixStack, this.playerInventoryTitle, 8.0F, (float) (this.imageHeight - 96 + 2), 4210752);
    }

    /**
     * Used to draw background and tooltips (items are highlighted, when hovering over them).
     * The order is very important: first draw background (environment of the container), then
     * draw the chest inventory and player inventory, and lastly draw the tooltip.
     */
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CONTAINER_TEXTURE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight, this.textureXSize, this.textureYSize);
    }
}
//========SOLI DEO GLORIA========//