package net.kenji.kenjiscombatforms.gameasset.skills;

import net.minecraft.world.InteractionHand;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

public class KaishuGuardSkill extends GuardSkill {

    public KaishuGuardSkill(Builder builder) {
        super(builder);
        guardMotions.clear();
        guardMotions.put(
                CapabilityItem.WeaponCategories.FIST,
                (item, player) -> {
                    return item.getLivingMotionModifier(player, InteractionHand.MAIN_HAND).get(LivingMotions.BLOCK);
                }
        );
    }


}
