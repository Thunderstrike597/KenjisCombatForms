package net.kenji.kenjiscombatforms.api.powers.power_powers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.ClientEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.power_data.PowerPlayerDataSets;
import net.kenji.kenjiscombatforms.api.handlers.power_data.SwiftPlayerDataSets;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbilityDamageGainStrategy;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.event.CommonFunctions;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.power_form.ClientPowerData;
import net.kenji.kenjiscombatforms.network.power_form.ability2.PowerEffectInflictPacket;
import net.kenji.kenjiscombatforms.network.power_form.ability2.SyncPowerData2Packet;
import net.kenji.kenjiscombatforms.network.swift_form.ability2.SyncSwiftData2Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.network.PacketDistributor;

import java.util.Map;
import java.util.UUID;

public class PowerEffectInflict implements Ability {

    private final PowerPlayerDataSets dataSets = PowerPlayerDataSets.getInstance();
    private final Map<UUID, PowerPlayerDataSets.PowerInflictPlayerData> playerDataMap = dataSets.A2playerDataMap;
    private static final PowerEffectInflict INSTANCE = new PowerEffectInflict();
    private final CommonFunctions dataHandlers = CommonFunctions.getInstance();


    @Override
    public String getName() {
        return AbilityManager.AbilityOption2.POWER_ABILITY2.name();
    }


    PowerPlayerDataSets.PowerInflictPlayerData getPlayerData(Player player){
        return (PowerPlayerDataSets.PowerInflictPlayerData) getAbilityData(player);
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

                AbstractAbilityData originalData = getInstance().getPlayerData(originalPlayer);
                AbstractAbilityData newData = getInstance().getPlayerData(newPlayer);


                getInstance().syncDataToClient(newPlayer);
            }
        }


        @SubscribeEvent
        public static void onLivingEntityHurt(LivingHurtEvent event){
            LivingEntity entity = event.getEntity();
            if(event.getSource().getEntity() instanceof Player player){
                PowerPlayerDataSets.PowerInflictPlayerData data = getInstance().getPlayerData(player);

                if(data.isInflictActive){
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1, true, true));
                }
            }
        }


    }

    public PowerEffectInflict() {
        // Private constructor to prevent instantiation
    }

    public static PowerEffectInflict getInstance() {
        return INSTANCE;
    }


    public float getAbilityCooldown(Player player) {
        PowerPlayerDataSets.PowerInflictPlayerData data = getPlayerData(player);
        return data.abilityCooldown;
    }
    public void setAbilityCooldown(Player player) {
        PowerPlayerDataSets.PowerInflictPlayerData data = getPlayerData(player);
       data.abilityCooldown = data.getMAX_COOLDOWN();
    }


    @Override
    public AbstractAbilityData getAbilityData(Player player) {
        return dataSets.getOrCreatePowerInflictPlayerData(player);
    }

    @Override
    public void fillPerSecondCooldown(Player player) {
        PowerPlayerDataSets.PowerInflictPlayerData data = getPlayerData(player);
        int cooldown = data.abilityCooldown;
        if (cooldown > 0){
            data.tickCount = dataHandlers.getTickCount(data.tickCount);
            data.abilityCooldown = dataHandlers.decreaseCooldown(data.abilityCooldown, data.tickCount);
        }
    }

    @Override
    public void drainPerSecondCooldown(Player player) {
        PowerPlayerDataSets.PowerInflictPlayerData data = getPlayerData(player);
        data.tickCount = dataHandlers.getTickCount(data.tickCount);
        data.abilityCooldown = dataHandlers.increaseCooldown(data.abilityCooldown, data.tickCount);
    }

    @Override
    public boolean getAbilityActive(Player player) {
        return getAbilityData(player).isAbilityActive();
    }

    @Override
    public void sendPacketToServer(Player player) {
        if(!ClientEventHandler.getInstance().getAreFinalsActive()){
            NetworkHandler.INSTANCE.sendToServer(new PowerEffectInflictPacket());
        }
    }


    @Override
    public void triggerAbility(ServerPlayer serverPlayer) {
        long currentTime = System.currentTimeMillis();
        PowerPlayerDataSets.PowerInflictPlayerData data = getPlayerData(serverPlayer);
        if(data.abilityCooldown <= 0 && !data.isInflictActive) {
            activateAbility(serverPlayer);
            playSound(serverPlayer);
        } else if(data.isInflictActive){
           deactivateAbilityOptional(serverPlayer);
        }
    }


    @Override
    public void activateAbility(ServerPlayer player) {
        PowerPlayerDataSets.PowerInflictPlayerData data = getPlayerData(player);
        data.isInflictActive = true;
    }

    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {
        PowerPlayerDataSets.PowerInflictPlayerData data = getPlayerData(serverPlayer);
        if(data.isInflictActive){
            data.isInflictActive = false;
        }
    }

    public static class CurrentDamageGainStrategy implements AbilityDamageGainStrategy {


        @Override
        public void fillDamageCooldown(Player player) {
            PowerPlayerDataSets.PowerInflictPlayerData data =getInstance().getPlayerData(player);
            if (KenjisCombatFormsCommon.ABILITY2_COMBAT_MODE.get()) {
                if(!data.isAbilityActive()) {
                    if (data.abilityCooldown > 0) {
                        data.abilityCooldown = data.abilityCooldown - KenjisCombatFormsCommon.COMBAT_MODE_GAIN_AMOUNT.get();
                    }
                    if (player instanceof ServerPlayer serverPlayer) {
                        getInstance().syncDataToClient(serverPlayer);
                    }
                }
            }
        }
    }


    public void decrementCooldown(Player player) {
        PowerPlayerDataSets.PowerInflictPlayerData data = playerDataMap.computeIfAbsent(player.getUUID(), k -> new PowerPlayerDataSets.PowerInflictPlayerData());
        System.out.println("Cooldown: " + data.abilityCooldown);

        if (player instanceof ServerPlayer serverPlayer) {
            if (EffectiveSide.get() == LogicalSide.SERVER) {
                if (!KenjisCombatFormsCommon.ABILITY2_COMBAT_MODE.get()) {

                    if (!data.isInflictActive) {
                        fillPerSecondCooldown(player);
                    }
                }
                if (data.abilityCooldown < data.getMAX_COOLDOWN() && data.isInflictActive) {
                    drainPerSecondCooldown(player);
                }
                if (data.abilityCooldown >= data.getMAX_COOLDOWN() && data.isInflictActive) {

                    deactivateAbilityOptional(serverPlayer);
                }
            }
        }
    }

    @Override
    public void tickServerAbilityData(ServerPlayer player) {
        PowerPlayerDataSets.PowerInflictPlayerData data = getPlayerData(player);

        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);

        if(abilityData.chosenAbility2.name().equals(getName())) {
            getInstance().decrementCooldown(player);
            syncDataToClient(player);
        }
    }

    @Override
    public void tickClientAbilityData(Player player) {

    }

    @Override
    public void syncDataToClient(ServerPlayer player) {
        PowerPlayerDataSets.PowerInflictPlayerData data = getPlayerData(player);
        if(isAbilityChosenOrEquipped(player)) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncPowerData2Packet(data.abilityCooldown)
            );
        }
    }

    public boolean isAbilityChosenOrEquipped(Player player){
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        return abilityData.chosenAbility2.name().equals(getName());
    }

    @Override
    public void playSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void playSound2(Player player) {

    }
}
