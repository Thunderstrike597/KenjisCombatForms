package net.kenji.kenjiscombatforms.network.fist_forms.form_choose;

import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class Form3ChoosePacket {
    String form3;

    public Form3ChoosePacket(String form3) {
        this.form3 = form3;
    }

    public Form3ChoosePacket(FriendlyByteBuf buf) {
        this.form3 = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.form3);
    }

    public static void handle(Form3ChoosePacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                FormChangeHandler.getInstance().chooseForm3(player, FormManager.FormSelectionOption.valueOf(msg.form3));
            }
        });
        ctx.setPacketHandled(true);
    }
}