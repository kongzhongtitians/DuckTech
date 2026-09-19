package org.quiltmc.users.duckteam.DuckTech.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.quiltmc.users.duckteam.DuckTech.DuckTech;
import org.quiltmc.users.duckteam.DuckTech.block.ModBlocks;

@Mod.EventBusSubscriber(
        modid = DuckTech.MODID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(
                    ModBlocks.RUBBER_LEAVES.get(),
                    RenderType.cutoutMipped()
            );
        });
    }
}