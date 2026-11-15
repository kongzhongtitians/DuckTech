package gd.rf.kongzhongtitian.DuckTech.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FrozenEssenceFrostWalkerTogglePacket {
    public static final String ENABLED_TAG = "ducktech_frost_walker_enabled";

    public FrozenEssenceFrostWalkerTogglePacket() {
    }

    public FrozenEssenceFrostWalkerTogglePacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public static void handle(FrozenEssenceFrostWalkerTogglePacket msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) {
                return;
            }

            CompoundTag tag = player.getPersistentData();
            boolean next = !isEnabled(player);
            tag.putBoolean(ENABLED_TAG, next);
            player.displayClientMessage(Component.literal(next ? "Frost Walker: ON" : "Frost Walker: OFF"), true);
        });
        ctx.setPacketHandled(true);
    }

    public static boolean isEnabled(Player player) {
        CompoundTag tag = player.getPersistentData();
        return !tag.contains(ENABLED_TAG) || tag.getBoolean(ENABLED_TAG);
    }
}
