package gd.rf.kongzhongtitian.DuckTech.blocks.machines.juice_extractor;

import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlocks;
import gd.rf.kongzhongtitian.DuckTech.items.DTItems;
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

public class JuiceExtractorBlockEntity extends BlockEntity implements MenuProvider {

    public static final int SLOT_INPUT  = 0;
    public static final int SLOT_BUCKET = 1;
    public static final int SLOT_OUTPUT = 2;

    private static final int MAX_RUBBER = 10000;
    private static final int RUBBER_PER_WOOD = 6000;
    private static final int RUBBER_CONSUME_PER_BUCKET = 1000;

    // 为了让 GUI 进度条一类的显示更平稳，可以每个 tick 只加少量，
    // 也可以一次加完。这里仍然一个 tick 一点，保持原行为。
    private static final int RUBBER_PER_TICK = 1;

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case SLOT_INPUT  -> stack.is(Item.byBlock(DTBlocks.RUBBER_WOOD.get()));
                case SLOT_BUCKET -> stack.is(Items.BUCKET);
                default -> false;
            };
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private int rubberAmount = 0;
    private int remainingFromWood = 0;

    public JuiceExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.JUICE_EXTRACTOR_BLOCK_ENTITY.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, JuiceExtractorBlockEntity be) {
        be.tick();
    }

    private void tick() {
        if (level == null || level.isClientSide) return;

        boolean changed = false;

        // ---- 1. 尝试从输入槽取一块木头开始加工 ----
        if (remainingFromWood <= 0 && rubberAmount < MAX_RUBBER) {
            ItemStack inputStack = itemHandler.getStackInSlot(SLOT_INPUT);
            if (!inputStack.isEmpty() && inputStack.is(Item.byBlock(DTBlocks.RUBBER_WOOD.get()))) {
                inputStack.shrink(1);
                remainingFromWood = RUBBER_PER_WOOD;
                changed = true;
            }
        }

        // ---- 2. 把剩余橡胶点转到 rubberAmount（与输入槽是否为空无关）----
        if (remainingFromWood > 0 && rubberAmount < MAX_RUBBER) {
            int canAdd = Math.min(remainingFromWood, MAX_RUBBER - rubberAmount);
            int step   = Math.min(canAdd, RUBBER_PER_TICK);
            rubberAmount += step;
            remainingFromWood -= step;
            changed = true;
        }

        // ---- 3. 有桶且橡胶够时产出 rubber_bucket ----
        ItemStack bucketStack = itemHandler.getStackInSlot(SLOT_BUCKET);
        ItemStack outputStack = itemHandler.getStackInSlot(SLOT_OUTPUT);

        if (!bucketStack.isEmpty() && rubberAmount >= RUBBER_CONSUME_PER_BUCKET) {
            ItemStack result = new ItemStack(DTItems.RUBBER_BUCKET.get());
            boolean canInsert = outputStack.isEmpty()
                    || (outputStack.is(result.getItem())
                    && outputStack.getCount() + 1 <= outputStack.getMaxStackSize());

            if (canInsert) {
                rubberAmount -= RUBBER_CONSUME_PER_BUCKET;
                bucketStack.shrink(1);
                if (outputStack.isEmpty()) {
                    itemHandler.setStackInSlot(SLOT_OUTPUT, result.copy());
                } else {
                    outputStack.grow(1);
                }
                changed = true;
            }
        }

        if (changed) {
            setChanged();
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
        return Component.translatable("block.ducktech.juice_extractor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new JuiceExtractorMenu(id, playerInventory, this);
    }

    public ItemStackHandler getItemHandler() { return itemHandler; }
    public int getRubberAmount() { return rubberAmount; }
    public int getRemainingFromWood() { return remainingFromWood; }
}