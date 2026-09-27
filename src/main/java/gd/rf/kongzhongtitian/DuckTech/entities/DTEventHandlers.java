package gd.rf.kongzhongtitian.DuckTech.entities;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DuckTech.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DTEventHandlers {
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(DTEntities.RUBBER_DUCK.get(), RubberDuckEntity.createAttributes().build());
    }
}