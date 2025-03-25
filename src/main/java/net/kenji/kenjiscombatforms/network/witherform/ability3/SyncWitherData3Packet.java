package net.kenji.kenjiscombatforms.network.witherform.ability3;

import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

public class SyncWitherData3Packet {
    private final int cooldown;
    private final boolean isWitherActive;
    private final boolean isWitherDashActive;
    private final UUID witherEntityUUID;


    public SyncWitherData3Packet(int cooldown, boolean isWitherActive, boolean isWitherDashActive,UUID witherEntityUUID) {
        this.cooldown = cooldown;
        this.isWitherActive = isWitherActive;
        this.witherEntityUUID = witherEntityUUID;
        this.isWitherDashActive = isWitherDashActive;
    }

    public SyncWitherData3Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
        this.isWitherActive = buf.readBoolean();
        this.isWitherDashActive = buf.readBoolean();
        this.witherEntityUUID = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
        buf.writeBoolean(isWitherActive);
        buf.writeBoolean(isWitherDashActive);
        buf.writeUUID(witherEntityUUID);
    }

    public static void handle(SyncWitherData3Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
        if(ctx.getDirection().getReceptionSide().isClient()){
            clientSideOperations(msg);
        }


        });
    }

@OnlyIn(Dist.CLIENT)
    public static void clientSideOperations(SyncWitherData3Packet msg){
    Minecraft minecraft = Minecraft.getInstance();

        ClientWitherData.setCooldown3(msg.cooldown);
        ClientWitherData.setIsWitherActive(msg.isWitherActive);
        ClientWitherData.setIsWitherDashActive(msg.isWitherDashActive);
        if(msg.witherEntityUUID != null) {
            ClientWitherData.setWitherUUID(msg.witherEntityUUID);
        }
    }
}
