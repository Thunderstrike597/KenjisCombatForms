package net.kenji.kenjiscombatforms.network.fist_forms.client_data;

import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class SyncClientAbilityPacket {

    private final String currentStoredAbility1;
    private final String currentStoredAbility2;
    private final String currentStoredAbility3;
    private final String chosenAbility1;
    private final String chosenAbility2;
    private final String chosenAbility3;
    private final String selectedForm;




    public SyncClientAbilityPacket(String currentAbility1, String currentAbility2, String currentAbility3, String chosenAbility1, String chosenAbility2, String chosenAbility3, String selectedForm) {
        this.currentStoredAbility1 = currentAbility1;
        this.currentStoredAbility2 = currentAbility2;
        this.currentStoredAbility3 = currentAbility3;
        this.chosenAbility1 = chosenAbility1;
        this.chosenAbility2 = chosenAbility2;
        this.chosenAbility3 = chosenAbility3;
        this.selectedForm = selectedForm;

    }


    public SyncClientAbilityPacket(FriendlyByteBuf buf) {
        this.currentStoredAbility1 = buf.readUtf();
        this.currentStoredAbility2 = buf.readUtf();
        this.currentStoredAbility3 = buf.readUtf();
        this.chosenAbility1 = buf.readUtf();
        this.chosenAbility2 = buf.readUtf();
        this.chosenAbility3 = buf.readUtf();
        this.selectedForm = buf.readUtf();

    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.currentStoredAbility1);
        buf.writeUtf(this.currentStoredAbility2);
        buf.writeUtf(this.currentStoredAbility3);
        buf.writeUtf(this.chosenAbility1);
        buf.writeUtf(this.chosenAbility2);
        buf.writeUtf(this.chosenAbility3);
        buf.writeUtf(this.selectedForm);
    }

    public static void handle(SyncClientAbilityPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            if(ctx.getDirection().getReceptionSide().isClient()) {
                clientSideOperations(msg);
            }
        });
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void clientSideOperations(SyncClientAbilityPacket msg){
       Player player = Minecraft.getInstance().player;

       if(player != null) {
           FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);
           Form form = FormManager.getInstance().getForm(formData.selectedForm);

           AbstractFormData specificFormData = form.getFormData(player.getUUID());

           FormManager.getInstance().getForm(formData.selectedForm).getFormData(player.getUUID());
           ClientFistData.setSpecificFormData(player);
           ClientFistData.setCurrentStoredAbility1(msg.currentStoredAbility1, player);
           ClientFistData.setCurrentStoredAbility2(msg.currentStoredAbility2, player);
           ClientFistData.setCurrentStoredAbility3(msg.currentStoredAbility3, player);
           ClientFistData.setChosenAbility1(msg.chosenAbility1);
           ClientFistData.setChosenAbility2(msg.chosenAbility2);
           ClientFistData.setChosenAbility3(msg.chosenAbility3);
           ClientFistData.setSelectedForm(msg.selectedForm);
       }
    }
}
