package net.kenji.kenjiscombatforms.api.powers.WitherPowers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.event.CommonFunctions;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability5.SyncWitherData5Packet;
import net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability5.WitherImplodePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WitherImplode implements FinalAbility {

    private final WitherPlayerDataSets dataSets = WitherPlayerDataSets.getInstance();
    private final Map<UUID, WitherPlayerDataSets.WitherImplodePlayerData> playerDataMap = WitherPlayerDataSets.getInstance().A5playerDataMap;
    private final CommonFunctions dataHandlers = CommonFunctions.getInstance();
    private static final WitherImplode INSTANCE = new WitherImplode();


    @Override
    public String getName() {
        return AbilityManager.AbilityOption5.WITHER_IMPLODE.name();
    }

    @Override
    public String getFinalAbilityName() {
        return AbilityManager.AbilityOption3.WITHER_FINAL.name();
    }


    public WitherPlayerDataSets.WitherImplodePlayerData getPlayerData(Player player) {
       return (WitherPlayerDataSets.WitherImplodePlayerData) getAbilityData(player);
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

                WitherPlayerDataSets.WitherImplodePlayerData originalData = getInstance().getPlayerData(originalPlayer);
                WitherPlayerDataSets.WitherImplodePlayerData newData = getInstance().getPlayerData(newPlayer);

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
                WitherPlayerDataSets.WitherImplodePlayerData data = getInstance().getPlayerData(player);
            }
        }
    }

    public WitherImplode() {
        // Private constructor to prevent instantiation
    }

    public static WitherImplode getInstance() {
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
        return dataSets.getOrCreateWitherImplodePlayerData(player);
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
        WitherPlayerDataSets.WitherImplodePlayerData data = getPlayerData(player);
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
            NetworkHandler.INSTANCE.sendToServer(new WitherImplodePacket());
    }


    @Override
    public void triggerAbility(ServerPlayer serverPlayer) {
        WitherPlayerDataSets.WitherImplodePlayerData data = getPlayerData(serverPlayer);
        WitherPlayerDataSets.WitherFormPlayerData wData = getOrCreateWitherFormPlayerData(serverPlayer);
        long currentTime = System.currentTimeMillis();
        if (data.abilityCooldown <= 0 && wData.isWitherActive) {
            playSound(serverPlayer);
            activateAbility(serverPlayer);
        }
    }

    @Override
    public void activateAbility(ServerPlayer serverPlayer) {
        WitherPlayerDataSets.WitherImplodePlayerData data = getPlayerData(serverPlayer);
        data.hasActivated = true;
        data.abilityCooldown = data.getMAX_COOLDOWN();
    }

    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {

    }


    @Override
    public void decrementCooldown(Player player) {
        WitherPlayerDataSets.WitherImplodePlayerData data = getPlayerData(player);
        WitherPlayerDataSets.WitherMinionPlayerData mData = dataSets.getOrCreateMinionPlayerData(player);
        WitherPlayerDataSets.WitherFormPlayerData wData = dataSets.getOrCreateWitherFormPlayerData(player);
        if(isAbilityChosenOrEquipped(player)) {
            if (!mData.areMinionsActive && wData.isWitherActive) {
                fillPerSecondCooldown(player);
            }
        }
    }


    @Override
    public void tickServerAbilityData(ServerPlayer player) {
        getInstance().decrementCooldown(player);
        WitherPlayerDataSets.WitherImplodePlayerData data = getPlayerData(player);
        handleNearbyEntities(player);
        syncDataToClient(player);
    }

    @Override
    public void tickClientAbilityData(Player player) {
        WitherPlayerDataSets.WitherImplodePlayerData data = getPlayerData(player);
    }

    @Override
    public void syncDataToClient(ServerPlayer player) {
        WitherPlayerDataSets.WitherImplodePlayerData data = getPlayerData(player);
        if(getFinalAbilityActive(player)) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncWitherData5Packet(data.abilityCooldown)
            );
        }
    }

    public boolean isAbilityChosenOrEquipped(Player player){
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        return abilityData.chosenFinal.name().equals(getName());
    }

    private void handleNearbyEntities(Player player) {
        WitherPlayerDataSets.WitherImplodePlayerData data = getPlayerData(player);
        double radius = 10.0;
        AABB searchArea = new AABB(player.getOnPos()).inflate(radius);
        TargetingConditions targetingConditions = TargetingConditions.forCombat();
        List<LivingEntity> nearbyEntities = player.level().getNearbyEntities(LivingEntity.class, targetingConditions, player, searchArea);
        for (LivingEntity entity: nearbyEntities) {
            if(data.hasActivated) {
                data.countTicker++;
                entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 30, 0, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 30, 0, false, false));
                if(data.countTicker > 30) {
                    entity.level().explode(entity, entity.position().x, entity.position().y, entity.position().z, data.EXPLOSION_DAMAGE, Level.ExplosionInteraction.MOB).explode();
                   entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 40, 0, false, false));
                    data.hasActivated = false;
                    data.countTicker = 0;
                }
            }
        }
    }


    public void playSound(Player player){
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.VEX_CHARGE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void playSound2(Player player) {

    }
}
