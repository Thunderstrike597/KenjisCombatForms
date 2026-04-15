package net.kenji.kenjiscombatforms.api.basegameassets.condition;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
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
        Player player = (Player) playerPatch.getOriginal();

        if (!player.onGround() && !player.isInWater()) {
            // Position slightly below player
            double checkY = player.getY() - 0.2;

            BlockPos pos = BlockPos.containing(player.getX(), checkY, player.getZ());

            // Check if the block is solid
            return !player.level().getBlockState(pos).isSolid();
        }

        return false;
    }

    public List<ParameterEditor> getAcceptingParameters(Screen screen) {
        return null;
    }
}
