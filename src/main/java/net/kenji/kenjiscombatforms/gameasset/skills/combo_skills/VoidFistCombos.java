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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import org.jline.utils.Log;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.gameasset.WOMSounds;
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
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.damagesource.StunType;

public class VoidFistCombos extends BaseComboBuilder {

    public static Skill buildSkills(String skillName, SkillBuildEvent.ModRegistryWorker registryWorker, FistTier tier) {

        ComboNode root = ComboNode.create();
        ComboNode basicAttack = ComboNode.createNode(Animations.DAGGER_DUAL_AUTO1)
                .setStunTypeModifier(StunType.HOLD)
                .setDamageMultiplier(ValueModifier.multiplier(0.5F))
                .setCanBeInterrupt(false);


        /// Tier 1
        ComboNode basic1 = createComboNode(AnimsAgony.AGONY_AUTO_1);
        ComboNode basic2 = createComboNode(AnimsAgony.AGONY_AUTO_2);
        ComboNode basic3 = createComboNode(AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_1, 1.5F);
        ComboNode basic4 = createComboNode(AnimsEnderblaster.ENDERBLASTER_TWOHAND_AUTO_1, 1.5F);

        ComboNode basicLeft1 = createComboNode(WOMAnimations.KICK_AUTO_2, 1.5F);
        ComboNode basicLeft2 = createComboNode(WOMAnimations.KICK_AUTO_3, 1.75F);
        ComboNode basicLeft3 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1, 1.5F);
        ComboNode basicLeft4 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_3, 1.5F);

        ComboNode basicRight1 = createComboNode(WOMAnimations.KICK_AUTO_1).setPlaySpeed(1.75F);
        ComboNode basicRight2 = createComboNode(WOMAnimations.KICK_AUTO_3).setPlaySpeed(1.75F);
        ComboNode basicRight3 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_1, 1.5F);
        ComboNode basicRight4 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_AUTO_2, 1.5F);

        /// Tier 2
        ComboNode basic5 = createComboNode(AnimsMoonless.MOONLESS_AUTO_1, 1.5F);
        ComboNode basic6 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_DASH, 1.6F);
        ComboNode basic7 = createComboNode(Animations.DAGGER_DUAL_AUTO1);
        ComboNode basic8 = createComboNode(AnimsAgony.AGONY_AUTO_2, 1.5F);
        /// Tier 3
        ComboNode basic9 = createComboNode(AnimsMoonless.MOONLESS_AUTO_2, 1.5F);
        ComboNode basic10 = createComboNode(AnimsMoonless.MOONLESS_AUTO_3, 1.5F);
        ComboNode basic11 = createComboNode(AnimsMoonless.MOONLESS_AUTO_3_VERSO, 1.5F);
        ComboNode basic12 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_SHOOT_3, 1.5F);
        ComboNode basic13 = createComboNode(AnimsMoonless.MOONLESS_AUTO_2);

        ComboNode dash = switch (tier.value) {
            case 0, 1 -> createComboNode(AnimsMoonless.MOONLESS_AUTO_3).addCondition(new SprintingCondition());
            default -> createComboNode(AnimsEnderblaster.ENDERBLASTER_TWOHAND_PISTOLERO).addCondition(new SprintingCondition());
        };
        ComboNode airSlash = switch (tier.value) {
            case 0 -> createAirComboNode(AnimsSatsujin.SATSUJIN_TSUKUYOMI).addCondition(new InAirCondition());
            case 1 -> createAirComboNode(AnimsEnderblaster.ENDERBLASTER_TWOHAND_AIRSHOOT).addCondition(new InAirCondition());
            default -> createComboNode(AnimsEnderblaster.ENDERBLASTER_TWOHAND_AIRSHOOT).addCondition(new InAirCondition());
        };
        ComboNode leftDodgeAttack = createDodgeComboNode(WOMAnimations.ENDERSTEP_LEFT, basicLeft1).addCondition(new LeftCondition());
        leftDodgeAttack.addCondition(new CooldownCounterCondition(leftDodgeAttack, 60));

        ComboNode rightDodgeAttack = createDodgeComboNode(WOMAnimations.ENDERSTEP_RIGHT, basicRight1).addCondition(new RightCondition());
        rightDodgeAttack.addCondition(new CooldownCounterCondition(leftDodgeAttack, 60));

        ComboNode downDodgeAttack = createDodgeComboNode(WOMAnimations.ENDERSTEP_BACKWARD, basicRight1).addCondition(new DownCondition());
        downDodgeAttack.addCondition(new CooldownCounterCondition(downDodgeAttack, 60));

        ComboNode shootAttack = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_SHOOT_1).addCondition(new PressedTimeCondition(6)).addCondition(new FormLevelCondition(FistTier.TIER_2));
        ComboNode shootAttack2 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_SHOOT_2).addCondition(new PressedTimeCondition(6)).addCondition(new FormLevelCondition(FistTier.TIER_3));
        ComboNode shootAttack3 = createComboNode(AnimsEnderblaster.ENDERBLASTER_ONEHAND_SHOOT_3).addCondition(new PressedTimeCondition(6)).addCondition(new FormLevelCondition(FistTier.TIER_3));


        createMovementCombo(root, basicAttack, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));


        ComboNode rootDecision = createMovementCombo(basicAttack, basic1, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));


        createMovementCombo(basic1, basic2, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
        createMovementCombo(basic2, basic3, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
        createMovementCombo(basic3, basic4, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
        createMovementCombo(basic4, basic5, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));

        createMovementCombo(basicLeft1, basicLeft2, new ComboNodeWrapper(rightDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
        createMovementCombo(basicLeft2, basicLeft3, new ComboNodeWrapper(rightDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
        createMovementCombo(basicLeft3, basicLeft4, new ComboNodeWrapper(rightDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
        createMovementCombo(basicLeft4, basic1, new ComboNodeWrapper(rightDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));

        createMovementCombo(basicRight1, basicRight2, new ComboNodeWrapper(leftDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
        createMovementCombo(basicRight2, basicRight3, new ComboNodeWrapper(leftDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
        createMovementCombo(basicRight3, basicRight4, new ComboNodeWrapper(leftDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
        createMovementCombo(basicRight4, basic1, new ComboNodeWrapper(leftDodgeAttack, downDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));


        if(tier.value >= 1) {
            createMovementCombo(basic5, basic6, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
            createMovementCombo(basic6, basic7, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
            createMovementCombo(basic7, basic8, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
            createMovementCombo(basic8, basic9, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));

            createMovementCombo(shootAttack, basic1, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack2));
        }

        if(tier.value >= 2){
            createMovementCombo(basic9, basic10, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
            createMovementCombo(basic10, basic11, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
            createMovementCombo(basic11, basic12, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
            createMovementCombo(basic12, basic13, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack));
            createMovementCombo(shootAttack2, basic1, new ComboNodeWrapper(leftDodgeAttack, rightDodgeAttack, downDodgeAttack, airSlash, dash, shootAttack3));
        }

        createMovementCombo(leftDodgeAttack, basic2, new ComboNodeWrapper(rightDodgeAttack, airSlash, dash, shootAttack));
        createMovementCombo(rightDodgeAttack, basic2, new ComboNodeWrapper(leftDodgeAttack, airSlash, dash, shootAttack));



        switch (tier.value){
            case 0: basic5.key1(rootDecision);
            break;
            case 1: basic9.key1(rootDecision);
            break;
            case 2: basic13.key1(rootDecision);
            break;
        }


        return registryWorker.build(skillName, ComboBasicAttack::new, ComboBasicAttack
                .createComboBasicAttack()
                .setCombo(root)
                .setMaxProtectTime(22)
                .setMaxPressTime(6)
                .setReserveTime(16)
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
                           Log.info("Logging Particles!");
                       }
                    })));

            node.addTimeEvent(new TimeStampedEvent(0.1F,
                    ((entityPatch, target, invinciblePlayer) -> {
                      entityPatch.getOriginal().level().playSound(null, entityPatch.getOriginal().blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F,1.5F);
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
