package net.kenji.kenjiscombatforms.network.fist_forms.ability_choose;

import net.kenji.kenjiscombatforms.api.handlers.AbilityChangeHandler;
import net.kenji.kenjiscombatforms.api.handlers.GlobalFormStrategyHandler;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class Ability2ChoosePacket {
    String abilityOption2;

    public Ability2ChoosePacket(String abilityOption2) {
        this.abilityOption2 = abilityOption2;
    }

    public Ability2ChoosePacket(FriendlyByteBuf buf) {
        this.abilityOption2 = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.abilityOption2);
    }

    public static void handle(Ability2ChoosePacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                if(AbilityManager.getInstance().getPlayerAbilityData(player).ability2 != AbilityManager.AbilityOption2.NONE) {
                    GlobalFormStrategyHandler.getInstance().setChosenAbility2(player, AbilityManager.AbilityOption2.valueOf(msg.abilityOption2));
                    GlobalFormStrategyHandler.getInstance().setPreviouslyChosenAbility2(player, AbilityManager.AbilityOption2.valueOf(msg.abilityOption2));

                }
                else {
                    GlobalFormStrategyHandler.getInstance().setChosenAbility2(player, AbilityManager.AbilityOption2.NONE);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}