package gd.rf.kongzhongtitian.DuckTech.blocks.machines.injection_machine;

import gd.rf.kongzhongtitian.DuckTech.api.block.DTBaseProcessingBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.api.recipes.InputOutputRecipe;
import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.config.DTConfig;
import gd.rf.kongzhongtitian.DuckTech.items.BasicEssenceArmorItem;
import gd.rf.kongzhongtitian.DuckTech.items.FrozenEssenceArmorItem;
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

public class InjectionMachineBlockEntity extends DTBaseProcessingBlockEntity implements MenuProvider {
    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int OUTPUT_SLOT = 2;

    public InjectionMachineBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.INJECTION_MACHINE_BLOCK_ENTITY.get(), pos, state);
        this.setItemStackHandler(5);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ducktech.injection_machine");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new InjectionMachineMenu(containerId, inventory, this, this.data);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        Optional<InjectionMachineRecipe> recipe = getRecipe(DTRecipe.INJECTION_MACHINE_RECIPE.get());

        if (recipe.isPresent() && hasRecipe(DTRecipe.INJECTION_MACHINE_RECIPE.get())) {
            this.maxProgress = recipe.get().getProcessingTime() > 0 ?
                    recipe.get().getProcessingTime() : 20;
            this.data.set(1, this.maxProgress);

            progress++;
            this.data.set(0, this.progress);
            setChanged();

            if (progress >= maxProgress) {
                craftItem(recipe.get());
                resetProgress();
                if (!level.isClientSide()&& DTConfig.switch_sound()) {
                    level.playSound(null, pos,
                            DTSounds.ZAOYIN.get(),
                            SoundSource.BLOCKS,
                            1.0F,
                            1.0F);
                }
            }
        } else {
            resetProgress();
        }
    }

    private void craftItem(InjectionMachineRecipe recipe) {
        List<ItemStack> outputs = resolveOutputs(recipe);
        if (outputs.isEmpty()) {
            return;
        }
        if (!RecipeOutputUtil.consumeInputs(recipe, itemStackHandler, List.of(INPUT_SLOT_1, INPUT_SLOT_2))) {
            return;
        }
        RecipeOutputUtil.produceOutputs(outputs, itemStackHandler, List.of(OUTPUT_SLOT));
    }

    /**
     * 计算实际产物。
     * 升级配方（outputTransform = 精华盔甲升级）：产物 = 输入精华盔甲的副本，等级 +1 且耐久回满（继承附魔/名称等 NBT）；
     * 普通配方：直接使用 JSON 中声明的静态输出。
     */
    private List<ItemStack> resolveOutputs(InjectionMachineRecipe recipe) {
        if (recipe.getOutputTransform() == InjectionMachineRecipe.TRANSFORM_ARMOR_LEVEL_UP) {
            ItemStack armor = findUpgradableArmorInInputs();
            if (armor.isEmpty()) {
                return List.of();
            }
            ItemStack upgraded = armor.copy();
            if (upgraded.getItem() instanceof BasicEssenceArmorItem) {
                BasicEssenceArmorItem.setLevel(upgraded, BasicEssenceArmorItem.getLevel(upgraded) + 1);
            } else if (upgraded.getItem() instanceof FrozenEssenceArmorItem) {
                FrozenEssenceArmorItem.setLevel(upgraded, FrozenEssenceArmorItem.getLevel(upgraded) + 1);
            } else {
                return List.of();
            }
            upgraded.setDamageValue(0);
            return List.of(upgraded);
        }
        return recipe.getOutputs();
    }

    /** 在输入槽中查找任意一件可升级的精华盔甲（基础或冷冻）。 */
    private ItemStack findUpgradableArmorInInputs() {
        for (int slot : new int[]{INPUT_SLOT_1, INPUT_SLOT_2}) {
            ItemStack stack = itemStackHandler.getStackInSlot(slot);
            if (!stack.isEmpty() && (stack.getItem() instanceof BasicEssenceArmorItem
                    || stack.getItem() instanceof FrozenEssenceArmorItem)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 20;
        this.data.set(0, progress);
        this.data.set(1, maxProgress);
        setChanged();
    }

    private <T extends InputOutputRecipe> boolean hasRecipe(RecipeType<T> recipeType) {
        if (recipeType == null) return false;

        Optional<List<ItemStack>> outputs = RecipeOutputUtil.getOutputs(recipeType, getSlotsItemStack(), level);
        if (outputs.isEmpty()) return false;

        return RecipeOutputUtil.canFitOutputs(outputs.get(), getSlotsOutputItemStack());
    }

    private <T extends InputOutputRecipe> Optional<T> getRecipe(RecipeType<T> recipeType) {
        if (level == null) return Optional.empty();
        return RecipeOutputUtil.getRecipe(recipeType, getSlotsItemStack(), level);
    }

    protected List<ItemStack> getSlotsItemStack() {
        return List.of(
                itemStackHandler.getStackInSlot(INPUT_SLOT_1),
                itemStackHandler.getStackInSlot(INPUT_SLOT_2)
        );
    }

    protected List<ItemStack> getSlotsOutputItemStack() {
        return List.of(
                itemStackHandler.getStackInSlot(OUTPUT_SLOT)
        );
    }
}
