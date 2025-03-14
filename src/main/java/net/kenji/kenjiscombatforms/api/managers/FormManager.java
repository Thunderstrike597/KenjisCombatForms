package net.kenji.kenjiscombatforms.api.managers;

import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FormManager {
    public final Map<UUID, PlayerFormData> playerDataMap = new ConcurrentHashMap<>();

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
        return forms.get(formName);
    }





    public enum FormSelectionOption {
      BASIC, VOID, WITHER, SWIFT, POWER, NONE
    }

    public static class PlayerFormData{
        public FormSelectionOption selectedForm = FormSelectionOption.BASIC;

        public FormSelectionOption form1 = FormSelectionOption.NONE;
        public FormSelectionOption form2 = FormSelectionOption.NONE;
        public FormSelectionOption form3 = FormSelectionOption.NONE;
    }




    public PlayerFormData getOrCreatePlayerFormData(Player player){
        return playerDataMap.computeIfAbsent(player.getUUID(), k -> new PlayerFormData());
    }
    public FormManager.PlayerFormData getFormData(Player player){
        return getOrCreatePlayerFormData(player);
    }
}
