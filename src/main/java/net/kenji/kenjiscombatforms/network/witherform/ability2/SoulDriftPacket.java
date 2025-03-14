package net.kenji.kenjiscombatforms.network.witherform.ability2;

import net.kenji.kenjiscombatforms.api.powers.WitherPowers.SoulDrift;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class SoulDriftPacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public SoulDriftPacket() {}

    public SoulDriftPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(SoulDriftPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                SoulDrift.getInstance().triggerAbility(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}