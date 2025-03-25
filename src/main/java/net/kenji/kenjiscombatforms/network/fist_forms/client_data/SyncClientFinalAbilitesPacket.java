package net.kenji.kenjiscombatforms.network.fist_forms.client_data;

import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientBasicFistData;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class SyncClientFinalAbilitesPacket {
    private final AbilityManager.AbilityOption4 ability4;
    private final AbilityManager.AbilityOption5 ability5;


    public SyncClientFinalAbilitesPacket(AbilityManager.AbilityOption4 abilityOption4, AbilityManager.AbilityOption5 abilityOption5) {
     this.ability4 = abilityOption4;
     this.ability5 = abilityOption5;

    }

    public SyncClientFinalAbilitesPacket(FriendlyByteBuf buf) {
        this.ability4 = buf.readEnum(AbilityManager.AbilityOption4.class);
        this.ability5 = buf.readEnum(AbilityManager.AbilityOption5.class);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.ability4);
        buf.writeEnum(this.ability5);
    }

    public static void handle(SyncClientFinalAbilitesPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.getSender();
            // Update client-side data
            ClientFistData.setAbility4(msg.ability4);
            ClientFistData.setAbility5(msg.ability5);

        });
    }
}
