package net.kenji.kenjiscombatforms.api.powers.VoidPowers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbilityDamageGainStrategy;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;

import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.entity.ModEntities;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.EnderEntity;
import net.kenji.kenjiscombatforms.event.CommonFunctions;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.kenji.kenjiscombatforms.network.particle_packets.EndParticlesTickPacket;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.kenji.kenjiscombatforms.network.voidform.ability3.SyncVoidData3Packet;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.network.PacketDistributor;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.events.PlayerEvents;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributeSupplier;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.*;

public class EnderFormAbility implements Ability {

    private final EnderPlayerDataSets dataSets = EnderPlayerDataSets.getInstance();
    private final Map<UUID, EnderPlayerDataSets.EnderFormPlayerData> playerDataMap = dataSets.A3playerDataMap;
    private final CommonFunctions dataHandlers = CommonFunctions.getInstance();
    private Entity enderEntity;

    private EnderPlayerDataSets.TeleportPlayerData getOrCreateTeleportPlayerData(Player player) {
        return dataSets.getOrCreateTeleportPlayerData(player);
    }

    private EnderPlayerDataSets.VoidRiftPlayerData getOrCreateVoidRiftPlayerData(Player player) {
        return dataSets.getOrCreateVoidRiftPlayerData(player);
    }

    public EnderPlayerDataSets.EnderFormPlayerData getPlayerData(Player player) {
       return (EnderPlayerDataSets.EnderFormPlayerData) getAbilityData(player);
    };


    @Override
    public String getName() {
        return AbilityManager.AbilityOption3.VOID_FINAL.name();
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
            Entity existingDecoy = getInstance().getPlayerData(event.getEntity()).playerEnderMap.remove(event.getEntity().getUUID());
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
            Entity existingDecoy = getInstance().getPlayerData(event.getEntity()).playerEnderMap.remove(event.getEntity().getUUID());
            if (existingDecoy != null) {
                existingDecoy.remove(Entity.RemovalReason.DISCARDED);
            }
            getInstance().playerDataMap.remove(event.getEntity().getUUID());
        }

        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            if (event.getOriginal() instanceof ServerPlayer originalPlayer &&
                    event.getEntity() instanceof ServerPlayer newPlayer) {

                EnderPlayerDataSets.EnderFormPlayerData originalData = getInstance().getPlayerData(originalPlayer);
                EnderPlayerDataSets.EnderFormPlayerData newData = getInstance().getPlayerData(newPlayer);

                // Transfer the cooldown data
                newData.abilityCooldown = originalData.abilityCooldown;

                // If you have other data to transfer, do it here
                // newData.otherAttribute = originalData.otherAttribute;

                // Sync the data to the client
                getInstance().syncDataToClient(newPlayer);
            }
        }
    }


    private static final EnderFormAbility INSTANCE = new EnderFormAbility();


    public EnderFormAbility() {
        // Private constructor to prevent instantiation
    }

    public static EnderFormAbility getInstance() {
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
        return dataSets.getOrCreateEnderFormPlayerData(player);
    }

    @Override
    public void fillPerSecondCooldown(Player player) {
        EnderPlayerDataSets.EnderFormPlayerData data = getPlayerData(player);
        if(data.abilityCooldown > 0) {
            data.tickCount = dataHandlers.getTickCount(data.tickCount);
            data.abilityCooldown = dataHandlers.decreaseCooldown(data.abilityCooldown, data.tickCount);
        }
    }

    @Override
    public void drainPerSecondCooldown(Player player) {
        EnderPlayerDataSets.EnderFormPlayerData data = getPlayerData(player);
        data.tickCount = dataHandlers.getTickCount(data.tickCount);
        data.abilityCooldown = dataHandlers.increaseCooldown(data.abilityCooldown, data.tickCount);
    }




    @Override
    public void triggerAbility(ServerPlayer serverPlayer) {
        EnderPlayerDataSets.EnderFormPlayerData data = getPlayerData(serverPlayer);
        if (!data.isEnderActive && data.abilityCooldown == 0) {
            activateAbility(serverPlayer);
            data.hasPlayedSound = false;
        } else {
            deactivateAbilityOptional(serverPlayer);
        }
    }

    @Override
    public void activateAbility(ServerPlayer serverPlayer) {
        EnderPlayerDataSets.EnderFormPlayerData data = getPlayerData(serverPlayer);
        data.isEnderActive = true;
        activateEnderSummon(serverPlayer);
        playSound(serverPlayer);
        syncDataToClient(serverPlayer);
    }

    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {
        EnderPlayerDataSets.EnderFormPlayerData data = getPlayerData(serverPlayer);
        serverPlayer.noPhysics = false;
        double radius = 2.0;
        serverPlayer.removeEffect(MobEffects.INVISIBILITY);
        serverPlayer.removeEffect(MobEffects.LEVITATION);
        data.isEnderActive = false;
        serverPlayer.setCamera(serverPlayer);
        serverPlayer.stopRiding();
        serverPlayer.setInvulnerable(false);

        Entity existingEnder = data.playerEnderMap.remove(serverPlayer.getUUID());
        data.enderEntity = existingEnder;
        if (existingEnder != null) {
            existingEnder.remove(Entity.RemovalReason.DISCARDED);
        }
        syncDataToClient(serverPlayer);
    }

    public static class CurrentDamageGainStrategy implements AbilityDamageGainStrategy {


        @Override
        public void fillDamageCooldown(Player player) {
            EnderPlayerDataSets.EnderFormPlayerData data = getInstance().getPlayerData(player);
            if(KenjisCombatFormsCommon.ABILITY3_COMBAT_MODE.get()) {
               if(!data.isAbilityActive()) {
                   if (data.abilityCooldown > 0) {
                       data.abilityCooldown = data.abilityCooldown - KenjisCombatFormsCommon.COMBAT_MODE_GAIN_AMOUNT.get();
                   }
               } else {
                   if (data.abilityCooldown > 0) {
                       data.abilityCooldown = data.abilityCooldown - KenjisCombatFormsCommon.COMBAT_MODE_GAIN_AMOUNT.get() / 2;
                   }
               }
                if(player instanceof ServerPlayer serverPlayer) {
                    getInstance().syncDataToClient(serverPlayer);
                }
            }
        }
    }

    @Override
    public void decrementCooldown(Player player) {
        EnderPlayerDataSets.EnderFormPlayerData data = getPlayerData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        if (isAbilityChosenOrEquipped(player)) {
            if (EffectiveSide.get() == LogicalSide.SERVER) {
                if (player instanceof ServerPlayer serverPlayer) {
                    if(!KenjisCombatFormsCommon.ABILITY3_COMBAT_MODE.get()) {
                        if (data.abilityCooldown > 0 && !data.isEnderActive) {
                                fillPerSecondCooldown(player);
                        }
                    }if (data.abilityCooldown < data.getMAX_COOLDOWN() && data.isEnderActive) {
                        drainPerSecondCooldown(player);
                    }
                    if (data.abilityCooldown >= data.getMAX_COOLDOWN() && data.isEnderActive) {
                        deactivateAbilityOptional(serverPlayer);
                        Entity existingShadow = data.playerEnderMap.remove(player.getUUID());
                        if (existingShadow != null) {
                            existingShadow.remove(Entity.RemovalReason.DISCARDED);
                        }
                        if (!data.hasPlayedSound) {
                            data.hasPlayedSound = true;
                            playSound(player);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void tickServerAbilityData(ServerPlayer player) {
        EnderPlayerDataSets.EnderFormPlayerData data = getPlayerData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);

        if(abilityData.chosenFinal.name().equals(getName())) {
            getInstance().decrementCooldown(player);
        }
        syncDataToClient(player);
        if(enderEntity instanceof EnderEntity entity){
            if (data.isEnderActive) {
                preventCombatActions(player);
                player.setCamera(enderEntity);
                player.setPose(Pose.FALL_FLYING);
                player.setShiftKeyDown(false);
                player.setInvulnerable(true);
                player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 5, 2, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 5, -1, false, false));
                ClientVoidData.setIsEnderActive(true);

                NetworkHandler.INSTANCE.send(
                        PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                        new EndParticlesTickPacket(player.getX(), player.getY(), player.getZ(), player.isInvisible())
                );

                if (entity.isDeadOrDying()) {
                    data.isEnderActive = false;
                    deactivateAbilityOptional(player);
                }
            }
        }
    }

    @Override
    public void tickClientAbilityData(Player player) {
        EnderPlayerDataSets.EnderFormPlayerData data = getInstance().playerDataMap.computeIfAbsent(player.getUUID(), k -> new EnderPlayerDataSets.EnderFormPlayerData());
        getInstance().decrementCooldown(player);
        if (ClientVoidData.getIsEnderActive()) {
            preventCombatActions(player);
            player.noPhysics = true;
        }
    }

    @Override
    public void syncDataToClient(ServerPlayer player) {
        EnderPlayerDataSets.EnderFormPlayerData data = getPlayerData(player);
       if(isAbilityChosenOrEquipped(player)) {
           NetworkHandler.INSTANCE.send(
                   PacketDistributor.PLAYER.with(() -> player),
                   new SyncVoidData3Packet(data.abilityCooldown, data.isEnderActive)
           );
       }
    }

    public boolean isAbilityChosenOrEquipped(Player player){
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);

        return abilityData.chosenFinal.name().equals(getName());
    }



    public void activateEnderSummon(ServerPlayer player) {
        EnderPlayerDataSets.EnderFormPlayerData data = getPlayerData(player);
        data.hasPlayedSound = false;
        data.isEnderActive = true;

        getNearestEntity(player);

        enderEntity = ModEntities.ENDER_PLAYER.get().spawn(player.serverLevel(), player.blockPosition(), MobSpawnType.TRIGGERED);
        data.enderEntity = enderEntity;
        if(data.enderEntity != null) {
            data.enderEntity.setYRot(player.getYRot());
            data.enderEntity.setXRot(player.getXRot());

            if (enderEntity != null) {
                data.playerEnderMap.put(player.getUUID(), enderEntity);
            }
        }
    }


    public void preventCombatActions(Player player) {
        EnderPlayerDataSets.EnderFormPlayerData data = getPlayerData(player);

        player.setPose(Pose.SITTING);
        player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
            if (cap instanceof PlayerPatch playerPatch) {
                playerPatch.getAnimator().playAnimation(Animations.BIPED_WALK, 0.0F);
                playerPatch.getEntityState().setState(EntityState.INACTION, true);
                playerPatch.setLastAttackSuccess(false);
                playerPatch.getOriginal().skipAttackInteraction(data.enderEntity);

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


    @Override
    public void playSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENDERMAN_SCREAM, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void playSound2(Player player) {
    }

    @OnlyIn(Dist.CLIENT)
    public void spawnParticles(Player player) {
        int currentVoidCooldown = ClientVoidData.getCooldown3();
        BlockParticleOption whiteFallingDust = new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.WHITE_WOOL.defaultBlockState());

        if (player.isInvisible()) {
            Random random = new Random();
            int particleCount = 100;

            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();

            for (int i = 0; i < particleCount; i++) {
                double offsetX = (random.nextDouble() - 0.5) * 2; // Spread in X direction
                double offsetY = (random.nextDouble() - 0.5) * 2; // Spread in Y direction
                double offsetZ = (random.nextDouble() - 0.5) * 2; // Spread in Z direction

                double velocityX = (random.nextDouble() - 0.5) * 0.5; // Random X velocity
                double velocityY = random.nextDouble() * 0.5; // Upward Y velocity
                double velocityZ = (random.nextDouble() - 0.5) * 0.5; // Random Z velocity

                player.level().addParticle(
                        ParticleTypes.PORTAL,
                        x + offsetX,
                        y + 1 + offsetY,
                        z + offsetZ,
                        velocityX,
                        velocityY,
                        velocityZ
                );
            }
        }
    }
}
