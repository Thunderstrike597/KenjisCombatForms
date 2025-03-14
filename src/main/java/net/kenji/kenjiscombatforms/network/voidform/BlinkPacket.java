package net.kenji.kenjiscombatforms.network.voidform;

import net.kenji.kenjiscombatforms.screen.guiEffects.BlinkEffect;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class BlinkPacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public BlinkPacket() {}

    public BlinkPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(BlinkPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                BlinkEffect.triggerFade(player);
            }else  System.out.println("DID NOT RECIEVE PACKET");
        });
        ctx.setPacketHandled(true);
    }
}