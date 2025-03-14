package net.kenji.kenjiscombatforms.api.handlers;

import net.kenji.kenjiscombatforms.api.handlers.data_handle.SavedDataHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.api.managers.forms.BasicForm;
import net.kenji.kenjiscombatforms.api.managers.forms.SwiftForm;
import net.kenji.kenjiscombatforms.api.managers.forms.VoidForm;
import net.kenji.kenjiscombatforms.api.managers.forms.WitherForm;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.interfaces.form.FormAbilityStrategy;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.client_data.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class AbilityChangeHandler {

   private static final AbilityManager.AbilityOption1 NONE = AbilityManager.AbilityOption1.NONE;
    private static final AbilityManager.AbilityOption2 NONE2 = AbilityManager.AbilityOption2.NONE;
    private static final AbilityManager.AbilityOption3 NONE3 = AbilityManager.AbilityOption3.NONE;
    private static final FormManager.FormSelectionOption BASIC = FormManager.FormSelectionOption.BASIC;
    private static final FormManager.FormSelectionOption VOID = FormManager.FormSelectionOption.VOID;
    private static final FormManager.FormSelectionOption WITHER = FormManager.FormSelectionOption.WITHER;





    private static final AbilityChangeHandler INSTANCE = new AbilityChangeHandler();

    public static AbilityChangeHandler getInstance() {
        return INSTANCE;
    }

    public boolean getVoid1Selected(Player player){
        return isAbilitySelected(player, AbilityManager.AbilityOption1.VOID_ABILITY1);
    }
    public boolean getWither1Selected(Player player){
        return isAbilitySelected(player, AbilityManager.AbilityOption1.WITHER_ABILITY1);
    }

    private boolean isAbilitySelected(Player player, AbilityManager.AbilityOption1 form) {
        AbilityManager.PlayerAbilityData data = AbilityManager.getInstance().getPlayerAbilityData(player);
        return data.chosenAbility1 == form;
    }


    public void setAllAbilities(Player player){
        setFormAbility1(player);
        setFormAbility2(player);
        setFormAbility3(player);
    }


    public void resetAllAbilityValues(Player player) {
        abilityValuesToReset(player);
    }

    public boolean getAllAbilityValuesReset(Player player) {
        return !getAbilityValues(player);
    }

    public void resetAllChosenAbilities(Player player) {
        resetChosenAbilities(player);
    }





    private void resetChosenAbilities(Player player) {
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        abilityData.chosenAbility1 = NONE;
        abilityData.chosenAbility2 = NONE2;
        abilityData.chosenFinal = NONE3;
    }


    private void abilityValuesToReset(Player player) {
        AbstractFormData voidFormData = VoidForm.getInstance().getFormData(player.getUUID());
        AbstractFormData witherFormData = WitherForm.getInstance().getFormData(player.getUUID());

        voidFormData.setCurrentStoredAbility1(AbilityManager.AbilityOption1.NONE);
        voidFormData.setCurrentStoredAbility2(AbilityManager.AbilityOption2.NONE);
        witherFormData.setCurrentStoredAbility1(AbilityManager.AbilityOption1.NONE);
        witherFormData.setCurrentStoredAbility2(AbilityManager.AbilityOption2.NONE);
    }


    private boolean getAbilityValues(Player player) {
        return true;
    }



    public void storeAbility1(Player player, String formName, AbilityManager.AbilityOption1 ability) {
        Form form = FormManager.getInstance().getForm(formName);
        if (form != null) {
            AbstractFormData formData = form.getFormData(player.getUUID());
            formData.setCurrentStoredAbility1(ability);

            setFormAbility1(player);
            form.updatePlayerData(player.getUUID(), formData);
            form.syncDataToClient(player);
        }
        AbilityManager.getInstance().updatePlayerData(player.getUUID(), AbilityManager.getInstance().getPlayerAbilityData(player));
    }

    public void storeAbility2(Player player, String formName, AbilityManager.AbilityOption2 ability) {
        Form form = FormManager.getInstance().getForm(formName);
        if (form != null) {
            AbstractFormData formData = form.getFormData(player.getUUID());
            formData.setCurrentStoredAbility2(ability);

            setFormAbility2(player);
            form.updatePlayerData(player.getUUID(), formData);
            form.syncDataToClient(player);
        }
        AbilityManager.getInstance().updatePlayerData(player.getUUID(), AbilityManager.getInstance().getPlayerAbilityData(player));
    }



    public void setAbility1(Player player, FormManager.PlayerFormData formData, AbilityManager.PlayerAbilityData abilityData) {
        Form form = FormManager.getInstance().getForm(formData.selectedForm.name());
        if (form != null) {
            AbstractFormData specificFormData = form.getFormData(player.getUUID());
            AbilityManager.AbilityOption1 storedAbility = specificFormData.getCurrentStoredAbility1();

            if (storedAbility != null) {
                abilityData.ability1 = storedAbility;
                form.updatePlayerData(player.getUUID(), specificFormData);
                form.syncDataToClient(player);
            }
        }


        AbilityManager.getInstance().updatePlayerData(player.getUUID(), AbilityManager.getInstance().getPlayerAbilityData(player));
    }

    public void setAbility2(Player player, FormManager.PlayerFormData formData, AbilityManager.PlayerAbilityData abilityData) {
        Form form = FormManager.getInstance().getForm(formData.selectedForm.name());
        if (form != null) {
            AbstractFormData specificFormData = form.getFormData(player.getUUID());
            AbilityManager.AbilityOption2 storedAbility = specificFormData.getCurrentStoredAbility2();

            if (storedAbility != null) {
                abilityData.ability2 = storedAbility;
                form.updatePlayerData(player.getUUID(), specificFormData);
                form.syncDataToClient(player);
            }
        }

        AbilityManager.getInstance().updatePlayerData(player.getUUID(), AbilityManager.getInstance().getPlayerAbilityData(player));
    }

    public void setAbility3(Player player, FormManager.PlayerFormData formData, AbilityManager.PlayerAbilityData abilityData) {
        Form form = FormManager.getInstance().getForm(formData.selectedForm.name());
        if (form != null) {
            AbstractFormData specificFormData = form.getFormData(player.getUUID());
            AbilityManager.AbilityOption3 storedAbility = specificFormData.getStoredAbility3();

            if (storedAbility != null) {
                abilityData.ability3 = storedAbility;
                form.updatePlayerData(player.getUUID(), specificFormData);
                form.syncDataToClient(player);
            }
        }

        AbilityManager.getInstance().updatePlayerData(player.getUUID(), AbilityManager.getInstance().getPlayerAbilityData(player));
    }




    private void syncDataToClient(Player player) {
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        Form currentForm = FormManager.getInstance().getForm(formData.selectedForm.name());
        AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());
       if(player instanceof ServerPlayer serverPlayer) {
           NetworkHandler.INSTANCE.send(
                   PacketDistributor.PLAYER.with(() -> serverPlayer),
                   new SyncClientAbilityPacket(abilityData.ability1, abilityData.ability2, abilityData.ability3, abilityData.chosenAbility1, abilityData.chosenAbility2, abilityData.chosenFinal, formData.selectedForm)
           );
       }
    }

    public void storeFormAbility1(Player player, AbilityManager.AbilityOption1 ability) {
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);

        String form = formData.selectedForm.name();
        if (player instanceof ServerPlayer serverPlayer) {
            SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
            savedData.updatePlayerData(player.getUUID());

            storeAbility1(player, form, ability);

            reValueAbility1(player, ability);
            savedData.setDirty();
        }
    }
    public void storeFormAbility2(Player player, AbilityManager.AbilityOption2 ability) {
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);
        String form = formData.selectedForm.name();
        if (player instanceof ServerPlayer serverPlayer) {
            SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
            savedData.updatePlayerData(player.getUUID());



            storeAbility2(player, form, ability);

            reValueAbility2(player, ability);
            savedData.setDirty();
        }
    }

    public void setFormAbility1(Player player){
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);

        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        setAbility1(player, formData, abilityData);
    }
    public void setFormAbility2(Player player){
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);

        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        setAbility2(player, formData, abilityData);
    }
    public void setFormAbility3(Player player){
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);

        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        setAbility3(player, formData, abilityData);
    }

    public void reValueAbility1(Player player, AbilityManager.AbilityOption1 ability1){
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);

        if(abilityData.chosenAbility1 != AbilityManager.AbilityOption1.NONE){
            GlobalFormStrategyHandler.getInstance().setChosenAbility1(player, ability1);
            GlobalFormStrategyHandler.getInstance().setPreviouslyChosenAbility1(player, ability1);
        }
    }
    public void reValueAbility2(Player player, AbilityManager.AbilityOption2 ability2){
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);


        if(abilityData.chosenAbility2 != AbilityManager.AbilityOption2.NONE){
            GlobalFormStrategyHandler.getInstance().setChosenAbility2(player, ability2);
            GlobalFormStrategyHandler.getInstance().setPreviouslyChosenAbility2(player, ability2);
        }
    }



   public void setFormsAndAbilities(Player player, AbstractFormData currentFormData) {

       AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
       setFormAbility1(player);
       setFormAbility2(player);
       setFormAbility3(player);
       setAbilities(player, currentFormData, abilityData);
       syncDataToClient(player);

       AbilityManager.getInstance().updatePlayerData(player.getUUID(), AbilityManager.getInstance().getPlayerAbilityData(player));
   }

   private void setAbilities(Player player, AbstractFormData currentFormData, AbilityManager.PlayerAbilityData data) {
        GlobalFormStrategyHandler.getInstance().setStoredChosenAbilities(player, currentFormData.getPreviousAbility1(), currentFormData.getPreviousAbility2(), currentFormData.getPreviousAbility3());
    }
}
