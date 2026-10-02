package gd.rf.kongzhongtitian.DuckTech.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * 冷冻精华盔甲的统一材质。
 * 护甲值与耐久基准值固定在这里，实际数值由 FrozenEssenceArmorItem
 * 根据物品 NBT 标签 "Level"（1~5）动态决定。
 */
public class FrozenEssenceArmorMaterial implements ArmorMaterial {

    // 耐久基准，对应 靴/腿/胸/头
    public static final int[] DURABILITY = new int[]{65, 75, 80, 55};
    // 护甲基准（会被 NBT 等级覆盖），对应 靴/腿/胸/头
    public static final int[] DEFENSE = new int[]{1, 4, 5, 2};

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return DURABILITY[type.getSlot().getIndex()];
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return DEFENSE[type.getSlot().getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return 10;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_IRON;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(DTItems.FROZEN_ESSENCE.get());
    }

    @Override
    public String getName() {
        return "ducktech:frozen_essence";
    }

    @Override
    public float getToughness() {
        return 0.0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.0F;
    }
}
