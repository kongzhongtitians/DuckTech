package gd.rf.kongzhongtitian.DuckTech.client;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import gd.rf.kongzhongtitian.DuckTech.blocks.reg.DTBlocks;
import gd.rf.kongzhongtitian.DuckTech.entities.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
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
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(DTEntities.RUBBER_DUCK.get(), RubberDuckRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(RubberDuckModel.LAYER_LOCATION, RubberDuckModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(
                    DTBlocks.RUBBER_LEAVES.get(),
                    RenderType.cutoutMipped()
            );
            // 树苗是 cross 模型（十字草状），必须用 cutout 渲染层，否则显示为不透明黑框
            ItemBlockRenderTypes.setRenderLayer(
                    DTBlocks.RUBBER_SAPLING.get(),
                    RenderType.cutout()
            );
        });
    }
}