package net.kenji.kenjiscombatforms.api.handlers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.PowerControl;
import net.kenji.kenjiscombatforms.api.capabilities.ExtraContainerCapability;

import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherFormAbility;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.kenji.kenjiscombatforms.keybinds.ModKeybinds;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.UpdateInventoryOpenPacket;
import net.kenji.kenjiscombatforms.network.capability.SyncNBTPacket;
import net.kenji.kenjiscombatforms.network.capability.SyncRemovedNBTPacket;
import net.kenji.kenjiscombatforms.network.movers.WitherInputPacket;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.ControllEngine;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {

    private static long lastPressTime = 0;
    private static final long PRESS_COOLDOWN = 100;
    private boolean canCrash = false;


    private static final ClientEventHandler INSTANCE = new ClientEventHandler();
    public Player currentPlayer;

    public static ClientEventHandler getInstance() {
        return INSTANCE;
    }

    private PowerControl.controlRelatedEvents.PlayerData getOrCreatePlayerData(Player player) {
        return PowerControl.controlRelatedEvents.getInstance().playerDataMap.computeIfAbsent(player.getUUID(), k -> new PowerControl.controlRelatedEvents.PlayerData());
    }

    public boolean getAreFinalsActive(){
       Player player = Minecraft.getInstance().player;
       if(player != null) {
           Ability ability3 = AbilityManager.getInstance().getCurrentAbilities(player).get(2);
           if (ability3 != null) {
               AbstractAbilityData ability3Data = ability3.getAbilityData(player);
               return ability3Data.isAbilityActive();
           }
       }
           return false;
    }

    public boolean getIsWitherActive() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {

        return WitherFormAbility.getInstance().getAbilityActive(player);
        }
        return false;
    }

    public void setCanCrash(boolean canCrash){
        this.canCrash = canCrash;
    }
    public boolean getCanCrash(){
        return this.canCrash;
    }

    public void crash(){
        throw new RuntimeException("Intentional debug crash!");
    }



    public int getOriginalSlot(Player player) {
        return CommonEventHandler.getInstance().getOriginalSlot(player);
    }
    // Handle key press to store the item
    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        Player player = Minecraft.getInstance().player;
        FormChangeHandler formChangeHandler = FormChangeHandler.getInstance();
        CommonEventHandler commonEventHandler = CommonEventHandler.getInstance();

        if (player == null) return;

        if (ModKeybinds.TOGGLE_HAND_COMBAT_KEY.consumeClick()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPressTime > PRESS_COOLDOWN) {
                lastPressTime = currentTime;
                int originalSlot = getInstance().getOriginalSlot(player);

                int selectedSlot = player.getInventory().selected;
                ItemStack currentItem = player.getInventory().getItem(selectedSlot);
                player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
                    ItemStack storedItem = commonEventHandler.getStoredItem(player);
                    if(!getInstance().getAreFinalsActive()) {
                        if (storedItem.isEmpty()) {
                            if (!(currentItem.getItem() instanceof BaseFistClass)) {
                                commonEventHandler.setStoredItemNBT(player, currentItem);
                                container.setStoredItem(currentItem);
                            }
                            commonEventHandler.setOriginalSlot(player, selectedSlot);
                            container.setOriginalSlot(selectedSlot);

                            NetworkHandler.INSTANCE.sendToServer(new SyncNBTPacket(currentItem, selectedSlot));
                                    player.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);

                        } else if (!storedItem.isEmpty()) {
                            if(originalSlot != -1) {
                                if (currentItem.getItem() instanceof BaseFistClass) {
                                    player.getInventory().setItem(originalSlot, ItemStack.EMPTY);
                                }
                                player.getInventory().setItem(originalSlot, storedItem);

                                NetworkHandler.INSTANCE.sendToServer(new SyncRemovedNBTPacket(storedItem, originalSlot));
                                container.setStoredItem(ItemStack.EMPTY);
                                commonEventHandler.setStoredItemNBT(player, ItemStack.EMPTY);
                                commonEventHandler.setOriginalSlot(player, -1);
                                container.setOriginalSlot(-1);

                                NetworkHandler.INSTANCE.sendToServer(new SyncRemovedNBTPacket(storedItem, originalSlot));

                                player.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);
                            }
                        }
                        if(storedItem.isEmpty()){
                            if (currentItem.getItem() instanceof BaseFistClass) {
                                player.getInventory().setItem(selectedSlot, ItemStack.EMPTY);
                                NetworkHandler.INSTANCE.sendToServer(new SyncRemovedNBTPacket(storedItem, selectedSlot));
                                container.setOriginalSlot(-1);
                            }
                        }
                    }
                });
            }
        }
    }

    public int getSelectedExtraSlot(Player player) {
        return player.getInventory().selected;
    }
    static boolean hasSelected = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            Player clientPlayer = mc.player;
            ControllEngine controllEngine = ClientEngine.getInstance().controllEngine;
            if (clientPlayer != null) {
                CommonEventHandler commonEventHandler = CommonEventHandler.getInstance();
                int originalSlot = getInstance().getOriginalSlot(clientPlayer);

                if (WitherFormAbility.getInstance().getAbilityActive(clientPlayer)) {
                    boolean jump = mc.options.keyJump.isDown();
                    boolean sneak = ControlHandler.controlRelatedEvents.getInstance().getShiftDown(clientPlayer);


                    NetworkHandler.INSTANCE.sendToServer(new WitherInputPacket(jump, sneak));
                    Vec3 velocity = clientPlayer.getDeltaMovement(); // Get current velocity
                    if (!clientPlayer.level().getBlockState(clientPlayer.blockPosition().below(2)).isAir() ||
                            !clientPlayer.level().getBlockState(clientPlayer.blockPosition()).isAir()) {
                        if (jump) {
                            clientPlayer.setDeltaMovement(velocity.x, 0.2, velocity.z); // Move up
                        }
                    }
                    if (sneak) {
                        clientPlayer.setDeltaMovement(velocity.x, -0.2, velocity.z); // Move down
                    }

                }
                    if(getInstance().getAreFinalsActive()) {
                        if(!hasSelected) {
                            CommonEventHandler.getInstance().setOriginalSlot(clientPlayer, clientPlayer.getInventory().selected);
                            hasSelected = true;
                        }
                        clientPlayer.getInventory().selected = CommonEventHandler.getInstance().getOriginalSlot(clientPlayer);
                    }if (!getInstance().getAreFinalsActive()) {
                        if (hasSelected) {
                            hasSelected = false;
                        }
                    }

                if (originalSlot != -1 && clientPlayer.getInventory().selected != originalSlot) {
                    clientPlayer.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
                        ItemStack storedItem = commonEventHandler.getStoredItem(clientPlayer);
                        if (!storedItem.isEmpty()) {
                            clientPlayer.getInventory().setItem(originalSlot, storedItem);
                            NetworkHandler.INSTANCE.sendToServer(new SyncRemovedNBTPacket(storedItem, originalSlot));
                            commonEventHandler.setStoredItemNBT(clientPlayer, ItemStack.EMPTY);
                            commonEventHandler.setOriginalSlot(clientPlayer, -1);
                            container.setOriginalSlot(-1);

                            NetworkHandler.INSTANCE.sendToServer(new SyncRemovedNBTPacket(storedItem, originalSlot));

                            clientPlayer.getInventory().setChanged();
                        }
                            if(clientPlayer.getInventory().getItem(originalSlot).getItem() instanceof BaseFistClass){
                                clientPlayer.getInventory().setItem(originalSlot, ItemStack.EMPTY);
                                NetworkHandler.INSTANCE.sendToServer(new SyncRemovedNBTPacket(storedItem, originalSlot));

                            }
                    });
                }
            }
            if(getInstance().getCanCrash()){
                getInstance().crash();
            }
        }
    }


    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        getInstance().currentPlayer = player;
        Minecraft mc = Minecraft.getInstance();
        // KeybindManager.getInstance().storeOriginalKey(player, mc.options.keyDrop.getKey());
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        Minecraft mc = Minecraft.getInstance();
        //  InputConstants.Key originalKey = KeybindManager.getInstance().getOriginalKey(player);
        // mc.options.keyDrop.setKey(originalKey);
    }

    Player returnPlayer() {
        return getInstance().currentPlayer;
    }


    private static boolean isHoldingFistFormItem(Player player) {
        // Your logic to check if the player is holding the special item
        return player.getMainHandItem().getItem() instanceof BaseFistClass;
    }

    @SubscribeEvent
    public static void onInventoryOpen(ScreenEvent.Init event) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
                ItemStack storedItem = container.getStoredItem();

                if (player.getMainHandItem().getItem() instanceof BaseFistClass) {
                    if (event.getScreen() instanceof InventoryScreen) {
                        if(!storedItem.isEmpty()) {
                            player.getInventory().setItem(getInstance().getOriginalSlot(player), storedItem);
                            NetworkHandler.INSTANCE.sendToServer(new SyncNBTPacket(storedItem, getInstance().getOriginalSlot(player)));

                        }
                        CommonEventHandler.getInstance().setStoredItemNBT(player, ItemStack.EMPTY);
                        CommonEventHandler.getInstance().setOriginalSlot(player, -1);

                        NetworkHandler.INSTANCE.sendToServer(new SyncNBTPacket(storedItem, getInstance().getOriginalSlot(player)));

                    }
                }
            });
        }
    }




    private static boolean lastScreenWasContainer = false;

    @SubscribeEvent
    public static void onGuiOpen(ScreenEvent.Init event) {
        if (event.getScreen() instanceof AbstractContainerScreen<?>) {
                // Send packet to server to update
                lastScreenWasContainer = true;
                NetworkHandler.INSTANCE.sendToServer(new UpdateInventoryOpenPacket(true));
        }
    }

    @SubscribeEvent
    public static void onGuiClose(ScreenEvent.Closing event) {
        // Send packet to server to update
        if (lastScreenWasContainer) {
            NetworkHandler.INSTANCE.sendToServer(new UpdateInventoryOpenPacket(false));
            lastScreenWasContainer = false;
        }
    }

    @SubscribeEvent
    public static void onHotbarScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            if(getInstance().getAreFinalsActive()) {
                event.setCanceled(true); // Stops the scroll wheel from changing the slot
            }
        }
    }
}
