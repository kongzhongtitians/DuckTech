package org.quiltmc.users.duckteam.DuckTech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.config.DTConfig;
import org.quiltmc.users.duckteam.DuckTech.gui.expulsion_machine.ExpulsionMachineMenu;
import org.quiltmc.users.duckteam.DuckTech.items.DTItems;
import org.quiltmc.users.duckteam.DuckTech.sounds.DTSounds;

public class ExpulsionMachineBlockEntity extends BlockEntity implements MenuProvider, WorldlyContainer {
    public static final int SLOT_COUNT = 1;
    public static final int FUEL_SLOT = 0;
    public static final int MAX_TIMER = 600; // 30 seconds * 20 ticks

    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private int timer = 0;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> timer;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) timer = value;
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public ExpulsionMachineBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.EXPULSION_MACHINE.get(), pos, state);
    }

    public NonNullList<ItemStack> getInventory() {
        return items;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ExpulsionMachineBlockEntity be) {
        if (be.timer > 0) {
            be.timer--;
            be.expelUndead();
            be.setChanged();
        } else {
            // try to consume fuel
            ItemStack fuel = be.items.get(FUEL_SLOT);
            if (fuel.is(DTItems.VOID_ESSENCE.get())) {
                fuel.shrink(1);
                be.timer = MAX_TIMER;
                if (!level.isClientSide()&& DTConfig.switch_sound()) {
                    level.playSound(null, pos,
                            DTSounds.ZAOYIN.get(),
                            SoundSource.BLOCKS,
                            1.0F,
                            1.0F);
                }
                be.setChanged();
            }
        }
    }

    private void expelUndead() {
        if (level == null || level.isClientSide) return;
        double half = 3.5; // radius for 7x7x7 area
        BlockPos pos = getBlockPos();
        Vec3 center = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        AABB bounds = new AABB(center.x - half, center.y - half, center.z - half,
                center.x + half, center.y + half, center.z + half);

        var undeadList = level.getEntitiesOfClass(Mob.class, bounds,
                e -> e.getMobType() == MobType.UNDEAD);

        for (Mob mob : undeadList) {
            Vec3 mobPos = mob.position();
            Vec3 dir = mobPos.subtract(center).normalize();
            if (dir.lengthSqr() == 0) dir = new Vec3(0, 1, 0);
            Vec3 newPos = center.add(dir.scale(half + 1.0));
            mob.teleportTo(newPos.x, newPos.y, newPos.z);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
        this.timer = tag.getInt("Timer");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putInt("Timer", timer);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ducktech.expulsion_machine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new ExpulsionMachineMenu(windowId, playerInv, this, dataAccess);
    }

    // ----- WorldlyContainer implementation (for hopper input) -----
    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{FUEL_SLOT};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return stack.is(DTItems.VOID_ESSENCE.get());
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        return items.get(FUEL_SLOT).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(items, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
    }

    @Override
    public boolean stillValid(Player player) {
        if (level == null || level.getBlockEntity(worldPosition) != this) return false;
        return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5) <= 64.0;
    }

    private LazyOptional<IItemHandler> itemHandler = LazyOptional.empty();

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && side != null && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.DOWN) return LazyOptional.empty();
            if (!itemHandler.isPresent()) {
                itemHandler = LazyOptional.of(() -> new SidedInvWrapper(this, side));
            }
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    // Forge capability (hopper interaction)
    private final LazyOptional<? extends IItemHandler>[] handlers =
            SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<?> handler : handlers) handler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        handlers[0] = LazyOptional.of(() -> new SidedInvWrapper(this, Direction.UP));
        handlers[1] = LazyOptional.of(() -> new SidedInvWrapper(this, Direction.DOWN));
        handlers[2] = LazyOptional.of(() -> new SidedInvWrapper(this, Direction.NORTH));
    }
}
