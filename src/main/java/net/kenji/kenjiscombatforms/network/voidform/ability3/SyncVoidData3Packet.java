package net.kenji.kenjiscombatforms.network.voidform.ability3;

import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncVoidData3Packet {
    private final int cooldown;
    private boolean isEnderActive;

    public SyncVoidData3Packet(int cooldown, boolean isEnderActive) {
        this.cooldown = cooldown;
        this.isEnderActive = isEnderActive;
    }

    public SyncVoidData3Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
        this.isEnderActive = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
        buf.writeBoolean(isEnderActive);
    }

    public static void handle(SyncVoidData3Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            ClientVoidData.setCooldown3(msg.cooldown);
            ClientVoidData.setEnderActive(msg.isEnderActive);
        });
    }
}
