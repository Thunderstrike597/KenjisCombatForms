package net.kenji.kenjiscombatforms.network.witherform.ability2;

import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncWitherData2Packet {
    private final int cooldown;

    public SyncWitherData2Packet(int cooldown) {
        this.cooldown = cooldown;
    }

    public SyncWitherData2Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public static void handle(SyncWitherData2Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            ClientWitherData.setCooldown2(msg.cooldown);
        });
    }
}
