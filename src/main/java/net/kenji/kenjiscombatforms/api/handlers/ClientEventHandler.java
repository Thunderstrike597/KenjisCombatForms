package net.kenji.kenjiscombatforms.api.handlers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.PowerControl;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.EnderEntity;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.WitherPlayerEntity;
import net.kenji.kenjiscombatforms.event.EntityCameraMovements;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.UpdateInventoryOpenPacket;
import net.kenji.kenjiscombatforms.network.movers.PlayerInputPacket;
import net.kenji.kenjiscombatforms.network.movers.attacking.EnderEntityAttackPacket;
import net.kenji.kenjiscombatforms.network.movers.attacking.WitherEntityAttackPacket;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.kenji.kenjiscombatforms.network.voidform.ender_abilities.TeleportEnderEntityPacket;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.kenji.kenjiscombatforms.network.witherform.WitherFormDashPacket;
import net.kenji.kenjiscombatforms.network.witherform.ability1.WitherDashPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {

    private static final ClientEventHandler INSTANCE = new ClientEventHandler();
    public Player currentPlayer;

    public static ClientEventHandler getInstance(){
        return INSTANCE;
    }

    private PowerControl.controlRelatedEvents.PlayerData getOrCreatePlayerData(Player player) {
        return PowerControl.controlRelatedEvents.getInstance().playerDataMap.computeIfAbsent(player.getUUID(), k -> new PowerControl.controlRelatedEvents.PlayerData());
    }


    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            Player clientPlayer = mc.player;
            ControllEngine controllEngine = ClientEngine.getInstance().controllEngine;
            if(clientPlayer != null) {
                if (controllEngine != null) {
                    onClickInput();
                }
                if (mc.cameraEntity instanceof EnderEntity || mc.cameraEntity instanceof WitherPlayerEntity) {
                    boolean forward = mc.options.keyUp.isDown();
                    boolean backward = mc.options.keyDown.isDown();
                    boolean left = mc.options.keyLeft.isDown();
                    boolean right = mc.options.keyRight.isDown();
                    boolean jump = mc.options.keyJump.isDown();
                    boolean sneak = ControlHandler.controlRelatedEvents.getInstance().getShiftDown(clientPlayer);

                    mc.cameraEntity.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
                                if (cap instanceof LivingEntityPatch<?> livingEntityPatch) {

                                    if (!livingEntityPatch.getEntityState().attacking()) {
                                        NetworkHandler.INSTANCE.sendToServer(new PlayerInputPacket(forward, backward, left, right, jump, sneak, mc.cameraEntity.getUUID()));
                                        EntityCameraMovements.handleEntityRotation(mc.cameraEntity);
                                    }
                                }
                    });
                }
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

    Player returnPlayer(){
        return getInstance().currentPlayer;
    }


    private static boolean isHoldingFistFormItem(Player player) {
        // Your logic to check if the player is holding the special item
        return player.getMainHandItem().getItem() instanceof BaseFistClass;
    }





    public static void onClickInput(){
        Player player = Minecraft.getInstance().player;
        boolean attackKey = ControllEngine.isKeyDown(EpicFightKeyMappings.ATTACK);
        boolean dodgeKey = ControllEngine.isKeyDown(EpicFightKeyMappings.DODGE);
        if(player != null) {
            EnderPlayerDataSets.EnderFormPlayerData data = EnderPlayerDataSets.getInstance().getOrCreateEnderFormPlayerData(player);
            WitherPlayerDataSets.WitherFormPlayerData wData = WitherPlayerDataSets.getInstance().getOrCreateWitherFormPlayerData(player);
            WitherPlayerDataSets.WitherFormDashPlayerData dData = WitherPlayerDataSets.getInstance().getOrCreateWitherFormDashPlayerData(player);

            player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
                if (cap instanceof PlayerPatch playerPatch) {

                    if (data.isEnderActive || ClientVoidData.getIsEnderActive()) {
                        if (attackKey) {

                            playerPatch.getEntityState().setState(EntityState.INACTION, true);
                            NetworkHandler.INSTANCE.sendToServer(new EnderEntityAttackPacket());
                        } else if (dodgeKey) {
                            NetworkHandler.INSTANCE.sendToServer(new TeleportEnderEntityPacket());
                        }
                    } else if (wData.isWitherActive || ClientWitherData.getIsWitherActive()) {
                        if (attackKey) {
                            playerPatch.getEntityState().setState(EntityState.INACTION, true);

                            NetworkHandler.INSTANCE.sendToServer(new WitherEntityAttackPacket());
                        } else if (dodgeKey) {
                            if (ClientWitherData.getWitherEntity() != null) {

                                NetworkHandler.INSTANCE.sendToServer(new WitherFormDashPacket(player.getUUID(), ClientWitherData.getWitherEntity().getLookAngle(), dData.MAX_SPEED));
                            }
                        }
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
}
