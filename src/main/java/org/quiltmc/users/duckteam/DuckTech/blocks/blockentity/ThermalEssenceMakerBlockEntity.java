package org.quiltmc.users.duckteam.DuckTech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemStackHandler;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.config.DTConfig;
import org.quiltmc.users.duckteam.DuckTech.gui.thermal_essence_maker.ThermalEssenceMakerMenu;
import org.quiltmc.users.duckteam.DuckTech.items.DTItems;
import org.quiltmc.users.duckteam.DuckTech.sounds.DTSounds;

import javax.annotation.Nullable;

public class ThermalEssenceMakerBlockEntity extends BlockEntity implements MenuProvider, Container {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int INVENTORY_SIZE = 2;

    private final ItemStackHandler itemHandler = new ItemStackHandler(INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return slot == SLOT_INPUT && getBurnTime(stack) >= 400;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot == SLOT_INPUT && getBurnTime(stack) < 400) return stack;
            return super.insertItem(slot, stack, simulate);
        }
    };

    private int progress = 0;
    private int maxProgress = 0;
    private int outputCount = 0;

    public ThermalEssenceMakerBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.THERMAL_ESSENCE_MAKER.get(), pos, state);
    }

    private static int getBurnTime(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, null);
    }

    private void tryStartProcessing() {
        if (outputCount > 0) return;

        ItemStack input = itemHandler.getStackInSlot(SLOT_INPUT);
        if (input.isEmpty()) return;

        int burn = getBurnTime(input);
        int count = burn / 400;
        if (count <= 0) return;

        ItemStack output = itemHandler.getStackInSlot(SLOT_OUTPUT);
        ItemStack essence = new ItemStack(DTItems.THERMAL_ESSENCE.get());
        int maxStack = essence.getMaxStackSize();

        if (!output.isEmpty() &&
                (!output.is(essence.getItem()) || output.getCount() + count > maxStack)) {
            return;
        }

        outputCount = count;
        progress = 0;
        maxProgress = burn;
        setChanged();
    }

    public void tickInternal(BlockPos pos) {
        if (level == null || level.isClientSide) return;

        if (outputCount <= 0) {
            tryStartProcessing();
            if (outputCount <= 0) {
                if (progress != 0) {
                    progress = 0;
                    setChanged();
                }
                return;
            }
        }

        ItemStack input = itemHandler.getStackInSlot(SLOT_INPUT);
        if (input.isEmpty() || getBurnTime(input) < 400) {
            outputCount = 0;
            progress = 0;
            setChanged();
            return;
        }

        progress++;
        if (progress >= maxProgress) {
            if (!level.isClientSide()&& DTConfig.switch_sound()) {
                level.playSound(null, pos,
                        DTSounds.ZAOYIN.get(),
                        SoundSource.BLOCKS,
                        1.0F,
                        1.0F);
            }

            input.shrink(1);

            ItemStack output = itemHandler.getStackInSlot(SLOT_OUTPUT);
            ItemStack essence = new ItemStack(DTItems.THERMAL_ESSENCE.get());
            if (output.isEmpty()) {
                essence.setCount(outputCount);
                itemHandler.setStackInSlot(SLOT_OUTPUT, essence);
            } else {
                output.grow(outputCount);
            }

            outputCount = 0;
            progress = 0;
            setChanged();

            tryStartProcessing();
        }
        setChanged();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("progress", progress);
        tag.putInt("maxProgress", maxProgress);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("progress");
        maxProgress = tag.getInt("maxProgress");
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ducktech.thermal_essence_maker");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player) {
        return new ThermalEssenceMakerMenu(containerId, playerInv, this);
    }

    public Container getInventory() {
        return this;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public int getContainerSize() {
        return itemHandler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = itemHandler.extractItem(slot, amount, false);
        setChanged();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = itemHandler.getStackInSlot(slot);
        itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (level == null || level.getBlockEntity(worldPosition) != this) return false;
        return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ThermalEssenceMakerBlockEntity entity) {
        if (level.isClientSide) return;
        entity.tickInternal(pos);
    }
}
