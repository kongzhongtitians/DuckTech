package gd.rf.kongzhongtitian.DuckTech.gui.levitation;

import gd.rf.kongzhongtitian.DuckTech.blocks.blockentity.LevitationMachineBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.gui.DTMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class LevitationMachineMenu extends AbstractContainerMenu {
    public final LevitationMachineBlockEntity blockEntity;
    public final ContainerData data;

    public LevitationMachineMenu(int containerId, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(containerId, inventory, (LevitationMachineBlockEntity) inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos()), new SimpleContainerData(1));
    }

    public LevitationMachineMenu(int containerId, Inventory inv, LevitationMachineBlockEntity entity , ContainerData data) {
        super(DTMenu.LEVITATION_MACHINE_MENU.get(), containerId);
        this.blockEntity = entity;
        this.data = data;

        addDataSlots(data);

        IItemHandler itemHandler = blockEntity.itemStackHandler;

        // 添加输入槽位
        this.addSlot(new SlotItemHandler(itemHandler, 0, 80, 36));

        // 添加玩家物品栏槽位
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    public int getTime(){
        return data.get(0);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // 0-35: 玩家物品栏 (36 slots)
        // 36: 机器槽位 (1 slot)

        if (index < 36) {
            // 从玩家物品栏移动到机器槽位
            if (!moveItemStackTo(sourceStack, 36, 37, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index == 36) {
            // 从机器槽位移动到玩家物品栏
            if (!moveItemStackTo(sourceStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex: " + index);
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        // 修复：实现正确的有效性检查
        return blockEntity != null &&
                blockEntity.getLevel() != null &&
                blockEntity.getLevel().getBlockEntity(blockEntity.getBlockPos()) == blockEntity &&
                player.distanceToSqr(
                        blockEntity.getBlockPos().getX() + 0.5D,
                        blockEntity.getBlockPos().getY() + 0.5D,
                        blockEntity.getBlockPos().getZ() + 0.5D
                ) <= 64.0D;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}