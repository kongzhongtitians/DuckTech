package gd.rf.kongzhongtitian.DuckTech.utils;

import gd.rf.kongzhongtitian.DuckTech.api.recipes.InputOutputRecipe;
import gd.rf.kongzhongtitian.DuckTech.recipe.CountedIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.SimpleContainer;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 配方输出工具类，用于简化通过输入获取输出的过程
 */
public class RecipeOutputUtil {

    /**
     * 根据输入物品和配方类型获取匹配的配方输出结果
     *
     * @param recipeType 配方类型
     * @param inputs 输入物品列表
     * @param level 世界等级对象
     * @return 输出物品列表，如果没有匹配的配方则返回空Optional
     */
    public static <T extends InputOutputRecipe> Optional<List<ItemStack>> getOutputs(RecipeType<T> recipeType, List<ItemStack> inputs, Level level) {
        Optional<T> recipe = getRecipe(recipeType, inputs, level);
        if (!recipe.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(recipe.get().getOutputs());
    }

    /**
     * 根据配方类型和输入物品获取匹配的配方
     *
     * @param recipeType 配方类型
     * @param inputs 输入物品列表
     * @param level 世界等级对象
     * @return 匹配的配方，如果没有匹配的配方则返回空Optional
     */
    public static <T extends InputOutputRecipe> Optional<T> getRecipe(RecipeType<T> recipeType, List<ItemStack> inputs, Level level) {
        SimpleContainer container = new SimpleContainer(inputs.toArray(new ItemStack[0]));
        return level.getRecipeManager().getRecipeFor(recipeType, container, level);
    }

    /**
     * 检查输入是否匹配指定类型的配方要求
     *
     * @param recipeType 配方类型
     * @param inputs 输入物品列表
     * @param level 世界等级对象
     * @return 是否匹配
     */
    public static <T extends InputOutputRecipe> boolean matches(RecipeType<T> recipeType, List<ItemStack> inputs, Level level) {
        return getRecipe(recipeType, inputs, level).isPresent();
    }


    /**
     * 消耗输入物品到指定的ItemStackHandler槽位
     *
     * @param recipe 配方
     * @param itemStackHandler ItemStackHandler对象
     * @param inputSlotIndices 输入槽位索引列表
     * @return 消耗是否成功
     */
    public static <T extends InputOutputRecipe> boolean consumeInputs(T recipe, ItemStackHandler itemStackHandler, List<Integer> inputSlotIndices) {
        List<CountedIngredient> requiredInputs = recipe.getInputs();

        // 创建当前输入槽位物品的副本用于验证
        List<ItemStack> inputStacks = new ArrayList<>();
        for (Integer slotIndex : inputSlotIndices) {
            inputStacks.add(itemStackHandler.getStackInSlot(slotIndex).copy());
        }

        // 验证是否能满足所有输入需求
        for (CountedIngredient requiredIngredient : requiredInputs) {
            int remainingCount = requiredIngredient.count();
            boolean found = false;

            for (ItemStack inputStack : inputStacks) {
                if (requiredIngredient.test(inputStack)) {
                    int consumeCount = Math.min(inputStack.getCount(), remainingCount);
                    inputStack.shrink(consumeCount);
                    remainingCount -= consumeCount;

                    if (remainingCount <= 0) {
                        found = true;
                        break;
                    }
                }
            }

            if (!found || remainingCount > 0) {
                return false; // 无法满足需求
            }
        }

        // 实际消耗物品
        for (CountedIngredient requiredIngredient : requiredInputs) {
            int remainingCount = requiredIngredient.count();

            for (int i = 0; i < inputSlotIndices.size() && remainingCount > 0; i++) {
                int slotIndex = inputSlotIndices.get(i);
                ItemStack inputStack = itemStackHandler.getStackInSlot(slotIndex);

                if (requiredIngredient.test(inputStack)) {
                    int consumeCount = Math.min(inputStack.getCount(), remainingCount);
                    inputStack.shrink(consumeCount);
                    itemStackHandler.setStackInSlot(slotIndex, inputStack);
                    remainingCount -= consumeCount;
                }
            }
        }

        return true;
    }

    /**
     * 生成输出物品到指定的ItemStackHandler槽位
     *
     * @param outputs 输出物品列表
     * @param itemStackHandler ItemStackHandler对象
     * @param outputSlotIndices 输出槽位索引列表
     * @return 生成是否成功
     */
    public static boolean produceOutputs(List<ItemStack> outputs, ItemStackHandler itemStackHandler, List<Integer> outputSlotIndices) {
        // 获取当前输出槽位状态
        List<ItemStack> outputSlots = new ArrayList<>();
        for (Integer slotIndex : outputSlotIndices) {
            outputSlots.add(itemStackHandler.getStackInSlot(slotIndex));
        }

        // 检查是否能放入所有输出物品
        if (!canFitOutputs(outputs, outputSlots)) {
            return false;
        }

        // 放置每个输出物品
        for (ItemStack output : outputs) {
            ItemStack resultCopy = output.copy();

            // 获取输出槽位引用
            List<ItemStack> currentOutputSlots = new ArrayList<>();
            for (Integer slotIndex : outputSlotIndices) {
                currentOutputSlots.add(itemStackHandler.getStackInSlot(slotIndex));
            }

            // 首先尝试合并到已有堆栈
            for (int i = 0; i < currentOutputSlots.size(); i++) {
                ItemStack slot = currentOutputSlots.get(i);
                if (!slot.isEmpty() && ItemStack.isSameItemSameTags(slot, resultCopy)) {
                    int space = slot.getMaxStackSize() - slot.getCount();
                    if (space >= resultCopy.getCount()) {
                        slot.grow(resultCopy.getCount());
                        itemStackHandler.setStackInSlot(outputSlotIndices.get(i), slot);
                        resultCopy = ItemStack.EMPTY;
                        break;
                    } else if (space > 0) {
                        resultCopy.shrink(space);
                        slot.grow(space);
                        itemStackHandler.setStackInSlot(outputSlotIndices.get(i), slot);
                    }
                }
            }

            // 然后尝试放入空槽
            if (!resultCopy.isEmpty()) {
                for (int i = 0; i < currentOutputSlots.size(); i++) {
                    ItemStack slot = currentOutputSlots.get(i);
                    if (slot.isEmpty()) {
                        itemStackHandler.setStackInSlot(outputSlotIndices.get(i), resultCopy.copy());
                        resultCopy = ItemStack.EMPTY;
                        break;
                    }
                }
            }
        }

        return true;
    }

    /**
     * 检查输出是否可以放入指定的输出槽中
     *
     * @param outputs 输出物品列表
     * @param outputSlots 输出槽物品列表
     * @return 是否可以放入
     */
    public static boolean canFitOutputs(List<ItemStack> outputs, List<ItemStack> outputSlots) {
        // 复制输出槽用于模拟放置
        List<ItemStack> simulatedSlots = new ArrayList<>();
        for (ItemStack stack : outputSlots) {
            simulatedSlots.add(stack.copy());
        }

        // 尝试放置每个输出物品
        for (ItemStack output : outputs) {
            ItemStack resultCopy = output.copy();

            // 首先尝试合并到已有堆栈
            boolean placed = false;
            for (ItemStack slot : simulatedSlots) {
                if (!slot.isEmpty() && ItemStack.isSameItemSameTags(slot, resultCopy)) {
                    int space = slot.getMaxStackSize() - slot.getCount();
                    if (space >= resultCopy.getCount()) {
                        slot.grow(resultCopy.getCount());
                        placed = true;
                        break;
                    } else if (space > 0) {
                        resultCopy.shrink(space);
                        slot.grow(space);
                    }
                }
            }

            // 然后尝试放入空槽
            if (!placed) {
                for (int i = 0; i < simulatedSlots.size(); i++) {
                    if (simulatedSlots.get(i).isEmpty()) {
                        simulatedSlots.set(i, resultCopy.copy());
                        placed = true;
                        break;
                    }
                }
            }

            if (!placed) {
                return false;
            }
        }

        return true;
    }

}
