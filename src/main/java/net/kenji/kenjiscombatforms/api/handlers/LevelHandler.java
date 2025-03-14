package net.kenji.kenjiscombatforms.api.handlers;

import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LevelHandler {
    public final Map<UUID, FormLevelManager.PlayerFormLevelData> playerDataMap = new ConcurrentHashMap<>();
    private final FormLevelManager levelManager = FormLevelManager.getInstance();

   private static final LevelHandler INSTANCE = new LevelHandler();
   public static LevelHandler getInstance(){
       return INSTANCE;
   }




    public FormLevelManager.PlayerFormLevelData getOrCreatePlayerLevelData(Player player){
        return playerDataMap.computeIfAbsent(player.getUUID(), k -> new FormLevelManager.PlayerFormLevelData());
    }
}
