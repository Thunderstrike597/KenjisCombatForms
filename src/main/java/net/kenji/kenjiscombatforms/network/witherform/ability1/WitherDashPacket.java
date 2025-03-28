package net.kenji.kenjiscombatforms.network.witherform.ability1;

import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherDash;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;


public class WitherDashPacket {
    private final UUID playerId;
    private final Vec3 dashDirection;
    private final double dashSpeed;
    // other necessary fields

    public WitherDashPacket(UUID playerId, Vec3 dashDirection, double dashSpeed) {
        this.playerId = playerId;
        this.dashDirection = dashDirection;
        this.dashSpeed = dashSpeed;
    }
    // New constructor for decoding
    public WitherDashPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readUUID();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        this.dashDirection = new Vec3(x, y, z);
        this.dashSpeed = buf.readDouble();
    }

    // Existing encode method
    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(playerId);
        buf.writeDouble(dashDirection.x);
        buf.writeDouble(dashDirection.y);
        buf.writeDouble(dashDirection.z);
        buf.writeDouble(dashSpeed);
    }
    // ... constructors, encode, decode methods ...

    public static void handle(WitherDashPacket packet, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            WitherDash.getInstance().triggerAbility(player);
        });
        ctx.setPacketHandled(true);
    }


    private static void handleServer(WitherDashPacket packet, ServerPlayer sender) {
        if (sender != null && sender.getUUID().equals(packet.playerId)) {
            // Validate dash request here
            applyDashEffect(sender, packet.dashDirection, packet.dashSpeed);
            // Broadcast to other nearby players
            NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> sender), packet);
        }
    }

    private static void applyDashEffect(Player player, Vec3 direction, double speed) {
        WitherDash.getInstance().activateDash(player, direction, speed);
    }
}