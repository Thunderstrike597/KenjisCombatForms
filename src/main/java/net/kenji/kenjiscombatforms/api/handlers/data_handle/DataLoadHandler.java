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

import java.util.List;
import java.util.Objects;
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


            loadPlayerData(serverPlayer, playerData);

            syncDataToClient(serverPlayer);
        }
    }



    public static void requestDataSync(ServerPlayer player) {
        SavedDataHandler savedData = SavedDataHandler.get(player.serverLevel());
        CompoundTag playerData = savedData.getPlayerData(player.getUUID());
        loadPlayerData(player, playerData);
    }

    static Form getCurrentForm(FormManager.PlayerFormData playerFormData){
       if(playerFormData.selectedForm != null) {
           return FormManager.getInstance().getForm(playerFormData.selectedForm);
       }
       playerFormData.selectedForm = BasicForm.getInstance().getName();
       return FormManager.getInstance().getForm(playerFormData.selectedForm);
    }

    private static void loadPlayerData(ServerPlayer player, CompoundTag savedData) {
        UUID playerUUID = player.getUUID();
        AbilityManager.PlayerAbilityData playerAbilityData = AbilityManager.getInstance().getOrCreatePlayerAbilityData(player);
        FormManager.PlayerFormData playerFormData = FormManager.getInstance().getOrCreatePlayerFormData(player);
        FormLevelManager.PlayerFormLevelData formLevelData = FormLevelManager.getInstance().getOrCreatePlayerLevelData(player);

            SavedDataHandler.triggerPlayerDataLoad(playerUUID, playerAbilityData, playerFormData, savedData);
            // These puts might not be necessary if the getOrCreate methods above already store the instances
            AbilityManager.getInstance().playerDataMap.put(playerUUID, playerAbilityData);
            FormManager.getInstance().playerDataMap.put(playerUUID, playerFormData);


            if(getCurrentForm(playerFormData) != null) {
                AbstractFormData currentFormData = getCurrentForm(playerFormData).getFormData(player.getUUID());
                AbilityChangeHandler.getInstance().setFormsAndAbilities(player, currentFormData);
            }
    }


    private static void syncDataToClient(Player player) {
        List<Form> formValue = FormManager.getInstance().getCurrentForms(player);
        List<AbstractFormData> formData = FormManager.getInstance().getCurrentFormData(player);


        Form currentForm = FormManager.getInstance().getForm(formValue.get(0).getName());
        AbstractFormData currentFormData = formData.get(0);



        Form basicForm = FormManager.getInstance().getForm(BasicForm.getInstance().getName());
        if(!Objects.equals(formValue.get(1).getName(), "NONE") && formValue.get(1) != null) {
            Form form1 = FormManager.getInstance().getForm(formValue.get(1).getName());
            if(form1 != null) {
                form1.syncDataToClient(player);
            }
        }if(!Objects.equals(formValue.get(2).getName(), "NONE") && formValue.get(2) != null) {
            Form form2 = FormManager.getInstance().getForm(formValue.get(2).getName());
            if(form2 != null) {
                form2.syncDataToClient(player);
            }
        }if(!Objects.equals(formValue.get(3).getName(), "NONE") && formValue.get(3) != null) {
            Form form3 = FormManager.getInstance().getForm(formValue.get(3).getName());
            if(form3 != null) {
                form3.syncDataToClient(player);
            }
        }



            if(player instanceof ServerPlayer serverPlayer) {
                NetworkHandler.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new SyncClientFormsPacket(formValue.get(1).getName(), formValue.get(2).getName(), formValue.get(3).getName(), currentFormData.getCurrentFormLevel(), currentFormData.getCurrentFormXp(), currentFormData.getCurrentFormXpMAX())
                );
                currentForm.syncDataToClient(player);
            }
           basicForm.syncDataToClient(player);
    }
}
