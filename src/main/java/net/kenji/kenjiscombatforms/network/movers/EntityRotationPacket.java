package net.kenji.kenjiscombatforms.network.movers;

import net.kenji.kenjiscombatforms.event.EntityCameraMovements;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

public class EntityRotationPacket {
    public final UUID entityUUID;
    public final float yaw;
    public final float pitch;

    public EntityRotationPacket(UUID entityUUID, float yaw, float pitch) {
        this.entityUUID = entityUUID;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(entityUUID);
        buf.writeFloat(yaw);
        buf.writeFloat(pitch);
    }

    public static EntityRotationPacket decode(FriendlyByteBuf buf) {
        return new EntityRotationPacket(buf.readUUID(), buf.readFloat(), buf.readFloat());
    }


    public static void handle(EntityRotationPacket message, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                handleClientSide(message);
            } else {
                handleServerSide(message, ctx.getSender());
            }
        });
        ctx.setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    private static void handleClientSide(EntityRotationPacket message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            Entity entity = null;
            for (Entity e : mc.level.entitiesForRendering()) {
                if (e.getUUID().equals(message.entityUUID)) {
                    entity = e;
                    break;
                }
            }
            if (entity != null && entity != mc.player) {
                EntityCameraMovements.handleServerRotation(entity, message.yaw, message.pitch);
            }
        }
    }

    private static void handleServerSide(EntityRotationPacket message, ServerPlayer sender) {
        if (sender != null) {
            ServerLevel level = sender.serverLevel();
            Entity entity = level.getEntity(message.entityUUID);
            if (entity != null) {
                entity.setYRot(message.yaw);
                entity.setXRot(message.pitch);
                entity.setYHeadRot(message.yaw);

                // Broadcast to other clients
                NetworkHandler.INSTANCE.send(
                        PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                        new EntityRotationPacket(entity.getUUID(), message.yaw, message.pitch)
                );
            }
        }
    }
}