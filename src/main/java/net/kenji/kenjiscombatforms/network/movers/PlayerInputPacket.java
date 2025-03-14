package net.kenji.kenjiscombatforms.network.movers;

import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.EnderEntity;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.WitherPlayerEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

public class PlayerInputPacket {
    public final boolean forward, backward, left, right, jump, sneak;
    public UUID entityUUID;

    public PlayerInputPacket(boolean forward, boolean backward, boolean left, boolean right, boolean jump, boolean sneak, UUID entity) {
        this.forward = forward;
        this.backward = backward;
        this.left = left;
        this.right = right;
        this.jump = jump;
        this.sneak = sneak;
        this.entityUUID = entity;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(forward);
        buf.writeBoolean(backward);
        buf.writeBoolean(left);
        buf.writeBoolean(right);
        buf.writeBoolean(jump);
        buf.writeBoolean(sneak);
        buf.writeUUID(entityUUID);
    }

    public static PlayerInputPacket decode(FriendlyByteBuf buf) {
        return new PlayerInputPacket(
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readUUID()
        );
    }

    public static boolean handle(PlayerInputPacket message, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                Entity entity = player.serverLevel().getEntity(message.entityUUID);
                if (entity instanceof EnderEntity enderEntity) {
                    if (!enderEntity.level().getBlockState(enderEntity.blockPosition().below(3)).isSolid()) {
                        enderEntity.setMovementInput(player, message.forward, message.backward, message.left, message.right, false, message.sneak);
                    }
                    else enderEntity.setMovementInput(player, message.forward, message.backward, message.left, message.right, message.jump, message.sneak);
                }
                else if (entity instanceof WitherPlayerEntity witherEntity) {
                    if (!witherEntity.level().getBlockState(witherEntity.blockPosition().below(3)).isSolid()) {
                        witherEntity.setMovementInput(player, message.forward, message.backward, message.left, message.right, false, message.sneak);
                    }
                    else witherEntity.setMovementInput(player, message.forward, message.backward, message.left, message.right, message.jump, message.sneak);
                }

            }
        });
        ctx.setPacketHandled(true);
        return true;
    }
}