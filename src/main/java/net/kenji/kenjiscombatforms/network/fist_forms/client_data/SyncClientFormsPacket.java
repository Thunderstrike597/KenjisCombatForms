package net.kenji.kenjiscombatforms.network.fist_forms.client_data;

import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class SyncClientFormsPacket {
    private final FormManager.FormSelectionOption form1;
    private final FormManager.FormSelectionOption from2;
    private final FormManager.FormSelectionOption from3;
    private final FormLevelManager.FormLevel currentFormLevel;
    private final int currentFormXp;
    private final int currentFormXpMAX;


    public SyncClientFormsPacket(FormManager.FormSelectionOption form1Option, FormManager.FormSelectionOption form2Option, FormManager.FormSelectionOption form3Option,  FormLevelManager.FormLevel currentFormLevel, int currentFormXp, int currentFormXpMAX) {
        this.form1 = form1Option;
        this.from2 = form2Option;
        this.from3 = form3Option;
        this.currentFormLevel = currentFormLevel;
        this.currentFormXp = currentFormXp;
        this.currentFormXpMAX = currentFormXpMAX;
    }

    public SyncClientFormsPacket(FriendlyByteBuf buf) {
     this.form1 = buf.readEnum(FormManager.FormSelectionOption.class);
     this.from2 = buf.readEnum(FormManager.FormSelectionOption.class);
     this.from3 = buf.readEnum(FormManager.FormSelectionOption.class);
     this.currentFormLevel = buf.readEnum(FormLevelManager.FormLevel.class);
     this.currentFormXp = buf.readInt();
     this.currentFormXpMAX = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.form1);
        buf.writeEnum(this.from2);
        buf.writeEnum(this.from3);
        buf.writeEnum(this.currentFormLevel);
        buf.writeInt(this.currentFormXp);
        buf.writeInt(this.currentFormXpMAX);
    }

    public static void handle(SyncClientFormsPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            if(ctx.getDirection().getReceptionSide().isClient()) {
                clientSideOperations(msg);
            }


        });
    }
    @OnlyIn(Dist.CLIENT)
    private static void clientSideOperations(SyncClientFormsPacket msg){
        Player player = Minecraft.getInstance().player;

        if(player != null) {
            ClientFistData.setForm1Option(msg.form1);
            ClientFistData.setForm2Option(msg.from2);
            ClientFistData.setForm3Option(msg.from3);
            ClientFistData.setSpecificFormData(player);
            ClientFistData.setCurrentFormLevel(msg.currentFormLevel);
            ClientFistData.setCurrentFormXp(msg.currentFormXp);
            ClientFistData.setCurrentFormXpMAX(msg.currentFormXpMAX);
        }

    }
}
