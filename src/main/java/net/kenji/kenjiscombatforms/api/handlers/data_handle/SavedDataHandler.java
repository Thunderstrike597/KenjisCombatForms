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
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

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




            FormLevelManager.PlayerFormLevelData formLevelData = new FormLevelManager.PlayerFormLevelData();
            loadPlayerData(uuid, playerAbilityData, playerFormData, playerTag);
            data.playerDataMap.put(uuid, playerTag);

            // Optionally, you can still update the manager instances here
            AbilityManager.getInstance().playerDataMap.put(uuid, playerAbilityData);
            FormManager.getInstance().playerDataMap.put(uuid, playerFormData);

            FormLevelManager.getInstance().playerDataMap.put(uuid, formLevelData);

        }
        return data;
    }



    private static void loadPlayerData(UUID uuid, AbilityManager.PlayerAbilityData abilityData, FormManager.PlayerFormData formData, CompoundTag tag) {
        loadFormData(uuid, BasicForm.getInstance(), tag, BasicForm.getInstance().getName());


        //loadFormData(uuid, VoidForm.getInstance(), tag, VoidForm.getInstance().getName());
        //loadFormData(uuid, WitherForm.getInstance(), tag, WitherForm.getInstance().getName());
        //loadFormData(uuid, SwiftForm.getInstance(), tag, SwiftForm.getInstance().getName());
        //loadFormData(uuid, PowerForm.getInstance(), tag, PowerForm.getInstance().getName());

        String ability1Data = tag.getString(CHOSEN_ABILITY1_DATA);
        abilityData.chosenAbility1 = !ability1Data.isEmpty() ? ability1Data : "NONE";

        String ability2Data = tag.getString(CHOSEN_ABILITY2_DATA);
        abilityData.chosenAbility2 = !ability2Data.isEmpty() ? ability2Data : "NONE";

        String ability3Data = tag.getString(CHOSEN_ABILITY3_DATA);
        abilityData.chosenFinal = !ability3Data.isEmpty() ? ability3Data : "NONE";

        if(!tag.getString(CHOSEN_FORM_DATA).isEmpty()) {
           formData.selectedForm = tag.getString(CHOSEN_FORM_DATA);
       }else {formData.selectedForm = BasicForm.getInstance().getName();}

       if(!tag.getString(FORM1_OPTION_DATA).isEmpty()) {
           formData.form1 = tag.getString(FORM1_OPTION_DATA);
       }else{
           formData.form1 = "NONE";
       }
       if(!tag.getString(FORM2_OPTION_DATA).isEmpty()) {
            formData.form2 = tag.getString(FORM2_OPTION_DATA);
       }else{
           formData.form2 = "NONE";
       }
       if(!tag.getString(FORM3_OPTION_DATA).isEmpty()) {
            formData.form3 = tag.getString(FORM3_OPTION_DATA);
       }else{
           formData.form3 = "NONE";
       }


                Form formOption1 = FormManager.getInstance().getForm(formData.form1);
                loadFormData(uuid, formOption1, tag, formOption1.getName());


                Form formOption2 = FormManager.getInstance().getForm(Objects.requireNonNullElse(formData.form2, "NONE"));
                loadFormData(uuid, formOption2, tag, formOption2.getName());

                Form formOption3 = FormManager.getInstance().getForm(Objects.requireNonNullElse(formData.form3, "NONE"));
                loadFormData(uuid, formOption3, tag, formOption3.getName());
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
        if(form != null) {
            if (!Objects.equals(form.getName(), "NONE")) {
                AbstractFormData formData = form.getFormData(uuid); // We'll need to modify this if player is required


                    String storedAbility1Data = tag.getString(STORED_ABILITY_DATA_ + formPrefix + "1");
                    String storedAbility2Data = tag.getString(STORED_ABILITY_DATA_ + formPrefix + "2");
                    String storedAbility3Data = tag.getString(STORED_ABILITY_DATA_ + formPrefix + "3");


                    formData.setCurrentStoredAbility1(tag.getString((!storedAbility1Data.isEmpty()) ? storedAbility1Data : "NONE"));
                    formData.setCurrentStoredAbility2(tag.getString((!storedAbility2Data.isEmpty()) ? storedAbility2Data : "NONE"));
                    formData.setCurrentStoredAbility2(tag.getString((!storedAbility3Data.isEmpty()) ? storedAbility3Data : "NONE"));

                try {
                    formData.setCurrentFormLevel(FormLevelManager.FormLevel.valueOf(tag.getString(FORM_LEVEL_DATA_ + formPrefix)));
                } catch (Exception e) {
                    formData.setCurrentFormLevel(FormLevelManager.FormLevel.LEVEL1);
                }


                formData.setCurrentFormXp(tag.getInt(FORM_XP_DATA_ + formPrefix));
                formData.setCurrentFormXpMAX(tag.getInt(FORM_MAX_XP_DATA_ + formPrefix));

                form.updatePlayerData(uuid, formData);
            }
        }
    }


    private static void savePlayerData(UUID playerUUID, AbilityManager.PlayerAbilityData abilityData, FormLevelManager.PlayerFormLevelData formLevelData, FormManager.PlayerFormData formData, CompoundTag tag) {
        saveFormData(playerUUID, BasicForm.getInstance(), tag, "BASIC");

        System.out.println(" Player Tag: " + tag);


        if(formData.form1 != null) {
            Form formOption1 = FormManager.getInstance().getForm(formData.form1);
                tag.putString(FORM1_OPTION_DATA, formData.form1);

            saveFormData(playerUUID, formOption1, tag, formOption1.getName());
        }
        if(formData.form2 != null) {
            Form formOption2 = FormManager.getInstance().getForm(formData.form2);
                tag.putString(FORM2_OPTION_DATA, formData.form2);

            saveFormData(playerUUID, formOption2, tag, formOption2.getName());
        }
        if(formData.form3 != null) {
            Form formOption3 = FormManager.getInstance().getForm(formData.form3);
                tag.putString(FORM3_OPTION_DATA, formData.form3);
            saveFormData(playerUUID, formOption3, tag, formOption3.getName());
        }


        //saveFormData(playerUUID, VoidForm.getInstance(), tag, "VOID");
        //saveFormData(playerUUID, WitherForm.getInstance(), tag, "WITHER");
        //saveFormData(playerUUID, SwiftForm.getInstance(), tag, "SWIFT");
        //saveFormData(playerUUID, PowerForm.getInstance(), tag, "POWER");


        if (abilityData != null) {

                tag.putString(CHOSEN_ABILITY1_DATA, abilityData.chosenAbility1);
                tag.putString(CHOSEN_ABILITY2_DATA, abilityData.chosenAbility2);
                tag.putString(CHOSEN_ABILITY3_DATA, abilityData.chosenFinal);
        }
        if(!tag.getString(CHOSEN_FORM_DATA).isEmpty()) {
            tag.putString(CHOSEN_FORM_DATA, formData.selectedForm);
        }else{tag.putString(CHOSEN_FORM_DATA, BasicForm.getInstance().getName());}
    }
    private static void saveFormData(UUID player, Form form, CompoundTag tag, String formPrefix) {
        if (!Objects.equals(formPrefix, "NONE")) {
            if (player == null) {
                System.out.println("Attempted to save form data for null player");
                return;
            }

            AbstractFormData formData = form.getFormData(player);
            if (formData == null) {
                System.out.println("No form data found for player {} and form {}" + form.getName());
                return;
            }

                tag.putString(STORED_ABILITY_DATA_ + formPrefix + "1", formData.getCurrentStoredAbility1());
                tag.putString(STORED_ABILITY_DATA_ + formPrefix + "2", formData.getCurrentStoredAbility2());
                tag.putString(STORED_ABILITY_DATA_ + formPrefix + "3", formData.getStoredAbility3());


            tag.putString(FORM_LEVEL_DATA_ + formPrefix, formData.getCurrentFormLevel().name());
            tag.putInt(FORM_XP_DATA_ + formPrefix, formData.getCurrentFormXp());
            tag.putInt(FORM_MAX_XP_DATA_ + formPrefix, formData.getCurrentFormXpMAX());
        }
    }

    public CompoundTag getPlayerData(UUID playerUUID) {
        return playerDataMap.computeIfAbsent(playerUUID, k -> new CompoundTag());
    }


    public void forceSave(ServerLevel level) {
        level.getDataStorage().set(getKenjiSavedData(), this);
    }
    public static SavedDataHandler get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(SavedDataHandler::load, SavedDataHandler::create, getKenjiSavedData());
    }
}
