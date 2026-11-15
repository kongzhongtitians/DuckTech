package gd.rf.kongzhongtitian.DuckTech.items;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import gd.rf.kongzhongtitian.DuckTech.blocks.machines.essence_weather_instrument.EssenceWeatherMachineButtonPacketRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DuckTech.MODID, value = Dist.CLIENT)
public class FrozenEssenceFrostWalkerClientEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        while (FrozenEssenceKeyMappings.TOGGLE_FROST_WALKER.consumeClick()) {
            EssenceWeatherMachineButtonPacketRegister.CHANNEL.sendToServer(new FrozenEssenceFrostWalkerTogglePacket());
        }
    }
}
