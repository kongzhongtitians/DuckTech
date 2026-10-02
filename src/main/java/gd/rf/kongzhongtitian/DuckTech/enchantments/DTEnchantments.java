package gd.rf.kongzhongtitian.DuckTech.enchantments;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DTEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, DuckTech.MOD_ID);

    public static final RegistryObject<Enchantment> LAVA_WALKER =
            ENCHANTMENTS.register("lava_walker", LavaWalkerEnchantment::new);

    public static void register(net.minecraftforge.eventbus.api.IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}