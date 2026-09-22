package gd.rf.kongzhongtitian.DuckTech.gui.advance_shredder;

import gd.rf.kongzhongtitian.DuckTech.blocks.DTBlocks;
import gd.rf.kongzhongtitian.DuckTech.blocks.blockentity.AdvanceShredderBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.gui.DTMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class AdvanceShredderMenu extends AbstractContainerMenu {
    public final AdvanceShredderBlockEntity blockEntity;
    public final Level level;
    private final ContainerData data;

    public AdvanceShredderMenu(int containerId, Inventory inventory,FriendlyByteBuf friendlyByteBuf) {
        this(containerId, inventory, ((AdvanceShredderBlockEntity) inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos())), new SimpleContainerData(2));
    }

    public AdvanceShredderMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(DTMenu.ADVANCE_SHREDDER_MENU.get(), containerId);

        blockEntity = (AdvanceShredderBlockEntity) entity;
        this.level = inv.player.level();
        this.data = data;

        //重要！！！！！！！！！！！！！！ 同步数据
        addDataSlots(data);//耗时半小时，拼尽全力终于战胜
        addPlayerInventory(inv);
        addPlayerHotbar(inv);


        // 添加机器槽位
        IItemHandler itemHandler = blockEntity.itemStackHandler;
        // 输入槽位
        this.addSlot(new SlotItemHandler(itemHandler, AdvanceShredderBlockEntity.INPUT_SLOT_1, 54, 24));
        this.addSlot(new SlotItemHandler(itemHandler, AdvanceShredderBlockEntity.INPUT_SLOT_2, 54, 44));
        // 输出槽位
        this.addSlot(new SlotItemHandler(itemHandler, AdvanceShredderBlockEntity.OUTPUT_SLOT_1, 116, 18){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });
        this.addSlot(new SlotItemHandler(itemHandler, AdvanceShredderBlockEntity.OUTPUT_SLOT_2, 116, 36){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });
        this.addSlot(new SlotItemHandler(itemHandler, AdvanceShredderBlockEntity.OUTPUT_SLOT_3, 116, 54){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });
    }




    public boolean isCrafting() {
//        System.out.println(data.get(0) +","+ data.get(1));
        return data.get(0) > 0;
    }

    public int getScaleArrowProgress(){
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowPixelSize = 24;

        return maxProgress != 0 &&  progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
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
        // 36-37: 输入槽 (2 slots) [INPUT_SLOT_1, INPUT_SLOT_2]
        // 38-40: 输出槽 (3 slots) [OUTPUT_SLOT_1, OUTPUT_SLOT_2, OUTPUT_SLOT_3]

        if (index < 36) {
            // 从玩家物品栏移动到机器槽位
            // 首先尝试放入输入槽（36-37）
            if (!moveItemStackTo(sourceStack, 36, 38, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index >= 36 && index < 38) {
            // 从输入槽移动到玩家物品栏（0-35）
            if (!moveItemStackTo(sourceStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index >= 38 && index < 41) {
            // 从输出槽移动到玩家物品栏（0-35）
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
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, DTBlocks.ADVANCE_SHREDDER.get());
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

    public ContainerData getData() {
        return data;
    }
}
