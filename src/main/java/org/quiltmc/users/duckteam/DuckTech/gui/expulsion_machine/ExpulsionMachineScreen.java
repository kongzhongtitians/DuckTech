package org.quiltmc.users.duckteam.DuckTech.gui.expulsion_machine;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.quiltmc.users.duckteam.DuckTech.DuckTech;

public class ExpulsionMachineScreen extends AbstractContainerScreen<ExpulsionMachineMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(DuckTech.MODID, "textures/screen/levitation.png");

    public ExpulsionMachineScreen(ExpulsionMachineMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // Timer bar
        int timer = menu.getTimer();
        int maxTimer = menu.getMaxTimer();
        if (timer > 0) {
            int barHeight = (int) (24.0 * timer / maxTimer);
            graphics.blit(TEXTURE, leftPos + 79, topPos + 55 - barHeight, 176, 24 - barHeight, 18, barHeight);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);
    }
}
