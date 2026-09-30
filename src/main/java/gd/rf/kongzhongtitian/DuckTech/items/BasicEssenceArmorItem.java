package gd.rf.kongzhongtitian.DuckTech.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

/**
 * 基础精华盔甲。等级通过物品 NBT 标签 "Level"（1~3）判定，不区分物品 id。
 * - 每件护甲值 = 等级值（1 级 1 点、2 级 2 点、3 级 3 点）
 * - 耐久度 = 50 × 等级（1 级 50、2 级 100、3 级 150）
 * 未设置标签时默认按 1 级处理。
 */
public class BasicEssenceArmorItem extends ArmorItem {

    public static final String LEVEL_TAG = "Level";
    public static final int MAX_LEVEL = 3;
    private static final int BASE_DURABILITY = 50;

    // 靴/腿/胸/头 的护甲属性修饰符 UUID（与原版标准值一致）
    private static final java.util.UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new java.util.UUID[]{
            java.util.UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
            java.util.UUID.fromString("D8499B04-0E66-4726-AB29-64469D593E6E"),
            java.util.UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
            java.util.UUID.fromString("2AD3F246-FEE1-4E67-B886-69FF380E1F22")
    };

    public BasicEssenceArmorItem(ArmorMaterial material, ArmorItem.Type type, Item.Properties properties) {
        super(material, type, properties);
    }

    /** 读取物品的等级，越界时夹到 1~3，未设置返回 1。 */
    public static int getLevel(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(LEVEL_TAG, CompoundTag.TAG_INT)) {
            return Math.max(1, Math.min(MAX_LEVEL, tag.getInt(LEVEL_TAG)));
        }
        return 1;
    }

    /** 设置物品等级（夹到 1~3）。 */
    public static void setLevel(ItemStack stack, int level) {
        stack.getOrCreateTag().putInt(LEVEL_TAG, Math.max(1, Math.min(MAX_LEVEL, level)));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return BASE_DURABILITY * getLevel(stack);
    }

    /** 按 NBT 等级返回护甲值属性（护甲值 = 等级）。 */
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != this.type.getSlot()) {
            return ImmutableMultimap.of();
        }
        int level = getLevel(stack);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ARMOR,
                new AttributeModifier(ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()],
                        "Basic Essence armor modifier", level, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }
}
