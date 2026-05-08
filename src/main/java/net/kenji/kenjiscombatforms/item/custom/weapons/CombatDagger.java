package net.kenji.kenjiscombatforms.item.custom.weapons;

import net.kenji.kenjiscombatforms.item.custom.base_items.BaseCombatWeapon;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import yesman.epicfight.world.item.GloveItem;

public class CombatDagger extends BaseCombatWeapon {

    public CombatDagger(Properties build, Tier materialIn) {
        super(build, materialIn);
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 4;
    }
}
