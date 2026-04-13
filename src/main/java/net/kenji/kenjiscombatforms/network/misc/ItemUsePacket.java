package net.kenji.kenjiscombatforms.network.misc;

import net.kenji.kenjiscombatforms.api.handlers.ControlHandler;
import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class ItemUsePacket {
    private boolean value;


    public ItemUsePacket(boolean value) {
        this.value = value;
    }

    public ItemUsePacket(FriendlyByteBuf buf) {
        value = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.value);
    }

    public ItemUsePacket decode(FriendlyByteBuf buf) {
        return new ItemUsePacket(this.value);
    }

    public static void handle(ItemUsePacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {

            ServerPlayer player = ctx.getSender();

            if (player != null) {
                ControlHandler.updateUseKey(player.getUUID(), msg.value);
            }
        });
        ctx.setPacketHandled(true);
    }
}