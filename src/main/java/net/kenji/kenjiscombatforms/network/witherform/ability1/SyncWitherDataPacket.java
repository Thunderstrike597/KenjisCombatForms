package net.kenji.kenjiscombatforms.network.witherform.ability1;

import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncWitherDataPacket {
    private final int cooldown;

    public SyncWitherDataPacket(int cooldown) {
        this.cooldown = cooldown;
    }

    public SyncWitherDataPacket(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public static void handle(SyncWitherDataPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            ClientWitherData.setCooldown(msg.cooldown);
        });
    }
}
