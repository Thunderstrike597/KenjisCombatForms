package net.kenji.kenjiscombatforms.network.globalformpackets;

import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherFormDashAbility;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class SyncAbility3Packet {
    private final int cooldown;
    private boolean iAbilityActive;
    private boolean isAbilityAltActive;


    public SyncAbility3Packet(int cooldown, boolean isAbilityActive,  boolean isAbilityAltActive) {
        this.cooldown = cooldown;
        this.iAbilityActive = isAbilityActive;
        this.isAbilityAltActive = isAbilityAltActive;

    }

    public SyncAbility3Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
        this.iAbilityActive = buf.readBoolean();
        this.isAbilityAltActive = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
        buf.writeBoolean(iAbilityActive);
        buf.writeBoolean(isAbilityAltActive);
    }

    public static void handle(SyncAbility3Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            if(ctx.getDirection().getReceptionSide().isClient()){
               clientSideOperations(msg);
            }
        });
    }
    @OnlyIn(Dist.CLIENT)
    private static void clientSideOperations(SyncAbility3Packet msg){
        Player player = Minecraft.getInstance().player;
        if(player != null) {
            AbilityManager abilityManager = AbilityManager.getInstance();


            AbstractAbilityData ability3Data = abilityManager.getCurrentAbilityData(player).get(2);
            FinalAbility ability3Dash = AbilityManager.getInstance().getFinalAbility(WitherFormDashAbility.getInstance().getName());

            ability3Data.setClientCooldown(player, msg.cooldown);
            ability3Data.setAbilityActive(msg.iAbilityActive);
            ability3Dash.getAbilityData(player).setAbilityAltActive(msg.isAbilityAltActive);
        }
    }
}
