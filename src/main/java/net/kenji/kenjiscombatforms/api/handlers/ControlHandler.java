package net.kenji.kenjiscombatforms.api.handlers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.api.managers.forms.BasicForm;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.kenji.kenjiscombatforms.keybinds.ModKeybinds;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.form_choose.BasicFormChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_choose.Form1ChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_choose.Form2ChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_choose.Form3ChoosePacket;
import net.kenji.kenjiscombatforms.screen.form_menu.FormChooseMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class ControlHandler {
    private static final long PRESS_COOLDOWN = 4;
    @Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT)
    public static class controlRelatedEvents {

        public Map<UUID, PlayerData> playerDataMap = new ConcurrentHashMap<>();
        private static final int INITIAL_PRESS_COUNTER = 90;// Adjust as needed
        WitherPlayerDataSets dataSets = WitherPlayerDataSets.getInstance();
        private static final controlRelatedEvents INSTANCE = new controlRelatedEvents();

        public static controlRelatedEvents getInstance() {
            return INSTANCE;
        }

        public static class PlayerData {
            public boolean isHandCombat = true;
            public boolean isShiftDown = false;
            public long lastPressTime = 0;
            int currentState = 0;
            int currentToggleState = 0;
        }


        public PlayerData getOrCreatePlayerData(Player player) {
            return getInstance().playerDataMap.computeIfAbsent(player.getUUID(), k -> new PlayerData());
        }

        private WitherPlayerDataSets.WitherFormPlayerData getOrCreateWitherFormPlayerData(Player player) {
            return dataSets.getOrCreateWitherFormPlayerData(player);
        }

        private WitherPlayerDataSets.WitherMinionPlayerData getOrCreateMinionPlayerData(Player player) {
            return dataSets.getOrCreateMinionPlayerData(player);
        }


        public boolean getShiftDown(Player player) {
            PlayerData data = getInstance().getOrCreatePlayerData(player);
            return data.isShiftDown;
        }

        public void setShiftDown(Player player, boolean value) {
            PlayerData data = getInstance().getOrCreatePlayerData(player);
            data.isShiftDown = value;
        }


        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            Minecraft mc = Minecraft.getInstance();
            Player clientPlayer = Minecraft.getInstance().player;
            ClientEventHandler clientEventHandler = ClientEventHandler.getInstance();

            if (clientPlayer != null) {
                PlayerData data = getInstance().getOrCreatePlayerData(clientPlayer);

                if (event.getKey() == mc.options.keyShift.getKey().getValue()) {
                    if (clientEventHandler.getIsWitherActive()) {
                        if (event.getAction() == GLFW.GLFW_PRESS || event.getAction() == GLFW.GLFW_REPEAT) {
                            mc.options.keyShift.setDown(false);
                            getInstance().setShiftDown(clientPlayer, true);
                        }
                        else if (event.getAction() == GLFW.GLFW_RELEASE) {
                            getInstance().setShiftDown(clientPlayer, false);
                        }
                    }
                }else data.isShiftDown = false;
                if (clientPlayer.getMainHandItem().getItem() instanceof BaseFistClass) {
                    if (event.getKey() == mc.options.keyDrop.getKey().getValue()) {
                        clientPlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    }
                }





                if (ModKeybinds.FORM_MENU_OPEN_KEY.consumeClick()) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - data.lastPressTime > PRESS_COOLDOWN) {
                        if (mc.screen == null) {
                            mc.setScreen(new FormChooseMenu(Component.translatable("screen.kenjiscombatforms.choose_form_screen")));
                        } else mc.setScreen(null);
                        data.lastPressTime = currentTime;
                    }
                }


                if (ModKeybinds.QUICK_FORM_CHANGE_KEY.consumeClick()) {
                    String currentForm1 = ClientFistData.getForm1Option();
                    String currentForm2 = ClientFistData.getForm2Option();
                    String currentForm3 = ClientFistData.getForm3Option();

                    boolean isFinalActive = ClientEventHandler.getInstance().getAreFinalsActive();


                    long currentTime = System.currentTimeMillis();
                    if (!isFinalActive) {
                        if (currentTime - data.lastPressTime > PRESS_COOLDOWN) {
                            data.lastPressTime = currentTime;
                            // Increment the state
                            data.currentState = (data.currentState + 1) % 4;  // Cycle through 0, 1, 2, 3

                            if (clientPlayer.getMainHandItem().getItem() instanceof BaseFistClass) {

                                clientPlayer.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);

                                switch (data.currentState) {
                                    case 0:
                                        ClientFistData.setSelectedForm(BasicForm.getInstance().getName());
                                        ClientFistData.setSpecificFormData(clientPlayer);
                                        NetworkHandler.INSTANCE.sendToServer(new BasicFormChoosePacket());
                                        break;
                                    case 1:
                                        if (!Objects.equals(ClientFistData.getForm1Option(), "NONE")) {
                                            ClientFistData.setSelectedForm(ClientFistData.getForm1Option());
                                            ClientFistData.setSpecificFormData(clientPlayer);
                                            NetworkHandler.INSTANCE.sendToServer(new Form1ChoosePacket(currentForm1));
                                        } else {
                                            data.currentState = 0;
                                            ClientFistData.setSelectedForm(BasicForm.getInstance().getName());
                                            ClientFistData.setSpecificFormData(clientPlayer);
                                            NetworkHandler.INSTANCE.sendToServer(new BasicFormChoosePacket());
                                        }
                                        break;
                                    case 2:
                                        if (!Objects.equals(ClientFistData.getForm2Option(), "NONE")) {
                                            ClientFistData.setSelectedForm(ClientFistData.getForm2Option());
                                            ClientFistData.setSpecificFormData(clientPlayer);
                                            NetworkHandler.INSTANCE.sendToServer(new Form2ChoosePacket(currentForm2));
                                        } else {
                                            data.currentState = 0;
                                            ClientFistData.setSelectedForm(BasicForm.getInstance().getName());
                                            ClientFistData.setSpecificFormData(clientPlayer);
                                            NetworkHandler.INSTANCE.sendToServer(new BasicFormChoosePacket());
                                        }
                                        break;
                                    case 3:
                                        if (!Objects.equals(ClientFistData.getForm3Option(), "NONE")) {
                                            ClientFistData.setSelectedForm(ClientFistData.getForm3Option());
                                            ClientFistData.setSpecificFormData(clientPlayer);
                                            NetworkHandler.INSTANCE.sendToServer(new Form3ChoosePacket(currentForm3));
                                        } else {
                                            data.currentState = 0;
                                            ClientFistData.setSelectedForm(BasicForm.getInstance().getName());
                                            ClientFistData.setSpecificFormData(clientPlayer);
                                            NetworkHandler.INSTANCE.sendToServer(new BasicFormChoosePacket());
                                        }
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
