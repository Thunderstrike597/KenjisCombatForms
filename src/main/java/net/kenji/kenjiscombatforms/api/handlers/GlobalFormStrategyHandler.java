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

import net.kenji.kenjiscombatforms.network.fist_forms.client_data.SyncClientFormsPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GlobalFormStrategyHandler {

    private static final GlobalFormStrategyHandler INSTANCE = new GlobalFormStrategyHandler();

   public static GlobalFormStrategyHandler getInstance(){
       return INSTANCE;
   }



    private void syncDataToClient(Player player) {
        List<Form> forms = FormManager.getInstance().getCurrentForms(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        Form currentForm = FormManager.getInstance().getForm(forms.get(0).getName());
        AbstractFormData currentFormData = currentForm.getFormData(player.getUUID());
        if(player instanceof ServerPlayer serverPlayer) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new SyncClientFormsPacket(forms.get(1).getName(), forms.get(2).getName(), forms.get(3).getName(), currentFormData.getCurrentFormLevel(), currentFormData.getCurrentFormXp(), currentFormData.getCurrentFormXpMAX())
            );
            currentForm.syncDataToClient(player);
        }
    }
}
