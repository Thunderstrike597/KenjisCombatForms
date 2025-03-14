package net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability4;

import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncVoidData4Packet {
    private final int cooldown;

    public SyncVoidData4Packet(int cooldown) {
        this.cooldown = cooldown;
    }

    public SyncVoidData4Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
    }


    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public static void handle(SyncVoidData4Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            ClientVoidData.setLevitationCooldown(msg.cooldown);
        });
    }
}
