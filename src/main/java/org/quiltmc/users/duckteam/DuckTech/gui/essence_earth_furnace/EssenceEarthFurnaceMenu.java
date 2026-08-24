package org.quiltmc.users.duckteam.DuckTech.gui.essence_earth_furnace;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.quiltmc.users.duckteam.DuckTech.blocks.blockentity.EssenceEarthFurnaceBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.blocks.custom.EssenceEarthFurnace;
import org.quiltmc.users.duckteam.DuckTech.gui.DTMenu;

public class EssenceEarthFurnaceMenu extends AbstractFurnaceMenu {

    public EssenceEarthFurnaceMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(DTMenu.ESSENCE_EARTH_FURNACE.get(),
                RecipeType.SMELTING,
                RecipeBookType.FURNACE,
                containerId,
                playerInventory,
                container,
                data);
    }

    // 自定义燃料槽（继承 Slot，不依赖原版 FurnaceFuelSlot）
    private static class CustomFuelSlot extends Slot {
        private final EssenceEarthFurnaceBlockEntity furnace;

        public CustomFuelSlot(Container container, int slotIndex, int x, int y) {
            super(container, slotIndex, x, y);
            if (!(container instanceof EssenceEarthFurnaceBlockEntity)) {
                throw new IllegalArgumentException("Container must be EssenceEarthFurnaceBlockEntity");
            }
            this.furnace = (EssenceEarthFurnaceBlockEntity) container;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return furnace.isFuelItem(stack);
        }

        @Override
        public boolean isActive() {
            return true; // 保持激活
        }
    }

    @Override
    public Slot addSlot(Slot slot) {
        if (slot.container instanceof EssenceEarthFurnaceBlockEntity furnace) {
            int index = slot.getSlotIndex();
            if (index == 0) { // 输入槽
                return super.addSlot(new CustomInputSlot(slot.container, index, slot.x, slot.y));
            } else if (index == 1) { // 燃料槽
                return super.addSlot(new CustomFuelSlot(slot.container, index, slot.x, slot.y));
            }
            // 输出槽（索引2）保持原样，不做替换
        }
        return super.addSlot(slot);
    }

    public EssenceEarthFurnaceMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        // 调用你的服务端构造函数，但这里无法直接获取到 Container 和 ContainerData
        // 因此，我们需要调用一个更基础的构造函数，并传入一个“空”的实现。
        // 推荐的方式是：在客户端，创建一个仅用于显示的空容器和数据。
        this(containerId, playerInventory, new SimpleContainer(3), new SimpleContainerData(4));
    }

    private class CustomInputSlot extends Slot {
        private final EssenceEarthFurnaceBlockEntity furnace;

        public CustomInputSlot(Container container, int slotIndex, int x, int y) {
            super(container, slotIndex, x, y);
            if (!(container instanceof EssenceEarthFurnaceBlockEntity)) {
                throw new IllegalArgumentException("Container must be EssenceEarthFurnaceBlockEntity");
            }
            this.furnace = (EssenceEarthFurnaceBlockEntity) container;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            // 调用 BlockEntity 的 canPlaceItem
            return furnace.canPlaceItem(getSlotIndex(), stack);
        }
    }
}
