package net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability5;

import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class SyncWitherData5Packet {
    private final int cooldown;


    public SyncWitherData5Packet(int cooldown) {
        this.cooldown = cooldown;
    }

    public SyncWitherData5Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();

    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public static void handle(SyncWitherData5Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data\
            if(ctx.getDirection().getReceptionSide().isClient()){
                Player player = Minecraft.getInstance().player;
                if(player != null) {

                    FinalAbility ability5 = AbilityManager.getInstance().getCurrentFinalAbilities(player).get(1);
                    AbstractAbilityData ability5Data = ability5.getAbilityData(player);

                    ability5Data.setClientCooldown(player, msg.cooldown);
                }
            }
        });
    }
}
