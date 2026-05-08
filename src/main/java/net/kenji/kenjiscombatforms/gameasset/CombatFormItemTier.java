package net.kenji.kenjiscombatforms.gameasset;

import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import yesman.epicfight.world.item.EpicFightItemTier;

import java.util.function.Supplier;

public enum CombatFormItemTier implements Tier {
    COMBAT_DAGGER(4, 800, 1.0F, 4.0F, 14, () -> Ingredient.of(new ItemLike[]{Items.IRON_INGOT}));


    private final int harvestLevel;
    private final int maxUses;
    private final float speed;
    private final float attackDamage;
    private final int enchantability;
    private final LazyLoadedValue<Ingredient> repairMaterial;

    CombatFormItemTier(int harvestLevelIn, int maxUsesIn, float speed, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.speed = speed;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = new LazyLoadedValue(repairMaterialIn);
    }

    public int getUses() {
        return this.maxUses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    public int getLevel() {
        return this.harvestLevel;
    }

    public int getEnchantmentValue() {
        return this.enchantability;
    }

    public Ingredient getRepairIngredient() {
        return (Ingredient)this.repairMaterial.get();
    }
}

