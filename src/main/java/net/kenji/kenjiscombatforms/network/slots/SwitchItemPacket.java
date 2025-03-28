package net.kenji.kenjiscombatforms.network.slots;

import net.kenji.kenjiscombatforms.api.handlers.CommonEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
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