package net.kenji.kenjiscombatforms.api.handlers;

import net.kenji.kenjiscombatforms.api.handlers.data_handle.SavedDataHandler;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.forms.BasicForm;
import net.kenji.kenjiscombatforms.api.managers.forms.VoidForm;
import net.kenji.kenjiscombatforms.api.managers.forms.WitherForm;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.client_data.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AbilityChangeHandler {

   private static final String NONE = "NONE";
    private static final String NONE2 = "NONE";
    private static final String NONE3 = "NONE";




    private static final AbilityChangeHandler INSTANCE = new AbilityChangeHandler();

    public static AbilityChangeHandler getInstance() {
        return INSTANCE;
    }


    private boolean isAbilitySelected(Player player, String form) {
        AbilityManager.PlayerAbilityData data = AbilityManager.getInstance().getPlayerAbilityData(player);
        return Objects.equals(data.chosenAbility1, form);
    }


    public void setAllAbilities(Player player){
        setFormAbility1(player);
        setFormAbility2(player);
        setFormAbility3(player);
    }


    public void deactivateCurrentAbilities(Player player) {deactivateAbilities(player);}
    public void resetAllAbilityValues(Player player) {
        abilityValuesToReset(player);
    }
    public boolean getAllAbilityValuesReset(Player player) {
        return !getAbilityValues(player);
    }
    public void resetAllChosenAbilities(Player player) {
        resetChosenAbilities(player);
    }



    private void deactivateAbilities(Player player) {
        List<Ability> abilities = AbilityManager.getInstance().getCurrentAbilities(player);
        List<FinalAbility> finalAbilities = AbilityManager.getInstance().getCurrentFinalAbilities(player);

        if (player instanceof ServerPlayer serverPlayer) {
            if (abilities != null) {
                abilities.forEach(ability -> {
                    // Check if the ability is not null before accessing it
                    if (ability != null && ability.getAbilityData(player) != null && ability.getAbilityData(player).isAbilityActive()) {
                        ability.deactivateAbilityOptional(serverPlayer);
                    }
                });
            }

            if (finalAbilities != null) {
                finalAbilities.forEach(finalAbility -> {
                    // Check if the finalAbility is not null before accessing it
                    if (finalAbility != null && finalAbility.getAbilityData(player) != null && finalAbility.getAbilityData(player).isAbilityActive()) {
                        finalAbility.deactivateAbilityOptional(serverPlayer);
                    }
                });
            }
        }
    }
    private Optional<AbstractFormData> getFormData(Player player, String formName) {
        Form form = FormManager.getInstance().getForm(formName);
        return Optional.ofNullable(form).map(f -> f.getFormData(player.getUUID()));
    }



    private void resetChosenAbilities(Player player) {
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        abilityData.chosenAbility1 = NONE;
        abilityData.chosenAbility2 = NONE2;
        abilityData.chosenFinal = NONE3;
    }


    private void abilityValuesToReset(Player player) {
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        Form currentForm = FormManager.getInstance().getForm(formData.selectedForm);
        AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());
        currentFormData.setCurrentStoredAbility1("NONE");
        currentFormData.setCurrentStoredAbility2("NONE");
        currentFormData.setStoredAbility3("NONE");
    }


    private boolean getAbilityValues(Player player) {
        return true;
    }


    public void storeAbility1(Player player, String formName, String ability) {
        getFormData(player, formName).ifPresent(formData -> {
            formData.setCurrentStoredAbility1(ability);
            setFormAbility1(player);
            syncDataToClient(player);
        });
        AbilityManager.getInstance().updatePlayerData(player.getUUID(), AbilityManager.getInstance().getPlayerAbilityData(player));
    }

    public void storeAbility2(Player player, String formName, String ability) {
        getFormData(player, formName).ifPresent(formData -> {
            formData.setCurrentStoredAbility2(ability);
            setFormAbility2(player);
            syncDataToClient(player);
        });
        AbilityManager.getInstance().updatePlayerData(player.getUUID(), AbilityManager.getInstance().getPlayerAbilityData(player));
    }



    public void setAbility1(Player player, FormManager.PlayerFormData formData, AbilityManager.PlayerAbilityData abilityData) {
        Optional.ofNullable(FormManager.getInstance().getForm(formData.selectedForm))
                .ifPresent(form -> {
                    AbstractFormData specificFormData = form.getFormData(player.getUUID());
                    abilityData.chosenAbility1 = Optional.ofNullable(specificFormData.getCurrentStoredAbility1())
                            .orElse("NONE");

                    form.updatePlayerData(player.getUUID(), specificFormData);
                    syncDataToClient(player);
                });

        AbilityManager.getInstance().updatePlayerData(player.getUUID(),
                AbilityManager.getInstance().getPlayerAbilityData(player));
    }

    public void setAbility2(Player player, FormManager.PlayerFormData formData, AbilityManager.PlayerAbilityData abilityData) {
        Optional.ofNullable(FormManager.getInstance().getForm(formData.selectedForm))
                .ifPresent(form -> {
                    AbstractFormData specificFormData = form.getFormData(player.getUUID());
                    abilityData.chosenAbility2 = Optional.ofNullable(specificFormData.getCurrentStoredAbility2())
                            .orElse("NONE");

                    form.updatePlayerData(player.getUUID(), specificFormData);
                    syncDataToClient(player);
                });

        AbilityManager.getInstance().updatePlayerData(player.getUUID(),
                AbilityManager.getInstance().getPlayerAbilityData(player));
    }

    public void setAbility3(Player player, FormManager.PlayerFormData formData, AbilityManager.PlayerAbilityData abilityData) {
        Optional.ofNullable(FormManager.getInstance().getForm(formData.selectedForm))
                .ifPresent(form -> {
                    AbstractFormData specificFormData = form.getFormData(player.getUUID());
                    abilityData.chosenFinal = Optional.ofNullable(specificFormData.getStoredAbility3())
                            .orElse("NONE");

                    form.updatePlayerData(player.getUUID(), specificFormData);
                    syncDataToClient(player);
                });

        AbilityManager.getInstance().updatePlayerData(player.getUUID(),
                AbilityManager.getInstance().getPlayerAbilityData(player));
    }




    private void syncDataToClient(Player player) {
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        Form currentForm = FormManager.getInstance().getForm(formData.selectedForm);
        AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());
        System.out.println("Is Syncing To Client! Ability1: " + abilityData.chosenAbility1 + " Ability2: " + abilityData.chosenAbility2 + " Ability3 :" + abilityData.chosenFinal);
            currentForm.syncDataToClient(player);

    }

    public void storeFormAbility1(Player player, String ability) {
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);
        System.out.println("HasStoredAbility! Ability1: " + ability);
        String form = formData.selectedForm;
        if (player instanceof ServerPlayer serverPlayer) {
            SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
            savedData.updatePlayerData(player.getUUID());

            storeAbility1(player, form, ability);
            savedData.setDirty();
        }
    }
    public void storeFormAbility2(Player player, String ability) {
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);
        System.out.println("HasStoredAbility! Ability2: " + ability);
        String form = formData.selectedForm;
        if (player instanceof ServerPlayer serverPlayer) {
            SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
            savedData.updatePlayerData(player.getUUID());

            storeAbility2(player, form, ability);

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





   public void setFormsAndAbilities(Player player, AbstractFormData currentFormData) {

       AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
       setFormAbility1(player);
       setFormAbility2(player);
       setFormAbility3(player);

       AbilityChangeHandler abilityChangeHandler = AbilityChangeHandler.getInstance();
       abilityChangeHandler.deactivateCurrentAbilities(player);

       syncDataToClient(player);

       AbilityManager.getInstance().updatePlayerData(player.getUUID(), AbilityManager.getInstance().getPlayerAbilityData(player));
   }
}
