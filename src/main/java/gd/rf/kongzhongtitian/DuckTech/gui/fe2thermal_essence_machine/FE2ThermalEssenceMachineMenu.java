package gd.rf.kongzhongtitian.DuckTech.gui.fe2thermal_essence_machine;

import gd.rf.kongzhongtitian.DuckTech.blocks.blockentity.FE2ThermalEssenceMachineBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.gui.DTMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class FE2ThermalEssenceMachineMenu extends AbstractContainerMenu {

    private final FE2ThermalEssenceMachineBlockEntity blockEntity;
    private final ContainerData data;

    // 数据槽索引：0=能量，1=最大能量
    public FE2ThermalEssenceMachineMenu(int containerId, Inventory playerInventory, FE2ThermalEssenceMachineBlockEntity entity) {
        super(DTMenu.FE2THERMAL_ESSENCE_MACHINE_MENU.get(), containerId);
        this.blockEntity = entity;

        // 同步能量数据
        this.data = new SimpleContainerData(2);
        this.addDataSlots(data);

        // 输出槽（位置：GUI 中 80,35）
        this.addSlot(new SlotItemHandler(entity.getItemHandler(), 0, 80, 35) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false; // 禁止放入任何物品
            }

            @Override
            public boolean mayPickup(Player player) {
                return true; // 允许取出
            }
        });

        // 玩家物品栏
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

    // 当数据槽更新时由服务器自动调用，这里手动刷新到客户端
    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        // 同步能量值
        this.data.set(0, blockEntity.getEnergyStorage().getEnergyStored());
        this.data.set(1, blockEntity.getEnergyStorage().getMaxEnergyStored());
    }

    // 快速移动逻辑
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index == 0) {
                // 从输出槽移到玩家背包
                if (!this.moveItemStackTo(stack, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // 玩家背包不能放入输出槽
                if (!this.moveItemStackTo(stack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.getLevel() != null &&
                blockEntity.getLevel().getBlockEntity(blockEntity.getBlockPos()) == blockEntity &&
                player.distanceToSqr(blockEntity.getBlockPos().getX() + 0.5,
                        blockEntity.getBlockPos().getY() + 0.5,
                        blockEntity.getBlockPos().getZ() + 0.5) <= 64.0;
    }

    public int getEnergy() {
        return data.get(0);
    }

    public int getMaxEnergy() {
        return data.get(1);
    }
}