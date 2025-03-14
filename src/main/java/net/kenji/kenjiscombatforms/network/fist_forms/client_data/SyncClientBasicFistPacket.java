package net.kenji.kenjiscombatforms.network.fist_forms.client_data;

import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientBasicFistData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class SyncClientBasicFistPacket {
    private final AbilityManager.AbilityOption1 basicAbility1;
    private final AbilityManager.AbilityOption2 basicAbility2;


    public SyncClientBasicFistPacket(AbilityManager.AbilityOption1 basicAbility1, AbilityManager.AbilityOption2 basicAbility2) {
        this.basicAbility1 = basicAbility1;
        this.basicAbility2 = basicAbility2;

    }

    public SyncClientBasicFistPacket(FriendlyByteBuf buf) {
        this.basicAbility1 = buf.readEnum(AbilityManager.AbilityOption1.class);
        this.basicAbility2 = buf.readEnum(AbilityManager.AbilityOption2.class);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.basicAbility1);
        buf.writeEnum(this.basicAbility2);
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
