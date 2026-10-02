package gd.rf.kongzhongtitian.DuckTech.items;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DTCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DuckTech.MODID);

    public static final RegistryObject<CreativeModeTab> DUCKTECH_TAB = CREATIVE_TABS.register("ducktech_tab", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("item_group." + DuckTech.MODID + ".example"))
                    .icon(() -> new ItemStack(DTItems.RUBBER_DUCK.get()))
                    .displayItems((params, output) -> {
                        // BlockItem 已随方块注册进 ITEMS 注册表，只需遍历一次，避免每个方块重复出现
                        DTItems.ITEMS.getEntries().forEach(entry -> entry.ifPresent(item -> output.accept(item.getDefaultInstance())));
                        for (Item item : ForgeRegistries.ITEMS) {
                            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
                            if (id != null && "pipe_api".equals(id.getNamespace())) {
                                output.accept(item);
                            }
                        }
                    })
                    .build()
    );

    private static void addItemsFromRegistry(CreativeModeTab.Output output,
                                             Iterable<RegistryObject<Item>> registry) {
        for (RegistryObject<Item> entry : registry) {
            entry.ifPresent(item -> output.accept(item.getDefaultInstance()));
        }
    }
}