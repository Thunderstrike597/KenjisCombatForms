package net.kenji.kenjiscombatforms.network;

import net.kenji.kenjiscombatforms.event.ticking.FormChangeTick;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class UpdateHandCombatPacket {
   boolean isHandCombat;

    public UpdateHandCombatPacket(boolean isHandCombat) {
        this.isHandCombat = isHandCombat;
    }

    public UpdateHandCombatPacket(FriendlyByteBuf buf) {
       this.isHandCombat = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.isHandCombat);
    }

    public static void handle(UpdateHandCombatPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                FormChangeTick.setHandCombat(player.getUUID(), msg.isHandCombat);
            }
        });
        ctx.setPacketHandled(true);
    }
}