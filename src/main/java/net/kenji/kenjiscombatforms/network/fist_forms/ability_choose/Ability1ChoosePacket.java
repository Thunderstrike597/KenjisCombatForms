package net.kenji.kenjiscombatforms.network.fist_forms.ability_choose;

import net.kenji.kenjiscombatforms.api.handlers.AbilityChangeHandler;
import net.kenji.kenjiscombatforms.api.handlers.GlobalFormStrategyHandler;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class Ability1ChoosePacket {
    String abilityOption1;

    public Ability1ChoosePacket(String abilityOption1) {
        this.abilityOption1 = abilityOption1;
    }

    public Ability1ChoosePacket(FriendlyByteBuf buf) {
        this.abilityOption1 = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.abilityOption1);
    }

    public static void handle(Ability1ChoosePacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                if(AbilityManager.getInstance().getPlayerAbilityData(player).ability1 != AbilityManager.AbilityOption1.NONE) {
                    GlobalFormStrategyHandler.getInstance().setChosenAbility1(player, AbilityManager.AbilityOption1.valueOf(msg.abilityOption1));
                    GlobalFormStrategyHandler.getInstance().setPreviouslyChosenAbility1(player, AbilityManager.AbilityOption1.valueOf(msg.abilityOption1));
                }
               else {
                    GlobalFormStrategyHandler.getInstance().setChosenAbility1(player, AbilityManager.AbilityOption1.NONE);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}