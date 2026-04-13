package net.kenji.kenjiscombatforms.api.basegameassets.condition;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.data.conditions.Condition;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.List;

public class InAirCondition implements Condition<PlayerPatch<?>> {
    public Condition<PlayerPatch<?>> read(CompoundTag compoundTag) {
        return this;
    }

    public CompoundTag serializePredicate() {
        return new CompoundTag();
    }

    public boolean predicate(PlayerPatch<?> playerPatch) {
        Player player = (Player)playerPatch.getOriginal();
        return !player.onGround() && !player.isInWater();
    }

    public List<ParameterEditor> getAcceptingParameters(Screen screen) {
        return null;
    }
}
