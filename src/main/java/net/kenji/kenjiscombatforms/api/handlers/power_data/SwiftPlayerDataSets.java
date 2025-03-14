package net.kenji.kenjiscombatforms.api.handlers.power_data;

import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SwiftPlayerDataSets {

    public Map<UUID, SpeedPlayerData> A1playerDataMap = new ConcurrentHashMap<>();
    public Map<UUID, SwiftInflictPlayerData> A2playerDataMap = new ConcurrentHashMap<>();


    private static final SwiftPlayerDataSets INSTANCE = new SwiftPlayerDataSets();

    public static SwiftPlayerDataSets getInstance() {
        return INSTANCE;
    }


    public static class SpeedPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public int abilityCooldown = MAX_COOLDOWN;

        public SpeedPlayerData() {
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

    public static class SwiftInflictPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public boolean isInflictActive = false;
        public int abilityCooldown = MAX_COOLDOWN;

        public SwiftInflictPlayerData() {
            super(KenjisCombatFormsCommon.ABILITY2_COOLDOWN.get());
        }

        @Override
        public void resetAbility() {

        }

        @Override
        public boolean isAbilityActive() {
            return isInflictActive;
        }
    }

    public SpeedPlayerData getOrCreateSpeedPlayerData(Player player) {
        return getInstance().A1playerDataMap.computeIfAbsent(player.getUUID(), k -> new SpeedPlayerData());
    }
    public SwiftInflictPlayerData getOrCreateSwiftInflictPlayerData(Player player) {
        return getInstance().A2playerDataMap.computeIfAbsent(player.getUUID(), k -> new SwiftInflictPlayerData());
    }
}
