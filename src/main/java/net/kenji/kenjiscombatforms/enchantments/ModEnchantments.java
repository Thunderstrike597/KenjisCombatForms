package net.kenji.kenjiscombatforms.enchantments;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.item.custom.forms.BaseFormClass;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, KenjisCombatForms.MOD_ID);

    public static final RegistryObject<Enchantment> HEAVY_HITTER_ENCHANTMENT = ENCHANTMENTS.register("heavy_hitter", () -> new HeavyHitterEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.WEAPON, EquipmentSlot.values()));

    public static final RegistryObject<Enchantment> QUICK_STRIKER_ENCHANTMENT = ENCHANTMENTS.register("quick_striker", () -> new QuickStrikerEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.WEAPON, EquipmentSlot.values()));

    public static void register(IEventBus eventBus){
        ENCHANTMENTS.register(eventBus);
    }
}
