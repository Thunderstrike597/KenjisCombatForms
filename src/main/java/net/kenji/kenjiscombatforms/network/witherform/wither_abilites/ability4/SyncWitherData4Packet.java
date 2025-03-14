package net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability4;

import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncWitherData4Packet {
    private final int cooldown;
    private final boolean areMinionsActive;


    public SyncWitherData4Packet(int cooldown, boolean areMinionsActive) {
        this.cooldown = cooldown;
        this.areMinionsActive = areMinionsActive;
    }

    public SyncWitherData4Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
        this.areMinionsActive = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
        buf.writeBoolean(areMinionsActive);
    }

    public static void handle(SyncWitherData4Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            ClientWitherData.setMinionCooldown(msg.cooldown);
            ClientWitherData.setAreMinionsActive(msg.areMinionsActive);
        });
    }
}
