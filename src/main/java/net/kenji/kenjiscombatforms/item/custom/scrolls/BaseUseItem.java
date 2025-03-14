package net.kenji.kenjiscombatforms.item.custom.scrolls;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BaseUseItem extends Item {
    public BaseUseItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }
}
