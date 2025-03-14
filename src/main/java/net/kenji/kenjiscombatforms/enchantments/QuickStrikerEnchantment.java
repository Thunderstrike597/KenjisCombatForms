package net.kenji.kenjiscombatforms.enchantments;

import net.kenji.kenjiscombatforms.item.custom.forms.BaseFormClass;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.entity.eventlistener.AttackSpeedModifyEvent;
import yesman.epicfight.world.item.WeaponItem;


public class QuickStrikerEnchantment extends Enchantment {
    protected QuickStrikerEnchantment(Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot[] equipmentSlots) {
        super(rarity, EnchantmentCategory.WEAPON, equipmentSlots);
    }



    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int level) {
        return switch (level) {
            case 1 -> 18;
            case 2 -> 24;
            case 3 -> 30;
            default -> level;
        };
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
        return stack.getItem() instanceof BaseFormClass;
    }


}
