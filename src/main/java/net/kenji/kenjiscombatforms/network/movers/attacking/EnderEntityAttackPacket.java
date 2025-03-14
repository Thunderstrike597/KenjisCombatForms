package net.kenji.kenjiscombatforms.network.movers.attacking;

import net.kenji.kenjiscombatforms.event.EnderEntityAttacking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class EnderEntityAttackPacket {

    public EnderEntityAttackPacket() {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public EnderEntityAttackPacket(FriendlyByteBuf buf) {
    }



    public static void handle(EnderEntityAttackPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                   EnderEntityAttacking.getInstance().performAttack(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}