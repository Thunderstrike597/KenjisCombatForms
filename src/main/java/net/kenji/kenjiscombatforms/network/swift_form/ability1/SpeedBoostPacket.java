package net.kenji.kenjiscombatforms.network.swift_form.ability1;

import net.kenji.kenjiscombatforms.api.powers.swift_powers.SpeedBoost;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class SpeedBoostPacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public SpeedBoostPacket() {}

    public SpeedBoostPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(SpeedBoostPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                SpeedBoost.getInstance().triggerAbility(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}