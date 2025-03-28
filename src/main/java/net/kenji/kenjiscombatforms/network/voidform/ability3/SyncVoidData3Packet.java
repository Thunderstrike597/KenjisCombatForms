package net.kenji.kenjiscombatforms.network.voidform.ability3;

import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

public class SyncVoidData3Packet {
    private final int cooldown;
    private boolean isEnderActive;


    public SyncVoidData3Packet(int cooldown, boolean isEnderActive) {
        this.cooldown = cooldown;
        this.isEnderActive = isEnderActive;

    }

    public SyncVoidData3Packet(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
        this.isEnderActive = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
        buf.writeBoolean(isEnderActive);
    }

    public static void handle(SyncVoidData3Packet msg, NetworkEvent.Context ctx) {
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
