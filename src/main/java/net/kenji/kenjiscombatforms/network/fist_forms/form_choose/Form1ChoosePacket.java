package net.kenji.kenjiscombatforms.network.fist_forms.form_choose;

import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class Form1ChoosePacket {

    String form1;

    public Form1ChoosePacket(String form1) {
        this.form1 = form1;
    }

    public Form1ChoosePacket(FriendlyByteBuf buf) {
        form1 = buf.readUtf();

    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.form1);
    }

    public static void handle(Form1ChoosePacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            FormChangeHandler formChangeHandler = FormChangeHandler.getInstance();
            if (player != null) {
                FormChangeHandler.getInstance().setSelectedForm(player, FormManager.FormSelectionOption.valueOf(msg.form1));
                if (player.getMainHandItem().getItem() instanceof BaseFistClass) {
                    formChangeHandler.setSelectedFormChanged(player, player.getInventory().selected);

                }
            }
        });
        ctx.setPacketHandled(true);
    }
}