package net.kenji.kenjiscombatforms.network.power_form.ability2;

import net.kenji.kenjiscombatforms.network.power_form.ClientPowerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncPowerData2Packet {
    private final int cooldown;

    public SyncPowerData2Packet(int cooldown) {
        this.cooldown = cooldown;
    }

    public SyncPowerData2Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public static void handle(SyncPowerData2Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            ClientPowerData.setCooldown2(msg.cooldown);
        });
    }
}
