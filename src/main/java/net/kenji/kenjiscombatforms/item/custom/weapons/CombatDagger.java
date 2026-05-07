package net.kenji.kenjiscombatforms.item.custom.weapons;

import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import yesman.epicfight.world.item.GloveItem;

public class CombatDagger extends GloveItem {

    public CombatDagger(Properties build, Tier materialIn) {
        super(build, materialIn);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
        return false;
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public boolean isEnchantable(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return true;
    }

}
