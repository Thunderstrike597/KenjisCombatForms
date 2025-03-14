package net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability5;

import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherImplode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class WitherImplodePacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public WitherImplodePacket() {}

    public WitherImplodePacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(WitherImplodePacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                System.out.println("Received teleport request for player: " + player.getName().getString());
                WitherImplode.getInstance().triggerAbility(player);
            }else  System.out.println("DID NOT RECIEVE PACKET");
        });
        ctx.setPacketHandled(true);
    }
}