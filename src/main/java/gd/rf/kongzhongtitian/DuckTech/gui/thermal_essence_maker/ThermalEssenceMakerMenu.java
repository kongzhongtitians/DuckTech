package gd.rf.kongzhongtitian.DuckTech.gui.thermal_essence_maker;

import gd.rf.kongzhongtitian.DuckTech.blocks.DTBlocks;
import gd.rf.kongzhongtitian.DuckTech.blocks.blockentity.ThermalEssenceMakerBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.gui.DTMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class ThermalEssenceMakerMenu extends AbstractContainerMenu {
    private final ThermalEssenceMakerBlockEntity blockEntity;
    private final ContainerData data;

    // 客户端构造
    public ThermalEssenceMakerMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, null);
    }

    // 服务端构造（或从网络构造）
    public ThermalEssenceMakerMenu(int containerId, Inventory playerInv,
                                   @Nullable ThermalEssenceMakerBlockEntity entity) {
        super(DTMenu.THERMAL_ESSENCE_MAKER.get(), containerId);
        this.blockEntity = entity;

        if (entity != null) {
            // 添加方块实体的槽位
            addSlot(new SlotItemHandler(entity.getItemHandler(), 0, 56, 35));
            addSlot(new SlotItemHandler(entity.getItemHandler(), 1, 116, 35));

            // 同步进度数据
            this.data = new ContainerData() {
                @Override
                public int get(int index) {
                    return switch (index) {
                        case 0 -> entity.getProgress();
                        case 1 -> entity.getMaxProgress();
                        default -> 0;
                    };
                }

                @Override
                public void set(int index, int value) {
                    // no-op
                }

                @Override
                public int getCount() {
                    return 2;
                }
            };
        } else {
            // 客户端时，用虚拟数据
            this.data = new SimpleContainerData(2);
        }
        addDataSlots(data);

        // 玩家物品栏
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    public int getProgress() {
        return data.get(0);
    }

    public int getMaxProgress() {
        return data.get(1);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // 标准的 shift 点击逻辑
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < 2) {
                if (!moveItemStackTo(stack, 2, 38, true)) return ItemStack.EMPTY;
            } else {
                if (!moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity != null &&
                AbstractContainerMenu.stillValid(
                        ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
                        player, DTBlocks.THERMAL_ESSENCE_MAKER.get());
    }
}