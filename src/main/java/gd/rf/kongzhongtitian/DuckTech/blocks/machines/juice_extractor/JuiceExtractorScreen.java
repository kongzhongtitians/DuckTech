package gd.rf.kongzhongtitian.DuckTech.blocks.machines.juice_extractor;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class JuiceExtractorScreen extends AbstractContainerScreen<JuiceExtractorMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("ducktech", "textures/screen/juice_extractor.png");

    public JuiceExtractorScreen(JuiceExtractorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth  = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);

        int rubber    = this.menu.getRubberAmount();
        int remaining = this.menu.getRemainingFromWood();
        int progress  = this.menu.getRecipeProgress();
        int maxProgress = this.menu.getRecipeMaxProgress();
        int mode      = this.menu.getMode();
        MutableComponent ru=Component.translatable("gui.ducktech.rubber").append(rubber+" mB");

        guiGraphics.drawString(this.font, ru,    8, 16, 0x404040, false);
        guiGraphics.drawString(this.font,Component.translatable("gui.ducktech.remaining").append(remaining+" mB"), 8, 26, 0x404040, false);
        if (maxProgress > 0) {
            guiGraphics.drawString(this.font,Component.translatable("gui.ducktech.progress").append(progress+" / "+maxProgress+" tick"), 8, 36, 0x404040, false);
        }
        guiGraphics.drawString(this.font, Component.translatable("gui.ducktech.mode").append(mode == JuiceExtractorBlockEntity.MODE_LEGACY ? "Rubber" : "Recipe"), 8, 46, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}