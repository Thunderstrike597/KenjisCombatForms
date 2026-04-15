package net.kenji.kenjiscombatforms.gameasset;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.basegameassets.skills.BaseComboBuilder;
import net.kenji.kenjiscombatforms.gameasset.skills.combo_skills.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jline.utils.Log;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;

import java.util.ArrayList;
import java.util.List;
@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSkills {
    public static List<Skill> skills = new ArrayList<>();

    public static Skill BASIC_COMBO_1;
    public static Skill BASIC_COMBO_2;
    public static Skill BASIC_COMBO_3;

    public static Skill SWIFT_COMBO_1;
    public static Skill SWIFT_COMBO_2;
    public static Skill SWIFT_COMBO_3;

    public static Skill POWER_COMBO_1;
    public static Skill POWER_COMBO_2;
    public static Skill POWER_COMBO_3;

    public static Skill VOID_COMBO_1;
    public static Skill VOID_COMBO_2;
    public static Skill VOID_COMBO_3;

    public static Skill WITHER_COMBO_1;
    public static Skill WITHER_COMBO_2;
    public static Skill WITHER_COMBO_3;
    @SubscribeEvent
    public static void buildSkillEvent(SkillBuildEvent build){
        SkillBuildEvent.ModRegistryWorker modRegistry = build.createRegistryWorker(KenjisCombatForms.MOD_ID);

        skills.add(BASIC_COMBO_1 = BasicFistCombos.buildSkills("basic_fist_1", modRegistry, BaseComboBuilder.FistTier.TIER_1));
        skills.add(BASIC_COMBO_2 = BasicFistCombos.buildSkills("basic_fist_2", modRegistry, BaseComboBuilder.FistTier.TIER_2));
        skills.add(BASIC_COMBO_3 = BasicFistCombos.buildSkills("basic_fist_3", modRegistry, BaseComboBuilder.FistTier.TIER_3));

        skills.add(SWIFT_COMBO_1 = SwiftFistCombos.buildSkills("swift_fist_1", modRegistry, BaseComboBuilder.FistTier.TIER_1));
        skills.add(SWIFT_COMBO_2 = SwiftFistCombos.buildSkills("swift_fist_2", modRegistry, BaseComboBuilder.FistTier.TIER_2));
        skills.add(SWIFT_COMBO_3 = SwiftFistCombos.buildSkills("swift_fist_3", modRegistry, BaseComboBuilder.FistTier.TIER_3));

        skills.add(POWER_COMBO_1 = PowerFistCombos.buildSkills("power_fist_1", modRegistry, BaseComboBuilder.FistTier.TIER_1));
        skills.add(POWER_COMBO_2 = PowerFistCombos.buildSkills("power_fist_2", modRegistry, BaseComboBuilder.FistTier.TIER_2));
        skills.add(POWER_COMBO_3 = PowerFistCombos.buildSkills("power_fist_3", modRegistry, BaseComboBuilder.FistTier.TIER_3));

        skills.add(VOID_COMBO_1 = VoidFistCombos.buildSkills("void_fist_1", modRegistry, BaseComboBuilder.FistTier.TIER_1));
        skills.add(VOID_COMBO_2 = VoidFistCombos.buildSkills("void_fist_2", modRegistry, BaseComboBuilder.FistTier.TIER_2));
        skills.add(VOID_COMBO_3 = VoidFistCombos.buildSkills("void_fist_3", modRegistry, BaseComboBuilder.FistTier.TIER_3));

        skills.add(WITHER_COMBO_1 = WitherFistCombos.buildSkills("wither_fist_1", modRegistry, BaseComboBuilder.FistTier.TIER_1));
        skills.add(WITHER_COMBO_2 = WitherFistCombos.buildSkills("wither_fist_2", modRegistry, BaseComboBuilder.FistTier.TIER_2));
        skills.add(WITHER_COMBO_3 = WitherFistCombos.buildSkills("wither_fist_3", modRegistry, BaseComboBuilder.FistTier.TIER_3));
    }
}
