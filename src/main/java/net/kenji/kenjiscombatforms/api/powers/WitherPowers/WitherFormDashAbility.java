package net.kenji.kenjiscombatforms.api.powers.WitherPowers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.WitherPlayerEntity;
import net.kenji.kenjiscombatforms.event.CommonFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class WitherFormDashAbility implements Ability {

    private final WitherPlayerDataSets dataSets = WitherPlayerDataSets.getInstance();
    private final Map<UUID, WitherPlayerDataSets.WitherFormDashPlayerData> playerDataMap = dataSets.A6playerDataMap;
    private static final WitherFormDashAbility INSTANCE = new WitherFormDashAbility();
    private final CommonFunctions dataHandlers = CommonFunctions.getInstance();



    @Override
    public String getName() {
        return AbilityManager.AbilityOption3.WITHER_FINAL.name();
    }

    public WitherPlayerDataSets.WitherFormDashPlayerData getPlayerData(Player player) {
        return (WitherPlayerDataSets.WitherFormDashPlayerData) getAbilityData(player);
    }
    public WitherPlayerDataSets.WitherFormPlayerData getOrCreateWitherFormPlayerData(Player player) {
        return dataSets.getOrCreateWitherFormPlayerData(player);
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

                WitherPlayerDataSets.WitherFormDashPlayerData originalData = getInstance().getPlayerData(originalPlayer);
                WitherPlayerDataSets.WitherFormDashPlayerData newData = getInstance().getPlayerData(newPlayer);

                // Transfer the cooldown data
                newData.abilityCooldown = originalData.abilityCooldown;

                // If you have other data to transfer, do it here
                // newData.otherAttribute = originalData.otherAttribute;

                // Sync the data to the client
                getInstance().syncDataToClient(newPlayer);
            }
        }
    }




    public WitherFormDashAbility() {
        // Private constructor to prevent instantiation
    }

    public static WitherFormDashAbility getInstance() {
        return INSTANCE;
    }


    public float getAbilityCooldown(Player player) {
        return getPlayerData(player).abilityCooldown;
    }

    public void setAbilityCooldown(Player player) {
        getPlayerData(player).abilityCooldown = getPlayerData(player).abilityCooldown + getPlayerData(player).getMAX_COOLDOWN() / 2;
    }



    @Override
    public AbstractAbilityData getAbilityData(Player player) {
        return dataSets.getOrCreateWitherFormDashPlayerData(player);
    }

    @Override
    public void fillPerSecondCooldown(Player player) {
        WitherPlayerDataSets.WitherFormDashPlayerData data = getPlayerData(player);
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

    }

    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {

    }


    @Override
    public void decrementCooldown(Player player) {
        WitherPlayerDataSets.WitherFormPlayerData wData = dataSets.getOrCreateWitherFormPlayerData(player);
        WitherPlayerDataSets.WitherFormDashPlayerData eData = getPlayerData(player);
        long currentTime = System.currentTimeMillis();
       if(player instanceof ServerPlayer serverPlayer) {
           serverPlayer.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
               if (cap instanceof PlayerPatch playerPatch) {
                   if (playerPatch.getStamina() >= playerPatch.getMaxStamina()) {
                       if (getPlayerData(serverPlayer).abilityCooldown <= getPlayerData(serverPlayer).getMAX_COOLDOWN()) {
                           if (wData.isWitherActive && eData.abilityCooldown <= eData.getMAX_COOLDOWN()) {
                               fillPerSecondCooldown(player);
                           }
                       }
                   }
               }
           });
       }
    }


    @Override
    public void tickServerAbilityData(ServerPlayer player) {
        decrementCooldown(player);
        updateDash(player);
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
                SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.8F, 0.8F);
    }

    @Override
    public void playSound2(Player player) {

    }

    public void activateDash(Player player, Vec3 dashDirection, double dashSpeed) {
        WitherPlayerDataSets.WitherFormDashPlayerData data = getPlayerData(player);
        WitherPlayerDataSets.WitherFormPlayerData wData = getOrCreateWitherFormPlayerData(player);

        data.isDashActive = true;
        data.initialPosition = player.position();
        data.distanceTraveled = 0.0;
        data.dashTicksRemaining = data.MAX_DASH_TICKS;
        data.dashDirection = dashDirection.normalize();
        data.currentSpeed = Math.min(dashSpeed, data.MAX_SPEED);

        // Apply initial dash motion
        player.setDeltaMovement(data.dashDirection.scale(data.currentSpeed));
        player.hasImpulse = true;

    }

    private void moveWitherEntityThroughBlocks(Player player, Vec3 motion) {
        WitherPlayerDataSets.WitherFormPlayerData wData = getOrCreateWitherFormPlayerData(player);


        double newX = player.getX() + motion.x;
        double newY = player.getY() + motion.y;
        double newZ = player.getZ() + motion.z;

        // Check if the new position is inside a block
        BlockPos newBlockPos = new BlockPos((int)newX, (int)newY, (int)newZ);
        BlockState blockState =  player.level().getBlockState(newBlockPos);

        if (!blockState.isAir()) {
            // If inside a block, move the player to the nearest non-solid position
            newY = Math.ceil(newY);
        }
        player.setPos(newX, newY, newZ);
    }




    public void updateDash(Player player) {
        WitherPlayerDataSets.WitherFormDashPlayerData data = getPlayerData(player);
        WitherPlayerDataSets.WitherFormPlayerData wData = getOrCreateWitherFormPlayerData(player);

        if (data.isDashActive) {
            double remainingDistance = data.MAX_DISTANCE - data.distanceTraveled;

            // Deceleration logic
            if (remainingDistance < data.DECELERATION_DISTANCE) {
                double decelerationFactor = remainingDistance / data.DECELERATION_DISTANCE;
                data.currentSpeed = data.MAX_SPEED * decelerationFactor;
            }
            // Calculate motion for this tick
            Vec3 motion = data.dashDirection.scale(data.currentSpeed);

            moveWitherEntityThroughBlocks(player, motion);

            // Apply motion
            player.setDeltaMovement(motion);
            player.move(MoverType.SELF, motion);

            // Update distance traveled
            Vec3 newPosition = player.position();
            data.distanceTraveled = data.initialPosition.distanceTo(newPosition);

            data.dashTicksRemaining--;

            // Stop conditions
            if (data.distanceTraveled >= data.MAX_DISTANCE || data.dashTicksRemaining <= 0) {
                stopDash(player);
            }
        }
    }

    private void stopDash(Player player) {
        WitherPlayerDataSets.WitherFormDashPlayerData data = getPlayerData(player);
        WitherPlayerDataSets.WitherFormPlayerData wData = getOrCreateWitherFormPlayerData(player);
        data.isDashActive = false;
        ensureWitherStopped(player);
        data.distanceTraveled = 0.0;
        data.initialPosition = null;
        data.dashDirection = null;
        data.dashTicksRemaining = 0;
    }

    private void ensureWitherStopped(Player player) {
        WitherPlayerDataSets.WitherFormPlayerData wData = getOrCreateWitherFormPlayerData(player);

        player.setDeltaMovement(Vec3.ZERO);
        player.fallDistance = 0f;

        // Ensure player is not inside a block
        BlockPos witherPos = player.blockPosition();
        BlockState blockState = player.level().getBlockState(witherPos);
        if (!blockState.isAir()) {
            double newY = Math.ceil(player.getY());
            player.setPos(player.getX(), newY, player.getZ());
        }


            ServerLevel targetLevel = (ServerLevel) player.level();

            double newX = player.getX();
            double newY = player.getY();
            double newZ = player.getZ();
            Set<RelativeMovement> relativeMovements = Set.of();

            float newYaw = player.getYRot();  // or your desired yaw
            float newPitch = player.getXRot();  // or your desired pitch
        player.teleportTo(
                    targetLevel,
                    newX,
                    newY,
                    newZ,
                    relativeMovements,
                    newYaw,
                    newPitch
            );
    }
}
