package net.kenji.kenjiscombatforms.network.sound;

import net.kenji.kenjiscombatforms.api.capabilities.ExtraContainerCapability;
import net.kenji.kenjiscombatforms.api.handlers.CommonEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.event.ticking.FormChangeTick;
import net.kenji.kenjiscombatforms.network.UpdateHandCombatPacket;
import net.kenji.kenjiscombatforms.network.slots.PutEmptyItemPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class SoundTriggerPacket {
    public SoundTriggerPacket() {}

    public SoundTriggerPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {

    }
    public SoundTriggerPacket decode(FriendlyByteBuf buf) {
        return new SoundTriggerPacket(buf);
    }

    public static void handle(SoundTriggerPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            if(ctx.getDirection().getReceptionSide().isClient()){
                Player player = ctx.getSender();
                if(player != null){
                    ctx.getSender().playSound(SoundEvents.AMETHYST_BLOCK_CHIME);
                }
            }
        });
        ctx.setPacketHandled(true);
    }


}