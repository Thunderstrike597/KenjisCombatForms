package net.kenji.kenjiscombatforms.network.swift_form.ability1;

import net.kenji.kenjiscombatforms.network.swift_form.ClientSwiftData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncSwiftDataPacket {
    private final int cooldown;

    public SyncSwiftDataPacket(int cooldown) {
        this.cooldown = cooldown;
    }

    public SyncSwiftDataPacket(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeFloat(cooldown);
    }

    public static void handle(SyncSwiftDataPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            ClientSwiftData.setCooldown(msg.cooldown);
        });
    }
}
