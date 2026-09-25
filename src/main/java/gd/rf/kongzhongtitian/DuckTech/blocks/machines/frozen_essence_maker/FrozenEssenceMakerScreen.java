package gd.rf.kongzhongtitian.DuckTech.blocks.machines.frozen_essence_maker;

import com.mojang.blaze3d.systems.RenderSystem;
import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FrozenEssenceMakerScreen extends AbstractContainerScreen<FrozenEssenceMakerMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "textures/screen/fem.png");

    public static final ResourceLocation ARROW_TEXTURE = ResourceLocation.fromNamespaceAndPath(DuckTech.MODID,
            "textures/screen/arrow_progress.png");

    public FrozenEssenceMakerScreen(FrozenEssenceMakerMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        int progressWidth = menu.getScaleArrowProgress();
        if (progressWidth > 0) {
            graphics.blit(ARROW_TEXTURE, leftPos + 73, topPos + 35, 0, 0, progressWidth, 16, 24, 16);
        }
    }


    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);
        renderTooltip(graphics, mouseX, mouseY);
    }
}