package net.kenji.kenjiscombatforms.api.handlers.power_data;

import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EnderPlayerDataSets {

    public Map<UUID, TeleportPlayerData> A1playerDataMap = new ConcurrentHashMap<>();
    public Map<UUID, VoidRiftPlayerData> A2playerDataMap = new ConcurrentHashMap<>();
    public Map<UUID, EnderFormPlayerData> A3playerDataMap = new ConcurrentHashMap<>();
    public Map<UUID, EnderLevitationPlayerData> A4playerDataMap = new ConcurrentHashMap<>();
    public Map<UUID, VoidGrabPlayerData> A5playerDataMap = new ConcurrentHashMap<>();
    public Map<UUID, EnderWarpPlayerData> A7playerDataMap = new ConcurrentHashMap<>();

    private static final EnderPlayerDataSets INSTANCE = new EnderPlayerDataSets();

    public static EnderPlayerDataSets getInstance() {
        return INSTANCE;
    }

    private static final long PRESS_COOLDOWN = 4;


    public static class TeleportPlayerData extends AbstractAbilityData {
        public final int INITIAL_PRESS_COUNTER = 90;
        public int tickCount = 0;
        public int abilityCooldown = MAX_COOLDOWN;
        public int clientAbilityCooldown = MAX_COOLDOWN;
        public boolean isTpHoldActive = false;
        public Entity holdEntity;
        public int holdTickCounter = 0;
        public final int maxDist = EpicFightCombatFormsCommon.TELEPORT_DIST.get();
        public long pressCounter = INITIAL_PRESS_COUNTER;
        public long tpPressCounter = INITIAL_PRESS_COUNTER;


        public TeleportPlayerData() {
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
        public boolean isAbilityActive() {
            return false;
        }


    }

    public  static class VoidRiftPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public int abilityCooldown = MAX_COOLDOWN;
        public int clientAbilityCooldown = MAX_COOLDOWN;
        public boolean isRiftActive = false;
        public boolean hasPlayedSound = false;
        public BlockPos riftPosition = null;
        public int timerCount = 0;
        public boolean canCount = false;

        public VoidRiftPlayerData() {
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
        public void setAbilityActive(boolean value) {
            isRiftActive = value;
        }

        @Override
        public boolean isAbilityActive() {
            return isRiftActive;
        }


    }
    public static class EnderFormPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public boolean isEnderActive = false;
        public boolean hasPlayedSound = true;
        public int abilityCooldown = MAX_COOLDOWN;
        public int clientAbilityCooldown = MAX_COOLDOWN;
        public final Map<UUID, Entity> playerEnderMap = new ConcurrentHashMap<>();
        public Entity enderEntity;

        public EnderFormPlayerData() {
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
        public void setAbilityActive(boolean value) {
            isEnderActive = value;
        }

        @Override
        public boolean isAbilityActive() {
            return isEnderActive;
        }


    }

    public static class EnderLevitationPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public int abilityCooldown = MAX_COOLDOWN;
        public int clientAbilityCooldown = MAX_COOLDOWN;
        public boolean isActive = false;
        public int countTicker = 0;
        public Map<UUID, EntityState> affectedEntities = new HashMap<>();

        public static class EntityState {
            public int levitationTicks = 0;
        }

        public EnderLevitationPlayerData() {
            super(EpicFightCombatFormsCommon.ABILITY4_COOLDOWN.get());
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
            isActive = value;
        }

        @Override
        public boolean isAbilityActive() {
            return isActive;
        }


    }

    public static class VoidGrabPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public final int EXPLOSION_DAMAGE = EpicFightCombatFormsCommon.EXPLOSION_DAMAGE.get();
        public int abilityCooldown = MAX_COOLDOWN;
        public int clientAbilityCooldown = MAX_COOLDOWN;
        public boolean hasActivated = false;
        public int countTicker = 0;
        public Map<UUID, EntityState> affectedEntities = new HashMap<>();

        public static class EntityState {
            public int ticksRemaining;
            public boolean shouldThrow;
            public boolean hasThrown = false;
        }

        public VoidGrabPlayerData() {
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
    public static class EnderWarpPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public int abilityCooldown = MAX_COOLDOWN;
        public int clientAbilityCooldown = MAX_COOLDOWN;
        public final int maxDist = EpicFightCombatFormsCommon.TELEPORT_DIST.get();

        public EnderWarpPlayerData() {
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
            return false;
        }


    }



    public TeleportPlayerData getOrCreateTeleportPlayerData(Player player) {
        return getInstance().A1playerDataMap.computeIfAbsent(player.getUUID(), k -> new TeleportPlayerData());
    }
    public VoidRiftPlayerData getOrCreateVoidRiftPlayerData(Player player) {
        return getInstance().A2playerDataMap.computeIfAbsent(player.getUUID(), k -> new VoidRiftPlayerData());
    }
    public EnderFormPlayerData getOrCreateEnderFormPlayerData(Player player) {
        return getInstance().A3playerDataMap.computeIfAbsent(player.getUUID(), k -> new EnderFormPlayerData());
    }
    public EnderLevitationPlayerData getOrCreateEnderLevitationPlayerData(Player player) {
        return getInstance().A4playerDataMap.computeIfAbsent(player.getUUID(), k -> new EnderLevitationPlayerData());
    }
    public VoidGrabPlayerData getOrCreateVoidGrabPlayerData(Player player) {
        return getInstance().A5playerDataMap.computeIfAbsent(player.getUUID(), k -> new VoidGrabPlayerData());
    }
    public EnderWarpPlayerData getOrCreateEnderWarpPlayerData(Player player) {
        return getInstance().A7playerDataMap.computeIfAbsent(player.getUUID(), k -> new EnderWarpPlayerData());
    }
}
