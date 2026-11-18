package gd.rf.kongzhongtitian.DuckTech.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * 通用金属盔甲材质。耐久/护甲按 靴/腿/胸/头 槽位显式给出，
 * 修复原料为对应金属锭。
 */
public class MetalArmorMaterial implements ArmorMaterial {

    private final String name;
    private final int[] durability;      // [boots, leggings, chestplate, helmet]
    private final int[] defense;         // [boots, leggings, chestplate, helmet]
    private final int enchantmentValue;
    private final float toughness;
    private final float knockbackResistance;
    private final Ingredient repairIngredient;

    public MetalArmorMaterial(String name, int[] durability, int[] defense, int enchantmentValue,
                              float toughness, float knockbackResistance, Ingredient repairIngredient) {
        this.name = name;
        this.durability = durability;
        this.defense = defense;
        this.enchantmentValue = enchantmentValue;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return durability[type.getSlot().getIndex()];
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return defense[type.getSlot().getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_IRON;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient;
    }

    @Override
    public String getName() {
        return "ducktech:" + name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }

    // ---- 四个金属盔甲档位 ----

    public static final MetalArmorMaterial TIN = new MetalArmorMaterial(
            "tin",
            new int[]{5, 7, 8, 4},
            new int[]{1, 2, 3, 1},
            12, 0.0F, 0.0F,
            Ingredient.of(DTItems.TIN_INGOT.get()));

    public static final MetalArmorMaterial ALUMINUM = new MetalArmorMaterial(
            "aluminum",
            new int[]{8, 10, 11, 7},
            new int[]{2, 3, 5, 2},
            12, 0.0F, 0.0F,
            Ingredient.of(DTItems.ALUMINUM_INGOT.get()));

    public static final MetalArmorMaterial LEAD = new MetalArmorMaterial(
            "lead",
            new int[]{6, 8, 9, 5},
            new int[]{1, 3, 4, 1},
            8, 0.0F, 0.0F,
            Ingredient.of(DTItems.LEAD_INGOT.get()));

    public static final MetalArmorMaterial SILVER = new MetalArmorMaterial(
            "silver",
            new int[]{9, 11, 12, 8},
            new int[]{2, 4, 5, 2},
            20, 0.25F, 0.0F,
            Ingredient.of(DTItems.SILVER_INGOT.get()));
}
