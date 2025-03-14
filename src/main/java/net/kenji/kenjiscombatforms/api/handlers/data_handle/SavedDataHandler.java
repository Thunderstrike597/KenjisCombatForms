package net.kenji.kenjiscombatforms.api.handlers.data_handle;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.forms.*;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class SavedDataHandler extends SavedData {
    private static final String DATA_NAME = KenjisCombatForms.MOD_ID + "_" + "saved_data";


    private static final String CHOSEN_ABILITY1_DATA = "chosen_ability1";
    private static final String CHOSEN_ABILITY2_DATA = "chosen_ability2";
    private static final String CHOSEN_ABILITY3_DATA = "chosen_ability3";

    private static final String CHOSEN_FORM_DATA = "chosen_form";
    private static final String FORM1_OPTION_DATA = "form1_option";
    private static final String FORM2_OPTION_DATA = "form2_option";
    private static final String FORM3_OPTION_DATA = "form3_option";


    private static final String FORM_XP_DATA_ = "form_xp_data";
    private static final String PREVIOUSLY_CHOSEN_ABILITY_DATA_ = "previous_chosen_ability_data";
    private static final String STORED_ABILITY_DATA_ = "stored_ability_data";
    private static final String FORM_LEVEL_DATA_ = "form_level_data";
    private static final String FORM_MAX_XP_DATA_ = "form_max_xp_data";

   public static SavedDataHandler create(){
       return new SavedDataHandler();
   }

    private final Map<UUID, CompoundTag> playerDataMap = new HashMap<>();

    public static void triggerPlayerDataLoad(UUID uuid, AbilityManager.PlayerAbilityData abilityData, FormManager.PlayerFormData formData, CompoundTag tag){
       loadPlayerData(uuid, abilityData, formData, tag);
    }

    public static String getKenjiSavedData(){
        return DATA_NAME;
    }

    public void updatePlayerData(UUID playerUUID) {
        CompoundTag playerData = new CompoundTag();
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().playerDataMap.get(playerUUID);
        FormManager.PlayerFormData formData = FormManager.getInstance().playerDataMap.get(playerUUID);

        FormLevelManager.PlayerFormLevelData formLevelData = FormLevelManager.getInstance().playerDataMap.get(playerUUID);
        savePlayerData(playerUUID, abilityData, formLevelData, formData, playerData);
        this.playerDataMap.put(playerUUID, playerData);
        this.setDirty();
    }

    public static SavedDataHandler load(CompoundTag tag) {
        SavedDataHandler data = create();
        CompoundTag playersTag = tag.getCompound("players");
        for (String uuidString : playersTag.getAllKeys()) {
            UUID uuid = UUID.fromString(uuidString);
            CompoundTag playerTag = playersTag.getCompound(uuidString);
            AbilityManager.PlayerAbilityData playerAbilityData = new AbilityManager.PlayerAbilityData();
            FormManager.PlayerFormData playerFormData = new FormManager.PlayerFormData();
            VoidForm.FormData voidFormData = new VoidForm.FormData();
            WitherForm.FormData witherFormData = new WitherForm.FormData();

            FormLevelManager.PlayerFormLevelData formLevelData = new FormLevelManager.PlayerFormLevelData();
            loadPlayerData(uuid, playerAbilityData, playerFormData, playerTag);
            data.playerDataMap.put(uuid, playerTag);

            // Optionally, you can still update the manager instances here
            AbilityManager.getInstance().playerDataMap.put(uuid, playerAbilityData);
            FormManager.getInstance().playerDataMap.put(uuid, playerFormData);
            VoidForm.getInstance().playerDataMap.put(uuid, voidFormData);
            WitherForm.getInstance().playerDataMap.put(uuid, witherFormData);
            FormLevelManager.getInstance().playerDataMap.put(uuid, formLevelData);

        }
        return data;
    }



    private static void loadPlayerData(UUID uuid, AbilityManager.PlayerAbilityData abilityData, FormManager.PlayerFormData formData, CompoundTag tag) {
        loadFormData(uuid, VoidForm.getInstance(), tag, "VOID");
        loadFormData(uuid, WitherForm.getInstance(), tag, "WITHER");
        loadFormData(uuid, BasicForm.getInstance(), tag, "BASIC");
        loadFormData(uuid, SwiftForm.getInstance(), tag, "SWIFT");
        loadFormData(uuid, PowerForm.getInstance(), tag, "POWER");

       try {
           abilityData.chosenAbility1 = AbilityManager.AbilityOption1.valueOf(tag.getString(CHOSEN_ABILITY1_DATA));
       }catch (Exception e){
           abilityData.chosenAbility1 = AbilityManager.AbilityOption1.NONE;
       }try {
       abilityData.chosenAbility2 = AbilityManager.AbilityOption2.valueOf(tag.getString(CHOSEN_ABILITY2_DATA));
        }catch (Exception e){
           abilityData.chosenAbility2 = AbilityManager.AbilityOption2.NONE;
        }try {
            abilityData.chosenFinal = AbilityManager.AbilityOption3.valueOf(tag.getString(CHOSEN_ABILITY3_DATA));
        }catch (Exception e){
            abilityData.chosenFinal = AbilityManager.AbilityOption3.NONE;
        }

       try {
            formData.selectedForm = FormManager.FormSelectionOption.valueOf(tag.getString(CHOSEN_FORM_DATA));
        }catch (IllegalArgumentException e){
            formData.selectedForm = FormManager.FormSelectionOption.BASIC;
        }

        try {
            formData.form1 = FormManager.FormSelectionOption.valueOf(tag.getString(FORM1_OPTION_DATA));
        }catch (IllegalArgumentException e){
            formData.form1 = FormManager.FormSelectionOption.NONE;
        }  try {
            formData.form2 = FormManager.FormSelectionOption.valueOf(tag.getString(FORM2_OPTION_DATA));
        }catch (IllegalArgumentException e){
            formData.form2 = FormManager.FormSelectionOption.NONE;
        }   try {
            formData.form3 = FormManager.FormSelectionOption.valueOf(tag.getString(FORM3_OPTION_DATA));
        }catch (IllegalArgumentException e){
            formData.form3 = FormManager.FormSelectionOption.NONE;
        }
    }


    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag playersTag = new CompoundTag();
        Set<UUID> allUUIDs = new HashSet<>(AbilityManager.getInstance().playerDataMap.keySet());
        allUUIDs.addAll(FormManager.getInstance().playerDataMap.keySet());

        for (UUID uuid : allUUIDs) {
            CompoundTag playerTag = new CompoundTag();
            AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().playerDataMap.get(uuid);
            FormManager.PlayerFormData formData = FormManager.getInstance().playerDataMap.get(uuid);

            FormLevelManager.PlayerFormLevelData formLevelData = FormLevelManager.getInstance().playerDataMap.get(uuid);
            savePlayerData(uuid, abilityData, formLevelData, formData, playerTag);
            playersTag.put(uuid.toString(), playerTag);
        }
        tag.put("players", playersTag);
        return tag;
    }


    private static void loadFormData(UUID uuid, Form form, CompoundTag tag, String formPrefix) {
        AbstractFormData formData = form.getFormData(uuid); // We'll need to modify this if player is required

        try {
            formData.setCurrentStoredAbility1(AbilityManager.AbilityOption1.valueOf(tag.getString(STORED_ABILITY_DATA_ + formPrefix + "1")));
        } catch (Exception e) {
            formData.setCurrentStoredAbility1(AbilityManager.AbilityOption1.NONE);
        }try {
            formData.setCurrentStoredAbility2(AbilityManager.AbilityOption2.valueOf(tag.getString(STORED_ABILITY_DATA_ + formPrefix + "2")));
        } catch (Exception e) {
            formData.setCurrentStoredAbility2(AbilityManager.AbilityOption2.NONE);
        }try {
            formData.setStoredAbility3(AbilityManager.AbilityOption3.valueOf(tag.getString(STORED_ABILITY_DATA_ + formPrefix + "3")));
        } catch (Exception e) {
            formData.setStoredAbility3(AbilityManager.AbilityOption3.NONE);
        }

        try{
            formData.setCurrentFormLevel(FormLevelManager.FormLevel.valueOf(tag.getString(FORM_LEVEL_DATA_ + formPrefix)));
        }catch (Exception e){
            formData.setCurrentFormLevel(FormLevelManager.FormLevel.LEVEL1);
        }

        try{
            formData.setPreviousAbility1(AbilityManager.AbilityOption1.valueOf(tag.getString(PREVIOUSLY_CHOSEN_ABILITY_DATA_ + formPrefix + "1")));
        }catch (Exception e){
            formData.setPreviousAbility1(AbilityManager.AbilityOption1.NONE);
        }try{
            formData.setPreviousAbility2(AbilityManager.AbilityOption2.valueOf(tag.getString(PREVIOUSLY_CHOSEN_ABILITY_DATA_ + formPrefix + "2")));
        }catch (Exception e){
            formData.setPreviousAbility2(AbilityManager.AbilityOption2.NONE);
        }try{
            formData.setPreviousAbility3(AbilityManager.AbilityOption3.valueOf(tag.getString(PREVIOUSLY_CHOSEN_ABILITY_DATA_ + formPrefix + "3")));
        }catch (Exception e){
            formData.setPreviousAbility3(AbilityManager.AbilityOption3.NONE);
        }


        formData.setCurrentFormXp(tag.getInt(FORM_XP_DATA_ + formPrefix));
        formData.setCurrentFormXpMAX(tag.getInt(FORM_MAX_XP_DATA_ + formPrefix));

        form.updatePlayerData(uuid, formData);
    }


    private static void savePlayerData(UUID playerUUID, AbilityManager.PlayerAbilityData abilityData, FormLevelManager.PlayerFormLevelData formLevelData, FormManager.PlayerFormData formData, CompoundTag tag) {
        saveFormData(playerUUID, VoidForm.getInstance(), tag, "VOID");
        saveFormData(playerUUID, WitherForm.getInstance(), tag, "WITHER");
        saveFormData(playerUUID, BasicForm.getInstance(), tag, "BASIC");
        saveFormData(playerUUID, SwiftForm.getInstance(), tag, "SWIFT");
        saveFormData(playerUUID, PowerForm.getInstance(), tag, "POWER");

        if (abilityData != null) {
            tag.putString(CHOSEN_ABILITY1_DATA, abilityData.chosenAbility1.name());
            tag.putString(CHOSEN_ABILITY2_DATA, abilityData.chosenAbility2.name());
            tag.putString(CHOSEN_ABILITY3_DATA, abilityData.chosenFinal.name());
        }
        if (formData != null) {
            tag.putString(CHOSEN_FORM_DATA, formData.selectedForm.name());
            tag.putString(FORM1_OPTION_DATA, formData.form1.name());
            tag.putString(FORM2_OPTION_DATA, formData.form2.name());
            tag.putString(FORM3_OPTION_DATA, formData.form3.name());
        }
    }

    private static void saveFormData(UUID player, Form form, CompoundTag tag, String formPrefix) {
        if (player == null) {
           System.out.println("Attempted to save form data for null player");
            return;
        }

        AbstractFormData formData = form.getFormData(player);
        if (formData == null) {
            System.out.println("No form data found for player {} and form {}"  + form.getName());
            return;
        }

        tag.putString(STORED_ABILITY_DATA_ + formPrefix + "1", formData.getCurrentStoredAbility1().name());
        tag.putString(STORED_ABILITY_DATA_ + formPrefix + "2", formData.getCurrentStoredAbility2().name());
        tag.putString(STORED_ABILITY_DATA_ + formPrefix + "3", formData.getStoredAbility3().name());

        tag.putString(PREVIOUSLY_CHOSEN_ABILITY_DATA_ + formPrefix + "1", formData.getPreviousAbility1().name());
        tag.putString(PREVIOUSLY_CHOSEN_ABILITY_DATA_ + formPrefix + "2", formData.getPreviousAbility2().name());
        tag.putString(PREVIOUSLY_CHOSEN_ABILITY_DATA_ + formPrefix + "3", formData.getPreviousAbility3().name());



        tag.putString(FORM_LEVEL_DATA_ + formPrefix, formData.getCurrentFormLevel().name());
        tag.putInt(FORM_XP_DATA_ + formPrefix, formData.getCurrentFormXp());
        tag.putInt(FORM_MAX_XP_DATA_ + formPrefix, formData.getCurrentFormXpMAX());
    }

    public CompoundTag getPlayerData(UUID playerUUID) {
        return playerDataMap.computeIfAbsent(playerUUID, k -> new CompoundTag());
    }


    public void forceSave(ServerLevel level) {
        level.getDataStorage().set(DATA_NAME, this);
    }
    public static SavedDataHandler get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(SavedDataHandler::load, SavedDataHandler::create, DATA_NAME);
    }
}
