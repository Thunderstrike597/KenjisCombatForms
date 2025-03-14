package net.kenji.kenjiscombatforms.network.fist_forms.ability_choose;

import net.kenji.kenjiscombatforms.api.handlers.AbilityChangeHandler;
import net.kenji.kenjiscombatforms.api.handlers.GlobalFormStrategyHandler;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class Ability3ChoosePacket {
   String abilityOption3;

    public Ability3ChoosePacket(String abilityOption3) {
        this.abilityOption3 = abilityOption3;
    }

    public Ability3ChoosePacket(FriendlyByteBuf buf) {
        this.abilityOption3 = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.abilityOption3);
    }

    public static void handle(Ability3ChoosePacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                if(AbilityManager.getInstance().getPlayerAbilityData(player).ability3 != AbilityManager.AbilityOption3.NONE) {
                    GlobalFormStrategyHandler.getInstance().setChosenAbility3(player, AbilityManager.AbilityOption3.valueOf(msg.abilityOption3));
                    GlobalFormStrategyHandler.getInstance().setPreviouslyChosenAbility3(player, AbilityManager.AbilityOption3.valueOf(msg.abilityOption3));

                }
                else {
                    GlobalFormStrategyHandler.getInstance().setChosenAbility3(player, AbilityManager.AbilityOption3.NONE);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}