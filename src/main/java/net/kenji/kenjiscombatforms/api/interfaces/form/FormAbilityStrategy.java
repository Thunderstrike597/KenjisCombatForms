package net.kenji.kenjiscombatforms.api.interfaces.form;

import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.minecraft.world.entity.player.Player;

public interface FormAbilityStrategy {
    void setChosenAbility1(Player player, String ability, AbilityManager.PlayerAbilityData abilityData);
    void setChosenAbility2(Player player, String ability, AbilityManager.PlayerAbilityData abilityData);
    void setChooseFinalAbility(Player player, String ability, AbilityManager.PlayerAbilityData abilityData);

    void setLearnedFinalAbility(Player player, String finalAbility,  boolean hasLearnedAbility);
}
