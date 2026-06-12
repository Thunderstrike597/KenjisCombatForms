package net.kenji.kenjiscombatforms.mixins;

import com.google.common.collect.Maps;
import net.kenji.kenjiscombatforms.api.handlers.ControlHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.*;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = CapabilityItem.class, remap = false, priority = 800)
public class MixinCapabilityItem {


    @Inject(method = "getLivingMotionModifier", at = @At("HEAD"), cancellable = true, remap = false)
    private void getCustomLivingMotion(LivingEntityPatch<?> patch, InteractionHand hand, CallbackInfoReturnable<Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>>> cir) {
        CapabilityItem self = (CapabilityItem) (Object)this;
        if(self instanceof ShieldCapability) return;
        if(patch instanceof PlayerPatch<?> playerPatch) {
            if (!playerPatch.isEpicFightMode()) return;

            ItemStack formItem = FormManager.getCurrentFormItem(playerPatch.getOriginal());
            if (formItem == null) return;
            CapabilityItem capItem = playerPatch.getHoldingItemCapability(InteractionHand.MAIN_HAND);
            WeaponCategory category = capItem.getWeaponCategory();
            CapabilityItem formCapItem = EpicFightCapabilities.getItemStackCapability(formItem);

            if (EpicFightCombatFormsCommon.ALTER_FIST_LIVING_MOTION.get()) {
                if (FormManager.isHeldCategoryValid(playerPatch.getOriginal(), FormManager.getTrueStackOr(playerPatch.getOriginal(), playerPatch.getOriginal().getMainHandItem()))) {
                    boolean isToggled = ControlHandler.toggleHandCombatMap.getOrDefault(patch.getOriginal().getUUID(), true);
                    if(formCapItem instanceof WeaponCapability weaponCapability) {
                       if (isToggled) {
                           cir.setReturnValue(kenjiscombatforms$getLivingMotions(playerPatch, weaponCapability));
                       }
                   }
                }
            }
        }
    }
    @Inject(method = "getLivingMotionModifier", at = @At("RETURN"), cancellable = true, remap = false)
    private void geShieldCustomLivingMotion(LivingEntityPatch<?> patch, InteractionHand hand, CallbackInfoReturnable<Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>>> cir) {
        CapabilityItem self = (CapabilityItem) (Object)this;
        if(patch instanceof PlayerPatch<?> playerPatch) {
            if (!playerPatch.isEpicFightMode())
                return;
            if(!(patch.getOriginal().getOffhandItem().getItem() instanceof ShieldItem)) return;

            Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> originalMap = cir.getReturnValue();


            Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> mutableMap = new HashMap<>(originalMap);
            if(mutableMap.get(LivingMotions.BLOCK_SHIELD) == null || mutableMap.get(LivingMotions.BLOCK_SHIELD).get() == Animations.EMPTY_ANIMATION) {
                mutableMap.put(LivingMotions.BLOCK_SHIELD, Animations.BIPED_BLOCK);
            }

            cir.setReturnValue(mutableMap);
        }
    }
    @Unique
    private Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> kenjiscombatforms$getLivingMotions(PlayerPatch<?> playerPatch, WeaponCapability weaponCapability) {
        WeaponCapabilityAccessor accessor = (WeaponCapabilityAccessor) weaponCapability;

        Map<Style, Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>>> allMotions = accessor.accessLivingMotions();

        Style style = weaponCapability.getStyle(playerPatch);

        // DEBUG (you should log this)
        //System.out.println("Style: " + style);

        Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> styleMotions = allMotions.get(style);
        Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> commonMotions = allMotions.get(CapabilityItem.Styles.COMMON);

        // Always create a NEW map (avoid mutating internal data)
        Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> result = Maps.newHashMap();

        if (commonMotions != null) result.putAll(commonMotions);
        if (styleMotions != null) result.putAll(styleMotions);

        return result;
    }
}