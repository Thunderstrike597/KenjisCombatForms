package net.kenji.kenjiscombatforms.network.power_form.ability2;

import net.kenji.kenjiscombatforms.api.powers.power_powers.PowerEffectInflict;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class PowerEffectInflictPacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public PowerEffectInflictPacket() {}

    public PowerEffectInflictPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(PowerEffectInflictPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                PowerEffectInflict.getInstance().triggerAbility(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}