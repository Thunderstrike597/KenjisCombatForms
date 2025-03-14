package net.kenji.kenjiscombatforms.api.handlers.power_data;

import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
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


    public static class TeleportPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public int abilityCooldown = MAX_COOLDOWN;
        public boolean isTpHoldActive = false;
        public Entity holdEntity;
        public int holdTickCounter = 0;
        public final int maxDist = KenjisCombatFormsCommon.TELEPORT_DIST.get();

        public TeleportPlayerData() {
            super(KenjisCombatFormsCommon.ABILITY1_COOLDOWN.get());
        }

        @Override
        public void resetAbility() {

        }

        @Override
        public boolean isAbilityActive() {
            return false;
        }
    }

    public  static class VoidRiftPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public int abilityCooldown = MAX_COOLDOWN;
        public boolean isRiftActive = false;
        public boolean hasPlayedSound = false;
        public BlockPos riftPosition = null;
        public int timerCount = 0;
        public boolean canCount = false;

        public VoidRiftPlayerData() {
            super(KenjisCombatFormsCommon.ABILITY2_COOLDOWN.get());
        }

        @Override
        public void resetAbility() {

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
        public final Map<UUID, Entity> playerEnderMap = new ConcurrentHashMap<>();
        public Entity enderEntity;

        public EnderFormPlayerData() {
            super(KenjisCombatFormsCommon.ABILITY3_COOLDOWN.get());
        }

        @Override
        public void resetAbility() {

        }

        @Override
        public boolean isAbilityActive() {
            return isEnderActive;
        }
    }

    public static class EnderLevitationPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public int abilityCooldown = MAX_COOLDOWN;
        public boolean isActive = false;
        public int countTicker = 0;
        public Map<UUID, EntityState> affectedEntities = new HashMap<>();

        public static class EntityState {
            public int levitationTicks = 0;
        }

        public EnderLevitationPlayerData() {
            super(KenjisCombatFormsCommon.ABILITY4_COOLDOWN.get());
        }

        @Override
        public void resetAbility() {

        }

        @Override
        public boolean isAbilityActive() {
            return isActive;
        }
    }

    public static class VoidGrabPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public final int EXPLOSION_DAMAGE = KenjisCombatFormsCommon.EXPLOSION_DAMAGE.get();
        public int abilityCooldown = MAX_COOLDOWN;
        public boolean hasActivated = false;
        public int countTicker = 0;
        public Map<UUID, EntityState> affectedEntities = new HashMap<>();

        public static class EntityState {
            public int ticksRemaining;
            public boolean shouldThrow;
            public boolean hasThrown = false;
        }

        public VoidGrabPlayerData() {
            super(KenjisCombatFormsCommon.ABILITY5_COOLDOWN.get());
        }

        @Override
        public void resetAbility() {

        }

        @Override
        public boolean isAbilityActive() {
            return false;
        }
    }
    public static class EnderWarpPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public int abilityCooldown = MAX_COOLDOWN;
        public final int maxDist = KenjisCombatFormsCommon.TELEPORT_DIST.get();

        public EnderWarpPlayerData() {
            super(14);
        }

        @Override
        public void resetAbility() {

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
