package net.kenji.kenjiscombatforms.network.fist_forms.form_choose;

import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class Form2ChoosePacket {
    String form2;

    public Form2ChoosePacket(String form2) {
        this.form2 = form2;
    }

    public Form2ChoosePacket(FriendlyByteBuf buf) {
        this.form2 = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.form2);
    }

    public static void handle(Form2ChoosePacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                FormChangeHandler.getInstance().chooseForm2(player, FormManager.FormSelectionOption.valueOf(msg.form2));
            }
        });
        ctx.setPacketHandled(true);
    }
}