package net.kenji.kenjiscombatforms.api;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.handlers.AbilityChangeHandler;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.powers.VoidPowers.EnderFormAbility;
import net.kenji.kenjiscombatforms.api.powers.VoidPowers.EnderLevitation;
import net.kenji.kenjiscombatforms.api.powers.VoidPowers.TeleportPlayer;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherDash;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherFormAbility;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherMinions;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.event.sound.SoundManager;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.kenji.kenjiscombatforms.keybinds.ModKeybinds;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.network.power_form.ability1.StrengthBoostPacket;
import net.kenji.kenjiscombatforms.network.power_form.ability2.PowerEffectInflictPacket;
import net.kenji.kenjiscombatforms.network.swift_form.ability1.SpeedBoostPacket;
import net.kenji.kenjiscombatforms.network.swift_form.ability2.SwiftEffectInflictPacket;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.kenji.kenjiscombatforms.network.voidform.ability2.TeleportPlayerBehindPacket;
import net.kenji.kenjiscombatforms.network.voidform.ability1.TeleportPlayerPacket;
import net.kenji.kenjiscombatforms.network.voidform.ability2.VoidRiftPacket;
import net.kenji.kenjiscombatforms.network.voidform.ability3.ToggleEnderPacket;
import net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability4.EnderLevitationPacket;
import net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability5.VoidGrabPacket;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.kenji.kenjiscombatforms.network.witherform.ability1.WitherPausePacket;
import net.kenji.kenjiscombatforms.network.witherform.ability2.SoulDriftPacket;
import net.kenji.kenjiscombatforms.network.witherform.ability1.WitherDashPacket;
import net.kenji.kenjiscombatforms.network.witherform.ability3.ToggleWitherFormPacket;
import net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability4.SummonWitherMinionsPacket;
import net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability5.WitherImplodePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
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
            public boolean isShiftDown = false;
            public  long lastPressTime = 0;
            public long currentDashState = 0;
            public long pressCounter = INITIAL_PRESS_COUNTER;
            public long tpPressCounter = INITIAL_PRESS_COUNTER;
        }




        private controlRelatedEvents.PlayerData getOrCreatePlayerData(Player player) {
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
                return KenjisCombatFormsCommon.CAN_USE_ABILITIES_NO_FORM.get();
            }
            return true;
        }


        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            Minecraft mc = Minecraft.getInstance();
            Player clientPlayer = Minecraft.getInstance().player;




            if (clientPlayer != null) {
                WitherPlayerDataSets.WitherDashPlayerData wData = getInstance().dataSets.getOrCreateDashPlayerData(clientPlayer);
                EnderPlayerDataSets.TeleportPlayerData tpData = EnderPlayerDataSets.getInstance().getOrCreateTeleportPlayerData(clientPlayer);


                if (getCanUseAbilitiesWithoutForms(clientPlayer)) {

                    PlayerData data = getInstance().getOrCreatePlayerData(clientPlayer);
                    WitherPlayerDataSets.WitherFormPlayerData wPlayerData = getInstance().getOrCreateWitherFormPlayerData(clientPlayer);
                    WitherPlayerDataSets.WitherMinionPlayerData wmPlayerData = getInstance().getOrCreateMinionPlayerData(clientPlayer);
                    if (ModKeybinds.ABILITY1_KEY.consumeClick()) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - data.lastPressTime > PRESS_COOLDOWN) {
                            if (AbilityManager.getInstance().getPlayerAbilityData(clientPlayer).chosenAbility1 == AbilityManager.AbilityOption1.VOID_ABILITY1 ||
                                    ClientFistData.getChosenAbility1() == AbilityManager.AbilityOption1.VOID_ABILITY1 || AbilityChangeHandler.getInstance().getVoid1Selected(clientPlayer)) {
                                if (!ClientVoidData.getIsEnderActive() && ClientVoidData.getCooldown() <= EnderPlayerDataSets.getInstance().getOrCreateTeleportPlayerData(clientPlayer).getMAX_COOLDOWN() / KenjisCombatFormsCommon.ABILITY1_COOLDOWN_DIVISION.get()) {
                                    data.tpPressCounter--;
                                        NetworkHandler.INSTANCE.sendToServer(new TeleportPlayerBehindPacket());
                                }
                            }
                            if (ClientVoidData.getIsEnderActive()) {
                                NetworkHandler.INSTANCE.sendToServer(new EnderLevitationPacket());
                            } else if (AbilityManager.getInstance().getPlayerAbilityData(clientPlayer).chosenAbility1 == AbilityManager.AbilityOption1.WITHER_ABILITY1 ||
                                    ClientFistData.getChosenAbility1() == AbilityManager.AbilityOption1.WITHER_ABILITY1) {
                                if (!net.kenji.kenjiscombatforms.network.witherform.ClientWitherData.getIsWitherActive() && !net.kenji.kenjiscombatforms.network.witherform.ClientWitherData.getMinionsActive()) {

                                  if(ClientWitherData.getCooldown() <= wData.getMAX_COOLDOWN() / KenjisCombatFormsCommon.ABILITY1_COOLDOWN_DIVISION.get())
                                    if (data.currentDashState == 0) {
                                        NetworkHandler.INSTANCE.sendToServer(new WitherPausePacket(clientPlayer.getUUID()));
                                        // Apply temporary client-side effect
                                        WitherDash.getInstance().activatePause(clientPlayer);
                                        data.currentDashState = 1;
                                        data.lastPressTime = currentTime;
                                    } else if (data.currentDashState >= 1) {
                                        NetworkHandler.INSTANCE.sendToServer(new WitherDashPacket(clientPlayer.getUUID(), clientPlayer.getLookAngle(), wData.MAX_SPEED));
                                        // Apply temporary client-side effect
                                        WitherDash.getInstance().activateDash(clientPlayer, clientPlayer.getLookAngle(), wData.MAX_SPEED);
                                        data.currentDashState = 0;
                                        SoundManager.playDashSound(clientPlayer);
                                        WitherDash.getInstance().setAbilityCooldown(clientPlayer);
                                        data.lastPressTime = currentTime;
                                    }

                                }
                            }

                            if (ClientWitherData.getIsWitherActive() || ClientWitherData.getMinionsActive()) {
                                NetworkHandler.INSTANCE.sendToServer(new SummonWitherMinionsPacket());
                                WitherMinions.getInstance().clientChangeActive(clientPlayer);
                            }
                            if (ClientFistData.getChosenAbility1() == AbilityManager.AbilityOption1.SWIFT_ABILITY1) {
                                NetworkHandler.INSTANCE.sendToServer(new SpeedBoostPacket());
                            }
                            if (ClientFistData.getChosenAbility1() == AbilityManager.AbilityOption1.POWER_ABILITY1) {
                                NetworkHandler.INSTANCE.sendToServer(new StrengthBoostPacket());
                            }
                        }
                    }
                    if (ModKeybinds.ABILITY2_KEY.consumeClick()) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - data.lastPressTime > PRESS_COOLDOWN) {
                            if (AbilityManager.getInstance().getPlayerAbilityData(clientPlayer).chosenAbility2 == AbilityManager.AbilityOption2.VOID_ABILITY2 ||
                                    ClientFistData.getChosenAbility2() == AbilityManager.AbilityOption2.VOID_ABILITY2) {
                                if (!ClientVoidData.getIsEnderActive()) {
                                    NetworkHandler.INSTANCE.sendToServer(new VoidRiftPacket());
                                    data.lastPressTime = currentTime;
                                }
                            }
                            if (ClientVoidData.getIsEnderActive()) {
                                NetworkHandler.INSTANCE.sendToServer(new VoidGrabPacket());
                            } else if (AbilityManager.getInstance().getPlayerAbilityData(clientPlayer).chosenAbility2 == AbilityManager.AbilityOption2.WITHER_ABILITY2 ||
                                    ClientFistData.getChosenAbility2() == AbilityManager.AbilityOption2.WITHER_ABILITY2) {
                                if (!net.kenji.kenjiscombatforms.network.witherform.ClientWitherData.getIsWitherActive()) {
                                    NetworkHandler.INSTANCE.sendToServer(new SoulDriftPacket());
                                } else NetworkHandler.INSTANCE.sendToServer(new WitherImplodePacket());
                            }
                            if (ClientWitherData.getIsWitherActive()) {
                                NetworkHandler.INSTANCE.sendToServer(new WitherImplodePacket());
                            }
                            if (ClientFistData.getChosenAbility2() == AbilityManager.AbilityOption2.SWIFT_ABILITY2) {
                                NetworkHandler.INSTANCE.sendToServer(new SwiftEffectInflictPacket());
                            }
                            if (ClientFistData.getChosenAbility2() == AbilityManager.AbilityOption2.POWER_ABILITY2) {
                                NetworkHandler.INSTANCE.sendToServer(new PowerEffectInflictPacket());
                            }
                            data.lastPressTime = currentTime;
                        }
                    }
                    if (ModKeybinds.ABILITY3_KEY.consumeClick()) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - data.lastPressTime > PRESS_COOLDOWN) {
                            if (AbilityManager.getInstance().getPlayerAbilityData(clientPlayer).chosenFinal == AbilityManager.AbilityOption3.VOID_FINAL ||
                                    ClientFistData.getChosenAbility3() == AbilityManager.AbilityOption3.VOID_FINAL) {
                                NetworkHandler.INSTANCE.sendToServer(new ToggleEnderPacket());
                                EnderFormAbility.getInstance().spawnParticles(clientPlayer);

                                EnderFormAbility.getInstance().jumpUp(clientPlayer);
                                WitherFormAbility.getInstance().jumpUp(clientPlayer);

                                data.lastPressTime = currentTime;
                            } else if (AbilityManager.getInstance().getPlayerAbilityData(clientPlayer).chosenFinal == AbilityManager.AbilityOption3.WITHER_FINAL ||
                                    ClientFistData.getChosenAbility3() == AbilityManager.AbilityOption3.WITHER_FINAL) {
                                NetworkHandler.INSTANCE.sendToServer(new ToggleWitherFormPacket());

                                Vec3 velocity = clientPlayer.getDeltaMovement();

                                clientPlayer.setDeltaMovement(velocity.x, 0.5, velocity.z);

                                WitherFormAbility.getInstance().spawnParticles(clientPlayer);
                                data.lastPressTime = currentTime;
                            }
                        }
                    }

                    if (!ModKeybinds.ABILITY1_KEY.isDown()) {
                        long currentTime = System.currentTimeMillis();


                        if (data.tpPressCounter <= 0) {
                            if (AbilityManager.getInstance().getPlayerAbilityData(clientPlayer).chosenAbility1 == AbilityManager.AbilityOption1.VOID_ABILITY1 ||
                                    ClientFistData.getChosenAbility1() == AbilityManager.AbilityOption1.VOID_ABILITY1) {
                                // Send packet to server
                                NetworkHandler.INSTANCE.sendToServer(new TeleportPlayerPacket());

                                data.lastPressTime = currentTime;
                                TeleportPlayer.getInstance().blink(clientPlayer);
                            }
                        }
                        data.tpPressCounter = INITIAL_PRESS_COUNTER;
                        data.pressCounter = INITIAL_PRESS_COUNTER;
                    }
                }
            }
        }
    }
}
