package net.kenji.kenjiscombatforms.network.voidform.ability1;

import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class SyncVoidDataPacket {
    private final int cooldown;

    public SyncVoidDataPacket(int cooldown) {
        this.cooldown = cooldown;
    }

    public SyncVoidDataPacket(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public static void handle(SyncVoidDataPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            if(ctx.getDirection().getReceptionSide().isClient()){
                Player player = Minecraft.getInstance().player;
                if(player != null) {

                }
            }
        });
    }
}
