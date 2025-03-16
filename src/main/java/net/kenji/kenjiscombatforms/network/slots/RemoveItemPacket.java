package net.kenji.kenjiscombatforms.network.slots;

import net.kenji.kenjiscombatforms.api.capabilities.ExtraContainerCapability;
import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class RemoveItemPacket {
    private int originalSlot;
    private ItemStack storedItem;

    public RemoveItemPacket(int originalSlot, ItemStack storedItem) {
        this.originalSlot = originalSlot;
        this.storedItem = storedItem;
    }

    public RemoveItemPacket(FriendlyByteBuf buf) {
        originalSlot = buf.readInt();
        storedItem = buf.readItem();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.originalSlot);
        buf.writeItem(this.storedItem);
    }

    public RemoveItemPacket decode(FriendlyByteBuf buf) {
        return new RemoveItemPacket(this.originalSlot, this.storedItem);
    }

    public static void handle(RemoveItemPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {

            ServerPlayer player = ctx.getSender();

            if (player != null) {

                FormChangeHandler formChangeHandler = FormChangeHandler.getInstance();
                int selectedSlot = player.getInventory().selected;
                ItemStack currentItem = player.getInventory().getItem(selectedSlot);
                player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
                    // Store the current item if there’s something to store

                        formChangeHandler.removeCurrentFormItem(player, selectedSlot);
                        player.getInventory().setItem(msg.originalSlot, msg.storedItem);
                        container.setStoredItem(ItemStack.EMPTY);
                        player.getInventory().setChanged();

                    // Always place a weapon, regardless of capability state
                });
            }
        });
        ctx.setPacketHandled(true);
    }
}