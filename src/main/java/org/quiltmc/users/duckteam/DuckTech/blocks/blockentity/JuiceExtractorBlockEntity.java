package org.quiltmc.users.duckteam.DuckTech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.users.duckteam.DuckTech.api.block.DTBaseProcessingBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.api.recipes.InputOutputRecipe;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.config.DTConfig;
import org.quiltmc.users.duckteam.DuckTech.gui.juice_extractor.JuiceExtractorMenu;
import org.quiltmc.users.duckteam.DuckTech.recipe.DTRecipe;
import org.quiltmc.users.duckteam.DuckTech.recipe.custom.juice_extractor.JuiceExtractorRecipe;
import org.quiltmc.users.duckteam.DuckTech.sounds.DTSounds;
import org.quiltmc.users.duckteam.DuckTech.utils.RecipeOutputUtil;

import java.util.List;
import java.util.Optional;

public class JuiceExtractorBlockEntity extends DTBaseProcessingBlockEntity implements MenuProvider {

    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int OUTPUT_SLOT_1 = 2;
    public static final int OUTPUT_SLOT_2 = 3;
    public static final int OUTPUT_SLOT_3 = 4;

    public JuiceExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.JUICE_EXTRACTOR_BLOCK_ENTITY.get(), pos, state);
        this.setItemStackHandler(5);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ducktech.juice_extractor");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new JuiceExtractorMenu(containerId, inventory, this, this.data);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        Optional<JuiceExtractorRecipe> advanceRecipe = getRecipe(DTRecipe.JUICE_EXTRACTOR_RECIPE.get());

        if (advanceRecipe.isPresent() && hasRecipe(DTRecipe.JUICE_EXTRACTOR_RECIPE.get())) {
            this.maxProgress = advanceRecipe.get().getProcessingTime() > 0 ?
                    advanceRecipe.get().getProcessingTime() : 20;
            this.data.set(1, this.maxProgress);

            progress++;
            this.data.set(0, this.progress);
            setChanged();

            if (progress >= maxProgress) {
                if (!level.isClientSide()&& DTConfig.switch_sound()) {
                    level.playSound(null, pos,
                            DTSounds.ZAOYIN.get(),
                            SoundSource.BLOCKS,
                            1.0F,
                            1.0F);
                }

                craftItem(advanceRecipe.get());
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private <T extends InputOutputRecipe> void craftItem(T recipe){

        RecipeOutputUtil.consumeInputs(recipe, itemStackHandler, List.of(0,1));

        RecipeOutputUtil.produceOutputs(recipe.getOutputs(), itemStackHandler, List.of(2,3,4));

    }


    private void resetProgress() {
        progress = 0;
        maxProgress = 20;
        this.data.set(0, progress);
        this.data.set(1, maxProgress);
        setChanged();
    }

    private <T extends InputOutputRecipe> boolean hasRecipe(RecipeType<T> recipe) {
        if (recipe == null) return false;

        Optional<List<ItemStack>> outputs1 = RecipeOutputUtil.getOutputs(recipe, getSlotsItemStack(), level);

        return outputs1.filter(itemStacks -> RecipeOutputUtil.canFitOutputs(itemStacks, getSlotsOutputItemStack())).isPresent();

    }


    private  <T extends InputOutputRecipe> Optional<T> getRecipe(RecipeType<T> recipeType) {
        if (level == null) return Optional.empty();

        return RecipeOutputUtil.getRecipe(recipeType, getSlotsItemStack(), level);
    }

    protected List<ItemStack> getSlotsItemStack(){
        return List.of(
                itemStackHandler.getStackInSlot(INPUT_SLOT_1),
                itemStackHandler.getStackInSlot(INPUT_SLOT_2)
        );
    }

    protected List<ItemStack> getSlotsOutputItemStack(){
        return List.of(
                itemStackHandler.getStackInSlot(OUTPUT_SLOT_1),
                itemStackHandler.getStackInSlot(OUTPUT_SLOT_2),
                itemStackHandler.getStackInSlot(OUTPUT_SLOT_3)

        );
    }
}
