package net.kenji.kenjiscombatforms.network.globalformpackets;

import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class SyncAbility4Packet {
    private final int cooldown;
    private boolean isAbilityActive;

    public SyncAbility4Packet(int cooldown, boolean isAbilityActive) {
        this.cooldown = cooldown;
        this.isAbilityActive = isAbilityActive;
    }

    public SyncAbility4Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
        this.isAbilityActive = buf.readBoolean();
    }


    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
        buf.writeBoolean(isAbilityActive);
    }

    public static void handle(SyncAbility4Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            if(ctx.getDirection().getReceptionSide().isClient()){
            clientSideOperations(msg);
            }
        });
    }
    @OnlyIn(Dist.CLIENT)
    private static void clientSideOperations(SyncAbility4Packet msg){
        Player player = Minecraft.getInstance().player;
        if(player != null) {

            AbilityManager abilityManager = AbilityManager.getInstance();

            AbstractAbilityData ability4Data = abilityManager.getCurrentFinalAbilityData(player).get(0);

            ability4Data.setClientCooldown(player, msg.cooldown);
            ability4Data.setAbilityActive(msg.isAbilityActive);
        }
    }
}
