package gd.rf.kongzhongtitian.DuckTech.blocks.machines.weather_machine;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EssenceWeatherMachineButtonPacket {

    private final BlockPos pos;
    private final int mode;

    public EssenceWeatherMachineButtonPacket(BlockPos pos, int mode) {
        this.pos = pos;
        this.mode = mode;
    }

    public EssenceWeatherMachineButtonPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.mode = buf.readVarInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeVarInt(this.mode);
    }

    // 改为静态方法，签名符合 BiConsumer<MSG, Supplier<NetworkEvent.Context>>
    public static void handle(EssenceWeatherMachineButtonPacket msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) {
                return;
            }
            if (player.level().getBlockEntity(msg.pos) instanceof EssenceWeatherMachineBlockEntity be) {
                be.applyWeather(msg.mode);
            }
        });
        ctx.setPacketHandled(true);
    }
}