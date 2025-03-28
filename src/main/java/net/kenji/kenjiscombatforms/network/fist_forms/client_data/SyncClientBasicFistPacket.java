package net.kenji.kenjiscombatforms.network.fist_forms.client_data;

import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientBasicFistData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class SyncClientBasicFistPacket {
    private final String basicAbility1;
    private final String basicAbility2;


    public SyncClientBasicFistPacket(String basicAbility1, String basicAbility2) {
        this.basicAbility1 = basicAbility1;
        this.basicAbility2 = basicAbility2;

    }

    public SyncClientBasicFistPacket(FriendlyByteBuf buf) {
        this.basicAbility1 = buf.readUtf();
        this.basicAbility2 = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.basicAbility1);
        buf.writeUtf(this.basicAbility2);
    }

    public static void handle(SyncClientBasicFistPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.getSender();
            // Update client-side data
            ClientBasicFistData.setBasicAbility1(player, msg.basicAbility1);
            ClientBasicFistData.setBasicAbility2(player, msg.basicAbility2);
        });
    }
}
