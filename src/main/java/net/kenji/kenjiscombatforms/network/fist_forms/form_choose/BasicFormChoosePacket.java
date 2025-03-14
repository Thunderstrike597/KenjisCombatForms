package net.kenji.kenjiscombatforms.network.fist_forms.form_choose;

import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class BasicFormChoosePacket {
    // You can add fields here if you need to send any specific data
    // For a simple toggle, we might not need any fields
    public BasicFormChoosePacket() {}

    public BasicFormChoosePacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(BasicFormChoosePacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                FormChangeHandler.getInstance().chooseBasicForm(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}