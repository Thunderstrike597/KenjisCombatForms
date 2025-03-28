package net.kenji.kenjiscombatforms.api.handlers.power_data;

import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.kenji.kenjiscombatforms.network.power_form.ClientPowerData;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PowerPlayerDataSets {

    public Map<UUID, StrengthPlayerData> A1playerDataMap = new ConcurrentHashMap<>();
    public Map<UUID, PowerInflictPlayerData> A2playerDataMap = new ConcurrentHashMap<>();


    private static final PowerPlayerDataSets INSTANCE = new PowerPlayerDataSets();

    public static PowerPlayerDataSets getInstance() {
        return INSTANCE;
    }


    public static class StrengthPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public int abilityCooldown = MAX_COOLDOWN;
        public int clientAbilityCooldown = MAX_COOLDOWN;

        public StrengthPlayerData() {
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
    public static class PowerInflictPlayerData extends AbstractAbilityData {
        public int tickCount = 0;
        public boolean isInflictActive = false;
        public int abilityCooldown = MAX_COOLDOWN;
        public int clientAbilityCooldown = MAX_COOLDOWN;

        public PowerInflictPlayerData() {
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
            isInflictActive = value;
        }

        @Override
        public boolean isAbilityActive() {
            return isInflictActive;
        }
    }

    public StrengthPlayerData getOrCreateStrengthPlayerData(Player player) {
        return getInstance().A1playerDataMap.computeIfAbsent(player.getUUID(), k -> new StrengthPlayerData());
    }
    public PowerInflictPlayerData getOrCreatePowerInflictPlayerData(Player player) {
        return getInstance().A2playerDataMap.computeIfAbsent(player.getUUID(), k -> new PowerInflictPlayerData());
    }
}
