package net.kenji.kenjiscombatforms.api.handlers.power_data;

import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WitherPlayerDataSets {

    public Map<UUID, WitherDashPlayerData> A1playerDataMap = new ConcurrentHashMap<>();
    public Map<UUID, SoulDriftPlayerData> A2playerDataMap = new ConcurrentHashMap<>();
    public Map<UUID, WitherFormPlayerData> A3playerDataMap = new ConcurrentHashMap<>();

    public Map<UUID, WitherMinionPlayerData> A4playerDataMap = new ConcurrentHashMap<>();
    public Map<UUID, WitherImplodePlayerData> A5playerDataMap = new ConcurrentHashMap<>();
    public Map<UUID, WitherFormDashPlayerData> A6playerDataMap = new ConcurrentHashMap<>();


    private static final WitherPlayerDataSets INSTANCE = new WitherPlayerDataSets();


    public static WitherPlayerDataSets getInstance() {
        return INSTANCE;
    }

    public static class WitherDashPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public int abilityCooldown = MAX_COOLDOWN;
        public int clientAbilityCooldown = MAX_COOLDOWN;
        public final double MAX_SPEED = 3.0; // Blocks per tick
        public final double MAX_DISTANCE = 12.00;
        public final int MAX_DASH_TICKS = 12;
        public final double DECELERATION_DISTANCE = 5.0;
        public boolean isPauseActive = false;
        public boolean isDashActive = false;
        public boolean canIgnoreCollide = false;
        public final int MAX_BLOCKS = 5;
        public double distanceTraveled = 0.0;
        public Vec3 initialPosition = null;
        public  int dashTicksRemaining = 0;
        public Vec3 dashDirection = null;
        public double currentSpeed = 0.0;


        public WitherDashPlayerData() {
            super(EpicFightCombatFormsCommon.ABILITY1_COOLDOWN.get());
        }

        @Override
        public void resetAbility() {

        }
        @Override
        public void setClientCooldown(Player player, int value) {
            clientAbilityCooldown = value;
        }

        @Override
        public int getClientAbilityCooldown() {
            return clientAbilityCooldown;
        }

        @Override
        public void setAbilityAltActive(boolean value) {
            canIgnoreCollide = value;
        }

        @Override
        public boolean getAbilityAltActive() {
            return canIgnoreCollide;
        }

        @Override
        public void setAbilityActive(boolean value) {
            isDashActive = value;
        }

        @Override
        public boolean isAbilityActive() {
            return isDashActive;
        }
    }
    public static class SoulDriftPlayerData extends AbstractAbilityData{
        public int tickCount = 0;
        public int abilityCooldown = MAX_COOLDOWN;
        public int clientAbilityCooldown = MAX_COOLDOWN;
        public boolean isSoulDriftActive = false;
        public boolean hasPlayedSound = true;

        public SoulDriftPlayerData() {
            super(EpicFightCombatFormsCommon.ABILITY2_COOLDOWN.get());
        }

        @Override
        public void resetAbility() {

        }

        @Override
        public void setClientCooldown(Player player, int value) {
            clientAbilityCooldown = value;
        }

        @Override
        public int getClientAbilityCooldown() {
            return clientAbilityCooldown;
        }


        @Override
        public boolean isAbilityActive() {
            return isSoulDriftActive;
        }

        @Override
        public void setAbilityActive(boolean value) {
            isSoulDriftActive = value;
        }
    }

    public static class WitherFormPlayerData extends AbstractAbilityData{
        public int tickCount = 0;
        public boolean isWitherActive = false;
        public boolean isDelayPause = false;
        public boolean hasPlayedSound = true;
        public boolean isDashActive = false;
        public int abilityCooldown = MAX_COOLDOWN;
        public int clientAbilityCooldown = MAX_COOLDOWN;
        public final Map<UUID, Entity> playerWitherMap = new ConcurrentHashMap<>();
        public Entity witherEntity;

        public WitherFormPlayerData() {
            super(EpicFightCombatFormsCommon.ABILITY3_COOLDOWN.get());
        }

        @Override
        public void resetAbility() {

        }
        @Override
        public void setClientCooldown(Player player, int value) {
            clientAbilityCooldown = value;
        }

        @Override
        public int getClientAbilityCooldown() {
            return clientAbilityCooldown;
        }


        @Override
        public void setAbilityAltActive(boolean value) {
            isDashActive = value;
        }

        @Override
        public boolean getAbilityAltActive() {
            return isDashActive;
        }

        @Override
        public void setAbilityActive(boolean value) {
            isWitherActive = value;
        }

        @Override
        public boolean isAbilityActive() {
            return isWitherActive;
        }
    }
    public static class WitherMinionPlayerData extends AbstractAbilityData{
        public int tickCount = 0;
        public final int MINION_COUNT = EpicFightCombatFormsCommon.MINION_COUNT.get();
        public boolean areMinionsActive = false;
        public UUID playerUUID;
        public int abilityCooldown = MAX_COOLDOWN;
        public int clientAbilityCooldown = MAX_COOLDOWN;
        public boolean hasPlayedSound = false;
        public int timerCount = 0;
        public boolean canCount = false;
        public Entity minionEntity;
        public Entity minionEntity2;
        public Entity minionEntity3;
        public Entity minionEntity4;
        public final Map<UUID, Entity> playerMinionMap = new ConcurrentHashMap<>();
        public final Map<UUID, Entity> playerMinion2Map = new ConcurrentHashMap<>();
        public final Map<UUID, Entity> playerMinion3Map = new ConcurrentHashMap<>();
        public final Map<UUID, Entity> playerMinion4Map = new ConcurrentHashMap<>();

        public WitherMinionPlayerData() {
            super(EpicFightCombatFormsCommon.ABILITY4_COOLDOWN.get() * 6);
        }

        @Override
        public void resetAbility() {

        }

        @Override
        public void setClientCooldown(Player player, int value) {
            clientAbilityCooldown = value;
        }

        @Override
        public int getClientAbilityCooldown() {
            return clientAbilityCooldown;
        }


        @Override
        public void setAbilityActive(boolean value) {
            areMinionsActive = value;
        }


        @Override
        public boolean isAbilityActive() {
            return areMinionsActive;
        }

    }

    public static class WitherImplodePlayerData extends AbstractAbilityData{
        public int tickCount = 0;
        public final int EXPLOSION_DAMAGE = EpicFightCombatFormsCommon.EXPLOSION_DAMAGE.get();
        public int abilityCooldown = MAX_COOLDOWN;
        public int clientAbilityCooldown = MAX_COOLDOWN;
        public boolean hasActivated = false;
        public int countTicker = 0;

        public WitherImplodePlayerData() {
            super(EpicFightCombatFormsCommon.ABILITY5_COOLDOWN.get());
        }

        @Override
        public void resetAbility() {

        }

        @Override
        public void setClientCooldown(Player player, int value) {
            clientAbilityCooldown = value;
        }

        @Override
        public int getClientAbilityCooldown() {
            return clientAbilityCooldown;
        }
        @Override
        public boolean isAbilityActive() {
            return false;
        }

    }
    public static class WitherFormDashPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public int abilityCooldown = MAX_COOLDOWN;
        public int clientAbilityCooldown = MAX_COOLDOWN;
        public final double MAX_SPEED = 3.0; // Blocks per tick
        public final double MAX_DISTANCE = 12.00;
        public final int MAX_DASH_TICKS = 12;
        public final double DECELERATION_DISTANCE = 5.0;
        public boolean isDashActive = false;
        public double distanceTraveled = 0.0;
        public Vec3 initialPosition = null;
        public  int dashTicksRemaining = 0;
        public Vec3 dashDirection = null;
        public double currentSpeed = 0.0;

        public WitherFormDashPlayerData() {
            super(14);
        }

        @Override
        public void resetAbility() {

        }
        @Override
        public void setClientCooldown(Player player, int value) {
            clientAbilityCooldown = value;
        }

        @Override
        public int getClientAbilityCooldown() {
            return clientAbilityCooldown;
        }

        @Override
        public boolean isAbilityActive() {
            return isDashActive;
        }

        @Override
        public void setAbilityActive(boolean value) {
            isDashActive = value;
        }
    }

    public WitherDashPlayerData getOrCreateDashPlayerData(Player player){
       return getInstance().A1playerDataMap.computeIfAbsent(player.getUUID(), k -> new WitherDashPlayerData());
    }
    public SoulDriftPlayerData getOrCreateSoulDriftPlayerData(Player player){
        return getInstance().A2playerDataMap.computeIfAbsent(player.getUUID(), k -> new SoulDriftPlayerData());
    }
    public WitherFormPlayerData getOrCreateWitherFormPlayerData(Player player){
        return getInstance().A3playerDataMap.computeIfAbsent(player.getUUID(), k -> new WitherFormPlayerData());
    }
    public WitherMinionPlayerData getOrCreateMinionPlayerData(Player player){
        return getInstance().A4playerDataMap.computeIfAbsent(player.getUUID(), k -> new WitherMinionPlayerData());
    }
    public WitherImplodePlayerData getOrCreateWitherImplodePlayerData(Player player){
        return getInstance().A5playerDataMap.computeIfAbsent(player.getUUID(), k -> new WitherImplodePlayerData());
    }
    public WitherFormDashPlayerData getOrCreateWitherFormDashPlayerData(Player player){
        return getInstance().A6playerDataMap.computeIfAbsent(player.getUUID(), k -> new WitherFormDashPlayerData());
    }
}
