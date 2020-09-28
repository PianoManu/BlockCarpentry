package mod.pianomanu.blockcarpentry.client;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.pianomanu.blockcarpentry.container.ChestFrameContainer;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Here's where you can find all information about the Frame Chest Screen (GUI)
 * The constructor takes some parameters like the Container of the Chest (containing all information about itemStacks stored in the chest), the PlayerInventory (self-explanatory) and the Title of the Chest (what to write at the top, when the chest is opened)
 * The constructor sets values like the vertical size, rows of the chest and where to put the title
 *
 * @author PianoManu
 * @version 1.0 09/22/20
 */
@OnlyIn(Dist.CLIENT)
public class ChestFrameScreen extends ContainerScreen<ChestFrameContainer> implements IHasContainer<ChestFrameContainer> {
    /**
     * The ResourceLocation containing the chest GUI texture.
     */
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    /**
     * Window height is calculated with these values; the more rows, the higher
     */
    private final int inventoryRows;

    public ChestFrameScreen(ChestFrameContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.passEvents = false;
        int i = 222;
        int j = 114;
        this.inventoryRows = 3;
        this.ySize = 114 + this.inventoryRows * 18;
        //this.playerInventoryTitleY = this.ySize - 94;
    }

    /**
     * Used to draw background and tooltips (items are highlighted, when hovering over them)
     */
    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    /**
     * Yeah, it draws the foreground layer of the GUI
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer( mouseX, mouseY);
        this.font.drawString(this.title.getString(), 8.0f, 6.0f, 4210752);
    }

    /**
     * I just took this from the Vanilla Chest Screen {@link net.minecraft.client.gui.screen.inventory.ChestScreen}
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.blit(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
//========SOLI DEO GLORIA========//