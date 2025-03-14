package net.kenji.kenjiscombatforms.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class KenjisCombatFormsClient {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> HIDE_BASIC_LEVEL_GUI;
    public static final ForgeConfigSpec.ConfigValue<Boolean> HIDE_ALL_LEVEL_GUI;

    public static final ForgeConfigSpec.ConfigValue<Boolean> HIDE_ABILITY_BARS_FIRST_PERSON;
    public static final ForgeConfigSpec.ConfigValue<Boolean> HIDE_ABILITY_BARS;

    public static final ForgeConfigSpec.ConfigValue<Boolean> TRANSLUCENT_GUI;
    public static final ForgeConfigSpec.ConfigValue<Double> GUI_TRANSLUCENT_AMOUNT;

    static {
        BUILDER.push("Gui Configs");

        HIDE_BASIC_LEVEL_GUI = BUILDER
                .comment("If true, the displayed level gui will always be hidden")
                .define("hide basic form level gui", false);
        HIDE_ALL_LEVEL_GUI = BUILDER
                .comment("If true, the displayed level for ALL forms will be hidden")
                .define("hide all form level gui", false);



        HIDE_ABILITY_BARS_FIRST_PERSON = BUILDER
                .comment("If true, hides ability bars when in first person")
                .define("hide ability bars, first person", false);
        HIDE_ABILITY_BARS = BUILDER
                .comment("If true, hides ability bars when not in hand combat mode")
                .define("hide ability bars", false);

        TRANSLUCENT_GUI = BUILDER
                .comment("If true, most gui such as text and f3 menu, will be translucent. Why is this an option? This was originally a bug, so I made it a config option :D")
                .define("translucent vanilla gui", false);

        GUI_TRANSLUCENT_AMOUNT = BUILDER
                .comment("Want to adjust the above option? Well you can! This will adjust the amount of transparency vanilla gui will have, if translucent gui is set to true!")
                .defineInRange("translucent amount", 0.5, 0.0, 1.0);



        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
