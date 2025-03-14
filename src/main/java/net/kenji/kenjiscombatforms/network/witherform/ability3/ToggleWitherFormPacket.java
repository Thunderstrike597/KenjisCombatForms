package net.kenji.kenjiscombatforms.network.witherform.ability3;

import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherFormAbility;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class ToggleWitherFormPacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public ToggleWitherFormPacket() {}

    public ToggleWitherFormPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(ToggleWitherFormPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                WitherFormAbility.getInstance().triggerAbility(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}