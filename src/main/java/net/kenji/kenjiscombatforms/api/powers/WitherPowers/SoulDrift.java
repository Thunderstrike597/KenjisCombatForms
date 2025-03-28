package net.kenji.kenjiscombatforms.api.powers.WitherPowers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.ClientEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbilityDamageGainStrategy;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.kenji.kenjiscombatforms.entity.ModEntities;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.ShadowPlayerEntity;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.EnderEntity;
import net.kenji.kenjiscombatforms.event.CommonFunctions;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.globalformpackets.SyncAbility2Packet;
import net.kenji.kenjiscombatforms.network.particle_packets.SmokeParticlesPacket;
import net.kenji.kenjiscombatforms.network.particle_packets.SoulParticlesTickPacket;
import net.kenji.kenjiscombatforms.network.witherform.ability2.SoulDriftPacket;
import net.kenji.kenjiscombatforms.network.witherform.ability2.SyncWitherData2Packet;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SoulDrift implements Ability {
    private Entity shadowEntity;
    private final WitherPlayerDataSets dataSets = WitherPlayerDataSets.getInstance();
    private final CommonFunctions dataHandlers = CommonFunctions.getInstance();


    @Override
    public String getName() {
        return "WITHER_ABILITY2";
    }

    @Override
    public int getGUIDrawPosY() {
        return 180;
    }

    @Override
    public int getGUIDrawPosX() {
        return 186;
    }


    private final Map<UUID, WitherPlayerDataSets.SoulDriftPlayerData> playerDataMap = WitherPlayerDataSets.getInstance().A2playerDataMap;
    public Map<UUID, Entity> playerShadowMap = new ConcurrentHashMap<>();



    public WitherPlayerDataSets.SoulDriftPlayerData getPlayerData(Player player) {
      return (WitherPlayerDataSets.SoulDriftPlayerData) getAbilityData(player);
    }



    @Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ServerForgeEvents {
        @SubscribeEvent
        public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                getInstance().getPlayerData(serverPlayer);
                getInstance().syncDataToClient(serverPlayer);
               getInstance().deactivateAbilityOptional(serverPlayer);
            }
        }

        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
            Entity existingDecoy = getInstance().playerShadowMap.remove(event.getEntity().getUUID());
            if (existingDecoy != null) {
                existingDecoy.remove(Entity.RemovalReason.DISCARDED);
            }
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                getInstance().setAbilityCooldown(serverPlayer, getInstance().getPlayerData(serverPlayer).getMAX_COOLDOWN());
                getInstance().syncDataToClient(serverPlayer);
            }
        }

        @SubscribeEvent
        public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
            Entity existingDecoy = getInstance().playerShadowMap.remove(event.getEntity().getUUID());
            if (existingDecoy != null) {
                existingDecoy.remove(Entity.RemovalReason.DISCARDED);
            }
            getInstance().playerDataMap.remove(event.getEntity().getUUID());
        }

        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            if (event.getOriginal() instanceof ServerPlayer originalPlayer &&
                    event.getEntity() instanceof ServerPlayer newPlayer) {
                WitherPlayerDataSets.SoulDriftPlayerData originalData = getInstance().getPlayerData(originalPlayer);
                WitherPlayerDataSets.SoulDriftPlayerData newData = getInstance().getPlayerData(newPlayer);

                // Transfer the cooldown data
                newData.abilityCooldown = originalData.abilityCooldown;

                // If you have other data to transfer, do it here
                // newData.otherAttribute = originalData.otherAttribute;

                // Sync the data to the client
                getInstance().syncDataToClient(newPlayer);
            }
        }
    }




    private static final SoulDrift INSTANCE = new SoulDrift();


    public SoulDrift() {
        // Private constructor to prevent instantiation
    }

    public static SoulDrift getInstance() {
        return INSTANCE;
    }


    public int getAbilityCooldown(ServerPlayer player) {
        return getPlayerData(player).abilityCooldown;
    }

    public void setAbilityCooldown(ServerPlayer player, int cooldown) {
        getPlayerData(player).abilityCooldown = Math.min(Math.max(cooldown, 0), getPlayerData(player).getMAX_COOLDOWN());
    }


    @Override
    public AbstractAbilityData getAbilityData(Player player) {
        return dataSets.getOrCreateSoulDriftPlayerData(player);
    }

    @Override
    public void fillPerSecondCooldown(Player player) {
        WitherPlayerDataSets.SoulDriftPlayerData data = getPlayerData(player);
        int cooldown = data.abilityCooldown;
        if (cooldown > 0){
            data.tickCount = dataHandlers.getTickCount(data.tickCount);
            data.abilityCooldown = dataHandlers.decreaseCooldown(data.abilityCooldown, data.tickCount);
        }
    }

    @Override
    public void drainPerSecondCooldown(Player player) {
        WitherPlayerDataSets.SoulDriftPlayerData data = getPlayerData(player);
        data.tickCount = dataHandlers.getTickCount(data.tickCount);
        data.abilityCooldown = dataHandlers.increaseCooldown(data.abilityCooldown, data.tickCount);
    }

    @Override
    public boolean getAbilityActive(Player player) {
        return getAbilityData(player).isAbilityActive();
    }

    @Override
    public void sendPacketToServer(Player player) {
        if(!ClientEventHandler.getInstance().getAreFinalsActive()) {
            NetworkHandler.INSTANCE.sendToServer(new SoulDriftPacket());
        }
    }


    public static class CurrentDamageGainStrategy implements AbilityDamageGainStrategy {


        @Override
        public void fillDamageCooldown(Player player) {
            WitherPlayerDataSets.SoulDriftPlayerData data = getInstance().getPlayerData(player);

            if(EpicFightCombatFormsCommon.ABILITY2_COMBAT_MODE.get()) {
                if(!getInstance().getAbilityActive(player)) {
                    if (data.abilityCooldown > 0) {
                        data.abilityCooldown = data.abilityCooldown - EpicFightCombatFormsCommon.COMBAT_MODE_GAIN_AMOUNT.get();
                    }
                    if (player instanceof ServerPlayer serverPlayer) {
                        getInstance().syncDataToClient(serverPlayer);
                    }
                }
            }
        }
    }

    @Override
    public void triggerAbility(ServerPlayer serverPlayer) {
        WitherPlayerDataSets.SoulDriftPlayerData data = getPlayerData(serverPlayer);
        if (!getAbilityActive(serverPlayer) && data.abilityCooldown <= 0) {
            activateAbility(serverPlayer);
            data.hasPlayedSound = false;
        }
        else if (getAbilityActive(serverPlayer)) {
            deactivateAbilityOptional(serverPlayer);
        }
    }

    @Override
    public void activateAbility(ServerPlayer serverPlayer) {
        WitherPlayerDataSets.SoulDriftPlayerData data = getPlayerData(serverPlayer);
        Vec3 pushVector = new Vec3(0, 2.0, 0);
        data.setAbilityActive(true);
        activateDecoySummon(serverPlayer);


        serverPlayer.push(pushVector.x, pushVector.y, pushVector.z);
        serverPlayer.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, data.getMAX_COOLDOWN() * 20, 2, false, false));
        serverPlayer.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 12, 1, false, false));
        serverPlayer.serverLevel().getServer().tell(new net.minecraft.server.TickTask(serverPlayer.serverLevel().getServer().getTickCount() + 12, () -> {
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.LEVITATION, data.getMAX_COOLDOWN() * 20, -1, false, false));
        }));


        handleNearbyMonsters(serverPlayer);
        playSound(serverPlayer);
        syncDataToClient(serverPlayer);

        NetworkHandler.INSTANCE.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverPlayer),
                new SmokeParticlesPacket(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ()));
    }

    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {
        WitherPlayerDataSets.SoulDriftPlayerData data = getPlayerData(serverPlayer);
        double radius = 2.0;
        serverPlayer.removeEffect(MobEffects.INVISIBILITY);
        serverPlayer.removeEffect(MobEffects.LEVITATION);
        data.setAbilityActive(false);
        Entity existingDecoy = playerShadowMap.remove(serverPlayer.getUUID());
        if (existingDecoy != null) {
            existingDecoy.remove(Entity.RemovalReason.DISCARDED);
        }

        NetworkHandler.INSTANCE.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverPlayer),
                new SmokeParticlesPacket(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ()));
        syncDataToClient(serverPlayer);

    }

    @Override
    public void decrementCooldown(Player player) {
        WitherPlayerDataSets.SoulDriftPlayerData data = playerDataMap.computeIfAbsent(player.getUUID(), k -> new WitherPlayerDataSets.SoulDriftPlayerData());
        if (isAbilityChosenOrEquipped(player)) {
            if(!EpicFightCombatFormsCommon.ABILITY2_COMBAT_MODE.get()) {
                if (!getAbilityActive(player)) {
                    fillPerSecondCooldown(player);
                }
            }else if (data.abilityCooldown < data.getMAX_COOLDOWN() && getAbilityActive(player)) {
                drainPerSecondCooldown(player);
                drainPerSecondCooldown(player);
            }
            if (data.abilityCooldown >= data.getMAX_COOLDOWN()) {
                player.removeEffect(MobEffects.INVISIBILITY);
                player.removeEffect(MobEffects.LEVITATION);
                player.setInvisible(false);
                data.setAbilityActive(false);
                Entity existingShadow = playerShadowMap.remove(player.getUUID());
                if (existingShadow != null) {
                    existingShadow.remove(Entity.RemovalReason.DISCARDED);
                }
                if (!data.hasPlayedSound) {
                    data.hasPlayedSound = true;
                    playSound(player);
                }
            }
            if (data.abilityCooldown >= data.getMAX_COOLDOWN() && getAbilityActive(player)) {
                deactivateClientInvisibility(player);
            }
        }
    }


    @Override
    public void tickServerAbilityData(ServerPlayer player) {
        WitherPlayerDataSets.SoulDriftPlayerData data = getPlayerData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);

        if(abilityData.chosenAbility2.equals(getName())) {
            getInstance().decrementCooldown(player);
        }
        syncDataToClient(player);
        if (getAbilityActive(player)) {

            NetworkHandler.INSTANCE.send(
                    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                    new SoulParticlesTickPacket(player.getX(), player.getY(), player.getZ(), player.isInvisible()));

            if (player.isInvisible() && shadowEntity instanceof EnderEntity && shadowEntity.isAlive()) {

                ((EnderEntity) shadowEntity).setOpacity(7.0f);
            }
        }
    }

    @Override
    public void tickClientAbilityData(Player player) {
        WitherPlayerDataSets.SoulDriftPlayerData data = getInstance().playerDataMap.computeIfAbsent(player.getUUID(), k -> new WitherPlayerDataSets.SoulDriftPlayerData());
        getInstance().decrementCooldown(player);
        if (getAbilityActive(player)){
            preventCombatActions(player);
            if (player.isShiftKeyDown() || player.isCrouching()) {
                if (!player.level().getBlockState(player.blockPosition()).isSolid()) {
                    Vec3 pushVector = new Vec3(0, -0.03, 0);
                    player.push(pushVector.x, pushVector.y, pushVector.z);
                }
            }else if (!player.level().getBlockState(player.blockPosition().below(3)).isSolid() && !player.level().getBlockState(player.blockPosition()).isSolid()) {
                Vec3 pushVector = new Vec3(0, -0.03, 0);
                player.push(pushVector.x, pushVector.y, pushVector.z);
            }
        }
    }

    @Override
    public void syncDataToClient(ServerPlayer player) {
        WitherPlayerDataSets.SoulDriftPlayerData data = getPlayerData(player);
        if(isAbilityChosenOrEquipped(player)) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncAbility2Packet(data.abilityCooldown, getAbilityActive(player), false)
            );
        }
    }

    public boolean isAbilityChosenOrEquipped(Player player){
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);

        return abilityData.chosenAbility2.equals(getName());
    }


    public void activateDecoySummon(ServerPlayer player) {
        WitherPlayerDataSets.SoulDriftPlayerData data = getPlayerData(player);
        double radius = 10.0;
        data.hasPlayedSound = false;

        getNearestEntity(player);
        // Remove existing decoy if present
        Scoreboard scoreboard = player.level().getScoreboard();
        String teamName = "active_shadows";
        PlayerTeam team = scoreboard.getPlayerTeam(teamName);

        shadowEntity = ModEntities.SHADOW_PLAYER.get().spawn(player.serverLevel(), player.blockPosition(), MobSpawnType.TRIGGERED);
        if(team == null){
            team = scoreboard.addPlayerTeam(teamName);
            team.setColor(ChatFormatting.DARK_GRAY);
            team.setAllowFriendlyFire(false);
        }

        PlayerTeam minionTeam = scoreboard.getPlayerTeam(WitherMinions.getInstance().teamName);

       if (shadowEntity != null) {
           scoreboard.addPlayerToTeam(shadowEntity.getStringUUID(), team);

            playerShadowMap.put(player.getUUID(), shadowEntity);
            if (shadowEntity instanceof ShadowPlayerEntity shadowPlayer) {
                shadowPlayer.setOwnerUUID(player.getUUID());
            }
            if(minionTeam != null){
           Objects.requireNonNull(scoreboard.getPlayerTeam(teamName)).isAlliedTo(minionTeam);
           }
        }
    }


    public static void preventCombatActions(Player player) {
        player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
            if (cap instanceof PlayerPatch playerPatch) {
                playerPatch.getAnimator().playAnimation(Animations.BIPED_WALK, 0.0F);
                // Reset the player's combat state
                playerPatch.getEntityState().inaction();
            }
        });
    }
    public static void ResetCombatActions(Player player) {
        player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
            if (cap instanceof PlayerPatch playerPatch) {
                // Reset the player's combat state
                playerPatch.getEntityState().inaction();
            }
        });
    }

    private Entity getNearestEntity(Player player) {
        double searchRadius = 10.0; // Adjust this value as needed
        AABB searchBox = player.getBoundingBox().inflate(searchRadius);

        List<Entity> nearbyEntities = player.level().getEntities(player, searchBox);

        Entity closestEntity = null;
        double closestDistanceSq = Double.MAX_VALUE;

        for (Entity entity : nearbyEntities) {
            if (entity instanceof EnderEntity) continue;
            double distanceSq = player.distanceToSqr(entity);
            if (distanceSq < closestDistanceSq) {
                closestDistanceSq = distanceSq;
                closestEntity = entity;
            }
        }

        return closestEntity;
    }


    public void deactivateClientInvisibility(Player player) {
        preventCombatActions(player);
    }

    @Override
    public void playSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.VEX_CHARGE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void playSound2(Player player) {

    }

    private void handleNearbyMonsters(Player player) {
        double radius = 30.0;
        AABB searchArea = new AABB(player.getOnPos()).inflate(radius);
        TargetingConditions targetingConditions = TargetingConditions.forCombat();
        List<Monster> nearbyEntities = player.level().getNearbyEntities(Monster.class, targetingConditions, player, searchArea);
        for (Monster monster : nearbyEntities) {
            if (monster.getTarget() != null && monster.getTarget().is(player) && player.level().getBrightness(LightLayer.BLOCK, player.blockPosition()) < 8) {
                monster.setTarget(null);
                monster.targetSelector.removeGoal(new NearestAttackableTargetGoal<>(monster, Player.class, true));
            }
        }
    }
}
