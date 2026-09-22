package gd.rf.kongzhongtitian.DuckTech.recipe;


import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

//这是一个支持指定数量的容器
public record CountedIngredient(Ingredient ingredient, int count) {

    public boolean test(ItemStack stack) {
        return ingredient.test(stack) && stack.getCount() >= count;
    }

    public static CountedIngredient fromJson(JsonObject json) {
        Ingredient ingredient = Ingredient.fromJson(json);
        int count = GsonHelper.getAsInt(json, "count", 1);
        return new CountedIngredient(ingredient, count);
    }

    public JsonObject toJson() {
        JsonObject json = ingredient.toJson().getAsJsonObject();
        if (count > 1) {
            json.addProperty("count", count);
        }
        return json;
    }


    // 添加创建 ItemStack 的方法
    public ItemStack createItemStack() {
        ItemStack[] matchingStacks = ingredient.getItems();
        if (matchingStacks.length > 0) {
            ItemStack stack = matchingStacks[0].copy();
            stack.setCount(count);
            return stack;
        }
        return ItemStack.EMPTY;
    }
}

