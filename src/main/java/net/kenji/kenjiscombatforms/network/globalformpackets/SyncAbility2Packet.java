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

public class SyncAbility2Packet {
    private final int cooldown;
    private boolean isAbilityActive;
    private boolean isAbilityAltActive;

    public SyncAbility2Packet(int cooldown, boolean isAbilityActive, boolean isAbilityAltActive) {
        this.cooldown = cooldown;
        this.isAbilityActive = isAbilityActive;
        this.isAbilityAltActive = isAbilityAltActive;
    }

    public SyncAbility2Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
        this.isAbilityActive = buf.readBoolean();
        this.isAbilityAltActive = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
        buf.writeBoolean(isAbilityActive);
        buf.writeBoolean(isAbilityAltActive);
    }

    public static void handle(SyncAbility2Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            if(ctx.getDirection().getReceptionSide().isClient()){
              clientSideOperations(msg);
            }
        });
    }
    @OnlyIn(Dist.CLIENT)
    private static void clientSideOperations(SyncAbility2Packet msg){
        Player player = Minecraft.getInstance().player;
        if(player != null) {
            AbilityManager abilityManager = AbilityManager.getInstance();


            AbstractAbilityData ability2Data = abilityManager.getCurrentAbilityData(player).get(1);
            ability2Data.setClientCooldown(player, msg.cooldown);
            ability2Data.setAbilityActive(msg.isAbilityActive);
            ability2Data.setAbilityAltActive(msg.isAbilityAltActive);
        }
    }
}
