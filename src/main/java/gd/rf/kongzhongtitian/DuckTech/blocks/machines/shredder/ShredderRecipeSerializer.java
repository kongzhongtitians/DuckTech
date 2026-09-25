package gd.rf.kongzhongtitian.DuckTech.blocks.machines.shredder;

import com.google.gson.JsonObject;
import gd.rf.kongzhongtitian.DuckTech.api.recipes.InputOutputRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;

public class ShredderRecipeSerializer extends InputOutputRecipeSerializer<ShredderRecipe> {
    public static final ShredderRecipeSerializer INSTANCE = new ShredderRecipeSerializer();

    public ShredderRecipeSerializer() {
        super(data -> new ShredderRecipe(data.inputs, data.outputs, data.id), 2, 3);
    }

    //粉碎机配方不需要处理时间，使用默认值20
    @Override
    protected int readProcessingTimeFromJson(JsonObject jsonObject) {
        return 20;
    }

    @Override
    protected int readProcessingTimeFromNetwork(FriendlyByteBuf buffer) {
        return 20;
    }
}
