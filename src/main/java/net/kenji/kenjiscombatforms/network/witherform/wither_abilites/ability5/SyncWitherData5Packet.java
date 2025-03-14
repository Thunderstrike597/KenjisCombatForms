package net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability5;

import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncWitherData5Packet {
    private final int cooldown;


    public SyncWitherData5Packet(int cooldown) {
        this.cooldown = cooldown;
    }

    public SyncWitherData5Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();

    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public static void handle(SyncWitherData5Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            ClientWitherData.setImplodeCooldown(msg.cooldown);
        });
    }
}
