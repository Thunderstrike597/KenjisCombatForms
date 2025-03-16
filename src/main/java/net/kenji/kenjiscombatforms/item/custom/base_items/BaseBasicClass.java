package net.kenji.kenjiscombatforms.item.custom.base_items;

import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.minecraft.world.item.ItemStack;

public class BaseBasicClass extends BaseFistClass {
    public BaseBasicClass(Properties properties) {
        super(properties);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        final int basicFormCompat = KenjisCombatFormsCommon.BASIC_FORM_COMPAT.get();

        super.setDamage(stack, getDamage(stack) * basicFormCompat);
    }
}
