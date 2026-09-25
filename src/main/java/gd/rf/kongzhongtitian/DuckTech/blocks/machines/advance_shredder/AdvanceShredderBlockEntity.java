package gd.rf.kongzhongtitian.DuckTech.blocks.machines.advance_shredder;

import gd.rf.kongzhongtitian.DuckTech.api.block.DTBaseProcessingBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.api.recipes.InputOutputRecipe;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.shredder.ShredderRecipe;
import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.config.DTConfig;
import gd.rf.kongzhongtitian.DuckTech.recipe.DTRecipe;
import gd.rf.kongzhongtitian.DuckTech.sounds.DTSounds;
import gd.rf.kongzhongtitian.DuckTech.utils.RecipeOutputUtil;
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

import java.util.List;
import java.util.Optional;

public class AdvanceShredderBlockEntity extends DTBaseProcessingBlockEntity implements MenuProvider {

    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int OUTPUT_SLOT_1 = 2;
    public static final int OUTPUT_SLOT_2 = 3;
    public static final int OUTPUT_SLOT_3 = 4;

    public AdvanceShredderBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.ADVANCE_SHREDDER_BLOCK_ENTITY.get(), pos, state);
        this.setItemStackHandler(5);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ducktech.advance_shredder");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new AdvanceShredderMenu(containerId, inventory, this, this.data);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        Optional<AdvanceShredderRecipe> advanceRecipe = getRecipe(DTRecipe.ADVANCE_SHREDDER_RECIPE.get());
        Optional<ShredderRecipe> basicRecipe = advanceRecipe.isPresent() ? Optional.empty() : getRecipe(DTRecipe.SHREDDER_RECIPE.get());

        if (advanceRecipe.isPresent() && hasRecipe(DTRecipe.ADVANCE_SHREDDER_RECIPE.get())) {
            this.maxProgress = advanceRecipe.get().getProcessingTime() > 0 ?
                    advanceRecipe.get().getProcessingTime() : 20;
            this.data.set(1, this.maxProgress);

            progress++;
            this.data.set(0, this.progress);
            setChanged();

            if (progress >= maxProgress) {
                craftItem(advanceRecipe.get());
                resetProgress();
            }
        } else if (basicRecipe.isPresent() && hasRecipe(DTRecipe.SHREDDER_RECIPE.get())) {
            // 基础配方统一使用20 ticks处理时间
            this.maxProgress = 20;
            this.data.set(1, this.maxProgress);

            progress++;
            this.data.set(0, this.progress);
            setChanged();

            if (progress >= maxProgress) {
                craftItem(basicRecipe.get());
                if (!level.isClientSide()&& DTConfig.switch_sound()) {
                    level.playSound(null, pos,
                            DTSounds.ZAOYIN.get(),
                            SoundSource.BLOCKS,
                            1.0F,
                            1.0F);
                }
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
