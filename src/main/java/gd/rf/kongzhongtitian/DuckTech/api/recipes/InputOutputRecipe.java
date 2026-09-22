package gd.rf.kongzhongtitian.DuckTech.api.recipes;

import gd.rf.kongzhongtitian.DuckTech.recipe.CountedIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用输入输出配方基类，支持多个带数量的输入和多个输出
 *
 *
 */
public abstract class InputOutputRecipe implements Recipe<SimpleContainer> {
    protected final List<CountedIngredient> inputs;
    protected final List<ItemStack> outputs;
    protected final ResourceLocation id;
    protected final int maxInputs;
    protected final int maxOutputs;

    /**
     * 构造函数
     *
     * @param inputs    输入材料列表
     * @param outputs   输出物品列表
     * @param id        配方ID
     * @param maxInputs 最大输入数
     * @param maxOutputs 最大输出数
     */
    public InputOutputRecipe(List<CountedIngredient> inputs, List<ItemStack> outputs, ResourceLocation id,
                             int maxInputs, int maxOutputs) {
        this.maxInputs = maxInputs;
        this.maxOutputs = maxOutputs;

        // 限制输入数量
        this.inputs = new ArrayList<>();
        for (int i = 0; i < Math.min(inputs.size(), maxInputs); i++) {
            this.inputs.add(inputs.get(i));
        }

        // 限制输出数量
        this.outputs = new ArrayList<>();
        for (int i = 0; i < Math.min(outputs.size(), maxOutputs); i++) {
            this.outputs.add(outputs.get(i));
        }

        this.id = id;
    }


    // 无序匹配
    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) return false;

        if (container.getContainerSize() < inputs.size()) {
            return false;
        }

        List<CountedIngredient> requiredInputs = new ArrayList<>(this.inputs);
        List<ItemStack> containerItems = new ArrayList<>();

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                containerItems.add(stack);
            }
        }

        for (CountedIngredient requiredInput : requiredInputs) {
            boolean found = false;
            for (int i = 0; i < containerItems.size(); i++) {
                ItemStack containerItem = containerItems.get(i);
                if (requiredInput.test(containerItem)) {
                    containerItems.remove(i);
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        return true;
    }


    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= inputs.size();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0).copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (CountedIngredient input : inputs) {
            ingredients.add(input.ingredient());
        }
        return ingredients;
    }

    public List<CountedIngredient> getInputs() {
        return new ArrayList<>(inputs);
    }

    public List<ItemStack> getOutputs() {
        return new ArrayList<>(outputs);
    }

    // 网络序列化支持
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeVarInt(inputs.size());
        for (CountedIngredient input : inputs) {
            input.ingredient().toNetwork(buffer);
            buffer.writeVarInt(input.count());
        }

        buffer.writeVarInt(outputs.size());
        for (ItemStack stack : outputs) {
            buffer.writeItem(stack);
        }
    }

    public static List<CountedIngredient> readInputsFromNetwork(FriendlyByteBuf buffer) {
        int inputCount = buffer.readVarInt();
        List<CountedIngredient> inputs = new ArrayList<>();
        for (int i = 0; i < inputCount; i++) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int count = buffer.readVarInt();
            inputs.add(new CountedIngredient(ingredient, count));
        }
        return inputs;
    }

    public static List<ItemStack> readOutputsFromNetwork(FriendlyByteBuf buffer) {
        int outputCount = buffer.readVarInt();
        List<ItemStack> outputs = new ArrayList<>();
        for (int i = 0; i < outputCount; i++) {
            outputs.add(buffer.readItem());
        }
        return outputs;
    }
}
