package net.kenji.kenjiscombatforms.item.custom.forms;

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

import java.util.List;

public class PowerForm3Item extends BaseFormClass {
    public PowerForm3Item(Tier tier, int damageIn, float speedIn, Properties builder) {
        super(tier, damageIn, speedIn, builder);
    }


    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60));
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
        pTooltipComponents.add(Component.translatable("tooltip.kenjiscombatforms.power_form3.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return !stack.isEnchanted();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level lvl, Entity entity, int pInt, boolean pBool) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.getMainHandItem().is(ModItems.POWERFORM3.get())) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 0, false, false));
                if (player.getOffhandItem().is(Items.AIR)) {
                    player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 5, 1, false, false));
                }
                if (!player.getOffhandItem().is(Items.AIR)) {
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 5, 1, false, false));
                }
            }

        }
        super.inventoryTick(stack, lvl, entity, pInt, pBool);
    }


}