package net.kenji.kenjiscombatforms.network.fist_forms.form_level;

import net.kenji.kenjiscombatforms.api.handlers.data_handle.SavedDataHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.client_data.SyncClientFormsPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class SyncServerFormLevelPacket {

    private final String currentForm;
    private final int formXp;
    private final int formXpMAX;
    private final FormLevelManager.FormLevel formLevel;

    public SyncServerFormLevelPacket(String currentForm, int formXp, int formXpMAX, FormLevelManager.FormLevel formLevel) {
        this.currentForm = currentForm;
        this.formXp = formXp;
        this.formXpMAX = formXpMAX;
        this.formLevel = formLevel;
    }


    public SyncServerFormLevelPacket(FriendlyByteBuf buf) {
       this.currentForm = buf.readUtf();
        this.formXp = buf.readInt();
        this.formXpMAX = buf.readInt();
        this.formLevel = buf.readEnum(FormLevelManager.FormLevel.class);
    }

    public void encode(FriendlyByteBuf buf) {
      buf.writeUtf(this.currentForm);
        buf.writeInt(this.formXp);
        buf.writeInt(this.formXpMAX);
        buf.writeEnum(this.formLevel);
    }

    public static void handle(SyncServerFormLevelPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {

                List<Form> formValue = FormManager.getInstance().getCurrentForms(player);
                List<AbstractFormData> formData = FormManager.getInstance().getCurrentFormData(player);


                Form currentForm = FormManager.getInstance().getForm(formValue.get(0).getName());
                AbstractFormData currentFormData = formData.get(0);

                // Update server-side data
                currentFormData.setCurrentFormXp(msg.formXp);
                currentFormData.setCurrentFormXpMAX(msg.formXpMAX);
                currentFormData.setCurrentFormLevel(msg.formLevel);

                SavedDataHandler savedData = SavedDataHandler.get(player.serverLevel());
                savedData.updatePlayerData(player.getUUID());

                savedData.setDirty();


                NetworkHandler.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new SyncClientFormsPacket(formValue.get(1).getName(), formValue.get(2).getName(), formValue.get(3).getName(), msg.formLevel, msg.formXp, msg.formXpMAX)
                );
                // Sync to client
            }
        });
        ctx.setPacketHandled(true);
    }
}
