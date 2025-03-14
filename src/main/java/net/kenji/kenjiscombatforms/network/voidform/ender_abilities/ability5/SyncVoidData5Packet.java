package net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability5;

import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncVoidData5Packet {
    private final int cooldown;

    public SyncVoidData5Packet(int cooldown) {
        this.cooldown = cooldown;
    }

    public SyncVoidData5Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
    }


    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public static void handle(SyncVoidData5Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            ClientVoidData.setGrabCooldown(msg.cooldown);
        });
    }
}
