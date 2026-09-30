package gd.rf.kongzhongtitian.DuckTech.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public abstract class BasicEssenceLevelTwo implements ArmorMaterial {
    // 基础耐久度数组，分别对应靴子、护腿、胸甲、头盔
    private static final int[] BASE_DURABILITY = new int[]{1, 1, 1, 1};
    // 护甲值数组，分别对应靴子、护腿、胸甲、头盔:cite[1]
    private static final int[] PROTECTION_VALUES = new int[]{2, 2, 2, 2};

    public int getDurabilityForSlot(EquipmentSlot slot) {
        return BASE_DURABILITY[slot.getIndex()] * 50; // 15是耐久系数，类似铁
    }
    public int getDefenseForSlot(EquipmentSlot slot) {
        return PROTECTION_VALUES[slot.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return 0; // 附魔能力，参考铁甲为9:cite[3]
    }
    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_IRON;
    }
    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(Items.AIR); // 修复材料
    }
    @Override
    public String getName() {
        // 重要：此处应返回你的模组ID加上材质名称
        return "ducktech:basic_essence";
    }
    @Override
    public float getToughness() {
        return 0.0F; // 盔甲韧性，钻石为2.0F
    }
    @Override
    public float getKnockbackResistance() {
        return 0.0F; // 击退抗性
    }
}