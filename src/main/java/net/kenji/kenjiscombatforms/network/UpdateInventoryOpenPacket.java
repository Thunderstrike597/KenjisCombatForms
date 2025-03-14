package net.kenji.kenjiscombatforms.network;

import net.kenji.kenjiscombatforms.event.ticking.FormChangeTick;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class UpdateInventoryOpenPacket {
   boolean isGuiOpen;

    public UpdateInventoryOpenPacket(boolean isGuiOpen) {
        this.isGuiOpen = isGuiOpen;
    }

    public UpdateInventoryOpenPacket(FriendlyByteBuf buf) {
       this.isGuiOpen = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.isGuiOpen);
    }

    public static void handle(UpdateInventoryOpenPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                FormChangeTick.setGuiOpen(player.getUUID(), msg.isGuiOpen);
            }
        });
        ctx.setPacketHandled(true);
    }
}