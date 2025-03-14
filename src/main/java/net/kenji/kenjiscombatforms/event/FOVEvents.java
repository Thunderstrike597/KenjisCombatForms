package net.kenji.kenjiscombatforms.event;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.PowerControl;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.powers.VoidPowers.TeleportPlayer;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherDash;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.kenji.kenjiscombatforms.item.custom.scrolls.BaseUseItem;
import net.kenji.kenjiscombatforms.keybinds.ModKeybinds;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT)
public class FOVEvents {

    private static final FOVEvents INSTANCE = new FOVEvents();
    public static FOVEvents getInstance(){
        return INSTANCE;
    }
    public Map<UUID, PowerControl.controlRelatedEvents.PlayerData> playerDataMap = new ConcurrentHashMap<>();
    WitherPlayerDataSets witherDataSets = WitherPlayerDataSets.getInstance();
    EnderPlayerDataSets enderDataSets = EnderPlayerDataSets.getInstance();

    private static final float MAX_ZOOM_OUT = 112.0F;
    private static final float MAX_ZOOM_IN = 40.0F;
    private static final int INITIAL_PRESS_COUNTER = 90;
    private static final int ENDER_FOV = 80;

    private PowerControl.controlRelatedEvents.PlayerData getOrCreatePlayerData(Player player) {
        return PowerControl.controlRelatedEvents.getInstance().playerDataMap.computeIfAbsent(player.getUUID(), k -> new PowerControl.controlRelatedEvents.PlayerData());
    }
    private WitherPlayerDataSets.WitherMinionPlayerData getOrCreateMinionPlayerData(Player player) {
        return witherDataSets.getOrCreateMinionPlayerData(player);
    }
    private WitherPlayerDataSets.WitherFormPlayerData getOrCreateWitherFormPlayerData(Player player) {
        return witherDataSets.getOrCreateWitherFormPlayerData(player);
    }
    private EnderPlayerDataSets.EnderFormPlayerData getOrCreateEnderFormPlayerData(Player player) {
        return enderDataSets.getOrCreateEnderFormPlayerData(player);
    }




    @SubscribeEvent
    public static void onComputeFov(ViewportEvent.ComputeFov event) {
        final double DEFAULT_FOV = event.getFOV();

        Player clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer != null) {
            PowerControl.controlRelatedEvents.PlayerData data = getInstance().getOrCreatePlayerData(clientPlayer);
            WitherPlayerDataSets.WitherDashPlayerData wData = getInstance().witherDataSets.getOrCreateDashPlayerData(clientPlayer);
            WitherPlayerDataSets.WitherMinionPlayerData wmPlayerData = getInstance().getOrCreateMinionPlayerData(clientPlayer);
            EnderPlayerDataSets.EnderFormPlayerData ePlayerData = getInstance().getOrCreateEnderFormPlayerData(clientPlayer);
            int currentWitherCooldown = ClientWitherData.getCooldown();
            ItemStack itemInUse = clientPlayer.getUseItem();


            if (ModKeybinds.ABILITY1_KEY.isDown()) {
                if (AbilityManager.getInstance().getPlayerAbilityData(clientPlayer).chosenAbility1 == AbilityManager.AbilityOption1.VOID_ABILITY1 ||
                        ClientFistData.getChosenAbility1() == AbilityManager.AbilityOption1.VOID_ABILITY1) {

                    if (ClientVoidData.getCooldown() <= EnderPlayerDataSets.getInstance().getOrCreateTeleportPlayerData(clientPlayer).getMAX_COOLDOWN() / 2) {
                        float zoomProgress = 1.0F - ((float) data.tpPressCounter / INITIAL_PRESS_COUNTER);

                        // Calculate target FOV, but only zoom out to halfway point
                        double halfZoomOut = DEFAULT_FOV + (MAX_ZOOM_IN - DEFAULT_FOV);
                        double targetFOV = DEFAULT_FOV + (halfZoomOut - DEFAULT_FOV) * Math.min(zoomProgress * 1, 1.0F);

                        // Set the new FOV

                        event.setFOV(targetFOV);

                    } else if (!clientPlayer.isUsingItem()) {
                        // Reset to default FOV when not zooming
                        event.setFOV(DEFAULT_FOV);
                    }
                }

            }
            if (ePlayerData.isEnderActive) {
                event.setFOV(ENDER_FOV);
            }
        }
    }


    private static final float MIN_FOV_MODIFIER = 0.7F; // Adjust this for maximum zoom
    private static final int CHARGE_DURATION = 40; // Should match the value in VoidScroll

    @SubscribeEvent
    public static void onComputeFovModifier(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInUse = player.getUseItem();

        if (itemInUse.getItem() instanceof BaseUseItem) {
            if (player.isUsingItem()) {
                int useTime = itemInUse.getUseDuration() - player.getUseItemRemainingTicks();
                float chargeProgress = Math.min(1.0F, (float)useTime / CHARGE_DURATION);
                float newFovModifier = 1.0F - (chargeProgress * (1.0F - MIN_FOV_MODIFIER));
                event.setNewFovModifier(newFovModifier);
            }
        }
    }
}
