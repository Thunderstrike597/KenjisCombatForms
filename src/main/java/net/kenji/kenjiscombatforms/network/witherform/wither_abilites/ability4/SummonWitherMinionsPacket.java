package net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability4;

import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherMinions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class SummonWitherMinionsPacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public SummonWitherMinionsPacket() {}

    public SummonWitherMinionsPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(SummonWitherMinionsPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                WitherMinions.getInstance().triggerAbility(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}