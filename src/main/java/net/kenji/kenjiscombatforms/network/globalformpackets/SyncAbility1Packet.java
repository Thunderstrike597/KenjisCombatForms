package net.kenji.kenjiscombatforms.network.globalformpackets;

import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class SyncAbility1Packet {
    private final int cooldown;
    private boolean isAbilityActive;
    private boolean isAbilityAltActive;

    public SyncAbility1Packet(int cooldown,  boolean isAbilityActive, boolean isAbilityAltActive) {
        this.cooldown = cooldown;
        this.isAbilityActive = isAbilityActive;
        this.isAbilityAltActive = isAbilityAltActive;
    }

    public SyncAbility1Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
        this.isAbilityActive = buf.readBoolean();
        this.isAbilityAltActive = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
        buf.writeBoolean(isAbilityActive);
        buf.writeBoolean(isAbilityAltActive);
    }

    public static void handle(SyncAbility1Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            if(ctx.getDirection().getReceptionSide().isClient()){
              clientSideOperations(msg);
            }
        });
    }
    @OnlyIn(Dist.CLIENT)
    private static void clientSideOperations(SyncAbility1Packet msg){
        Player player = Minecraft.getInstance().player;
        if(player != null) {

            AbilityManager abilityManager = AbilityManager.getInstance();


            AbstractAbilityData ability1Data = abilityManager.getCurrentAbilityData(player).get(0);
            ability1Data.setClientCooldown(player, msg.cooldown);
            ability1Data.setAbilityActive(msg.isAbilityActive);
            ability1Data.setAbilityAltActive(msg.isAbilityAltActive);
        }
    }
}
