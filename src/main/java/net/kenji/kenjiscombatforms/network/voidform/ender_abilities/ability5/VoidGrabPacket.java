package net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability5;

import net.kenji.kenjiscombatforms.api.powers.VoidPowers.VoidGrab;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class VoidGrabPacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public VoidGrabPacket() {}

    public VoidGrabPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(VoidGrabPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                VoidGrab.getInstance().triggerAbility(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}