package net.kenji.kenjiscombatforms.network.voidform.ability1;

import net.kenji.kenjiscombatforms.api.powers.VoidPowers.TeleportPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class TeleportPlayerPacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public TeleportPlayerPacket() {}

    public TeleportPlayerPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(TeleportPlayerPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                TeleportPlayer.getInstance().triggerAbility(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}