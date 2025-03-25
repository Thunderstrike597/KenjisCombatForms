package net.kenji.kenjiscombatforms.api.handlers;

import net.kenji.kenjiscombatforms.api.handlers.data_handle.SavedDataHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.managers.forms.*;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
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
import net.kenji.kenjiscombatforms.network.capability.SyncRemovedNBTPacket;
import net.kenji.kenjiscombatforms.network.fist_forms.client_data.*;
import net.kenji.kenjiscombatforms.network.fist_forms.form_swap.FormToSwapPacket;
import net.kenji.kenjiscombatforms.network.slots.SwitchItemPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

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
        setFormValues(player, FormManager.FormSelectionOption.NONE);
    }

    public boolean hasSelectedForms(Player player) {
        FormManager.PlayerFormData data = formManager.getFormData(player);
        syncDataToClient(player);
        return data.form1 != FormManager.FormSelectionOption.NONE ||
                data.form2 != FormManager.FormSelectionOption.NONE ||
                data.form3 != FormManager.FormSelectionOption.NONE;
    }

    public void resetCurrentFormLevel(Player player) {
        FormManager.PlayerFormData formData = formManager.getFormData(player);
        if (shouldResetForm(formData.selectedForm)) {
            resetFormProgress(player);
        }
        syncDataToClient(player);
    }

    private boolean shouldResetForm(FormManager.FormSelectionOption form) {
        return switch (form) {
            case BASIC, VOID, WITHER, SWIFT, POWER -> true;
            default -> false;
        };
    }

    private void resetFormProgress(Player player) {
        FormManager.PlayerFormData formData = formManager.getFormData(player);
        AbstractFormData currentFormData = getFormData(player, formData.selectedForm);

        currentFormData.setCurrentFormLevel(FormLevelManager.FormLevel.LEVEL1);
        currentFormData.setCurrentFormXp(0);
        currentFormData.setCurrentFormXpMAX(KenjisCombatFormsCommon.MAX_FORM_STARTING_XP.get());

        syncDataToClient(player);
    }

    private void setFormValues(Player player, FormManager.FormSelectionOption value) {
        FormManager.PlayerFormData data = formManager.getFormData(player);
        data.form1 = value;
        data.form2 = value;
        data.form3 = value;
        syncDataToClient(player);
    }

    public void setFormOption(Player player, FormManager.FormSelectionOption formOption) {
        FormManager.PlayerFormData data = formManager.getFormData(player);
        formManager.updatePlayerData(player.getUUID(), data);

        if (player instanceof ServerPlayer serverPlayer) {
            if (hasSelectedForms(player)) {
                NetworkHandler.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new FormToSwapPacket(formOption.name())
                );
            }

            if (data.form1 == FormManager.FormSelectionOption.NONE) {
                data.form1 = formOption;
            } else if (data.form2 == FormManager.FormSelectionOption.NONE) {
                data.form2 = formOption;
            } else if (data.form3 == FormManager.FormSelectionOption.NONE) {
                data.form3 = formOption;
            }

            updateSavedData(serverPlayer);
        }
    }


    public void setFormSwapOption(Player player, int slot, FormManager.FormSelectionOption formOption) {
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

    public void setSelectedForm(Player player, FormManager.FormSelectionOption form) {
        FormManager.PlayerFormData data = formManager.getFormData(player);
        data.selectedForm = form;
        AbstractFormData formData = getFormData(player, form);

        AbilityChangeHandler.getInstance().setFormsAndAbilities(player, formData);
        syncDataToClient(player);
    }

    private AbstractFormData getFormData(Player player, FormManager.FormSelectionOption form) {
        return formManager.getForm(form.name()).getFormData(player.getUUID());
    }

    public boolean isFormSelected(Player player, FormManager.FormSelectionOption form) {
        return formManager.getFormData(player).selectedForm == form;
    }

    public boolean getBasicSelected(Player player) {
        return isFormSelected(player, FormManager.FormSelectionOption.BASIC);
    }

    public boolean getVoidSelected(Player player) {
        return isFormSelected(player, FormManager.FormSelectionOption.VOID);
    }

    public boolean getWitherSelected(Player player) {
        return isFormSelected(player, FormManager.FormSelectionOption.WITHER);
    }

    public boolean getSwiftSelected(Player player) {
        return isFormSelected(player, FormManager.FormSelectionOption.SWIFT);
    }

    public boolean getPowerSelected(Player player) {
        return isFormSelected(player, FormManager.FormSelectionOption.POWER);
    }

    private void syncDataToClient(Player player) {
        FormManager.PlayerFormData formData = formManager.getFormData(player);
        AbstractFormData currentFormData = getFormData(player, formData.selectedForm);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);

        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new SyncClientFormsPacket(formData.form1, formData.form2, formData.form3,
                            currentFormData.getCurrentFormLevel(), currentFormData.getCurrentFormXp(),
                            currentFormData.getCurrentFormXpMAX())
            );
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new SyncClientAbilityPacket(abilityData.ability1, abilityData.ability2, abilityData.ability3,
                            abilityData.chosenAbility1, abilityData.chosenAbility2, abilityData.chosenFinal,
                            formData.selectedForm)
            );
        }
    }

    private void updateSavedData(ServerPlayer serverPlayer) {
        SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
        savedData.updatePlayerData(serverPlayer.getUUID());
        savedData.setDirty();
    }


    public void setSelectedFormChanged(ServerPlayer player, int slot){
        LevelHandler levelHandler = LevelHandler.getInstance();


            if (basicSelected(player)) {
                setBasicFistForm(player, slot);
            } else if (voidSelected(player)) {
                setVoidFistForm(player, slot);
            } else if (witherSelected(player)) {
                setWitherFistForm(player, slot);
            } else if (swiftSelected(player)) {
                setSwiftFistForm(player, slot);
            } else if (powerSelected(player)) {
                setPowerFistForm(player, slot);
            }
    }
    public void removeCurrentFormItem(Player player, int slot){
        player.getMainHandItem().isEmpty();
    }


    private static boolean basicSelected(Player player){
        return FormChangeHandler.getInstance().getBasicSelected(player);
    }
    private static boolean voidSelected(Player player){
        return FormChangeHandler.getInstance().getVoidSelected(player);
    }
    private static boolean witherSelected(Player player){
        return FormChangeHandler.getInstance().getWitherSelected(player);
    }
    private static boolean swiftSelected(Player player){
        return FormChangeHandler.getInstance().getSwiftSelected(player);
    }
    private static boolean powerSelected(Player player){
        return FormChangeHandler.getInstance().getPowerSelected(player);
    }


    private static boolean isNearItem(Player player){
        return CommonEventHandler.getInstance().getIsNearItem(player);
    }

    private static void setBasicFistForm(ServerPlayer player, int slot){
        BasicFistItem basicFistItem = BasicFistItem.getInstance();
        BasicFist2Item basicFist2Item = BasicFist2Item.getInstance();
        BasicFist3Item basicFist3Item = BasicFist3Item.getInstance();

        AbstractFormData basicFormData = BasicForm.getInstance().getFormData(player.getUUID());

        if (basicFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
            basicFistItem.setFormMainHand(player, slot);
        }
        else if (basicFormData.getCurrentFormLevel()  == FormLevelManager.FormLevel.LEVEL2) {
            basicFist2Item.setFormMainHand(player, slot);
        }
        else if (basicFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL3) {
            basicFist3Item.setFormMainHand(player, slot);
        }
    }



    private static void setVoidFistForm(ServerPlayer player, int slot){
        VoidFistItem voidFistItem = VoidFistItem.getInstance();
        VoidFist2Item voidFist2Item = VoidFist2Item.getInstance();
        VoidFist2Item voidFist3Item = VoidFist2Item.getInstance();

        AbstractFormData voidFormData = VoidForm.getInstance().getFormData(player.getUUID());

        if (voidFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
            voidFistItem.setVoidFormMainHand(player, slot);
        }
        else if (voidFormData.getCurrentFormLevel()  == FormLevelManager.FormLevel.LEVEL2) {
            voidFist2Item.setVoidFormMainHand(player, slot);
        }
        else if (voidFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL3) {
            voidFist3Item.setVoidFormMainHand(player, slot);
        }
    }
    private static void setWitherFistForm(ServerPlayer player, int slot){
        WitherFistItem witherFistItem = WitherFistItem.getInstance();
        WitherFist2Item witherFist2Item = WitherFist2Item.getInstance();
        WitherFist3Item witherFist3Item = WitherFist3Item.getInstance();

        AbstractFormData witherFormData = WitherForm.getInstance().getFormData(player.getUUID());

        if (witherFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
            witherFistItem.setWitherFormMainHand(player, slot);
        }
        else if (witherFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
            witherFist2Item.setWitherFormMainHand(player, slot);
        }
        else if (witherFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL3) {
            witherFist3Item.setWitherFormMainHand(player, slot);
        }
    }
    private static void setSwiftFistForm(ServerPlayer player, int slot){
        SwiftFistItem fistItem = SwiftFistItem.getInstance();
        SwiftFist2Item fist2Item = SwiftFist2Item.getInstance();
        SwiftFist3Item fist3Item = SwiftFist3Item.getInstance();

        AbstractFormData swiftFormData = SwiftForm.getInstance().getFormData(player.getUUID());

        if (swiftFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
            fistItem.setFormMainHand(player, slot);
        }
        else if (swiftFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
            fist2Item.setFormMainHand(player, slot);
        }
        else if (swiftFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL3) {
            fist3Item.setFormMainHand(player, slot);
        }
    }
    private static void setPowerFistForm(ServerPlayer player, int slot){
        PowerFistItem fistItem = PowerFistItem.getInstance();
        PowerFist2Item fist2Item = PowerFist2Item.getInstance();
        PowerFist3Item fist3Item = PowerFist3Item.getInstance();

        AbstractFormData powerFormData = PowerForm.getInstance().getFormData(player.getUUID());

        if (powerFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
            fistItem.setFormMainHand(player, slot);
        }
        else if (powerFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
            fist2Item.setFormMainHand(player, slot);
        }
        else if (powerFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL3) {
            fist3Item.setFormMainHand(player, slot);
        }
    }
}
