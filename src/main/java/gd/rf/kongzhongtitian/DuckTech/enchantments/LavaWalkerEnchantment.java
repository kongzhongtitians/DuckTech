package gd.rf.kongzhongtitian.DuckTech.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class LavaWalkerEnchantment extends Enchantment {

    public LavaWalkerEnchantment() {
        // 稀有度: RARE，类别: ARMOR_FEET (靴子)，生效槽位: FEET
        super(Rarity.RARE, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
    }

    @Override
    public int getMaxLevel() {
        return 2; // 最大等级 II，等级越高，作用半径越大
    }

    @Override
    public int getMinCost(int level) {
        return 10 + level * 10; // 附魔台消耗经验等级
    }

    @Override
    public int getMaxCost(int level) {
        return this.getMinCost(level) + 15;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        // 与冰霜行者不兼容
        return super.checkCompatibility(other) && other != Enchantments.FROST_WALKER;
    }
}