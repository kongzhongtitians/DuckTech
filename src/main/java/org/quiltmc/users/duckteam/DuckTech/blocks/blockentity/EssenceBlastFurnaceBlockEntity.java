package org.quiltmc.users.duckteam.DuckTech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.quiltmc.users.duckteam.DuckTech.DuckTech;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.config.DTConfig;
import org.quiltmc.users.duckteam.DuckTech.gui.essence_blast_furnace.EssenceBlastFurnaceMenu;
import org.quiltmc.users.duckteam.DuckTech.items.DTItems;
import org.quiltmc.users.duckteam.DuckTech.sounds.DTSounds;

public class EssenceBlastFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    private static final TagKey<Item> ORE_TAG =
            TagKey.create(Registries.ITEM, new ResourceLocation("forge", "ores"));

    private int prevCookTime = 0;

    public EssenceBlastFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.ESSENCE_BLAST_FURNACE_BLOCK_ENTITY.get(), pos, state, RecipeType.SMELTING);
        DuckTech.LOGGER.debug("EssenceBlastFurnaceBlockEntity created with RecipeType.SMELTING");
    }

    protected EssenceBlastFurnaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, RecipeType<? extends AbstractCookingRecipe> recipeType) {
        super(type, pos, state, recipeType);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state, EssenceBlastFurnaceBlockEntity furnace) {
        int oldCookTime = furnace.dataAccess.get(2);
        int oldOutputCount = furnace.getItem(2).isEmpty() ? 0 : furnace.getItem(2).getCount();

        AbstractFurnaceBlockEntity.serverTick(level, pos, state, furnace);

        int newCookTime = furnace.dataAccess.get(2);
        int newOutputCount = furnace.getItem(2).isEmpty() ? 0 : furnace.getItem(2).getCount();

        // 检测烧炼完成：旧进度 > 0，新进度 == 0，且输出增加
        if (oldCookTime > 0 && newCookTime == 0) {
            if (!level.isClientSide()&& DTConfig.switch_sound()) {
                level.playSound(null, pos,
                        DTSounds.ZAOYIN.get(),
                        SoundSource.BLOCKS,
                        1.0F,
                        1.0F);
            }
            int added = newOutputCount - oldOutputCount;
            if (added > 0) {
                ItemStack output = furnace.getItem(2);
                int maxStack = output.getMaxStackSize();
                int newCount = oldOutputCount + added * 2;
                if (newCount > maxStack) newCount = maxStack;
                output.setCount(newCount);
                furnace.setChanged();
            }
        }
    }

    public boolean isFuelItem(ItemStack stack) {
        return stack.is(DTItems.THERMAL_ESSENCE.get());
    }

    @Override
    protected int getBurnDuration(ItemStack stack) {
        int duration = isFuelItem(stack) ? 400 : 0;
        DuckTech.LOGGER.debug("getBurnDuration for {}: {}", stack, duration);
        return duration;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == 0) {
            return stack.is(ORE_TAG);
        } else if (slot == 1) {
            return isFuelItem(stack);
        }
        return false;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.ducktech.essence_blast_furnace");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new EssenceBlastFurnaceMenu(id, inv, this, this.dataAccess);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new EssenceBlastFurnaceMenu(id, inv, this, this.dataAccess);
    }

    public ContainerData getContainerData() {
        return this.dataAccess;
    }
}
