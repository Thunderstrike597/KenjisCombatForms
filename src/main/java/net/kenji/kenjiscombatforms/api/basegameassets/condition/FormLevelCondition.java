package net.kenji.kenjiscombatforms.api.basegameassets.condition;

import net.kenji.kenjiscombatforms.api.basegameassets.skills.BaseComboBuilder;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import yesman.epicfight.data.conditions.Condition;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.List;

public class FormLevelCondition implements Condition<PlayerPatch<?>> {

    public final BaseComboBuilder.FistTier fistTier;
    public FormLevelCondition(BaseComboBuilder.FistTier fistTier){
        this.fistTier = fistTier;
    }
    public Condition<PlayerPatch<?>> read(CompoundTag compoundTag) {
        return this;
    }

    public CompoundTag serializePredicate() {
        return new CompoundTag();
    }

    @Override
    public boolean predicate(PlayerPatch<?> playerPatch) {
        FormLevelManager.FormLevel formLevel = FormManager.getCurrentForm(playerPatch.getOriginal()).getFormData(playerPatch.getOriginal().getUUID()).getCurrentFormLevel();

        return formLevel.value >= this.fistTier.value;
    }

    public List<ParameterEditor> getAcceptingParameters(Screen screen) {
        return null;
    }
}
