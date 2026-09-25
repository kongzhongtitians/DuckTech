package gd.rf.kongzhongtitian.DuckTech.blocks.machines.fe2thermal_essence_machine;

import com.mojang.blaze3d.systems.RenderSystem;
import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FE2ThermalEssenceMachineScreen extends AbstractContainerScreen<FE2ThermalEssenceMachineMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "textures/screen/fe2tem.png");

    public FE2ThermalEssenceMachineScreen(FE2ThermalEssenceMachineMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // 渲染能量条
        int energy = menu.getEnergy();
        int maxEnergy = menu.getMaxEnergy();
        if (maxEnergy > 0) {
            int energyHeight = (int) (energy * 52.0 / maxEnergy); // 能量条高度52像素
            guiGraphics.blit(TEXTURE, leftPos + 17, topPos + 18 + (52 - energyHeight),
                    176, 52 - energyHeight, 14, energyHeight);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // 不绘制标题，保持背景图自带标题
    }
}