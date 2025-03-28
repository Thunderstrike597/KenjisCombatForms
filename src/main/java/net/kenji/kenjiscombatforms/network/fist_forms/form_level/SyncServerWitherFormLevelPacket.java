package net.kenji.kenjiscombatforms.network.fist_forms.form_level;

import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.forms.WitherForm;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class SyncServerWitherFormLevelPacket {


    private final int witherFormXp;
    private final int witherFormXpMAX;
    private final FormLevelManager.FormLevel witherFormLevel;

    public SyncServerWitherFormLevelPacket(int witherFormXp, int witherFormXpMAX, FormLevelManager.FormLevel witherFormLevel) {
      this.witherFormXp = witherFormXp;
      this.witherFormXpMAX = witherFormXpMAX;
      this.witherFormLevel = witherFormLevel;
    }


    public SyncServerWitherFormLevelPacket(FriendlyByteBuf buf) {
        this.witherFormXp = buf.readInt();
        this.witherFormXpMAX = buf.readInt();
        this.witherFormLevel = buf.readEnum(FormLevelManager.FormLevel.class);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.witherFormXp);
        buf.writeInt(this.witherFormXpMAX);
        buf.writeEnum(this.witherFormLevel);
    }

    public static void handle(SyncServerWitherFormLevelPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                AbstractFormData formData = WitherForm.getInstance().getFormData(player.getUUID());


                    // Update server-side data
                    formData.setCurrentFormXp(msg.witherFormXp);
                    formData.setCurrentFormXpMAX(msg.witherFormXpMAX);
                    WitherForm.getInstance().getFormData(player.getUUID()).setCurrentFormLevel(msg.witherFormLevel);

                    // Sync to client
                }
        });
        ctx.setPacketHandled(true);
    }
}