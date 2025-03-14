package net.kenji.kenjiscombatforms.api.handlers;

import net.kenji.kenjiscombatforms.api.handlers.data_handle.SavedDataHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.managers.forms.BasicForm;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.client_data.*;
import net.kenji.kenjiscombatforms.network.fist_forms.form_swap.FormToSwapPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class FormChangeHandler {
    private static final FormChangeHandler INSTANCE = new FormChangeHandler();
    FormManager formManager = FormManager.getInstance();

    public static FormChangeHandler getInstance(){
        return INSTANCE;
    }



    public void resetAllFormValues(Player player){
        setFormValues(player, FormManager.FormSelectionOption.NONE);
    }

    public boolean getFormValuesReset(Player player){
        return !formValues(player);
    }

    public void resetCurrentFormLevel(Player player){
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        Form currentForm = FormManager.getInstance().getForm(formData.selectedForm.name());
        AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());

        switch (formData.selectedForm){
            case BASIC, VOID, WITHER, SWIFT, POWER -> formValuesToReset(player);
        }
        syncDataToClient(player, currentForm);
    }



    private void formValuesToReset(Player player) {
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);
        Form currentForm = FormManager.getInstance().getForm(formData.selectedForm.name());
        AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());


          currentFormData.setCurrentFormLevel(FormLevelManager.FormLevel.LEVEL1);
          currentFormData.setCurrentFormXp(0);
          currentFormData.setCurrentFormXpMAX(KenjisCombatFormsCommon.MAX_FORM_STARTING_XP.get());

        syncDataToClient(player, currentForm);
    }

    private boolean formValues(Player player){
        FormManager.PlayerFormData data = formManager.getFormData(player);
        Form currentForm = FormManager.getInstance().getForm(data.selectedForm.name());

        syncDataToClient(player, currentForm);
        return data.form1 != FormManager.FormSelectionOption.NONE || data.form2 != FormManager.FormSelectionOption.NONE || data.form3 != FormManager.FormSelectionOption.NONE;
    }

    private void setFormValues(Player player, FormManager.FormSelectionOption value){
        FormManager.PlayerFormData data = formManager.getFormData(player);
        Form currentForm = FormManager.getInstance().getForm(data.selectedForm.name());

        data.form1 = value;
        data.form2 = value;
        data.form3 = value;
        syncDataToClient(player, currentForm);
    }

    private void setSelectedForm(Player player, FormManager.FormSelectionOption form) {


        FormManager.PlayerFormData data = formManager.getFormData(player);
        data.selectedForm = form;
        Form currentForm = FormManager.getInstance().getForm(form.name());
        AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());

        AbilityChangeHandler.getInstance().setFormsAndAbilities(player, currentFormData);
        syncDataToClient(player, currentForm);
    }

    private boolean isFormSelected(Player player, FormManager.FormSelectionOption form) {
        FormManager.PlayerFormData data = formManager.getFormData(player);
        Form currentForm = FormManager.getInstance().getForm(form.name());

        syncDataToClient(player, currentForm);
        return data.selectedForm == form;
    }

    public void setFormOption(Player player, FormManager.FormSelectionOption formOption) {
        FormManager.PlayerFormData data = formManager.getFormData(player);
        FormManager.getInstance().updatePlayerData(player.getUUID(), FormManager.getInstance().getFormData(player));
        if (player instanceof ServerPlayer serverPlayer) {
            if(data.form1 != FormManager.FormSelectionOption.NONE && data.form2 != FormManager.FormSelectionOption.NONE && data.form3 != FormManager.FormSelectionOption.NONE){
                NetworkHandler.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new FormToSwapPacket(formOption.name())
                );
            }

           if(data.form1 == FormManager.FormSelectionOption.NONE){
               data.form1 = formOption;
           }
           else if(data.form2 == FormManager.FormSelectionOption.NONE){
               data.form2 = formOption;
           }
           else if(data.form3 == FormManager.FormSelectionOption.NONE){
               data.form3 = formOption;
           }



            SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
            savedData.updatePlayerData(player.getUUID());

            savedData.setDirty();
        }
    }
    public void setForm1SwapOption(Player player, FormManager.FormSelectionOption formOption) {
        FormManager.PlayerFormData data = formManager.getFormData(player);
        Form currentForm = FormManager.getInstance().getForm(formOption.name());
        AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());


        FormManager.getInstance().updatePlayerData(player.getUUID(), FormManager.getInstance().getFormData(player));
        data.form1 = formOption;
        setSelectedForm(player, formOption);
        AbilityChangeHandler.getInstance().setFormsAndAbilities(player, currentFormData);
        syncDataToClient(player, currentForm);
    }
    public void setForm2SwapOption(Player player, FormManager.FormSelectionOption formOption) {
        FormManager.PlayerFormData data = formManager.getFormData(player);
        Form currentForm = FormManager.getInstance().getForm(formOption.name());
        AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());

        FormManager.getInstance().updatePlayerData(player.getUUID(), FormManager.getInstance().getFormData(player));
        data.form2 = formOption;
        setSelectedForm(player, formOption);
        AbilityChangeHandler.getInstance().setFormsAndAbilities(player, currentFormData);
        syncDataToClient(player, currentForm);
    }
    public void setForm3SwapOption(Player player, FormManager.FormSelectionOption formOption) {
        FormManager.PlayerFormData data = formManager.getFormData(player);
        Form currentForm = FormManager.getInstance().getForm(formOption.name());
        AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());

        FormManager.getInstance().updatePlayerData(player.getUUID(), FormManager.getInstance().getFormData(player));
        data.form3 = formOption;
        setSelectedForm(player, formOption);
        AbilityChangeHandler.getInstance().setFormsAndAbilities(player, currentFormData);
        syncDataToClient(player, currentForm);
    }


    public boolean getVoidSelected(Player player){
        return isFormSelected(player, FormManager.FormSelectionOption.VOID);
    }
    public boolean getWitherSelected(Player player){
        return isFormSelected(player, FormManager.FormSelectionOption.WITHER);
    }
    public boolean getBasicSelected(Player player){
        return isFormSelected(player, FormManager.FormSelectionOption.BASIC);
    }
    public boolean getSwiftSelected(Player player){
        return isFormSelected(player, FormManager.FormSelectionOption.SWIFT);
    }
    public boolean getPowerSelected(Player player){
        return isFormSelected(player, FormManager.FormSelectionOption.POWER);
    }

    public void chooseForm1(Player player, FormManager.FormSelectionOption form){
        setSelectedForm(player, form);
        if(player instanceof ServerPlayer serverPlayer){
        SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
        savedData.updatePlayerData(player.getUUID());

        savedData.setDirty();
        }
    }

    public void chooseForm2(Player player, FormManager.FormSelectionOption form){
        setSelectedForm(player, form);
        if(player instanceof ServerPlayer serverPlayer){
            SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
            savedData.updatePlayerData(player.getUUID());

            savedData.setDirty();
        }
    }
    public void chooseForm3(Player player, FormManager.FormSelectionOption form){
        setSelectedForm(player, form);
        if(player instanceof ServerPlayer serverPlayer){
            SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
            savedData.updatePlayerData(player.getUUID());

            savedData.setDirty();
        }
    }


    public void chooseBasicForm(Player player){
        FormManager.PlayerFormData data = formManager.getFormData(player);
        Form basicForm = FormManager.getInstance().getForm(BasicForm.getInstance().getName());
        AbstractFormData basicFormData = basicForm.getFormData(player.getUUID());

        setSelectedForm(player, FormManager.FormSelectionOption.BASIC);


        AbilityChangeHandler.getInstance().setFormsAndAbilities(player, basicFormData);
    }

    private void syncDataToClient(Player player, Form currentForm) {
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());
        if(player instanceof ServerPlayer serverPlayer) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new SyncClientFormsPacket(formData.form1, formData.form2, formData.form3, currentFormData.getCurrentFormLevel(), currentFormData.getCurrentFormXp(),currentFormData.getCurrentFormXpMAX())
            );
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new SyncClientAbilityPacket(abilityData.ability1, abilityData.ability2, abilityData.ability3, abilityData.chosenAbility1, abilityData.chosenAbility2, abilityData.chosenFinal, formData.selectedForm)
            );
        }
    }
}
