package gd.rf.kongzhongtitian.DuckTech.recipe;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import gd.rf.kongzhongtitian.DuckTech.recipe.custom.advanceshredder.AdvanceShredderRecipe;
import gd.rf.kongzhongtitian.DuckTech.recipe.custom.advanceshredder.AdvanceShredderRecipeSerializer;
import gd.rf.kongzhongtitian.DuckTech.recipe.custom.essence_conversion_machine.EssenceConversionMachineRecipe;
import gd.rf.kongzhongtitian.DuckTech.recipe.custom.essence_conversion_machine.EssenceConversionMachineRecipeSerializer;
import gd.rf.kongzhongtitian.DuckTech.recipe.custom.injection_machine.InjectionMachineRecipe;
import gd.rf.kongzhongtitian.DuckTech.recipe.custom.injection_machine.InjectionMachineRecipeSerializer;
import gd.rf.kongzhongtitian.DuckTech.recipe.custom.juice_extractor.JuiceExtractorRecipe;
import gd.rf.kongzhongtitian.DuckTech.recipe.custom.juice_extractor.JuiceExtractorRecipeSerializer;
import gd.rf.kongzhongtitian.DuckTech.recipe.custom.shredder.ShredderRecipe;
import gd.rf.kongzhongtitian.DuckTech.recipe.custom.shredder.ShredderRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DTRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DuckTech.MODID);

    public static final RegistryObject<RecipeSerializer<ShredderRecipe>> SHREDDER_SERIALIZER =
            SERIALIZERS.register("shredder", () -> ShredderRecipeSerializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<AdvanceShredderRecipe>> ADVANCE_SHREDDER_SERIALIZER =
            SERIALIZERS.register("advance_shredder", () -> AdvanceShredderRecipeSerializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<EssenceConversionMachineRecipe>> ESSENCE_CONVERSION_MACHINE_SERIALIZER =
            SERIALIZERS.register("essence_conversion_machine", () -> EssenceConversionMachineRecipeSerializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<InjectionMachineRecipe>> INJECTION_MACHINE_SERIALIZER =
            SERIALIZERS.register("injection_machine", () -> InjectionMachineRecipeSerializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<JuiceExtractorRecipe>> JUICE_EXTRACTOR_SERIALIZER =
            SERIALIZERS.register("juice_extractor", () -> JuiceExtractorRecipeSerializer.INSTANCE);
}
