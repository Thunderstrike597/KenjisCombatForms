package net.kenji.kenjiscombatforms.api.managers.client_data;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.forms.BasicForm;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class ClientFistData {

    private static AbstractFormData specificFormData;
    private static final FormManager.PlayerFormData clientFormData = new FormManager.PlayerFormData();
    private static final AbilityManager.PlayerAbilityData clientAbilityData = new AbilityManager.PlayerAbilityData();



    public static String getCurrentStoredAbility1(Player player) {
        String currentStoredAbility = specificFormData.getCurrentStoredAbility1();
        return !currentStoredAbility.isEmpty() ? currentStoredAbility : "NONE";
    }
    public static String getCurrentStoredAbility2(Player player) {
        String currentStoredAbility = specificFormData.getCurrentStoredAbility2();
        return !currentStoredAbility.isEmpty() ? currentStoredAbility : "NONE";
    }
    public static String getCurrentStoredAbility3(Player player) {
        String currentStoredAbility = specificFormData.getStoredAbility3();
        return !currentStoredAbility.isEmpty() ? currentStoredAbility : "NONE";
    }
    public static String getCurrentAbility4() {
        return clientAbilityData.ability4;
    }
    public static String getCurrentAbility5() {
        return clientAbilityData.ability5;
    }


    public static String getSelectedForm() {
        return clientFormData.selectedForm;
    }

    public static String getChosenAbility1() {
        return clientAbilityData.chosenAbility1;
    }
    public static String getChosenAbility2() {
        return clientAbilityData.chosenAbility2;
    }
    public static String getChosenAbility3() {
        return clientAbilityData.chosenFinal;
    }

    public static String getForm1Option(){
        return clientFormData.form1;
    }
    public static String getForm2Option(){
        return clientFormData.form2;
    }
    public static String getForm3Option(){
        return clientFormData.form3;
    }
    public static void setChosenAbility1(String value) {
        clientAbilityData.chosenAbility1 = value;
    }
    public static void setChosenAbility2(String value) {
        clientAbilityData.chosenAbility2 = value;
    }
    public static void setChosenAbility3(String value) {
        clientAbilityData.chosenFinal = value;
    }
    public static void setCurrentStoredAbility1(String value, Player player) {
        getSpecificFormData(player).setCurrentStoredAbility1(value);
    }
    public static void setCurrentStoredAbility2(String value, Player player) {
        getSpecificFormData(player).setCurrentStoredAbility2(value);
    }

    public static void setCurrentStoredAbility3(String value, Player player) {
        getSpecificFormData(player).setStoredAbility3(value);
    }
    public static void setSelectedForm(String selectedForm) {
        clientFormData.selectedForm = selectedForm;
    }

    public static void setAbility4(String abilityOption4) {
        clientAbilityData.ability4 = abilityOption4;
    }
    public static void setAbility5(String abilityOption5) {
        clientAbilityData.ability5 = abilityOption5;
    }
    public static void setForm1Option(String value){
        clientFormData.form1 = value;
    }
    public static void setForm2Option(String value){
        clientFormData.form2 = value;
    }
    public static void setForm3Option(String value){
        clientFormData.form3 = value;
    }

    public static void setSpecificFormData(Player player){
        Form form = FormManager.getInstance().getForm(getSelectedForm());
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
        Form form = FormManager.getInstance().getForm(ClientFistData.getSelectedForm());

        return form.getFormData(player.getUUID());
    }

    public static int getCurrentFormXp(){
        if (specificFormData != null) {
            return specificFormData.getCurrentFormXp();
        } return 0;
    }
    public static int getCurrentFormXpMAX(){
        if(specificFormData != null) {
            return specificFormData.getCurrentFormXpMAX();
        } return EpicFightCombatFormsCommon.MAX_FORM_STARTING_XP.get();
    }

    public static FormLevelManager.FormLevel getCurrentFormLevel(){
        if(specificFormData != null) {
            return specificFormData.getCurrentFormLevel();
        }
        return FormLevelManager.FormLevel.LEVEL1;
    }






}