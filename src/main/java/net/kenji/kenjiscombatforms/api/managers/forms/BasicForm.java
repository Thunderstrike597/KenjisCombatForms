package net.kenji.kenjiscombatforms.api.managers.forms;

import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.FormLevelStrategy;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.interfaces.form.FormAbilityStrategy;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseBasicClass;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.client_data.SyncClientBasicFistPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BasicForm implements Form {
    public final Map<UUID, BasicFormData> playerDataMap = new ConcurrentHashMap<>();

    private static BasicForm INSTANCE = new BasicForm();
    public static BasicForm getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BasicForm();
        }
        return INSTANCE;
    }

    public void updatePlayerData(UUID playerUUID, BasicFormData data) {
        playerDataMap.put(playerUUID, data);
    }

    @Override
    public String getName() {
        return "BASIC";
    }
    @Override
    public AbstractFormData getFormData(UUID playerUUID) {
        if (playerUUID == null) {
            // Handle the null playerUUID case
            // You might want to log this occurrence or return a default AbstractFormData
            return new BasicFormData(); // Or return null, depending on your needs
        }
        return playerDataMap.computeIfAbsent(playerUUID, k -> new BasicFormData());
    }

    @Override
    public void updatePlayerData(UUID playerUUID, AbstractFormData formData) {
        if (formData instanceof BasicFormData) {
            playerDataMap.put(playerUUID, (BasicFormData) formData);
        } else {
            throw new IllegalArgumentException("AbstractFormData must be of type BasicFormData");
        }
    }

    @Override
    public void syncDataToClient(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            AbstractFormData formData = getFormData(player.getUUID());
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new SyncClientBasicFistPacket(formData.getCurrentStoredAbility1(), formData.getCurrentStoredAbility2())
            );
        }
    }
    public static class BasicFormData extends AbstractFormData {
        private AbilityManager.AbilityOption1 basicStoredAbility1;
        private AbilityManager.AbilityOption2 basicStoredAbility2;
        private AbilityManager.AbilityOption1 basicPreviousAbility1;
        private AbilityManager.AbilityOption2 basicPreviousAbility2;

        private FormLevelManager.FormLevel basicFormLevel;
        private int basicFormXp;
        private int basicFormXpMAX;

        public BasicFormData() {
            this.basicStoredAbility1 = AbilityManager.AbilityOption1.NONE;
            this.basicStoredAbility2 = AbilityManager.AbilityOption2.NONE;
            this.basicPreviousAbility1 = AbilityManager.AbilityOption1.NONE;
            this.basicPreviousAbility2 = AbilityManager.AbilityOption2.NONE;


            this.basicFormLevel = FormLevelManager.FormLevel.LEVEL1;
            this.basicFormXp = 0;
            this.basicFormXpMAX = KenjisCombatFormsCommon.MAX_FORM_STARTING_XP.get();
        }

        @Override
        public AbilityManager.AbilityOption1 getCurrentStoredAbility1() {
            return basicStoredAbility1;
        }

        @Override
        public void setCurrentStoredAbility1(AbilityManager.AbilityOption1 ability) {
            this.basicStoredAbility1 = ability;
        }

        @Override
        public AbilityManager.AbilityOption2 getCurrentStoredAbility2() {
            return basicStoredAbility2;
        }

        @Override
        public void setCurrentStoredAbility2(AbilityManager.AbilityOption2 ability) {
            this.basicStoredAbility2 = ability;
        }

        @Override
        public AbilityManager.AbilityOption3 getStoredAbility3() {
            return AbilityManager.AbilityOption3.NONE;
        }

        @Override
        public void setStoredAbility3(AbilityManager.AbilityOption3 ability) {

        }


        @Override
        public FormLevelManager.FormLevel getCurrentFormLevel() {
            return this.basicFormLevel;
        }

        @Override
        public void setCurrentFormLevel(FormLevelManager.FormLevel currentFormLevel) {
            this.basicFormLevel = currentFormLevel;
        }

        @Override
        public void setPreviousAbility1(AbilityManager.AbilityOption1 ability1) {
            this.basicPreviousAbility1 = ability1;
        }

        @Override
        public void setPreviousAbility2(AbilityManager.AbilityOption2 ability2) {
            this.basicPreviousAbility2 = ability2;
        }

        @Override
        public void setPreviousAbility3(AbilityManager.AbilityOption3 ability3) {

        }

        @Override
        public AbilityManager.AbilityOption1 getPreviousAbility1() {
            return this.basicPreviousAbility1;
        }

        @Override
        public AbilityManager.AbilityOption2 getPreviousAbility2() {
            return this.basicPreviousAbility2;
        }

        @Override
        public AbilityManager.AbilityOption3 getPreviousAbility3() {
            return AbilityManager.AbilityOption3.NONE;
        }

        @Override
        public int getCurrentFormXp() {
            return this.basicFormXp;
        }

        @Override
        public int getCurrentFormXpMAX() {
            if (this.basicFormXpMAX == 0) {
                this.basicFormXpMAX = KenjisCombatFormsCommon.MAX_FORM_STARTING_XP.get();
            }
            return this.basicFormXpMAX;
        }

        @Override
        public void setCurrentFormXp(int amount) {
            this.basicFormXp = amount;
        }

        @Override
        public void setCurrentFormXpMAX(int amount) {
            this.basicFormXpMAX = amount;
        }
    }


    public static class CurrentFormLevelStrategy implements FormLevelStrategy {
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
    }

    public static class BasicFormAbilityStrategy implements FormAbilityStrategy {
        @Override
        public void setChosenAbility1(Player player, AbilityManager.AbilityOption1 ability, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());

            abilityData.chosenAbility1 = ability;

                abilityData.chosenAbility2 = AbilityManager.AbilityOption2.NONE;
                abilityData.chosenFinal = AbilityManager.AbilityOption3.NONE;


            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }
        @Override
        public void setChosenAbility2(Player player, AbilityManager.AbilityOption2 ability, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());

            abilityData.chosenAbility2 = ability;

                abilityData.chosenAbility1 = AbilityManager.AbilityOption1.NONE;
                abilityData.chosenFinal = AbilityManager.AbilityOption3.NONE;

            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }
        @Override
        public void storeChosenAbility1(Player player, AbilityManager.AbilityOption1 ability, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());


            formData.setPreviousAbility1(ability);


            formData.setPreviousAbility2(AbilityManager.AbilityOption2.NONE);
            formData.setPreviousAbility3(AbilityManager.AbilityOption3.NONE);

            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }
        @Override
        public void storeChosenAbility2(Player player, AbilityManager.AbilityOption2 ability, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());


            formData.setPreviousAbility2(ability);

            formData.setPreviousAbility1(AbilityManager.AbilityOption1.NONE);
            formData.setPreviousAbility3(AbilityManager.AbilityOption3.NONE);


            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }

        @Override
        public void storeChooseFinalAbility(Player player, AbilityManager.AbilityOption3 ability, AbilityManager.PlayerAbilityData abilityData) {
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));

        }

        @Override
        public void setChooseFinalAbility(Player player, AbilityManager.AbilityOption3 ability, AbilityManager.PlayerAbilityData abilityData) {
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }

        @Override
        public void setStoredChosenAbilities(Player player, AbilityManager.AbilityOption1 ability, AbilityManager.AbilityOption2 ability2, AbilityManager.AbilityOption3 ability3, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());
            if(ability != AbilityManager.AbilityOption1.NONE) {
                abilityData.chosenAbility1 = ability;
            }
            if(ability2 != AbilityManager.AbilityOption2.NONE) {
                abilityData.chosenAbility2 = ability2;
            }
            if(ability3 != AbilityManager.AbilityOption3.NONE) {
                abilityData.chosenFinal = ability3;
            }
            if(ability == AbilityManager.AbilityOption1.NONE){
                abilityData.chosenAbility1 = AbilityManager.AbilityOption1.NONE;
            }
            if(ability2 == AbilityManager.AbilityOption2.NONE){
                abilityData.chosenAbility2 = AbilityManager.AbilityOption2.NONE;
            }
            if(ability3 == AbilityManager.AbilityOption3.NONE){
                abilityData.chosenFinal= AbilityManager.AbilityOption3.NONE;
            }
        }

        @Override
        public void setLearnedFinalAbility(Player player, AbilityManager.AbilityOption3 ability, boolean hasLearnedAbility) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());
            formData.setStoredAbility3(ability);
            getInstance().syncDataToClient(player);
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }
    }



    public BasicFormData getOrCreateFormData(Player player){
        return playerDataMap.computeIfAbsent(player.getUUID(), k -> new BasicFormData());
    }
}
