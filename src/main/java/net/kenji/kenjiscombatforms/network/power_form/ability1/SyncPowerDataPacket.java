package net.kenji.kenjiscombatforms.network.power_form.ability1;

import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.network.power_form.ClientPowerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class SyncPowerDataPacket {
    private final int cooldown;

    public SyncPowerDataPacket(int cooldown) {
        this.cooldown = cooldown;
    }

    public SyncPowerDataPacket(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeFloat(cooldown);
    }

    public static void handle(SyncPowerDataPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            Player player = ctx.getSender();
            if(player != null) {


            }
        });
    }
}
