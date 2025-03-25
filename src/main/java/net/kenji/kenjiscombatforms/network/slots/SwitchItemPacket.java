package net.kenji.kenjiscombatforms.network.slots;

import net.kenji.kenjiscombatforms.api.capabilities.ExtraContainerCapability;
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

    public SwitchItemPacket(int originalSlot){
        this.originalSlot = originalSlot;
    }

    public SwitchItemPacket(FriendlyByteBuf buf) {
        originalSlot = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.originalSlot);
    }

    public SwitchItemPacket decode(FriendlyByteBuf buf) {
        return new SwitchItemPacket(this.originalSlot);
    }

    public static void handle(SwitchItemPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {

            ServerPlayer player = ctx.getSender();


            if (player != null) {
                FormChangeHandler formChangeHandler = FormChangeHandler.getInstance();
                CommonEventHandler commonEventHandler = CommonEventHandler.getInstance();
                commonEventHandler.setOriginalSlot(player, msg.originalSlot);

            }
        });
        ctx.setPacketHandled(true);
    }
}