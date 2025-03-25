package net.kenji.kenjiscombatforms.api.powers.VoidPowers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.event.CommonFunctions;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability5.SyncVoidData5Packet;
import net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability5.VoidGrabPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

public class VoidGrab implements FinalAbility {

    private final EnderPlayerDataSets dataSets = EnderPlayerDataSets.getInstance();
    private final Map<UUID, EnderPlayerDataSets.VoidGrabPlayerData> playerDataMap = EnderPlayerDataSets.getInstance().A5playerDataMap;
    private final CommonFunctions dataHandlers = CommonFunctions.getInstance();
    private static final VoidGrab INSTANCE = new VoidGrab();


    @Override
    public String getName() {
        return AbilityManager.AbilityOption5.VOID_GRAB.name();
    }

    @Override
    public String getFinalAbilityName() {
        return AbilityManager.AbilityOption3.VOID_FINAL.name();
    }


    public EnderPlayerDataSets.VoidGrabPlayerData getPlayerData(Player player) {
        return (EnderPlayerDataSets.VoidGrabPlayerData) getAbilityData(player);
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
            }Player player = event.getEntity();

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

                EnderPlayerDataSets.VoidGrabPlayerData originalData = getInstance().getPlayerData(originalPlayer);
                EnderPlayerDataSets.VoidGrabPlayerData newData = getInstance().getPlayerData(newPlayer);

                // Transfer the cooldown data
                newData.abilityCooldown = originalData.abilityCooldown;

                // If you have other data to transfer, do it here
                // newData.otherAttribute = originalData.otherAttribute;

                // Sync the data to the client
                getInstance().syncDataToClient(newPlayer);
            }
        }

        @SubscribeEvent
        public void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                Player player = event.player;
                EnderPlayerDataSets.VoidGrabPlayerData data = getInstance().getPlayerData(player);
            }
        }
    }




    public VoidGrab() {
    }

    public static VoidGrab getInstance() {
        return INSTANCE;
    }


    public int getAbilityCooldown(Player player) {
        return getPlayerData(player).abilityCooldown;
    }



    public void setAbilityCooldown(Player player) {
        getPlayerData(player).abilityCooldown = getPlayerData(player).abilityCooldown + getPlayerData(player).getMAX_COOLDOWN() / 2;
    }

    @Override
    public AbstractAbilityData getAbilityData(Player player) {
        return dataSets.getOrCreateVoidGrabPlayerData(player);
    }

    @Override
    public boolean getFinalAbilityActive(Player player) {
        return AbilityManager.getInstance().getAbility(getFinalAbilityName()).getAbilityData(player).isAbilityActive();
    }

    @Override
    public boolean getAbilityActive(Player player) {
        return getAbilityData(player).isAbilityActive();
    }

    @Override
    public void fillPerSecondCooldown(Player player) {
        EnderPlayerDataSets.VoidGrabPlayerData data = getPlayerData(player);
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
    public void sendPacketToServer(Player player) {
        NetworkHandler.INSTANCE.sendToServer(new VoidGrabPacket());
    }


    @Override
    public void triggerAbility(ServerPlayer serverPlayer) {
        EnderPlayerDataSets.VoidGrabPlayerData data = getPlayerData(serverPlayer);
        EnderPlayerDataSets.EnderFormPlayerData wData = getOrCreateEnderFormPlayerData(serverPlayer);
        long currentTime = System.currentTimeMillis();
        if (data.abilityCooldown <= 0 && wData.isAbilityActive()) {
            playSound(serverPlayer);
            activateAbility(serverPlayer);
        }
    }

    @Override
    public void activateAbility(ServerPlayer serverPlayer) {
        EnderPlayerDataSets.VoidGrabPlayerData data = getPlayerData(serverPlayer);
        data.hasActivated = true;
        data.abilityCooldown = data.getMAX_COOLDOWN();
    }

    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {

    }


    @Override
    public void decrementCooldown(Player player) {
        EnderPlayerDataSets.VoidGrabPlayerData data = getPlayerData(player);
        EnderPlayerDataSets.EnderFormPlayerData wData = dataSets.getOrCreateEnderFormPlayerData(player);
        if (wData.isAbilityActive()) {
            fillPerSecondCooldown(player);
        }
    }


    @Override
    public void tickServerAbilityData(ServerPlayer player) {
        getInstance().decrementCooldown(player);
        EnderPlayerDataSets.VoidGrabPlayerData data = getPlayerData(player);
        handleNearbyZombies(player);
        syncDataToClient(player);
    }

    @Override
    public void tickClientAbilityData(Player player) {

    }

    @Override
    public void syncDataToClient(ServerPlayer player) {
        EnderPlayerDataSets.VoidGrabPlayerData data = getPlayerData(player);
        if(getFinalAbilityActive(player)) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncVoidData5Packet(data.abilityCooldown)
            );
        }
    }


    public boolean isAbilityChosenOrEquipped(Player player){
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);

        return abilityData.chosenFinal.name().equals(getName());
    }


    @Override
    public void playSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.VEX_CHARGE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void playSound2(Player player) {

    }



    private void handleNearbyZombies(ServerPlayer player) {
        EnderPlayerDataSets.VoidGrabPlayerData data = getPlayerData(player);
        EnderPlayerDataSets.EnderFormPlayerData eData = EnderPlayerDataSets.getInstance().getOrCreateEnderFormPlayerData(player);

        if (data.hasActivated && data.countTicker == 0) {
            // Initial activation: Find and add nearby entities
            double radius = 10.0;
            AABB searchArea = new AABB(player.getOnPos()).inflate(radius);
            TargetingConditions targetingConditions = TargetingConditions.forCombat();
            List<LivingEntity> nearbyEntities = player.level().getNearbyEntities(LivingEntity.class, targetingConditions, player, searchArea);

            for (LivingEntity entity : nearbyEntities) {
                data.affectedEntities.put(entity.getUUID(), new EnderPlayerDataSets.VoidGrabPlayerData.EntityState());
            }
        }

        if (data.hasActivated) {
            data.countTicker++;

            Iterator<Map.Entry<UUID, EnderPlayerDataSets.VoidGrabPlayerData.EntityState>> it = data.affectedEntities.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<UUID, EnderPlayerDataSets.VoidGrabPlayerData.EntityState> entry = it.next();
                UUID entityId = entry.getKey();
                EnderPlayerDataSets.VoidGrabPlayerData.EntityState state = entry.getValue();

                LivingEntity entity = (LivingEntity) player.serverLevel().getEntity(entityId);

                if (entity == null) {
                    it.remove();
                    continue;
                }

                if (data.countTicker <= 30) {
                    // Apply levitation and glowing effects
                    entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 2, 0, false, false));
                    entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 2, 0, false, false));
                } else if (!state.hasThrown) {
                    // Throw the entity
                    Vec3 lookVec = player.getLookAngle();
                    double throwStrength = 4.0;
                    Vec3 throwVec = lookVec.scale(throwStrength);

                    entity.setDeltaMovement(throwVec);
                    entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 40, 0, false, false));
                    state.hasThrown = true;
                }
            }

            if (data.countTicker > 30) {
                // Check if all entities have been thrown
                boolean allThrown = data.affectedEntities.values().stream().allMatch(state -> state.hasThrown);
                if (allThrown) {
                    data.hasActivated = false;
                    data.countTicker = 0;
                    data.affectedEntities.clear();
                }
            }
        }
    }

    private void unsafeTeleport(Entity entity, double x, double y, double z) {
        entity.teleportTo(x, y, z);
        if (entity instanceof ServerPlayer) {
            ((ServerPlayer)entity).connection.teleport(x, y, z, entity.getYRot(), entity.getXRot());
        }
    }
}
