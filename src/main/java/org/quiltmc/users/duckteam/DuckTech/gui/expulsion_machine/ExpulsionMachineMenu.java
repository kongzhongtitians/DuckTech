package org.quiltmc.users.duckteam.DuckTech.gui.expulsion_machine;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlocks;
import org.quiltmc.users.duckteam.DuckTech.blocks.blockentity.ExpulsionMachineBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.gui.DTMenu;
import org.quiltmc.users.duckteam.DuckTech.items.DTItems;

public class ExpulsionMachineMenu extends AbstractContainerMenu {
    private final ExpulsionMachineBlockEntity blockEntity;
    private final ContainerData data;

    public ExpulsionMachineMenu(int windowId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(windowId, playerInv,
                (ExpulsionMachineBlockEntity) playerInv.player.level().getBlockEntity(extraData.readBlockPos()),
                new SimpleContainerData(1));
    }

    public ExpulsionMachineMenu(int windowId, Inventory playerInv,
                                ExpulsionMachineBlockEntity blockEntity, ContainerData data) {
        super(DTMenu.EXPULSION_MACHINE.get(), windowId);
        this.blockEntity = blockEntity;
        this.data = data;

        this.addSlot(new Slot(blockEntity, 0, 80, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(DTItems.VOID_ESSENCE.get());
            }
        });

        for (int row = 0; row < 3; ++row)
            for (int col = 0; col < 9; ++col)
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        for (int col = 0; col < 9; ++col)
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));

        this.addDataSlots(data);
    }


    public int getTimer() {
        return data.get(0);
    }

    public int getMaxTimer() {
        return ExpulsionMachineBlockEntity.MAX_TIMER;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack original = slot.getItem();
            copy = original.copy();
            if (index == 0) {
                if (!this.moveItemStackTo(original, 1, 37, true)) return ItemStack.EMPTY;
            } else if (original.is(DTItems.VOID_ESSENCE.get())) {
                if (!this.moveItemStackTo(original, 0, 1, false)) return ItemStack.EMPTY;
            } else if (index < 28) {
                if (!this.moveItemStackTo(original, 28, 37, false)) return ItemStack.EMPTY;
            } else if (index < 37) {
                if (!this.moveItemStackTo(original, 1, 28, false)) return ItemStack.EMPTY;
            }
            if (original.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
            if (original.getCount() == copy.getCount()) return ItemStack.EMPTY;
            slot.onTake(player, original);
        }
        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
                player, DTBlocks.EXPULSION_MACHINE.get());
    }
}
