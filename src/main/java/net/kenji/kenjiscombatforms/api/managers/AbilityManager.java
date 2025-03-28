package net.kenji.kenjiscombatforms.api.managers;

import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.api.powers.EmptyAbility;
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
        return abilityName != null && !abilityName.isEmpty() ? abilities.get(abilityName) :abilities.get("NONE");
    }
    public FinalAbility getFinalAbility(String abilityName) {
        return finalAbilities.get(abilityName);
    }



    public static class PlayerAbilityData {
        public String chosenAbility1 = "NONE";
        public String chosenAbility2 = "NONE";
        public String chosenFinal = "NONE";
        public String ability4 = "NONE";
        public String ability5 = "NONE";

    }



    public List<Ability> getCurrentAbilities(Player player){
        AbilityManager.PlayerAbilityData abilityData = getInstance().getPlayerAbilityData(player);

        if (!player.level().isClientSide) {
            return Arrays.asList(
                    getInstance().getAbility(Objects.requireNonNullElse(abilityData.chosenAbility1, EmptyAbility.getInstance().getName())),
                    getInstance().getAbility(Objects.requireNonNullElse(abilityData.chosenAbility2, EmptyAbility.getInstance().getName())),
                    getInstance().getAbility(Objects.requireNonNullElse(abilityData.chosenFinal, EmptyAbility.getInstance().getName()))
            );
        } else return Arrays.asList(
                getInstance().getAbility(Objects.requireNonNullElse(ClientFistData.getChosenAbility1(), EmptyAbility.getInstance().getName())),
                getInstance().getAbility(Objects.requireNonNullElse(ClientFistData.getChosenAbility2(), EmptyAbility.getInstance().getName())),
                getInstance().getAbility(Objects.requireNonNullElse(ClientFistData.getChosenAbility3(), EmptyAbility.getInstance().getName()))
        );
    }

    public List<AbstractAbilityData> getCurrentAbilityData(Player player){
        Ability ability1 = getCurrentAbilities(player).get(0);
        Ability ability2 = getCurrentAbilities(player).get(1);
        Ability ability3 = getCurrentAbilities(player).get(2);

        return Arrays.asList(ability1.getAbilityData(player),
                ability2.getAbilityData(player),
                ability3.getAbilityData(player));
    }
    public List<AbstractAbilityData> getCurrentFinalAbilityData(Player player){
        FinalAbility ability1 = getCurrentFinalAbilities(player).get(0);
        FinalAbility ability2 = getCurrentFinalAbilities(player).get(1);

        return Arrays.asList(ability1.getAbilityData(player),
                ability2.getAbilityData(player));
    }


    public List<FinalAbility> getCurrentFinalAbilities(Player player){
        AbilityManager.PlayerAbilityData abilityData = getInstance().getPlayerAbilityData(player);
            if (!player.level().isClientSide) {
                return Arrays.asList(
                        getInstance().getFinalAbility(abilityData.ability4),
                        getInstance().getFinalAbility(abilityData.ability5)
                );
            } else return Arrays.asList(
                    getInstance().getFinalAbility(ClientFistData.getCurrentAbility4()),
                    getInstance().getFinalAbility(ClientFistData.getCurrentAbility5())
            );
    }



    public PlayerAbilityData getOrCreatePlayerAbilityData(Player player){
        return playerDataMap.computeIfAbsent(player.getUUID(), k -> new PlayerAbilityData());
    }
    public PlayerAbilityData getPlayerAbilityData(Player player){
        return getOrCreatePlayerAbilityData(player);
    }

}
