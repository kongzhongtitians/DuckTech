package gd.rf.kongzhongtitian.DuckTech.blocks.machines.weather_machine;

import com.mojang.blaze3d.systems.RenderSystem;
import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EssenceWeatherMachineScreen extends AbstractContainerScreen<EssenceWeatherMachineMenu> {

    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "textures/screen/levitation.png");

    /** 按钮布局常量（相对于 GUI 左上角） */
    private static final int BTN_X = 130;
    private static final int BTN_Y = 14;
    private static final int BTN_W = 40;
    private static final int BTN_H = 20;
    private static final int BTN_GAP = 26;

    public EssenceWeatherMachineScreen(EssenceWeatherMachineMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        int x = this.leftPos + BTN_X;
        int y = this.topPos + BTN_Y;

        this.addRenderableWidget(Button.builder(
                        Component.translatable("gui.ducktech.weather.clear"),
                        b -> sendWeather(EssenceWeatherMachineBlockEntity.WEATHER_CLEAR))
                .bounds(x, y, BTN_W, BTN_H)
                .build());

        this.addRenderableWidget(Button.builder(
                        Component.translatable("gui.ducktech.weather.rain"),
                        b -> sendWeather(EssenceWeatherMachineBlockEntity.WEATHER_RAIN))
                .bounds(x, y + BTN_GAP, BTN_W, BTN_H)
                .build());

        this.addRenderableWidget(Button.builder(
                        Component.translatable("gui.ducktech.weather.thunder"),
                        b -> sendWeather(EssenceWeatherMachineBlockEntity.WEATHER_THUNDER))
                .bounds(x, y + BTN_GAP * 2, BTN_W, BTN_H)
                .build());
    }

    private void sendWeather(int mode) {
        EssenceWeatherMachineBlockEntity be = this.menu.getBlockEntity();
        if (be != null) {
            EssenceWeatherMachineButtonPacketRegister.CHANNEL.sendToServer(
                    new EssenceWeatherMachineButtonPacket(be.getBlockPos(), mode));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2 - 1;

        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, 256, 256);
    }
}