package gd.rf.kongzhongtitian.DuckTech.items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * 四种金属（锡/铝/铅/银）的工具材质（Tier）。
 * 数值参照原版木/石/铁/金档位，按金属特性分配耐久、挖掘速度、攻击加成与附魔等级。
 */
public class DTMetalTier implements Tier {
    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int level;
    private final int enchantmentValue;
    private final Ingredient repairIngredient;

    public DTMetalTier(int uses, float speed, float attackDamageBonus, int level,
                       int enchantmentValue, Ingredient repairIngredient) {
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.level = level;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamageBonus;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient;
    }

    // ---- 四个金属档位 ----
    public static final DTMetalTier TIN = new DTMetalTier(
            100, 4.0F, 1.0F, 1, 12,
            Ingredient.of(new ItemStack(DTItems.TIN_INGOT.get()))
    );

    public static final DTMetalTier ALUMINUM = new DTMetalTier(
            220, 5.0F, 1.0F, 2, 12,
            Ingredient.of(new ItemStack(DTItems.ALUMINUM_INGOT.get()))    );

    public static final DTMetalTier LEAD = new DTMetalTier(
            150, 3.5F, 1.0F, 1, 8,
            Ingredient.of(new ItemStack(DTItems.LEAD_INGOT.get()))    );

    public static final DTMetalTier SILVER = new DTMetalTier(
            280, 6.0F, 2.0F, 2, 20,
            Ingredient.of(new ItemStack(DTItems.SILVER_INGOT.get())));
}
