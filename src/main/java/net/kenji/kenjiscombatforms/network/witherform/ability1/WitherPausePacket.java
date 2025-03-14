package net.kenji.kenjiscombatforms.network.witherform.ability1;

import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherDash;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;


public class WitherPausePacket {
    private final UUID playerId;

    // other necessary fields

    public WitherPausePacket(UUID playerId) {
        this.playerId = playerId;

    }
    // New constructor for decoding
    public WitherPausePacket(FriendlyByteBuf buf) {
        this.playerId = buf.readUUID();
    }

    // Existing encode method
    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(playerId);
    }
    // ... constructors, encode, decode methods ...

    public static void handle(WitherPausePacket packet, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                handleClient(packet);
            } else {
                handleServer(packet, ctx.getSender());
            }
        });
        ctx.setPacketHandled(true);
    }

    private static void handleClient(WitherPausePacket packet) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.level.getPlayerByUUID(packet.playerId);
        if (player != null) {
            applyDashEffect(player);
        }
    }

    private static void handleServer(WitherPausePacket packet, ServerPlayer sender) {
        if (sender != null && sender.getUUID().equals(packet.playerId)) {
            // Validate dash request here
            applyDashEffect(sender);
            // Broadcast to other nearby players
            NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> sender), packet);
        }
    }

    private static void applyDashEffect(Player player) {
        WitherDash.getInstance().activatePause(player);
    }
}