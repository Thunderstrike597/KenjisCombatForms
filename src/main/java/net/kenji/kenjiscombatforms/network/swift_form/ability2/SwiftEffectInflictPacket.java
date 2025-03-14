package net.kenji.kenjiscombatforms.network.swift_form.ability2;

import net.kenji.kenjiscombatforms.api.powers.swift_powers.SpeedBoost;
import net.kenji.kenjiscombatforms.api.powers.swift_powers.SwiftEffectInflict;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class SwiftEffectInflictPacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public SwiftEffectInflictPacket() {}

    public SwiftEffectInflictPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(SwiftEffectInflictPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                SwiftEffectInflict.getInstance().triggerAbility(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}