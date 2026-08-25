package org.quiltmc.users.duckteam.DuckTech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlocks;
import org.quiltmc.users.duckteam.DuckTech.gui.juice_extractor.JuiceExtractorMenu;
import org.quiltmc.users.duckteam.DuckTech.items.DTItems;

public class JuiceExtractorBlockEntity extends BlockEntity implements MenuProvider {

    // 槽位索引
    public static final int SLOT_INPUT = 0;      // 输入：rubber_wood
    public static final int SLOT_BUCKET = 1;     // 输入：桶
    public static final int SLOT_OUTPUT = 2;     // 输出：rubber_bucket

    private static final int MAX_RUBBER = 10000; // 橡胶最大存储量（可调整）
    private static final int RUBBER_PER_WOOD = 6000; // 每个 rubber_wood 提供的橡胶点
    private static final int RUBBER_CONSUME_PER_BUCKET = 1000; // 制造一个 rubber_bucket 所需橡胶

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case SLOT_INPUT -> stack.is(Item.byBlock(DTBlocks.RUBBER_WOOD.get()));
                case SLOT_BUCKET -> stack.is(Items.BUCKET); // 原版桶
                case SLOT_OUTPUT -> false; // 输出槽不允许手动放入
                default -> false;
            };
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private int rubberAmount = 0;          // 当前存储的橡胶点
    private int remainingFromWood = 0;    // 当前正在加工的 rubber_wood 剩余可产橡胶

    public JuiceExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.JUICE_EXTRACTOR_BLOCK_ENTITY.get(), pos, state);
    }

    // 每 tick 由方块调用，通常在方块实体的 tick 方法中注册
    public static void serverTick(Level level, BlockPos pos, BlockState state, JuiceExtractorBlockEntity be) {
        if (level.isClientSide) return;
        be.tick();
    }

    private void tick() {
        if (level == null) return;

        // 1. 从输入槽消耗 rubber_wood 生产橡胶
        ItemStack inputStack = itemHandler.getStackInSlot(SLOT_INPUT);
        if (!inputStack.isEmpty()) {
            if (remainingFromWood <= 0) {
                // 需要新的 rubber_wood
                if (rubberAmount < MAX_RUBBER) {
                    inputStack.shrink(1);
                    remainingFromWood = RUBBER_PER_WOOD;
                    setChanged();
                }
            }
            if (remainingFromWood > 0 && rubberAmount < MAX_RUBBER) {
                rubberAmount++;
                remainingFromWood--;
                setChanged();
            }
        }

        // 2. 如果桶槽有桶且橡胶足够，尝试生产 rubber_bucket
        ItemStack bucketStack = itemHandler.getStackInSlot(SLOT_BUCKET);
        ItemStack outputStack = itemHandler.getStackInSlot(SLOT_OUTPUT);

        if (!bucketStack.isEmpty() && rubberAmount >= RUBBER_CONSUME_PER_BUCKET) {
            ItemStack result = new ItemStack(DTItems.RUBBER_BUCKET.get());
            boolean canInsert = false;
            if (outputStack.isEmpty()) {
                canInsert = true;
            } else if (outputStack.is(result.getItem()) && outputStack.getCount() + 1 <= outputStack.getMaxStackSize()) {
                canInsert = true;
            }

            if (canInsert) {
                rubberAmount -= RUBBER_CONSUME_PER_BUCKET;
                bucketStack.shrink(1);
                if (outputStack.isEmpty()) {
                    itemHandler.setStackInSlot(SLOT_OUTPUT, result.copy());
                } else {
                    outputStack.grow(1);
                }
                setChanged();
            }
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("rubberAmount", rubberAmount);
        tag.putInt("remainingFromWood", remainingFromWood);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        rubberAmount = tag.getInt("rubberAmount");
        remainingFromWood = tag.getInt("remainingFromWood");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.ducktech.juice_extractor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new JuiceExtractorMenu(id, playerInventory, this);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public int getRubberAmount() {
        return rubberAmount;
    }

    public int getRemainingFromWood() {
        return remainingFromWood;
    }

    // 客户端同步橡胶量用（可选）
    public void setRubberAmount(int amount) {
        this.rubberAmount = amount;
    }

    public void setRemainingFromWood(int remaining) {
        this.remainingFromWood = remaining;
    }
}