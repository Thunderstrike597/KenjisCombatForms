package net.kenji.kenjiscombatforms.network.fist_forms.form_swap;

import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.screen.form_menu.FormChooseMenu;
import net.kenji.kenjiscombatforms.screen.form_menu.FormSwapMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;


public class FormToSwapPacket {

    String formToSwap;

    public FormToSwapPacket(String formToSwap) {
        this.formToSwap = formToSwap;
    }

    public FormToSwapPacket(FriendlyByteBuf buf) {
        formToSwap = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.formToSwap);
    }

    public static void handle(FormToSwapPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
               System.out.println("HAS RECEIVED PACKET");
                if(ctx.getDirection().getReceptionSide().isClient()){
                setSwapFormScreen(msg);
                System.out.println("HAS ATTEMPTED TO SET SCREEN");
            }
        });
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void setSwapFormScreen(FormToSwapPacket msg){
        Minecraft.getInstance().setScreen(new FormSwapMenu(Component.translatable("screen.kenjiscombatforms.swap_form_screen")));
        FormSwapMenu.setFormToSwap(msg.formToSwap);
    }
}