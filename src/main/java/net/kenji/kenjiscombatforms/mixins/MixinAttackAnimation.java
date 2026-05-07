package net.kenji.kenjiscombatforms.mixins;

import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.gameasset.CombatFormWeaponCategories;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.gameasset.colliders.WOMWeaponColliders;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlot;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.Optional;
import java.util.function.BiFunction;

@Mixin(value = AttackAnimation.class, remap = false)
public class MixinAttackAnimation {

    @Inject(method = "getHitSound", at = @At("RETURN"), cancellable = true, remap = false)
    public void onGetHitSound(LivingEntityPatch<?> entitypatch, AttackAnimation.Phase phase, CallbackInfoReturnable<SoundEvent> cir) {
        AttackAnimation self = (AttackAnimation) (Object)this;
        if(!(entitypatch instanceof PlayerPatch<?> patch))return;
        if(!FormManager.isHeldCategoryValid(patch.getOriginal(), patch.getOriginal().getInventory().getSelected())) return;
        CapabilityItem capItem = EpicFightCapabilities.getItemStackCapability(FormManager.trueStackMap.getOrDefault(patch.getOriginal().getUUID(), ItemStack.EMPTY));

        if(phase.getProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND).isPresent()) {
            if (capItem == null || !(capItem.getWeaponCategory() instanceof CombatFormWeaponCategories)) {
                if (phase.getProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND).get() == EpicFightSounds.BLUNT_HIT.get())
                    return;
                if (phase.getProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND).get() == EpicFightSounds.BLUNT_HIT_HARD.get())
                    return;
            }
        }
        if(capItem != null && capItem.getWeaponCategory() instanceof CombatFormWeaponCategories combatFormCategory){
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
        SoundEvent sound = EpicFightCapabilities.getItemStackCapability(patch.getOriginal().getInventory().getSelected()).getHitSound();

        SoundEvent finalHitSound = sound != null ? sound : EpicFightSounds.BLUNT_HIT.get();
        cir.setReturnValue(finalHitSound);
    }
    @Inject(method = "spawnHitParticle", at = @At("HEAD"), cancellable = true, remap = false)
    public void onSpawnHitParticle(ServerLevel world, LivingEntityPatch<?> attacker, Entity hit, AttackAnimation.Phase phase, CallbackInfo ci) {
        AttackAnimation self = (AttackAnimation) (Object)this;
        if(!(attacker instanceof PlayerPatch<?> patch))return;
        if(!FormManager.isHeldCategoryValid(patch.getOriginal(), patch.getOriginal().getInventory().getSelected())) return;

        ci.cancel();
        HitParticleType particle = EpicFightParticles.HIT_BLUNT.get();
        particle.spawnParticleWithArgument(world, null, null, hit, attacker.getOriginal());

    }
}