package gd.rf.kongzhongtitian.DuckTech.blocks.machines.injection_machine;

import com.google.gson.JsonObject;
import gd.rf.kongzhongtitian.DuckTech.api.recipes.InputOutputRecipeSerializer;
import gd.rf.kongzhongtitian.DuckTech.recipe.CountedIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class InjectionMachineRecipeSerializer extends InputOutputRecipeSerializer<InjectionMachineRecipe> {
    public static final InjectionMachineRecipeSerializer INSTANCE = new InjectionMachineRecipeSerializer();

    public InjectionMachineRecipeSerializer() {
        super(data -> new InjectionMachineRecipe(data.inputs, data.outputs, data.id, data.processingTime,
                InjectionMachineRecipe.TRANSFORM_NONE), 2, 1);
    }

    @Override
    public InjectionMachineRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
        List<CountedIngredient> inputs = readInputsFromJson(jsonObject);
        List<ItemStack> outputs = readOutputsFromJson(jsonObject);
        int processingTime = readProcessingTimeFromJson(jsonObject);
        int outputTransform = GsonHelper.getAsInt(jsonObject, "outputTransform", InjectionMachineRecipe.TRANSFORM_NONE);
        return new InjectionMachineRecipe(inputs, outputs, recipeId, processingTime, outputTransform);
    }

    @Override
    public InjectionMachineRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        List<CountedIngredient> inputs = readInputsFromNetwork(buffer);
        List<ItemStack> outputs = readOutputsFromNetwork(buffer);
        int processingTime = readProcessingTimeFromNetwork(buffer);
        int outputTransform = buffer.readVarInt();
        return new InjectionMachineRecipe(inputs, outputs, recipeId, processingTime, outputTransform);
    }
}
