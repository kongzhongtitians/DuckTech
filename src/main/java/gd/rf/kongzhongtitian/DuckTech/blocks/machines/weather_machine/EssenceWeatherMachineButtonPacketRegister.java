package gd.rf.kongzhongtitian.DuckTech.blocks.machines.weather_machine;

import gd.rf.kongzhongtitian.DuckTech.DuckTech;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = DuckTech.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EssenceWeatherMachineButtonPacketRegister {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private EssenceWeatherMachineButtonPacketRegister() {
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            int id = 0;
            CHANNEL.messageBuilder(EssenceWeatherMachineButtonPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                    .encoder(EssenceWeatherMachineButtonPacket::encode)
                    .decoder(EssenceWeatherMachineButtonPacket::new)
                    .consumerMainThread(EssenceWeatherMachineButtonPacket::handle)
                    .add();
        });
    }
}