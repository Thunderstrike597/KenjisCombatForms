package net.kenji.kenjiscombatforms.item.custom.base_items;

import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.minecraft.world.item.ItemStack;

public class BasePowerClass extends BaseFistClass {
    public BasePowerClass(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
       final int powerFormCompat = KenjisCombatFormsCommon.POWER_FORM_COMPAT.get();

        super.setDamage(stack, getDamage(stack) * powerFormCompat);
    }
}
