package net.kenji.kenjiscombatforms.network.fist_forms.form_swap;

import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class Form3SwapPacket {

    String form3;

    public Form3SwapPacket(String form3) {
        this.form3 = form3;
    }

    public Form3SwapPacket(FriendlyByteBuf buf) {
        form3 = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.form3);
    }

    public static void handle(Form3SwapPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                FormChangeHandler.getInstance().setForm3SwapOption(player, FormManager.FormSelectionOption.valueOf(msg.form3));
            }
        });
        ctx.setPacketHandled(true);
    }
}