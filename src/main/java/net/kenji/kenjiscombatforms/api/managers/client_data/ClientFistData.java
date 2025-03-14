package net.kenji.kenjiscombatforms.api.managers.client_data;

import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.minecraft.world.entity.player.Player;

public class ClientFistData {

    private static AbstractFormData specificFormData;
    private static final FormManager.PlayerFormData clientFormData = new FormManager.PlayerFormData();
    private static final AbilityManager.PlayerAbilityData clientAbilityData = new AbilityManager.PlayerAbilityData();



    public static AbilityManager.AbilityOption1 getCurrentAbility1() {
        return clientAbilityData.ability1;
    }
    public static AbilityManager.AbilityOption2 getCurrentAbility2() {
        return clientAbilityData.ability2;
    }
    public static AbilityManager.AbilityOption3 getCurrentAbility3() {
        return clientAbilityData.ability3;
    }
    public static FormManager.FormSelectionOption getSelectedForm() {
        return clientFormData.selectedForm;
    }

    public static AbilityManager.AbilityOption1 getChosenAbility1() {
        return clientAbilityData.chosenAbility1;
    }
    public static AbilityManager.AbilityOption2 getChosenAbility2() {
        return clientAbilityData.chosenAbility2;
    }
    public static AbilityManager.AbilityOption3 getChosenAbility3() {
        return clientAbilityData.chosenFinal;
    }

    public static FormManager.FormSelectionOption getForm1Option(){
        return clientFormData.form1;
    }
    public static FormManager.FormSelectionOption getForm2Option(){
        return clientFormData.form2;
    }
    public static FormManager.FormSelectionOption getForm3Option(){
        return clientFormData.form3;
    }
    public static void setChosenAbility1(AbilityManager.AbilityOption1 value) {
        clientAbilityData.chosenAbility1 = value;
    }
    public static void setChosenAbility2(AbilityManager.AbilityOption2 value) {
        clientAbilityData.chosenAbility2 = value;
    }
    public static void setChosenAbility3(AbilityManager.AbilityOption3 value) {
        clientAbilityData.chosenFinal = value;
    }
    public static void setCurrentAbility1(AbilityManager.AbilityOption1 value) {
        clientAbilityData.ability1 = value;
    }
    public static void setCurrentAbility2(AbilityManager.AbilityOption2 value) {
        clientAbilityData.ability2 = value;
    }

    public static void setCurrentAbility3(AbilityManager.AbilityOption3 currentAbility3) {
        clientAbilityData.ability3 = currentAbility3;
    }
    public static void setSelectedForm(FormManager.FormSelectionOption selectedForm) {
        clientFormData.selectedForm = selectedForm;
    }
    public static void setForm1Option(FormManager.FormSelectionOption value){
        clientFormData.form1 = value;
    }
    public static void setForm2Option(FormManager.FormSelectionOption value){
        clientFormData.form2 = value;
    }
    public static void setForm3Option(FormManager.FormSelectionOption value){
        clientFormData.form3 = value;
    }

    public static void setSpecificFormData(Player player){
        Form form = FormManager.getInstance().getForm(getSelectedForm().name());
        specificFormData = form.getFormData(player.getUUID());
    }

    public static void setCurrentFormLevel(FormLevelManager.FormLevel level){
        specificFormData.setCurrentFormLevel(level);
    }
    public static void setCurrentFormXp(int value){
        specificFormData.setCurrentFormXp(value);
    }
    public static void setCurrentFormXpMAX(int value){
        specificFormData.setCurrentFormXpMAX(value);
    }


    public static AbstractFormData getSpecificFormData(Player player){
        Form form = FormManager.getInstance().getForm(ClientFistData.getSelectedForm().name());

        return form.getFormData(player.getUUID());
    }

    public static int getCurrentFormXp(){
        return specificFormData.getCurrentFormXp();
    }
    public static int getCurrentFormXpMAX(){
        return specificFormData.getCurrentFormXpMAX();
    }

    public static FormLevelManager.FormLevel getCurrentFormLevel(){
       return specificFormData.getCurrentFormLevel();
    }






}