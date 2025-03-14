package net.kenji.kenjiscombatforms.item.custom.forms;

import net.kenji.kenjiscombatforms.enchantments.ModEnchantments;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.world.item.WeaponItem;

import java.util.List;

    public class SwiftForm1Item extends BaseFormClass {
        public SwiftForm1Item(Tier tier, int damageIn, float speedIn, Properties builder) {
            super(tier, damageIn, speedIn, builder);
        }

        @Override
        public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

            return enchantment != Enchantments.SHARPNESS &&
                    enchantment != Enchantments.MENDING &&
                    enchantment != Enchantments.SWEEPING_EDGE &&
                    enchantment != Enchantments.UNBREAKING && enchantment.category == EnchantmentCategory.WEAPON;

        }




        @Override
        public boolean canBeDepleted() {
            return true;
        }

        @Override
        public boolean isValidRepairItem(ItemStack item1, ItemStack item2) {
            return item2.getItem() == ModItems.EMPTYSLOT.get();
        }

        @Override
        public boolean isEnchantable(ItemStack stack) {
            return !stack.isEnchanted();
        }

        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
            pTooltipComponents.add(Component.translatable("tooltip.kenjiscombatforms.swift_form1.tooltip"));
            super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        }
}
