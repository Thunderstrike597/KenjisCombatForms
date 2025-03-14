package net.kenji.kenjiscombatforms.api.interfaces.ability;

import net.minecraft.world.entity.player.Player;

public class NoOpDamageGainStrategy implements AbilityDamageGainStrategy{
    @Override
    public void fillDamageCooldown(Player player) {

    }
}
