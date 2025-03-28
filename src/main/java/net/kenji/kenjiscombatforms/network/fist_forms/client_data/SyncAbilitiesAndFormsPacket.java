package net.kenji.kenjiscombatforms.network.fist_forms.client_data;

import net.kenji.kenjiscombatforms.api.handlers.AbilityChangeHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class SyncAbilitiesAndFormsPacket {




    public SyncAbilitiesAndFormsPacket() {
    }


    public SyncAbilitiesAndFormsPacket(FriendlyByteBuf buf) {

    }

    public void encode(FriendlyByteBuf buf) {

    }

    public static void handle(SyncAbilitiesAndFormsPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            ServerPlayer player = ctx.getSender();
            if(player != null) {
                FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);
                Form currentForm = FormManager.getInstance().getForm(formData.selectedForm);
                AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());

                AbilityChangeHandler.getInstance().setFormsAndAbilities(player, currentFormData);
            }
        });
        ctx.setPacketHandled(true);
    }
}
