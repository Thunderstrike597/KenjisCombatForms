package net.kenji.kenjiscombatforms.network.voidform.ability2;

import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncVoidData2Packet {
    private final int cooldown;

    public SyncVoidData2Packet(int cooldown) {
        this.cooldown = cooldown;
    }

    public SyncVoidData2Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public static void handle(SyncVoidData2Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            ClientVoidData.setCooldown2(msg.cooldown);
        });
    }
}
