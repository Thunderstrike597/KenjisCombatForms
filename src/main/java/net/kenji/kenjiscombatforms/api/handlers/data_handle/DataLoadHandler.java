package net.kenji.kenjiscombatforms.api.handlers.data_handle;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.AbilityChangeHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.forms.BasicForm;
import net.kenji.kenjiscombatforms.api.managers.forms.VoidForm;
import net.kenji.kenjiscombatforms.api.managers.forms.WitherForm;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.client_data.SyncClientFormsPacket;
import net.kenji.kenjiscombatforms.network.fist_forms.client_data.SyncClientAbilityPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DataLoadHandler {



    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (!event.getLevel().isClientSide()) {
            ServerLevel level = (ServerLevel) event.getLevel();
            SavedDataHandler.get(level); // This ensures the data is loaded
        }
    }


    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        ServerLevel level = event.getServer().getLevel(Level.OVERWORLD);
        SavedDataHandler savedData = SavedDataHandler.get(level);
        savedData.setDirty();
        level.getDataStorage().save();

    }
    @SubscribeEvent
    public static void onWorldSave(LevelEvent.Save event) {
        if (!event.getLevel().isClientSide()) {
            ServerLevel level = (ServerLevel) event.getLevel();
            SavedDataHandler savedData = SavedDataHandler.get(level);
            savedData.setDirty();
            savedData.forceSave(level);
            level.getDataStorage().save();
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.getEntity().level().isClientSide) {
            ServerLevel level = (ServerLevel) event.getEntity().level();
            SavedDataHandler.get(level).setDirty();
            level.getDataStorage().save();
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            SavedDataHandler savedData = SavedDataHandler.get(serverPlayer.serverLevel());
            CompoundTag playerData = savedData.getPlayerData(serverPlayer.getUUID());

            // Load player data
            loadPlayerData(serverPlayer, playerData);
            // Sync data to client
            syncDataToClient(serverPlayer);
        }
    }



    public static void requestDataSync(ServerPlayer player) {
        SavedDataHandler savedData = SavedDataHandler.get(player.serverLevel());
        CompoundTag playerData = savedData.getPlayerData(player.getUUID());
        loadPlayerData(player, playerData);
    }


    private static void loadPlayerData(ServerPlayer player, CompoundTag savedData) {
        UUID playerUUID = player.getUUID();
        AbilityManager.PlayerAbilityData playerAbilityData = AbilityManager.getInstance().getOrCreatePlayerAbilityData(player);
        FormManager.PlayerFormData playerFormData = FormManager.getInstance().getOrCreatePlayerFormData(player);
        VoidForm.FormData voidFormData = VoidForm.getInstance().getOrCreateFormData(player);
        WitherForm.FormData witherFormData = WitherForm.getInstance().getOrCreateFormData(player);
        BasicForm.BasicFormData basicFormData = BasicForm.getInstance().getOrCreateFormData(player);
        FormLevelManager.PlayerFormLevelData formLevelData = FormLevelManager.getInstance().getOrCreatePlayerLevelData(player);

        try {
            SavedDataHandler.triggerPlayerDataLoad(playerUUID, playerAbilityData, playerFormData, savedData);

            // These puts might not be necessary if the getOrCreate methods above already store the instances
            AbilityManager.getInstance().playerDataMap.put(playerUUID, playerAbilityData);
            FormManager.getInstance().playerDataMap.put(playerUUID, playerFormData);
            VoidForm.getInstance().playerDataMap.put(playerUUID, voidFormData);
            WitherForm.getInstance().playerDataMap.put(playerUUID, witherFormData);
            BasicForm.getInstance().playerDataMap.put(playerUUID, basicFormData);
            FormLevelManager.getInstance().playerDataMap.put(playerUUID, formLevelData);
        } catch (Exception e) {
            System.out.println("Error loading data for player: " + playerUUID);
        }
        Form currentForm = FormManager.getInstance().getForm(playerFormData.selectedForm.name());
        AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());

        AbilityChangeHandler.getInstance().setFormsAndAbilities(player, currentFormData);
    }


    private static void syncDataToClient(Player player) {
        AbstractFormData voidFormData = VoidForm.getInstance().getFormData(player.getUUID());
        AbstractFormData witherFormData = WitherForm.getInstance().getFormData(player.getUUID());
        FormManager.PlayerFormData formData = FormManager.getInstance().getFormData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        Form currentForm = FormManager.getInstance().getForm(formData.selectedForm.name());
        AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());

        if(player instanceof ServerPlayer serverPlayer) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new SyncClientFormsPacket(formData.form1, formData.form2, formData.form3, currentFormData.getCurrentFormLevel(), currentFormData.getCurrentFormXp(),currentFormData.getCurrentFormXpMAX())
            );
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new SyncClientAbilityPacket(abilityData.ability1, abilityData.ability2, abilityData.ability3, abilityData.chosenAbility1, abilityData.chosenAbility2, abilityData.chosenFinal, formData.selectedForm)
            );
        }
    }
}
