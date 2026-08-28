package org.quiltmc.users.duckteam.DuckTech.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.quiltmc.users.duckteam.DuckTech.DuckTech;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlocks;
import org.quiltmc.users.duckteam.DuckTech.recipe.DTRecipe;
import org.quiltmc.users.duckteam.DuckTech.recipe.custom.essence_conversion_machine.EssenceConversionMachineRecipe;

import java.util.List;

@JeiPlugin
public class EssenceConversionMachineRecipeJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new EssenceConversionMachineRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<EssenceConversionMachineRecipe> allRecipesFor = recipeManager.getAllRecipesFor(DTRecipe.ESSENCE_CONVERSION_MACHINE_RECIPE.get());

        registration.addRecipes(EssenceConversionMachineRecipeCategory.RECIPE_TYPE, allRecipesFor);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(DTBlocks.ESSENCE_CONVERSION_MACHINE.get()) , EssenceConversionMachineRecipeCategory.RECIPE_TYPE);
    }
}
