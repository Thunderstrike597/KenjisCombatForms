package net.kenji.kenjiscombatforms.gameasset;

import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

public enum CombatFormStyles implements Style {
    COMBAT_FORM(true);

    public final int id;
    public final boolean canUseOffhand;
    CombatFormStyles(boolean offhandUsage) {
        this.id = Style.ENUM_MANAGER.assign(this);
        this.canUseOffhand = offhandUsage;
    }


    public int universalOrdinal() {
        return this.id;
    }

    @Override
    public boolean canUseOffhand() {
        return canUseOffhand;
    }
}
