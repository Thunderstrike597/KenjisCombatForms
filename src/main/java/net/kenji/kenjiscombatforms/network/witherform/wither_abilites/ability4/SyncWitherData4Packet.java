package net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability4;

import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class SyncWitherData4Packet {
    private final int cooldown;
    private final boolean areMinionsActive;


    public SyncWitherData4Packet(int cooldown, boolean areMinionsActive) {
        this.cooldown = cooldown;
        this.areMinionsActive = areMinionsActive;
    }

    public SyncWitherData4Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
        this.areMinionsActive = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
        buf.writeBoolean(areMinionsActive);
    }

    public static void handle(SyncWitherData4Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            if(ctx.getDirection().getReceptionSide().isClient()){
                Player player = Minecraft.getInstance().player;
                if(player != null) {

                    FinalAbility ability4 = AbilityManager.getInstance().getCurrentFinalAbilities(player).get(0);
                    AbstractAbilityData ability4Data = ability4.getAbilityData(player);

                    ability4Data.setClientCooldown(player, msg.cooldown);

                    ClientWitherData.setAreMinionsActive(msg.areMinionsActive);
                }
            }
        });
    }
}
