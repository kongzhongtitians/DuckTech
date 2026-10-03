package gd.rf.kongzhongtitian.DuckTech.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * 冷冻精华盔甲。等级通过物品 NBT 标签 "Level"（1~5）判定，不区分物品 id。
 * - 护甲值随等级提升（1~5 级分别对应 1/4/5/2 → 3/7/8/4 等防御数组）
 * - 耐久基准来自 FrozenEssenceArmorMaterial
 * 未设置标签时默认按 1 级处理。
 * 自动降级替换：非 1 级的盔甲耐久耗尽时不会损坏，而是自动降低 1 级并回满耐久；
 * 1 级盔甲耐久耗尽则按原版逻辑正常损坏消失。
 * 套装效果见 FrozenEssenceArmorHandler：
 * 集齐四件 → 水下呼吸 + 水下速掘；四件套均 ≥3 级 → 冰霜行者。
 */
public class FrozenEssenceArmorItem extends ArmorItem {

    public static final String LEVEL_TAG = "Level";
    public static final int MAX_LEVEL = 5;
    private static final int[] BASE_DURABILITY = FrozenEssenceArmorMaterial.DURABILITY;
    private static final int[] BASE_DEFENSE = FrozenEssenceArmorMaterial.DEFENSE;
    private static final int[] SECOND_DEFENSE = new int[]{2, 5, 6, 2};
    private static final int[] THIRD_DEFENSE = new int[]{3, 6, 7, 3};
    private static final int[] FORTH_DEFENSE = new int[]{3, 6, 8, 3};
    private static final int[] FIFTH_DEFENSE = new int[]{3, 7, 8, 4};

    // 靴/腿/胸/头 的护甲属性修饰符 UUID（与原版标准值一致）
    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{
            UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
            UUID.fromString("D8499B04-0E66-4726-AB29-64469D593E6E"),
            UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
            UUID.fromString("2AD3F246-FEE1-4E67-B886-69FF380E1F22")
    };

    public FrozenEssenceArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    /** 读取物品的等级，越界时夹到 1~3，未设置返回 1。 */
    public static int getLevel(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(LEVEL_TAG, CompoundTag.TAG_INT)) {
            return Math.max(1, Math.min(MAX_LEVEL, tag.getInt(LEVEL_TAG)));
        }
        setLevel(stack,1);
        return 1;
    }

    /** 设置物品等级（夹到 1~3）。 */
    public static void setLevel(ItemStack stack, int level) {
        stack.getOrCreateTag().putInt(LEVEL_TAG, Math.max(1, Math.min(MAX_LEVEL, level)));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return BASE_DURABILITY[type.getSlot().getIndex()];
    }

    /** 按 NBT 等级返回护甲值属性（护甲值 = 等级）。 */
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != this.type.getSlot()) {
            return ImmutableMultimap.of();
        }
        int level = getLevel(stack);
        int[] defense;
        switch (level){
            case 1:defense=BASE_DEFENSE;break;
            case 2:defense=SECOND_DEFENSE;break;
            case 3:defense=THIRD_DEFENSE;break;
            case 4:defense=FORTH_DEFENSE;break;
            case 5:defense=FIFTH_DEFENSE;break;
            default:defense=new int[]{1, 1, 1, 1};break;
        }
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ARMOR,
                new AttributeModifier(ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()],
                        "Basic Essence armor modifier", defense[type.getSlot().getIndex()], AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    /**
     * 耐久消耗入口（护甲受击时调用）。
     * 非 1 级盔甲在耐久即将耗尽时自动降低一级并回满耐久，避免损坏；
     * 1 级盔甲则走原版逻辑正常消耗/损坏。
     */
    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        // 创造模式不消耗耐久
        if (entity instanceof Player player && player.getAbilities().instabuild) {
            return 0;
        }

        int level = getLevel(stack);
        if (level > 1) {
            // 剩余可承受伤害 <= 本次伤害 → 耐久将耗尽，降级并回满，拦截本次消耗
            int remaining = stack.getMaxDamage() - stack.getDamageValue();
            if (amount >= remaining) {
                setLevel(stack, level - 1);
                stack.setDamageValue(0);
                if (entity instanceof Player player) {
                    player.sendSystemMessage(Component.translatable("gui.ducktech.downgrade",level));
                }
                return 0;
            }
        }

        return super.damageItem(stack, amount, entity, onBroken);
    }

    /** 在背包悬停提示中显示当前等级。 */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.ducktech.essence_armor_level", getLevel(stack), MAX_LEVEL)
                .withStyle(ChatFormatting.GRAY));
    }
}
