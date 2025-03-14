package net.kenji.kenjiscombatforms.item.custom.forms;

import net.kenji.kenjiscombatforms.enchantments.ModEnchantments;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.world.item.WeaponItem;

import java.util.List;

public class PowerForm2Item extends BaseFormClass {
    public PowerForm2Item(Tier tier, int damageIn, float speedIn, Properties builder) {
        super(tier, damageIn, speedIn, builder);
    }

    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        Player player = (Player) attacker;
        if (player.getOffhandItem().is(Items.AIR)) {
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60));
        }if (!player.getOffhandItem().is(Items.AIR)) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 50, 0, false, false));
        }

        return super.hurtEnemy(itemStack, target, attacker);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

        return enchantment != Enchantments.SHARPNESS &&
                enchantment != Enchantments.MENDING &&
                enchantment != Enchantments.SWEEPING_EDGE &&
                enchantment != Enchantments.UNBREAKING && enchantment.category == EnchantmentCategory.WEAPON;

    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.kenjiscombatforms.power_form2.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
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
    public void inventoryTick(ItemStack stack, Level lvl, Entity entity, int pInt, boolean pBool) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.getMainHandItem().is(ModItems.POWERFORM2.get())) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 0, false, false));
            }
        }
        super.inventoryTick(stack, lvl, entity, pInt, pBool);
    }
}