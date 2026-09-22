package gd.rf.kongzhongtitian.DuckTech.api.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gd.rf.kongzhongtitian.DuckTech.recipe.CountedIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 通用输入输出配方序列化器基类
 * @param <T> 配方类型
 */
public abstract class InputOutputRecipeSerializer<T extends InputOutputRecipe> implements RecipeSerializer<T> {

    /**
     * 从JSON创建配方的函数接口
     */
    protected final Function<RecipeJsonData, T> recipeFactory;

    /**
     * 输入数量限制
     */
    protected final int maxInputs;

    /**
     * 输出数量限制
     */
    protected final int maxOutputs;

    public InputOutputRecipeSerializer(Function<RecipeJsonData, T> recipeFactory, int maxInputs, int maxOutputs) {
        this.recipeFactory = recipeFactory;
        this.maxInputs = maxInputs;
        this.maxOutputs = maxOutputs;
    }

    @Override
    public T fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
        List<CountedIngredient> inputs = readInputsFromJson(jsonObject);
        List<ItemStack> outputs = readOutputsFromJson(jsonObject);
        int processingTime = readProcessingTimeFromJson(jsonObject);

        RecipeJsonData data = new RecipeJsonData(recipeId, inputs, outputs, processingTime);
        return recipeFactory.apply(data);
    }

    @Override
    @Nullable
    public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        List<CountedIngredient> inputs = readInputsFromNetwork(buffer);
        List<ItemStack> outputs = readOutputsFromNetwork(buffer);
        int processingTime = readProcessingTimeFromNetwork(buffer);

        RecipeJsonData data = new RecipeJsonData(recipeId, inputs, outputs, processingTime);
        return recipeFactory.apply(data);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        recipe.toNetwork(buffer);
    }

    /**
     * 从JSON读取输入材料
     */
    protected List<CountedIngredient> readInputsFromJson(JsonObject jsonObject) {
        List<CountedIngredient> inputs = new ArrayList<>();
        if (jsonObject.has("input")) {
            JsonElement inputElement = jsonObject.get("input");
            if (inputElement.isJsonObject()) {
                inputs.add(CountedIngredient.fromJson(inputElement.getAsJsonObject()));
            } else if (inputElement.isJsonArray()) {
                JsonArray inputsArray = inputElement.getAsJsonArray();
                int maxInputs = Math.min(inputsArray.size(), this.maxInputs);
                for (int i = 0; i < maxInputs; i++) {
                    inputs.add(CountedIngredient.fromJson(inputsArray.get(i).getAsJsonObject()));
                }
            }
        }
        return inputs;
    }

    /**
     * 从JSON读取输出物品
     */
    protected List<ItemStack> readOutputsFromJson(JsonObject jsonObject) {
        List<ItemStack> outputs = new ArrayList<>();
        if (jsonObject.has("output")) {
            JsonArray outputsArray = GsonHelper.getAsJsonArray(jsonObject, "output");
            int maxOutputs = Math.min(outputsArray.size(), this.maxOutputs);
            for (int i = 0; i < maxOutputs; i++) {
                JsonElement element = outputsArray.get(i);
                outputs.add(ShapedRecipe.itemStackFromJson(element.getAsJsonObject()));
            }
        }
        return outputs;
    }

    /**
     * 从JSON读取处理时间，默认值为20
     */
    protected int readProcessingTimeFromJson(JsonObject jsonObject) {
        return GsonHelper.getAsInt(jsonObject, "processingTime", 20);
    }

    /**
     * 从网络缓冲区读取输入材料
     */
    protected List<CountedIngredient> readInputsFromNetwork(FriendlyByteBuf buffer) {
        int inputCount = buffer.readVarInt();
        List<CountedIngredient> inputs = new ArrayList<>();
        for (int i = 0; i < inputCount; i++) {
            net.minecraft.world.item.crafting.Ingredient ingredient =
                    net.minecraft.world.item.crafting.Ingredient.fromNetwork(buffer);
            int count = buffer.readVarInt();
            inputs.add(new CountedIngredient(ingredient, count));
        }
        return inputs;
    }

    /**
     * 从网络缓冲区读取输出物品
     */
    protected List<ItemStack> readOutputsFromNetwork(FriendlyByteBuf buffer) {
        int outputCount = buffer.readVarInt();
        List<ItemStack> outputs = new ArrayList<>();
        for (int i = 0; i < outputCount; i++) {
            outputs.add(buffer.readItem());
        }
        return outputs;
    }

    /**
     * 从网络缓冲区读取处理时间
     */
    protected int readProcessingTimeFromNetwork(FriendlyByteBuf buffer) {
        return buffer.readVarInt();
    }

    /**
     * 配方JSON数据载体类
     */
    public static class RecipeJsonData {
        public final ResourceLocation id;
        public final List<CountedIngredient> inputs;
        public final List<ItemStack> outputs;
        public final int processingTime;

        public RecipeJsonData(ResourceLocation id, List<CountedIngredient> inputs,
                              List<ItemStack> outputs, int processingTime) {
            this.id = id;
            this.inputs = inputs;
            this.outputs = outputs;
            this.processingTime = processingTime;
        }
    }
}
