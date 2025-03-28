package net.kenji.kenjiscombatforms.network.fist_forms.client_data;

import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class SyncClientFinalAbilitesPacket {
    private final String ability4;
    private final String ability5;


    public SyncClientFinalAbilitesPacket(String abilityOption4, String abilityOption5) {
     this.ability4 = abilityOption4;
     this.ability5 = abilityOption5;

    }

    public SyncClientFinalAbilitesPacket(FriendlyByteBuf buf) {
        this.ability4 = buf.readUtf();
        this.ability5 = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.ability4);
        buf.writeUtf(this.ability5);
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
