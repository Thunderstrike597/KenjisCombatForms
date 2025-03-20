package net.kenji.kenjiscombatforms.network.movers;

import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherFormAbility;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.EnderEntity;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.WitherPlayerEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

public class WitherInputPacket {
    public final boolean jump, sneak;

    public WitherInputPacket(boolean jump, boolean sneak) {
       this.jump = jump;
       this.sneak = sneak;
    }

    public void encode(FriendlyByteBuf buf) {

        buf.writeBoolean(jump);
        buf.writeBoolean(sneak);
    }

    public static WitherInputPacket decode(FriendlyByteBuf buf) {
        return new WitherInputPacket(buf.readBoolean(), buf.readBoolean()
        );
    }

    public static boolean handle(WitherInputPacket message, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                WitherFormAbility.getInstance().upDown(player, message.sneak, message.jump);
            }
        });
        ctx.setPacketHandled(true);
        return true;
    }
}