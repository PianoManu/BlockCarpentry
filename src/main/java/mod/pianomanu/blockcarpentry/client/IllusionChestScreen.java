package mod.pianomanu.blockcarpentry.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mod.pianomanu.blockcarpentry.container.IllusionChestContainer;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Here's where you can find all information about the Illusion Chest Screen (GUI)
 * The constructor takes some parameters like the Container of the Chest (containing all information about itemStacks stored in the chest), the PlayerInventory (self-explanatory) and the Title of the Chest (what to write at the top, when the chest is opened)
 * The constructor sets values like the vertical size, rows of the chest and where to put the title
 *
 * @author PianoManu
 * @version 1.0 09/22/20
 */
@OnlyIn(Dist.CLIENT)
public class IllusionChestScreen extends ContainerScreen<IllusionChestContainer> implements IHasContainer<IllusionChestContainer> {
    /** The ResourceLocation containing the chest GUI texture. */
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    /** Window height is calculated with these values; the more rows, the higher */
    private final int inventoryRows;

    public IllusionChestScreen(IllusionChestContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.field_230711_n_ = false;
        int i = 222;
        int j = 114;
        this.inventoryRows = 3;
        this.ySize = 114 + this.inventoryRows * 18;
        this.field_238745_s_ = this.ySize - 94;
    }

    /**
     * Used to draw background and tooltips (items are highlighted, when hovering over them)
     */
    @Override
    public void func_230430_a_(MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.func_230446_a_(matrixStack);
        super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrixStack, mouseX, mouseY);
    }

    /**
     * Yeah, it draws the foreground layer of the GUI
     */
    @Override
    protected void func_230451_b_(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.func_230451_b_(matrixStack, mouseX, mouseY);
        this.field_230712_o_.func_238421_b_(matrixStack, this.field_230704_d_.getString(), 8.0f, 6.0f, 4210752);
    }

    /**
     * I just took this from the Vanilla Chest Screen {@link net.minecraft.client.gui.screen.inventory.ChestScreen}
     */
    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_230706_i_.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.field_230708_k_ - this.xSize) / 2;
        int j = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(matrixStack, i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.func_238474_b_(matrixStack, i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
//========SOLI DEO GLORIA========//