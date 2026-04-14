package net.kenji.kenjiscombatforms.gameasset;

import com.mojang.datafixers.util.Pair;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFistItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.gameasset.animations.weapons.*;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCapabilityPresets;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

import java.util.UUID;
import java.util.function.Function;

public class CombatFistCapabilityPresets {

    public static final Function<Item, CapabilityItem.Builder> BASIC_FIST_TIER1 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.FIST)
                .styleProvider((playerPatch) -> {
                    return CapabilityItem.Styles.COMMON;
                })
                .newStyleCombo(CapabilityItem.Styles.COMMON,
                        Animations.DAGGER_DUAL_AUTO1,
                        Animations.DAGGER_DUAL_AUTO1,
                        Animations.DAGGER_DUAL_AUTO2,
                        Animations.SPEAR_DASH, WOMAnimations.STAFF_KINKONG
                        )
                .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.IDLE, Animations.BIPED_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.WALK, Animations.BIPED_WALK)
                .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.RUN, Animations.BIPED_RUN)

                .swingSound(EpicFightSounds.WHOOSH.get())
                .hitSound(EpicFightSounds.BLUNT_HIT.get())
                .collider(ColliderPreset.DAGGER)
                .addStyleAttibutes(CapabilityItem.Styles.COMMON, Pair.of((Attribute) Attributes.ATTACK_SPEED, EpicFightAttributes.getSpeedBonusModifier(
                                0)
                ))
                .innateSkill(CapabilityItem.Styles.COMMON, itemStack -> ModSkills.BASIC_COMBO_1);

        return builder;

    };
    public static final Function<Item, CapabilityItem.Builder> BASIC_FIST_TIER2 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.FIST)
                .styleProvider((playerPatch) -> {
                    return CapabilityItem.Styles.COMMON;
                })
                .newStyleCombo(CapabilityItem.Styles.COMMON,
                        Animations.DAGGER_DUAL_AUTO1,
                        Animations.DAGGER_DUAL_AUTO1,
                        Animations.DAGGER_DUAL_AUTO2,
                        Animations.SPEAR_DASH, WOMAnimations.STAFF_KINKONG
                )
                .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.IDLE, Animations.BIPED_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.WALK, Animations.BIPED_WALK)
                .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.RUN, Animations.BIPED_RUN)
                .swingSound(EpicFightSounds.WHOOSH.get())
                .hitSound(EpicFightSounds.BLUNT_HIT.get())
                .collider(ColliderPreset.DAGGER)
                .addStyleAttibutes(CapabilityItem.Styles.COMMON, Pair.of((Attribute) Attributes.ATTACK_SPEED, EpicFightAttributes.getSpeedBonusModifier(
                        0)                ))
                .innateSkill(CapabilityItem.Styles.COMMON, itemStack -> ModSkills.BASIC_COMBO_2);

        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> BASIC_FIST_TIER3 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.FIST)
                .styleProvider((playerPatch) -> {
                    return CapabilityItem.Styles.COMMON;
                })
                .newStyleCombo(CapabilityItem.Styles.COMMON,
                        Animations.DAGGER_DUAL_AUTO1,
                        Animations.DAGGER_DUAL_AUTO1,
                        Animations.DAGGER_DUAL_AUTO2,
                        Animations.SPEAR_DASH, WOMAnimations.STAFF_KINKONG
                )
                .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.IDLE, Animations.BIPED_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.WALK, Animations.BIPED_WALK)
                .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.RUN, Animations.BIPED_RUN)
                .swingSound(EpicFightSounds.WHOOSH.get())
                .hitSound(EpicFightSounds.BLUNT_HIT.get())
                .collider(ColliderPreset.DAGGER)
                .addStyleAttibutes(CapabilityItem.Styles.COMMON, Pair.of((Attribute) Attributes.ATTACK_SPEED, EpicFightAttributes.getSpeedBonusModifier(
                        0)
                ))
                .innateSkill(CapabilityItem.Styles.COMMON, itemStack -> ModSkills.BASIC_COMBO_3);
        return builder;
    };

    public static final Function<Item, CapabilityItem.Builder> POWER_FORM_1 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.FIST)
                .styleProvider((playerPatch) -> {
                    if(playerPatch instanceof PlayerPatch<?> patch){
                        if(patch.getSkill(EpicFightSkills.LIECHTENAUER) != null){
                            if(patch.getSkill(EpicFightSkills.LIECHTENAUER).isActivated())
                                return CapabilityItem.Styles.OCHS;
                        }
                    }
                    return CapabilityItem.Styles.TWO_HAND;
                })
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        Animations.SPEAR_DASH,
                        Animations.SPEAR_DASH,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsMoonless.MOONLESS_AUTO_3,
                        AnimsMoonless.MOONLESS_AUTO_1, AnimsMoonless.MOONLESS_CRESCENT
                )
                .newStyleCombo(CapabilityItem.Styles.OCHS,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsAgony.AGONY_CLAWSTRIKE,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsHerrscher.HERRSCHER_VERDAMMNIS,
                        AnimsMoonless.MOONLESS_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsMoonless.MOONLESS_CRESCENT,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH, AnimsAgony.AGONY_AIR_ATTACK_1
                        )
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, WOMAnimations.STAFF_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, AnimsEnderblaster.ENDERBLASTER_ONEHAND_WALK)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, WOMAnimations.STAFF_RUN)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.IDLE, WOMAnimations.STAFF_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.WALK, AnimsEnderblaster.ENDERBLASTER_ONEHAND_WALK)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.RUN, WOMAnimations.STAFF_RUN)

                .swingSound(EpicFightSounds.WHOOSH.get())
                .hitSound(EpicFightSounds.BLUNT_HIT.get())
                .collider(ColliderPreset.DAGGER)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, itemStack -> ModSkills.POWER_COMBO_1);
        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> POWER_FORM_2 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.FIST)
                .styleProvider((playerPatch) -> {
                    if(playerPatch instanceof PlayerPatch<?> patch){
                        if(patch.getSkill(EpicFightSkills.LIECHTENAUER) != null){
                            if(patch.getSkill(EpicFightSkills.LIECHTENAUER).isActivated())
                                return CapabilityItem.Styles.OCHS;
                        }
                    }
                    return CapabilityItem.Styles.TWO_HAND;
                })
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        Animations.SPEAR_DASH,
                        Animations.SPEAR_DASH,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsMoonless.MOONLESS_AUTO_3,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsMoonless.MOONLESS_AUTO_1, AnimsMoonless.MOONLESS_CRESCENT
                )
                .newStyleCombo(CapabilityItem.Styles.OCHS,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsAgony.AGONY_CLAWSTRIKE,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsHerrscher.HERRSCHER_VERDAMMNIS,
                        AnimsMoonless.MOONLESS_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsMoonless.MOONLESS_CRESCENT,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsMoonless.MOONLESS_CRESCENT,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH, AnimsAgony.AGONY_AIR_ATTACK_1
                )
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, WOMAnimations.STAFF_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, AnimsEnderblaster.ENDERBLASTER_ONEHAND_WALK)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, WOMAnimations.STAFF_RUN)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.IDLE, WOMAnimations.STAFF_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.WALK, AnimsEnderblaster.ENDERBLASTER_ONEHAND_WALK)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.RUN, WOMAnimations.STAFF_RUN)

                .swingSound(EpicFightSounds.WHOOSH.get())
                .hitSound(EpicFightSounds.BLUNT_HIT.get())
                .collider(ColliderPreset.DAGGER)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, itemStack -> ModSkills.POWER_COMBO_2);
        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> POWER_FORM_3 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.FIST)
                .styleProvider((playerPatch) -> {
                    if(playerPatch instanceof PlayerPatch<?> patch){
                        if(patch.getSkill(EpicFightSkills.LIECHTENAUER) != null){
                            if(patch.getSkill(EpicFightSkills.LIECHTENAUER).isActivated())
                                return CapabilityItem.Styles.OCHS;
                        }
                    }
                    return CapabilityItem.Styles.TWO_HAND;
                })
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        Animations.SPEAR_DASH,
                        Animations.SPEAR_DASH,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsMoonless.MOONLESS_AUTO_3,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsMoonless.MOONLESS_AUTO_3,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsMoonless.MOONLESS_AUTO_1, AnimsMoonless.MOONLESS_CRESCENT
                )
                .newStyleCombo(CapabilityItem.Styles.OCHS,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsAgony.AGONY_CLAWSTRIKE,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsHerrscher.HERRSCHER_VERDAMMNIS,
                        AnimsMoonless.MOONLESS_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsMoonless.MOONLESS_CRESCENT,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsMoonless.MOONLESS_CRESCENT,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH, AnimsAgony.AGONY_AIR_ATTACK_1
                )
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, WOMAnimations.STAFF_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, AnimsEnderblaster.ENDERBLASTER_ONEHAND_WALK)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, WOMAnimations.STAFF_RUN)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.IDLE, WOMAnimations.STAFF_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.WALK, AnimsEnderblaster.ENDERBLASTER_ONEHAND_WALK)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.RUN, WOMAnimations.STAFF_RUN)

                .swingSound(EpicFightSounds.WHOOSH.get())
                .hitSound(EpicFightSounds.BLUNT_HIT.get())
                .collider(ColliderPreset.DAGGER)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, itemStack -> ModSkills.POWER_COMBO_3);

        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> SWIFT_FORM_1 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.FIST)
                .styleProvider((playerPatch) -> {
                    if(playerPatch instanceof PlayerPatch<?> patch){
                        if(patch.getSkill(EpicFightSkills.LIECHTENAUER) != null){
                            if(patch.getSkill(EpicFightSkills.LIECHTENAUER).isActivated())
                                return CapabilityItem.Styles.OCHS;
                        }
                    }
                    return CapabilityItem.Styles.TWO_HAND;
                })
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        Animations.DAGGER_DUAL_AUTO1,
                        Animations.DAGGER_DUAL_AUTO1,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_4,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH, AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_4
                        )
                .newStyleCombo(CapabilityItem.Styles.OCHS,
                      AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_2_VERSO,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_4,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH, AnimsAgony.AGONY_AIR_ATTACK_1
                )
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, AnimsHerrscher.HERRSCHER_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, AnimsMoonless.MOONLESS_WALK)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, AnimsMoonless.MOONLESS_RUN)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.IDLE, AnimsHerrscher.HERRSCHER_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.WALK, AnimsMoonless.MOONLESS_WALK)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.RUN, AnimsSatsujin.SATSUJIN_RUN)

                .swingSound(EpicFightSounds.WHOOSH.get())
                .hitSound(EpicFightSounds.BLUNT_HIT.get())
                .collider(ColliderPreset.DAGGER)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, itemStack -> ModSkills.SWIFT_COMBO_1);

        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> SWIFT_FORM_2 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.FIST)
                .styleProvider((playerPatch) -> {
                    if(playerPatch instanceof PlayerPatch<?> patch){
                        if(patch.getSkill(EpicFightSkills.LIECHTENAUER) != null){
                            if(patch.getSkill(EpicFightSkills.LIECHTENAUER).isActivated())
                                return CapabilityItem.Styles.OCHS;
                        }
                    }
                    return CapabilityItem.Styles.TWO_HAND;
                })
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        Animations.DAGGER_DUAL_AUTO1,
                        Animations.DAGGER_DUAL_AUTO1,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH, AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_4
                )
                .newStyleCombo(CapabilityItem.Styles.OCHS,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_2_VERSO,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_4,
                        AnimsMoonless.MOONLESS_AUTO_2_VERSO,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH, AnimsAgony.AGONY_AIR_ATTACK_1
                )
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, AnimsHerrscher.HERRSCHER_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, AnimsMoonless.MOONLESS_WALK)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, AnimsMoonless.MOONLESS_RUN)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.IDLE, AnimsHerrscher.HERRSCHER_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.WALK, AnimsMoonless.MOONLESS_WALK)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.RUN, AnimsSatsujin.SATSUJIN_RUN)

                .swingSound(EpicFightSounds.WHOOSH.get())
                .hitSound(EpicFightSounds.BLUNT_HIT.get())
                .collider(ColliderPreset.DAGGER)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, itemStack -> ModSkills.SWIFT_COMBO_2);

        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> SWIFT_FORM_3 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.FIST)
                .styleProvider((playerPatch) -> {
                    if(playerPatch instanceof PlayerPatch<?> patch){
                        if(patch.getSkill(EpicFightSkills.LIECHTENAUER) != null){
                            if(patch.getSkill(EpicFightSkills.LIECHTENAUER).isActivated())
                                return CapabilityItem.Styles.OCHS;
                        }
                    }
                    return CapabilityItem.Styles.TWO_HAND;
                })
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        Animations.DAGGER_DUAL_AUTO1,
                        Animations.DAGGER_DUAL_AUTO1,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        Animations.SPEAR_DASH,
                        Animations.SPEAR_DASH,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsMoonless.MOONLESS_AUTO_3,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH, AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_4
                )
                .newStyleCombo(CapabilityItem.Styles.OCHS,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_2_VERSO,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_4,
                        AnimsMoonless.MOONLESS_AUTO_2_VERSO,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        Animations.SPEAR_DASH,
                        Animations.SPEAR_DASH,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsMoonless.MOONLESS_AUTO_3,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_4,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH, AnimsAgony.AGONY_AIR_ATTACK_1
                )
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, AnimsHerrscher.HERRSCHER_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, AnimsMoonless.MOONLESS_WALK)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, AnimsMoonless.MOONLESS_RUN)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.IDLE, AnimsHerrscher.HERRSCHER_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.WALK, AnimsMoonless.MOONLESS_WALK)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.RUN, AnimsSatsujin.SATSUJIN_RUN)

                .swingSound(EpicFightSounds.WHOOSH.get())
                .hitSound(EpicFightSounds.BLUNT_HIT.get())
                .collider(ColliderPreset.DAGGER)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, itemStack -> ModSkills.SWIFT_COMBO_3);
        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> VOID_FORM_1 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.FIST)
                .styleProvider((playerPatch) -> {
                    return CapabilityItem.Styles.TWO_HAND;
                })
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH,
                        WOMAnimations.STAFF_AUTO_2,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_3, AnimsSatsujin.SATSUJIN_TSUKUYOMI
                )
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, AnimsAgony.AGONY_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, AnimsAgony.AGONY_WALK)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, AnimsAgony.AGONY_RUN)
                .swingSound(EpicFightSounds.WHOOSH.get())
                .hitSound(EpicFightSounds.BLUNT_HIT.get())
                .collider(ColliderPreset.DAGGER)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, itemStack -> ModSkills.VOID_COMBO_1);

        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> VOID_FORM_2 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.FIST)
                .styleProvider((playerPatch) -> {
                    return CapabilityItem.Styles.TWO_HAND;
                })
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH,
                        WOMAnimations.STAFF_AUTO_2,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_3,
                        AnimsMoonless.MOONLESS_AUTO_3,  AnimsEnderblaster.ENDERBLASTER_TWOHAND_AIRSHOOT
                )
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, AnimsAgony.AGONY_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, AnimsAgony.AGONY_WALK)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, AnimsAgony.AGONY_RUN)
                .swingSound(EpicFightSounds.WHOOSH.get())
                .hitSound(EpicFightSounds.BLUNT_HIT.get())
                .collider(ColliderPreset.DAGGER)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, itemStack -> ModSkills.VOID_COMBO_2);
        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> VOID_FORM_3 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.FIST)
                .styleProvider((playerPatch) -> {
                    return CapabilityItem.Styles.TWO_HAND;
                })
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH,
                        WOMAnimations.STAFF_AUTO_2,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_3,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_SHOOT_3,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_SHOOT_DASH, AnimsEnderblaster.ENDERBLASTER_TWOHAND_AIRSHOOT
                )
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, AnimsAgony.AGONY_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, AnimsAgony.AGONY_WALK)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, AnimsAgony.AGONY_RUN)
                .swingSound(EpicFightSounds.WHOOSH.get())
                .hitSound(EpicFightSounds.BLUNT_HIT.get())
                .collider(ColliderPreset.DAGGER)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, itemStack -> ModSkills.VOID_COMBO_3);
        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> WITHER_FORM_1 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.FIST)
                .styleProvider((playerPatch) -> {
                    if(playerPatch instanceof PlayerPatch<?> patch){
                        if(patch.getSkill(EpicFightSkills.LIECHTENAUER) != null){
                            if(patch.getSkill(EpicFightSkills.LIECHTENAUER).isActivated())
                                return CapabilityItem.Styles.OCHS;
                        }
                    }
                    return CapabilityItem.Styles.TWO_HAND;
                })
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        AnimsMoonless.MOONLESS_AUTO_1_VERSO,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_4,

                        AnimsMoonless.MOONLESS_REWINDER, AnimsEnderblaster.ENDERBLASTER_TWOHAND_TISHNAW
                )
                .newStyleCombo(CapabilityItem.Styles.OCHS,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_3,
                        AnimsMoonless.MOONLESS_AUTO_2_VERSO,
                        AnimsMoonless.MOONLESS_REWINDER,
                        AnimsAgony.AGONY_CLAWSTRIKE,
                        WOMAnimations.STAFF_AUTO_2,

                        AnimsMoonless.MOONLESS_REWINDER, AnimsEnderblaster.ENDERBLASTER_TWOHAND_TISHNAW
                        )
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, AnimsSatsujin.SATSUJIN_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, AnimsEnderblaster.ENDERBLASTER_ONEHAND_WALK)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.IDLE, AnimsSolar.SOLAR_OBSCURIDAD_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.WALK, WOMAnimations.ANTITHEUS_ASCENDED_WALK)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.RUN, WOMAnimations.TORMENT_BERSERK_RUN)
                .swingSound(EpicFightSounds.WHOOSH.get())
                .hitSound(EpicFightSounds.BLUNT_HIT.get())
                .collider(ColliderPreset.DAGGER)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, itemStack -> ModSkills.WITHER_COMBO_1);


        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> WITHER_FORM_2 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.FIST)
                .styleProvider((playerPatch) -> {
                    if(playerPatch instanceof PlayerPatch<?> patch){
                        if(patch.getSkill(EpicFightSkills.LIECHTENAUER) != null){
                            if(patch.getSkill(EpicFightSkills.LIECHTENAUER).isActivated())
                                return CapabilityItem.Styles.OCHS;
                        }
                    }
                    return CapabilityItem.Styles.TWO_HAND;
                })
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        AnimsMoonless.MOONLESS_AUTO_1_VERSO,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_4,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsMoonless.MOONLESS_AUTO_3,

                        AnimsMoonless.MOONLESS_REWINDER, AnimsEnderblaster.ENDERBLASTER_TWOHAND_TISHNAW
                )
                .newStyleCombo(CapabilityItem.Styles.OCHS,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_3,
                        AnimsMoonless.MOONLESS_AUTO_2_VERSO,
                        AnimsMoonless.MOONLESS_REWINDER,
                        AnimsAgony.AGONY_CLAWSTRIKE,
                        WOMAnimations.STAFF_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsAgony.AGONY_AIR_ATTACK_1,

                        AnimsMoonless.MOONLESS_REWINDER, AnimsEnderblaster.ENDERBLASTER_TWOHAND_TISHNAW
                )
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, AnimsSatsujin.SATSUJIN_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, AnimsEnderblaster.ENDERBLASTER_ONEHAND_WALK)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.IDLE, AnimsSolar.SOLAR_OBSCURIDAD_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.WALK, WOMAnimations.ANTITHEUS_ASCENDED_WALK)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.RUN, WOMAnimations.TORMENT_BERSERK_RUN)
                .swingSound(EpicFightSounds.WHOOSH.get())
                .hitSound(EpicFightSounds.BLUNT_HIT.get())
                .collider(ColliderPreset.DAGGER)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, itemStack -> ModSkills.WITHER_COMBO_2);

        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> WITHER_FORM_3 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.FIST)
                .styleProvider((playerPatch) -> {
                    if(playerPatch instanceof PlayerPatch<?> patch){
                        if(patch.getSkill(EpicFightSkills.LIECHTENAUER) != null){
                            if(patch.getSkill(EpicFightSkills.LIECHTENAUER).isActivated())
                                return CapabilityItem.Styles.OCHS;
                        }
                    }
                    return CapabilityItem.Styles.TWO_HAND;
                })
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        AnimsMoonless.MOONLESS_AUTO_1_VERSO,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_4,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsAgony.AGONY_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_3_VERSO,
                        AnimsMoonless.MOONLESS_AUTO_3,
                        AnimsAgony.AGONY_AUTO_1,
                        WOMAnimations.STAFF_AUTO_2,
                        AnimsAgony.AGONY_AUTO_1,
                        WOMAnimations.STAFF_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsAgony.AGONY_AIR_ATTACK_1,

                        AnimsMoonless.MOONLESS_REWINDER, AnimsEnderblaster.ENDERBLASTER_TWOHAND_TISHNAW
                )
                .newStyleCombo(CapabilityItem.Styles.OCHS,
                        AnimsAgony.AGONY_AUTO_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_1,
                        AnimsMoonless.MOONLESS_AUTO_3,
                        AnimsMoonless.MOONLESS_AUTO_2_VERSO,
                        AnimsMoonless.MOONLESS_REWINDER,
                        AnimsAgony.AGONY_CLAWSTRIKE,
                        WOMAnimations.STAFF_AUTO_2,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsAgony.AGONY_AIR_ATTACK_1,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH,
                        AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_2,
                        AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_3,
                        AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_3,
                        AnimsMoonless.MOONLESS_AUTO_2,
                        AnimsAgony.AGONY_AUTO_1,

                        AnimsMoonless.MOONLESS_REWINDER, AnimsEnderblaster.ENDERBLASTER_TWOHAND_TISHNAW
                )
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, AnimsSatsujin.SATSUJIN_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, AnimsEnderblaster.ENDERBLASTER_ONEHAND_WALK)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.IDLE, AnimsSolar.SOLAR_OBSCURIDAD_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.WALK, WOMAnimations.ANTITHEUS_ASCENDED_WALK)
                .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.RUN, WOMAnimations.TORMENT_BERSERK_RUN)
                .swingSound(EpicFightSounds.WHOOSH.get())
                .hitSound(EpicFightSounds.BLUNT_HIT.get())
                .collider(ColliderPreset.DAGGER)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, itemStack -> ModSkills.WITHER_COMBO_3);
        return builder;
    };
}
