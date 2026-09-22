package gd.rf.kongzhongtitian.DuckTech.items;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import gd.rf.kongzhongtitian.DuckTech.blocks.DTBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DTCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DuckTech.MODID);

    public static final RegistryObject<CreativeModeTab> DUCKTECH_TAB = CREATIVE_TABS.register("ducktech_tab", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("item_group." + DuckTech.MODID + ".example"))
                    .icon(() -> new ItemStack(DTItems.RUBBER_DUCK.get()))
                    .displayItems((params, output) -> {
                        DTItems.ITEMS.getEntries().forEach(entry -> entry.ifPresent(item -> output.accept(item.getDefaultInstance())));
                        DTBlocks.BLOCKS.getEntries().forEach(blockEntry -> {
                            blockEntry.ifPresent(block -> {
                                Item item = block.asItem();
                                if (item != Items.AIR) {
                                    output.accept(item);
                                }
                            });
                        });
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