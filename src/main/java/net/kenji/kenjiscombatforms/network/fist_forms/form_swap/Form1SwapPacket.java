package net.kenji.kenjiscombatforms.network.fist_forms.form_swap;

import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class Form1SwapPacket {

    String form1;

    public Form1SwapPacket(String form1) {
        this.form1 = form1;
    }

    public Form1SwapPacket(FriendlyByteBuf buf) {
        form1 = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.form1);
    }

    public static void handle(Form1SwapPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                FormChangeHandler.getInstance().setFormSwapOption(player, 1,FormManager.FormSelectionOption.valueOf(msg.form1));
            }
        });
        ctx.setPacketHandled(true);
    }
}