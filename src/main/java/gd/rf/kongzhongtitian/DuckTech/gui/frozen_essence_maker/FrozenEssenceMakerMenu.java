package gd.rf.kongzhongtitian.DuckTech.gui.frozen_essence_maker;

import gd.rf.kongzhongtitian.DuckTech.blocks.DTBlocks;
import gd.rf.kongzhongtitian.DuckTech.blocks.blockentity.FrozenEssenceMakerBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.gui.DTMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class FrozenEssenceMakerMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final ContainerData data;

    // 服务端/客户端通用构造：传入实体
    public FrozenEssenceMakerMenu(int containerId, Inventory playerInv, FrozenEssenceMakerBlockEntity entity) {
        super(DTMenu.FROZEN_ESSENCE_MAKER_MENU.get(), containerId);
        this.access = ContainerLevelAccess.create(entity.getLevel(), entity.getBlockPos());

        // 用 ContainerData 绑定实体的进度数据，自动同步到客户端
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
            public void set(int index, int value) {}
            @Override
            public int getCount() { return 2; }
        };
        addDataSlots(this.data);

        // 机器槽位
        addSlot(new SlotItemHandler(entity.getItemHandler(), FrozenEssenceMakerBlockEntity.SLOT_INPUT, 56, 35));
        addSlot(new SlotItemHandler(entity.getItemHandler(), FrozenEssenceMakerBlockEntity.SLOT_OUTPUT, 116, 35));

        // 玩家物品栏
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        for (int k = 0; k < 9; ++k)
            addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
    }

    // 备用构造：通过 BlockPos 获取实体（用于客户端或特殊情况）
    public FrozenEssenceMakerMenu(int containerId, Inventory playerInv, BlockPos pos) {
        this(containerId, playerInv,
                (FrozenEssenceMakerBlockEntity) playerInv.player.level().getBlockEntity(pos));
        // 若实体为 null，会抛出 ClassCastException，但实际打开 GUI 时实体必然存在
    }

    // 供 Screen 读取进度
    public int getProgress() {
        return data.get(0);
    }

    public int getMaxProgress() {
        return data.get(1);
    }

    // 计算箭头绘制的宽度（箭头纹理总宽 24 像素）
    public int getScaleArrowProgress() {
        int progress = getProgress();
        int max = getMaxProgress();
        return max == 0 ? 0 : progress * 24 / max;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(access, player, DTBlocks.FROZEN_ESSENCE_MAKER.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < 2) { // 机器槽位 → 玩家背包
                if (!moveItemStackTo(stack, 2, 38, true))
                    return ItemStack.EMPTY;
            } else { // 玩家背包 → 输入槽
                if (!moveItemStackTo(stack, 0, 1, false))
                    return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }
        return result;
    }
}