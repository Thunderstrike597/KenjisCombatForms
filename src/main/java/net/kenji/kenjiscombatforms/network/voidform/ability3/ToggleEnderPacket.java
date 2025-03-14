package net.kenji.kenjiscombatforms.network.voidform.ability3;

import net.kenji.kenjiscombatforms.api.powers.VoidPowers.EnderFormAbility;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class ToggleEnderPacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public ToggleEnderPacket() {}

    public ToggleEnderPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(ToggleEnderPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                EnderFormAbility.getInstance().triggerAbility(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}