package net.kenji.kenjiscombatforms.api.interfaces.form;

import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.minecraft.world.entity.player.Player;

public interface FormAbilityStrategy {
    void setChosenAbility1(Player player, AbilityManager.AbilityOption1 ability, AbilityManager.PlayerAbilityData abilityData);
    void setChosenAbility2(Player player, AbilityManager.AbilityOption2 ability, AbilityManager.PlayerAbilityData abilityData);
    void setChooseFinalAbility(Player player, AbilityManager.AbilityOption3 ability, AbilityManager.PlayerAbilityData abilityData);

    void storeChosenAbility1(Player player, AbilityManager.AbilityOption1 ability, AbilityManager.PlayerAbilityData abilityData);
    void storeChosenAbility2(Player player, AbilityManager.AbilityOption2 ability, AbilityManager.PlayerAbilityData abilityData);
    void storeChooseFinalAbility(Player player, AbilityManager.AbilityOption3 ability, AbilityManager.PlayerAbilityData abilityData);

    void setStoredChosenAbilities(Player player, AbilityManager.AbilityOption1 ability, AbilityManager.AbilityOption2 ability2, AbilityManager.AbilityOption3 ability3, AbilityManager.PlayerAbilityData abilityData);


    void setLearnedFinalAbility(Player player, AbilityManager.AbilityOption3 finalAbility,  boolean hasLearnedAbility);
}
