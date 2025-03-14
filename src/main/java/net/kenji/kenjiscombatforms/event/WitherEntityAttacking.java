package net.kenji.kenjiscombatforms.event;

import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.WitherPlayerEntity;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import reascer.wom.animation.attacks.AntitheusShootAttackAnimation;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

import java.util.List;

public class WitherEntityAttacking {

    private static final WitherEntityAttacking INSTANCE = new WitherEntityAttacking();

    private int currentState = 0;
    private long lastPressTime = 0;
    private static final long PRESS_COOLDOWN = 200;

    private final EpicFightMod epicFightMod = EpicFightMod.getInstance();
    private final AnimationManager animationManager = AnimationManager.getInstance();


    private final StaticAnimation attackAnimation1 = animationManager.refreshAnimation(WOMAnimations.ANTITHEUS_ASCENDED_AUTO_2);
    private final StaticAnimation attackAnimation2 = animationManager.refreshAnimation(WOMAnimations.ANTITHEUS_ASCENDED_AUTO_2);
    private final StaticAnimation attackAnimation3 = animationManager.refreshAnimation(WOMAnimations.MOONLESS_REWINDER);
    private final StaticAnimation attackAnimation4 = animationManager.refreshAnimation(WOMAnimations.MOONLESS_BYPASS);
    private final AntitheusShootAttackAnimation attackShoot = (AntitheusShootAttackAnimation)animationManager.refreshAnimation(WOMAnimations.ANTITHEUS_SHOOT);


    public static WitherEntityAttacking getInstance(){
        return INSTANCE;
    }

    WitherPlayerDataSets.WitherFormPlayerData getOrCreateWitherData(Player player){
        return WitherPlayerDataSets.getInstance().getOrCreateWitherFormPlayerData(player);
    }




    public void performAttack(Player player) {
        WitherPlayerDataSets.WitherFormPlayerData data = getOrCreateWitherData(player);
        data.witherEntity.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
            if (cap instanceof LivingEntityPatch<?> entityPatch) {


                DamageSources damageSources = data.witherEntity.level().damageSources();
                DamageSource vanillaDamageSource = damageSources.mobAttack((LivingEntity) data.witherEntity);
                EpicFightDamageSource damageSource = new EpicFightDamageSource(vanillaDamageSource);

                Entity target = findClosestEntity(data.witherEntity, 5);

                InteractionHand hand = InteractionHand.MAIN_HAND;


                if (!isCurrentlyPlayingAnimation(entityPatch)) {
                    playAlternatingAnimations(entityPatch, attackAnimation1, attackAnimation2, attackAnimation3, attackAnimation4, attackShoot);

                    if (target != null && !(target instanceof WitherPlayerEntity)) {
                        entityPatch.attack(damageSource, target, hand);

                    }
                }
            }
        });
    }


    public void playAlternatingAnimations(LivingEntityPatch<?> livingEntityPatch, StaticAnimation firstCombo, StaticAnimation secondCombo, StaticAnimation thirdCombo, StaticAnimation fourthCombo, AntitheusShootAttackAnimation attackShoot) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPressTime > PRESS_COOLDOWN) {
            currentState++;
            if (currentState == 1) {
                livingEntityPatch.playAnimationSynchronized(firstCombo, -0.2f);
            } else if (currentState == 2) {
                livingEntityPatch.playAnimationSynchronized(secondCombo, -0.2f);
            }else if (currentState == 3) {
                livingEntityPatch.playAnimationSynchronized(thirdCombo, -0.22f);
            }else if (currentState == 4) {
                livingEntityPatch.playAnimationSynchronized(fourthCombo, 0.22f);
            }else if(currentState == 5){
                livingEntityPatch.playAnimationSynchronized(attackShoot, -0.15f);
                currentState = 0;
            }
            lastPressTime = currentTime;
        }
    }

    private boolean isAnimationPlaying(Animator animator, StaticAnimation animation) {
        AnimationPlayer player = animator.getPlayerFor(animation.getRealAnimation());
        return player.isEnd() || player.isEmpty();
    }

    private boolean isCurrentlyPlayingAnimation(LivingEntityPatch entityPatch) {
        Animator animator = entityPatch.getAnimator();
        return !isAnimationPlaying(animator, attackShoot) ||
                !isAnimationPlaying(animator, attackAnimation1) ||
                !isAnimationPlaying(animator, attackAnimation2) ||
                !isAnimationPlaying(animator, attackAnimation3);
    }


    public Entity findClosestEntity(Entity source, double range) {
        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE;

        List<Entity> nearbyEntities = source.level().getEntities(source, source.getBoundingBox().inflate(range));

        for (Entity entity : nearbyEntities) {
            if (entity == source) continue; // Skip the source entity
            double distance = source.distanceToSqr(entity);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestEntity = entity;
            }
        }
        return closestEntity;
    }

}