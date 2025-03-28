package net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability5;

import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class SyncVoidData5Packet {
    private final int cooldown;

    public SyncVoidData5Packet(int cooldown) {
        this.cooldown = cooldown;
    }

    public SyncVoidData5Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
    }


    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public static void handle(SyncVoidData5Packet msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
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
