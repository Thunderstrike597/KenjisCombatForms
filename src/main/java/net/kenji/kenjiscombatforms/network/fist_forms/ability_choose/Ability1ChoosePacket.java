package net.kenji.kenjiscombatforms.network.fist_forms.ability_choose;

import net.kenji.kenjiscombatforms.api.handlers.GlobalFormStrategyHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;


public class Ability1ChoosePacket {
    String abilityOption1;

    public Ability1ChoosePacket(String abilityOption1) {
        this.abilityOption1 = abilityOption1;
    }

    public Ability1ChoosePacket(FriendlyByteBuf buf) {
        this.abilityOption1 = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.abilityOption1);
    }

    public static void handle(Ability1ChoosePacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);

                Form currentForm = FormManager.getInstance().getForm(formData.selectedForm);
                AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());
            }
        });
        ctx.setPacketHandled(true);
    }
}