package net.kenji.kenjiscombatforms.item.custom.forms;

import net.minecraft.world.item.Tier;
import yesman.epicfight.world.item.WeaponItem;

public class BaseFormClass extends WeaponItem {
    public BaseFormClass(Tier tier, int damageIn, float speedIn, Properties builder) {
        super(tier, damageIn, speedIn, builder);
    }
}
