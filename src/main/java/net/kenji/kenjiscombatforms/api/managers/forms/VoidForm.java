package net.kenji.kenjiscombatforms.api.managers.forms;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.AbilityChangeHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.interfaces.form.FormAbilityStrategy;
import net.kenji.kenjiscombatforms.api.interfaces.form.FormLevelStrategy;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseVoidClass;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.void_form.VoidFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.void_form.VoidFistItem;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.client_data.SyncClientAbilityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public List<ResourceLocation> getLevelResources() {
        return List.of(
                new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/void_lvl1.png"),
                new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/void_lvl2.png"),
                new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/void_lvl3.png"));
    }

    @Override
    public int getGUIDrawPosY() {
        return 169;
    }

    @Override
    public int getGUIDrawPosX() {
        return 90;
    }

    @Override
    public int getGUISwapPos() {
        return 151;
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
            Form currentForm = FormManager.getInstance().getForm(generalFormData.selectedForm);
            AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new SyncClientAbilityPacket(currentFormData.getCurrentStoredAbility1(), currentFormData.getCurrentStoredAbility2(), currentFormData.getStoredAbility3(), abilityData.chosenAbility1, abilityData.chosenAbility2, abilityData.chosenFinal, generalFormData.selectedForm)
            );
        }
    }

    @Override
    public void setCurrentForm(Player player, int slot) {
        List<Form> formValue = FormManager.getInstance().getCurrentForms(player);
        String currentForm = formValue.get(0).getName();

        if(Objects.equals(currentForm, this.getName())){

            VoidFistItem voidFistItem = VoidFistItem.getInstance();
            VoidFist2Item voidFist2Item = VoidFist2Item.getInstance();
            VoidFist2Item voidFist3Item = VoidFist2Item.getInstance();

            AbstractFormData currentFormData = this.getFormData(player.getUUID());

            if (currentFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
                voidFistItem.setVoidFormMainHand(player, slot);
            } else if (currentFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
                voidFist2Item.setVoidFormMainHand(player, slot);
            } else if (currentFormData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL3) {
                voidFist3Item.setVoidFormMainHand(player, slot);
            }
        }
    }

    public static class FormData extends AbstractFormData {
        private String currentStoredAbility1;
        private String currentStoredAbility2;
        private String currentStoredAbility3;

        private FormLevelManager.FormLevel currentFormLevel;
        private int currentFormXp;
        private int currentFormXpMAX;

        public FormData() {
            this.currentStoredAbility1 = "NONE";
            this.currentStoredAbility2 = "NONE";
            this.currentStoredAbility3 = "NONE";

            this.currentFormLevel = FormLevelManager.FormLevel.LEVEL1;
            this.currentFormXp = 0;
            this.currentFormXpMAX = EpicFightCombatFormsCommon.MAX_FORM_STARTING_XP.get();
        }

        @Override
        public String getCurrentStoredAbility1() {
            return currentStoredAbility1 != null && !currentStoredAbility1.isEmpty() ? currentStoredAbility1 : "NONE";
        }

        @Override
        public void setCurrentStoredAbility1(String ability) {
            this.currentStoredAbility1 = ability;
        }

        @Override
        public String getCurrentStoredAbility2() {
            return currentStoredAbility2 != null && !currentStoredAbility2.isEmpty() ? currentStoredAbility2 : "NONE";        }

        @Override
        public void setCurrentStoredAbility2(String ability) {
            this.currentStoredAbility2 = ability;
        }

        @Override
        public String getStoredAbility3() {
            return currentStoredAbility3 != null && !currentStoredAbility3.isEmpty() ? currentStoredAbility3 : "NONE";
        }

        @Override
        public void setStoredAbility3(String ability) {
            this.currentStoredAbility3 = ability;
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
        public int getCurrentFormXp() {
            return this.currentFormXp;
        }

        @Override
        public int getCurrentFormXpMAX() {
            if (this.currentFormXpMAX == 0) {
                this.currentFormXpMAX = EpicFightCombatFormsCommon.MAX_FORM_STARTING_XP.get();
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
        public void setChosenAbility1(Player player, String ability, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());

            abilityData.chosenAbility1 = ability;

            getInstance().syncDataToClient(player);
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }

        @Override
        public void setChosenAbility2(Player player, String ability, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());

            abilityData.chosenAbility2 = ability;


            getInstance().syncDataToClient(player);
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }

        @Override
        public void setChooseFinalAbility(Player player, String ability, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());

            abilityData.chosenFinal = ability;

            getInstance().syncDataToClient(player);
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }


        @Override
        public void setLearnedFinalAbility(Player player, String ability, boolean hasLearnedAbility) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());
            formData.setStoredAbility3(ability);
            AbilityChangeHandler.getInstance().setFormAbility3(player);
            getInstance().syncDataToClient(player);
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }
    }

    public VoidForm.FormData getOrCreateFormData(Player player){
        return playerDataMap.computeIfAbsent(player.getUUID(), k -> new VoidForm.FormData());
    }
}

