package gd.rf.kongzhongtitian.DuckTech.recipe.custom.shredder;

import gd.rf.kongzhongtitian.DuckTech.api.recipes.InputOutputRecipe;
import gd.rf.kongzhongtitian.DuckTech.recipe.CountedIngredient;
import gd.rf.kongzhongtitian.DuckTech.recipe.DTRecipe;
import gd.rf.kongzhongtitian.DuckTech.recipe.DTRecipeSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public class ShredderRecipe extends InputOutputRecipe {
    public ShredderRecipe(List<CountedIngredient> inputs, List<ItemStack> outputs, ResourceLocation id) {
        super(inputs, outputs, id, 2, 3); // 最多2个输入，最多3个输出
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DTRecipeSerializers.SHREDDER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return DTRecipe.SHREDDER_RECIPE.get();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public List<ItemStack> getOutput() {
        return getOutputs();
    }

    public static ShredderRecipe fromNetwork(ResourceLocation id, net.minecraft.network.FriendlyByteBuf buffer) {
        List<CountedIngredient> inputs = readInputsFromNetwork(buffer);
        List<ItemStack> outputs = readOutputsFromNetwork(buffer);
        return new ShredderRecipe(inputs, outputs, id);
    }
}
