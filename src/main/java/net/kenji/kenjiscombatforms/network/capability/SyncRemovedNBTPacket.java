package net.kenji.kenjiscombatforms.network.capability;

import net.kenji.kenjiscombatforms.api.capabilities.ExtraContainerCapability;
import net.kenji.kenjiscombatforms.api.handlers.CommonEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;


public class SyncRemovedNBTPacket {
   private final ItemStack storedItem;
   private final int originalSlot;

    public SyncRemovedNBTPacket(ItemStack storedItem, int originalSlot) {
        this.storedItem = storedItem;
        this.originalSlot = originalSlot;
    }

    public SyncRemovedNBTPacket(FriendlyByteBuf buf) {
       this.storedItem = buf.readItem();
       this.originalSlot = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(this.storedItem);
        buf.writeInt(this.originalSlot);
    }

    public static void handle(SyncRemovedNBTPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {

            if(ctx.getDirection().getReceptionSide().isServer()) {
                ServerPlayer player = ctx.getSender();
                if (player != null) {
                    if(msg.originalSlot != -1) {
                        player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
                            ItemStack currentItem = player.getInventory().getItem(player.getInventory().selected);

                            CommonEventHandler commonEventHandler = CommonEventHandler.getInstance();


                            if (!msg.storedItem.isEmpty()) {
                                player.getInventory().setItem(msg.originalSlot, msg.storedItem);
                            }
                            if (msg.storedItem.isEmpty()) {
                                container.setStoredItem(ItemStack.EMPTY);
                                if (player.getInventory().getItem(msg.originalSlot).getItem() instanceof BaseFistClass) {
                                    player.getInventory().setItem(msg.originalSlot, ItemStack.EMPTY);
                                }
                            }
                            commonEventHandler.setStoredItemNBT(player, msg.storedItem);
                            commonEventHandler.setOriginalSlot(player, msg.originalSlot);
                            container.setOriginalSlot(msg.originalSlot);
                        });
                    }
                }
            }else if(ctx.getDirection().getReceptionSide().isClient()){
                Player player = ctx.getSender();
                if (player != null) {
                    if(player.level().isClientSide){
                    clientSideSync(player, msg);
                    }
                }
            }
        });
        ctx.setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    private static void clientSideSync(Player player, SyncRemovedNBTPacket msg) {
        if (msg.originalSlot != -1) {
            player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
                ItemStack currentItem = player.getInventory().getItem(player.getInventory().selected);
                CommonEventHandler commonEventHandler = CommonEventHandler.getInstance();


                if (!msg.storedItem.isEmpty()) {
                    player.getInventory().setItem(msg.originalSlot, msg.storedItem);
                }
                if (msg.storedItem.isEmpty()) {
                    container.setStoredItem(ItemStack.EMPTY);
                    if (player.getInventory().getItem(msg.originalSlot).getItem() instanceof BaseFistClass) {
                        player.getInventory().setItem(msg.originalSlot, ItemStack.EMPTY);
                    }
                }
                commonEventHandler.setStoredItemNBT(player, msg.storedItem);
                commonEventHandler.setOriginalSlot(player, msg.originalSlot);
                container.setOriginalSlot(msg.originalSlot);
            });
        }
    }

}