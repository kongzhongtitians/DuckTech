package gd.rf.kongzhongtitian.DuckTech.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class JuiceItem extends Item {
    public JuiceItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);

        if (!level.isClientSide) {
            if (result.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            } else {
                if (entity instanceof Player player) {
                    if (!player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE))) {
                        player.drop(new ItemStack(Items.GLASS_BOTTLE), false);
                    }
                }
            }
        }
        return result;
    }
}