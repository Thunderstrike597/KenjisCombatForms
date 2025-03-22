package net.kenji.kenjiscombatforms.network.slots;

import net.kenji.kenjiscombatforms.api.capabilities.ExtraContainerCapability;
import net.kenji.kenjiscombatforms.api.handlers.ClientEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.CommonEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.api.powers.VoidPowers.EnderFormAbility;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherFormAbility;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.EnderFormItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.WitherFormItem;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
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


                player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
                    int selectedSlot = player.getInventory().selected;
                    if (ctx.getDirection().getReceptionSide().isClient()) {
                        container.setStoredItem(msg.storedItem);
                        if(ClientWitherData.getIsWitherActive()) {
                            player.getInventory().setItem(selectedSlot, WitherFormItem.getInstance().getDefaultInstance());
                        }
                        if(ClientVoidData.getIsEnderActive()) {
                            player.getInventory().setItem(selectedSlot, EnderFormItem.getInstance().getDefaultInstance());
                        }
                        else if(player.getInventory().getItem(msg.originalSlot).isEmpty()){
                            formChangeHandler.setSelectedFormChanged(player, selectedSlot);
                            player.getInventory().setChanged();
                        }
                    }
                    if (ctx.getDirection().getReceptionSide().isServer()) {
                        if (!EnderFormAbility.getInstance().getEnderFormActive(player) && !WitherFormAbility.getInstance().getWitherFormActive(player)) {

                            ItemStack currentItem = player.getInventory().getItem(selectedSlot);
                            if (!currentItem.isEmpty() && container.getStoredItem().isEmpty() && !(currentItem.getItem() instanceof BaseFistClass)) {
                                container.setStoredItem(currentItem.copy());
                                player.getInventory().setItem(selectedSlot, ItemStack.EMPTY);
                                CommonEventHandler.getInstance().setOriginalSlot(msg.originalSlot);
                                formChangeHandler.setSelectedFormChanged(player, selectedSlot);
                                player.getInventory().setChanged();

                                CompoundTag nbt = player.getPersistentData();
                                nbt.put("storedItem", msg.storedItem.serializeNBT());


                            } else if (currentItem.isEmpty() && container.getStoredItem().isEmpty()) {
                                CommonEventHandler.getInstance().setOriginalSlot(msg.originalSlot);

                                formChangeHandler.setSelectedFormChanged(player, selectedSlot);
                                player.getInventory().setChanged();
                            }
                        }
                    }
                });
            }
        });
        ctx.setPacketHandled(true);
    }
}