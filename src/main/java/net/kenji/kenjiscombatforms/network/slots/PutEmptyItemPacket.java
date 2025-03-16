package net.kenji.kenjiscombatforms.network.slots;

import net.kenji.kenjiscombatforms.api.capabilities.ExtraContainerCapability;
import net.kenji.kenjiscombatforms.api.handlers.CommonEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PutEmptyItemPacket {
    private int originalSlot;
    private ItemStack storedItem;

    public PutEmptyItemPacket(int originalSlot, ItemStack storedItem) {
        this.originalSlot = originalSlot;
        this.storedItem = storedItem;
    }

    public PutEmptyItemPacket(FriendlyByteBuf buf) {
        originalSlot = buf.readInt();
        storedItem = buf.readItem();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.originalSlot);
        buf.writeItem(this.storedItem);
    }

    public PutEmptyItemPacket decode(FriendlyByteBuf buf) {
        return new PutEmptyItemPacket(this.originalSlot, this.storedItem);
    }

    public static void handle(PutEmptyItemPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {

            ServerPlayer player = ctx.getSender();

            if (player != null) {
                FormChangeHandler formChangeHandler = FormChangeHandler.getInstance();
                int selectedSlot = player.getInventory().selected;
                ItemStack currentItem = player.getInventory().getItem(selectedSlot);
                player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {

                        container.setStoredItem(currentItem.copy());
                        CommonEventHandler.getInstance().setOriginalSlot(msg.originalSlot);

                        formChangeHandler.setSelectedFormChanged(player, selectedSlot);
                        player.getInventory().setChanged();
                    // Always place a weapon, regardless of capability state
                });
            }
        });
        ctx.setPacketHandled(true);
    }
}