package gd.rf.kongzhongtitian.DuckTech.items;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 冷冻精华盔甲套装效果：
 * - 集齐四件（头盔/胸甲/护腿/靴子）→ 水下呼吸（水肺）+ 水下速掘（抵消水中挖掘惩罚）
 * - 四件套每件等级均 ≥3 → 冰霜行者（行走水面自动结冰）
 */
@Mod.EventBusSubscriber
public class FrozenEssenceArmorHandler {

    /** 是否穿戴完整四件冷冻精华盔甲。 */
    private static boolean hasFullSet(Player player) {
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty() || !(stack.getItem() instanceof FrozenEssenceArmorItem)) {
                return false;
            }
        }
        return true;
    }

    /** 四件套中的最低等级。 */
    private static int minSetLevel(Player player) {
        int min = FrozenEssenceArmorItem.MAX_LEVEL;
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!(stack.getItem() instanceof FrozenEssenceArmorItem)) {
                return 0;
            }
            min = Math.min(min, FrozenEssenceArmorItem.getLevel(stack));
        }
        return min;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Player player = event.player;
        if (player.level().isClientSide) {
            return;
        }

        if (!hasFullSet(player)) {
            return;
        }

        // 水下呼吸：持续刷新水肺效果（脱装后约 3 秒内自然消退）
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 60, 0, false, false));

        // 冰霜行者：四件套均 ≥3 级时，调用原版冰霜行者逻辑（等效 2 级冰霜行者：半径 4 格水面结冰）
        if (minSetLevel(player) >= 3) {
            Level level = player.level();
            BlockPos pos = player.blockPosition();
            FrostWalkerEnchantment.onEntityMoved(player, level, pos, 2);
        }
    }

    /**
     * 水下速掘：穿戴完整四件套且眼睛在水中时，将挖掘速度恢复到陆地水平
     * （抵消原版水中 1/5 的挖掘惩罚，等效于“水下速掘”附魔）。
     */
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (hasFullSet(player) && player.isEyeInFluid(FluidTags.WATER)) {
            event.setNewSpeed(event.getOriginalSpeed() * 5.0F);
        }
    }
}
