package gd.rf.kongzhongtitian.DuckTech.blocks.blockentity;

import gd.rf.kongzhongtitian.DuckTech.blocks.DTBlockEntity;
import gd.rf.kongzhongtitian.DuckTech.config.DTConfig;
import gd.rf.kongzhongtitian.DuckTech.recipe.CountedIngredient;
import gd.rf.kongzhongtitian.DuckTech.recipe.DTRecipe;
import gd.rf.kongzhongtitian.DuckTech.recipe.custom.shredder.ShredderRecipe;
import gd.rf.kongzhongtitian.DuckTech.sounds.DTSounds;
import gd.rf.kongzhongtitian.DuckTech.utils.RecipeOutputUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShredderBlockEntity extends BlockEntity {
    private int processingTime = 20;
    private int currentTime = 0;
    private boolean isProcessing = false;

    private ShredderRecipe currentRecipe;
    private List<ItemEntity> matchedEntities;

    public ShredderBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(DTBlockEntity.SHREDDER_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public void tick(Level level, BlockPos pos, BlockState state, ShredderBlockEntity entity) {
        if (level.isClientSide) return;

        if (isProcessing) {
            currentTime++;

            if (currentTime >= processingTime) {
                completeProcessing(level, pos);
                resetProcessing();
            }
            return;
        }

        checkAndStartProcessing(level, pos);
    }

    private void checkAndStartProcessing(Level level, BlockPos pos) {
        List<ItemEntity> itemEntities = getItemEntitiesAbove(level, pos);

        if (itemEntities.isEmpty()) return;

        Optional<ShredderRecipe> recipe = getRecipe(level, itemEntities);

        if (recipe.isPresent()) {
            if (!level.isClientSide()&& DTConfig.switch_sound()) {
                level.playSound(null, pos,
                        DTSounds.ZAOYIN.get(),
                        SoundSource.BLOCKS,
                        1.0F,
                        1.0F);
            }
            startProcessing(recipe.get(), itemEntities);
        }
    }

    private Optional<ShredderRecipe> getRecipe(Level level, List<ItemEntity> itemEntities) {
        if (level == null) return Optional.empty();

        List<ItemStack> itemStacks = itemEntities.stream()
                .map(ItemEntity::getItem)
                .collect(Collectors.toList());

        return RecipeOutputUtil.getRecipe(DTRecipe.SHREDDER_RECIPE.get(), itemStacks, level);
    }

    private void startProcessing(ShredderRecipe recipe, List<ItemEntity> entities) {
        isProcessing = true;
        currentTime = 0;
        currentRecipe = recipe;
        matchedEntities = entities;
    }

    private void completeProcessing(Level level, BlockPos pos) {
        if (currentRecipe == null || matchedEntities == null) return;

        consumeInputs(level, matchedEntities, currentRecipe);

        spawnOutputs(level, pos, currentRecipe);
    }

    private void consumeInputs(Level level, List<ItemEntity> entities, ShredderRecipe recipe) {
        List<CountedIngredient> inputs = recipe.getInputs();

        for (int i = 0; i < Math.min(inputs.size(), entities.size()); i++) {
            ItemEntity entity = entities.get(i);
            if (entity != null && entity.isAlive()) {
                ItemStack stack = entity.getItem();
                int countToConsume = inputs.get(i).count();
                stack.shrink(countToConsume);

                if (stack.isEmpty()) {
                    entity.discard();
                } else {
                    entity.setItem(stack);
                }
            }
        }
    }

    private void spawnOutputs(Level level, BlockPos pos, ShredderRecipe recipe) {
        List<ItemStack> outputs = recipe.getOutput();

        for (ItemStack output : outputs) {
            if (!output.isEmpty()) {
                BlockPos spawnPos = findSpawnPosition(level, pos);
                ItemEntity outputEntity = new ItemEntity(
                        level,
                        spawnPos.getX() + 0.5,
                        spawnPos.getY() + 0.5,
                        spawnPos.getZ() + 0.5,
                        output.copy()
                );

                outputEntity.setDeltaMovement(
                        (level.random.nextFloat() - 0.5) * 0.1,
                        0.2,
                        (level.random.nextFloat() - 0.5) * 0.1
                );

                level.addFreshEntity(outputEntity);
            }
        }
    }

    private BlockPos findSpawnPosition(Level level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        if (level.isEmptyBlock(belowPos)) {
            return belowPos;
        }

        BlockPos[] sidePositions = {
                pos.north(), pos.south(), pos.east(), pos.west()
        };

        for (BlockPos sidePos : sidePositions) {
            if (level.isEmptyBlock(sidePos)) {
                return sidePos;
            }
        }

        return pos.above();
    }

    private void resetProcessing() {
        isProcessing = false;
        currentTime = 0;
        currentRecipe = null;
        matchedEntities = null;
    }

    private static List<ItemEntity> getItemEntitiesAbove(Level level, BlockPos pos) {
        AABB detectionArea = new AABB(
                pos.getX(), pos.getY() + 1, pos.getZ(),
                pos.getX() + 1, pos.getY() + 1.2, pos.getZ() + 1
        );
        return level.getEntitiesOfClass(ItemEntity.class, detectionArea);
    }
}
