package gd.rf.kongzhongtitian.DuckTech.JEICompat;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.shredder.ShredderRecipe;
import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlocks;
import gd.rf.kongzhongtitian.DuckTech.recipe.CountedIngredient;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class ShredderRecipeCategory implements IRecipeCategory<ShredderRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "shredder");

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "textures/gui/shredder.png");

    public static final ResourceLocation SLOT = ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "textures/gui/slot.png");

    public static final RecipeType<ShredderRecipe> RECIPE_TYPE =
            new RecipeType<>(UID, ShredderRecipe.class);

    private final IDrawableBuilder background;
    private final IDrawable icon;
    private final IDrawableBuilder slot;

    public ShredderRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(TEXTURE, 0, 0, 176, 94);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,  new ItemStack(DTBlocks.SHREDDER.get()));
        this.slot = guiHelper.drawableBuilder(SLOT, 0, 0, 18, 18);

        background.setTextureSize(176,94);
        slot.setTextureSize(18,18);
    }

    @Override
    public RecipeType<ShredderRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.ducktech.shredder");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    @SuppressWarnings("all")
    public @Nullable IDrawable getBackground() {
        return background.build();
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder iRecipeLayoutBuilder, ShredderRecipe shredderRecipe, IFocusGroup iFocusGroup) {
        NonNullList<Ingredient> ingredients = shredderRecipe.getIngredients();
        List<CountedIngredient> inputs = shredderRecipe.getInputs();

        if (ingredients.size() == 1){
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 44,34).addItemStack(inputs.get(0).createItemStack()).setBackground(slot.build(),-1,-1);
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 65,34).addItemStack(Items.AIR.getDefaultInstance()).setBackground(slot.build(),-1,-1);
        }else if (ingredients.size() == 2){
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 44,34).addItemStack(inputs.get(0).createItemStack()).setBackground(slot.build(),-1,-1);
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 65,34).addItemStack(inputs.get(1).createItemStack()).setBackground(slot.build(),-1,-1);
        }



        // 获取所有输出
        List<ItemStack> outputs = shredderRecipe.getOutput();


        // 根据输出数量动态布局
        if (outputs.size() == 1) {
            // 单个输出 - 居中显示
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 108, 34).setBackground(slot.build(),-1,-1)
                    .addItemStack(outputs.get(0));

        } else if (outputs.size() == 2) {
            // 两个输出 - 上下排列
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 108, 25).setBackground(slot.build(),-1,-1)
                    .addItemStack(outputs.get(0));
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 128, 25).setBackground(slot.build(),-1,-1)
                    .addItemStack(outputs.get(1));

        } else if (outputs.size() == 3) {
            // 三个输出 - 三角形排列
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 108, 25).setBackground(slot.build(),-1,-1)  // 上
                    .addItemStack(outputs.get(0));
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 98, 43) .setBackground(slot.build(),-1,-1)  // 左下
                    .addItemStack(outputs.get(1));
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 118, 43) .setBackground(slot.build(),-1,-1) // 右下
                    .addItemStack(outputs.get(2));

        }
        }

    @Override
    public void draw(ShredderRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        // 绘制文字
        Component text = Component.translatable("block.ducktech.shredder");
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                text,
                8,  // x坐标
                6,  // y坐标
                0x404040, // 颜色 (灰色)
                false // 是否有阴影
        );
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                Component.translatable("jei.tooltip.time").append(": 1 s"),
                120,  // x坐标
                70,  // y坐标
                0x404040, // 颜色 (灰色)
                false
        );
    }
}
