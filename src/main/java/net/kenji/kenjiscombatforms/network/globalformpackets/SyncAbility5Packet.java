package net.kenji.kenjiscombatforms.network.globalformpackets;

import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class SyncAbility5Packet {
    private final int cooldown;
    private boolean isAbilityActive;

    public SyncAbility5Packet(int cooldown, boolean isAbilityActive) {
        this.cooldown = cooldown;
        this.isAbilityActive = isAbilityActive;
    }

    public SyncAbility5Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
        this.isAbilityActive = buf.readBoolean();
    }


    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
        buf.writeBoolean(isAbilityActive);
    }

    public static void handle(SyncAbility5Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            if(ctx.getDirection().getReceptionSide().isClient()){
             clientSideOperations(msg);
            }
        });
    }
    @OnlyIn(Dist.CLIENT)
    private static void clientSideOperations(SyncAbility5Packet msg){
        Player player = Minecraft.getInstance().player;
        if(player != null) {

            AbilityManager abilityManager = AbilityManager.getInstance();

            AbstractAbilityData ability5Data = abilityManager.getCurrentFinalAbilityData(player).get(1);

            ability5Data.setAbilityActive(msg.isAbilityActive);
            ability5Data.setClientCooldown(player, msg.cooldown);
        }
    }
}
