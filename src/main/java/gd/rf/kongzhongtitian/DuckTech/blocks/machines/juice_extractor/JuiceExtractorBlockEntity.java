package gd.rf.kongzhongtitian.DuckTech.blocks.machines.juice_extractor;

import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlocks;
import gd.rf.kongzhongtitian.DuckTech.items.DTItems;
import gd.rf.kongzhongtitian.DuckTech.recipe.CountedIngredient;
import gd.rf.kongzhongtitian.DuckTech.recipe.DTRecipe;
import gd.rf.kongzhongtitian.DuckTech.utils.RecipeOutputUtil;
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

import java.util.List;
import java.util.Optional;

public class JuiceExtractorBlockEntity extends BlockEntity implements MenuProvider {

    public static final int SLOT_INPUT  = 0;  // 橡胶木（触发原流程）或配方输入 1
    public static final int SLOT_BUCKET = 1;  // 空桶（原流程）或配方输入 2
    public static final int SLOT_OUTPUT = 2;

    /** 当前工作模式：1=橡胶木原流程，0=配方驱动 */
    public static final int MODE_LEGACY = 1;
    public static final int MODE_RECIPE = 0;

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
                // 输入槽：橡胶木（原流程）或任一配方的输入
                case SLOT_INPUT  -> stack.is(Item.byBlock(DTBlocks.RUBBER_WOOD.get())) || isRecipeInput(stack);
                // 桶槽：空桶（原流程）或任一配方的输入
                case SLOT_BUCKET -> stack.is(Items.BUCKET) || isRecipeInput(stack);
                default -> false;
            };
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    // ---- 原流程（橡胶木）状态 ----
    private int rubberAmount = 0;
    private int remainingFromWood = 0;

    // ---- 配方驱动流程状态 ----
    private int recipeProgress = 0;
    private int recipeMaxProgress = 0;
    private int mode = MODE_RECIPE;

    public JuiceExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.JUICE_EXTRACTOR_BLOCK_ENTITY.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, JuiceExtractorBlockEntity be) {
        be.tick();
    }

    private void tick() {
        if (level == null || level.isClientSide) return;

        boolean changed = false;

        ItemStack inputStack = itemHandler.getStackInSlot(SLOT_INPUT);
        boolean legacyMode = !inputStack.isEmpty() && inputStack.is(Item.byBlock(DTBlocks.RUBBER_WOOD.get()));

        if (legacyMode) {
            // ---- 原流程：放入橡胶木时按原逻辑工作 ----
            // 从配方模式切回时，清空配方进度
            if (recipeProgress != 0 || recipeMaxProgress != 0) {
                recipeProgress = 0;
                recipeMaxProgress = 0;
                changed = true;
            }
            mode = MODE_LEGACY;

            // 1. 尝试从输入槽取一块木头开始加工
            if (remainingFromWood <= 0 && rubberAmount < MAX_RUBBER) {
                if (!inputStack.isEmpty() && inputStack.is(Item.byBlock(DTBlocks.RUBBER_WOOD.get()))) {
                    inputStack.shrink(1);
                    remainingFromWood = RUBBER_PER_WOOD;
                    changed = true;
                }
            }

            // 2. 把剩余橡胶点转到 rubberAmount（与输入槽是否为空无关）
            if (remainingFromWood > 0 && rubberAmount < MAX_RUBBER) {
                int canAdd = Math.min(remainingFromWood, MAX_RUBBER - rubberAmount);
                int step   = Math.min(canAdd, RUBBER_PER_TICK);
                rubberAmount += step;
                remainingFromWood -= step;
                changed = true;
            }

            // 3. 有桶且橡胶够时产出 rubber_bucket
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
        } else {
            // ---- 配方驱动流程：非橡胶木时按配方工作（2 输入 → 1 输出）----
            mode = MODE_RECIPE;

            Optional<JuiceExtractorRecipe> recipe = getRecipe();
            if (recipe.isPresent() && canFitOutputs(recipe.get())) {
                int processingTime = Math.max(1, recipe.get().getProcessingTime());
                if (recipeMaxProgress != processingTime) {
                    recipeMaxProgress = processingTime;
                    changed = true;
                }
                recipeProgress++;
                changed = true;
                if (recipeProgress >= recipeMaxProgress) {
                    craftItem(recipe.get());
                    recipeProgress = 0;
                }
            } else {
                // 无匹配配方或输出放不下时，进度归零
                if (recipeProgress != 0 || recipeMaxProgress != 0) {
                    recipeProgress = 0;
                    recipeMaxProgress = 0;
                    changed = true;
                }
            }
        }

        if (changed) {
            setChanged();
        }
    }

    /** 用当前两个输入槽的内容匹配配方（顺序无关） */
    private Optional<JuiceExtractorRecipe> getRecipe() {
        if (level == null) return Optional.empty();
        return RecipeOutputUtil.getRecipe(DTRecipe.JUICE_EXTRACTOR_RECIPE.get(),
                List.of(
                        itemHandler.getStackInSlot(SLOT_INPUT),
                        itemHandler.getStackInSlot(SLOT_BUCKET)),
                level);
    }

    private boolean canFitOutputs(JuiceExtractorRecipe recipe) {
        return RecipeOutputUtil.canFitOutputs(recipe.getOutputs(),
                List.of(itemHandler.getStackInSlot(SLOT_OUTPUT)));
    }

    private void craftItem(JuiceExtractorRecipe recipe) {
        RecipeOutputUtil.consumeInputs(recipe, itemHandler, List.of(SLOT_INPUT, SLOT_BUCKET));
        RecipeOutputUtil.produceOutputs(recipe.getOutputs(), itemHandler, List.of(SLOT_OUTPUT));
    }

    /** 判断物品类型是否为任一 juice_extractor 配方的输入（配方数据驱动） */
    private boolean isRecipeInput(ItemStack stack) {
        if (level == null) return true; // 世界未就绪时宽松放行
        for (JuiceExtractorRecipe recipe : level.getRecipeManager()
                .getAllRecipesFor(DTRecipe.JUICE_EXTRACTOR_RECIPE.get())) {
            for (CountedIngredient input : recipe.getInputs()) {
                // 槽位校验只看物品类型，不看数量；
                // 否则单个物品（如配方需要4个橡胶）永远放不进槽，无法叠满
                if (input.ingredient().test(stack)) return true;
            }
        }
        return false;
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
        tag.putInt("recipeProgress", recipeProgress);
        tag.putInt("recipeMaxProgress", recipeMaxProgress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        rubberAmount = tag.getInt("rubberAmount");
        remainingFromWood = tag.getInt("remainingFromWood");
        recipeProgress = tag.getInt("recipeProgress");
        recipeMaxProgress = tag.getInt("recipeMaxProgress");
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
    public int getRecipeProgress() { return recipeProgress; }
    public int getRecipeMaxProgress() { return recipeMaxProgress; }
    public int getMode() { return mode; }
}
