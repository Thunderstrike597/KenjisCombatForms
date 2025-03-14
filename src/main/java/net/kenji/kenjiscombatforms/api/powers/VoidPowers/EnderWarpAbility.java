package net.kenji.kenjiscombatforms.api.powers.VoidPowers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.event.CommonFunctions;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.Map;
import java.util.UUID;

public class EnderWarpAbility implements Ability {

    private final EnderPlayerDataSets dataSets = EnderPlayerDataSets.getInstance();
    private final Map<UUID, EnderPlayerDataSets.TeleportPlayerData> playerDataMap = dataSets.A1playerDataMap;
    private static final EnderWarpAbility INSTANCE = new EnderWarpAbility();
    private final CommonFunctions dataHandlers = CommonFunctions.getInstance();



    @Override
    public String getName() {
        return AbilityManager.AbilityOption3.VOID_FINAL.name();
    }

    public EnderPlayerDataSets.EnderWarpPlayerData getPlayerData(Player player) {
        return (EnderPlayerDataSets.EnderWarpPlayerData) getAbilityData(player);
    }
    public EnderPlayerDataSets.EnderFormPlayerData getOrCreateEnderFormPlayerData(Player player) {
        return dataSets.getOrCreateEnderFormPlayerData(player);
    }



    @Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ServerForgeEvents {
        @SubscribeEvent
        public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                getInstance().getPlayerData(serverPlayer);
                getInstance().syncDataToClient(serverPlayer);
            }
        }

        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                getInstance().setAbilityCooldown(serverPlayer);
                getInstance().syncDataToClient(serverPlayer);
            }
        }

        @SubscribeEvent
        public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
            getInstance().playerDataMap.remove(event.getEntity().getUUID());
        }

        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            if (event.getOriginal() instanceof ServerPlayer originalPlayer &&
                    event.getEntity() instanceof ServerPlayer newPlayer) {

                EnderPlayerDataSets.EnderWarpPlayerData originalData = getInstance().getPlayerData(originalPlayer);
                EnderPlayerDataSets.EnderWarpPlayerData newData = getInstance().getPlayerData(newPlayer);

                // Transfer the cooldown data
                newData.abilityCooldown = originalData.abilityCooldown;

                // If you have other data to transfer, do it here
                // newData.otherAttribute = originalData.otherAttribute;

                // Sync the data to the client
                getInstance().syncDataToClient(newPlayer);
            }
        }
    }




    public EnderWarpAbility() {
        // Private constructor to prevent instantiation
    }

    public static EnderWarpAbility getInstance() {
        return INSTANCE;
    }


    public float getAbilityCooldown(Player player) {
        return getPlayerData(player).abilityCooldown;
    }

    public void setAbilityCooldown(Player player) {
        getPlayerData(player).abilityCooldown = getPlayerData(player).abilityCooldown + getPlayerData(player).getMAX_COOLDOWN() / 2;
    }

    ServerPlayer serverPlayer;

    @Override
    public AbstractAbilityData getAbilityData(Player player) {
        return dataSets.getOrCreateEnderWarpPlayerData(player);
    }

    @Override
    public void fillPerSecondCooldown(Player player) {
        EnderPlayerDataSets.EnderWarpPlayerData data = getPlayerData(player);
        int cooldown = data.abilityCooldown;
        if (cooldown > 0){
            data.tickCount = dataHandlers.getTickCount(data.tickCount);
            data.abilityCooldown = dataHandlers.decreaseCooldown(data.abilityCooldown, data.tickCount);
        }
    }

    @Override
    public void drainPerSecondCooldown(Player player) {

    }


    @Override
    public void triggerAbility(ServerPlayer serverPlayer) {
        long currentTime = System.currentTimeMillis();
        serverPlayer.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
            if (cap instanceof PlayerPatch playerPatch) {
                if (playerPatch.getStamina() >= playerPatch.getMaxStamina()) {
                    if(getPlayerData(serverPlayer).abilityCooldown <= getPlayerData(serverPlayer).getMAX_COOLDOWN()) {
                        activateAbility(serverPlayer);
                        playSound(serverPlayer);
                        playerPatch.setStamina(playerPatch.getStamina() - playerPatch.getMaxStamina());
                        getPlayerData(serverPlayer).setAbilityCooldown(getPlayerData(serverPlayer).getMAX_COOLDOWN());
                    }
                }
            }
        });
    }

    @Override
    public void activateAbility(ServerPlayer serverPlayer) {
        EnderPlayerDataSets.EnderWarpPlayerData data = getPlayerData(serverPlayer);
        EnderPlayerDataSets.EnderFormPlayerData eData = getOrCreateEnderFormPlayerData(serverPlayer);
        int maxDistance = 8;
        Vec3 eyePos = eData.enderEntity.getEyePosition();
        Vec3 getViewVec = eData.enderEntity.getViewVector(1.0f);
        Vec3 endVec = eyePos.add(getViewVec.scale(maxDistance));

        eData.enderEntity.teleportTo(endVec.x, endVec.y, endVec.z);
    }

    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {

    }


    @Override
    public void decrementCooldown(Player player) {
        EnderPlayerDataSets.EnderFormPlayerData wData = dataSets.getOrCreateEnderFormPlayerData(player);
        EnderPlayerDataSets.EnderWarpPlayerData eData = getPlayerData(player);
        if(wData.isEnderActive && eData.abilityCooldown <= eData.getMAX_COOLDOWN()){
            fillPerSecondCooldown(player);
        }
    }


    @Override
    public void tickServerAbilityData(ServerPlayer player) {

    }

    @Override
    public void tickClientAbilityData(Player player) {

    }

    @Override
    public void syncDataToClient(ServerPlayer player) {

    }

    @Override
    public void playSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void playSound2(Player player) {

    }


    public void tickClientPlayer(Player player){
        EnderPlayerDataSets.EnderWarpPlayerData data = getPlayerData(player);
        getInstance().decrementCooldown(player);
    }
}
