package org.quiltmc.users.duckteam.DuckTech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.gui.air_purifier.AirPurifierMenu;

import java.util.List;

public class AirPurifierBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return isAirEssence(stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final LazyOptional<ItemStackHandler> inventoryOptional =
            LazyOptional.of(() -> inventory);

    private int tickCounter = 0;

    public AirPurifierBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.AIR_PURIFIER_BE.get(), pos, state);
    }

    public static void tick(
            Level level,
            BlockPos pos,
            BlockState state,
            AirPurifierBlockEntity blockEntity
    ) {
        blockEntity.serverTick();
    }

    public void serverTick() {
        if (level == null || level.isClientSide) {
            return;
        }

        tickCounter++;
        if (tickCounter % 20 != 0) {
            return;
        }

        // 以方块为中心，检测 15x15x15 立方体区域
        AABB area = new AABB(
                worldPosition.offset(-7, -7, -7),
                worldPosition.offset(7, 7, 7)
        );

        List<Player> players = level.getEntitiesOfClass(Player.class, area);
        int totalEffects = 0;

        for (Player player : players) {
            totalEffects += player.getActiveEffects().size();
        }

        if (totalEffects == 0) {
            return;
        }

        ItemStack fuel = inventory.getStackInSlot(0);
        if (!isAirEssence(fuel)) {
            return;
        }

        int required = totalEffects * 5;
        if (fuel.getCount() < required) {
            return;
        }

        fuel.shrink(required);

        for (Player player : players) {
            player.removeAllEffects();
        }

        setChanged();
    }

    public static boolean isAirEssence(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return key != null && key.toString().equals("ducktech:air_essence");
    }

    public LazyOptional<ItemStackHandler> getInventoryOptional() {
        return inventoryOptional;
    }

    public void dropContents() {
        if (level == null) {
            return;
        }
        ItemStack stack = inventory.getStackInSlot(0);
        if (!stack.isEmpty()) {
            Containers.dropItemStack(
                    level,
                    worldPosition.getX() + 0.5,
                    worldPosition.getY() + 0.5,
                    worldPosition.getZ() + 0.5,
                    stack
            );
            inventory.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ducktech.air_purifier");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new AirPurifierMenu(containerId, playerInventory, worldPosition);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return inventoryOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inventoryOptional.invalidate();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
    }
}