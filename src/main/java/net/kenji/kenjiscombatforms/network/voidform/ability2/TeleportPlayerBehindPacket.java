package net.kenji.kenjiscombatforms.network.voidform.ability2;

import net.kenji.kenjiscombatforms.api.powers.VoidPowers.TeleportPlayerBackstab;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class TeleportPlayerBehindPacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public TeleportPlayerBehindPacket() {}

    public TeleportPlayerBehindPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(TeleportPlayerBehindPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                TeleportPlayerBackstab.getInstance().triggerAbility(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}