package gd.rf.kongzhongtitian.DuckTech.blocks.machines.thermal_essence_maker;

import com.mojang.blaze3d.systems.RenderSystem;
import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ThermalEssenceMakerScreen extends AbstractContainerScreen<ThermalEssenceMakerMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "textures/screen/tem.png");

    public ThermalEssenceMakerScreen(ThermalEssenceMakerMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // 绘制进度箭头
        int progress = menu.getProgress();
        int maxProgress = menu.getMaxProgress();
        if (maxProgress > 0) {
            int arrowHeight = (int) (24 * ((float) progress / maxProgress));
            graphics.blit(TEXTURE, leftPos + 79, topPos + 34, 176, 0, 18, arrowHeight);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);
        renderTooltip(graphics, mouseX, mouseY);
    }
}