package net.kenji.kenjiscombatforms.item.custom.base_items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BaseComponent extends Item {
    public BaseComponent(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnchantable(ItemStack itemStack) {
        return false;
    }
}
