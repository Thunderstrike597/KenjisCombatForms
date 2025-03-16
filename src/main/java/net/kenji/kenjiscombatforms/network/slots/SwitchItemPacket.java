package net.kenji.kenjiscombatforms.network.slots;

import net.kenji.kenjiscombatforms.api.capabilities.ExtraContainerCapability;
import net.kenji.kenjiscombatforms.api.handlers.CommonEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class SwitchItemPacket {
    private int originalSlot;
    private ItemStack storedItem;

    public SwitchItemPacket(int originalSlot, ItemStack storedItem) {
        this.originalSlot = originalSlot;
        this.storedItem = storedItem;
    }

    public SwitchItemPacket(FriendlyByteBuf buf) {
        originalSlot = buf.readInt();
        storedItem = buf.readItem();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.originalSlot);
        buf.writeItem(this.storedItem);
    }

    public SwitchItemPacket decode(FriendlyByteBuf buf) {
        return new SwitchItemPacket(this.originalSlot, this.storedItem);
    }

    public static void handle(SwitchItemPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {

            ServerPlayer player = ctx.getSender();

            if (player != null) {
                FormChangeHandler formChangeHandler = FormChangeHandler.getInstance();
                int selectedSlot = player.getInventory().selected;
                ItemStack currentItem = player.getInventory().getItem(selectedSlot);
                player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
                if (!currentItem.isEmpty() && container.getStoredItem().isEmpty() && !(currentItem.getItem() instanceof BaseFistClass)) {
                    container.setStoredItem(currentItem.copy());
                    player.getInventory().setItem(selectedSlot, ItemStack.EMPTY);
                    CommonEventHandler.getInstance().setOriginalSlot(msg.originalSlot);
                    formChangeHandler.setSelectedFormChanged(player, selectedSlot);
                    player.getInventory().setChanged();
                    // Always place a weapon, regardless of capability state
                }
                else if(currentItem.isEmpty() && container.getStoredItem().isEmpty()){
                    CommonEventHandler.getInstance().setOriginalSlot(msg.originalSlot);

                    formChangeHandler.setSelectedFormChanged(player, selectedSlot);
                    player.getInventory().setChanged();
                    }
                });
            }
        });
        ctx.setPacketHandled(true);
    }
}