package net.kenji.kenjiscombatforms.network.voidform.ability1;

import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncVoidDataPacket {
    private final int cooldown;

    public SyncVoidDataPacket(int cooldown) {
        this.cooldown = cooldown;
    }

    public SyncVoidDataPacket(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public static void handle(SyncVoidDataPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            ClientVoidData.setCooldown(msg.cooldown);
        });
    }
}
