package net.kenji.kenjiscombatforms.api.managers.forms;

import net.kenji.kenjiscombatforms.api.handlers.AbilityChangeHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.interfaces.form.FormAbilityStrategy;
import net.kenji.kenjiscombatforms.api.interfaces.form.FormLevelStrategy;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseVoidClass;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.client_data.SyncClientAbilityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VoidForm implements Form {
    public final Map<UUID, FormData> playerDataMap = new ConcurrentHashMap<>();
    private static VoidForm INSTANCE = new VoidForm();


    public static VoidForm getInstance(){
       if(INSTANCE == null){
           INSTANCE = new VoidForm();
       }
        return INSTANCE;
    }

    public void updatePlayerData(UUID playerUUID, FormData data) {
        playerDataMap.put(playerUUID, data);
    }

    @Override
    public String getName() {
        return "VOID";
    }

    @Override
    public AbstractFormData getFormData(UUID player) {
        if (player == null) {
            // Handle the null player case
            // You might want to log this occurrence or return a default AbstractFormData
            return new FormData(); // Or return null, depending on your needs
        }
        return playerDataMap.computeIfAbsent(player, k -> new FormData());
    }
    @Override
    public void updatePlayerData(UUID playerUUID, AbstractFormData formData) {
        if (formData instanceof FormData) {
            playerDataMap.put(playerUUID, (FormData) formData);
        } else {
            throw new IllegalArgumentException("AbstractFormData must be of type FormData");
        }
    }

    @Override
    public void syncDataToClient(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
            FormManager.PlayerFormData generalFormData = FormManager.getInstance().getFormData(player);
            AbstractFormData formData = getFormData(player.getUUID());
            Form currentForm = FormManager.getInstance().getForm(generalFormData.selectedForm.name());
            AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new SyncClientAbilityPacket(abilityData.ability1, abilityData.ability2, abilityData.ability3, abilityData.chosenAbility1, abilityData.chosenAbility2, abilityData.chosenFinal, generalFormData.selectedForm)
            );
        }
    }
    public static class FormData extends AbstractFormData {
        private AbilityManager.AbilityOption1 currentStoredAbility1;
        private AbilityManager.AbilityOption2 currentStoredAbility2;
        private AbilityManager.AbilityOption3 currentFinalAbility3;
        private AbilityManager.AbilityOption1 currentPreviousAbility1;
        private AbilityManager.AbilityOption2 currentPreviousAbility2;
        private AbilityManager.AbilityOption3 currentPreviousAbility3;
        private FormLevelManager.FormLevel currentFormLevel;
        private int currentFormXp;
        private int currentFormXpMAX;


        public FormData() {
            this.currentStoredAbility1 = AbilityManager.AbilityOption1.NONE;
            this.currentStoredAbility2 = AbilityManager.AbilityOption2.NONE;
            this.currentFinalAbility3 = AbilityManager.AbilityOption3.NONE;
            this.currentPreviousAbility1 = AbilityManager.AbilityOption1.NONE;
            this.currentPreviousAbility2 = AbilityManager.AbilityOption2.NONE;
            this.currentPreviousAbility3 = AbilityManager.AbilityOption3.NONE;

            this.currentFormLevel = FormLevelManager.FormLevel.LEVEL1;
            this.currentFormXp = 0;
            this.currentFormXpMAX = KenjisCombatFormsCommon.MAX_FORM_STARTING_XP.get();
        }

        @Override
        public AbilityManager.AbilityOption1 getCurrentStoredAbility1() {
            return this.currentStoredAbility1;
        }

        @Override
        public void setCurrentStoredAbility1(AbilityManager.AbilityOption1 ability) {
            this.currentStoredAbility1 = ability;
        }

        @Override
        public AbilityManager.AbilityOption2 getCurrentStoredAbility2() {
            return this.currentStoredAbility2;
        }

        @Override
        public void setCurrentStoredAbility2(AbilityManager.AbilityOption2 ability) {
            this.currentStoredAbility2 = ability;
        }

        @Override
        public AbilityManager.AbilityOption3 getStoredAbility3() {
            return this.currentFinalAbility3;
        }

        @Override
        public void setStoredAbility3(AbilityManager.AbilityOption3 ability) {
               this.currentFinalAbility3 = ability;
        }



        @Override
        public FormLevelManager.FormLevel getCurrentFormLevel() {
            return this.currentFormLevel;
        }

        @Override
        public void setCurrentFormLevel(FormLevelManager.FormLevel currentFormLevel) {
            this.currentFormLevel = currentFormLevel;
        }

        @Override
        public void setPreviousAbility1(AbilityManager.AbilityOption1 ability1) {
            this.currentPreviousAbility1 = ability1;
        }

        @Override
        public void setPreviousAbility2(AbilityManager.AbilityOption2 ability2) {
            this.currentPreviousAbility2 = ability2;
        }

        @Override
        public void setPreviousAbility3(AbilityManager.AbilityOption3 ability3) {
            this.currentPreviousAbility3 = ability3;
        }

        @Override
        public AbilityManager.AbilityOption1 getPreviousAbility1() {
            return this.currentPreviousAbility1;
        }

        @Override
        public AbilityManager.AbilityOption2 getPreviousAbility2() {
            return this.currentPreviousAbility2;
        }

        @Override
        public AbilityManager.AbilityOption3 getPreviousAbility3() {
            return this.currentPreviousAbility3;
        }

        @Override
        public int getCurrentFormXp() {
            return this.currentFormXp;
        }

        @Override
        public int getCurrentFormXpMAX() {
            if (this.currentFormXpMAX == 0) {
                this.currentFormXpMAX = KenjisCombatFormsCommon.MAX_FORM_STARTING_XP.get();
            }
            return this.currentFormXpMAX;
        }

        @Override
        public void setCurrentFormXp(int amount) {
            this.currentFormXp = amount;
        }

        @Override
        public void setCurrentFormXpMAX(int amount) {
            this.currentFormXpMAX = amount;
        }
    }


    public static class CurrentFormLevelStrategy implements FormLevelStrategy {
        @Override
        public boolean isHoldingForm(ServerPlayer player) {
            return player.getMainHandItem().getItem() instanceof BaseVoidClass;
        }

        @Override
        public void gainFormXp(ServerPlayer player, Entity entity) {
            AbstractFormData voidFormData = getInstance().getFormData(player.getUUID());
            if (entity instanceof Monster || entity instanceof Player) {

                if (isHoldingForm(player)) {
                    if (voidFormData.getCurrentFormXp() < voidFormData.getCurrentFormXpMAX()) {
                        voidFormData.setCurrentFormXp(voidFormData.getCurrentFormXp() + 1);
                        getInstance().syncDataToClient(player);
                    }
                }
            }
        }
    }



    public static class CurrentFormAbilityStrategy implements FormAbilityStrategy {
        @Override
        public void setChosenAbility1(Player player, AbilityManager.AbilityOption1 ability, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());

            abilityData.chosenAbility1 = ability;

            if (formData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
                abilityData.chosenAbility2 = AbilityManager.AbilityOption2.NONE;
                abilityData.chosenFinal = AbilityManager.AbilityOption3.NONE;

            } else if (formData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
                abilityData.chosenFinal = AbilityManager.AbilityOption3.NONE;
            }
            getInstance().syncDataToClient(player);
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }

        @Override
        public void setChosenAbility2(Player player, AbilityManager.AbilityOption2 ability, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());

            abilityData.chosenAbility2 = ability;

            if (formData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
                abilityData.chosenAbility1 = AbilityManager.AbilityOption1.NONE;
                abilityData.chosenFinal = AbilityManager.AbilityOption3.NONE;
            } else if (formData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
                abilityData.chosenFinal = AbilityManager.AbilityOption3.NONE;
            }
            getInstance().syncDataToClient(player);
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }

        @Override
        public void setChooseFinalAbility(Player player, AbilityManager.AbilityOption3 ability, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());

            abilityData.chosenFinal = ability;

            if (formData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
                abilityData.chosenAbility1 = AbilityManager.AbilityOption1.NONE;
                abilityData.chosenAbility2 = AbilityManager.AbilityOption2.NONE;
            } else if (formData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
                abilityData.chosenAbility2 = AbilityManager.AbilityOption2.NONE;
            }
            getInstance().syncDataToClient(player);
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }
        @Override
        public void storeChosenAbility1(Player player, AbilityManager.AbilityOption1 ability, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());

            formData.setPreviousAbility1(ability);

            if (formData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
                formData.setPreviousAbility2(AbilityManager.AbilityOption2.NONE);
                formData.setPreviousAbility3(AbilityManager.AbilityOption3.NONE);

            } else if (formData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
                formData.setPreviousAbility3(AbilityManager.AbilityOption3.NONE);
            }
            getInstance().syncDataToClient(player);
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }

        @Override
        public void storeChosenAbility2(Player player, AbilityManager.AbilityOption2 ability, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());


            formData.setPreviousAbility2(ability);

            if (formData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
                formData.setPreviousAbility1(AbilityManager.AbilityOption1.NONE);
                formData.setPreviousAbility3(AbilityManager.AbilityOption3.NONE);
            } else if (formData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
                formData.setPreviousAbility3(AbilityManager.AbilityOption3.NONE);
            }
            getInstance().syncDataToClient(player);
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }

        @Override
        public void storeChooseFinalAbility(Player player, AbilityManager.AbilityOption3 ability, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());

            formData.setPreviousAbility3(ability);

            if (formData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
                formData.setPreviousAbility1(AbilityManager.AbilityOption1.NONE);
                formData.setPreviousAbility2(AbilityManager.AbilityOption2.NONE);
            } else if (formData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
                formData.setPreviousAbility2(AbilityManager.AbilityOption2.NONE);
            }
            getInstance().syncDataToClient(player);
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }

        @Override
        public void setStoredChosenAbilities(Player player, AbilityManager.AbilityOption1 ability, AbilityManager.AbilityOption2 ability2, AbilityManager.AbilityOption3 ability3, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());
            if(formData.getPreviousAbility1() != AbilityManager.AbilityOption1.NONE) {
                abilityData.chosenAbility1 = ability;
            }
            if(formData.getPreviousAbility2() != AbilityManager.AbilityOption2.NONE) {
                abilityData.chosenAbility2 = ability2;
            }
            if(formData.getPreviousAbility3() != AbilityManager.AbilityOption3.NONE) {
                abilityData.chosenFinal = ability3;
            }
            if(formData.getPreviousAbility1() == AbilityManager.AbilityOption1.NONE){
                abilityData.chosenAbility1 = AbilityManager.AbilityOption1.NONE;
            }
            if(formData.getPreviousAbility2() == AbilityManager.AbilityOption2.NONE){
                abilityData.chosenAbility2 = AbilityManager.AbilityOption2.NONE;
            }
            if(formData.getPreviousAbility3() == AbilityManager.AbilityOption3.NONE){
                abilityData.chosenFinal= AbilityManager.AbilityOption3.NONE;
            }
        }

        @Override
        public void setLearnedFinalAbility(Player player, AbilityManager.AbilityOption3 ability, boolean hasLearnedAbility) {
           AbstractFormData formData = getInstance().getFormData(player.getUUID());
           formData.setStoredAbility3(ability);
            AbilityChangeHandler.getInstance().setFormAbility3(player);
            getInstance().syncDataToClient(player);
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }
    }




    public FormData getOrCreateFormData(Player player){
        return playerDataMap.computeIfAbsent(player.getUUID(), k -> new FormData());
    }
}
