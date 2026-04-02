package net.kenji.kenjiscombatforms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

import java.util.Map;

@Mixin(value = WeaponCapability.class, remap = false)
public interface WeaponCapabilityAccessor {

    @Accessor("livingMotionModifiers")
    Map<Style, Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>>> accessLivingMotions();
}
