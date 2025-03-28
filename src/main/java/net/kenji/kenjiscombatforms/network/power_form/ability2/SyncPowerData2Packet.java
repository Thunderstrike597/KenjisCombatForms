package net.kenji.kenjiscombatforms.network.power_form.ability2;

import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.network.power_form.ClientPowerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
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
            Player player = ctx.getSender();

            if(player != null) {

            }
        });
    }
}
