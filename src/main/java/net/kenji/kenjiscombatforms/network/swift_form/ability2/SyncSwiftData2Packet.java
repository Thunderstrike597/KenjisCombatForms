package net.kenji.kenjiscombatforms.network.swift_form.ability2;

import net.kenji.kenjiscombatforms.network.swift_form.ClientSwiftData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncSwiftData2Packet {
    private final int cooldown;

    public SyncSwiftData2Packet(int cooldown) {
        this.cooldown = cooldown;
    }

    public SyncSwiftData2Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public static void handle(SyncSwiftData2Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            ClientSwiftData.setCooldown2(msg.cooldown);
        });
    }
}
