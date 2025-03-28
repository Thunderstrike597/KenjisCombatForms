package net.kenji.kenjiscombatforms.network.voidform.ability2;

import net.kenji.kenjiscombatforms.api.powers.VoidPowers.VoidAnchorRift;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class VoidRiftPacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public VoidRiftPacket() {}

    public VoidRiftPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(VoidRiftPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                VoidAnchorRift.getInstance().triggerAbility(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}