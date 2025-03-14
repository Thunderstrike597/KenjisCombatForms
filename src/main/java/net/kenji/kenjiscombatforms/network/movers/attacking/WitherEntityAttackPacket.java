package net.kenji.kenjiscombatforms.network.movers.attacking;

import net.kenji.kenjiscombatforms.event.EnderEntityAttacking;
import net.kenji.kenjiscombatforms.event.WitherEntityAttacking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class WitherEntityAttackPacket {

    public WitherEntityAttackPacket() {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public WitherEntityAttackPacket(FriendlyByteBuf buf) {
    }


    public static void handle(WitherEntityAttackPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                   WitherEntityAttacking.getInstance().performAttack(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}