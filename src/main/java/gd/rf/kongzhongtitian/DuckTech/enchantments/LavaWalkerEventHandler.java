package gd.rf.kongzhongtitian.DuckTech.enchantments;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DuckTech.MODID)
public class LavaWalkerEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        Level level = player.level();

        if (level.isClientSide()) return;

        // 获取靴子上的附魔等级
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        int enchantLevel = EnchantmentHelper.getItemEnchantmentLevel(DTEnchantments.LAVA_WALKER.get(), boots);
        if (enchantLevel <= 0) return;

        // 检查玩家是否站在熔岩上方或内部
        BlockPos playerPos = player.blockPosition();
        if (!isOverLava(level, playerPos)) return;

        // 等级影响作用半径：1级半径2，2级半径4
        int radius = 1 + enchantLevel * 2;

        for (BlockPos pos : BlockPos.betweenClosed(
                playerPos.offset(-radius, -1, -radius),
                playerPos.offset(radius, 1, radius))) {
            // 只转换熔岩
            if (level.getFluidState(pos).getType() == Fluids.LAVA) {
                level.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
            }
        }
    }

    private static boolean isOverLava(Level level, BlockPos pos) {
        // 检查玩家脚下及周围是否为熔岩
        for (BlockPos p : BlockPos.betweenClosed(pos.offset(-1, -2, -1), pos.offset(1, 0, 1))) {
            if (level.getFluidState(p).getType() == Fluids.LAVA) {
                return true;
            }
        }
        return false;
    }
}