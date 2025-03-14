package net.kenji.kenjiscombatforms.api.handlers;


import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.data_handle.SavedDataHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.interfaces.form.FormAbilityStrategy;
import net.kenji.kenjiscombatforms.api.interfaces.form.FormLevelStrategy;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.forms.*;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.client_data.SyncClientAbilityPacket;
import net.kenji.kenjiscombatforms.network.fist_forms.client_data.SyncClientFormsPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GlobalFormStrategyHandler {

    private static final GlobalFormStrategyHandler INSTANCE = new GlobalFormStrategyHandler();

   public static GlobalFormStrategyHandler getInstance(){
       return INSTANCE;
   }

    public void setStoredChosenAbilities(Player player, AbilityManager.AbilityOption1 ability, AbilityManager.AbilityOption2 ability2, AbilityManager.AbilityOption3 ability3) {
        FormLevelManager.PlayerFormLevelData levelData = LevelHandler.getInstance().getOrCreatePlayerLevelData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);

        FormAbilityStrategy strategy = switch (formData.selectedForm) {
            case VOID -> new VoidForm.CurrentFormAbilityStrategy();
            case WITHER -> new WitherForm.CurrentFormAbilityStrategy();
            case BASIC -> new BasicForm.BasicFormAbilityStrategy();
            case SWIFT -> new SwiftForm.CurrentFormAbilityStrategy();
            case POWER -> new PowerForm.CurrentFormAbilityStrategy();
            default -> throw new IllegalStateException("Unexpected form: " + formData.selectedForm);
        };
        if(player instanceof ServerPlayer serverPlayer){
            SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
            savedData.updatePlayerData(player.getUUID());

            savedData.setDirty();
        }

        strategy.setStoredChosenAbilities(player, ability, ability2, ability3, abilityData);
        syncDataToClient(player);
        AbilityManager.getInstance().updatePlayerData(player.getUUID(), abilityData);
    }


    public void setChosenAbility1(Player player, AbilityManager.AbilityOption1 ability) {
        FormLevelManager.PlayerFormLevelData levelData = LevelHandler.getInstance().getOrCreatePlayerLevelData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);

        FormAbilityStrategy strategy = switch (formData.selectedForm) {
            case VOID -> new VoidForm.CurrentFormAbilityStrategy();
            case WITHER -> new WitherForm.CurrentFormAbilityStrategy();
            case BASIC -> new BasicForm.BasicFormAbilityStrategy();
            case SWIFT -> new SwiftForm.CurrentFormAbilityStrategy();
            case POWER -> new PowerForm.CurrentFormAbilityStrategy();
            default -> throw new IllegalStateException("Unexpected form: " + formData.selectedForm);
        };
        if(player instanceof ServerPlayer serverPlayer){
            SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
            savedData.updatePlayerData(player.getUUID());

            savedData.setDirty();
        }
        strategy.setChosenAbility1(player, ability, abilityData);
        syncDataToClient(player);
        AbilityManager.getInstance().updatePlayerData(player.getUUID(), abilityData);
    }


    public void setChosenAbility2(Player player, AbilityManager.AbilityOption2 ability) {
        FormLevelManager.PlayerFormLevelData levelData = LevelHandler.getInstance().getOrCreatePlayerLevelData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);

        FormAbilityStrategy strategy = switch (formData.selectedForm) {
            case VOID -> new VoidForm.CurrentFormAbilityStrategy();
            case WITHER -> new WitherForm.CurrentFormAbilityStrategy();
            case BASIC -> new BasicForm.BasicFormAbilityStrategy();
            case SWIFT -> new SwiftForm.CurrentFormAbilityStrategy();
            case POWER -> new PowerForm.CurrentFormAbilityStrategy();
            default -> throw new IllegalStateException("Unexpected form: " + formData.selectedForm);
        };
        if(player instanceof ServerPlayer serverPlayer){
            SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
            savedData.updatePlayerData(player.getUUID());

            savedData.setDirty();
        }
        strategy.setChosenAbility2(player, ability, abilityData);
        syncDataToClient(player);
        AbilityManager.getInstance().updatePlayerData(player.getUUID(), abilityData);
    }


    public void setChosenAbility3(Player player, AbilityManager.AbilityOption3 ability) {
        FormLevelManager.PlayerFormLevelData levelData = LevelHandler.getInstance().getOrCreatePlayerLevelData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);

        FormAbilityStrategy strategy = switch (formData.selectedForm) {
            case VOID -> new VoidForm.CurrentFormAbilityStrategy();
            case WITHER -> new WitherForm.CurrentFormAbilityStrategy();
            case BASIC -> new BasicForm.BasicFormAbilityStrategy();
            case SWIFT -> new SwiftForm.CurrentFormAbilityStrategy();
            case POWER -> new PowerForm.CurrentFormAbilityStrategy();
            default -> throw new IllegalStateException("Unexpected form: " + formData.selectedForm);
        };
        if(player instanceof ServerPlayer serverPlayer){
            SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
            savedData.updatePlayerData(player.getUUID());

            savedData.setDirty();
        }
        strategy.setChooseFinalAbility(player, ability, abilityData);
        syncDataToClient(player);
        AbilityManager.getInstance().updatePlayerData(player.getUUID(), abilityData);
    }


    public void setPreviouslyChosenAbility1(Player player, AbilityManager.AbilityOption1 ability) {
        FormLevelManager.PlayerFormLevelData levelData = LevelHandler.getInstance().getOrCreatePlayerLevelData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);

        FormAbilityStrategy strategy = switch (formData.selectedForm) {
            case VOID -> new VoidForm.CurrentFormAbilityStrategy();
            case WITHER -> new WitherForm.CurrentFormAbilityStrategy();
            case BASIC -> new BasicForm.BasicFormAbilityStrategy();
            case SWIFT -> new SwiftForm.CurrentFormAbilityStrategy();
            case POWER -> new PowerForm.CurrentFormAbilityStrategy();
            default -> throw new IllegalStateException("Unexpected form: " + formData.selectedForm);
        };
        if(player instanceof ServerPlayer serverPlayer){
            SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
            savedData.updatePlayerData(player.getUUID());

            savedData.setDirty();
        }
        strategy.storeChosenAbility1(player, ability, abilityData);
        syncDataToClient(player);
        AbilityManager.getInstance().updatePlayerData(player.getUUID(), abilityData);
    }


    public void setPreviouslyChosenAbility2(Player player, AbilityManager.AbilityOption2 ability) {
        FormLevelManager.PlayerFormLevelData levelData = LevelHandler.getInstance().getOrCreatePlayerLevelData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);

        FormAbilityStrategy strategy = switch (formData.selectedForm) {
            case VOID -> new VoidForm.CurrentFormAbilityStrategy();
            case WITHER -> new WitherForm.CurrentFormAbilityStrategy();
            case BASIC -> new BasicForm.BasicFormAbilityStrategy();
            case SWIFT -> new SwiftForm.CurrentFormAbilityStrategy();
            case POWER -> new PowerForm.CurrentFormAbilityStrategy();
            default -> throw new IllegalStateException("Unexpected form: " + formData.selectedForm);
        };
        if(player instanceof ServerPlayer serverPlayer){
            SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
            savedData.updatePlayerData(player.getUUID());

            savedData.setDirty();
        }
        strategy.storeChosenAbility2(player, ability, abilityData);
        syncDataToClient(player);
        AbilityManager.getInstance().updatePlayerData(player.getUUID(), abilityData);
    }


    public void setPreviouslyChosenAbility3(Player player, AbilityManager.AbilityOption3 ability) {
        FormLevelManager.PlayerFormLevelData levelData = LevelHandler.getInstance().getOrCreatePlayerLevelData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);

        FormAbilityStrategy strategy = switch (formData.selectedForm) {
            case VOID -> new VoidForm.CurrentFormAbilityStrategy();
            case WITHER -> new WitherForm.CurrentFormAbilityStrategy();
            case BASIC -> new BasicForm.BasicFormAbilityStrategy();
            case SWIFT -> new SwiftForm.CurrentFormAbilityStrategy();
            case POWER -> new PowerForm.CurrentFormAbilityStrategy();
            default -> throw new IllegalStateException("Unexpected form: " + formData.selectedForm);
        };
        if(player instanceof ServerPlayer serverPlayer){
            SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
            savedData.updatePlayerData(player.getUUID());

            savedData.setDirty();
        }
        strategy.storeChooseFinalAbility(player, ability, abilityData);
        syncDataToClient(player);
        AbilityManager.getInstance().updatePlayerData(player.getUUID(), abilityData);
    }


    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event){
        if(event.getSource().getEntity() instanceof ServerPlayer player){
            FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);
            Entity entity = event.getEntity();
            FormLevelStrategy strategy = switch (formData.selectedForm) {
                case BASIC -> new BasicForm.CurrentFormLevelStrategy();
                case VOID -> new VoidForm.CurrentFormLevelStrategy();
                case WITHER -> new WitherForm.CurrentFormLevelStrategy();
                case SWIFT -> new SwiftForm.CurrentFormLevelStrategy();
                case POWER -> new PowerForm.CurrentFormLevelStrategy();
                default -> throw new IllegalStateException("Unexpected form: " + formData.selectedForm);
            };
                SavedDataHandler savedData = SavedDataHandler.get(player.serverLevel());
                savedData.updatePlayerData(player.getUUID());

                savedData.setDirty();
            strategy.gainFormXp(player, entity);
            getInstance().syncDataToClient(player);
        }
    }

    private void syncDataToClient(Player player) {
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        Form currentForm = FormManager.getInstance().getForm(formData.selectedForm.name());
        AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());
        if(player instanceof ServerPlayer serverPlayer) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new SyncClientAbilityPacket(abilityData.ability1, abilityData.ability2, abilityData.ability3, abilityData.chosenAbility1, abilityData.chosenAbility2, abilityData.chosenFinal, formData.selectedForm)
            );
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new SyncClientFormsPacket(formData.form1, formData.form2, formData.form3, currentFormData.getCurrentFormLevel(), currentFormData.getCurrentFormXp(), currentFormData.getCurrentFormXpMAX())
            );
        }
    }
}
