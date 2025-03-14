package net.kenji.kenjiscombatforms.enchantments;

import net.kenji.kenjiscombatforms.item.custom.forms.BaseFormClass;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class HeavyHitterEnchantment extends Enchantment {
    protected HeavyHitterEnchantment(Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot[] equipmentSlots) {
        super(rarity, enchantmentCategory, equipmentSlots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }


    @Override
    public int getMinCost(int level) {
        return switch (level) {
            case 1 -> 14;
            case 2 -> 24;
            case 3 -> 30;
            default -> level;
        };
    }

    @Override
    public int getMaxCost(int p_44691_) {
        return 30;
    }

    @Override
    public float getDamageBonus(int level, MobType mobType, ItemStack enchantedItem) {
        if(level == 1) {
            return 1f;
        }
        if(level == 2) {
           return 1.6f;
        }
        if(level == 3) {
            return 2f;
        }
        else return 0;

    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof BaseFormClass;
    }


    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem().asItem() instanceof BaseFormClass;
    }

}
