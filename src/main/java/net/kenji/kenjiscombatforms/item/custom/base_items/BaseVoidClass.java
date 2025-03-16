package net.kenji.kenjiscombatforms.item.custom.base_items;

import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.minecraft.world.item.ItemStack;

public class BaseVoidClass extends BaseFistClass {
    public BaseVoidClass(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
      final int voidFormCompat = KenjisCombatFormsCommon.VOID_FORM_COMPAT.get();

        super.setDamage(stack, getDamage(stack) * voidFormCompat);
    }

}
