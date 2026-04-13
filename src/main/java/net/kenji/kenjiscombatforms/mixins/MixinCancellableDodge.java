package net.kenji.kenjiscombatforms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import reascer.wom.animation.dodges.CancelableDodgeAnimation;
import yesman.epicfight.api.animation.AnimationManager;

@Mixin(value = CancelableDodgeAnimation.class, remap = false)
public class MixinCancellableDodge {
    @ModifyVariable(
            method = "<init>(FFFFFLyesman/epicfight/api/animation/AnimationManager$AnimationAccessor;FFLyesman/epicfight/api/asset/AssetAccessor;)V",
            at = @At("HEAD"),
            name = "arg2", argsOnly = true)
    private static float modifyShadowDash(float value){
            return 0.05F;
    } @ModifyVariable(
            method = "<init>(FFFFFLyesman/epicfight/api/animation/AnimationManager$AnimationAccessor;FFLyesman/epicfight/api/asset/AssetAccessor;)V",
            at = @At("HEAD"),
            name = "arg3", argsOnly = true)
    private static float modifyShadowDash2(float value){
            return 0.05F;
    }

}
