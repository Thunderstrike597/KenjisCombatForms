package net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability4;

import net.kenji.kenjiscombatforms.api.powers.VoidPowers.EnderLevitation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class EnderLevitationPacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public EnderLevitationPacket() {}

    public EnderLevitationPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(EnderLevitationPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                EnderLevitation.getInstance().triggerAbility(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}