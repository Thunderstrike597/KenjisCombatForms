package net.kenji.kenjiscombatforms.event;

import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.EnderEntity;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

import java.util.List;

public class EnderEntityAttacking {

    private static final EnderEntityAttacking INSTANCE = new EnderEntityAttacking();

    private int currentState = 0;
    private long lastPressTime = 0;
    private static final long PRESS_COOLDOWN = 200;

    private final EpicFightMod epicFightMod = EpicFightMod.getInstance();
    private final AnimationManager animationManager = AnimationManager.getInstance();


    private final StaticAnimation attackAnimation = animationManager.refreshAnimation(WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_2);
    private final StaticAnimation attackAnimation2 = animationManager.refreshAnimation(WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_3);
    private final StaticAnimation attackAnimation3 = animationManager.refreshAnimation(WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_4);
    private final StaticAnimation attackAnimation4 = animationManager.refreshAnimation(WOMAnimations.ENDERBLASTER_ONEHAND_AIRSHOOT);


    public static EnderEntityAttacking getInstance(){
        return INSTANCE;
    }

    EnderPlayerDataSets.EnderFormPlayerData getOrCreateEnderData(Player player){
        return EnderPlayerDataSets.getInstance().getOrCreateEnderFormPlayerData(player);
    }


    public void performAttack(Player player) {
        EnderPlayerDataSets.EnderFormPlayerData data = getOrCreateEnderData(player);

        data.enderEntity.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
            if (cap instanceof LivingEntityPatch<?> entityPatch) {
                if(entityPatch.getOriginal() instanceof EnderEntity entity) {
                  if(entity.getLastHurtByMob() == player){
                      player.skipAttackInteraction(data.enderEntity);
                  }

                    DamageSources damageSources = data.enderEntity.level().damageSources();
                    DamageSource vanillaDamageSource = damageSources.mobAttack((LivingEntity) data.enderEntity);
                    EpicFightDamageSource damageSource = new EpicFightDamageSource(vanillaDamageSource);

                    Entity target = findClosestEntity(data.enderEntity, 5);

                    InteractionHand hand = InteractionHand.MAIN_HAND;


                    if (!isCurrentlyPlayingAnimation(entityPatch)) {
                        playAlternatingAnimations(entityPatch, attackAnimation, attackAnimation2, attackAnimation3, attackAnimation4);

                        if (target != null && !(target instanceof EnderEntity)) {
                            entityPatch.attack(damageSource, target, hand);
                        }
                    }
                }
            }
        });
    }

    public void playAlternatingAnimations(LivingEntityPatch<?> livingEntityPatch, StaticAnimation firstCombo, StaticAnimation secondCombo, StaticAnimation thirdCombo, StaticAnimation fourthCombo) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPressTime > PRESS_COOLDOWN) {
            currentState++;
            if (currentState == 1) {
                livingEntityPatch.playAnimationSynchronized(firstCombo, 0);
            } else if (currentState == 2) {
                livingEntityPatch.playAnimationSynchronized(secondCombo, 0);
            }else if (currentState == 3) {
                livingEntityPatch.playAnimationSynchronized(thirdCombo, 0);
            }else if (currentState == 4) {
                livingEntityPatch.playAnimationSynchronized(fourthCombo, 0);
                currentState = 0;
            }
            lastPressTime = currentTime;
        }
    }

    private boolean isCurrentlyPlayingAnimation(LivingEntityPatch entityPatch){
        Animator animator = entityPatch.getAnimator();
        return !animator.getPlayerFor(attackAnimation.getRealAnimation()).isEmpty() &&
                !animator.getPlayerFor(attackAnimation2.getRealAnimation()).isEmpty() &&
                !animator.getPlayerFor(attackAnimation3.getRealAnimation()).isEmpty() &&
                !animator.getPlayerFor(attackAnimation4.getRealAnimation()).isEmpty();
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