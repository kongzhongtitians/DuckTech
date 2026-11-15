package gd.rf.kongzhongtitian.DuckTech.items;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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

    private static boolean isFrostWalkerEnabled(Player player) {
        CompoundTag tag = player.getPersistentData();
        return !tag.contains(FrozenEssenceFrostWalkerTogglePacket.ENABLED_TAG)
                || tag.getBoolean(FrozenEssenceFrostWalkerTogglePacket.ENABLED_TAG);
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

        if (minSetLevel(player) >= 10) {
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 60, 0, false, false));
        }

        if (minSetLevel(player) >= 5 && isFrostWalkerEnabled(player)) {
            Level level = player.level();
            BlockPos pos = player.blockPosition();
            FrostWalkerEnchantment.onEntityMoved(player, level, pos, 2);
        }
    }

    /**
     * （抵消原版水中 1/5 的挖掘惩罚，等效于“水下速掘”附魔）。
     */
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (minSetLevel(player) >= 15) {
            if (hasFullSet(player) && player.isEyeInFluid(FluidTags.WATER)) {
                event.setNewSpeed(event.getOriginalSpeed() * 5.0F);
            }
        }
    }
}