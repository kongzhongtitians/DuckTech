package gd.rf.kongzhongtitian.DuckTech.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class ReplacerHandler {

    public static RegistryObject<Item> BASIC_ESSENCE_HELMET_LEVEL_TWO;
    public static RegistryObject<Item> BASIC_ESSENCE_HELMET_LEVEL_ONE;
    public static RegistryObject<Item> BASIC_ESSENCE_HELMET_LEVEL_THREE;


    // 冷却机制，防止每tick都执行（可选）
    private static final Map<UUID, Integer> COOLDOWN_MAP = new HashMap<>();
    private static final int COOLDOWN_TICKS = 20; // 1秒冷却

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Player player = event.player;

        // 只在服务器端执行
        if (player.level().isClientSide) {
            return;
        }

        // 冷却检查（可选，提高性能）
        UUID playerId = player.getUUID();
        int currentTick = (int) (player.level().getGameTime() % Integer.MAX_VALUE);

        if (COOLDOWN_MAP.containsKey(playerId)) {
            int lastCheck = COOLDOWN_MAP.get(playerId);
            if (currentTick - lastCheck < COOLDOWN_TICKS) {
                return;
            }
        }

        COOLDOWN_MAP.put(playerId, currentTick);

        ItemStack helmetStack = player.getItemBySlot(EquipmentSlot.HEAD);

        if (helmetStack.isEmpty()) {
            return;
        }

        // 检查是否为指定的二级头盔
        if (helmetStack.getItem() == BASIC_ESSENCE_HELMET_LEVEL_TWO.get()) {
            // 检查耐久是否即将耗尽
            int currentDamage = helmetStack.getDamageValue();
            int maxDamage = helmetStack.getMaxDamage();

            // 耐久为1或更少时替换
            if (currentDamage >= maxDamage - 1) {
                // 创建新的一级头盔
                ItemStack newHelmet = createReplacementHelmet(helmetStack,0);

                // 替换头盔
                player.setItemSlot(EquipmentSlot.HEAD, newHelmet);

                // 同步到客户端
                player.inventoryMenu.broadcastChanges();

                // 可选：发送替换消息给玩家
                player.sendSystemMessage(Component.literal("你的二级基础精华头盔已降级为一级！"));
            }
        }

        if (helmetStack.getItem() == BASIC_ESSENCE_HELMET_LEVEL_THREE.get()) {
            // 检查耐久是否即将耗尽
            int currentDamage = helmetStack.getDamageValue();
            int maxDamage = helmetStack.getMaxDamage();

            // 耐久为1或更少时替换
            if (currentDamage >= maxDamage - 1) {
                // 创建新的一级头盔
                ItemStack newHelmet = createReplacementHelmet(helmetStack,1);

                // 替换头盔
                player.setItemSlot(EquipmentSlot.HEAD, newHelmet);

                // 同步到客户端
                player.inventoryMenu.broadcastChanges();

                // 可选：发送替换消息给玩家
                player.sendSystemMessage(Component.literal("你的三级基础精华头盔已降级为二级！"));
            }
        }
    }

    /**
     * 创建替换头盔，保留原头盔的NBT数据
     */
    private static ItemStack createReplacementHelmet(ItemStack oldHelmet,int mind) {
        ItemStack newHelmet;
        if (mind == 0){
            newHelmet = new ItemStack(BASIC_ESSENCE_HELMET_LEVEL_ONE.get());
        } else if (mind == 1) {
            newHelmet = new ItemStack(BASIC_ESSENCE_HELMET_LEVEL_TWO.get());
        }else {
            newHelmet = new ItemStack(Items.STICK);
        }

        // 复制NBT数据（但不复制损坏值）
        if (oldHelmet.hasTag()) {
            // 创建NBT的深拷贝，避免修改原NBT
            newHelmet.setTag(oldHelmet.getTag().copy());
        }

        // 移除损坏标签，新头盔应该是完好的
        newHelmet.removeTagKey("Damage");

        // 复制自定义名称
        if (oldHelmet.hasCustomHoverName()) {
            newHelmet.setHoverName(oldHelmet.getHoverName());
        }

        // 复制修复成本（可选）
        if (oldHelmet.getBaseRepairCost() > 0) {
            newHelmet.setRepairCost(oldHelmet.getBaseRepairCost());
        }

        return newHelmet;
    }

    /**
     * 清理冷却地图，防止内存泄漏（在玩家退出时调用）
     */
    @SubscribeEvent
    public static void onPlayerLoggedOut(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent event) {
        COOLDOWN_MAP.remove(event.getEntity().getUUID());
    }
}