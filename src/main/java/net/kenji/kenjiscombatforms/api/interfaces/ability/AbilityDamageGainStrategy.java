package net.kenji.kenjiscombatforms.api.interfaces.ability;

import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.minecraft.world.entity.player.Player;

public interface AbilityDamageGainStrategy {
    void fillDamageCooldown(Player player);
}
