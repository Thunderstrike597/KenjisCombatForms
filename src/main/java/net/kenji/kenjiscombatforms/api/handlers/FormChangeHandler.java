package net.kenji.kenjiscombatforms.api.handlers;

import net.kenji.kenjiscombatforms.api.handlers.data_handle.SavedDataHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.managers.forms.*;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.power_form.PowerFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.power_form.PowerFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.power_form.PowerFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.swift_form.SwiftFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.swift_form.SwiftFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.swift_form.SwiftFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.void_form.VoidFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.void_form.VoidFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.wither_form.WitherFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.wither_form.WitherFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.wither_form.WitherFistItem;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.client_data.*;
import net.kenji.kenjiscombatforms.network.fist_forms.form_swap.FormToSwapPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Objects;

public class FormChangeHandler {
    private static final FormChangeHandler INSTANCE = new FormChangeHandler();
    private final FormManager formManager = FormManager.getInstance();

    public static FormChangeHandler getInstance() {
        return INSTANCE;
    }
    public boolean getFormValuesReset(Player player){
        return !hasSelectedForms(player);
    }


    public void resetAllFormValues(Player player) {
        setFormValues(player, "NONE");
    }

    public boolean hasSelectedForms(Player player) {
        FormManager.PlayerFormData data = formManager.getFormData(player);
        syncDataToClient(player);
        List<Form> forms = FormManager.getInstance().getCurrentForms(player);

        return !Objects.equals(forms.get(1).getName(), "NONE") &&
                !Objects.equals(forms.get(2).getName(), "NONE") &&
                !Objects.equals(forms.get(3).getName(), "NONE");
    }

    public void resetCurrentFormLevel(Player player) {
        FormManager.PlayerFormData formData = formManager.getFormData(player);

            resetFormProgress(player);

        syncDataToClient(player);
    }



    private void resetFormProgress(Player player) {
        FormManager.PlayerFormData formData = formManager.getFormData(player);
        AbstractFormData currentFormData = getFormData(player, formData.selectedForm);

        currentFormData.setCurrentFormLevel(FormLevelManager.FormLevel.LEVEL1);
        currentFormData.setCurrentFormXp(0);
        currentFormData.setCurrentFormXpMAX(EpicFightCombatFormsCommon.MAX_FORM_STARTING_XP.get());

        syncDataToClient(player);
    }

    private void setFormValues(Player player, String value) {
        FormManager.PlayerFormData data = formManager.getFormData(player);
        data.form1 = value;
        data.form2 = value;
        data.form3 = value;
        syncDataToClient(player);
    }

    public void setFormOption(Player player, String formOption) {
        FormManager.PlayerFormData data = formManager.getFormData(player);
        formManager.updatePlayerData(player.getUUID(), data);

        if (player instanceof ServerPlayer serverPlayer) {
            if (hasSelectedForms(player)) {
                NetworkHandler.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new FormToSwapPacket(formOption)
                );
            }

            if (Objects.equals(data.form1, "NONE")) {
                data.form1 = formOption;
            } else if (Objects.equals(data.form2, "NONE")) {
                data.form2 = formOption;
            } else if (Objects.equals(data.form3, "NONE")) {
                data.form3 = formOption;
            }


            updateSavedData(serverPlayer);
        }
    }


    public void setFormSwapOption(Player player, int slot, String formOption) {
        FormManager.PlayerFormData data = formManager.getFormData(player);
        AbstractFormData formData = getFormData(player, formOption);
        formManager.updatePlayerData(player.getUUID(), data);
        switch (slot) {
            case 1 -> data.form1 = formOption;
            case 2 -> data.form2 = formOption;
            case 3 -> data.form3 = formOption;
        }

        setSelectedForm(player, formOption);
        AbilityChangeHandler.getInstance().setFormsAndAbilities(player, formData);
        syncDataToClient(player);
    }

    public void setSelectedForm(Player player, String form) {
        FormManager.PlayerFormData data = formManager.getFormData(player);
        data.selectedForm = form;
        AbstractFormData formData = getFormData(player, form);

        AbilityChangeHandler.getInstance().setFormsAndAbilities(player, formData);
        syncDataToClient(player);
    }

    private AbstractFormData getFormData(Player player, String form) {
        return formManager.getForm(form).getFormData(player.getUUID());
    }

    public boolean isFormSelected(Player player, String form) {
        return Objects.equals(formManager.getFormData(player).selectedForm, form);
    }

    public boolean getBasicSelected(Player player) {
        return isFormSelected(player, BasicForm.getInstance().getName());
    }

    public boolean getVoidSelected(Player player) {
        return isFormSelected(player, VoidForm.getInstance().getName());
    }

    public boolean getWitherSelected(Player player) {
        return isFormSelected(player, WitherForm.getInstance().getName());
    }

    public boolean getSwiftSelected(Player player) {
        return isFormSelected(player, SwiftForm.getInstance().getName());
    }

    public boolean getPowerSelected(Player player) {
        return isFormSelected(player, PowerForm.getInstance().getName());
    }

    private void syncDataToClient(Player player) {
        List<Form> formValue = FormManager.getInstance().getCurrentForms(player);
        List<AbstractFormData> formData = FormManager.getInstance().getCurrentFormData(player);


        Form currentForm = FormManager.getInstance().getForm(formValue.get(0).getName());
        AbstractFormData currentFormData = formData.get(0);


        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new SyncClientFormsPacket(formValue.get(1).getName(), formValue.get(2).getName(), formValue.get(3).getName(),
                            currentFormData.getCurrentFormLevel(), currentFormData.getCurrentFormXp(),
                            currentFormData.getCurrentFormXpMAX())
            );
          currentForm.syncDataToClient(player);
        }
    }

    private void updateSavedData(ServerPlayer serverPlayer) {
        SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
        savedData.updatePlayerData(serverPlayer.getUUID());
        savedData.setDirty();
    }

    public void setSelectedFormChanged(ServerPlayer player, int slot){
        List<Form> formValue = FormManager.getInstance().getCurrentForms(player);
        Form currentForm = FormManager.getInstance().getForm(formValue.get(0).getName());

           currentForm.setCurrentForm(player, slot);
    }
    public void removeCurrentFormItem(Player player, int slot){
        player.getMainHandItem().isEmpty();
    }
}
