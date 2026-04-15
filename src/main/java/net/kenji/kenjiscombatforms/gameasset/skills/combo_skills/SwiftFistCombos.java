package net.kenji.kenjiscombatforms.gameasset.skills.combo_skills;

import com.p1nero.invincible.api.events.BaseEvent;
import com.p1nero.invincible.api.events.TimeStampedEvent;
import com.p1nero.invincible.api.skill.ComboNode;
import com.p1nero.invincible.client.InputManager;
import com.p1nero.invincible.client.particles.InvincibleParticles;
import com.p1nero.invincible.conditions.*;
import com.p1nero.invincible.skill.ComboBasicAttack;
import net.kenji.kenjiscombatforms.api.basegameassets.condition.CooldownCounterCondition;
import net.kenji.kenjiscombatforms.api.basegameassets.condition.FormLevelCondition;
import net.kenji.kenjiscombatforms.api.basegameassets.condition.InAirCondition;
import net.kenji.kenjiscombatforms.api.basegameassets.skills.BaseComboBuilder;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import org.jline.utils.Log;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.gameasset.animations.weapons.AnimsAgony;
import reascer.wom.gameasset.animations.weapons.AnimsEnderblaster;
import reascer.wom.gameasset.animations.weapons.AnimsMoonless;
import reascer.wom.gameasset.animations.weapons.AnimsSatsujin;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.api.utils.TimePairList;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.damagesource.StunType;

public class SwiftFistCombos extends BaseComboBuilder {

    public static Skill buildSkills(String skillName, SkillBuildEvent.ModRegistryWorker registryWorker, FistTier tier) {

        ComboNode root = ComboNode.create();
        ComboNode basicAttack = createComboNode(Animations.DAGGER_DUAL_AUTO1)
                .setStunTypeModifier(StunType.HOLD)
                .setDamageMultiplier(ValueModifier.multiplier(0.5F))
                .setCanBeInterrupt(false);


        /// Tier 1
        ComboNode basic1 = createComboNode(Animations.DAGGER_AUTO1);
        ComboNode basic2 = createComboNode(AnimsAgony.AGONY_COUNTER);
        ComboNode basic3 = createComboNode(AnimsMoonless.MOONLESS_AUTO_3_VERSO);
        ComboNode basic4 = createComboNode(AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_1, 1.5F);
        ComboNode basic6 = createComboNode(AnimsAgony.AGONY_AUTO_1);

        ComboNode basicLeft1 = createComboNode(WOMAnimations.KICK_AUTO_2, 1.5F);
        ComboNode basicLeft2 = createComboNode(WOMAnimations.KICK_AUTO_3, 1.75F);
        ComboNode basicLeft3 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1, 1.5F);
        ComboNode basicLeft4 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3, 1.5F);

        ComboNode basicRight1 = createComboNode(WOMAnimations.KICK_AUTO_1).setPlaySpeed(1.75F);
        ComboNode basicRight2 = createComboNode(WOMAnimations.KICK_AUTO_3).setPlaySpeed(1.75F);
        ComboNode basicRight3 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1, 1.5F);
        ComboNode basicRight4 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_2, 1.5F);

        /// Tier 2
        ComboNode basic7 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3);
        ComboNode basic8 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3, 1.5F);
        ComboNode basic9 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH, 1.5F);
        ComboNode basic5 = createComboNode(AnimsMoonless.MOONLESS_AUTO_3, 1.5F);

        /// Tier 3
        ComboNode basic10 = createComboNode(Animations.SPEAR_DASH, 1.5F);
        ComboNode basic11 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3, 1.5F);
        ComboNode basic12 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1, 1.5F);
        ComboNode basic13 = createComboNode(AnimsMoonless.MOONLESS_AUTO_3_VERSO);
        ComboNode basic14 = createComboNode(AnimsMoonless.MOONLESS_AUTO_3);

        ComboNode dash = switch (tier.value) {
            default -> createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH).addCondition(new SprintingCondition());
        };
        ComboNode airSlash = switch (tier.value) {
            default -> createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_4).addCondition(new InAirCondition());
        };
        ComboNode leftDodgeAttack = createDodgeComboNode(Animations.BIPED_STEP_LEFT, basicLeft1).setPlaySpeed(1.35F).addCondition(new LeftCondition());
        leftDodgeAttack.addCondition(new CooldownCounterCondition(leftDodgeAttack, 60));

        ComboNode rightDodgeAttack = createDodgeComboNode(Animations.BIPED_STEP_RIGHT, basicRight1).setPlaySpeed(1.35F).addCondition(new RightCondition());
        rightDodgeAttack.addCondition(new CooldownCounterCondition(leftDodgeAttack, 60));

        ComboNode downDodgeAttack = createDodgeComboNode(Animations.BIPED_STEP_BACKWARD, basicRight1).setPlaySpeed(1.35F).addCondition(new DownCondition());
        downDodgeAttack.addCondition(new CooldownCounterCondition(downDodgeAttack, 60));


        createMovementCombo(root, basicAttack, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash));


        ComboNode rootDecision = createMovementCombo(basicAttack, basic1, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash));


        createMovementCombo(basic1, basic2, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash));
        createMovementCombo(basic2, basic3, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash));
        createMovementCombo(basic3, basic4, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash));
        createMovementCombo(basic5, basic6, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash));

        createMovementCombo(basicLeft1, basicLeft2, new ComboNodeWrapper(rightDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash));
        createMovementCombo(basicLeft2, basicLeft3, new ComboNodeWrapper(rightDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash));
        createMovementCombo(basicLeft3, basicLeft4, new ComboNodeWrapper(rightDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash));
        createMovementCombo(basicLeft4, basic1, new ComboNodeWrapper(rightDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash));

        createMovementCombo(basicRight1, basicRight2, new ComboNodeWrapper(leftDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash));
        createMovementCombo(basicRight2, basicRight3, new ComboNodeWrapper(leftDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash));
        createMovementCombo(basicRight3, basicRight4, new ComboNodeWrapper(leftDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash));
        createMovementCombo(basicRight4, basic1, new ComboNodeWrapper(leftDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash));


        if(tier.value >= 1) {
            createMovementCombo(basic6, basic7, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash));
            createMovementCombo(basic7, basic8, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash));
            createMovementCombo(basic8, basic9, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash));
            createMovementCombo(basic4, basic5, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash));
        }

        if(tier.value >= 2){
            createMovementCombo(basic9, basic10, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash));
            createMovementCombo(basic10, basic11, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash));
            createMovementCombo(basic11, basic12, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash));
            createMovementCombo(basic12, basic13, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash));
            createMovementCombo(basic13, basic14, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash));
        }

        createMovementCombo(leftDodgeAttack, basic2, new ComboNodeWrapper(rightDodgeAttack, airSlash, dash));
        createMovementCombo(rightDodgeAttack, basic2, new ComboNodeWrapper(leftDodgeAttack, airSlash, dash));



        switch (tier.value){
            case 0: basic6.key1(rootDecision);
            break;
            case 1: basic9.key1(rootDecision);
            break;
            case 2: basic13.key1(rootDecision);
            break;
        }
        dash.key1(rootDecision);
        airSlash.key1(rootDecision);

        return registryWorker.build(skillName, ComboBasicAttack::new, ComboBasicAttack
                .createComboBasicAttack()
                .setCombo(root)
                .setMaxProtectTime(22)
                .setMaxPressTime(10)
                .setReserveTime(10)
                .setShouldDrawGui(false));
    }


    private static ComboNode createComboNode(AnimationManager.AnimationAccessor<? extends StaticAnimation> animation){
        ComboNode node = ComboNode.createNode(animation);

        // Defer the addTimeEvent call until animation.get() is non-null
        DEFERRED_SETUP.add(() -> {
            StaticAnimation anim = animation.get();
            if(anim instanceof AttackAnimation attackAnimation){
                for(AttackAnimation.Phase phase : attackAnimation.phases){
                    phase.addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH.get());
                }

            }
            return null;
        });

        return node;
    }
    private static ComboNode createComboNode(AnimationManager.AnimationAccessor<? extends StaticAnimation> animation, float playSpeed){
        ComboNode node = ComboNode.createNode(animation).setPlaySpeed(playSpeed);

        // Defer the addTimeEvent call until animation.get() is non-null
        DEFERRED_SETUP.add(() -> {
            StaticAnimation anim = animation.get();
            if(anim instanceof AttackAnimation attackAnimation){
                for(AttackAnimation.Phase phase : attackAnimation.phases){
                    phase.addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH.get());
                }

            }
            return null;
        });

        return node;
    }
    private static ComboNode createDodgeComboNode(AnimationManager.AnimationAccessor<? extends StaticAnimation> animation, ComboNode followUpCombo){
        ComboNode node = ComboNode.createNode(animation);

        // Defer the addTimeEvent call until animation.get() is non-null
        DEFERRED_SETUP.add(() -> {
            StaticAnimation anim = animation.get();
            float end = anim.getTotalTime() - 0.1F;
            node.addTimeEvent(new TimeStampedEvent(end,
                    ((entityPatch, target, invinciblePlayer) -> {
                        ComboBasicAttack comboAttack = InputManager.getComboBasicSkill();

                        if(comboAttack != null){
                            SkillContainer container = entityPatch.getSkill(FormManager.getCurrentFormSkill(entityPatch.getOriginal()));

                            if(container != null){
                                comboAttack.executeNodeOnServer(container, followUpCombo, 1, 1);
                            }
                        }
                    })));
            node.addBeginEvent(new BaseEvent(
                    ((entityPatch, target, invinciblePlayer) -> {
                       if(entityPatch.getOriginal().level() instanceof ServerLevel serverLevel) {
                           Vec3 pos = entityPatch.getOriginal().position();
                           serverLevel.sendParticles(InvincibleParticles.TRANSPARENT_AFTER_IMAGE.get(), entityPatch.getOriginal().getX(), entityPatch.getOriginal().getY(), entityPatch.getOriginal().getZ(), 1, entityPatch.getOriginal().getId(), 1, 1, entityPatch.getOriginal().getId());
                       }
                    })));

            node.addTimeEvent(new TimeStampedEvent(0.1F,
                    ((entityPatch, target, invinciblePlayer) -> {
                      entityPatch.getOriginal().level().playSound(null, entityPatch.getOriginal().blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1F,1.2F);
                    })));
            return null;
        });

        return node;
    }
    private static ComboNode createAirComboNode(AnimationManager.AnimationAccessor<? extends AttackAnimation> animation){
        ComboNode node = ComboNode.createNode(animation);

        // Defer the addTimeEvent call until animation.get() is non-null
        DEFERRED_SETUP.add(() -> {
            AttackAnimation anim = animation.get();
            float contact = anim.phases[anim.phases.length - 1].contact;
            float recovery = anim.phases[anim.phases.length - 1].end;
            ((AttackAnimation)anim).addProperty(
                    AnimationProperty.AttackAnimationProperty.NO_GRAVITY_TIME,
                    TimePairList.create(new float[]{0.1F, recovery})
            );
            return null;
        });

        return node;
    }
}
