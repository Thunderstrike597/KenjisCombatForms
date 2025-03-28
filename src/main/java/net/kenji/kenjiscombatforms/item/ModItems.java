package net.kenji.kenjiscombatforms.item;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.entity.ModEntities;
import net.kenji.kenjiscombatforms.item.custom.crafting_components.WitherAbility1Component;
import net.kenji.kenjiscombatforms.item.custom.crafting_components.WitherAbility2Component;
import net.kenji.kenjiscombatforms.item.custom.crafting_components.VoidAbility1Component;
import net.kenji.kenjiscombatforms.item.custom.crafting_components.VoidAbility2Component;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.EnderFormItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.WitherFormItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.power_form.PowerFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.power_form.PowerFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.power_form.PowerFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.swift_form.SwiftFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.swift_form.SwiftFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.swift_form.SwiftFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.void_form.VoidFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.void_form.VoidFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.void_form.VoidFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.wither_form.WitherFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.wither_form.WitherFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.wither_form.WitherFistItem;
import net.kenji.kenjiscombatforms.item.custom.scrolls.BlankAbilityScroll;
import net.kenji.kenjiscombatforms.item.custom.scrolls.BlankFormScroll;
import net.kenji.kenjiscombatforms.item.custom.scrolls.ability_scrolls.*;
import net.kenji.kenjiscombatforms.item.custom.scrolls.form_scrolls.PowerScroll;
import net.kenji.kenjiscombatforms.item.custom.scrolls.form_scrolls.SwiftScroll;
import net.kenji.kenjiscombatforms.item.custom.scrolls.form_scrolls.VoidScroll;
import net.kenji.kenjiscombatforms.item.custom.form_essence.*;
import net.kenji.kenjiscombatforms.item.custom.forms.*;
import net.kenji.kenjiscombatforms.item.custom.crafting_components.*;
import net.kenji.kenjiscombatforms.item.custom.materials.SlotLiner;
import net.kenji.kenjiscombatforms.item.custom.scrolls.form_scrolls.WitherScroll;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {


    public ModItems(){}

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, KenjisCombatForms.MOD_ID);

    public static final RegistryObject<Item> EMPTYSLOT = ITEMS.register("empty_slot", () -> new EmptySlot(new Item.Properties()));
    public static final RegistryObject<Item> SWIFTFORMTEMPLATE = ITEMS.register("swift_form_template", () -> new SwiftFormTemplate(new Item.Properties()));
    public static final RegistryObject<Item> POWERFORMTEMPLATE = ITEMS.register("power_form_template", () -> new PowerFormTemplate(new Item.Properties()));
    public static final RegistryObject<Item> ABILITYFORMTEMPLATE = ITEMS.register("ability_form_template", () -> new AbilityFormTemplate(new Item.Properties()));
    public static final RegistryObject<Item> EMPTYABILITYFORM = ITEMS.register("empty_ability_form", () -> new EmptyAbilityForm(new Item.Properties()));

    public static final RegistryObject<Item> SLOTLINER = ITEMS.register("slot_liner", () -> new SlotLiner(new Item.Properties()));
    public static final RegistryObject<Item> ENDER_CHARGE = ITEMS.register("ender_charge", () -> new EnderCharge(new Item.Properties()));
 public static final RegistryObject<Item> NETHER_CHARGE = ITEMS.register("nether_charge", () -> new NetherCharge(new Item.Properties()));

    public static final RegistryObject<Item> OVERCHARGED_ENDER_PLASMA = ITEMS.register("overcharged_ender_plasma", () -> new OverchargedEnderPlasma(new Item.Properties()));
    public static final RegistryObject<Item> OVERCHARGED_NETHER_PLASMA = ITEMS.register("overcharged_nether_plasma", () -> new OverchargedNetherPlasma(new Item.Properties()));


    public static final RegistryObject<Item> BLANK_FORM_SCROLL = ITEMS.register("blank_form_scroll", () -> new BlankFormScroll(new Item.Properties()));
    public static final RegistryObject<Item> BLANK_ABILITY_SCROLL = ITEMS.register("blank_ability_scroll", () -> new BlankAbilityScroll(new Item.Properties()));

    public static final RegistryObject<Item> SCROLL_DUST = ITEMS.register("glowing_scroll_dust", () -> new ScrollDust(new Item.Properties()));
   public static final RegistryObject<Item> MYSTERIOUS_DUST = ITEMS.register("mysterious_dust", () -> new MysteriousDust(new Item.Properties()));


    public static final RegistryObject<SpawnEggItem> EXILED_DEVIL_SPAWN_EGG = ITEMS.register("exiled_devil_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.EXILED_DEVIL, 0x8B0000, 0x4B0000, new Item.Properties().stacksTo(16)));
    public static final RegistryObject<SpawnEggItem> EXILED_SENSEI_SPAWN_EGG = ITEMS.register("exiled_sensei_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.EXILED_SENSEI, 0xffae42, 0xe0b334, new Item.Properties().stacksTo(16)));
    public static final RegistryObject<SpawnEggItem> UNDEAD_SENSEI_SPAWN_EGG = ITEMS.register("undead_sensei_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.UNDEAD_SENSEI, 0x006400, 0xffc00, new Item.Properties().stacksTo(16)));
    public static final RegistryObject<SpawnEggItem> ABILITY_TRADER_SPAWN_EGG = ITEMS.register("ability_trader_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SCROLL_TRADER, 0xADDAE6, 0x00008B, new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> SWIFTNESSESSENCE_TIER1 = ITEMS.register("tier1_swiftness_essence", () -> new SwiftnessEssenceTier1(new Item.Properties()));
    public static final RegistryObject<Item> SWIFTNESSESSENCE_TIER2 = ITEMS.register("tier2_swiftness_essence", () -> new SwiftnessEssenceTier2(new Item.Properties()));
    public static final RegistryObject<Item> SWIFTNESSESSENCE_TIER3 = ITEMS.register("tier3_swiftness_essence", () -> new SwiftnessEssenceTier3(new Item.Properties()));

    public static final RegistryObject<Item> POWERESSENCE_TIER1 = ITEMS.register("tier1_power_essence", () -> new PowerEssenceTier1(new Item.Properties()));
    public static final RegistryObject<Item> POWERESSENCE_TIER2 = ITEMS.register("tier2_power_essence", () -> new PowerEssenceTier2(new Item.Properties()));
    public static final RegistryObject<Item> POWERESSENCE_TIER3 = ITEMS.register("tier3_power_essence", () -> new PowerEssenceTier3(new Item.Properties()));
    public static final RegistryObject<Item> ABILITY_ESSENCE = ITEMS.register("ability_essence", () -> new AbilityEssence(new Item.Properties()));

    public static final RegistryObject<Item> VOID_FORM_SCROLL = ITEMS.register("void_form_scroll", () -> new VoidScroll(new Item.Properties()));
    public static final RegistryObject<Item> WITHER_FORM_SCROLL = ITEMS.register("wither_form_scroll", () -> new WitherScroll(new Item.Properties()));
    public static final RegistryObject<Item> SWIFT_FORM_SCROLL = ITEMS.register("swift_form_scroll", () -> new SwiftScroll(new Item.Properties()));
    public static final RegistryObject<Item> POWER_FORM_SCROLL = ITEMS.register("power_form_scroll", () -> new PowerScroll(new Item.Properties()));

    public static final RegistryObject<Item> VOID_ABILITY1_SCROLL = ITEMS.register("void_ability1_scroll", () -> new VoidAbility1Scroll(new Item.Properties()));
    public static final RegistryObject<Item> VOID_ABILITY2_SCROLL = ITEMS.register("void_ability2_scroll", () -> new VoidAbility2Scroll(new Item.Properties()));
    public static final RegistryObject<Item> VOID_FINAL_ABILITY_SCROLL = ITEMS.register("void_final_scroll", () -> new VoidFinalAbilityScroll(new Item.Properties()));

    public static final RegistryObject<Item> WITHER_ABILITY1_SCROLL = ITEMS.register("wither_ability1_scroll", () -> new WitherAbility1Scroll(new Item.Properties()));
    public static final RegistryObject<Item> WITHER_ABILITY2_SCROLL = ITEMS.register("wither_ability2_scroll", () -> new WitherAbility2Scroll(new Item.Properties()));
    public static final RegistryObject<Item> WITHER_FINAL_ABILITY_SCROLL = ITEMS.register("wither_final_scroll", () -> new WitherFinalAbilityScroll(new Item.Properties()));

    public static final RegistryObject<Item> SWIFT_ABILITY1_SCROLL = ITEMS.register("swift_ability1_scroll", () -> new SwiftAbility1Scroll(new Item.Properties()));
    public static final RegistryObject<Item> SWIFT_ABILITY2_SCROLL = ITEMS.register("swift_ability2_scroll", () -> new SwiftAbility2Scroll(new Item.Properties()));


    public static final RegistryObject<Item> POWER_ABILITY1_SCROLL = ITEMS.register("power_ability1_scroll", () -> new PowerAbility1Scroll(new Item.Properties()));
    public static final RegistryObject<Item> POWER_ABILITY2_SCROLL = ITEMS.register("power_ability2_scroll", () -> new PowerAbility2Scroll(new Item.Properties()));


    public static final RegistryObject<Item> VOID_ABILITY1_COMPONENT = ITEMS.register("void_ability1_component", () -> new VoidAbility1Component(new Item.Properties()));
    public static final RegistryObject<Item> VOID_ABILITY2_COMPONENT = ITEMS.register("void_ability2_component", () -> new VoidAbility2Component(new Item.Properties()));
    public static final RegistryObject<Item> VOID_ABILITY3_COMPONENT = ITEMS.register("void_ability3_component", () -> new VoidAbility3Component(new Item.Properties()));

    public static final RegistryObject<Item> WITHER_ABILITY1_COMPONENT = ITEMS.register("wither_ability1_component", () -> new WitherAbility1Component(new Item.Properties()));
    public static final RegistryObject<Item> WITHER_ABILITY2_COMPONENT = ITEMS.register("wither_ability2_component", () -> new WitherAbility2Component(new Item.Properties()));
    public static final RegistryObject<Item> WITHER_ABILITY3_COMPONENT = ITEMS.register("wither_ability3_component", () -> new WitherAbility3Component(new Item.Properties()));

 public static final RegistryObject<Item> SWIFT_ABILITY1_COMPONENT = ITEMS.register("swift_ability1_component", () -> new SwiftAbility1Component(new Item.Properties()));
    public static final RegistryObject<Item> SWIFT_ABILITY2_COMPONENT = ITEMS.register("swift_ability2_component", () -> new SwiftAbility2Component(new Item.Properties()));

    public static final RegistryObject<Item> POWER_ABILITY1_COMPONENT = ITEMS.register("power_ability1_component", () -> new PowerAbility1Component(new Item.Properties()));
    public static final RegistryObject<Item> POWER_ABILITY2_COMPONENT = ITEMS.register("power_ability2_component", () -> new PowerAbility2Component(new Item.Properties()));



 public static final RegistryObject<Item> VOID_CORE_COMPONENT = ITEMS.register("void_core_component", () -> new VoidCoreComponent(new Item.Properties()));
 public static final RegistryObject<Item> WITHER_CORE_COMPONENT = ITEMS.register("wither_core_component", () -> new WitherCoreComponent(new Item.Properties()));
 public static final RegistryObject<Item> SWIFT_CORE_COMPONENT = ITEMS.register("swift_core_component", () -> new SwiftCoreComponent(new Item.Properties()));
 public static final RegistryObject<Item> POWER_CORE_COMPONENT = ITEMS.register("power_core_component", () -> new PowerCoreComponent(new Item.Properties()));


 public static final RegistryObject<Item> TYPE1_COMPONENT = ITEMS.register("type1_component", () -> new Type1_Component(new Item.Properties()));
    public static final RegistryObject<Item> TYPE2_COMPONENT = ITEMS.register("type2_component", () -> new Type2_Component(new Item.Properties()));
    public static final RegistryObject<Item> TYPE3_COMPONENT = ITEMS.register("type3_component", () -> new Type3_Component(new Item.Properties()));
    public static final RegistryObject<Item> ABILITY_COMPONENT = ITEMS.register("ability_component", () -> new Ability_Component(new Item.Properties()));


    public static final RegistryObject<Item> BASICFORM1 = ITEMS.register("basic_form1", () -> new BasicForm1Item(Tiers.WOOD, 2, 1, new Item.Properties().stacksTo(1).durability(548)));
    public static final RegistryObject<Item> BASICFORM2 = ITEMS.register("basic_form2", () -> new BasicForm2Item(Tiers.WOOD, 2, 1, new Item.Properties().stacksTo(1).durability(884)));
    public static final RegistryObject<Item> BASICFORM3 = ITEMS.register("basic_form3", () -> new BasicForm3Item(Tiers.WOOD, 3, 1, new Item.Properties().stacksTo(1).durability(924)));

    public static final RegistryObject<Item> SWIFTFORM1 = ITEMS.register("swift_form1", () -> new SwiftForm1Item(Tiers.WOOD, 2, -1.75f, new Item.Properties().stacksTo(1).durability(964)));
    public static final RegistryObject<Item> SWIFTFORM2 = ITEMS.register("swift_form2", () -> new SwiftForm2Item(Tiers.WOOD, 2, -5, new Item.Properties().stacksTo(1).durability(1255)));
    public static final RegistryObject<Item> SWIFTFORM3 = ITEMS.register("swift_form3", () -> new SwiftForm3Item(Tiers.WOOD, 3, -5, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> POWERFORM1 = ITEMS.register("power_form1", () -> new PowerForm1Item(Tiers.STONE, 2, -2.2f, new Item.Properties().stacksTo(1).durability(964)));
    public static final RegistryObject<Item> POWERFORM2 = ITEMS.register("power_form2", () -> new PowerForm2Item(Tiers.IRON, 3, -2f, new Item.Properties().stacksTo(1).durability(1255)));
    public static final RegistryObject<Item> POWERFORM3 = ITEMS.register("power_form3", () -> new PowerForm3Item(Tiers.IRON, 3, -1.9f, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BASIC_FIST_ITEM = ITEMS.register("fist", () -> new BasicFistItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BASIC_FIST2_ITEM = ITEMS.register("fist_tier2", () -> new BasicFist2Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BASIC_FIST3_ITEM = ITEMS.register("fist_tier3", () -> new BasicFist3Item(new Item.Properties().stacksTo(1)));


    public static final RegistryObject<Item> VOID_FIST_ITEM = ITEMS.register("void_fist", () -> new VoidFistItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> VOID_FIST2_ITEM = ITEMS.register("void_fist_tier2", () -> new VoidFist2Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> VOID_FIST3_ITEM = ITEMS.register("void_fist_tier3", () -> new VoidFist3Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> WITHER_FIST_ITEM = ITEMS.register("wither_fist", () -> new WitherFistItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> WITHER_FIST2_ITEM = ITEMS.register("wither_fist_tier2", () -> new WitherFist2Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> WITHER_FIST3_ITEM = ITEMS.register("wither_fist_tier3", () -> new WitherFist3Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SWIFT_FIST_ITEM = ITEMS.register("swift_fist", () -> new SwiftFistItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SWIFT_FIST2_ITEM = ITEMS.register("swift_fist_tier2", () -> new SwiftFist2Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SWIFT_FIST3_ITEM = ITEMS.register("swift_fist_tier3", () -> new SwiftFist3Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> POWER_FIST_ITEM = ITEMS.register("power_fist", () -> new PowerFistItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> POWER_FIST2_ITEM = ITEMS.register("power_fist_tier2", () -> new PowerFist2Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> POWER_FIST3_ITEM = ITEMS.register("power_fist_tier3", () -> new PowerFist3Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> VOID_FORM_ITEM = ITEMS.register("ender_form_fist", () -> new EnderFormItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> WITHER_FORM_ITEM = ITEMS.register("wither_form_fist", () -> new WitherFormItem(new Item.Properties().stacksTo(1)));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static String getDefaultItemName() {
        return BuiltInRegistries.ITEM.getKey(POWERFORM2.get()).toString();
    }


}
