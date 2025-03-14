package net.kenji.kenjiscombatforms.api.managers;

import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AbilityManager {

    public final Map<UUID, PlayerAbilityData> playerDataMap = new ConcurrentHashMap<>();

    public void updatePlayerData(UUID playerUUID, AbilityManager.PlayerAbilityData data) {
        playerDataMap.put(playerUUID, data);
    }

    private static final AbilityManager INSTANCE = new AbilityManager();
    public static AbilityManager getInstance(){
        return INSTANCE;
    }


    private final Map<String, Ability> abilities = new HashMap<>();



    public void registerAbility(Ability ability) {
        abilities.put(ability.getName(), ability);
    }

    public Ability getAbility(String abilityName) {
        return abilities.get(abilityName);
    }


    public enum AbilityOption1 {
        NONE,
        VOID_ABILITY1,
        WITHER_ABILITY1,
        SWIFT_ABILITY1,
        POWER_ABILITY1
    }
    public enum AbilityOption2 {
        NONE,
        VOID_ABILITY2,
        WITHER_ABILITY2,
        SWIFT_ABILITY2,
        POWER_ABILITY2
    }
    public enum AbilityOption3 {
        NONE,
        VOID_FINAL,
        WITHER_FINAL
    }



    public static class PlayerAbilityData {
        public AbilityOption1 chosenAbility1 = AbilityOption1.NONE;
        public AbilityOption2 chosenAbility2 = AbilityOption2.NONE;
        public AbilityOption3 chosenFinal = AbilityOption3.NONE;
        public AbilityOption1 ability1 = AbilityOption1.NONE;
        public AbilityOption2 ability2 = AbilityOption2.NONE;
        public AbilityOption3 ability3 = AbilityOption3.NONE;
    }




    public PlayerAbilityData getOrCreatePlayerAbilityData(Player player){
        return playerDataMap.computeIfAbsent(player.getUUID(), k -> new PlayerAbilityData());
    }
    public PlayerAbilityData getPlayerAbilityData(Player player){
        return getOrCreatePlayerAbilityData(player);
    }

}
