package net.kenji.kenjiscombatforms.mixins;

import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.gameasset.CombatFormWeaponCategory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jline.utils.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import reascer.wom.gameasset.colliders.WOMWeaponColliders;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

@Mixin(value = AttackAnimation.class, remap = false, priority = 1)
public class MixinAttackAnimation {



    @Inject(method = "getHitSound", at = @At("RETURN"), cancellable = true, remap = false)
    public void onGetHitSound(LivingEntityPatch<?> entitypatch, AttackAnimation.Phase phase, CallbackInfoReturnable<SoundEvent> cir) {
        AttackAnimation self = (AttackAnimation) (Object)this;
        if(!(entitypatch instanceof PlayerPatch<?> patch))return;
        ItemStack trueStack = FormManager.getTrueStackOr(patch.getOriginal(), ItemStack.EMPTY);
        if(!FormManager.isHeldCategoryValid(patch.getOriginal(), trueStack)) return;
        CapabilityItem capItem = EpicFightCapabilities.getItemStackCapability(trueStack);
        if(phase.getProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND).isPresent()) {
            if (capItem == null || !(capItem.getWeaponCategory() instanceof CombatFormWeaponCategory)) {
                if (phase.getProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND).get() == EpicFightSounds.BLUNT_HIT.get())
                    return;
                if (phase.getProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND).get() == EpicFightSounds.BLUNT_HIT_HARD.get())
                    return;
            }
        }
        if(capItem != null && capItem.getWeaponCategory() instanceof CombatFormWeaponCategory combatFormCategory){
            HumanoidArmature biped = Armatures.BIPED.get();
            boolean shouldCancelHitSound = false;
            for(AttackAnimation.JointColliderPair pair : phase.getColliders()) {
                if(pair.getFirst() == biped.legR || pair.getFirst() == biped.legL ||
                        pair.getSecond() == WOMWeaponColliders.KICK ||
                        pair.getSecond() == WOMWeaponColliders.KICK_HUGE ||
                        pair.getSecond() == WOMWeaponColliders.KNEE) {
                    shouldCancelHitSound = true;
                    break;
                }
            }
            if(!shouldCancelHitSound) {
                cir.setReturnValue(combatFormCategory.hitSound.get().get());
                return;
            }
        }
        else{
            ItemStack offHandStack = entitypatch.getOriginal().getOffhandItem();

            CapabilityItem offHandCap = EpicFightCapabilities.getItemStackCapability(offHandStack);

            if(offHandCap != null && offHandCap.getWeaponCategory() instanceof CombatFormWeaponCategory combatFormCategory){
                HumanoidArmature biped = Armatures.BIPED.get();
                boolean shouldAddHitSound = false;
                for(AttackAnimation.JointColliderPair pair : phase.getColliders()) {
                    if(pair.getFirst() == biped.toolL || pair.getFirst() == biped.handL) {
                        shouldAddHitSound = true;
                        break;
                    }
                }
                if(shouldAddHitSound) {
                    cir.setReturnValue(combatFormCategory.hitSound.get().get());
                    return;
                }
            }
        }

        SoundEvent sound = EpicFightCapabilities.getItemStackCapability(trueStack).getHitSound();

        SoundEvent finalHitSound = sound != null ? sound : EpicFightSounds.BLUNT_HIT.get();
        cir.setReturnValue(finalHitSound);
    }
    @Inject(method = "spawnHitParticle", at = @At("HEAD"), cancellable = true, remap = false)
    public void onSpawnHitParticle(ServerLevel world, LivingEntityPatch<?> attacker, Entity hit, AttackAnimation.Phase phase, CallbackInfo ci) {
        AttackAnimation self = (AttackAnimation) (Object)this;
        if(!(attacker instanceof PlayerPatch<?> patch))return;
        ItemStack trueStack = FormManager.getTrueStackOr(patch.getOriginal(), ItemStack.EMPTY);

        if(!FormManager.isHeldCategoryValid(patch.getOriginal(), trueStack)) return;

        ci.cancel();
        HitParticleType particle = EpicFightParticles.HIT_BLUNT.get();
        CapabilityItem capItem = EpicFightCapabilities.getItemStackCapability(trueStack);

        HitParticleType finalParticle = particle;
        if(capItem != null && capItem.getWeaponCategory() instanceof CombatFormWeaponCategory combatFormCategory){
            HumanoidArmature biped = Armatures.BIPED.get();
            boolean shouldCancelHitParticle = false;
            for(AttackAnimation.JointColliderPair pair : phase.getColliders()) {
                if(pair.getFirst() == biped.legR || pair.getFirst() == biped.legL ||
                        pair.getSecond() == WOMWeaponColliders.KICK ||
                        pair.getSecond() == WOMWeaponColliders.KICK_HUGE ||
                        pair.getSecond() == WOMWeaponColliders.KNEE) {
                    shouldCancelHitParticle = true;
                    break;
                }
            }
            if(!shouldCancelHitParticle) {
                if (combatFormCategory.hitParticle.get() != null) {
                    finalParticle = combatFormCategory.hitParticle.get().get();
                }
            }
        }
        else{
            ItemStack offHandStack = patch.getOriginal().getOffhandItem();

            CapabilityItem offHandCap = EpicFightCapabilities.getItemStackCapability(offHandStack);

            if(offHandCap != null && offHandCap.getWeaponCategory() instanceof CombatFormWeaponCategory combatFormCategory){
                HumanoidArmature biped = Armatures.BIPED.get();
                boolean shouldAddHitSound = false;
                for(AttackAnimation.JointColliderPair pair : phase.getColliders()) {
                    if(pair.getFirst() == biped.toolL || pair.getFirst() == biped.handL) {
                        shouldAddHitSound = true;
                        break;
                    }
                }
                if(shouldAddHitSound) {
                    finalParticle = combatFormCategory.hitParticle.get().get();
                }
            }
        }
        finalParticle.spawnParticleWithArgument(world, null, null, hit, attacker.getOriginal());
    }
}