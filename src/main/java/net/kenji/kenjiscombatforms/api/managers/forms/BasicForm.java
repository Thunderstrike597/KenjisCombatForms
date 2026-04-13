package net.kenji.kenjiscombatforms.api.managers.forms;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.FormLevelStrategy;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.interfaces.form.FormAbilityStrategy;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.kenji.kenjiscombatforms.gameasset.CombatFistCapabilityPresets;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseBasicClass;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFistItem;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.client_data.SyncClientBasicFistPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

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
    public List<ResourceLocation> getLevelResources() {
        return List.of(

                new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/level_1.png"),
                new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/level_2.png"),
                new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/level_3.png"));
    }

    @Override
    public int getGUIDrawPosY() {
        return 148;
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

    @Override
    public void setCurrentForm(Player player, int slot) {
        List<Form> formValue = FormManager.getInstance().getCurrentForms(player);
        String currentForm = formValue.get(0).getName();

    }

    @Override
    public ItemStack getFormItem(UUID playerId) {
      switch (getFormData(playerId).getCurrentFormLevel()) {
          case LEVEL2:
              return ModItems.BASIC_FIST2_ITEM.get().getDefaultInstance();
          case LEVEL3:
              return ModItems.BASIC_FIST3_ITEM.get().getDefaultInstance();
          default: return ModItems.BASIC_FIST_ITEM.get().getDefaultInstance();
      }
    }

    @Override
    public Skill getFormSkill(Player player) {
        CapabilityItem formCap = EpicFightCapabilities.getItemStackCapability(getFormItem(player.getUUID()));

        return formCap.getInnateSkill(EpicFightCapabilities.getPlayerPatch(player), getFormItem(player.getUUID()));
    }

    public static class BasicFormData extends AbstractFormData {
        private String basicStoredAbility1;
        private String basicStoredAbility2;

        private FormLevelManager.FormLevel basicFormLevel;
        private int basicFormXp;
        private int basicFormXpMAX;

        public BasicFormData() {
            this.basicStoredAbility1 = "NONE";
            this.basicStoredAbility2 = "NONE";

            this.basicFormLevel = FormLevelManager.FormLevel.LEVEL1;
            this.basicFormXp = 0;
            this.basicFormXpMAX = EpicFightCombatFormsCommon.MAX_FORM_STARTING_XP.get();
        }

        @Override
        public String getCurrentStoredAbility1() {
                return basicStoredAbility1 != null && !basicStoredAbility1.isEmpty() ? basicStoredAbility1 : "NONE";
        }

        @Override
        public void setCurrentStoredAbility1(String ability) {
                this.basicStoredAbility1 = ability;
        }

        @Override
        public String getCurrentStoredAbility2() {
            return basicStoredAbility2 != null && !basicStoredAbility2.isEmpty() ? basicStoredAbility2 : "NONE";
        }

        @Override
        public void setCurrentStoredAbility2(String ability) {
            this.basicStoredAbility2 = ability;
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
            return this.basicFormLevel;
        }

        @Override
        public void setCurrentFormLevel(FormLevelManager.FormLevel currentFormLevel) {
            this.basicFormLevel = currentFormLevel;
        }


        @Override
        public int getCurrentFormXp() {
            return this.basicFormXp;
        }

        @Override
        public int getCurrentFormXpMAX() {
            if (this.basicFormXpMAX == 0) {
                this.basicFormXpMAX = EpicFightCombatFormsCommon.MAX_FORM_STARTING_XP.get();
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
        public void setChosenAbility1(Player player, String ability, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());

            abilityData.chosenAbility1 = ability;


            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }
        @Override
        public void setChosenAbility2(Player player, String ability, AbilityManager.PlayerAbilityData abilityData) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());

            abilityData.chosenAbility2 = ability;

            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }

        @Override
        public void setChooseFinalAbility(Player player, String ability, AbilityManager.PlayerAbilityData abilityData) {
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }


        @Override
        public void setLearnedFinalAbility(Player player, String ability, boolean hasLearnedAbility) {
            AbstractFormData formData = getInstance().getFormData(player.getUUID());
            getInstance().syncDataToClient(player);
            getInstance().updatePlayerData(player.getUUID(), getInstance().getOrCreateFormData(player));
        }
    }



    public BasicFormData getOrCreateFormData(Player player){
        return playerDataMap.computeIfAbsent(player.getUUID(), k -> new BasicFormData());
    }
}
