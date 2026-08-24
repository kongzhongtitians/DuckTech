package org.quiltmc.users.duckteam.DuckTech.gui.air_purifier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlocks;
import org.quiltmc.users.duckteam.DuckTech.blocks.blockentity.AirPurifierBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.gui.DTMenu;

public class AirPurifierMenu extends AbstractContainerMenu {

    private final AirPurifierBlockEntity blockEntity;
    private final ContainerLevelAccess access;

    public AirPurifierMenu(int containerId, Inventory playerInventory, BlockPos pos) {
        super(DTMenu.AIR_PURIFIER_MENU.get(), containerId);

        this.access = ContainerLevelAccess.create(playerInventory.player.level(), pos);
        this.blockEntity = (AirPurifierBlockEntity) playerInventory.player.level().getBlockEntity(pos);

        if (blockEntity != null) {
            blockEntity.getInventoryOptional().ifPresent(handler -> {
                this.addSlot(new SlotItemHandler(handler, 0, 80, 35));
            });
        }

        // 玩家主背包
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // 玩家快捷栏
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            result = stackInSlot.copy();

            if (index == 0) {
                if (!this.moveItemStackTo(stackInSlot, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (AirPurifierBlockEntity.isAirEssence(stackInSlot)) {
                if (!this.moveItemStackTo(stackInSlot, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 28) {
                if (!this.moveItemStackTo(stackInSlot, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 37) {
                if (!this.moveItemStackTo(stackInSlot, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }

        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, DTBlocks.AIR_PURIFIER.get());
    }

    public AirPurifierBlockEntity getBlockEntity() {
        return blockEntity;
    }
}