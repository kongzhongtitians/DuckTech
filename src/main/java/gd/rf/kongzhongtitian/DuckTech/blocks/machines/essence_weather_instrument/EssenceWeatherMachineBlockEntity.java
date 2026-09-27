package gd.rf.kongzhongtitian.DuckTech.blocks.machines.essence_weather_instrument;

import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.items.DTItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EssenceWeatherMachineBlockEntity extends BlockEntity implements MenuProvider {

    /** 每次切换天气需要消耗的 AIR 精华数量 */
    public static final int REQUIRED_AIR = 64;
    /** 每次触发后的冷却（tick），防止连点 */
    private static final int COOLDOWN_TICKS = 100;

    /** 按钮模式 */
    public static final int WEATHER_CLEAR = 0;
    public static final int WEATHER_RAIN = 1;
    public static final int WEATHER_THUNDER = 2;

    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.is(DTItems.AIR_ESSENCE.get());
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final LazyOptional<ItemStackHandler> inventoryOptional = LazyOptional.of(() -> inventory);

    private int cooldown = 0;

    public EssenceWeatherMachineBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.ESSENCE_WEATHER_MACHINE_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EssenceWeatherMachineBlockEntity be) {
        be.serverTick();
    }

    /**
     * 只做冷却递减，不再自动消耗精华/自动切天气。
     * 天气切换完全由 GUI 按钮触发（见 applyWeather）。
     */
    private void serverTick() {
        if (level == null || level.isClientSide) {
            return;
        }
        if (cooldown > 0) {
            cooldown--;
        }
    }

    /**
     * 由按钮触发的天气切换。只有这里才会消耗精华。
     *
     * @param mode {@link #WEATHER_CLEAR} / {@link #WEATHER_RAIN} / {@link #WEATHER_THUNDER}
     * @return 是否成功执行
     */
    public boolean applyWeather(int mode) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return false;
        }

        // 冷却中直接拒绝
        if (cooldown > 0) {
            return false;
        }

        ItemStack fuel = inventory.getStackInSlot(0);
        if (!fuel.is(DTItems.AIR_ESSENCE.get()) || fuel.getCount() < REQUIRED_AIR) {
            return false;
        }

        // 先扣精华，再切天气
        fuel.shrink(REQUIRED_AIR);

        // setWeatherParameters(clearWeatherTime, weatherTime, raining, thundering)
        switch (mode) {
            case WEATHER_RAIN ->
                    serverLevel.setWeatherParameters(0, 12000, true, false);
            case WEATHER_THUNDER ->
                    serverLevel.setWeatherParameters(0, 12000, true, true);
            case WEATHER_CLEAR ->
                    serverLevel.setWeatherParameters(12000, 0, false, false);
            default -> {
                return false;
            }
        }

        cooldown = COOLDOWN_TICKS;
        setChanged();
        return true;
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
            Containers.dropItemStack(level,
                    worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5,
                    stack);
            inventory.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ducktech.essence_weather_machine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new EssenceWeatherMachineMenu(containerId, playerInventory, worldPosition);
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
        cooldown = tag.getInt("Cooldown");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
        tag.putInt("Cooldown", cooldown);
    }
}