package gd.rf.kongzhongtitian.DuckTech.gui.essence_blast_furnace;

import gd.rf.kongzhongtitian.DuckTech.items.DTItems;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;

import java.util.Set;

public class EssenceBlastFurnaceScreen extends AbstractFurnaceScreen<EssenceBlastFurnaceMenu> {

    public EssenceBlastFurnaceScreen(EssenceBlastFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, new FurnaceRecipeBook(), playerInventory, title,
                new ResourceLocation("textures/gui/container/furnace.png"));
    }

    static class FurnaceRecipeBook extends AbstractFurnaceRecipeBookComponent {

        @Override
        public boolean isVisible() {
            return false;   // 隐藏配方书按钮
        }

        @Override
        protected Set<Item> getFuelItems() {
            // 本熔炉唯一可用的燃料
            return Set.of(DTItems.THERMAL_ESSENCE.get());
        }
    }
}