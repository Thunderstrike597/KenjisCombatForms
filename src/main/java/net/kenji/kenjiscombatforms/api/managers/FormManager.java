package net.kenji.kenjiscombatforms.api.managers;

import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.api.managers.forms.BasicForm;
import net.kenji.kenjiscombatforms.gameasset.CombatFormWeaponCategories;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FormManager {
    public final Map<UUID, PlayerFormData> playerDataMap = new ConcurrentHashMap<>();
    public static final Map<UUID, Form> lastForm = new HashMap<>();
    public static final Map<UUID, ItemStack> trueStackMap = new HashMap<>();

    private static final FormManager INSTANCE = new FormManager();
    public static FormManager getInstance(){
        return INSTANCE;
    }
    public void updatePlayerData(UUID playerUUID, FormManager.PlayerFormData data) {
        playerDataMap.put(playerUUID, data);
    }

    private final Map<String, Form> forms = new HashMap<>();

    public void registerForm(Form form) {
        forms.put(form.getName(), form);
    }

    public Form getForm(String formName) {
        return formName != null && !formName.isEmpty() ? forms.get(formName) : forms.get("NONE");
    }


    public static class PlayerFormData{
        public String selectedForm = BasicForm.getInstance().getName();

        public String form1 = "NONE";
        public String form2 = "NONE";
        public String form3 = "NONE";
    }

    public static Form getLastForm(Player player){
        return lastForm.getOrDefault(player.getUUID(), getCurrentForm(player));
    }
    public static void updateLastForm(Player player){
        lastForm.put(player.getUUID(), getCurrentForm(player));
    }
    public static boolean isFormChanged(Player player){
        return getLastForm(player) != getCurrentForm(player);
    }

    public List<String> getCurrentFormsValues(Player player){
        if (!player.level().isClientSide) {
            FormManager.PlayerFormData formData = getInstance().getOrCreatePlayerFormData(player.getUUID());

            return Arrays.asList(
                  Objects.requireNonNullElse(formData.selectedForm, "NONE"),

                  Objects.requireNonNullElse(formData.form1, "NONE"),
                    Objects.requireNonNullElse(formData.form2, "NONE"),
                    Objects.requireNonNullElse(formData.form3, "NONE")
            );
        } else return Arrays.asList(
                Objects.requireNonNullElse(ClientFistData.getSelectedForm(), "NONE"),

               Objects.requireNonNullElse(ClientFistData.getForm1Option(), "NONE"),
                Objects.requireNonNullElse(ClientFistData.getForm2Option(), "NONE"),
                Objects.requireNonNullElse(ClientFistData.getForm3Option(), "NONE"));
    }

    public List<Form> getCurrentForms(Player player){

        Form noneForm = FormManager.INSTANCE.getForm("NONE");

        Form selectedForm = FormManager.INSTANCE.getForm(getCurrentFormsValues(player).get(0));
        Form form1 = FormManager.INSTANCE.getForm(getCurrentFormsValues(player).get(1));
        Form form2 = FormManager.INSTANCE.getForm(getCurrentFormsValues(player).get(2));
        Form form3 = FormManager.INSTANCE.getForm(getCurrentFormsValues(player).get(3));


        if (!player.level().isClientSide) {
            return Arrays.asList(
                    Objects.requireNonNullElse(selectedForm, noneForm),

                    Objects.requireNonNullElse(form1, noneForm),
                    Objects.requireNonNullElse(form2, noneForm),
                    Objects.requireNonNullElse(form3, noneForm)
            );
        } else return Arrays.asList(
                FormManager.getInstance().getForm(Objects.requireNonNullElse(ClientFistData.getSelectedForm(), "NONE")),

                FormManager.getInstance().getForm(Objects.requireNonNullElse(ClientFistData.getForm1Option(), "NONE")),
                FormManager.getInstance().getForm(Objects.requireNonNullElse(ClientFistData.getForm2Option(), "NONE")),
                FormManager.getInstance().getForm(Objects.requireNonNullElse(ClientFistData.getForm3Option(), "NONE"))
        );
    }
    public List<AbstractFormData> getCurrentFormData(Player player){
        Form currentForm = getCurrentForms(player).get(0);
        Form form1 = getCurrentForms(player).get(1);
        Form form2 = getCurrentForms(player).get(2);
        Form form3 = getCurrentForms(player).get(3);

        return Arrays.asList(
                currentForm.getFormData(player.getUUID()),
                form1.getFormData(player.getUUID()),
                form2.getFormData(player.getUUID()),
                form3.getFormData(player.getUUID()));
    }

    public static Form getCurrentForm(Player player){
        String formName = FormManager.getInstance().getOrCreatePlayerFormData(player.getUUID()).selectedForm;
        return FormManager.getInstance().getForm(formName);
    }
    public static Skill getCurrentFormSkill(Player player){
        Form form = getCurrentForm(player);
        return form.getFormSkill(player);
    }
    public static ItemStack getCurrentFormItem(Player player){
        Form form = getCurrentForm(player);
        return form.getFormItem(player.getUUID());
    }
    public static boolean isHeldCategoryValid(Player player, ItemStack stack){
       CapabilityItem capItem = EpicFightCapabilities.getItemStackCapability(stack);
       WeaponCategory category = capItem.getWeaponCategory();

       return category == CapabilityItem.WeaponCategories.FIST || category == CapabilityItem.WeaponCategories.NOT_WEAPON || category == CombatFormWeaponCategories.COMBAT_DAGGER;
    }


    public PlayerFormData getOrCreatePlayerFormData(UUID playerUuid){
        return playerDataMap.computeIfAbsent(playerUuid, k -> new PlayerFormData());
    }
    public FormManager.PlayerFormData getFormData(Player player){
        return getOrCreatePlayerFormData(player.getUUID());
    }
}
