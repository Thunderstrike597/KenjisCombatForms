package net.kenji.kenjiscombatforms.api.managers.forms;

import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseBasicClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.skill.Skill;

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

    @Override
    public ItemStack getFormItem(UUID playerId) {
        return null;
    }

    @Override
    public Skill getFormSkill(Player player) {
        return null;
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
        public boolean isHoldingForm(ServerPlayer player) {
            return player.getMainHandItem().getItem() instanceof BaseBasicClass;
        }

        @Override
        public void gainFormXp(ServerPlayer player, Entity entity) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());
            if (entity instanceof Monster || entity instanceof Player) {

                if (isHoldingForm(player)) {
                    if (formData.getCurrentFormXp() < formData.getCurrentFormXpMAX()) {
                        formData.setCurrentFormXp(formData.getCurrentFormXp() + 1);
                        getInstance().syncDataToClient(player);
                    }
                }
            }
        }
        @Override
        public void setCurrentFormXpMAX(int amount) {

        }

        @Override
        public void setCurrentFormLevel(FormLevelManager.FormLevel currentFormLevel) {

        }
    }
}
