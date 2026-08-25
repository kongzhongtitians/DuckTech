package org.quiltmc.users.duckteam.DuckTech.gui.juice_extractor;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlocks;
import org.quiltmc.users.duckteam.DuckTech.blocks.blockentity.JuiceExtractorBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.gui.DTMenu;

public class JuiceExtractorMenu extends AbstractContainerMenu {

    private final JuiceExtractorBlockEntity blockEntity;
    private final Level level;

    // 客户端构造
    public JuiceExtractorMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, null);
    }

    // 服务端构造
    public JuiceExtractorMenu(int id, Inventory playerInventory, JuiceExtractorBlockEntity blockEntity) {
        super(DTMenu.JUICE_EXTRACTOR_MENU.get(), id);
        this.blockEntity = blockEntity;
        this.level = playerInventory.player.level();

        if (blockEntity != null) {
            IItemHandler handler = blockEntity.getItemHandler();
            // 输入槽
            this.addSlot(new SlotItemHandler(handler, JuiceExtractorBlockEntity.SLOT_INPUT, 44, 35));
            // 桶槽
            this.addSlot(new SlotItemHandler(handler, JuiceExtractorBlockEntity.SLOT_BUCKET, 80, 35));
            // 输出槽
            this.addSlot(new SlotItemHandler(handler, JuiceExtractorBlockEntity.SLOT_OUTPUT, 116, 35));
        }

        // 玩家背包
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index < 3) {
                // 从机器槽移到玩家背包
                if (!this.moveItemStackTo(stack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // 从玩家背包移到机器槽
                if (stack.is(Item.byBlock(DTBlocks.RUBBER_WOOD.get()))) {
                    if (!this.moveItemStackTo(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (stack.is(Items.BUCKET)) {
                    if (!this.moveItemStackTo(stack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, DTBlocks.JUICE_EXTRACTOR.get());
    }
}