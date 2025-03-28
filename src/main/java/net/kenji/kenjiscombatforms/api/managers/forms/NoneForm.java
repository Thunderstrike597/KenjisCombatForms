package net.kenji.kenjiscombatforms.api.managers.forms;

import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NoneForm implements Form {
    public final Map<UUID, PowerForm.FormData> playerDataMap = new ConcurrentHashMap<>();

    private static NoneForm INSTANCE = new NoneForm();

    @Override
    public String getName() {
        return "NONE";
    }

    @Override
    public AbstractFormData getFormData(UUID player) {
        if (player == null) {
            // Handle the null player case
            // You might want to log this occurrence or return a default AbstractFormData
            return new PowerForm.FormData(); // Or return null, depending on your needs
        }
        return playerDataMap.computeIfAbsent(player, k -> new PowerForm.FormData());
    }

    public static NoneForm getInstance(){
        if(INSTANCE == null){
            INSTANCE = new NoneForm();
        }
        return INSTANCE;
    }


    @Override
    public List<ResourceLocation> getLevelResources() {
        return List.of();
    }

    @Override
    public int getGUIDrawPosY() {
        return 0;
    }

    @Override
    public int getGUIDrawPosX() {
        return 90;
    }

    @Override
    public int getGUISwapPos() {
        return 0;
    }

    @Override
    public void updatePlayerData(UUID playerUUID, AbstractFormData formData) {

    }

    @Override
    public void syncDataToClient(Player player) {

    }

    @Override
    public void setCurrentForm(Player player, int slot) {

    }

    public class FormData extends AbstractFormData{

        @Override
        public String getCurrentStoredAbility1() {
            return "NONE";
        }

        @Override
        public void setCurrentStoredAbility1(String ability) {

        }

        @Override
        public String getCurrentStoredAbility2() {
            return "NONE";
        }

        @Override
        public void setCurrentStoredAbility2(String ability) {

        }

        @Override
        public String getStoredAbility3() {
            return "NONE";
        }

        @Override
        public void setStoredAbility3(String ability) {

        }

        @Override
        public FormLevelManager.FormLevel getCurrentFormLevel() {
            return FormLevelManager.FormLevel.LEVEL1;
        }

        @Override
        public int getCurrentFormXp() {
            return 0;
        }

        @Override
        public int getCurrentFormXpMAX() {
            return 0;
        }

        @Override
        public void setCurrentFormXp(int amount) {

        }

        @Override
        public void setCurrentFormXpMAX(int amount) {

        }

        @Override
        public void setCurrentFormLevel(FormLevelManager.FormLevel currentFormLevel) {

        }
    }
}
