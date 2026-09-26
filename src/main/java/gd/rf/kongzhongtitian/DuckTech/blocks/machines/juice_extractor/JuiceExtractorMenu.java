package gd.rf.kongzhongtitian.DuckTech.blocks.machines.juice_extractor;

import gd.rf.kongzhongtitian.DuckTech.blocks.gui.DTMenu;
import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlocks;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class JuiceExtractorMenu extends AbstractContainerMenu {

    private final JuiceExtractorBlockEntity blockEntity;
    private final Level level;

    // 客户端缓存（由 Forge 通过 ClientboundContainerSetDataPacket 填充）
    private int clientRubberAmount;
    private int clientRemainingFromWood;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            // ★ 关键修复：用 isClientSide 判断，不能用 blockEntity != null
            //   因为客户端也能拿到 blockEntity，但它的字段是旧的/未同步的。
            if (level != null && !level.isClientSide && blockEntity != null) {
                return switch (index) {
                    case 0 -> blockEntity.getRubberAmount();
                    case 1 -> blockEntity.getRemainingFromWood();
                    default -> 0;
                };
            }
            // 客户端：读 Forge 同步过来的缓存
            return switch (index) {
                case 0 -> clientRubberAmount;
                case 1 -> clientRemainingFromWood;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            // 只会在客户端被 Forge 调用
            switch (index) {
                case 0 -> clientRubberAmount = value;
                case 1 -> clientRemainingFromWood = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    // 客户端 fallback 构造（BE 未找到时）
    public JuiceExtractorMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, null);
    }

    // 服务端 / 客户端主构造
    public JuiceExtractorMenu(int id, Inventory playerInventory, JuiceExtractorBlockEntity blockEntity) {
        super(DTMenu.JUICE_EXTRACTOR_MENU.get(), id);
        this.blockEntity = blockEntity;
        this.level = playerInventory.player.level();

        IItemHandler handler = (blockEntity != null)
                ? blockEntity.getItemHandler()
                : new ItemStackHandler(3);

        this.addSlot(new SlotItemHandler(handler, JuiceExtractorBlockEntity.SLOT_INPUT,  44, 35));
        this.addSlot(new SlotItemHandler(handler, JuiceExtractorBlockEntity.SLOT_BUCKET, 80, 35));
        this.addSlot(new SlotItemHandler(handler, JuiceExtractorBlockEntity.SLOT_OUTPUT, 116, 35));

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        this.addDataSlots(this.data);
    }

    public int getRubberAmount() {
        return this.data.get(0);
    }

    public int getRemainingFromWood() {
        return this.data.get(1);
    }

    public JuiceExtractorBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();

            if (index < 3) {
                if (!this.moveItemStackTo(stack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
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
        if (blockEntity == null) return true;
        return stillValid(
                ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, DTBlocks.JUICE_EXTRACTOR.get());
    }
}