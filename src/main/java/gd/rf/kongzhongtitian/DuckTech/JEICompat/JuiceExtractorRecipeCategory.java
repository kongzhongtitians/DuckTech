package gd.rf.kongzhongtitian.DuckTech.JEICompat;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.juice_extractor.JuiceExtractorRecipe;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class JuiceExtractorRecipeCategory implements IRecipeCategory<JuiceExtractorRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "juice_extractor");

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "textures/gui/shredder.png");

    public static final ResourceLocation SLOT = ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "textures/gui/slot.png");

    public static final RecipeType<JuiceExtractorRecipe> RECIPE_TYPE =
            new RecipeType<>(UID, JuiceExtractorRecipe.class);

    private final IDrawableBuilder background;
    private final IDrawable icon;
    private final IDrawableBuilder slot;

    public JuiceExtractorRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(TEXTURE, 0, 0, 176, 94);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,  new ItemStack(DTBlocks.JUICE_EXTRACTOR.get()));
        this.slot = guiHelper.drawableBuilder(SLOT, 0, 0, 18, 18);

        background.setTextureSize(176,94);
        slot.setTextureSize(18,18);
    }

    @Override
    public RecipeType<JuiceExtractorRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.ducktech.juice_extractor");
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
    public void setRecipe(@NotNull IRecipeLayoutBuilder iRecipeLayoutBuilder, JuiceExtractorRecipe recipe, IFocusGroup iFocusGroup) {
        List<CountedIngredient> inputs = recipe.getInputs();
        List<ItemStack> outputs = recipe.getOutputs();

        int inputCount = Math.min(inputs.size(), 2);
        for (int i = 0; i < inputCount; i++) {
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 44 + i * 21, 34)
                    .addItemStack(inputs.get(i).createItemStack())
                    .setBackground(slot.build(), -1, -1);
        }
        if (inputCount == 1) {
            // 单输入配方时，第二个输入位留空占位
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 65, 34)
                    .addItemStack(Items.AIR.getDefaultInstance())
                    .setBackground(slot.build(), -1, -1);
        }

        // 单输出
        if (!outputs.isEmpty()) {
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 108, 34)
                    .addItemStack(outputs.get(0))
                    .setBackground(slot.build(), -1, -1);
        }
    }

    @Override
    public void draw(JuiceExtractorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        // 绘制文字
        Component text = Component.translatable("block.ducktech.juice_extractor");

        int processingTime = recipe.getProcessingTime();
        double s = (double) processingTime / 20;

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
                Component.translatable("jei.tooltip.time").append(": " + s +" s"),
                120,  // x坐标
                70,  // y坐标
                0x404040, // 颜色 (灰色)
                false // 是否有阴影
        );
    }
}
