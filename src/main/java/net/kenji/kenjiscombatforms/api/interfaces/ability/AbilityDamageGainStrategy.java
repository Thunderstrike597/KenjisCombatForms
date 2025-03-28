package net.kenji.kenjiscombatforms.api.interfaces.ability;

import net.minecraft.world.entity.player.Player;

public interface AbilityDamageGainStrategy {
    void fillDamageCooldown(Player player);
}
