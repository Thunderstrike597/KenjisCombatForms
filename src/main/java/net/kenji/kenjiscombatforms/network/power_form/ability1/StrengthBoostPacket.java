package net.kenji.kenjiscombatforms.network.power_form.ability1;

import net.kenji.kenjiscombatforms.api.powers.power_powers.StrengthBoost;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class StrengthBoostPacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public StrengthBoostPacket() {}

    public StrengthBoostPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(StrengthBoostPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                StrengthBoost.getInstance().triggerAbility(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}