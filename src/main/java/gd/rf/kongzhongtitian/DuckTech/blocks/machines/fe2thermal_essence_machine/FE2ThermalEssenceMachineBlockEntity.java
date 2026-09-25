package gd.rf.kongzhongtitian.DuckTech.blocks.machines.fe2thermal_essence_machine;

import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.items.DTItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FE2ThermalEssenceMachineBlockEntity extends BlockEntity implements MenuProvider {

    // 能量存储
    private final EnergyStorage energyStorage = new EnergyStorage(10000, 1000, 0, 0);
    private final LazyOptional<EnergyStorage> energyLazy = LazyOptional.of(() -> energyStorage);

    // 物品存储：只有一个输出槽
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }
    };
    private final LazyOptional<ItemStackHandler> itemLazy = LazyOptional.of(() -> itemHandler);

    private int tickCounter = 0;
    private static final int TICKS_PER_OPERATION = 20;
    private static final int ENERGY_PER_OPERATION = 100;

    public FE2ThermalEssenceMachineBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.FE2THERMAL_ESSENCE_MACHINE_BLOCK_ENTITY.get(), pos, state);
    }

    public void serverTick() {
        if (level == null || level.isClientSide()) return;

        tickCounter++;
        if (tickCounter >= TICKS_PER_OPERATION) {
            tickCounter = 0;

            if (energyStorage.getEnergyStored() < ENERGY_PER_OPERATION) {
                return;
            }

            ItemStack output = itemHandler.getStackInSlot(0);
            if (output.isEmpty()) {
                energyStorage.extractEnergy(ENERGY_PER_OPERATION, false);
                itemHandler.setStackInSlot(0, new ItemStack(DTItems.THERMAL_ESSENCE.get(), 1));
            } else if (output.getItem() == DTItems.THERMAL_ESSENCE.get() && output.getCount() < output.getMaxStackSize()) {
                energyStorage.extractEnergy(ENERGY_PER_OPERATION, false);
                output.grow(1);
                itemHandler.setStackInSlot(0, output);
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.oakgenerator.oak_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FE2ThermalEssenceMachineMenu(containerId, playerInventory, this);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyLazy.cast();
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemLazy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyLazy.invalidate();
        itemLazy.invalidate();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        energyStorage.deserializeNBT(tag.get("Energy"));
        itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Energy", energyStorage.serializeNBT());
        tag.put("Inventory", itemHandler.serializeNBT());
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }
}