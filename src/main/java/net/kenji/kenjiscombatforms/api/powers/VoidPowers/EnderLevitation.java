package net.kenji.kenjiscombatforms.api.powers.VoidPowers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.event.CommonFunctions;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.globalformpackets.SyncAbility4Packet;
import net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability4.EnderLevitationPacket;
import net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability4.SyncVoidData4Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EnderLevitation implements FinalAbility {
    EnderPlayerDataSets dataSets = EnderPlayerDataSets.getInstance();
    private final Map<UUID, EnderPlayerDataSets.EnderLevitationPlayerData> playerDataMap = dataSets.A4playerDataMap;

    private final CommonFunctions dataHandlers = CommonFunctions.getInstance();
    private static final EnderLevitation INSTANCE = new EnderLevitation();




    @Override
    public String getName() {
        return "ENDER_LEVITATION";
    }

    @Override
    public String getFinalAbilityName() {
        return "VOID_FINAL";
    }


    private EnderPlayerDataSets.EnderLevitationPlayerData getPlayerData(Player player) {
       return (EnderPlayerDataSets.EnderLevitationPlayerData) getAbilityData(player);
    }

    private EnderPlayerDataSets.EnderFormPlayerData getOrCreateEnderFormPlayerData(Player player) {
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
                getInstance().setAbilityCooldown(serverPlayer, getInstance().getPlayerData(serverPlayer).getMAX_COOLDOWN());
                getInstance().syncDataToClient(serverPlayer);
            }
        }
        @SubscribeEvent
        public static void onPlayerDeath(LivingDeathEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {

            }
        }


        @SubscribeEvent
        public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer){
            }
            getInstance().playerDataMap.remove(event.getEntity().getUUID());
        }

        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            if (event.getOriginal() instanceof ServerPlayer originalPlayer &&
                    event.getEntity() instanceof ServerPlayer newPlayer) {

                EnderPlayerDataSets.EnderLevitationPlayerData originalData = getInstance().getPlayerData(originalPlayer);
                EnderPlayerDataSets.EnderLevitationPlayerData newData = getInstance().getPlayerData(newPlayer);

                getInstance().syncDataToClient(newPlayer);
            }
        }
    }



    public EnderLevitation() {
        // Private constructor to prevent instantiation
    }

    public static EnderLevitation getInstance() {
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
        return dataSets.getOrCreateEnderLevitationPlayerData(player);
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
        EnderPlayerDataSets.EnderLevitationPlayerData data = getPlayerData(player);
        int cooldown = data.abilityCooldown;
        if (cooldown > 0){

            data.tickCount = dataHandlers.getTickCount(data.tickCount);
            data.abilityCooldown = dataHandlers.decreaseCooldown(data.abilityCooldown, data.tickCount);
        }
    }

    @Override
    public void drainPerSecondCooldown(Player player) {
        EnderPlayerDataSets.EnderLevitationPlayerData data = getPlayerData(player);
        data.tickCount = dataHandlers.getTickCount(data.tickCount);
        data.abilityCooldown = dataHandlers.increaseCooldown(data.abilityCooldown, data.tickCount);
    }

    @Override
    public void sendPacketToServer(Player player) {
        NetworkHandler.INSTANCE.sendToServer(new EnderLevitationPacket());
    }


    @Override
    public void triggerAbility(ServerPlayer serverPlayer) {
        EnderPlayerDataSets.EnderLevitationPlayerData data = getPlayerData(serverPlayer);
        EnderPlayerDataSets.EnderFormPlayerData bData = dataSets.getOrCreateEnderFormPlayerData(serverPlayer);
        long currentTime = System.currentTimeMillis();
        if (data.abilityCooldown <= 0) {
            activateAbility(serverPlayer);
        }else {
            deactivateAbilityOptional(serverPlayer);
        }
    }

    @Override
    public void activateAbility(ServerPlayer serverPlayer) {
        EnderPlayerDataSets.EnderLevitationPlayerData data = getPlayerData(serverPlayer);
        data.isActive = true;
        playSound(serverPlayer);
    }

    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {
        EnderPlayerDataSets.EnderLevitationPlayerData data = getPlayerData(serverPlayer);
        data.isActive = false;
    }

    @Override
    public void decrementCooldown(Player player) {
        EnderPlayerDataSets.EnderLevitationPlayerData data = getPlayerData(player);
        EnderPlayerDataSets.EnderFormPlayerData bData = dataSets.getOrCreateEnderFormPlayerData(player);
            if (player instanceof ServerPlayer serverPlayer) {
                if (!data.isActive && getFinalAbilityActive(serverPlayer)) {
                    fillPerSecondCooldown(player);
                    fillPerSecondCooldown(player);
                }
              else if (data.isActive) {
                    drainPerSecondCooldown(player);
                }
                else if (data.abilityCooldown >= data.getMAX_COOLDOWN() && data.isActive) {
                    deactivateAbilityOptional(serverPlayer);
                }
            }
    }

    @Override
    public void tickServerAbilityData(ServerPlayer player) {
        getInstance().decrementCooldown(player);
        syncDataToClient(player);
       handlerNearbyEntities(player);
    }

    @Override
    public void tickClientAbilityData(Player player) {

    }

    @Override
    public void syncDataToClient(ServerPlayer player) {
        EnderPlayerDataSets.EnderLevitationPlayerData data = getPlayerData(player);
        if(getFinalAbilityActive(player)) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncAbility4Packet(data.abilityCooldown, false)
            );
        }
    }



    private void handlerNearbyEntities(ServerPlayer player) {
        EnderPlayerDataSets.EnderLevitationPlayerData data = getPlayerData(player);
        EnderPlayerDataSets.EnderFormPlayerData eData = getInstance().getOrCreateEnderFormPlayerData(player);

        if (getFinalAbilityActive(player)) {
            double radius = 10.0;
            AABB searchArea = new AABB(player.getOnPos()).inflate(radius);

            if (data.isActive) {

                // Process existing affected entities
                Iterator<Map.Entry<UUID, EnderPlayerDataSets.EnderLevitationPlayerData.EntityState>> it = data.affectedEntities.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<UUID, EnderPlayerDataSets.EnderLevitationPlayerData.EntityState> entry = it.next();
                    UUID entityId = entry.getKey();
                    EnderPlayerDataSets.EnderLevitationPlayerData.EntityState state = entry.getValue();

                    LivingEntity entity = (LivingEntity) player.serverLevel().getEntity(entityId);
                    if (entity == null || !searchArea.contains(entity.position())) {
                        // Entity is null or out of range, remove gravity effect
                        if (entity != null) {
                            entity.setNoGravity(false);
                        }
                        it.remove();
                        continue;
                    }

                    if (state.levitationTicks < 30) {
                        entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 2, 0, false, false));
                        state.levitationTicks++;
                    } else {
                        entity.setNoGravity(true);
                    }
                }

                // Add new entities to the affected list
                TargetingConditions targetingConditions = TargetingConditions.forCombat();
                List<LivingEntity> nearbyEntities = player.level().getNearbyEntities(LivingEntity.class, targetingConditions, player, searchArea);
                for (LivingEntity entity : nearbyEntities) {
                    if (!data.affectedEntities.containsKey(entity.getUUID())) {
                        data.affectedEntities.put(entity.getUUID(), new EnderPlayerDataSets.EnderLevitationPlayerData.EntityState());
                    }
                }
            } else {
                // Ability is not active, remove all effects
                for (UUID entityId : data.affectedEntities.keySet()) {
                    LivingEntity entity = (LivingEntity) player.serverLevel().getEntity(entityId);
                    if (entity != null) {
                        entity.setNoGravity(false);
                    }
                }
                data.affectedEntities.clear();
            }
        }
    }


    @Override
    public void playSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void playSound2(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}
