package net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability4;

import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class SyncVoidData4Packet {
    private final int cooldown;

    public SyncVoidData4Packet(int cooldown) {
        this.cooldown = cooldown;
    }

    public SyncVoidData4Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
    }


    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public static void handle(SyncVoidData4Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            if(ctx.getDirection().getReceptionSide().isClient()){
                Player player = Minecraft.getInstance().player;
                if(player != null) {

                FinalAbility ability4 = AbilityManager.getInstance().getCurrentFinalAbilities(player).get(0);
                AbstractAbilityData ability4Data = ability4.getAbilityData(player);

                ability4Data.setClientCooldown(player, msg.cooldown);
                }
            }
        });
    }
}
