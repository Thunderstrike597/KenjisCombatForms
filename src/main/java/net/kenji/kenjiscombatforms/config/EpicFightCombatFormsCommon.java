package net.kenji.kenjiscombatforms.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

public class EpicFightCombatFormsCommon {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> EXILED_SENSEI_HEALTH;
    public static final ForgeConfigSpec.ConfigValue<Integer> TAMED_EXILED_SENSEI_HEALTH;
    public static final ForgeConfigSpec.ConfigValue<Integer> EXILED_DEVIL_HEALTH;
    public static final ForgeConfigSpec.ConfigValue<Integer> UNDEAD_SENSEI_HEALTH;

    public static final ForgeConfigSpec.ConfigValue<Double> TIER1_ESSENCE_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<Double> TIER2_ESSENCE_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<Double> TIER3_ESSENCE_CHANCE;

    public static final ForgeConfigSpec.ConfigValue<Double> CHARGE_CHANCE;

    public static final ForgeConfigSpec.ConfigValue<Boolean> IS_SENSEI_TAMABLE;
    public static final ForgeConfigSpec.ConfigValue<Integer> TAMING_EMERALDS_MIN;
    public static final ForgeConfigSpec.ConfigValue<Double> IS_AFRAID_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> IS_AFRAID_MIN_HEALTH;

    public static ForgeConfigSpec.ConfigValue<String> SENSEI_TAME_ITEM;

    public static final ForgeConfigSpec.ConfigValue<Integer> UNDEAD_SENSEI_SPAWNDIST;
    public static final ForgeConfigSpec.ConfigValue<Integer> EXILED_SENSEI_SPAWNDIST;
    public static final ForgeConfigSpec.ConfigValue<Integer> EXILED_DEVIL_SPAWNDIST;
    public static final ForgeConfigSpec.ConfigValue<Integer> ABILITY_TRADER_SPAWNDIST;
    public static final ForgeConfigSpec.ConfigValue<Integer> ABILITY_TRADER_PLAYER_SPAWNDIST;


    public static final ForgeConfigSpec.ConfigValue<Double> UNDEAD_SENSEI_SPAWN_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<Double> EXILED_SENSEI_SPAWN_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<Double> EXILED_DEVIL_SPAWN_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<Double> ABILITY_TRADER_SPAWN_CHANCE;

    public static final ForgeConfigSpec.ConfigValue<Integer> ABILITY1_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> ABILITY2_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> ABILITY3_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> ABILITY4_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> ABILITY5_COOLDOWN;

    public static final ForgeConfigSpec.ConfigValue<Integer> TELEPORT_DIST;
    public static final ForgeConfigSpec.ConfigValue<Integer> EXPLOSION_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> MINION_COUNT;

    public static final ForgeConfigSpec.ConfigValue<Integer> ABILITY1_COOLDOWN_DIVISION;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ABILITY2_COMBAT_MODE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ABILITY3_COMBAT_MODE;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMBAT_MODE_GAIN_AMOUNT;


    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_FORM_STARTING_XP;
    public static final ForgeConfigSpec.ConfigValue<Integer> LEVEL2_FORM_MAX_XP_ADDITION;

    public static final ForgeConfigSpec.ConfigValue<Boolean> CAN_USE_ABILITIES_NO_FORM;

    public static final ForgeConfigSpec.ConfigValue<Boolean> CAN_LOCK_SLOT;
    public static final ForgeConfigSpec.ConfigValue<Integer> FORM_LOCK_SLOT;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DIFFICULTY_COMPAT_MODE;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMPAT_MODE_UNDEAD_SENSEI_HEALTH;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMPAT_MODE_EXILED_DEVIL_HEALTH;

    public static final ForgeConfigSpec.ConfigValue<Integer> BASIC_FORM_BASE_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> SWIFT_FORM_BASE_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> POWER_FORM_BASE_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> VOID_FORM_BASE_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHER_FORM_BASE_DAMAGE;

    public static final ForgeConfigSpec.ConfigValue<Double> LEVEL1_DAMAGE_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> LEVEL2_DAMAGE_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> LEVEL3_DAMAGE_MULTIPLIER;

    public static final ForgeConfigSpec.ConfigValue<Double> BASIC_FORM_BASE_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Double> SWIFT_FORM_BASE_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Double> POWER_FORM_BASE_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Double> VOID_FORM_BASE_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Double> WITHER_FORM_BASE_SPEED;

    public static final ForgeConfigSpec.ConfigValue<Double> LEVEL1_SPEED_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> LEVEL2_SPEED_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> LEVEL3_SPEED_MULTIPLIER;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ABILITY_SELECTION_MODE;

    static final String defaultItem = "kenjiscombatforms:power_form2";
    static final List<String> validItemNames = Arrays.asList("kenjiscombatforms:power_form1", "kenjiscombatforms:power_form2","kenjiscombatforms:power_form3", "kenjiscombatforms:swift_form1", "kenjiscombatforms:swift_form2","kenjiscombatforms:swift_form3");



    static {
        BUILDER.push("Entity Health Settings");

        EXILED_SENSEI_HEALTH = BUILDER.comment("How much health the \"Exiled Sensei\" should have when not tamed (Default value = 80)")
                .define("exiled Sensei Health", 80);
        TAMED_EXILED_SENSEI_HEALTH = BUILDER.comment("How much health the \"Exiled Sensei\" should have when tamed (Default value = 120)")
                .define("exiled Sensei Health(tame)", 120);

        UNDEAD_SENSEI_HEALTH = BUILDER.comment("How much health the \"Undead Sensei\" should have (Default value = 40)")
                .define("Undead Sensei Health", 40);
        EXILED_DEVIL_HEALTH = BUILDER.comment("How much health the \"Exiled Devil\" should have (Default value = 140)")
                .define("Exiled Devil Health", 140);
        BUILDER.pop();

        BUILDER.push("Entity Loot Drop Chances");
        TIER1_ESSENCE_CHANCE = BUILDER.comment("The drop chance of Tier 1 Swiftness/Power-Essence (Default value = 0.16)")
                .defineInRange("Tier1 Essence Chance", 0.16, 0.0, 1.0);

        TIER2_ESSENCE_CHANCE = BUILDER.comment("The drop chance of Tier 2 Swiftness/Power-Essence (Default value = 0.30)")
                .defineInRange("Tier2 Essence Chance", 0.30, 0.0, 1.0);
        
        TIER3_ESSENCE_CHANCE = BUILDER.comment("The drop chance of Tier 3 Swiftness/Power-Essence (Default value = 0.50)")
                .defineInRange("Tier3 Essence Chance", 0.50, 0.0, 1.0);

        CHARGE_CHANCE = BUILDER.comment("The drop chance of charges(eg. ender/nether charge) - (Default value = 0.16)")
                .defineInRange("Charge Drop Chance", 0.24, 0.0, 1.0);
        BUILDER.pop();

        BUILDER.push("Taming Settings");
        IS_SENSEI_TAMABLE = BUILDER.comment("Whether or not the Exiled Sensei able to be tamed(Will not effect already tamed sensei's! - Default value = true)")
                .define("Can Tame Exiled Sensei", true);

        SENSEI_TAME_ITEM = BUILDER.comment("What item can be used to tame the Exiled Sensei (Item MUST be a form and its tier-swift/power_form1/2/3, eg. swift_form1, swift_form2, power_from2, swift_form3 etc...)")
                .defineInList("Exiled Sensei Taming Item", defaultItem, validItemNames);

        TAMING_EMERALDS_MIN = BUILDER.comment("The minimum amount of emeralds needed in order to tame the Exiled Sensei (Default Value = 5)")
                        .defineInRange("Min Emeralds For Tame", 5, 0, 64);

        IS_AFRAID_CHANCE = BUILDER.comment("The chance that an Exiled Sensei will have to become afraid when below a certain amount of health(Default value = 0.45)")
                .defineInRange("Exiled Sensei Afraid Chance", 0.45, 0.0, 1);

        IS_AFRAID_MIN_HEALTH = BUILDER.comment("The amount of health the exiled sensei has to be below in order to have a chance to be tamed (Default value = 20)")
                .define("Exiled Sensei Afraid Health Amount", 20);
        BUILDER.pop();

        BUILDER.push("Entity Spawns");
        UNDEAD_SENSEI_SPAWNDIST = BUILDER.comment("The distance between undead sensei's to be allowed to spawn (Default is 80)")
                .define("Undead Sensei Spawn Distance", 80);
        EXILED_SENSEI_SPAWNDIST = BUILDER.comment("The distance between exiled sensei's to be allowed to spawn (Default is 128)")
                .define("Exiled Sensei Spawn Distance", 128);
        EXILED_DEVIL_SPAWNDIST = BUILDER.comment("The distance between exiled devil's to be allowed to spawn (Default is 164)")
                .define("Exiled Devil Spawn Distance", 164);
        ABILITY_TRADER_SPAWNDIST = BUILDER.comment("The distance between ability traders to be allowed to spawn (Default is 148)")
                .define("Ability Trader Spawn Distance", 148);
        ABILITY_TRADER_PLAYER_SPAWNDIST = BUILDER.comment("The maximum distance from the player for the ability trader to be able to spawn (default value is 48)")
                .define("Ability Trader Player Spawn Distance", 48);

        UNDEAD_SENSEI_SPAWN_CHANCE = BUILDER.comment("The overall chance for the undead sensei to spawn (Default is 0.6)")
                .defineInRange("Undead Sensei Spawn Chance", 0.6, 0, 1);
        EXILED_SENSEI_SPAWN_CHANCE = BUILDER.comment("The overall chance for the exiled sensei to spawn (Default is 0.2)")
                .defineInRange("Exiled Sensei Spawn Chance", 0.2, 0, 1);
        EXILED_DEVIL_SPAWN_CHANCE = BUILDER.comment("The overall chance for the exiled devil to spawn (Default is 0.22)")
                .defineInRange("Exiled Devil Spawn Chance", 0.22, 0, 1);
        ABILITY_TRADER_SPAWN_CHANCE = BUILDER.comment("The overall chance for the ability trader to spawn (Default is 0.10)")
                .defineInRange("Ability Trader Spawn Chance", 0.10, 0, 1);
        BUILDER.pop();

        BUILDER.push("Ability Cooldowns");

        ABILITY1_COOLDOWN = BUILDER.comment("The cooldown(In Seconds) for 1st form abilites (Default is 30)")
                .defineInRange("Ability 1 Cooldown's", 30, 15, 120);
        ABILITY2_COOLDOWN = BUILDER.comment("The cooldown(In Seconds) for 2nd form abilities (Default is 45)")
                .defineInRange("Ability 2 Cooldown's", 45, 15, 180);
        ABILITY3_COOLDOWN = BUILDER.comment("The cooldown(In Seconds) for 3rd form abilities (Default is 60)")
                .defineInRange("Ability 3 Cooldown's", 60, 15, 240);
        ABILITY4_COOLDOWN = BUILDER.comment("The cooldown(In Seconds) for 4th form abilities (Default is 45)")
                .defineInRange("Ability 4 Cooldown's", 45, 15, 120);
        ABILITY5_COOLDOWN = BUILDER.comment("The cooldown(In Seconds) for 5th form abilities (Default is 30)")
                .defineInRange("Ability 5 Cooldown's", 30, 15, 120);
        BUILDER.pop();

        BUILDER.push("Ability Adjustments");

        TELEPORT_DIST = BUILDER.comment("The maximum distance(In Blocks) which you can teleport with the first void form ability (Default is 50)")
                .defineInRange("Teleport Max Distance", 50, 20, 180);
        EXPLOSION_DAMAGE = BUILDER.comment("The damage caused by the 5th wither form ability (Default is 6)")
                .defineInRange("Wither Explosion Damage", 6, 2, 20);
        MINION_COUNT = BUILDER.comment("The amount of minions able to be summoned at a time with the 4th wither form ability")
                .defineInRange("Minion Count", 2, 1, 4);
        ABILITY1_COOLDOWN_DIVISION = BUILDER.comment("The amount of cooldown that should be taken from ability 1 cooldown's aswell as the amount needed to use the ability(divided). eg. 2 will use half the bar, 3 will use a third of the bar etc.")
                .defineInRange("Ability 1 Cooldown Depletion/Requirement", 2, 1, 4);

        ABILITY2_COMBAT_MODE = BUILDER.comment("If true, the ability cooldowns for 2nd abilities will instead be filled by damaging enemies")
                .define("Ability 2 Combat Mode", false);
        ABILITY3_COMBAT_MODE = BUILDER.comment("If true, the ability cooldowns for 3rd abilities will instead be filled by damaging enemies")
                .define("Ability 3 Combat Mode", false);
        COMBAT_MODE_GAIN_AMOUNT = BUILDER.comment("if the above option/s are true(Ability Combat Mode), determine here how much of the cooldown is filled per hit of an enemy.")
                .defineInRange("Cooldown Gain Per Hit", 2, 1, 12);

        BUILDER.pop();

        BUILDER.push("Forms & Leveling");

        MAX_FORM_STARTING_XP = BUILDER.comment("The maximum amount of xp(Killed Monsters) required to level up to level 2 of a form (Default is 45)")
                .defineInRange("Maximum Level 1 Xp", 45, 2, 180);

        LEVEL2_FORM_MAX_XP_ADDITION = BUILDER.comment("The amount of maximum xp multiplied per level (Default value is 2)")
                .defineInRange("Multiplied Max Level Xp", 2, 2, 6);

        BUILDER.pop();
        BUILDER.push("Misc");
        CAN_LOCK_SLOT = BUILDER.comment("Whether there is a slot to be reserved for hand-to-hand combat")
                .define("Should Have Reserved Hand Combat Slot", true);

        FORM_LOCK_SLOT = BUILDER.comment("What slot the locked/form slot is set as(0 is first hotbar slot and 9 is last)")
                .defineInRange("Form Lock Slot Index", 0, 0, 9);

        CAN_USE_ABILITIES_NO_FORM = BUILDER.comment("if true, you are able to use your current chosen abilities regardless whether you are \"holding\" a combat form or not")
                .define("Can Use Abilities Without Form", true);

        ABILITY_SELECTION_MODE = BUILDER.comment("This allows you to use one key to activate a chosen ability, without using several keys")
                .define("Ability Selection Mode", true);
        BUILDER.pop();

        BUILDER.push("Compatibility");

        DIFFICULTY_COMPAT_MODE = BUILDER.comment("If true, will allow the entities from this mod to be weaker and more desirable for mods that increase difficulty over time/distance(Improved Mobs, RpgDifficulty etc.)[Please not this only changes default health values, and only for 2 of the sensei's]")
                .define("Dynamic Difficulty Compat Mode", false);

        COMPAT_MODE_UNDEAD_SENSEI_HEALTH = BUILDER.comment("How much health the \"Undead Sensei\" should start with if dynamic difficulty combat is enabled")
                .define("Compat Mode Undead Sensei Health", 12);
        COMPAT_MODE_EXILED_DEVIL_HEALTH = BUILDER.comment("How much health the \"Exiled Devil\" should start with if dynamic difficulty combat is enabled")
                .define("Compat Mode Exiled Devil Health", 18);



        BUILDER.pop();

        BUILDER.push("Form Damage & Speed");

        BASIC_FORM_BASE_DAMAGE = BUILDER.comment("The base damage for the 'Basic Form'")
                .defineInRange("Basic Form Base Damage", 3, 1, 20);
        SWIFT_FORM_BASE_DAMAGE = BUILDER.comment("The base damage for the 'Swift Form'")
                .defineInRange("Swift Form Base Damage", 4, 1, 24);
        POWER_FORM_BASE_DAMAGE = BUILDER.comment("The base damage for the 'Power Form'")
                .defineInRange("Power Form Base Damage", 5, 1, 28);
        VOID_FORM_BASE_DAMAGE = BUILDER.comment("The base damage for the 'Void Form'")
                .defineInRange("Void Form Base Damage", 6, 1, 30);
        WITHER_FORM_BASE_DAMAGE = BUILDER.comment("The base damage for the 'Wither Form'")
                .defineInRange("Wither Form Base Damage", 7, 1, 32);

        LEVEL1_DAMAGE_MULTIPLIER = BUILDER.comment("This defines the amount of damage any Level 1 form is multiplied by")
                .defineInRange("Level 1 Damage Multiplier", 1.0, 1.0,1.0);

        LEVEL2_DAMAGE_MULTIPLIER = BUILDER.comment("This defines the amount of damage any Level 2 form is multiplied by")
                .defineInRange("Level 2 Damage Multiplier", 1.4, 1.0, 6.0);
        LEVEL3_DAMAGE_MULTIPLIER = BUILDER.comment("This defines the amount of damage any Level 2 form is multiplied by")
                .defineInRange("Level 3 Damage Multiplier", 1.6, 1.0, 8.0);

        BASIC_FORM_BASE_SPEED = BUILDER.comment("The base attack speed for the 'Basic Form'")
                .defineInRange("Basic Form Base Attack Speed", 0.6, 0.1, 15);

        SWIFT_FORM_BASE_SPEED = BUILDER.comment("The base attack speed for the 'Swift Form'")
                .defineInRange("Swift Form Base Attack Speed", 0.9, 0.1, 15);

        POWER_FORM_BASE_SPEED = BUILDER.comment("The base attack speed for the 'Power Form'")
                .defineInRange("Power Form Base Attack Speed", 0.5, 0.1, 15);

        VOID_FORM_BASE_SPEED = BUILDER.comment("The base attack speed for the 'Void Form'")
                .defineInRange("Void Form Base Attack Speed", 0.68, 0.1, 15);

        WITHER_FORM_BASE_SPEED = BUILDER.comment("The base attack speed for the 'Wither Form'")
                .defineInRange("Wither Form Base Attack Speed", 0.7, 0.1, 15);

        LEVEL1_SPEED_MULTIPLIER = BUILDER.comment("This defines the amount of attack speed any Level 1 form is multiplied by")
                .defineInRange("Level 1 Speed Multiplier", 1.18, 1,5.0);

        LEVEL2_SPEED_MULTIPLIER = BUILDER.comment("This defines the amount of attack speed any Level 2 form is multiplied by")
                .defineInRange("Level 2 Speed Multiplier", 1.25, 1,5.0);

        LEVEL3_SPEED_MULTIPLIER = BUILDER.comment("This defines the amount of attack speed any Level 3 form is multiplied by")
                .defineInRange("Level 3 Speed Multiplier", 1.3, 1,5.0);

        BUILDER.pop();

        SPEC = BUILDER.build();


    }

    public static Item getSenseiTameItem() {
        // Retrieve the registry name from the configuration
        String itemName = SENSEI_TAME_ITEM.get();

        // Convert the string to a ResourceLocation
        ResourceLocation itemResourceLocation = new ResourceLocation(itemName);

        // Use the registry to get the actual Item object
        Item tameItem = ForgeRegistries.ITEMS.getValue(itemResourceLocation);

        // Check if the item was found
        if (tameItem == null) {
            throw new IllegalArgumentException("Item not found in registry: " + itemName);
        }

        return tameItem;
    }
}
