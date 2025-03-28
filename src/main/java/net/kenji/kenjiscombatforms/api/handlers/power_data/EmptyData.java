package net.kenji.kenjiscombatforms.api.handlers.power_data;

import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EmptyData {
    public Map<UUID, EmptyPlayerData> A1playerDataMap = new ConcurrentHashMap<>();

    private static EmptyData INSTANCE = new EmptyData();

    public static EmptyData getInstance() {
        return INSTANCE;
    }

    public static class EmptyPlayerData extends AbstractAbilityData {
    public EmptyPlayerData() {
        super(0);
    }

    @Override
    public void resetAbility() {

    }

    @Override
    public boolean isAbilityActive() {
        return false;
    }
}

    public EmptyPlayerData getOrCreateEmptyPlayerData(Player player) {
        return A1playerDataMap.computeIfAbsent(player.getUUID(), k -> new EmptyPlayerData());
    }
}
