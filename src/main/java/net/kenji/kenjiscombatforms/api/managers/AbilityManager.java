package net.kenji.kenjiscombatforms.api.managers;

import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.minecraft.world.entity.player.Player;

import java.util.*;
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
    private final Map<String, FinalAbility> finalAbilities = new HashMap<>();


    public void registerAbility(Ability ability) {
        abilities.put(ability.getName(), ability);
    }
    public void registerFinalAbility(FinalAbility finalAbility) {
        finalAbilities.put(finalAbility.getName(), finalAbility);
    }

    public Ability getAbility(String abilityName) {
        return abilities.get(abilityName);
    }
    public FinalAbility getFinalAbility(String abilityName) {
        return finalAbilities.get(abilityName);
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
    public enum AbilityOption4 {
        NONE,
        ENDER_LEVITATION,
        WITHER_MINIONS,
    }
    public enum AbilityOption5 {
        NONE,
        VOID_GRAB,
        WITHER_IMPLODE,
    }
    public enum AltAbilityOption {
        NONE,
        ENDER_WARP,
        WITHER_DASH,
        VOID_BACKSTAB
    }



    public static class PlayerAbilityData {
        public AbilityOption1 chosenAbility1 = AbilityOption1.NONE;
        public AbilityOption2 chosenAbility2 = AbilityOption2.NONE;
        public AbilityOption3 chosenFinal = AbilityOption3.NONE;
        public AbilityOption1 ability1 = AbilityOption1.NONE;
        public AbilityOption2 ability2 = AbilityOption2.NONE;
        public AbilityOption3 ability3 = AbilityOption3.NONE;
        public AbilityOption4 ability4 = AbilityOption4.NONE;
        public AbilityOption5 ability5 = AbilityOption5.NONE;

    }


    public List<Ability> getCurrentAbilities(Player player){
        AbilityManager.PlayerAbilityData abilityData = getInstance().getPlayerAbilityData(player);
        if(!player.level().isClientSide) {
            return Arrays.asList(
                    getInstance().getAbility(abilityData.chosenAbility1.name()),
                    getInstance().getAbility(abilityData.chosenAbility2.name()),
                    getInstance().getAbility(abilityData.chosenFinal.name())
            );
        }else return  Arrays.asList(
                getInstance().getAbility(ClientFistData.getCurrentAbility1().name()),
                getInstance().getAbility(ClientFistData.getCurrentAbility2().name()),
                getInstance().getAbility(ClientFistData.getCurrentAbility3().name())
        );
    }
    public List<FinalAbility> getCurrentFinalAbilities(Player player){
        AbilityManager.PlayerAbilityData abilityData = getInstance().getPlayerAbilityData(player);
        if(!player.level().isClientSide) {
        return Arrays.asList(
                getInstance().getFinalAbility(abilityData.ability4.name()),
                getInstance().getFinalAbility(abilityData.ability5.name())
        );
        }else  return Arrays.asList(
                getInstance().getFinalAbility(ClientFistData.getCurrentAbility4().name()),
                getInstance().getFinalAbility(ClientFistData.getCurrentAbility5().name())
        );
    }



    public PlayerAbilityData getOrCreatePlayerAbilityData(Player player){
        return playerDataMap.computeIfAbsent(player.getUUID(), k -> new PlayerAbilityData());
    }
    public PlayerAbilityData getPlayerAbilityData(Player player){
        return getOrCreatePlayerAbilityData(player);
    }

}
