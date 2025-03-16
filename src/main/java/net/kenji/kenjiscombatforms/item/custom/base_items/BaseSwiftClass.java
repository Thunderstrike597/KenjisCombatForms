package net.kenji.kenjiscombatforms.item.custom.base_items;

import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.minecraft.world.item.ItemStack;

public class BaseSwiftClass extends BaseFistClass {
    public BaseSwiftClass(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        final int swiftFormCompat = KenjisCombatFormsCommon.SWIFT_FORM_COMPAT.get();

        super.setDamage(stack, getDamage(stack) * swiftFormCompat);
    }
}
