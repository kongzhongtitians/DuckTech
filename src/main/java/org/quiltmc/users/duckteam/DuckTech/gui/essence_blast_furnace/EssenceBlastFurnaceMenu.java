package org.quiltmc.users.duckteam.DuckTech.gui.essence_blast_furnace;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.quiltmc.users.duckteam.DuckTech.blocks.blockentity.EssenceBlastFurnaceBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.gui.DTMenu;

public class EssenceBlastFurnaceMenu extends AbstractFurnaceMenu {

    public EssenceBlastFurnaceMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(DTMenu.ESSENCE_BLAST_FURNACE_MENU.get(),
                RecipeType.SMELTING,
                RecipeBookType.FURNACE,
                containerId,
                playerInventory,
                container,
                data);
    }

    // 客户端用的构造函数
    public EssenceBlastFurnaceMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, new SimpleContainer(3), new SimpleContainerData(4));
    }

    @Override
    public Slot addSlot(Slot slot) {
        if (slot.container instanceof EssenceBlastFurnaceBlockEntity furnace) {
            int index = slot.getSlotIndex();
            if (index == 0) {
                return super.addSlot(new CustomInputSlot(slot.container, index, slot.x, slot.y));
            } else if (index == 1) {
                return super.addSlot(new CustomFuelSlot(slot.container, index, slot.x, slot.y));
            }
        }
        return super.addSlot(slot);
    }

    // 自定义输入槽
    private class CustomInputSlot extends Slot {
        private final EssenceBlastFurnaceBlockEntity furnace;

        public CustomInputSlot(Container container, int slotIndex, int x, int y) {
            super(container, slotIndex, x, y);
            if (!(container instanceof EssenceBlastFurnaceBlockEntity)) {
                throw new IllegalArgumentException("Container must be EssenceBlastFurnaceBlockEntity");
            }
            this.furnace = (EssenceBlastFurnaceBlockEntity) container;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return furnace.canPlaceItem(getSlotIndex(), stack);
        }
    }

    // 自定义燃料槽
    private class CustomFuelSlot extends Slot {
        private final EssenceBlastFurnaceBlockEntity furnace;

        public CustomFuelSlot(Container container, int slotIndex, int x, int y) {
            super(container, slotIndex, x, y);
            if (!(container instanceof EssenceBlastFurnaceBlockEntity)) {
                throw new IllegalArgumentException("Container must be EssenceBlastFurnaceBlockEntity");
            }
            this.furnace = (EssenceBlastFurnaceBlockEntity) container;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return furnace.isFuelItem(stack);
        }
    }
}