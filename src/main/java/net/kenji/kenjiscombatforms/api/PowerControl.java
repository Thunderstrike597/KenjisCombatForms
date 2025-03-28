package net.kenji.kenjiscombatforms.api;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.ClientEventHandler;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.kenji.kenjiscombatforms.keybinds.ModKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PowerControl {
    private static final long PRESS_COOLDOWN = 4;

    @Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT)
    public static class controlRelatedEvents {

        public Map<UUID, controlRelatedEvents.PlayerData> playerDataMap = new ConcurrentHashMap<>();
        private static final int INITIAL_PRESS_COUNTER = 90;// Adjust as needed
        WitherPlayerDataSets dataSets = WitherPlayerDataSets.getInstance();
        private static final controlRelatedEvents INSTANCE = new controlRelatedEvents();

        public static controlRelatedEvents getInstance() {
            return INSTANCE;
        }

        public static class PlayerData {
            public int currentAbilityIndex = 1;

            public boolean isShiftDown = false;
            public  long lastPressTime = 0;
            public long currentDashState = 0;
            public long pressCounter = INITIAL_PRESS_COUNTER;
            public long tpPressCounter = INITIAL_PRESS_COUNTER;
        }




        public controlRelatedEvents.PlayerData getOrCreatePlayerData(Player player) {
            return getInstance().playerDataMap.computeIfAbsent(player.getUUID(), k -> new controlRelatedEvents.PlayerData());
        }

        private WitherPlayerDataSets.WitherFormPlayerData getOrCreateWitherFormPlayerData(Player player) {
            return dataSets.getOrCreateWitherFormPlayerData(player);
        }

        private WitherPlayerDataSets.WitherMinionPlayerData getOrCreateMinionPlayerData(Player player) {
            return dataSets.getOrCreateMinionPlayerData(player);
        }


        public static boolean getCanUseAbilitiesWithoutForms(Player player){
            if(!(player.getMainHandItem().getItem() instanceof BaseFistClass)){
                return EpicFightCombatFormsCommon.CAN_USE_ABILITIES_NO_FORM.get();
            }
            return true;
        }

        boolean getAbilitiesNotActive(Player player, AbilityManager.PlayerAbilityData abilityData) {
            Ability[] abilities = {
                    AbilityManager.getInstance().getAbility(abilityData.chosenAbility1),
                    AbilityManager.getInstance().getAbility(abilityData.chosenAbility2),
                  //  AbilityManager.getInstance().getAbility(abilityData.chosenFinal.name())
            };

            for (Ability ability : abilities) {
                if (ability != null && ability.getAbilityData(player).isAbilityActive()) {
                    return false; // If any ability is active, return false
                }
            }
            return true; // If no ability is active, return true
        }

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            Minecraft mc = Minecraft.getInstance();
            Player clientPlayer = Minecraft.getInstance().player;




            if (clientPlayer != null) {
                WitherPlayerDataSets.WitherDashPlayerData wData = getInstance().dataSets.getOrCreateDashPlayerData(clientPlayer);
                EnderPlayerDataSets.TeleportPlayerData tpData = EnderPlayerDataSets.getInstance().getOrCreateTeleportPlayerData(clientPlayer);
                AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(clientPlayer);

               Ability ability1 = AbilityManager.getInstance().getCurrentAbilities(clientPlayer).get(0);
                Ability ability2 = AbilityManager.getInstance().getCurrentAbilities(clientPlayer).get(1);
                Ability ability3 = AbilityManager.getInstance().getCurrentAbilities(clientPlayer).get(2);
                FinalAbility ability4 = AbilityManager.getInstance().getCurrentFinalAbilities(clientPlayer).get(0);
                FinalAbility ability5 = AbilityManager.getInstance().getCurrentFinalAbilities(clientPlayer).get(1);

                if(ability1 != null) {
                    System.out.println(" Ability1 Cooldown: " + ability1.getAbilityData(clientPlayer).getClientAbilityCooldown());
                }

                if(ModKeybinds.SWITCH_CURRENT_ABILITY_KEY.isDown()){
                    PlayerData data = getInstance().getOrCreatePlayerData(clientPlayer);

                    long currentTime = System.currentTimeMillis();
                    if (currentTime - data.lastPressTime > PRESS_COOLDOWN) {
                     //   if(!ClientEventHandler.getInstance().getAreFinalsActive()) {
                            if(getInstance().getAbilitiesNotActive(clientPlayer, abilityData)) {
                                if (data.currentAbilityIndex <= 2) {
                                    data.currentAbilityIndex++;
                                }
                                else data.currentAbilityIndex = 1;
                            }
                    //    }
                    }
                    clientPlayer.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);
                    System.out.println("CurrentAbilityIndex: " + data.currentAbilityIndex);
                }

                if(!EpicFightCombatFormsCommon.ABILITY_SELECTION_MODE.get()) {
                    if (getCanUseAbilitiesWithoutForms(clientPlayer)) {

                        PlayerData data = getInstance().getOrCreatePlayerData(clientPlayer);
                        WitherPlayerDataSets.WitherFormPlayerData wPlayerData = getInstance().getOrCreateWitherFormPlayerData(clientPlayer);
                        WitherPlayerDataSets.WitherMinionPlayerData wmPlayerData = getInstance().getOrCreateMinionPlayerData(clientPlayer);
                        ClientEventHandler clientEventHandler = ClientEventHandler.getInstance();

                        boolean areFinalActive = clientEventHandler.getAreFinalsActive();
                        if (ModKeybinds.ABILITY1_KEY.consumeClick()) {
                            long currentTime = System.currentTimeMillis();
                            if (currentTime - data.lastPressTime > PRESS_COOLDOWN) {
                                if (ability1 != null && !areFinalActive){
                                    if(ability4 != null && !ability4.getAbilityActive(clientPlayer)) {
                                        ability1.sendPacketToServer(clientPlayer);
                                    }else if(ability4 == null){
                                        ability1.sendPacketToServer(clientPlayer);
                                    }
                                    if(ability4 != null && ability4.getFinalAbilityActive(clientPlayer) | ability4.getAbilityActive(clientPlayer)){
                                        ability4.sendPacketToServer(clientPlayer);
                                    }
                                }
                            }
                        }
                        if (ModKeybinds.ABILITY2_KEY.consumeClick()) {
                            long currentTime = System.currentTimeMillis();
                            if (currentTime - data.lastPressTime > PRESS_COOLDOWN) {
                                    if (ability2 != null && !areFinalActive) {
                                        if (ability5 != null && !ability5.getAbilityActive(clientPlayer)) {
                                            ability2.sendPacketToServer(clientPlayer);
                                        } else if (ability5 == null) {
                                            ability2.sendPacketToServer(clientPlayer);
                                        }
                                    }
                                    if (ability5 != null && ability5.getFinalAbilityActive(clientPlayer) | ability5.getAbilityActive(clientPlayer)) {
                                        ability5.sendPacketToServer(clientPlayer);
                                    }
                                    data.lastPressTime = currentTime;
                            }
                        }
                        if (ModKeybinds.ABILITY3_KEY.consumeClick()) {
                            long currentTime = System.currentTimeMillis();
                            if (currentTime - data.lastPressTime > PRESS_COOLDOWN) {
                                if (ability3 != null) {
                                    ability3.sendPacketToServer(clientPlayer);
                                }
                            }
                        }
                    }
                }

                else if (ModKeybinds.ACTIVATE_CURRENT_ABILITY_KEY.consumeClick()) {
                    PlayerData data = getInstance().getOrCreatePlayerData(clientPlayer);
                    WitherPlayerDataSets.WitherFormPlayerData wPlayerData = getInstance().getOrCreateWitherFormPlayerData(clientPlayer);
                    WitherPlayerDataSets.WitherMinionPlayerData wmPlayerData = getInstance().getOrCreateMinionPlayerData(clientPlayer);
                   ClientEventHandler clientEventHandler = ClientEventHandler.getInstance();
                   boolean areFinalActive = clientEventHandler.getAreFinalsActive();
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - data.lastPressTime > PRESS_COOLDOWN) {
                        if (data.currentAbilityIndex == 1) {

                            if (ability1 != null && !areFinalActive){
                                if(ability4 != null && !ability4.getAbilityActive(clientPlayer)) {
                                    ability1.sendPacketToServer(clientPlayer);

                                }else if(ability4 == null){
                                    ability1.sendPacketToServer(clientPlayer);
                                }
                            }
                            if(ability4 != null && ability4.getFinalAbilityActive(clientPlayer) | ability4.getAbilityActive(clientPlayer)){
                                ability4.sendPacketToServer(clientPlayer);
                            }
                        } else if (data.currentAbilityIndex == 2) {
                            if (ability2 != null && !areFinalActive) {
                                if(ability5 != null && !ability5.getAbilityActive(clientPlayer)) {
                                    ability2.sendPacketToServer(clientPlayer);
                                }else if(ability5 == null){
                                    ability2.sendPacketToServer(clientPlayer);
                                }
                            }
                            if(ability5 != null && ability5.getFinalAbilityActive(clientPlayer) | ability5.getAbilityActive(clientPlayer)){
                                ability5.sendPacketToServer(clientPlayer);
                            }
                        }
                        if (data.currentAbilityIndex == 3) {
                            if (ability3 != null) {
                                ability3.sendPacketToServer(clientPlayer);
                            }
                        }
                        data.lastPressTime = currentTime;
                    }
                }
            }
        }
    }
}
