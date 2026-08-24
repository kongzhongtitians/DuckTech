package org.quiltmc.users.duckteam.DuckTech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.quiltmc.users.duckteam.DuckTech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.DuckTech.gui.essence_earth_furnace.EssenceEarthFurnaceMenu;
import org.quiltmc.users.duckteam.DuckTech.items.DTItems;

public class EssenceEarthFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    private static final TagKey<Item> ORE_TAG =
            TagKey.create(Registries.ITEM, new ResourceLocation("forge", "ores"));

    public EssenceEarthFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.ESSENCE_EARTH_FURNACE_BLOCK_ENTITY.get(), pos, state, RecipeType.SMELTING);
    }

    // 自定义燃料判断（供菜单槽使用）
    public boolean isFuelItem(ItemStack stack) {
        return stack.is(DTItems.THERMAL_ESSENCE.get());
    }

    // 覆写燃烧时间
    @Override
    protected int getBurnDuration(ItemStack stack) {
        return isFuelItem(stack) ? 400 : 0;
    }

    // 输入槽限制
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == 0) {
            return !stack.is(ORE_TAG);
        } else if (slot == 1) {
            return isFuelItem(stack);
        } else {
            return false;
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.ducktech.essence_earth_furnace");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new EssenceEarthFurnaceMenu(id, inv, this, this.dataAccess);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new EssenceEarthFurnaceMenu(id, inv, this, this.dataAccess);
    }

    public ContainerData getContainerData() {
        return this.dataAccess;
    }
}
