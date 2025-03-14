package net.kenji.kenjiscombatforms.api.powers.WitherPowers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.event.CommonFunctions;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.kenji.kenjiscombatforms.network.witherform.ability1.SyncWitherDataPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class WitherDash implements Ability {

    private final WitherPlayerDataSets dataSets = WitherPlayerDataSets.getInstance();
    private final CommonFunctions dataHandlers = CommonFunctions.getInstance();
    private final Map<UUID, WitherPlayerDataSets.WitherDashPlayerData> playerDataMap = WitherPlayerDataSets.getInstance().A1playerDataMap;

    private static final WitherDash INSTANCE = new WitherDash();
    public GuiGraphics guiGraphics;


    @Override
    public String getName() {
        return AbilityManager.AbilityOption1.WITHER_ABILITY1.name();
    }


    WitherPlayerDataSets.WitherDashPlayerData getPlayerData(Player player) {
        return (WitherPlayerDataSets.WitherDashPlayerData) getAbilityData(player);
    }

    WitherPlayerDataSets.WitherMinionPlayerData getOrCreateMinionPlayerData(Player player) {
        return dataSets.getOrCreateMinionPlayerData(player);
    }

    @Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ServerForgeEvents {
        @SubscribeEvent
        public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                getInstance().getPlayerData(serverPlayer);
                getInstance().stopDash(serverPlayer);
            }
            Player player = event.getEntity();
            getInstance().stopDash(player);
        }

        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                getInstance().setAbilityCooldown(serverPlayer);
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
            }
        }

        @SubscribeEvent
        public void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                Player player = event.player;
                WitherPlayerDataSets.WitherDashPlayerData data = getInstance().getPlayerData(player);
            }
        }
    }


    public WitherDash() {
        // Private constructor to prevent instantiation
    }

    public static WitherDash getInstance() {
        return INSTANCE;
    }


    public int getAbilityCooldown(Player player) {
        return getPlayerData(player).abilityCooldown;
    }


    public void setAbilityCooldown(Player player) {
        getPlayerData(player).abilityCooldown = getPlayerData(player).abilityCooldown + getPlayerData(player).getMAX_COOLDOWN() / KenjisCombatFormsCommon.ABILITY1_COOLDOWN_DIVISION.get();
    }

    @Override
    public AbstractAbilityData getAbilityData(Player player) {
        return dataSets.getOrCreateDashPlayerData(player);
    }

    @Override
    public void fillPerSecondCooldown(Player player) {
        WitherPlayerDataSets.WitherDashPlayerData data = getPlayerData(player);
        int cooldown = data.abilityCooldown;
        if (cooldown > 0) {
            data.tickCount = dataHandlers.getTickCount(data.tickCount);
            data.abilityCooldown = dataHandlers.decreaseCooldown(data.abilityCooldown, data.tickCount);
        }
    }

    @Override
    public void drainPerSecondCooldown(Player player) {

    }


    @Override
    public void triggerAbility(ServerPlayer serverPlayer) {

    }

    @Override
    public void activateAbility(ServerPlayer serverPlayer) {

    }

    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {

    }


    public void decrementCooldown(Player player) {
        WitherPlayerDataSets.WitherMinionPlayerData mData = getOrCreateMinionPlayerData(player);
        if (isAbilityChosenOrEquipped(player)) {
            if (!mData.areMinionsActive) {
                fillPerSecondCooldown(player);
            }
        }
    }

    @Override
    public void tickServerAbilityData(ServerPlayer player) {
        WitherPlayerDataSets.WitherDashPlayerData data = getPlayerData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        if (abilityData.chosenAbility1.name().equals(getName())) {
            getInstance().decrementCooldown(player);
            updateDash(player);
            tickPause(player);
            syncDataToClient(player);
        }
    }

    @Override
    public void tickClientAbilityData(Player player) {
        updateDash(player);
    }

    @Override
    public void syncDataToClient(ServerPlayer player) {
        WitherPlayerDataSets.WitherDashPlayerData data = getPlayerData(player);
        if (isAbilityChosenOrEquipped(player)) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncWitherDataPacket(data.abilityCooldown)
            );
        }
    }

    public boolean isAbilityChosenOrEquipped(Player player) {
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);

        return abilityData.chosenAbility1.name().equals(getName());
    }

    @Override
    public void playSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.VEX_CHARGE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void playSound2(Player player) {

    }

    public void activatePause(Player player) {
        WitherPlayerDataSets.WitherDashPlayerData data = getPlayerData(player);
        data.isPauseActive = true;
        player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0f, 1.0f);
        // Apply initial dash motion
        player.setDeltaMovement(Vec3.ZERO);
        player.hasImpulse = true;
    }

    public void tickPause(Player player) {
        WitherPlayerDataSets.WitherDashPlayerData data = getPlayerData(player);
        if (data.isPauseActive) {
            player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 5, 1, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 5, -1, false, false));

            // Apply initial dash motion
            player.setDeltaMovement(Vec3.ZERO);
        }
    }

    public void activateDash(Player player, Vec3 dashDirection, double dashSpeed) {
        WitherPlayerDataSets.WitherDashPlayerData data = getPlayerData(player);
        data.isPauseActive = false;
        data.isDashActive = true;
        data.initialPosition = player.position();
        data.distanceTraveled = 0.0;
        data.dashTicksRemaining = data.MAX_DASH_TICKS;
        data.dashDirection = dashDirection.normalize();
        data.currentSpeed = Math.min(dashSpeed, data.MAX_SPEED);
        player.level().playSound(null, player.blockPosition(), SoundEvents.VEX_CHARGE, SoundSource.PLAYERS, 1.0f, 1.0f);

        // Apply initial dash motion
        player.setDeltaMovement(data.dashDirection.scale(data.currentSpeed));
        player.hasImpulse = true;

        data.abilityCooldown = data.abilityCooldown + data.getMAX_COOLDOWN() / 2;
    }

    private void movePlayerThroughBlocks(Player player, Vec3 motion) {
        double newX = player.getX() + motion.x;
        double newY = player.getY() + motion.y;
        double newZ = player.getZ() + motion.z;

        // Check if the new position is inside a block
        BlockPos newBlockPos = new BlockPos((int) newX, (int) newY, (int) newZ);
        BlockState blockState = player.level().getBlockState(newBlockPos);

        if (!blockState.isAir()) {
            // If inside a block, move the player to the nearest non-solid position
            newY = Math.ceil(newY);
        }
        player.setPos(newX, newY, newZ);
    }


    public void updateDash(Player player) {
        WitherPlayerDataSets.WitherDashPlayerData data = getPlayerData(player);

        if (player.level().isClientSide)
            if (data.isDashActive) {
                double remainingDistance = data.MAX_DISTANCE - data.distanceTraveled;

                // Deceleration logic
                if (remainingDistance < data.DECELERATION_DISTANCE) {
                    double decelerationFactor = remainingDistance / data.DECELERATION_DISTANCE;
                    data.currentSpeed = data.MAX_SPEED * decelerationFactor;
                }
                // Calculate motion for this tick
                Vec3 motion = data.dashDirection.scale(data.currentSpeed);

                movePlayerThroughBlocks(player, motion);

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
        WitherPlayerDataSets.WitherDashPlayerData data = getPlayerData(player);
        data.isDashActive = false;
        ensurePlayerStopped(player);
        data.distanceTraveled = 0.0;
        data.initialPosition = null;
        data.dashDirection = null;
        data.dashTicksRemaining = 0;
    }

    private void ensurePlayerStopped(Player player) {
        player.setDeltaMovement(Vec3.ZERO);
        player.fallDistance = 0f;

        // Ensure player is not inside a block
        BlockPos playerPos = player.blockPosition();
        BlockState blockState = player.level().getBlockState(playerPos);
        if (!blockState.isAir()) {
            double newY = Math.ceil(player.getY());
            player.setPos(player.getX(), newY, player.getZ());
        }

        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.teleport(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
            ServerLevel targetLevel = serverPlayer.serverLevel();

            double newX = serverPlayer.getX();
            double newY = serverPlayer.getY();
            double newZ = serverPlayer.getZ();
            Set<RelativeMovement> relativeMovements = Set.of();

            float newYaw = serverPlayer.getYRot();  // or your desired yaw
            float newPitch = serverPlayer.getXRot();  // or your desired pitch
            serverPlayer.teleportTo(
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
}
