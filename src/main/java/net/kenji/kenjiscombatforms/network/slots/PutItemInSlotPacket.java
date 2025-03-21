package net.kenji.kenjiscombatforms.network.slots;

import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PutItemInSlotPacket {
    private int slot;


    public PutItemInSlotPacket(int slot) {
        this.slot = slot;
    }

    public PutItemInSlotPacket(FriendlyByteBuf buf) {
        slot = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.slot);
    }

    public PutItemInSlotPacket decode(FriendlyByteBuf buf) {
        return new PutItemInSlotPacket(this.slot);
    }

    public static void handle(PutItemInSlotPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {

            ServerPlayer player = ctx.getSender();

            if (player != null) {
                FormChangeHandler formChangeHandler = FormChangeHandler.getInstance();
                int selectedSlot = player.getInventory().selected;
                ItemStack currentItem = player.getInventory().getItem(selectedSlot);
                if(player.getOffhandItem().isEmpty()) {
                    formChangeHandler.setSelectedFormChanged(player, msg.slot);
                }
                else if(player.getOffhandItem().getItem() instanceof BaseFistClass){
                    player.getInventory().setItem(msg.slot, ItemStack.EMPTY);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}