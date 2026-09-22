package gd.rf.kongzhongtitian.DuckTech.client;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import gd.rf.kongzhongtitian.DuckTech.blocks.DTBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

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
                    DTBlocks.RUBBER_LEAVES.get(),
                    RenderType.cutoutMipped()
            );
        });
    }
}