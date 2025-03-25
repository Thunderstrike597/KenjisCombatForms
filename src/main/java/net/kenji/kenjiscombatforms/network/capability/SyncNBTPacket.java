package net.kenji.kenjiscombatforms.network.capability;

import net.kenji.kenjiscombatforms.api.capabilities.ExtraContainerCapability;
import net.kenji.kenjiscombatforms.api.handlers.CommonEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;


public class SyncNBTPacket {
   private final ItemStack storedItem;
   private final int originalSlot;

    public SyncNBTPacket(ItemStack storedItem, int originalSlot) {
        this.storedItem = storedItem;
        this.originalSlot = originalSlot;
    }

    public SyncNBTPacket(FriendlyByteBuf buf) {
       this.storedItem = buf.readItem();
       this.originalSlot = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(this.storedItem);
        buf.writeInt(this.originalSlot);
    }

    public static void handle(SyncNBTPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {


                if (ctx.getDirection().getReceptionSide().isServer()) {
                    ServerPlayer player = ctx.getSender();
                    if (player != null) {
                        ItemStack currentItem = player.getInventory().getItem(player.getInventory().selected);
                        player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
                            CommonEventHandler commonEventHandler = CommonEventHandler.getInstance();

                            if (msg.storedItem.isEmpty()) {
                                if(currentItem.isEmpty()) {
                                    FormChangeHandler.getInstance().setSelectedFormChanged(player, msg.originalSlot);
                                } else if(currentItem.getItem() instanceof BaseFistClass) {
                                    player.getInventory().setItem(msg.originalSlot, ItemStack.EMPTY);
                                }
                            }

                            FormChangeHandler.getInstance().setSelectedFormChanged(player, msg.originalSlot);
                            container.setStoredItem(msg.storedItem);
                            commonEventHandler.setStoredItemNBT(player, msg.storedItem);
                            commonEventHandler.setOriginalSlot(player, msg.originalSlot);
                            container.setOriginalSlot(msg.originalSlot);


                    });
                }
            }
            else {
                    Player player = ctx.getSender();
                    if (player != null) {
                        player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
                            CommonEventHandler commonEventHandler = CommonEventHandler.getInstance();

                            player.getInventory().setItem(msg.originalSlot, msg.storedItem);

                            container.setStoredItem(msg.storedItem);
                            commonEventHandler.setStoredItemNBT(player, msg.storedItem);
                            commonEventHandler.setOriginalSlot(player, msg.originalSlot);
                            container.setOriginalSlot(msg.originalSlot);
                            player.inventoryMenu.broadcastChanges();
                        });
                    }
                }
        });
        ctx.setPacketHandled(true);
    }
}