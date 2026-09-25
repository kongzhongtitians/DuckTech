package gd.rf.kongzhongtitian.DuckTech.blocks.machines.juice_extractor;

import gd.rf.kongzhongtitian.DuckTech.api.recipes.InputOutputRecipe;
import gd.rf.kongzhongtitian.DuckTech.recipe.CountedIngredient;
import gd.rf.kongzhongtitian.DuckTech.recipe.DTRecipe;
import gd.rf.kongzhongtitian.DuckTech.recipe.DTRecipeSerializers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public class JuiceExtractorRecipe extends InputOutputRecipe {
    private final int processingTime;

    public JuiceExtractorRecipe(List<CountedIngredient> inputs, List<ItemStack> outputs,
                                ResourceLocation id, int processingTime) {
        super(inputs, outputs, id, 2, 3); // 最多2个输入，最多3个输出
        this.processingTime = processingTime;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DTRecipeSerializers.JUICE_EXTRACTOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return DTRecipe.JUICE_EXTRACTOR_RECIPE.get();
    }

    public int getProcessingTime() {
        return processingTime;
    }

    //特定于该配方类型的网络序列化方法
    public void toNetwork(FriendlyByteBuf buffer) {
        super.toNetwork(buffer);
        buffer.writeVarInt(processingTime);
    }

    public static JuiceExtractorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        List<CountedIngredient> inputs = readInputsFromNetwork(buffer);
        List<ItemStack> outputs = readOutputsFromNetwork(buffer);
        int processingTime = buffer.readVarInt();
        return new JuiceExtractorRecipe(inputs, outputs, id, processingTime);
    }
}

