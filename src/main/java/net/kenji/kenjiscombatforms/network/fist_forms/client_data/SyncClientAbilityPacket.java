package net.kenji.kenjiscombatforms.network.fist_forms.client_data;

import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class SyncClientAbilityPacket {

    private final AbilityManager.AbilityOption1 currentAbility1;
    private final AbilityManager.AbilityOption2 currentAbility2;
    private final AbilityManager.AbilityOption3 currentAbility3;
    private final AbilityManager.AbilityOption1 chosenAbility1;
    private final AbilityManager.AbilityOption2 chosenAbility2;
    private final AbilityManager.AbilityOption3 chosenAbility3;
    private final FormManager.FormSelectionOption selectedForm;




    public SyncClientAbilityPacket(AbilityManager.AbilityOption1 currentAbility1, AbilityManager.AbilityOption2 currentAbility2, AbilityManager.AbilityOption3 currentAbility3, AbilityManager.AbilityOption1 chosenAbility1, AbilityManager.AbilityOption2 chosenAbility2, AbilityManager.AbilityOption3 chosenAbility3, FormManager.FormSelectionOption selectedForm) {
        this.currentAbility1 = currentAbility1;
        this.currentAbility2 = currentAbility2;
        this.currentAbility3 = currentAbility3;
        this.chosenAbility1 = chosenAbility1;
        this.chosenAbility2 = chosenAbility2;
        this.chosenAbility3 = chosenAbility3;
        this.selectedForm = selectedForm;

    }


    public SyncClientAbilityPacket(FriendlyByteBuf buf) {
        this.currentAbility1 = buf.readEnum(AbilityManager.AbilityOption1.class);
        this.currentAbility2 = buf.readEnum(AbilityManager.AbilityOption2.class);
        this.currentAbility3 = buf.readEnum(AbilityManager.AbilityOption3.class);
        this.chosenAbility1 = buf.readEnum(AbilityManager.AbilityOption1.class);
        this.chosenAbility2 = buf.readEnum(AbilityManager.AbilityOption2.class);
        this.chosenAbility3 = buf.readEnum(AbilityManager.AbilityOption3.class);
        this.selectedForm = buf.readEnum(FormManager.FormSelectionOption.class);

    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.currentAbility1);
        buf.writeEnum(this.currentAbility2);
        buf.writeEnum(this.currentAbility3);
        buf.writeEnum(this.chosenAbility1);
        buf.writeEnum(this.chosenAbility2);
        buf.writeEnum(this.chosenAbility3);
        buf.writeEnum(this.selectedForm);
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
           ClientFistData.setSpecificFormData(player);
           ClientFistData.setCurrentAbility1(msg.currentAbility1);
           ClientFistData.setCurrentAbility2(msg.currentAbility2);
           ClientFistData.setCurrentAbility3(msg.currentAbility3);
           ClientFistData.setChosenAbility1(msg.chosenAbility1);
           ClientFistData.setChosenAbility2(msg.chosenAbility2);
           ClientFistData.setChosenAbility3(msg.chosenAbility3);
           ClientFistData.setSelectedForm(msg.selectedForm);
       }
    }
}
