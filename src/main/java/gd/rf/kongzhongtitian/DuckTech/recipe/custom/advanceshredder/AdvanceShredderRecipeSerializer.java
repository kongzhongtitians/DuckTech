package gd.rf.kongzhongtitian.DuckTech.recipe.custom.advanceshredder;

import com.google.gson.JsonObject;
import gd.rf.kongzhongtitian.DuckTech.api.recipes.InputOutputRecipeSerializer;
import net.minecraft.util.GsonHelper;

public class AdvanceShredderRecipeSerializer extends InputOutputRecipeSerializer<AdvanceShredderRecipe> {
    public static final AdvanceShredderRecipeSerializer INSTANCE = new AdvanceShredderRecipeSerializer();

    public AdvanceShredderRecipeSerializer() {
        super(data -> new AdvanceShredderRecipe(data.inputs, data.outputs, data.id, data.processingTime), 2, 3);
    }

    //如果需要自定义处理时间默认值，可以重写该方法
    @Override
    protected int readProcessingTimeFromJson(JsonObject jsonObject) {
        return GsonHelper.getAsInt(jsonObject, "processingTime", 20);
    }
}

