package net.kenji.kenjiscombatforms.api.managers;

import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FormLevelManager {

    public final Map<UUID, PlayerFormLevelData> playerDataMap = new ConcurrentHashMap<>();

    private static final FormLevelManager INSTANCE = new FormLevelManager();
    public static FormLevelManager getInstance(){
        return INSTANCE;
    }

    public void updatePlayerData(UUID playerUUID, PlayerFormLevelData data) {
        playerDataMap.put(playerUUID, data);
    }



    public static class PlayerFormLevelData {
        public int voidFormMAX =  EpicFightCombatFormsCommon.MAX_FORM_STARTING_XP.get();;
        public int witherFormMAX =  EpicFightCombatFormsCommon.MAX_FORM_STARTING_XP.get();;
    }

    public enum FormLevel {
        LEVEL1, LEVEL2, LEVEL3
    }


    public PlayerFormLevelData getOrCreatePlayerLevelData(Player player){
        return playerDataMap.computeIfAbsent(player.getUUID(), k -> new PlayerFormLevelData());
    }
}
