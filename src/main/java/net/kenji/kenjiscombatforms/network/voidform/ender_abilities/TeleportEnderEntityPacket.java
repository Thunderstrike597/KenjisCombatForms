package net.kenji.kenjiscombatforms.network.voidform.ender_abilities;

import net.kenji.kenjiscombatforms.api.powers.VoidPowers.EnderWarpAbility;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class TeleportEnderEntityPacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public TeleportEnderEntityPacket() {}

    public TeleportEnderEntityPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(TeleportEnderEntityPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                EnderWarpAbility.getInstance().triggerAbility(player);
                ctx.setPacketHandled(true);
            }
        });
    }
}